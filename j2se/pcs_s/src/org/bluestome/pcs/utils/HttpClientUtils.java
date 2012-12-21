package org.bluestome.pcs.utils;

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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
	
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	//HTTP响应头中的文件大小描述
	public static String CONTENTLENGTH = "Content-Length";
    private static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	
	public static boolean validationURL(String url) {
		boolean success = false;
		URL sUrl = null;
		HttpURLConnection conn = null;
		try {
			sUrl = new URL(url);
			conn = (HttpURLConnection)sUrl.openConnection();
			conn.setConnectTimeout(5*1000);
			conn.addRequestProperty("Cache-Control", "no-cache");
			conn.addRequestProperty("Connection", "keep-alive");
			conn.setReadTimeout(15*1000);
			conn.connect();
			long ss = System.currentTimeMillis();
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
				logger.debug("\t>>>>> 获取状态码耗时:[" + (System.currentTimeMillis()-ss)+"]ms");
				success = true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(null != conn){
				conn.disconnect();
				conn = null;
			}
			if(null != sUrl){
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
			conn = (HttpURLConnection)sUrl.openConnection();
			conn.setConnectTimeout(5*1000);
			conn.addRequestProperty("Cache-Control", "no-cache");
			conn.addRequestProperty("Connection", "keep-alive");
			conn.setReadTimeout(15*1000);
			conn.connect();
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
				success = true;
			}
		} catch (Exception e) {
		} finally {
			if(null != conn){
				conn.disconnect();
				conn = null;
			}
			if(null != sUrl){
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
			http = (HttpURLConnection)urlO.openConnection();
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5*1000);
			http.setReadTimeout(15*1000);
			http.connect();
			int code = http.getResponseCode();
			if(code == HttpURLConnection.HTTP_OK){
				Map<String, List<String>> responseHeader = http.getHeaderFields();
				Set<String> keySet = responseHeader.keySet();
				Iterator iterator = keySet.iterator();
				while(iterator.hasNext()){
					String key = (String)iterator.next();
					if(null != key){
						List<String> valueList = responseHeader.get(key);
						for(String value:valueList){
							result.put(key, value);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(null != http){
				http.disconnect();
			}
		}
		return result;
	}

	/**
	 * 调用HttpURLConnection获取文件大小
	 * @param url
	 * @return
	 */
	public static String getHttpConentLength(String url){
		long start = System.currentTimeMillis();
		String result = "0";
		URL urlc = null;
		HttpURLConnection conn = null;
		try{
			urlc = new URL(url);
			conn = (HttpURLConnection)urlc.openConnection();
			conn.setDoInput(true);
			conn.addRequestProperty("Cache-Control", "no-cache");
			conn.addRequestProperty("Connection", "keep-alive");
			conn.setConnectTimeout(5*1000);
			conn.setReadTimeout(15*1000);
			conn.connect();
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
				result = String.valueOf(conn.getContentLength());
			}
		}catch(Exception e){
			System.err.println(" > ERROR:"+e);
		}finally{
			logger.debug(" > 获取文件大小耗时:["+(System.currentTimeMillis()-start)+"]ms");
			if(null != conn){
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
			http = (HttpURLConnection)urlO.openConnection();
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5*1000);
			http.setReadTimeout(15*1000);
			http.connect();
			int code = http.getResponseCode();
			if(code == HttpURLConnection.HTTP_OK){
				Map<String, List<String>> responseHeader = http.getHeaderFields();
				Set<String> keySet = responseHeader.keySet();
				Iterator iterator = keySet.iterator();
				while(iterator.hasNext()){
					String key = (String)iterator.next();
					if(null != key && key.trim().length() > 0){
						if(headerName.equals(key)){
							List<String> valueList = responseHeader.get(key);
							for(String value:valueList){
								result = value;
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(null != http){
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
			http = (HttpURLConnection)urlO.openConnection();
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5*1000);
			http.setReadTimeout(15*1000);
			http.connect();
			int code = http.getResponseCode();
			if(code == HttpURLConnection.HTTP_OK){
				is = http.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int ch;
				while((ch = is.read()) != -1){
					out.write(ch);
				}
				out.flush();
				//默认使用当前系统的字符集
				result = out.toString();
				out.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(null != http){
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
	public static String getResponseBody(String url,String charset) {
		String result = null;
		URL urlO = null;
		HttpURLConnection http = null;
		InputStream is = null;
		try {
			urlO = new URL(url);
			http = (HttpURLConnection)urlO.openConnection();
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5*1000);
			http.setReadTimeout(15*1000);
			http.connect();
			int code = http.getResponseCode();
			if(code == HttpURLConnection.HTTP_OK){
				is = http.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int ch;
				while((ch = is.read()) != -1){
					out.write(ch);
				}
				out.flush();
				result = out.toString(charset);
				out.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(null != http){
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
					sb
							.append(URLEncoder.encode(noAsciiPart.toString(),
									encode));
					noAsciiPart.delete(0, noAsciiPart.length());
				}
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取指定URL的内容
	 * @param url 
	 * @param header 请求头名
	 * @param headerValue 请求头值
	 * @return
	 */
	public static int getBodyLength(String url){
		int value = -1;
		URL cURL = null;
		URLConnection connection = null;
		try{
			cURL = new URL(url);
			connection = cURL.openConnection();
			//获取输出流
			connection.setDoOutput(true);
			connection.addRequestProperty("Cache-Control", "no-cache");
			connection.addRequestProperty("Connection", "keep-alive");
			connection.setConnectTimeout(5*1000);
			connection.setReadTimeout(15*1000);
			connection.connect();
			
			value = connection.getContentLength();
		}catch(Exception e){
			System.err.println("ERROR:"+e);
		}
		return value;
	}
	
	/**
	 * 获取指定URL的内容
	 * @param url 
	 * @param header 请求头名
	 * @param headerValue 请求头值
	 * @return
	 */
	public static int getBodyLength(String url,String header,String headerValue){
		int value = -1;
		URL cURL = null;
		URLConnection connection = null;
		try{
			cURL = new URL(url);
			connection = cURL.openConnection();
			//获取输出流
			connection.setDoOutput(true);
			connection.addRequestProperty(header, headerValue);
			connection.setConnectTimeout(5*1000);
			connection.setReadTimeout(15*1000);
			connection.connect();
			
			value = connection.getContentLength();
		}catch(Exception e){
			System.err.println("ERROR:"+e);
		}
		return value;
	}
	
	/**
	 * 获取指定URL内容
	 * @param url
	 * @param headerName 请求头名
	 * @param headerValue 请求头内容
	 * @return
	 */
	public static byte[] getBody(String url,String headerName,String headerValue){
		byte[] value =null;
		URL cURL = null;
		URLConnection connection = null;
		InputStream is = null;
		try{
			cURL = new URL(url);
			connection = cURL.openConnection();
			//获取输出流
			connection.setDoOutput(true);
			connection.setConnectTimeout(5*1000);
			connection.addRequestProperty(headerName, headerValue);
			connection.setReadTimeout(15*1000);
			connection.connect();
			int length = connection.getContentLength();
			is = connection.getInputStream();
			if(length > 0){
				value = IOUtils.toByteArray(is);
			}
		}catch(Exception e){
			System.err.println("ERROR:"+e);
		}finally{
			if(null != is){
				IOUtils.closeQuietly(is);
			}
		}
		return value;
	}
	
	/**
	 * 获取指定URL内容
	 * @param url
	 * @return
	 */
	public static byte[] getBody(String url){
		byte[] value =null;
		URL cURL = null;
		URLConnection connection = null;
		InputStream is = null;
		try{
			cURL = new URL(url);
			connection = cURL.openConnection();
			//获取输出流
			connection.setDoOutput(true);
			connection.setConnectTimeout(5*1000);
//			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.0.3; de-ch; HTC Sensation Build/IML74K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
			connection.addRequestProperty("Cache-Control", "no-cache");
			connection.addRequestProperty("Connection", "keep-alive");
			int length = connection.getContentLength();
			is = connection.getInputStream();
			if(length > 0){
				value = IOUtils.toByteArray(is);
			}
		}catch(Exception e){
			System.err.println("ERROR:"+e);
		}finally{
			if(null != is){
				IOUtils.closeQuietly(is);
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
			http.setConnectTimeout(5*1000);
			http.setReadTimeout(15*1000);
			http.connect();
			long start = System.currentTimeMillis();
			int code = http.getResponseCode();
			logger.debug("\t 获取数据流耗时:"+(System.currentTimeMillis()-start));
			if (code == HttpURLConnection.HTTP_OK) {
				start = System.currentTimeMillis();
				int length = http.getContentLength();
				if (length > 0) {
					logger.debug("网页内容大小:"+length);
					out = new ByteArrayOutputStream();
					is = http.getInputStream();
					int ch;
					while ((ch = is.read()) != -1) {
						out.write(ch);
					}
					out.flush();
					result = out.toByteArray();
				}
				long spend = (System.currentTimeMillis()-start);
				logger.debug("\t 获取数据流2耗时:"+spend);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
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
	public static HttpObject request2URL(Map<String,String> requestHeader,
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
				if(requestHeader.size() > 0){
		    		Iterator it = requestHeader.keySet().iterator();
		    		while(it.hasNext()){
		    			String key = (String)it.next();
		    			String value = (String)requestHeader.get(key);
		    			http.addRequestProperty(key, value);
		    		}
				}
			}
			
			http.addRequestProperty("Cache-Control", "no-cache");
			http.addRequestProperty("Connection", "keep-alive");
			http.setConnectTimeout(5*1000);
			http.setReadTimeout(15*1000);
			http.connect();
			int code = http.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				object = new HttpObject();
				Map<String, List<String>> responseHeader = http.getHeaderFields();
				Set<String> keySet = responseHeader.keySet();
				Iterator iterator = keySet.iterator();
				while(iterator.hasNext()){
					String key = (String)iterator.next();
					if(null != key){
						List<String> valueList = responseHeader.get(key);
						for(String value:valueList){
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
			logger.error(e.getMessage());
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
     * @param bytes1       字节串1
     * @param bytes2       字节串2
     * @param sizeOfBytes2 需要从 bytes2 中取出的长度
     *
     * @return bytes1 和 bytes2 中的前 sizeOfBytes2 个字节合并后的结果
     */
    private static byte[] concatByteArrays(byte[] bytes1, byte[] bytes2, int sizeOfBytes2) {
        byte[] result = Arrays.copyOf(bytes1, (bytes1.length + sizeOfBytes2));
        System.arraycopy(bytes2, 0, result, bytes1.length, sizeOfBytes2);
        return result;
    }
    
	/**
	 * 获取页面的最后修改时间
	 * @param url
	 * @return
	 */
	public static String getLastModifiedByUrl(String url){
		String value = "1970-01-01 00:00:00";
		URL cURL = null;
		try{
			cURL = new URL(url);
			URLConnection connection = cURL.openConnection();
			//获取输出流
			connection.setDoOutput(true);
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
			connection.setConnectTimeout(5*1000);
			connection.setReadTimeout(15*1000);
			connection.connect();
			
			String time = connection.getHeaderField("Last-Modified");
			Date date = DateUtils.parserDate(time);
			value = DateUtils.formatDate(date, DateUtils.FULL_STANDARD_PATTERN2);
		}catch(Exception e){
			System.err.println("ERROR:"+e);
		}		
		return value;
	}

	/**
	 * 入口函数
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			//这个URL是一个验证码地址
//			byte[] body = getResponseBodyAsByte(null,null,"http://www.hzti.com/government/CreateCheckCode.aspx?d="+System.currentTimeMillis());
			exec.scheduleWithFixedDelay(new Runnable(){
				public void run(){
					long start = System.currentTimeMillis();
					byte[] body = getResponseBodyAsByte("http://i.autohome.com.cn/home.html",null,"http://club.autohome.com.cn/bbs/thread-c-2001-15822375-1.html");
					long spend = System.currentTimeMillis() - start;
					if(null != body){
						Parser p1 = null;
						try {
							p1 = new Parser();
							p1.setInputHTML(new String(body));
							NodeFilter filter = new NodeClassFilter(Span.class);
							NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("class", "fr fon12"));
							if(null != list && list.size() > 0){
								Span tag = (Span)list.elementAt(0);
								logger.debug("\t>>>>>> 获取页面耗时:["+spend+"]ms \t点击数:"+tag.toHtml());
							}
							list = null;
							filter = null;
						} catch (ParserException e) {
							e.printStackTrace();
						} finally{
							if(null != p1){
								p1 = null;
							}
						}
					}else{
						logger.debug("没有返回内容");
					}
				}
			}, 5*1000L,10*1000L, TimeUnit.MILLISECONDS);
			
			/**
			body = getResponseBodyAsByte(null, null, "http://www.hzti.com/service/qry/violation_veh.aspx?type=2&node=249");
			if(null != body){
				String print = ByteUtils.bytesAsHexString(body, Short.MAX_VALUE);
				logger.debug("页面内容大小:"+print);
				logger.debug("页面内容大小:"+body.length);
			}else{
				logger.debug("没有返回内容");
			}
			**/
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
