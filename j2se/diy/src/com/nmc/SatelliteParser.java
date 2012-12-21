package com.nmc;

import java.util.Date;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinamilitary.util.DateUtils;
import com.chinamilitary.util.HttpClientUtils;

public class SatelliteParser {

	static String mURL = "http://www.nmc.gov.cn/publish/satellite/fy2.htm";
	// 卫星云图图片地址前缀
	final static String PREFIX_SATELINE_CLOUD_IMG_URL = "http://image.weather.gov.cn/";
	static Logger logger = LoggerFactory.getLogger(SatelliteParser.class);

	/**
	 * 获取分类链接
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void catalog(String url) throws Exception { // WebsiteBean bean
		Parser parser = new Parser();
		byte[] body = HttpClientUtils.getBody(mURL);
		if (null == body || body.length == 0) {
			return;
		}
		String html = new String(body, "GB2312");
		parser.setInputHTML(html);
		parser.setEncoding("GB2312");

		NodeFilter fileter = new NodeClassFilter(CompositeTag.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "mycarousel")); // id

		if (null != list && list.size() > 0) {
			CompositeTag div = (CompositeTag) list.elementAt(0);
			parser = new Parser();
			parser.setInputHTML(div.toHtml());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = parser.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					String str = link.getLink().replace("view_text_img(", "")
							.replace(")", "").replace("'", "");
					if (null != str && str.length() > 0) {
						String[] tmps = str.split(",");
						logger.info("\t 字符串长度:" + tmps.length);
						int k = 0;
						for (String st : tmps) {
							logger.info(k + "=" + st);
							k++;
						}
						/**
						 * 0:小图 1:大图
						 */
						String name = analysisURL(tmps[0]);
						logger.info("\tzhang:" + name);
						String date = analysisURL2(name);
						logger.info("\tzhang:" + date);
						body = HttpClientUtils
								.getBody(PREFIX_SATELINE_CLOUD_IMG_URL
										+ tmps[0]);
						logger.info("\t小图[" + name + "],时间[" + date + "],大小:"
								+ body.length);
						name = analysisURL(tmps[1]);
						date = analysisURL2(name);
						body = HttpClientUtils
								.getBody(PREFIX_SATELINE_CLOUD_IMG_URL
										+ tmps[1]);
						if (null != body) {
							logger.info("\t大图[" + name + "],时间[" + date
									+ "],大小:" + body.length);
						}
					}
					logger.info("\r\n");
				}
			}
		}
		if (null != parser)
			parser = null;
	}

	/**
	 * 分析URL,获取图片文件名
	 * 
	 * @param url
	 * @return
	 */
	private static String analysisURL(String url) {
		int s = url.lastIndexOf("/");
		String name = url.substring(s + 1, url.length());
		if (null == name || name.equals("")) {
			name = String.valueOf(System.currentTimeMillis());
		}
		return name;
	}

	/**
	 * 从文件名中分析出时间信息
	 */
	private static String analysisURL2(String name) {
		String date = DateUtils.formatDate(new Date(),
				DateUtils.DEFAULT_PATTERN);
		if (null != name && name.length() > 0 && !name.equals("")) {
			String[] tmps = name.substring(0, name.lastIndexOf(".")).split("_");
			if (tmps.length > 8) {
				date = tmps[8];
			}
		}
		if (null != date && date.length() > 8 && !date.equals("")) {
			date = date.substring(0, 8);
		}
		return date;
	}

	public static void main(String args[]) {
		try {
			catalog(mURL);
			String lastModify = HttpClientUtils.getLastModifiedByUrl(mURL);
			System.out.println("" + lastModify);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
