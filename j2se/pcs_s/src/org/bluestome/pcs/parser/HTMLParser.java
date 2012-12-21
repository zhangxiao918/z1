package org.bluestome.pcs.parser;

import java.util.ArrayList;
import java.util.List;

import org.bluestome.pcs.bean.LinkBean;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;

public class HTMLParser {

	private static List<LinkBean> LinkList = new ArrayList<LinkBean>();
	
	/**
	 * 解析首页得到图片页面地址
	 */
	public static void test6(){
		StringBuffer sb = new StringBuffer();
		final String url = "http://tuku.military.china.com/military/";
		LinkBean linkBean = null;
		try{
			Parser p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("UTF-8");
			//获取指定ID的DIV内容
			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("class","ImgTitle"));
			if(list != null){
				for(int i=0;i<list.size();i++){
					Div div = (Div)list.elementAt(i);
					if(div != null){
						NodeList nodeList = div.getChildren();
						LinkTag link = (LinkTag)nodeList.elementAt(0);
						if(link != null){
							linkBean = new LinkBean();
							String name1 = link.getLinkText();
							String link1 = link.getLink();
							linkBean.setName(name1);
							linkBean.setLink(getRealUrl(link1));

							LinkList.add(linkBean);
						}	
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析得到真实的页面地址
	 * @param link
	 * @return
	 * @throws Exception
	 */
	public static String getRealUrl(String link) throws Exception{
        Parser parser=new Parser(link); 
        parser.setEncoding("utf-8"); 
        NodeList nodelist=parser.extractAllNodesThatMatch(new NodeFilter(){
			public boolean accept(Node arg0) {
				// TODO Auto-generated method stub
				return true;
			}
			
		}).extractAllNodesThatMatch(new NodeClassFilter(ScriptTag.class)); 		
		String content = nodelist.toHtml();
		String url = content.substring(content.indexOf("\"")+1,content.lastIndexOf("\""));
		return url;
	}

	/**
	 * 得到XML地址
	 * @param link
	 * @return
	 * @throws Exception
	 */
	public static String getXmlUrl(String link) throws Exception{
        Parser parser=new Parser(link); 
		String xmlUrl = null;
        parser.setEncoding("UTF-8"); 
		NodeFilter filter =  new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("class","PicShowBox"));
		if(list != null){
			String content = list.toHtml();
			int start = content.indexOf("&xmlpath=");
			int end = content.indexOf("&curid");
			if(start > 0 && end > 0 ){
				xmlUrl = content.substring(start+9,end);
			}else{ 
				//使用新的解析XML方法获取XML
				xmlUrl = getXmlUrl2(link);
			}
			if(!xmlUrl.toLowerCase().endsWith(".xml") || xmlUrl == null){
				return null;
			}
		}
		return xmlUrl;
	}
	
	/**
	 * 得到XML地址
	 * @param link
	 * @return
	 * @throws Exception
	 */
	public static String getXmlUrl2(String link) throws Exception{
        Parser parser=new Parser(link); 
//        parser.setEncoding("GB2312"); 
        parser.setEncoding("utf-8"); 
        NodeList nodelist=parser.extractAllNodesThatMatch(new NodeFilter(){
			public boolean accept(Node arg0) {
				// TODO Auto-generated method stub
				return true;
			}
			
		}).extractAllNodesThatMatch(new NodeClassFilter(ScriptTag.class)); 		
		String content = nodelist.toHtml();
		String url = content.substring(content.indexOf("\"")+1,content.lastIndexOf("\""));
		String xmlUrl = null;
		if(url != null){
	    	int start = url.indexOf("getmessage(\"");
	    	int end = url.indexOf(".xml\");");
	    	xmlUrl = url.substring(start+12, end+4);
	    	if(!xmlUrl.toLowerCase().endsWith(".xml")){
	    		return null;
	    	}
		}
		return xmlUrl;
	}
	
	public static List getLinkList(){
		return LinkList;
	}

	public static void clear(){
		System.out.println("清除List中的内容");
		LinkList.clear();
	}
}
