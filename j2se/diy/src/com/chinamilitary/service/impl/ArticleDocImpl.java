package com.chinamilitary.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.ArticleDoc;
import com.chinamilitary.dao.ArticleDocDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitray.service.IArticleDocService;

public class ArticleDocImpl implements IArticleDocService {

	Log logger = LogFactory.getLog(ArticleDocImpl.class);
	private ArticleDocDao articleDocDao = DAOFactory.getInstance().getArticleDocDao();	
	private MemcacheClient client = MemcacheClient.getInstance();
	String PCPOP="WEB_PCPOP";
	
	public boolean add(ArticleDoc doc){
		int id = -1;
		try {
			if(null == client.get(getKey(doc.getUrl()))){
				id = articleDocDao.insert(doc);
				if(id > 0){
					doc.setId(id);
					doc.setStatus(1);
					client.add(getKey(doc.getUrl()), doc);
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public void processAuthor() throws Exception {
		List<ArticleDoc> list = articleDocDao.findAll(1,1310);
		for(ArticleDoc bean : list){
			
			if(!bean.getUrl().startsWith("http://www.pcpop.com/doc/")){
				continue;
			}
			//更新文章的作者和发布时间
			try{
				String author = author(bean.getUrl());
				String tmp1 = author.substring(author.lastIndexOf("作者")+3,author.lastIndexOf("编辑")-1);
				String tmp2 = author.substring(0,author.indexOf(":")-2);
				if(null == tmp1 || null ==  tmp2){
					break;
				}
				bean.setAuthor(tmp1);
				bean.setPublishTime(tmp2);
				bean.setStatus(2);
				if(!articleDocDao.update(bean)){
					logger.error("更新作者失败:"+bean.getUrl());
				}else{
					System.out.println("["+bean.getId()+"]更新成功:"+bean.getUrl());
				}
			}catch(java.io.FileNotFoundException e){
				bean.setStatus(10);
				try {
					if(!articleDocDao.update(bean)){
						logger.error("更新作者失败:"+bean.getUrl());
					}else{
						logger.error("["+bean.getId()+"]更新记录状态为10[文件或地址查找找不到]:"+bean.getUrl());
					}
				} catch (Exception e1) {
					logger.error(" >> exception:" + e1);
				}
				continue;
			}catch(org.htmlparser.util.ParserException e){
				bean.setStatus(10);
				try {
					if(!articleDocDao.update(bean)){
						logger.error("更新作者失败:"+bean.getUrl());
					}else{
						logger.error("["+bean.getId()+"]更新记录状态为10[URL解析失败]:"+bean.getUrl());
					}
				} catch (Exception e1) {
					logger.error(" >> exception:" + e1);
				}
				continue;
			}catch(Exception e){
				bean.setStatus(11);
				bean.setContent(e.getMessage());
				try {
					if(!articleDocDao.update(bean)){
						logger.error("更新作者和文章发布时间失败:"+bean.getUrl());
					}else{
						logger.error("["+bean.getId()+"]更新记录状态为11[其他异常情况]:"+bean.getUrl());
					}
				} catch (Exception e1) {
					logger.error(" >> exception:" + e1);
				}
				continue;
			}
		}
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private String author(String url) throws Exception{
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");

		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
				new HasAttributeFilter("class", "otb14"));
		String author = null;
		if (list != null && list.size() > 0) {
			Div div = (Div)list.elementAt(0);
			String tmp = div.getStringText();
			author = tmp;
		}
		
		if(null == author){
			parser = new Parser();
			parser.setURL(url);
			parser.setEncoding("GB2312");
			
			NodeFilter fileter1 = new NodeClassFilter(Div.class);
			NodeList list1 = parser.extractAllNodesThatMatch(fileter1).extractAllNodesThatMatch(
					new HasAttributeFilter("class", "pop_2_1_2"));
			if(null != list1 && list1.size() > 0){
				Div div = (Div)list1.elementAt(1);
				String tmp = div.getStringText();
				author = tmp.substring(tmp.indexOf("</a>")+4);
				System.out.println("author:"+author);
			}
		}
		return author;
	}
	
	private String getKey(String url){
		return PCPOP+":"+url;
	}

	
}
