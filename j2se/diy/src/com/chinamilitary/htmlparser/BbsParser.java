package com.chinamilitary.htmlparser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.util.CommonUtil;

public class BbsParser{
	private static final String URL = "http://military.china.com/bbs2/";
	
	private static final String TUKU_URL = "http://military.china.com/zh_cn/bbs2/";
	
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
				bean = new LinkBean();
				if(link.getLink().startsWith(TUKU_URL)){
					bean = new LinkBean();
					bean.setLink(link.getLink());
					bean.setName(link.getLinkText());
					add(link.getLink(), bean);
				}
			}
		}
		p1 = null;
	}
	
	static void parserHTML(String link) throws Exception{
		//是否存在分页
		if(isPage(link)){
			System.out.print("需要解析分页");
			System.out.println("地址:"+link);
//			parserHTML(link);
		}else{
			System.out.print("不需要解析的页面地址为");
			System.out.println(link);
		}
//		System.out.println("解析页面");
		
	}
	
	static boolean isPage(String link) throws Exception{
		boolean b = false;
		Parser p1 = new Parser();
		p1.setURL(link);
		NodeFilter filter = new NodeClassFilter(Div.class); 
		NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("id","chan_multipageNumN"));
		if(list != null){
			
			b = true;
		}
		return b;
	}
	
	static HashMap<String,LinkBean> getLINKHASH(){
		return LINKHASH;
	}
	
	static void add(String key,LinkBean bean){
		LINKHASH.put(key,bean);
	}
	
	static void clear(){
		if(LINKHASH.size() > 0){
			LINKHASH.clear();
		}
	}
	
	public static void main(String args[]){
		try{
			getLink();
			
			if(LINKHASH.size() > 0){
				
				Set<String> key = LINKHASH.keySet();
				Iterator<String> it = key.iterator();
				while(it.hasNext()){
					LinkBean bean = (LinkBean)LINKHASH.get(it.next());
					System.out.println(bean.getName());
					parserHTML(bean.getLink());
				}
				clear();
			    	
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
