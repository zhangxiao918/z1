package com.chinamilitary.htmlparser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.util.IOUtil;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

public class CommonParaser {

	private static HashMap<String,LinkBean> LINKHASH = new HashMap<String,LinkBean>();
	
	static String SAVE_DIR = "G:\\x201i\\wallpaper";
	
	static String TMP_URL = "http://www-06.ibm.com/jp/pc/tplife/wp/";
	
	static String FILE_EXTENDS = ".jpg";
	
	public HashMap<String, LinkBean> getDiv(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, LinkBean> getDiv(String url, String attribute,
			String value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	static void getLink(String url) throws Exception {
		Parser p1 = new Parser();
		p1.setURL(url);
		p1.setEncoding("GB2312");
		
		NodeFilter filter = new NodeClassFilter(LinkTag.class); 
		NodeList list = p1.extractAllNodesThatMatch(filter);
		LinkBean bean = null;
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				LinkTag link = (LinkTag)list.elementAt(i);
				bean = new LinkBean();
				if(link.getLink().toLowerCase().endsWith(FILE_EXTENDS)
						&& link.getLink().startsWith(TMP_URL)){
					System.out.println("地址:"+link.getLink());
					bean = new LinkBean();
					bean.setLink(link.getLink());
					bean.setName(link.getLinkText());
					LINKHASH.put(link.getLink(), bean);
				}
			}
		}
		p1 = null;
	}

	public HashMap<String, LinkBean> getLink(String url, String attribute,
			String value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String args[]){
		try{
		getLink("http://www.jpeg.org.cn/thinkpad/");
		
		if(LINKHASH.size() > 0){
			System.out.println("已解析地址数量:"+LINKHASH.size());
			Set<String> key = LINKHASH.keySet();
			Iterator<String> it = key.iterator();
			while(it.hasNext()){
				LinkBean bean = (LinkBean)LINKHASH.get(it.next());
				try{
				String name = bean.getLink().substring(bean.getLink().lastIndexOf("/")+1);
				String subDir = bean.getName().replaceAll(",", "");
				System.out.println("文件名："+name);
				System.out.println("目录名:"+subDir);
				IOUtil.createPicFile(bean.getLink(),SAVE_DIR+File.separator+subDir+File.separator+name);
				}catch(Exception e){
					System.out.println(e.getMessage()+"\t连接异常:"+bean.getLink());
					continue;
				}
			}
			
			LINKHASH.clear();	
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
