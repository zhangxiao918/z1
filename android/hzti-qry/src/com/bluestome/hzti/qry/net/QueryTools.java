
package com.bluestome.hzti.qry.net;

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

import junit.framework.Assert;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;

import com.bluestome.hzti.qry.common.Constants;

public class QueryTools {

    static byte[] CONTENT_BODY = null;

    public static void setCONTENT_BODY(byte[] cONTENT_BODY) {
        CONTENT_BODY = cONTENT_BODY;
    }

    public static byte[] getCONNTENT_BODY() {
        return CONTENT_BODY;
    }

    /**
     * 初始化数据获取网站正文
     */
    private static void init(String cookie) {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        ByteArrayOutputStream out = null;
        byte[] result = null;
        String sid = null;
        try {
            url = new URL(Constants.URL);
            connection = (HttpURLConnection) url.openConnection();
            connection
                    .addRequestProperty("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.addRequestProperty("Accept-Encoding",
                    "gzip,deflate,sdch");
            connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            connection.addRequestProperty("Accept-Charset",
                    "GBK,utf-8;q=0.7,*;q=0.3");
            connection.addRequestProperty("Connection", "close");// keep-alive
            connection.addRequestProperty("Cache-Control", "max-age=0");
            connection.addRequestProperty("Cookie", cookie);
            connection
                    .addRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(15 * 1000);
            connection.connect();

            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                int length = connection.getContentLength();
                if (length > 0) {
                    System.out.println("网页内容大小:" + length);
                    out = new ByteArrayOutputStream();
                    is = connection.getInputStream();
                    int ch;
                    while ((ch = is.read()) != -1) {
                        out.write(ch);
                    }
                    out.flush();
                    result = out.toByteArray();
                }
            }
            CONTENT_BODY = result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection)
                connection.disconnect();
        }
    }

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
            if (null != list && list.size() > 0) {
                // InputTag input = (InputTag) list.elementAt(0);
                // String value = input.getAttribute("value");
                // System.out.println(value);
            }

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
                System.out.println("\t__EVENTVALIDATION.length:"
                        + value.length());
                System.out.println(value);
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
                System.out.println("\t__VIEWSTATE.length:" + value.length());
                System.out.println(value);
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
        ByteArrayOutputStream tmps = new ByteArrayOutputStream(10 * 1024);
        HashMap<String, String> params = queryHTML(CONTENT_BODY);
        URL url = null;
        HttpURLConnection connection = null;
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
            // String body = new String(tmpBody);

            System.out.println("\t 请求正文长度：" + tmpBody.length);
            System.out.println("请求正文：" + tmpBody);
            url = new URL(Constants.URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(15 * 1000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", "www.hzti.com");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Length", String.valueOf(tmpBody.length));
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
            connection.setRequestProperty("Cookie",
                    cookie);
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());

            out.write(tmpBody);
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
                    System.out.println(result);
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
}
