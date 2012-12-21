package com.chinamilitary.xmlparser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;

public class XMLParser {

	
	public static void readXmlFromURL(String url,String dir) throws Exception{
		SAXReader reader = new SAXReader();
		Document document = null;
		try{
			//读取XML文件
			document = reader.read(new URL(url));
		}catch(Exception e){
			String value = HttpClientUtils.getResponseBody(url);
			if(!"".equalsIgnoreCase(value)){
				return;
			}
			if(!"".equalsIgnoreCase(value)){
				int start = value.indexOf("intro=")+6;
				int end = value.indexOf("comment");
				String tmp = value.substring(start,end);
				System.out.println("value:"+value.replace(tmp, "\"\""));
				String tmp2 = value.replace(tmp, "\"\" ");
				//读取XML文件
				document = reader.read(tmp2);
			}
		}
		Element root = document.getRootElement();// 得到根节点
//		Element ele = root.element("list");
		List list = root.elements("Image");
		ImageBean bean = null;
		String title = null;
		ImageDao dao = DAOFactory.getInstance().getImageDao();
		for(int i=0;i<list.size();i++){
			bean = new ImageBean();
			Element eles = (Element)list.get(i);
			title = eles.attributeValue("title");
			String name = eles.attributeValue("name");
			String imgurl = eles.attributeValue("imgurl");
			String httpurl = eles.attributeValue("httpurl");
			Integer id = Integer.valueOf(eles.attributeValue("id"));
			String time = eles.attributeValue("time");
			String intro = eles.attributeValue("intro");
			String commentsuburl = eles.attributeValue("commentsuburl");
			String commentshowurl = eles.attributeValue("commentshowurl");
			String link = eles.attributeValue("link");
			bean.setTitle(title);
			bean.setCommentshowurl(commentshowurl);
			bean.setCommentsuburl(commentsuburl);
			bean.setHttpUrl(httpurl);
			bean.setOrderId(id);
			bean.setImgUrl(imgurl);
			bean.setIntro(intro);
			bean.setLink(link);
			bean.setName(name);
			bean.setTime(time);
			int ids = dao.insert(bean);
//			System.out.println("imgurl:"+httpurl);
			String s_fileName = imgurl.substring(imgurl.lastIndexOf("/")+1,imgurl.length());
			String fileName = httpurl.substring(httpurl.lastIndexOf("/")+1,httpurl.length());
			s_fileName = s_fileName.replace(".", "_s.");
//			System.out.println("intro:"+intro);
//			System.out.println("fileName:"+fileName);
			IOUtil.createPicFile(imgurl, dir+fileName.replace(".", "_s."));
			IOUtil.createPicFile(httpurl, dir+fileName);
			System.out.println("添加图片数据记录，ID为："+ids);
		}
		if(title == null || title.equals(""))
			title = "readme";
		IOUtil.createFile("XMLURL:"+url+"\n", dir+title,".txt");
	}
	public static List<ImageBean> readXmlFromURL(String xmlUrl) throws Exception{
		List<ImageBean> imageList = new ArrayList<ImageBean>();
		SAXReader reader = new SAXReader();
		Document document = null;
		try{
			//读取XML文件
			document = reader.read(new URL(xmlUrl));
		}catch(Exception e){
			String value = HttpClientUtils.getResponseBody(xmlUrl);
			if("".equalsIgnoreCase(value)){
				return imageList;
			}
			if(!"".equalsIgnoreCase(value)){
				int start = value.indexOf("intro=")+6;
				int end = value.indexOf("comment");
				String tmp = value.substring(start,end-1);
				System.out.println(tmp);
				String tmp2 = value.replace(tmp, "\"\" ");
//				tmp2 = tmp2.replace("“", "'").replace("”", "'");
//				String filePath = IOUtil.createFile(new String(tmp2.getBytes("GBK"),"GBK"), "tmp", ".xml");
//				System.out.println(tmp2);
//				 读取XML字符串文件
				try{
					document = reader.read(tmp2);
				}catch(Exception ex){
					System.out.println("ex:"+ex);
				}
			}
		}
		if(null == document){
			System.out.println(">> doucment is null");
			return imageList;
		}
//		reader.read(new URL(xmlUrl));// 读取XML文件
		Element root = document.getRootElement();// 得到根节点
		List list = root.elements("Image");
		ImageBean bean = null;
		String title = null;
		for(int i=0;i<list.size();i++){
			bean = new ImageBean();
			Element eles = (Element)list.get(i);
			title = eles.attributeValue("title");
			String name = eles.attributeValue("name");
			String imgurl = eles.attributeValue("imgurl");
			String httpurl = eles.attributeValue("httpurl");
			Integer id = Integer.valueOf(eles.attributeValue("id"));
			String time = eles.attributeValue("time");
			String intro = eles.attributeValue("intro");
			String commentsuburl = eles.attributeValue("commentsuburl");
			String commentshowurl = eles.attributeValue("commentshowurl");
			String link = eles.attributeValue("link");
			bean.setTitle(title);
			bean.setCommentshowurl(commentshowurl);
			bean.setCommentsuburl(commentsuburl);
			bean.setHttpUrl(httpurl);
			bean.setOrderId(id);
			bean.setImgUrl(imgurl);
			bean.setIntro(intro);
			bean.setLink("NED");
			bean.setName(name);
			bean.setTime(time);
			bean.setReferer(link);
			imageList.add(bean);
		}
		return imageList;
	}
	
