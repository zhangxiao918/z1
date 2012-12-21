package com.chinamilitary.htmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.ContentTypeBean;
import com.chinamilitary.memcache.MemcacheClient;

public class ContentTypePageParser {

	static final String URL = "http://reliableanswers.com/contenttype/ctype.asp";

	static HashMap<String,String> PAGE_HASH = new HashMap<String,String>();
	
	static MemcacheClient client = MemcacheClient.getInstance();

	/**
	 * 根据URL获取内容
	 * @param url
	 * @return
	 * @throws Exception
	 */
	static String content(String url) throws Exception{
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("UTF-8");

		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
				new HasAttributeFilter("id", "column2"));
		String content = null;
		if(null != list && list.size() > 0){
			Div div = (Div)list.elementAt(0);
			String tmp = div.getStringText();
			content = tmp;
		}
		return content;
	}
	
	static void getPageLink(String url) throws Exception{
		try{
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("UTF-8");
		
		NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
		NodeList linkList = parser.extractAllNodesThatMatch(linkFilter);
		if(null != linkList && linkList.size() > 0){
			for(int i=0;i<linkList.size();i++){
				LinkTag link = (LinkTag)linkList.elementAt(i);
				String tmpUrl = link.getLink().substring(0,link.getLink().indexOf("&"));
				System.out.println("tmpUrl:"+tmpUrl);
				if(null == client.get(tmpUrl)){
					if(tmpUrl.startsWith("http://reliableanswers.com/contenttype/ctype.asp?page=")){
						String tmp = content(tmpUrl);
						System.out.println(tmpUrl+"的页面大小:"+tmp.getBytes().length);
						PAGE_HASH.put(tmpUrl, tmp);
						client.add(tmpUrl, tmpUrl);
						getPageLink(tmpUrl);
					}
				}else{
					System.out.println("缓存中已存在相同连接["+tmpUrl+"]");
				}
			}
		}
		}catch(Exception e){
			System.out.println("解析【"+url+"】失败");
		}
	}
	
	static List<ContentTypeBean> getContentDiv(String content){
		List<ContentTypeBean> list = new ArrayList<ContentTypeBean>();
		try{
			Parser parser = new Parser();
			parser.setInputHTML(content);
			
			NodeFilter table = new NodeClassFilter(TableTag.class);
			NodeList tableList = parser.extractAllNodesThatMatch(table);
			
			if(null != tableList && tableList.size() > 0){
				try{
					TableTag tableTag = (TableTag)tableList.elementAt(1);
					TableRow [] tr=tableTag.getRows();
					//得到该table所有的tr
					ContentTypeBean bean = null;
//					遍历所有tr
					for(TableRow r:tr){
						TableColumn [] tc=r.getColumns();
						if(!tc[0].toPlainTextString().contentEquals("Class") 
									&& !tc[1].toPlainTextString().contentEquals("Content Type") 
									&& !tc[2].toPlainTextString().contentEquals("Submissions")){
							bean = new ContentTypeBean();
							bean.setExt(tc[0].toPlainTextString());
							bean.setContentType(tc[1].toPlainTextString());
							bean.setSubMissions(tc[2].toPlainTextString());
							list.add(bean);
						}
//						遍历所有的td
//						for(TableColumn c:tc){
//							System.out.print(c.toPlainTextString());
//						}
					}
				}catch(Exception e){
					System.out.println("数组不越界");
					return null;
				}
			}
		}catch(Exception e){
		
			return null;
		}
		return list;
	}
	
	public static void main(String args[]){
		try{
			getPageLink(URL);
			Iterator it = PAGE_HASH.keySet().iterator();
			while(it.hasNext()){
				String url = (String)it.next();
				String tmp = PAGE_HASH.get(url);
				if(null != tmp){
					List<ContentTypeBean> list = getContentDiv(tmp);
					System.out.println("列表大小："+list.size());
					for(ContentTypeBean bean:list){
						System.out.println("Ext:"+bean.getExt());
						System.out.println("ContentType:"+bean.getContentType());
						System.out.println("subMissions:"+bean.getSubMissions());
					}
					PAGE_HASH.remove(url);
					client.remove(url);
				}
			}
			PAGE_HASH.clear();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
