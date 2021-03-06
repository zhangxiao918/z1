
package org.bluestome.satelliteweather.biz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.bluestome.android.utils.DateUtils;
import com.bluestome.android.utils.HttpClientUtils;

import org.bluestome.satelliteweather.MainActivity;
import org.bluestome.satelliteweather.MainApp;
import org.bluestome.satelliteweather.R;
import org.bluestome.satelliteweather.common.Constants;
import org.bluestome.satelliteweather.db.DaoFactory;
import org.bluestome.satelliteweather.db.dao.FY2DAO;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 简单卫星云图业务类
 * 
 * @author bluestome
 */
public class SatelliteWeatherSimpleBiz {

    private static String TAG = SatelliteWeatherSimpleBiz.class.getSimpleName();

    Handler mHandler = null;

    /**
     * 带参数的构造函数
     * 
     * @param mHandler 外部传入的处理类，用于同步UI
     */
    public SatelliteWeatherSimpleBiz(Handler mHandler) {
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    /**
     * 获取云端的更新列表
     * 
     * @throws Exception
     */
    public void updateList(final String lastModifyTime) throws Exception {
        MainApp.i().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                DaoFactory factory = DaoFactory.getInstance(MainApp.i());
                List<String> urlList = new ArrayList<String>();
                try {
                    byte[] body = HttpClientUtils.getBody(
                            Constants.SATELINE_CLOUD_URL, "If-Modified-Since",
                            new Date().toGMTString());
                    if (null == body || body.length == 0) {
                        return;
                    }
                    Parser parser = new Parser();
                    String html = new String(body, "GB2312");
                    parser.setInputHTML(html);
                    parser.setEncoding("GB2312");

                    NodeFilter fileter = new NodeClassFilter(CompositeTag.class);
                    NodeList list = parser.extractAllNodesThatMatch(fileter)
                            .extractAllNodesThatMatch(
                                    new HasAttributeFilter("id", "mycarousel")); // id

                    if (null != list && list.size() > 0) {
                        CompositeTag div = (CompositeTag) list.elementAt(0);
                        parser = new Parser();
                        parser.setInputHTML(div.toHtml());
                        NodeFilter linkFilter = new NodeClassFilter(
                                LinkTag.class);
                        NodeList linkList = parser
                                .extractAllNodesThatMatch(linkFilter);
                        if (linkList != null && linkList.size() > 0) {
                            for (int i = 0; i < linkList.size(); i++) {
                                LinkTag link = (LinkTag) linkList.elementAt(i);
                                String str = link.getLink()
                                        .replace("view_text_img(", "")
                                        .replace(")", "").replace("'", "");
                                if (null != str && str.length() > 0) {
                                    final String[] tmps = str.split(",");

                                    FY2DAO dao = factory.getFY2DAO();
                                    String name = analysisURL(tmps[0]);
                                    if (!dao.checkNImage(name)) {
                                        // 数据库操作
                                        String date = analysisURL2(name);
                                        String spath = analysisURL3(tmps[0]);
                                        String bpath = analysisURL3(tmps[1]);
                                        int id = dao.insert(name, spath, bpath,
                                                date);
                                        if (id > 0) {
                                            Log.d(TAG, "\tzhang 添加数据成功");
                                        }
                                    }
                                    urlList.add(0, tmps[0]);
                                    MainApp.i().getExecutorService()
                                            .execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    downloadImage(Constants.PREFIX_SATELINE_CLOUD_IMG_URL
                                                            + tmps[0]);
                                                }
                                            });
                                }

                            }
                            MainApp.i().setLastModifyTime(lastModifyTime);
                        }
                    }
                    if (null != parser)
                        parser = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 下载图片
     * 
     * @param url
     * @return
     */
    private synchronized String downloadImage(String url) {
        URL cURL = null;
        HttpURLConnection connection = null;
        InputStream in = null;
        File file = null;
        try {
            cURL = new URL(url);
            connection = (HttpURLConnection) cURL.openConnection();
            // 获取输出流
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(15 * 1000);
            connection.connect();
            int code = connection.getResponseCode();
            switch (code) {
                case 200:
                    String name = analysisURL(url);
                    String date = analysisURL2(name);
                    file = new File(Constants.SATELINE_CLOUD_IMAGE_PATH
                            + File.separator + date + File.separator + name);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    if (file.exists()) {
                        return name;
                    }
                    in = connection.getInputStream();
                    byte[] buffer = new byte[2 * 1024];
                    OutputStream byteBuffer = new FileOutputStream(file, false);
                    int ch;
                    while ((ch = in.read(buffer)) != -1) {
                        byteBuffer.write(buffer, 0, ch);
                        byteBuffer.flush();
                    }
                    byteBuffer.close();
                    return name;
                default:
                    break;
            }
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
            if (null != connection) {
                connection.disconnect();
            }
        }
        return null;
    }

    /**
     * 分析URL,获取图片文件名,将文件名中无需要的文字去掉， eg:
     * SEVP_NSMC_WXCL_ASC_E99_ACHN_LNO_PY_20130315010000000.JPG?v=1363312970836
     * ?v=1363312970836 这段数据不需要，要截取掉
     * 
     * @param url
     * @return
     */
    private String analysisURL(String url) {
        int s = url.lastIndexOf("/");
        String name = url.substring(s + 1, url.length());
        if (null == name || name.equals("")) {
            name = String.valueOf(System.currentTimeMillis());
        }
        if (name.indexOf("?") != -1) {
            int pos = name.indexOf("?");
            name = name.substring(0, pos);
        }
        return name;
    }

    /**
     * 从文件名中分析出时间信息
     */
    private String analysisURL2(String name) {
        String date = com.bluestome.android.utils.DateUtils.formatDate(
                new Date(),
                com.bluestome.android.utils.DateUtils.DEFAULT_PATTERN);
        if (null != name && name.length() > 0 && !name.equals("")) {
            String[] tmps = name.substring(0, name.lastIndexOf(".")).split("_");
            if (tmps.length > 8) {
                date = tmps[8];
            }
        }
        if (null != date && date.length() > 8 && !date.equals("")) {
            date = date.substring(0, 8);
        }
        return date;
    }

    /**
     * 分析URL,获取路径
     */
    private String analysisURL3(String url) {
        int s = url.lastIndexOf("/");
        String name = url.substring(0, s + 1);
        if (null == name || name.equals("")) {
            name = "/product/2012/201212/"
                    + DateUtils.getShortNow().replace("-", "").trim()
                    + "/WXCL/";
        }
        return name;
    }

    /**
     * 通知
     * 
     * @param content
     */
    public void notifyNS(final String content) {
        MainApp.i().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                String ns = Context.NOTIFICATION_SERVICE;
                NotificationManager mNotificationManager = (NotificationManager) MainApp
                        .i().getSystemService(ns);
                int icon = R.drawable.ic_launcher;
                CharSequence tickerText = "卫星云图";
                long when = System.currentTimeMillis();
                Notification notification = new Notification(icon, tickerText,
                        when);
                // 点击后自动清除通知栏的当前通知
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.defaults |= Notification.DEFAULT_SOUND;
                Context context = MainApp.i();
                CharSequence contentTitle = "同步数据";
                Intent notificationIntent = new Intent(MainApp.i(),
                        MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(
                        MainApp.i(), 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setLatestEventInfo(context, contentTitle, content,
                        contentIntent);
                mNotificationManager.notify(Constants.NOTIFY_ID, notification);
            }
        });
    }

}
