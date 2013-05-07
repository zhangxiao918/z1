
package com.bluestome.hzti.qry.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import junit.framework.Assert;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;

import android.util.Log;

import com.bluestome.hzti.qry.R;

public class MobileGo {

    private String TAG = MobileGo.class.getSimpleName();

    private StringBuilder cookie = null;
    HttpURLConnection connection = null;
    byte[] CONTENT_BODY = null;

    public synchronized void request(String site) {
        URL url = null;
        String cookieVal = null;
        String sessionId = "";
        String key = null;
        try {
            url = new URL(site);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Host", "www.hzti.com");
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
                        }
                    }
                    cookie.append(sessionId);
                }
                System.out.println("cookie:" + cookie.toString());
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
                    CONTENT_BODY = baos.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized byte[] requestCheckCode(String site) {
        URL url = null;
        String cookieVal = null;
        String sessionId = "";
        String key = null;
        byte[] body = null;
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
                    body = baos.toByteArray();
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
    public String doQuery(String cookie, String reffer, String carType,
            String carNum, String carId, String checkCode) {
        String bodyString = null;
        if (null == CONTENT_BODY) {
            Log.e(TAG, "内容为空");
            return bodyString;
        }
        Log.d(TAG, "查询时请求时的SESSIONID:" + cookie);
        ByteArrayOutputStream tmps = new ByteArrayOutputStream(10 * 1024);
        HashMap<String, String> params = parserHTML(CONTENT_BODY);
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
                    Log.d(TAG, "响应的长度为：" + length);
                    byteArray = new ByteArrayOutputStream(length);
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
                    loadImage2(Constants.AUTH_CODE_URL, R.id.checkCodeView);
                    bodyString = result;
                    break;
                default:
                    Log.e(TAG, connection.getResponseCode() + ":"
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
                    Log.e(TAG, e.getMessage());
                }
            }

            if (null != url) {
                url = null;
            }
            sb = null;
            Log.e(TAG, e.getMessage());
        }
        return bodyString;
    }

    /**
     * 读取服务端的验证码
     * 
     * @param site
     * @param id
     */
    void loadImage2(final String site, final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    requestCheckCode(Constants.AUTH_CODE_URL);
                }
            }
        }).start();
    }

    /**
     * 执行查询
     * 
     * @param requestURL 请求地址
     * @param cookie COOKIE
     * @param reffer 引用页
     */
    public byte[] request4Body(String requestURL, String cookie, String reffer) {
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
            switch (code) {
                case 200:
                    in = connection.getInputStream();
                    byteArray = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int ch;
                    while ((ch = in.read(buffer)) != -1) {
                        byteArray.write(buffer, 0, ch);
                    }
                    byteArray.flush();
                    byteArray.close();
                    body = byteArray.toByteArray();
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
            if (null != byteArray) {
                try {
                    byteArray.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
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
        connection.setRequestProperty("Host", "www.hzgjj.gov.cn:8080");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Origin", "http://www.hzgjj.gov.cn:8080");
        connection
                .setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Encoding",
                "gzip,deflate,sdch");
        connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        connection.setRequestProperty("Accept-Charset",
                "GBK,utf-8;q=0.7,*;q=0.3");
    }

    public String adapterResponseCode(String code) {
        String result = "";
        if (null == code || code.equals("") || code.length() == 0) {
            result = "响应内容为空";
        } else {
            code = code.trim();
            if (code.equals("-1")) {
                result = "输入用户名或密码不正确,请重新录入";
            } else if (code.equals("1")) {
                result = "认证成功";
            } else if (code.equals("2")) {
                result = "验证码不正确,请重新录入";
            } else if (code.equals("3")) {
                result = "用户被停用，若有疑问请咨询公积金管理中心";
            } else if (code.equals("5")) {
                result = "您市民邮箱的个人信息与中心不符";
            } else if (code.equals("6")) {
                result = "市民邮箱接口错误";
            } else if (code.equals("7")) {
                result = "输入的市民邮箱或密码错误,不能登录";
            } else if (code.equals("8")) {
                result = "您市民邮箱的个人姓名不正确";
            } else if (code.equals("9")) {
                result = "您市民邮箱的个人身份证号码不正确";
            } else if (code.equals("10")) {
                result = "该用户在业务系统中存在重复注册的情况，请选择其他方式登录";
            } else {
                result = "未知状态";
            }
        }
        return result;
    }

    // public List<DepositBean> parser(String body) throws IOException,
    // ParserException {
    // List<DepositBean> rList = new ArrayList<DepositBean>(15);
    // DepositBean bean = null;
    // Parser p = new Parser();
    // p.setInputHTML(body);
    // p.setEncoding("GBK");
    //
    // NodeFilter fileter = new NodeClassFilter(TableTag.class);
    // NodeList list = p.extractAllNodesThatMatch(fileter)
    // .extractAllNodesThatMatch(
    // new HasAttributeFilter("class", "BStyle_TB"));
    // if (null != list && list.size() > 0) {
    // TableTag table = (TableTag) list.elementAt(0);
    // p = new Parser();
    // p.setInputHTML(table.toHtml());
    // fileter = new NodeClassFilter(TableRow.class);
    // list = p.extractAllNodesThatMatch(fileter);
    // if (null != list) {
    // int c = list.size();
    // // 获取最终数据
    // for (int i = 0; i < list.size(); i++) {
    // TableRow tr = (TableRow) list.elementAt(i);
    // if (i < 2) { // 前2项为提示和标题，不需要
    // continue;
    // }
    // p = new Parser();
    // p.setInputHTML(tr.toHtml());
    // fileter = new NodeClassFilter(TableColumn.class);
    // NodeList sl = p.extractAllNodesThatMatch(fileter);
    // if (null != sl && sl.size() > 0) {
    // bean = new DepositBean();
    // bean.setIndex(Integer.valueOf(sl.elementAt(0).toPlainTextString().trim()));
    // bean.setDate(sl.elementAt(1).toPlainTextString().trim());
    // bean.setType(sl.elementAt(2).toPlainTextString().trim());
    // bean.setCompany(sl.elementAt(3).toPlainTextString().trim());
    // bean.setSelf(sl.elementAt(4).toPlainTextString().trim());
    // bean.setTotal(sl.elementAt(5).toPlainTextString().trim());
    // bean.setPay(sl.elementAt(6).toPlainTextString().trim());
    // bean.setIn(sl.elementAt(7).toPlainTextString().trim());
    // rList.add(bean);
    // }
    // }
    // } else {
    // System.err.println("No Sub Element Fond!");
    // }
    // } else {
    // System.err.println("No Element Fond!");
    // }
    // p = null;
    // return rList;
    // }

    public StringBuilder getCookie() {
        return cookie;
    }

    public void setCookie(StringBuilder cookie) {
        this.cookie = cookie;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    public void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }

    public void reset() {
        cookie = null;
        connection = null;
    }
}
