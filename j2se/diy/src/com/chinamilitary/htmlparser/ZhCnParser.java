package com.chinamilitary.htmlparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.LinkBean;

public class ZhCnParser {
	private static final String URL = "http://www.taobao.com";
	
	private static final String TUKU_URL = "http://tuku.news.china.com/history/html/";
	
	private static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();
	
	static void getLink() throws Exception{
		Parser p1 = new Parser();
		p1.setURL(URL);
		
		NodeFilter filter = new NodeClassFilter(Div.class); 
		NodeList list = p1.extractAllNodesThatMatch(filter);
		LinkBean bean = null;
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
//				LinkTag link = (LinkTag)list.elementAt(i);
//				System.out.println("超连接地址:"+link.getLink());
				Div div = (Div)list.elementAt(i);
				System.out.println("DIV内容块:"+div.toHtml());
//				if(link.getLink().startsWith(TUKU_URL)){
//					System.out.println("连接地址:"+link.getLink());
//					bean = new LinkBean();
//					bean.setLink(link.getLink());
//					bean.setName(link.getLinkText());
//					LINKLIST.add(bean);
//				}
			}
		}
		p1 = null;
	}
	
	static void getCategoryList() throws Exception{
		Parser p1 = new Parser();
		p1.setURL("http://www.taobao.com");
		
		NodeFilter filter = new NodeClassFilter(Div.class); 
		NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("id","category")); //category category-list
		
		if(list != null){
			System.out.println("list.size():"+list.size());
			if(list.size()> 0 ){
				//DIV块
				for(int i=0;i<list.size();i++){
					Div div = (Div)list.elementAt(i);
					p1.setInputHTML(div.toHtml());
					System.out.println("div.toHtml():"+div.toHtml());
					filter = new NodeClassFilter(Div.class); 
					NodeList divlist = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("class","category-list"));
//					System.out.println("DIV数量:"+divlist.size());
					
					//分级目录
					for(int j=1;j<divlist.size();j++){
//						System.out.println("目录级别:"+j);
						Div div1 = (Div)divlist.elementAt(j);
						p1.setInputHTML(div1.toHtml());
						
						filter = new NodeClassFilter(LinkTag.class); 
						NodeList linkList = p1.extractAllNodesThatMatch(filter);
//						System.out.println("超连接数量:"+linkList.size());
						//分级目录下的类别
						for(int k=0;k<linkList.size();k++){
							LinkTag link = (LinkTag)linkList.elementAt(k);
							System.out.println("第"+j+"级"+"\t"+link.getLinkText());
							System.out.println("连接地址:\t"+link.getLink());
							System.out.println();
						}
					}
				}
			}
		}
	}
	static List<LinkBean> getLinkList(){
		return LINKLIST;
	}
	
	static void add(LinkBean bean){
		LINKLIST.add(bean);
	}
	
	static void clear(){
		if(LINKLIST.size() > 0){
			LINKLIST.clear();
		}
	}
	
	
	public static void main(String args[]){
		try{
//			getLink();
			getCategoryList();
//			if(LINKLIST.size() > 0){
//				for(int i=0;i<LINKLIST.size();i++){
//					LinkBean bean = (LinkBean)LINKLIST.get(i);
//					System.out.println("link:"+bean.getLink()+"\tname:"+bean.getName());
//					HTMLParser.test10(bean.getLink(),"H:\\china\\military\\pic\\history\\"+bean.getName()+"\\");
//				}
//			}
			
			File file = new File("sfs");
			file.listFiles();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
