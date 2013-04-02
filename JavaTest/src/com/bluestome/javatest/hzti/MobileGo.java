
package com.bluestome.javatest.hzti;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;

/**
 * @ClassName: MobileGo
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-3-27 下午4:08:00
 */
public class MobileGo {

    static AtomicLong lastRequestTimes = new AtomicLong();
    static byte[] CONTENT_BODY = null;
    static HttpURLConnection connection = null;
    static String cookie = "";

    /**
     * 查询页面
     */
    public static HashMap<String, String> queryHTML(byte[] content) {
        Parser p1 = null;
        HashMap<String, String> values = new HashMap<String, String>();
        try {
            String body = new String(content, "UTF-8");
            p1 = new Parser();
            p1.setInputHTML(body);

            // 获取__VIEWSTATE 参数
            NodeFilter fileter = new NodeClassFilter(InputTag.class);
            NodeList list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__VIEWSTATE"));
            // 获取__EVENTTARGET 参数
            p1.setInputHTML(body);
            fileter = new NodeClassFilter(InputTag.class);
            list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__EVENTTARGET"));
            if (null != list && list.size() > 0) {
                InputTag input = (InputTag) list.elementAt(0);
                Assert.assertNotNull(input);

                String value = input.getAttribute("value");
                Assert.assertNotNull(value);
                values.put("__EVENTTARGET", value);
            }

            // 获取__EVENTARGUMENT 参数
            p1.setInputHTML(body);
            fileter = new NodeClassFilter(InputTag.class);
            list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__EVENTARGUMENT"));
            if (null != list && list.size() > 0) {
                InputTag input = (InputTag) list.elementAt(0);
                Assert.assertNotNull(input);

                String value = input.getAttribute("value");
                Assert.assertNotNull(value);
                values.put("__EVENTARGUMENT", value);
            }

            // 获取__EVENTVALIDATION 参数
            p1.setInputHTML(body);
            fileter = new NodeClassFilter(InputTag.class);
            list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__EVENTVALIDATION"));
            if (null != list && list.size() > 0) {
                InputTag input = (InputTag) list.elementAt(0);
                Assert.assertNotNull(input);

                String value = input.getAttribute("value");
                Assert.assertNotNull(value);
                values.put("__EVENTVALIDATION", value);
            }

            // 获取__VIEWSTATE 参数
            p1.setInputHTML(body);
            fileter = new NodeClassFilter(InputTag.class);
            list = p1.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "__VIEWSTATE"));
            if (null != list && list.size() > 0) {
                InputTag input = (InputTag) list.elementAt(0);
                Assert.assertNotNull(input);

                String value = input.getAttribute("value");
                Assert.assertNotNull(value);
                values.put("__VIEWSTATE", value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != p1) {
                p1 = null;
            }
        }
        return values;
    }

    /**
     * 执行查询
     * 
     * @param cookie COOKIE
     * @param reffer 引用页
     * @param carType 车辆类型
     * @param carNum 车牌号码
     * @param carId 车架编号后6位
     * @param checkCode 验证码
     */
    public static void doQuery(String cookie, String reffer, String carType,
            String carNum, String carId, String checkCode) {
        if (null == CONTENT_BODY) {
            System.out.println("CONTENT_BODY is null");
            return;
        }
        if (null == cookie || cookie.equals("") || cookie.length() == 0) {
            System.out.println("cookie is null");
            return;
        }
        System.out.println("请求时的:" + cookie);
        ByteArrayOutputStream tmps = new ByteArrayOutputStream(10 * 1024);
        HashMap<String, String> params = queryHTML(CONTENT_BODY);
        URL url = null;
        DataOutputStream out = null;
        InputStream in = null;
        ByteArrayOutputStream byteArray = null;
        StringBuffer sb = new StringBuffer(100 * 1024);
        try {
            // TODO 写提交的数据
            sb.append(URLEncoder.encode("ctl00$ScriptManager1", "UTF-8")
                    + "="
                    + URLEncoder
                            .encode("ctl00$ContentPlaceHolder1$UpdatePanel1|ctl00$ContentPlaceHolder1$Button1",
                                    "UTF-8"));
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append("__EVENTTARGET=");
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append("__EVENTARGUMENT=");
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(1024 * 10);
            sb.append("&");
            String __VIEWSTATE = URLEncoder.encode(params.get("__VIEWSTATE"), "UTF-8");
            sb.append("__VIEWSTATE=" + __VIEWSTATE);
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(1024 * 10);
            sb.append("&");
            String __EVENTVALIDATION = URLEncoder.encode(
                    params.get("__EVENTVALIDATION"), "UTF-8");
            sb.append("__EVENTVALIDATION=" + __EVENTVALIDATION);
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$hpzl",
                    "UTF-8") + "=" + URLEncoder.encode(carType, "UTF-8"));
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$steelno",
                    "UTF-8") + "=" + URLEncoder.encode(carNum, "UTF-8"));
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode(
                    "ctl00$ContentPlaceHolder1$identificationcode", "UTF-8")
                    + "=" + carId);
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$checkcode",
                    "UTF-8") + "=" + checkCode);
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append("__ASYNCPOST=true");
            tmps.write(sb.toString().getBytes());

            sb = null;
            sb = new StringBuffer(200);
            sb.append("&");
            sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$Button1",
                    "UTF-8")).append("=").append(URLEncoder.encode("查询", "UTF-8"));
            tmps.write(sb.toString().getBytes());
            tmps.flush();
            tmps.close();
            byte[] tmpBody = tmps.toByteArray();
            String body = new String(tmpBody);

            System.out.println("\t 请求正文长度：" + body.length());
            System.out.println("请求正文：" + body);
            url = new URL(Constants.URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(15 * 1000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", "www.hzti.com");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Origin", "http://www.hzti.com");
            connection.setRequestProperty("X-MicrosoftAjax", "Delta=true");
            connection
                    .setRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Referer", reffer);
            connection.setRequestProperty("Accept-Encoding",
                    "gzip,deflate,sdch");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            connection.setRequestProperty("Accept-Charset",
                    "GBK,utf-8;q=0.7,*;q=0.3");
            connection.setRequestProperty("Cookie", cookie);
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());

            out.write(body.getBytes());
            out.flush();
            out.close();

            int code = -1;
            code = connection.getResponseCode();
            switch (code) {
                case 200:
                    in = connection.getInputStream();
                    int length = connection.getContentLength();
                    System.out.println("响应的长度为：" + length);
                    byteArray = new ByteArrayOutputStream(length);
                    byte[] buffer = new byte[1024];
                    int ch;
                    while ((ch = in.read(buffer)) != -1) {
                        byteArray.write(buffer, 0, ch);
                    }
                    byteArray.flush();
                    byteArray.close();
                    Map<String, List<String>> responseHead = connection.getHeaderFields();
                    Iterator<String> iterator = responseHead.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        List<String> values = responseHead.get(key);
                        for (String v : values) {
                            System.out.println("response:[" + key + ":" + v + "]");
                        }
                    }
                    String result = byteArray.toString("UTF-8");
                    int pos = result.indexOf("\r\n");
                    if (pos != -1) {
                        System.out.println(result.substring(0, pos));
                    } else {
                        System.out.println(result);
                    }
                    requestCheckCode(Constants.CHECK_CODE_URL);
                    break;
                default:
                    System.err.println(connection.getResponseCode() + ":"
                            + connection.getResponseMessage());
                    break;
            }

            if (null != out) {
                out.close();
            }
            if (null != in) {
                in.close();
            }
        } catch (Exception e) {
            if (null != byteArray) {
                try {
                    byteArray.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if (null != connection) {
                connection.disconnect();
                connection = null;
            }

            if (null != url) {
                url = null;
            }
            sb = null;
            e.printStackTrace();
        }
    }

    static synchronized void request(String site) {
        CONTENT_BODY = null;
        URL url = null;
        String cookieVal = null;
        String sessionId = "";
        String key = null;
        StringBuilder sb = null;
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
                if (null == sb) {
                    sb = new StringBuilder();
                    for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            cookieVal = connection.getHeaderField(i);
                            cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                            sessionId = sessionId + cookieVal + ";";
                        }
                    }
                    sb.append(sessionId);
                }
                if (null != sb && !sb.toString().equals("") && sb.toString().length() > 0) {
                    cookie = sb.toString();
                }
                System.out.println("服务端 cookies:" + cookie);
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
                    if (null != body && body.length > 0 && (null == CONTENT_BODY)) {
                        CONTENT_BODY = body;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static synchronized void requestCheckCode(String site) {
        URL url = null;
        String cookieVal = null;
        String sessionId = "";
        String key = null;
        StringBuilder sb = null;
        System.out.println("验证码请求到服务端SESSIONID:" + cookie);
        try {
            url = new URL(site);
            connection = (HttpURLConnection) url.openConnection();
            connection
                    .addRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            if (null != cookie && !cookie.equals("")) {
                connection.setRequestProperty("Cookie", cookie);
            }
            connection.setRequestMethod("GET");
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // 服务端响应成功
                if (null == sb) {
                    sb = new StringBuilder();
                    for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            cookieVal = connection.getHeaderField(i);
                            cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                            sessionId = sessionId + cookieVal + ";";
                        }
                    }
                    sb.append(sessionId);
                }
                if (null != sb && !sb.toString().equals("") && sb.toString().length() > 0) {
                    cookie = sb.toString();
                    System.out.println("验证码获取的服务端SESSIONID:" + cookie);
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
                    baos = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        boolean isOk = true;
        int i = 0;
        while (isOk) {
            System.out.println(" 第" + i++ + "次");
            if ((System.currentTimeMillis() - lastRequestTimes.get()) > 60 * 5 * 1000L) {
                System.out.println("执行一次数据请求");
                request(Constants.URL);
            }
            requestCheckCode(Constants.CHECK_CODE_URL);
            doQuery(cookie, Constants.URL, "小型汽车",
                    "浙A249NT", "005953", "gkwe");
            lastRequestTimes.set(System.currentTimeMillis());
            try {
                Thread.sleep(30 * 1000L);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        }

        // try {
        // InputStream is = getStream(new URL(Constants.URL), null, new
        // URL(Constants.URL));
        // if (null != is) {
        // BufferedInputStream ii = new BufferedInputStream(is);
        // int c;
        // byte[] buffer = new byte[2048];
        // try {
        // while ((c = ii.read(buffer)) != -1) {
        // System.out.println(new String(buffer));
        // }
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        // } catch (MalformedURLException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }
}
