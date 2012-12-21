package org.bluestome.pcs.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bluestome.pcs.bean.ImageBean;
import org.bluestome.pcs.utils.HttpClientUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLParser {

	
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
	
}
