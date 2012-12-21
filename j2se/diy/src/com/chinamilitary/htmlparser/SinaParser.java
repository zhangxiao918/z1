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
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.ArticleDoc;
import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDocDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;

public class SinaParser {
	
	private static final String URL = "http://www.sina.com.cn";

	private static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();
	
	private static Integer D_PARENT_ID = 54;
	static ArticleDocDao articleDocDao = DAOFactory.getInstance().getArticleDocDao();	

	/**
	 * 目录
	 * 
	 * @throws Exception
	 */
	static void catelogy(WebsiteBean bean) throws Exception {
		WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
		Parser parser = new Parser();
		parser.setURL(bean.getUrl());

		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "blk_lmdh_01")); //id blk_lmdh_01 "class", "nav"
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p1.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					if(link.getLink().startsWith(bean.getUrl())){
						System.out.println(link.getLinkText());
						System.out.println(link.getLink() + "\n");
						tmp = new WebsiteBean();
						tmp.setName(link.getLinkText());
						tmp.setUrl(link.getLink());
						tmp.setParentId(bean.getId());
						boolean b =dao.insert(tmp);
						if(b){
							System.out.println("成功");
						}else{
							System.out.println("失败");
						}
					}
				}
			}
		}
	}

	/**
	 * 新闻链接和名称
	 * 
	 * @throws Exception
	 */
	static void news() throws Exception {
		Parser parser = new Parser();
		parser.setURL(URL);

		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(new HasAttributeFilter("id", "news"));
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p1.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					if (link.getLink().startsWith("http://news.sina.com.cn")) {
						System.out.println(link.getLinkText());
						System.out.println(link.getLink() + "\n");
					}
				}
			}
		}
	}

	static void news(String url,String encoding) throws Exception{
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding(encoding);
		
		NodeFilter fileter = new NodeClassFilter(LinkTag.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter);
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p1.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					LinkBean bean = null;
					if (link.getLink().toLowerCase().startsWith(url) && !link.getLink().equalsIgnoreCase(url) 
							&& link.getLink().endsWith(".html")
							&& !link.getLink().startsWith("http://v.sina.com.cn")
							&& !link.getLinkText().equalsIgnoreCase("")){
						bean = new LinkBean();
						bean.setLink(link.getLink());
						bean.setName(link.getLinkText());
						LINKHASH.put(link.getLink(), bean);
					}
				}
			}
		}
	}
	
	static String author(String url,String encoding) throws Exception{
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding(encoding);

		NodeFilter fileter = new NodeClassFilter(Span.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
				new HasAttributeFilter("id", "media_name"));
		String author = null;
		if (list != null && list.size() > 0) {
			Span div = (Span)list.elementAt(0);
			String tmp = div.getStringText();
			author = tmp;
		}
		
		if(null == author){
//			System.out.println("重新解析作者栏");
			parser = new Parser();
			parser.setURL(url);
			parser.setEncoding("UTF-8");
			
			NodeFilter fileter1 = new NodeClassFilter(Span.class);
			NodeList list1 = parser.extractAllNodesThatMatch(fileter1).extractAllNodesThatMatch(
					new HasAttributeFilter("id", "media_name"));
			if(null != list1 && list1.size() > 0){
				Span div = (Span)list1.elementAt(1);
				String tmp = div.getStringText();
				author = tmp;
			}
		}
		return author;
	}
	/**
	 * 获取新闻内容
	 * @throws Exception
	 */
	static void newsContent() throws Exception {
		Parser parser = new Parser();
		parser
				.setURL("http://news.sina.com.cn/c/2010-01-25/014719536832.shtml");
		parser.setEncoding("GB2312");
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "artibody"));
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			p1.setEncoding("GB2312");
			NodeFilter divFilter = new NodeClassFilter(Div.class);
			NodeList linkList = p1.extractAllNodesThatMatch(divFilter);
			if (linkList != null && linkList.size() > 0) {
				//记者
				List<String> reportList = new ArrayList<String>();
				//回答
				List<String> answerList = new ArrayList<String>();
				for (int i = 0; i < linkList.size(); i++) {
					Div div = (Div) linkList.elementAt(i);
					String content = div.toPlainTextString().trim().replace(" ","");
					String[] sc = content.split("\n");
					for(String s:sc){
						if(null != s.trim() && !"".equals(s.trim())){
							if(s.startsWith("　　记者：")){
								reportList.add(s);
							}else if(s.startsWith("　　国务院新闻办发言人：")){
								answerList.add(s);
							}
						}
					}
				}
				
				System.out.println("记者提问列表");
				for(String rs:reportList){
					System.out.println(rs);
				}
				System.out.println("\r\n");
				System.out.println("回答列表");
				for(String rs:answerList){
					System.out.println(rs);
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// catelogy();
			// news();
//			newsContent();
			WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
//			try {
//				catelogy(dao.findById(55));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
//			List<WebsiteBean>  webList = dao.findByParentId(54);
//			for(WebsiteBean bean:webList){
//				try{
//					news(bean.getUrl(),"UTF-8");
//				}catch(org.htmlparser.util.EncodingChangeException e){
//					System.out.println("编码失败，使用GB2312编码");
//					news(bean.getUrl(),"GB2312");
//				}
//				Iterator it = LINKHASH.keySet().iterator();
//				ArticleDoc doc = null;
//				while (it.hasNext()) {
//					doc = new ArticleDoc();
//					String key = (String) it.next();
//					LinkBean link = (LinkBean) LINKHASH.get(key);
//					doc.setTitle(link.getName());
//					doc.setUrl(link.getLink());
//					doc.setWebId(bean.getId());
//					int id = articleDocDao.insert(doc);
//					if(!(id>0)){
//						System.out.println("失败，\t链接名称：" + link.getName() + "\n链接地址："+ link.getLink());
//					}
//				}
//				LINKHASH.clear();
//			}
			newsContent();
//			System.out.println(author("http://ent.sina.com.cn/s/m/2010-06-18/06132990753.shtml","GB2312"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}
	}

}
