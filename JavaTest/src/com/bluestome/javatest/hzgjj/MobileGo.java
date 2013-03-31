
package com.bluestome.javatest.hzgjj;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MobileGo {

    // 杭州公积金登录界面
    static final String HZGJJ_LOGIN_URL = "http://www.hzgjj.gov.cn:8080/WebAccounts/pages/per/login.jsp";
    // 杭州公积金验证码地址
    static final String HZGJJ_CHECKCODE_URL = "http://www.hzgjj.gov.cn:8080/WebAccounts/codeMaker?d="
            + System.currentTimeMillis();
    // 杭州公积金登录请求地址
    static final String HZGJJ_LOGIN_POST_URL = "http://www.hzgjj.gov.cn:8080/WebAccounts/userLogin.do";

    StringBuilder cookie = null;
    HttpURLConnection connection = null;

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
     * @param cookie COOKIE
     * @param reffer 引用页
     * @param account 帐号
     * @param pwd 密码
     * @param authCode 验证码
     */
    public void loginPost(String postURL, String cookie, String reffer, String account, String pwd,
            String authCode) {
        URL url = null;
        InputStream in = null;
        ByteArrayOutputStream byteArray = null;
        StringBuffer sb = new StringBuffer(100 * 1024);
        try {
            // TODO 写提交的数据

            // 1.cust_type
            sb.append("cust_type=2");

            // 2.flag
            sb.append("&");
            sb.append("flag=1");

            // 3.user_type_2
            sb.append("&");
            sb.append("user_type_2=3");

            // 4.user_type
            sb.append("&");
            sb.append("user_type=3");

            // 5.cust_no
            sb.append("&");
            sb.append("cust_no=").append(account);

            // 6.password
            sb.append("&");
            sb.append("password=").append(pwd);

            // 7.validate_code
            sb.append("&");
            sb.append("validate_code=").append(authCode);

            postURL += "?" + sb.toString();
            System.out.println("postURL:\r\n" + postURL);
            if (null == url) {
                url = new URL(postURL);
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(15 * 1000);
            connection.setRequestMethod("POST");
            setCommonHeardFileds(connection);
            connection.setRequestProperty("X-Prototype-Version", "1.6.0.3");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
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
                    String result = byteArray.toString("GBK");
                    adapterResponseCode(result);
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
            sb = null;
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
        System.out.println("登录请求时的SESSIONID:" + cookie);
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

    private void adapterResponseCode(String code) {
        if (null == code || code.equals("") || code.length() == 0) {
            System.err.println("响应内容为空");
            return;
        } else {
            code = code.trim();
            if (code.equals("-1")) {
                System.out.println("输入用户名或密码不正确,请重新录入");
                return;
            } else if (code.equals("1")) {
                System.out.println("");
                return;
            } else if (code.equals("2")) {
                System.out.println("验证码不正确,请重新录入");
                return;
            } else if (code.equals("3")) {
                System.out.println("用户被停用，若有疑问请咨询公积金管理中心");
                return;
            } else if (code.equals("5")) {
                System.out.println("您市民邮箱的个人信息与中心不符");
                return;
            } else if (code.equals("6")) {
                System.out.println("市民邮箱接口错误");
                return;
            } else if (code.equals("7")) {
                System.out.println("输入的市民邮箱或密码错误,不能登录");
                return;
            } else if (code.equals("8")) {
                System.out.println("您市民邮箱的个人姓名不正确");
                return;
            } else if (code.equals("9")) {
                System.out.println("您市民邮箱的个人身份证号码不正确");
                return;
            } else if (code.equals("10")) {
                System.out.println("该用户在业务系统中存在重复注册的情况，请选择其他方式登录");
                return;
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        MobileGo go = new MobileGo();
        go.request(HZGJJ_LOGIN_URL);
        go.requestCheckCode(HZGJJ_CHECKCODE_URL);
        go.loginPost(HZGJJ_LOGIN_POST_URL,
                go.cookie.toString(),
                HZGJJ_LOGIN_URL, "jack", "123456", "abcd");
        byte[] body = go.request4Body(HZGJJ_LOGIN_URL, go.cookie.toString(), HZGJJ_LOGIN_URL);
        try {
            System.out.println(new String(body, "GBK"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
