
package com.bluestome;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class J {

    final static int MAX_SIZE = 2048;

    public static void main(String[] args) {
        if (null == args || args.length == 0) {
            System.out.println("请输入需要下载的文件地址!");
            return;
        }
        String web = args[0];
        URL url = null;
        HttpURLConnection connection = null;
        ByteArrayOutputStream baos = null;
        InputStream is = null;
        try {
            url = new URL(web);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Connection", "keep-alive");
            connection
                    .setRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                baos = new ByteArrayOutputStream();
                is = connection.getInputStream();
                // 非文件获取时数据不准确。例如:html,
                int total = connection.getContentLength();
                int c;
                long start = System.currentTimeMillis();
                byte[] tmp = new byte[MAX_SIZE];
                java.text.NumberFormat percentFormat = java.text.NumberFormat.getPercentInstance();
                percentFormat.setMaximumFractionDigits(2);
                while ((c = is.read(tmp)) != -1) {
                    baos.write(tmp, 0, c);
                    if (args.length > 1 && null != args[1]) {
                        saveFile(tmp, true, args[1]);
                    }
                    float size = baos.size();
                    long now = (System.currentTimeMillis() - start);
                    if (size > 0 && now > 0) {
                        float size2 = size / 1024;
                        float times = (float) now / 1000;
                        float result = size2 / times;
                        System.out.println("\t 下载速度:" + round(result, 2, BigDecimal.ROUND_HALF_UP)
                                + " KB/s| 下载百分比:" + percentFormat.format(size / total)
                                + "|| 已下载大小:" + size + "| 时间:" + now);
                    } else {
                        System.out.println("\t 暂无估算" + now);
                    }
                }
                if (total == baos.size()) {
                    System.out.println("\t 数据大小一致\t total:" + total + "|baos.size:" + baos.size());
                } else {
                    System.out.println("\t 数据大小不一致,实际下载:" + baos.size() + "|正常大小:" + total);
                }
                System.out.println("\t耗时:" + (System.currentTimeMillis() - start));
                baos.flush();
                baos.close();
            }

        } catch (Exception e) {

        } finally {
            if (null != connection)
                connection.disconnect();
            if (null != url)
                url = null;
        }
    }

    private static void saveFile(byte[] body, boolean isAppend, String savePath) {
        if (null != body && body.length > 0) {
            OutputStream os = null;
            File file = null;
            try {
                file = new File(savePath);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }
                os = new FileOutputStream(file, isAppend);
                os.write(body);
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                    }

                }
            }
        } else {
            System.err.println("\t body 没有有效内容!");
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
    private static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

}
