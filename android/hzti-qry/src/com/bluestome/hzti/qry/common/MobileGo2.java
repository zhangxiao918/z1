
package com.bluestome.hzti.qry.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;

import android.util.Log;

import com.bluestome.hzti.qry.utils.ByteUtils;
import com.bluestome.hzti.qry.utils.StringUtils;

public class MobileGo2 {

    private String TAG = MobileGo2.class.getSimpleName();

    private StringBuilder cookie = null;
    HttpClient client = null;
    HttpGet httpGet = null;
    HttpPost httpPost = null;

    public MobileGo2() {

    }

    public synchronized String request4Header(String site) {
        String rCookie = "";
        String cookieVal = null;
        String sessionId = "";
        try {
            client = new DefaultHttpClient();
            httpGet = new HttpGet(site);
            httpGet.addHeader("Host", "www.hzti.com");
            httpGet
                    .addHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            HttpResponse response = client.execute(httpGet);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                // 服务端响应成功
                if (null == cookie) {
                    cookie = new StringBuilder();
                    for (Header header : response.getAllHeaders()) {
                        if (header.getName().equalsIgnoreCase("set-cookie")) {
                            cookieVal = header.getValue();
                            cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                            sessionId = sessionId + cookieVal + ";";
                        } else {
                            Log.d(TAG, header.getName() + ":" + header.getValue());
                        }
                    }
                    cookie.append(sessionId);
                    rCookie = cookie.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rCookie;
    }

    public synchronized byte[] request(String oCookie, String site) {
        byte[] body = null;
        String cookieVal = null;
        String sessionId = "";
        try {
            client = new DefaultHttpClient();
            httpGet = new HttpGet(site);
            httpGet.addHeader("Host", "www.hzti.com");
            httpGet.addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            if (!StringUtils.isBlank(oCookie)) {
                httpGet.addHeader("Cookie", oCookie);
            }
            HttpResponse response = client.execute(httpGet);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                // 服务端响应成功
                StringBuilder sbc = new StringBuilder();
                for (Header header : response.getAllHeaders()) {
                    if (header.getName().equalsIgnoreCase("set-cookie")) {
                        cookieVal = header.getValue();
                        cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                        sessionId = sessionId + cookieVal + ";";
                    } else {
                        Log.d(TAG, header.getName() + ":" + header.getValue());
                    }
                }
                sbc.append(sessionId);
                if (!StringUtils.isBlank(sbc.toString())) {
                    cookie = sbc;
                }
                try {
                    InputStream is = response.getEntity().getContent();
                    byte[] buffer = new byte[2048];
                    int c;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((c = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, c);
                        baos.flush();
                    }
                    body = baos.toByteArray();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    public synchronized byte[] requestCheckCode(String oCookie, String site) {
        byte[] body = null;
        String cookieVal = null;
        String sessionId = "";
        try {
            client = new DefaultHttpClient();
            httpGet = new HttpGet(site);
            httpGet.addHeader("Host", "www.hzti.com");
            httpGet.addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
            if (!StringUtils.isBlank(oCookie)) {
                httpGet.addHeader("Cookie", oCookie);
            }
            HttpResponse response = client.execute(httpGet);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                // 服务端响应成功
                StringBuilder sbc = new StringBuilder();
                for (Header header : response.getAllHeaders()) {
                    if (header.getName().equalsIgnoreCase("set-cookie")) {
                        cookieVal = header.getValue();
                        cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                        sessionId = sessionId + cookieVal + ";";
                    } else {
                        Log.d(TAG, header.getName() + ":" + header.getValue());
                    }
                }
                sbc.append(sessionId);
                if (!StringUtils.isBlank(sbc.toString())) {
                    cookie = sbc;
                }
                try {
                    InputStream is = response.getEntity().getContent();
                    byte[] buffer = new byte[2048];
                    int c;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((c = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, c);
                        baos.flush();
                    }
                    body = baos.toByteArray();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 查询页面
     */
    public static HashMap<String, String> parserHTML(byte[] content) {
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
    public String doQuery(byte[] iBody, String oCookie, String reffer, String carType,
            String carNum, String carId, String checkCode) {
        String bodyString = null;
        if (ByteUtils.isBlank(iBody)) {
            Log.e(TAG, "内容为空");
            return bodyString;
        }
        HashMap<String, String> params = parserHTML(iBody);
        InputStream in = null;
        ByteArrayOutputStream byteArray = null;
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        try {
            client = new DefaultHttpClient();
            httpPost = new HttpPost(Constants.URL);

            NameValuePair pair1 = new BasicNameValuePair("ctl00$ScriptManager1", "");
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("__EVENTTARGET", "");
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("__EVENTARGUMENT", "");
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("__VIEWSTATE", params.get("__VIEWSTATE"));
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("__EVENTVALIDATION", params.get("__EVENTVALIDATION"));
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("ctl00$ContentPlaceHolder1$hpzl", carType);
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("ctl00$ContentPlaceHolder1$steelno", carNum);
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("ctl00$ContentPlaceHolder1$identificationcode", carId);
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("ctl00$ContentPlaceHolder1$checkcode", checkCode);
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("__ASYNCPOST", "true");
            paramList.add(pair1);

            pair1 = new BasicNameValuePair("ctl00$ContentPlaceHolder1$Button1", "查询");
            paramList.add(pair1);

            httpPost.addHeader("Host", "www.hzti.com");
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("Cache-Control", "no-cache");
            httpPost.addHeader("Origin", "http://www.hzti.com");
            httpPost.addHeader("X-MicrosoftAjax", "Delta=true");
            httpPost.addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22");
            httpPost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Referer", reffer);
            httpPost.addHeader("Accept-Encoding",
                    "gzip,deflate,sdch");
            httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpPost.addHeader("Accept-Charset",
                    "GBK,utf-8;q=0.7,*;q=0.3");
            if (!StringUtils.isBlank(oCookie)) {
                httpPost.addHeader("Cookie", oCookie);
            }
            HttpEntity he = null;
            try {
                he = new UrlEncodedFormEntity(paramList, "UTF-8");
                httpPost.setEntity(he);
            } catch (UnsupportedEncodingException e1) {
                Log.e(TAG, "对提交内容进行编解码失败");
            }
            HttpResponse response = client.execute(httpPost);

            int code = -1;
            code = response.getStatusLine().getStatusCode();
            switch (code) {
                case 200:
                    StringBuilder sbc = new StringBuilder();
                    String cookieVal = "";
                    String sessionId = "";
                    for (Header header : response.getAllHeaders()) {
                        if (header.getName().equalsIgnoreCase("set-cookie")) {
                            cookieVal = header.getValue();
                            cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                            sessionId = sessionId + cookieVal + ";";
                        }
                    }
                    sbc.append(sessionId);
                    if (!StringUtils.isBlank(sbc.toString())) {
                        cookie = sbc;
                    }
                    in = response.getEntity().getContent();
                    Long length = response.getEntity().getContentLength();
                    byteArray = new ByteArrayOutputStream(length.intValue());
                    byte[] buffer = new byte[1024];
                    int ch;
                    while ((ch = in.read(buffer)) != -1) {
                        byteArray.write(buffer, 0, ch);
                    }
                    byteArray.flush();
                    byteArray.close();
                    String result = byteArray.toString("UTF-8");
                    int pos = result.indexOf("\r\n");
                    if (length < 30 * 1024 && pos != -1) {
                        if (pos != -1) {
                            Log.d(TAG, result.substring(0, pos));
                        }
                    }
                    bodyString = result;
                    break;
                default:
                    Log.e(TAG, response.getStatusLine().getStatusCode() + ":"
                            + response.getStatusLine().getReasonPhrase());
                    return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (null != byteArray) {
                try {
                    byteArray.close();
                } catch (IOException e1) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return bodyString;
    }

    public String getCookie() {
        if (null != cookie) {
            return cookie.toString();
        }
        return null;
    }

    public void setCookie(StringBuilder cookie) {
        this.cookie = cookie;
    }

    public HttpClient getConnection() {
        return client;
    }

    public void setConnection(HttpClient client) {
        this.client = client;
    }

    public void reset() {
        cookie = null;
        client = null;
    }
}