	public static void readXmlFromURL(String linkUrl,String xmlUrl,String dir) throws Exception{
		SAXReader reader = new SAXReader();
		Document document = null;
		try{
			//读取XML文件
			document = reader.read(new URL(xmlUrl));
		}catch(Exception e){
			String value = HttpClientUtils.getResponseBody(xmlUrl);
			if(!"".equalsIgnoreCase(value)){
				return;
			}
			if(!"".equalsIgnoreCase(value)){
				int start = value.indexOf("intro=")+6;
				int end = value.indexOf("comment");
				String tmp = value.substring(start,end);
				System.out.println("value:"+value.replace(tmp, "\"\""));
				String tmp2 = value.replace(tmp, "\"\" ");
				//读取XML文件
				document = reader.read(tmp2);
			}
		}
		Element root = document.getRootElement();// 得到根节点
//		Element ele = root.element("list");
		List list = root.elements("Image");
		ImageBean bean = null;
		String title = null;
		ImageDao dao = DAOFactory.getInstance().getImageDao();
		for(int i=0;i<list.size();i++){
			bean = new ImageBean();
			Element eles = (Element)list.get(i);
			title = eles.attributeValue("title");
			String name = eles.attributeValue("name");
			String imgurl = eles.attributeValue("imgurl");
			String httpurl = eles.attributeValue("httpurl");
			Integer id = Integer.valueOf(eles.attributeValue("id"));
			String time = eles.attributeValue("time");
			String intro = eles.attributeValue("intro");
			String commentsuburl = eles.attributeValue("commentsuburl");
			String commentshowurl = eles.attributeValue("commentshowurl");
			String link = eles.attributeValue("link");
			bean.setTitle(title);
			bean.setCommentshowurl(commentshowurl);
			bean.setCommentsuburl(commentsuburl);
			bean.setHttpUrl(httpurl);
			bean.setOrderId(id);
			bean.setImgUrl(imgurl);
			bean.setIntro(intro);
			bean.setLink(link);
			bean.setName(name);
			bean.setTime(time);
			int ids = dao.insert(bean);
			System.out.println("添加图片数据记录，ID为："+ids);
			if( ids > 0 ){
				String s_fileName = imgurl.substring(imgurl.lastIndexOf("/")+1,imgurl.length());
				String fileName = httpurl.substring(httpurl.lastIndexOf("/")+1,httpurl.length());
				s_fileName = s_fileName.replace(".", "_s.");
				IOUtil.createPicFile(imgurl, dir+fileName.replace(".", "_s."));
				IOUtil.createPicFile(httpurl, dir+fileName);
				if(intro != null && !intro.equals(""))
					IOUtil.createFile(intro, dir+fileName.substring(0, fileName.lastIndexOf(".")-1),".txt");
				if(title == null || title.equals(""))
					title = "readme";
				IOUtil.createFile("LINK地址"+linkUrl+"\nXML地址:"+xmlUrl,dir+title,".txt");
			}
		}
	}
	
	public static void main(String args[]){
		try{
//			XMLParser.readXmlFromURL("http://tuku.game.china.com/game/html/2010-08-23/149750.xml","F:\\china\\military\\pic\\");
			readXmlFromURL("http://tuku.game.china.com/game/html/2010-08-23/149750.xml");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
