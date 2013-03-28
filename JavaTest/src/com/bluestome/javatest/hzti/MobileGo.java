
package com.bluestome.javatest.hzti;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MobileGo
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-3-27 下午4:08:00
 */
public class MobileGo {

    static StringBuilder sb = null;

    static synchronized void request(String site) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(site);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Connection", "keep-alive");
            connection.addRequestProperty("Cache-Control", "no-cache");
            if (null != sb)
                connection.addRequestProperty("Cookie", sb.toString());
            connection
                    .addRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            connection.setRequestMethod("GET");
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // 服务端响应成功
                Map<String, List<String>> map = connection.getHeaderFields();
                if (null == sb) {
                    sb = new StringBuilder();
                    Iterator<String> it = map.keySet().iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (null != key && !key.equals("")
                                && key.equals("Set-Cookie")) {
                            List<String> values = map.get(key);
                            for (String value : values) {
                                sb.append(parserValue(value)).append(";");
                            }

                        }
                    }
                    if (sb.toString().endsWith(";")) {
                        String tmp = sb.toString();
                        tmp = tmp.substring(0, tmp.lastIndexOf(";"));
                        sb = null;
                        sb = new StringBuilder(tmp);
                    }
                }
                // 获取COOKIE
                if (null != sb && !sb.toString().equals("")) {
                    String tmp = sb.toString();
                    String[] ts = tmp.split(";");
                    sb = null;
                    sb = new StringBuilder();
                    sb.append(ts[1]).append("; ").append(ts[0]);
                }
                System.out.println("sb:" + sb.toString());
                try {
                    InputStream is = connection.getInputStream();
                    byte[] buffer = new byte[2048];
                    int c;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((c = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, c);
                        baos.flush();
                    }
                    baos.close();
                    byte[] body = baos.toByteArray();
                    if (null != body && body.length > 0 && (null == QueryTools.getCONNTENT_BODY())) {
                        QueryTools.setCONTENT_BODY(body);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection)
                connection.disconnect();
        }
    }

    static String parserValue(String value) {
        String[] tmp = value.split(";");
        if (null != tmp && tmp.length > 0) {
            value = tmp[0];
        }
        return value;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        request(Constants.URL);
        QueryTools.doQuery(sb.toString(), Constants.URL, "小型汽车",
                "浙A249NT", "005953", "gkwe");

    }

}
