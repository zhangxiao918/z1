
package com.bluestome.javatest.kompass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class MobileGo {

    private StringBuilder cookie = null;
    private HttpURLConnection connection = null;
    private boolean chunked;

    synchronized void request(String site) {
        URL url = null;
        String cookieVal = null;
        String sessionId = "";
        String key = null;
        try {
            url = new URL(site);
            connection = (HttpURLConnection) url.openConnection();
            connection
                    .addRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            connection.setRequestMethod("GET");
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // 服务端响应成功
                if (null == cookie) {
                    cookie = new StringBuilder();
                    for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            cookieVal = connection.getHeaderField(i);
                            cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                            sessionId = sessionId + cookieVal + ";";
                        } else if (key.equalsIgnoreCase("Transfer-Encoding")) {
                            chunked = true;
                        } else {
                            System.out.println(key + ":" + connection.getHeaderField(key));
                        }
                    }
                    cookie.append(sessionId);
                }
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ByteArrayOutputStream readChunked(byte[] bytes, boolean isChunked) throws IOException {
        if (!isChunked) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(bytes, 0, bytes.length);
            return baos;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int mark = 0;
        int pos = 0;
        int orginLength = bytes.length;
        while ((pos + 2) < orginLength) {

            if ((int) bytes[pos + 1] == 13 && (int) bytes[pos + 2] == 10) {

                byte[] length2Byte = new byte[pos - mark + 1];
                System.arraycopy(bytes, mark, length2Byte, 0, pos - mark + 1);
                int length2Int = Integer.parseInt(new String(length2Byte), 16);
                if (length2Int == 0)
                    return baos;

                pos += 3;
                mark = pos;
                baos.write(bytes, mark, length2Int);

                pos += (length2Int - 1) + 3;
                mark = pos;
            } else {
                pos++;
            }
        }
        return baos;
    }

    synchronized void requestCheckCode(String site) {
        URL url = null;
        String cookieVal = null;
        String sessionId = "";
        String key = null;
        try {
            url = new URL(site);
            connection = (HttpURLConnection) url.openConnection();
            connection
                    .addRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            if (null != cookie && !cookie.toString().equals("")) {
                connection.setRequestProperty("Cookie", cookie.toString());
            }
            connection.setRequestMethod("GET");
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // 服务端响应成功
                if (null == cookie) {
                    cookie = new StringBuilder();
                    for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            cookieVal = connection.getHeaderField(i);
                            cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                            sessionId = sessionId + cookieVal + ";";
                        }
                    }
                    cookie.append(sessionId);
                }
                System.out.println("验证码获取的服务端SESSIONID:" + cookie.toString());
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行查询
     * 
     * @param requestURL 请求地址
     * @param cookie COOKIE
     * @param reffer 引用页
     */
    public byte[] request4Body(String requestURL, String cookie, String reffer) {
        System.out.println("请求时的SESSIONID:" + cookie);
        URL url = null;
        InputStream in = null;
        ByteArrayOutputStream byteArray = null;
        byte[] body = null;
        try {
            if (null == url) {
                url = new URL(requestURL);
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(15 * 1000);
            connection.setRequestMethod("GET");
            setCommonHeardFileds(connection);
            connection.setRequestProperty("Referer", reffer);
            connection.setRequestProperty("Cookie", cookie + "; cust_type=2");
            connection.connect();

            int code = -1;
            code = connection.getResponseCode();
            int len = connection.getContentLength();
            System.out.println("响应的内容长度：" + len);
            switch (code) {
                case 200:
                    in = connection.getInputStream();
                    // 使用GZIP处理返回流
                    body = ungizp(in);
                    break;
                default:
                    System.out.println(connection.getResponseCode() + ":"
                            + connection.getResponseMessage());
                    break;
            }

            if (null != in) {
                in.close();
            }
        } catch (Exception e) {
            // if (null != byteArray) {
            // try {
            // byteArray.close();
            // } catch (IOException e1) {
            // e1.printStackTrace();
            // }
            // }
            if (null != url) {
                url = null;
            }
        }
        return body;
    }

    /**
     * 设置通用头
     */
    private void setCommonHeardFileds(HttpURLConnection connection2) {
        connection.setRequestProperty("Host", "cn.kompass.com");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection
                .setRequestProperty(
                        "Wysistat",
                        "0.016620907234027982_1365389888381%uFFFD1%uFFFD1365391118838%uFFFD1%uFFFD1365389888%uFFFD0.016620907234027982_1365389888381");
        connection
                .setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Encoding",
                "gzip,deflate,sdch");
        connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        connection.setRequestProperty("Accept-Charset",
                "GBK,utf-8;q=0.7,*;q=0.3");
    }

    private byte[] ungizp(InputStream in) throws IOException {
        byte[] body = null;
        // 建立gzip解压工作流
        GZIPInputStream gzin = new GZIPInputStream(in);
        ByteArrayOutputStream fout = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int num;

        while ((num = gzin.read(buf, 0, buf.length)) != -1)
        {
            fout.write(buf, 0, num);
        }
        gzin.close();
        fout.close();
        body = fout.toByteArray();
        return body;
    }

    /**
     * @return the cookie
     */
    public StringBuilder getCookie() {
        if (null == cookie) {
            return new StringBuilder();
        }
        return cookie;
    }

    /**
     * @param cookie the cookie to set
     */
    public void setCookie(StringBuilder cookie) {
        this.cookie = cookie;
    }

    /**
     * @return the connection
     */
    public HttpURLConnection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }

}
