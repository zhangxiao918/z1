
package com.bluestome.asynctask.task;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bluestome.asynctask.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName: UrlLoadTask
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-19 上午11:37:21
 */
public class UrlLoadTask extends AsyncTask<String, Float, Float> {

    private Button download;
    private TextView tv;
    private Activity activity;
    private ProgressBar pb;
    private OutputStream baos = null;
    private final String savePath;
    private File file = null;
    private boolean isShow = false;

    public UrlLoadTask(Activity activity, Button download, TextView tv,
            ProgressBar pb, String savePath) {
        this.activity = activity;
        this.tv = tv;
        this.download = download;
        this.pb = pb;
        this.savePath = savePath;
    }

    @Override
    protected void onPreExecute() {
        file = new File(savePath + File.separator + System.currentTimeMillis() + ".tmp");
        if (!file.getParentFile().exists()) {
            System.out.println("\t 文件父目录不存在，创建父目录!");
            file.getParentFile().mkdirs();
        }

        if (file.exists()) {
            System.out.println("\t 文件已存在,删除该文件[" + file.getAbsolutePath() + "]");
            file.delete();
        }
    }

    @Override
    protected Float doInBackground(String... params) {
        Float result = 0.0F;
        URL url = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setConnectTimeout(10 * 1000);
            connection
                    .setRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                isShow = isImage(connection.getContentType());
                // 追加文件
                baos = new FileOutputStream(file, true);
                is = connection.getInputStream();
                long start = System.currentTimeMillis();
                int total = connection.getContentLength();
                int c;
                byte[] tmp = new byte[2048];
                pb.setMax(total);
                while ((c = is.read(tmp)) != -1) {
                    baos.write(tmp, 0, c);
                    float size = file.length();
                    result = (float) file.length();
                    long now = (System.currentTimeMillis() - start);
                    pb.setProgress((int) size);
                    publishProgress(size, (float) total, (float) now);
                    baos.flush();
                }
                baos.close();
            } else {
                tv.setText(connection.getResponseCode());
            }
        } catch (Exception e) {
            tv.setText("下载文件失败:" + e.getMessage());
        } finally {
            if (null != connection)
                connection.disconnect();
        }
        return result;
    }

    /**
     * 第一个参数 当前下载大小 第二个参数 文件总大小 第三个参数 当前消耗时间
     */
    @Override
    protected void onProgressUpdate(Float... progress) {
        float size = progress[0];
        float now = progress[2];

        float size2 = size / 1024;
        float times = now / 1000;
        float show = size2 / times;

        // 转成百分比
        java.text.NumberFormat percentFormat = java.text.NumberFormat
                .getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
        Float pres = progress[0] / progress[1];
        // activity.setTitle("正在执行..."+percentFormat.format(pres));
        tv.setText(round(show, 2, BigDecimal.ROUND_UP) + "kb/s\t"
                + percentFormat.format(pres));
    }

    @Override
    protected void onPostExecute(Float result) {
        if (result > 0) {
            tv.setText(tv.getText() + ".....下载成功!");
            download.setEnabled(true);
            Toast.makeText(activity, "下载文件成功", Toast.LENGTH_SHORT).show();
            if (isShow) {
                if (null != file && file.length() > 0) {
                    // byte[] body = baos.toByteArray();
                    Bitmap bitMap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    ImageView imageView =
                            ((ImageView) activity.findViewById(R.id.image_view));
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitMap);
                }
            }
        } else {
            tv.setText(tv.getText() + ".....失败!");
            download.setEnabled(true);
            pb.setVisibility(View.GONE);
            Toast.makeText(activity, "下载文件失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 对double数据进行取精度.
     * <p>
     * For example: <br>
     * double value = 100.345678; <br>
     * double ret = round(value,4,BigDecimal.ROUND_HALF_UP); <br>
     * ret为100.3457 <br>
     * 
     * @param value double数据.
     * @param scale 精度位数(保留的小数位数).
     * @param roundingMode 精度取值方式.
     * @return 精度计算后的数据.
     */
    private double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    /**
     * 判断Content-type是否为图片类型
     * 
     * @param contentType
     * @return
     */
    private boolean isImage(String contentType) {
        System.out.println("\t 元素类型:" + contentType);
        boolean b = false;
        if (null != contentType && !contentType.equals("")) {
            if (contentType.toLowerCase().startsWith("image/")) {
                b = true;
            }
        }
        return b;
    }

    /**
     * @return the download
     */
    public Button getDownload() {
        return download;
    }

    /**
     * @param download the download to set
     */
    public void setDownload(Button download) {
        this.download = download;
    }

    /**
     * @return the tv
     */
    public TextView getTv() {
        return tv;
    }

    /**
     * @param tv the tv to set
     */
    public void setTv(TextView tv) {
        this.tv = tv;
    }

    /**
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * @return the pb
     */
    public ProgressBar getPb() {
        return pb;
    }

    /**
     * @param pb the pb to set
     */
    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public OutputStream getBaos() {
        return baos;
    }

    public void setBaos(ByteArrayOutputStream baos) {
        this.baos = baos;
    }

    public String getSavePath() {
        return savePath;
    }

}
