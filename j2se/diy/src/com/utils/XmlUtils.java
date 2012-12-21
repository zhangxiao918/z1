package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
/**
 * XML读取
 * @author 蒋德华
 *
 */
public class XmlUtils {
	private static final Logger logger = Logger.getLogger(XmlUtils.class);

	// 获取document对象实例
	public static Document getInstance(String xmlPath) {
		Document doc = null;
		try {
			InputStreamReader isr = null;
			BufferedReader in = null;
			isr = new InputStreamReader(new FileInputStream(new File(xmlPath)), "UTF-8");
			in = new BufferedReader(isr);
			BufferedReader bf = new BufferedReader(in);
			DOMParser ps = new DOMParser();// xml解析器
			ps.parse(new InputSource(bf));// 解析xml
			doc = ps.getDocument();// 获得Document对象
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("xml parse error");
		}
		return doc;
	}

	// 获取节点值 只有一层
	public static String getNodeValue(Document dc, String codename) {
		NodeList nodelist = dc.getElementsByTagName(codename);
		if (nodelist != null) {
			Node curNode = nodelist.item(0);
			if (curNode != null) {
				return curNode.getTextContent();
			}
		}
		return null;
	}
}
