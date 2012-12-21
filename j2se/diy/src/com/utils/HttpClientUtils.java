package com.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.chinamilitary.util.DateUtils;

public class HttpClientUtils {

	private static HttpClient httpclient = null;

	private static GetMethod getMethod = null;

	private static PostMethod postMethod = null;

	// HTTP响应头中的文件大小描述
	public static String CONTENTLENGTH = "Content-Length";

	/**
	 * 验证URL是否可用
	 * 
	 * @param url
	 * @return
	 */
	public static boolean validationURL(String url) {
		boolean success = true;
		try {
			httpclient = new HttpClient();
			getMethod = new GetMethod(url);
			// 请求次数
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				success = false;
			}
		} catch (IOException ioe) {
			success = false;
			return false;
		} catch (Exception e) {
			success = false;
			return false;
		} finally {
			if (null != getMethod)
				getMethod.releaseConnection();
			if (null != httpclient)
				httpclient = null;
		}
		return success;
	}

	/**
	 * 获取响应头
	 * 
	 * @param url
	 * @return
	 */
	static HashMap<String, String> getHttpHeaderResponse(String url) {
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			httpclient = new HttpClient();
			getMethod = new GetMethod(url);

			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				Header[] headers = getMethod.getResponseHeaders();
				for (Header header : headers) {
					result.put(header.getName(), header.getValue());
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != getMethod)
				getMethod.releaseConnection();
			if (null != httpclient)
				httpclient = null;
		}
		return result;
	}

	/**
	 * 从url中获取响应头的内容
	 * 
	 * @param url
	 *            地址
	 * @param headerName
	 *            头名
	 * @return
	 */
	public static String getHttpHeaderResponse(String url, String headerName) {
		String result = null;
		long start = System.currentTimeMillis();
		try {
			httpclient = new HttpClient();
			getMethod = new GetMethod(url);

			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = getMethod.getResponseHeader(headerName).getValue();
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != getMethod)
				getMethod.releaseConnection();
			if (null != httpclient)
				httpclient = null;
		}
		long end = System.currentTimeMillis();
		return result;
	}

	/**
	 * 调用HttpURLConnection获取文件大小
	 * 
	 * @param url
	 * @return
	 */
	public static String getHttpConentLength(String url) {
		long start = System.currentTimeMillis();
		String result = null;
		URL urlc = null;
		HttpURLConnection conn = null;
		try {
			urlc = new URL(url);
			conn = (HttpURLConnection) urlc.openConnection();
			conn.connect();
			int code = conn.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				result = String.valueOf(conn.getContentLength());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println(" > 获取文件大小耗时:["
					+ (System.currentTimeMillis() - start) + "]ms");
			if (null != conn) {
				conn.disconnect();
			}
		}
		return result;
	}

	/**
	 * 获取响应体
	 * 
	 * @param url
	 *            请求的地址
	 * @return
	 */
	public static String getResponseBody(String url) {
		String value = "";
		try {
			httpclient = new HttpClient();
			getMethod = new GetMethod(url);
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				value = new String(getMethod.getResponseBody(), "UTF-8");
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != getMethod)
				getMethod.releaseConnection();
			if (null != httpclient)
				httpclient = null;
		}
		return value;
	}

	/**
	 * 获取响应体
	 * 
	 * @param url
	 * @return
	 */
	public static String getResponseBody(String url, String charset) {
		String value = "";
		try {
			httpclient = new HttpClient();
			getMethod = new GetMethod(url);
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				value = new String(getMethod.getResponseBody(), charset);
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != getMethod)
				getMethod.releaseConnection();
			if (null != httpclient)
				httpclient = null;
		}
		return value;
	}

	/**
	 * 获取页面的最后修改时间
	 * 
	 * @param url
	 * @return
	 */
	public static String getLastModifiedByUrl(String url) {
		String value = "1970-01-01 00:00:00";
		URL cURL = null;
		try {
			cURL = new URL(url);
			URLConnection connection = cURL.openConnection();
			connection.setConnectTimeout(15 * 1000);
			connection.connect();

			String time = connection.getHeaderField("Last-Modified");
			Date date = DateUtils.parserDate(time);
			value = DateUtils
					.formatDate(date, DateUtils.FULL_STANDARD_PATTERN2);
		} catch (Exception e) {
			System.err.println("ERROR:" + e);
		}
		return value;
	}

	/**
	 * 获取响应体
	 * 
	 * @param url
	 *            实际下载地址
	 * @return
	 */
	public static byte[] getResponseBodyAsByte(String url) {
		byte[] value = null;
		value = getResponseBodyAsByte(null, null, url);
		return value;
	}

	/**
	 * 获取响应体
	 * 
	 * @param cookie
	 *            cookie内容
	 * @param url
	 *            实际下载地址
	 * @return
	 */
	public static byte[] getResponseBodyAsByte(String cookie, String url) {
		byte[] value = null;
		value = getResponseBodyAsByte(null, cookie, url);
		return value;
	}

	public static String encodeURL(String url, String encode)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		StringBuilder noAsciiPart = new StringBuilder();
		for (int i = 0; i < url.length(); i++) {
			char c = url.charAt(i);
			if (c > 255) {
				noAsciiPart.append(c);
			} else {
				if (noAsciiPart.length() != 0) {
					sb.append(URLEncoder.encode(noAsciiPart.toString(), encode));
					noAsciiPart.delete(0, noAsciiPart.length());
				}
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取响应体
	 * 
	 * @param refence
	 *            引用的地址
	 * @param cookie
	 *            页面保存的信息
	 * @param url
	 * @return
	 */
	public static byte[] getResponseBodyAsByte(String refence, String cookie,
			String url) {
		byte[] value = null;
		HttpClientParams params = null;
		Header charset = null;
		try {
			httpclient = new HttpClient();
			// params.setContentCharset("GBK");
			getMethod = new GetMethod(encodeURL(url, "UTF-8"));
			// getMethod.setParams(params);

			// 破解防盗链配置
			if (null != refence)
				getMethod.addRequestHeader("Referer", refence);
			if (null != cookie)
				getMethod.setRequestHeader("Cookie", cookie);
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				// charset = getMethod.getResponseHeader("charset");
				// if(null == charset){
				//
				// }else{
				value = getMethod.getResponseBody();
				// }
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != getMethod)
				getMethod.releaseConnection();
			if (null != httpclient)
				httpclient = null;
		}
		return value;
	}

	public static void main(String args[]) {

		String value = "";
		String url = "http://tuku.military.china.com/military/html/2010-04-02/139456.xml";
		String pushUrl = "http://localhost:8080/mrpmusicfs/mrpmusic.do";
		String imgurl = "http://bizhi.zhuoku.com/2009//03/25/lvye/Lvye43.jpg";
		String website = "http://www.sky-mobi.com/";
		try {

			String length = getHttpHeaderResponse(website, CONTENTLENGTH);

			System.out.println(length == null ? "NULL" : length);

			// boolean suc = validationURL(url);
			// boolean suc = validationURL(pushUrl);

			// System.out.println("suc:"+suc);

			// httpclient = new HttpClient();
			// getMethod = new GetMethod(
			// url);
			//
			// int statusCode = httpclient.executeMethod(getMethod);
			// if (statusCode == HttpStatus.SC_OK) {
			// value = new String(getMethod.getResponseBody(), "UTF-8");
			// // result = getMethod.getResponseHeader(headerName).getValue();
			// }
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != getMethod)
				getMethod.releaseConnection();
			if (null != httpclient)
				httpclient = null;
		}
		// if (!"".equalsIgnoreCase(value)) {
		// int start = value.indexOf("intro=") + 6;
		// int end = value.indexOf("comment");
		// String tmp = value.substring(start, end);
		// System.out.println("value:" + value.replace(tmp, "\"\" "));
		// }
	}

}
