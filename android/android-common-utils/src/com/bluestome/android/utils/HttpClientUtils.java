package com.bluestome.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Http 工具类
 * 
 * @author bluestome
 * 
 */
public class HttpClientUtils {

	// HTTP响应头中的文件大小描述
	public static String CONTENTLENGTH = "Content-Length";

	public static boolean validationURL(String url) {
		boolean success = false;
		URL sUrl = null;
		HttpURLConnection conn = null;
		try {
			sUrl = new URL(url);
			conn = (HttpURLConnection) sUrl.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.connect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				success = true;
			}
		} catch (Exception e) {
		} finally {
			if (null != conn) {
				conn.disconnect();
				conn = null;
			}
			if (null != sUrl) {
				sUrl = null;
			}
		}
		return success;
	}

	public static boolean urlValidation(String url) {
		boolean success = false;
		URL sUrl = null;
		HttpURLConnection conn = null;
		try {
			sUrl = new URL(url);
			conn = (HttpURLConnection) sUrl.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.connect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				success = true;
			}
		} catch (Exception e) {
		} finally {
			if (null != conn) {
				conn.disconnect();
				conn = null;
			}
			if (null != sUrl) {
				sUrl = null;
			}
		}
		return success;
	}

	/**
	 * 获取响应头
	 * 
	 * @param url
	 * @return
	 */
	public static HashMap<String, String> getHttpHeaderResponse(String url) {
		HashMap<String, String> result = new HashMap<String, String>();
		URL urlO = null;
		HttpURLConnection http = null;
		try {
			urlO = new URL(url);
			http = (HttpURLConnection) urlO.openConnection();
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.connect();
			int code = http.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				Map<String, List<String>> responseHeader = http
						.getHeaderFields();
				Set<String> keySet = responseHeader.keySet();
				Iterator iterator = keySet.iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (null != key) {
						List<String> valueList = responseHeader.get(key);
						for (String value : valueList) {
							result.put(key, value);
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != http) {
				http.disconnect();
			}
		}
		return result;
	}

	/**
	 * 调用HttpURLConnection获取文件大小
	 * 
	 * @param url
	 * @return
	 */
	public static String getHttpConentLength(String refence, String cookie,
			String url) {
		long start = System.currentTimeMillis();
		String result = "0";
		URL urlc = null;
		HttpURLConnection conn = null;
		try {
			urlc = new URL(url);
			conn = (HttpURLConnection) urlc.openConnection();
			conn.setDoInput(true);
			if (null != refence) {
				conn.addRequestProperty("Referer", refence);
			}
			if (null != cookie) {
				conn.addRequestProperty("Cookie", cookie);
			}
			conn.addRequestProperty("Cache-Control", "no-cache");
			conn.addRequestProperty("Connection", "keep-alive");
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.connect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				result = String.valueOf(conn.getContentLength());
			}
		} catch (Exception e) {
			System.err.println(" > ERROR:" + e);
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
	 * 调用HttpURLConnection获取文件大小
	 * 
	 * @param url
	 * @return
	 */
	public static String getHttpConentLength(String url) {
		long start = System.currentTimeMillis();
		String result = "0";
		URL urlc = null;
		HttpURLConnection conn = null;
		try {
			urlc = new URL(url);
			conn = (HttpURLConnection) urlc.openConnection();
			conn.setDoInput(true);
			conn.addRequestProperty("Cache-Control", "no-cache");
			conn.addRequestProperty("Connection", "keep-alive");
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.connect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				result = String.valueOf(conn.getContentLength());
			}
		} catch (Exception e) {
			System.err.println(" > ERROR:" + e);
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
	 * 从url中获取响应头的内容
	 * 
	 * @param url
	 * @param headerName
	 * @return
	 */
	public static String getHttpHeaderResponse(String url, String headerName) {
		String result = null;
		URL urlO = null;
		HttpURLConnection http = null;
		try {
			urlO = new URL(url);
			http = (HttpURLConnection) urlO.openConnection();
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.connect();
			int code = http.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				Map<String, List<String>> responseHeader = http
						.getHeaderFields();
				Set<String> keySet = responseHeader.keySet();
				Iterator iterator = keySet.iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (null != key && key.trim().length() > 0) {
						if (headerName.equals(key)) {
							List<String> valueList = responseHeader.get(key);
							for (String value : valueList) {
								result = value;
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != http) {
				http.disconnect();
			}
		}
		return result;
	}

	/**
	 * 获取响应体
	 * 
	 * @param url
	 * @return
	 */
	public static String getResponseBody(String url) {
		String result = null;
		URL urlO = null;
		HttpURLConnection http = null;
		InputStream is = null;
		try {
			urlO = new URL(url);
			http = (HttpURLConnection) urlO.openConnection();
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.connect();
			int code = http.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				is = http.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int ch;
				while ((ch = is.read()) != -1) {
					out.write(ch);
				}
				out.flush();
				// 默认使用当前系统的字符集
				result = out.toString();
				out.close();
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != http) {
				http.disconnect();
			}
		}
		return result;
	}

	/**
	 * 获取响应体
	 * 
	 * @param url
	 * @return
	 */
	public static String getResponseBody(String url, String charset) {
		String result = null;
		URL urlO = null;
		HttpURLConnection http = null;
		InputStream is = null;
		try {
			urlO = new URL(url);
			http = (HttpURLConnection) urlO.openConnection();
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.connect();
			int code = http.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				is = http.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int ch;
				while ((ch = is.read()) != -1) {
					out.write(ch);
				}
				out.flush();
				result = out.toString(charset);
				out.close();
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != http) {
				http.disconnect();
			}
		}
		return result;
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
	 * 获取指定URL的内容
	 * 
	 * @param url
	 * @param header
	 *            请求头名
	 * @param headerValue
	 *            请求头值
	 * @return
	 */
	public static int getBodyLength(String url) {
		int value = -1;
		URL cURL = null;
		URLConnection connection = null;
		try {
			cURL = new URL(url);
			connection = cURL.openConnection();
			// 获取输出流
			connection.setDoOutput(true);
			connection.addRequestProperty("Cache-Control", "no-cache");
			connection.addRequestProperty("Connection", "keep-alive");
			connection.setConnectTimeout(5 * 1000);
			connection.setReadTimeout(10 * 1000);
			connection.connect();

			value = connection.getContentLength();
		} catch (Exception e) {
			System.err.println("ERROR:" + e);
		}
		return value;
	}

	/**
	 * 获取指定URL的内容
	 * 
	 * @param url
	 * @param header
	 *            请求头名
	 * @param headerValue
	 *            请求头值
	 * @return
	 */
	public static int getBodyLength(String url, String header,
			String headerValue) {
		int value = -1;
		URL cURL = null;
		URLConnection connection = null;
		try {
			cURL = new URL(url);
			connection = cURL.openConnection();
			// 获取输出流
			connection.setDoOutput(true);
			connection.addRequestProperty(header, headerValue);
			connection
					.addRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
			connection.setConnectTimeout(5 * 1000);
			connection.setReadTimeout(10 * 1000);
			connection.connect();

			value = connection.getContentLength();
		} catch (Exception e) {
			System.err.println("ERROR:" + e);
		}
		return value;
	}

	/**
	 * 获取指定URL内容
	 * 
	 * @param url
	 * @param headerName
	 *            请求头名
	 * @param headerValue
	 *            请求头内容
	 * @return
	 */
	public static byte[] getBody(String url, String headerName,
			String headerValue) {
		byte[] value = null;
		URL cURL = null;
		URLConnection connection = null;
		InputStream is = null;
		try {
			cURL = new URL(url);
			connection = cURL.openConnection();
			// 获取输出流
			connection.setDoOutput(true);
			connection.addRequestProperty(headerName, headerValue);
			connection.addRequestProperty("Connection", "keep-alive");
			connection
					.addRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
			connection.setConnectTimeout(5 * 1000);
			connection.setReadTimeout(10 * 1000);
			connection.connect();
			int length = connection.getContentLength();
			is = connection.getInputStream();
			if (length > 0) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[2 * 1024];
				int ch;
				while ((ch = is.read(buffer)) != -1) {
					baos.write(buffer, 0, ch);
					baos.flush();
				}
				baos.close();
				value = baos.toByteArray();
			}
		} catch (Exception e) {
			System.err.println("ERROR:" + e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return value;
	}

	/**
	 * 获取指定URL内容
	 * 
	 * @param url
	 * @return
	 */
	public static byte[] getBody(String url) {
		byte[] value = null;
		URL cURL = null;
		URLConnection connection = null;
		InputStream is = null;
		try {
			cURL = new URL(url);
			connection = cURL.openConnection();
			// 获取输出流
			connection.setDoOutput(true);
			connection
					.addRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4");
			connection.addRequestProperty("Connection", "keep-alive");
			connection.setConnectTimeout(10 * 1000);
			connection.setReadTimeout(10 * 1000);
			connection.connect();
			int length = connection.getContentLength();
			is = connection.getInputStream();
			if (length > 0) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[2 * 1024];
				int ch;
				while ((ch = is.read(buffer)) != -1) {
					baos.write(buffer, 0, ch);
					baos.flush();
				}
				baos.close();
				value = baos.toByteArray();
			}
		} catch (Exception e) {
			System.err.println("ERROR:" + e);
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return value;
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
		byte[] result = null;
		URL urlO = null;
		HttpURLConnection http = null;
		InputStream is = null;
		ByteArrayOutputStream out = null;
		try {
			urlO = new URL(url);
			http = (HttpURLConnection) urlO.openConnection();
			if (null != refence) {
				http.addRequestProperty("Referer", refence);
			}
			if (null != cookie) {
				http.addRequestProperty("Cookie", cookie);
			}
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.connect();
			int code = http.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				int length = http.getContentLength();
				if (length > 0) {
					System.out.println("网页内容大小:" + length);
					out = new ByteArrayOutputStream();
					is = http.getInputStream();
					int ch;
					while ((ch = is.read()) != -1) {
						out.write(ch);
					}
					out.flush();
					result = out.toByteArray();
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != http) {
				http.disconnect();
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return result;
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
	public static HttpObject request2URL(Map<String, String> requestHeader,
			String url) {
		HttpObject object = null;
		byte[] result = null;
		URL urlO = null;
		HttpURLConnection http = null;
		InputStream is = null;
		ByteArrayOutputStream out = null;
		try {
			urlO = new URL(url);
			http = (HttpURLConnection) urlO.openConnection();
			if (null != requestHeader) {
				if (requestHeader.size() > 0) {
					Iterator<String> it = requestHeader.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						String value = requestHeader.get(key);
						http.addRequestProperty(key, value);
					}
				}
			}

			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5 * 1000);
			http.setReadTimeout(10 * 1000);
			http.connect();
			int code = http.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				object = new HttpObject();
				Map<String, List<String>> responseHeader = http
						.getHeaderFields();
				Set<String> keySet = responseHeader.keySet();
				Iterator<String> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					if (null != key) {
						List<String> valueList = responseHeader.get(key);
						for (String value : valueList) {
							object.getHeader().put(key, value);
						}
					}
				}

				int length = http.getContentLength();
				if (length > 0) {
					out = new ByteArrayOutputStream();
					is = http.getInputStream();
					int ch;
					while ((ch = is.read()) != -1) {
						out.write(ch);
					}
					out.flush();
					result = out.toByteArray();
					object.setBody(result);
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			if (null != http) {
				http.disconnect();
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return object;
	}

	/**
	 * 合并两个字节串
	 * 
	 * @param bytes1
	 *            字节串1
	 * @param bytes2
	 *            字节串2
	 * @param sizeOfBytes2
	 *            需要从 bytes2 中取出的长度
	 * @return bytes1 和 bytes2 中的前 sizeOfBytes2 个字节合并后的结果
	 */
	private static byte[] concatByteArrays(byte[] bytes1, byte[] bytes2,
			int sizeOfBytes2) {
		byte[] result = Arrays.copyOf(bytes1, (bytes1.length + sizeOfBytes2));
		System.arraycopy(bytes2, 0, result, bytes1.length, sizeOfBytes2);
		return result;
	}

	/**
	 * 获取页面的最后修改时间
	 * 
	 * @param url
	 * @return
	 */
	public static String getLastModifiedByUrl(String url) {
		String value = null;
		URL cURL = null;
		try {
			cURL = new URL(url);
			URLConnection connection = cURL.openConnection();
			// 获取输出流
			connection.setDoOutput(true);
			connection.addRequestProperty("Connection", "close");
			connection
					.addRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4");
			connection.setConnectTimeout(5 * 1000);
			connection.setReadTimeout(10 * 1000);
			connection.connect();

			String time = connection.getHeaderField("Last-Modified");
			Date date = DateUtils.parserDate(time);
			value = DateUtils
					.formatDate(date, DateUtils.FULL_STANDARD_PATTERN2);
			return value;
		} catch (Exception e) {
			System.err.println("ERROR:" + e);
		}
		return null;
	}

}
