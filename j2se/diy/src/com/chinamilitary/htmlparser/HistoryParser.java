package com.chinamilitary.htmlparser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
//import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.util.CommonUtil;

public class HistoryParser {

	private static final String URL = "http://tuku.news.china.com/history/";
	
	private static final String TUKU_URL = "http://tuku.news.china.com/history/html/20";
	
	private static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();
	
	private static HashMap<String,LinkBean> LINKHASH = new HashMap<String,LinkBean>();
	
	static void getLink() throws Exception{
		Parser p1 = new Parser();
		p1.setURL(URL);
		
		NodeFilter filter = new NodeClassFilter(LinkTag.class); 
		NodeList list = p1.extractAllNodesThatMatch(filter);
		LinkBean bean = null;
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				LinkTag link = (LinkTag)list.elementAt(i);
				if(link.getLink().startsWith(TUKU_URL)){
					bean = new LinkBean();
					bean.setLink(link.getLink());
					String name = link.getLinkText();
					//判断连接中是否存在创建文件夹时的非法字符
					if(name.indexOf("“") != -1 && name.indexOf("”") != -1){
						name = name.replaceAll("“", "");
						name = name.replace("”", "");
					}
					//判断连接中是否存在创建文件夹时的非法字符
					if(name.indexOf("\"") != -1 && name.indexOf("\"") != -1){
						name = name.replace("\"", "");
					}
					bean.setName(name);
					LINKHASH.put(link.getLink(), bean);
				}
			}
		}
		p1 = null;
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
		if(LINKHASH.size() > 0){
			LINKHASH.clear();
		}
	}
	
	
	public static void main(String args[]){
		try{
			getLink();
			if(LINKLIST.size() > 0){
				System.out.println("LINKLIST.size():"+LINKLIST.size());
//				for(int i=0;i<LINKLIST.size();i++){
//					LinkBean bean = (LinkBean)LINKLIST.get(i);
//					System.out.println("link:"+bean.getLink()+"\tname:"+bean.getName());
//					HTMLParser.test10(bean.getLink(),"H:\\china\\military\\pic\\history\\"+CommonUtil.getDate("")+File.separator+bean.getName()+File.separator);
//				}
			}
			if(LINKHASH.size() > 0){
				System.out.println("LINKHASH.size():"+LINKHASH.size());
				Set<String> key = LINKHASH.keySet();
				Iterator<String> it = key.iterator();
				while(it.hasNext()){
					LinkBean bean = (LinkBean)LINKHASH.get(it.next());
					System.out.println("name:"+bean.getName());
					HTMLParser.test10(bean.getLink(),"H:\\china\\military\\pic\\history\\"+CommonUtil.getDate("")+File.separator+bean.getName()+File.separator);
				}
				clear();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
