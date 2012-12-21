package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UrlUtils {
	protected static final Log logger = LogFactory.getLog(UrlUtils.class);

	public static String get(String url) {

		if (logger.isDebugEnabled()) {
			logger.debug("request get [" + url + "]");
		}

		if (url.endsWith("/")) {
			url = url.substring(0, url.lastIndexOf('/'));
		}

		HttpClient httpClient = new HttpClient();

		GetMethod getMethod = null;

		try {
			// 翟惠林 修改
			int index = url.indexOf("?");
			if (index > 0) {
				String burl = url.substring(0, url.indexOf("?"));

				String queryString = url.substring(url.indexOf("?") + 1);
				String params[] = queryString.split("&");
				String q = "";
				for (String param : params) {
					String p[] = param.split("=");
					if (p.length > 1) {
						if (q.length() == 0) {
							q += "?" + p[0] + "="
									+ URLEncoder.encode(p[1], "UTF-8");
						} else {
							q += "&" + p[0] + "="
									+ URLEncoder.encode(p[1], "UTF-8");
						}
					}
				}
				url = burl + q;
			}

			getMethod = new GetMethod(url);

			// 执行getMethod
			logger.info("processing url [" + getMethod.getURI());
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Url [" + url + "], Method failed: "
						+ getMethod.getStatusLine());
			} else {
				// 读取内容
				ByteArrayOutputStream _ouputStream = new ByteArrayOutputStream();
				FileUtils.copyStream(getMethod.getResponseBodyAsStream(),
						_ouputStream);
				String charset = getMethod.getResponseCharSet();
				return _ouputStream.toString(charset);
			}
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			logger.error("Please check your provided http address!", e);
		} catch (IOException e) {
			// 发生网络异常
			logger.error("", e);
		} catch (Exception e) {
			// 发生其他异常
			logger.error("", e);
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}

		return "";
	}
}
