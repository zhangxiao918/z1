package org.bluestome.pcs.parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bluestome.pcs.bean.ArticleDoc;
import org.bluestome.pcs.bean.LinkBean;
import org.bluestome.pcs.bean.WebsiteBean;
import org.bluestome.pcs.common.factory.DAOFactory;
import org.bluestome.pcs.dao.ArticleDocDao;
import org.bluestome.pcs.dao.WebSiteDao;
import org.bluestome.pcs.memcache.MemcacheClient;
import org.bluestome.pcs.utils.HttpClientUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PCPOPHtmlParser {
	
	static Logger logger = LoggerFactory.getLogger(PCPOPHtmlParser.class);

	static final String PCPOP_URL = "http://www.pcpop.com/";

	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();
	
	static HashMap<String,String> HTMLHASH = new HashMap<String,String>();

	static ArticleDocDao articleDocDao = DAOFactory.getInstance().getArticleDocDao();	
	
	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();
	
	static MemcacheClient client = MemcacheClient.getInstance();
	
	final static String PCPOP="WEB_PCPOP";
	
	static Integer UCOUNT = 0;
	
	/**
	 * 获取指定站点下的页面源码
	 * @param webList
	 */
	static void initHTML(WebsiteBean bean){
		Long start = System.currentTimeMillis();
		try{
			Long start1 = System.currentTimeMillis();
			String content = ViewSourceFrame(bean.getUrl());
			if(null != content && !"".equalsIgnoreCase(content)){
				processWithDoc(bean.getId(), content);
				Long end1 = System.currentTimeMillis();
				logger.info(" >> 站点["+bean.getName()+"|"+bean.getUrl()+"] 单条耗时:"+(end1-start1)+"长度："+content.getBytes().length);
			}
		}catch(Exception e){
			logger.debug("Exception:"+e.getMessage());
		}
		Long end = System.currentTimeMillis();
		logger.info("总耗时:"+(end-start));
	}
	
	/**
	 * 获取指定URL下的源码
	 * @param url1
	 * @return
	 */
    public static String ViewSourceFrame(String url1) throws Exception{
    	String body = null;
    	byte[] tbody = HttpClientUtils.getBody(url1);
    	body = new String(tbody,"GBK");
    	return body;
   }
	
	/**
	 * 获取文章链接
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void doc(String url, String pre) throws Exception {
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");

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
					if (link.getLink().toLowerCase().startsWith(pre) && !link.getLinkText().equalsIgnoreCase("详细内容")){
						bean = new LinkBean();
						bean.setLink(link.getLink());
						bean.setName(link.getLinkText());
						LINKHASH.put(link.getLink(), bean);
					}
				}
			}
		}
	}

	/**
	 * 获取文章链接
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void docByHTML(String content, String pre) throws Exception {
		Parser parser = new Parser();
		parser.setInputHTML(content);
		parser.setEncoding("GB2312");

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
					if (link.getLink().toLowerCase().startsWith(pre) && !link.getLinkText().equalsIgnoreCase("详细内容")){
						if(null == client.get(getKey(link.getLink()))){
							bean = new LinkBean();
							bean.setLink(link.getLink());
							bean.setName(link.getLinkText());
							LINKHASH.put(link.getLink(), bean);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 根据URL获取内容
	 * @param url
	 * @return
	 * @throws Exception
	 */
	static String content(String url) throws Exception{
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");

		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
				new HasAttributeFilter("id", "contentDiv"));
		String content = null;
		if(null != list && list.size() > 0){
			Div div = (Div)list.elementAt(0);
			String tmp = div.getStringText();
			content = tmp;
		}
		return content;
	}

	public static void main(String args[]) {
		try{
			String body = ViewSourceFrame("http://www.pcpop.com/doc/0/848/848552.shtml");
			System.out.println(body);
			if(true)
				return;
			//指定父ID下的网站列表
			final List<WebsiteBean> weblist = webSiteDao.findByParentId(166);
			for(WebsiteBean bean:weblist){
				initHTML(bean);
				try {
					processAuthor(bean.getId());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static String getKey(String url){
		return PCPOP+":"+url;
	}
	
	/**
	 * 根据文章内容获取最新发布的文章
	 * @param webid
	 * @param content
	 * @throws Exception
	 */
	static void processWithDoc(int webid,String content) throws Exception{
		docByHTML(content, "http://www.pcpop.com/doc/");
		Iterator it = LINKHASH.keySet().iterator();
		ArticleDoc doc = null;
		while (it.hasNext()) {
			String key1 = (String) it.next();
			if(null == client.get(getKey(key1))){
				LinkBean link = (LinkBean) LINKHASH.get(key1);
				doc = new ArticleDoc();
				doc.setTitle(link.getName());
				doc.setUrl(link.getLink());
				doc.setWebId(webid);
				int id = articleDocDao.insert(doc);
				if(id > 0){
					client.add(getKey(doc.getUrl()), doc);
					doc.setId(id);
					doc.setStatus(1);
				}
			}
		}
		LINKHASH.clear();
	}
	/**
	 * 获取文章作者，发布时间等数据
	 *
	 */
	static void processAuthor(int webId) throws Exception{
		List<ArticleDoc> list = articleDocDao.findDoc(webId, 1);
		for(ArticleDoc bean : list){
			
			if(!bean.getUrl().startsWith("http://www.pcpop.com/doc/")){
				continue;
			}
			//更新文章的作者和发布时间
			try{
				String author = author3(bean.getUrl());

				if(null == author){
					logger.debug(" > url:["+bean.getUrl()+"] get author is null!");
					continue;
				}
                String tmp1 = null;
                String tmp2 = null;
                if(author.indexOf("</a>") != -1){
                    //新样式处理
                    /**
                     * <div class="cen01">2012年10月24日 00:11&nbsp;出处：<a title="转到泡泡网首页!" href="http://www.pcpop.com/">泡泡网</a>&nbsp;【原创】 作者:张伟伟 编辑:张伟伟</div>
                     */
                    tmp2 = author.substring(0,author.indexOf("&nbsp;"));
                    String atmp = author.substring(author.indexOf("</a>")+4);
                    tmp1 = atmp.substring(atmp.lastIndexOf("作者")+3,atmp.lastIndexOf("编辑")-1);
                    logger.info("新版:\t"+bean.getUrl()+"|作者:"+tmp1+"|发布时间:"+tmp2);
                }else{
                    tmp1 = author.substring(author.lastIndexOf("作者")+3,author.lastIndexOf("编辑")-1);
                    if(author.indexOf("：") != -1){
                    	tmp2 = author.substring(0,author.indexOf("：")-2);
                    }
                    if(null == tmp1 || null ==  tmp2){
                        logger.error(bean.getUrl()+"的作者["+tmp1+"]或者发布时间["+tmp2+"]为空!");
                        break;
                    }
                    logger.info("老板:\t"+bean.getUrl()+"|作者:"+tmp1+"|发布时间:"+tmp2);
                }
                logger.info("> doc:\t"+bean.getUrl()+"|"+tmp1+"|"+tmp2);
				bean.setAuthor(tmp1);
				bean.setPublishTime(tmp2);
				bean.setStatus(2);
				if(!articleDocDao.update(bean)){
					logger.error("更新作者失败:"+bean.getUrl());
				}else{
					logger.debug("["+bean.getId()+"]更新成功:"+bean.getUrl());
				}
			}catch(java.io.FileNotFoundException e){
				bean.setStatus(10);
				if(!articleDocDao.update(bean)){
					logger.error("更新作者失败:"+bean.getUrl());
				}else{
					logger.error("["+bean.getId()+"]更新记录状态为10[文件或地址查找找不到]:"+bean.getUrl());
				}
				continue;
			}catch(org.htmlparser.util.ParserException e){
				bean.setStatus(10);
				if(!articleDocDao.update(bean)){
					logger.error("更新作者失败:"+bean.getUrl());
				}else{
					logger.error("["+bean.getId()+"]更新记录状态为10[URL解析失败]:"+bean.getUrl());
				}
				continue;
			}catch(Exception e){
				bean.setStatus(11);
				bean.setContent(e.getMessage());
				if(!articleDocDao.update(bean)){
					logger.error("更新作者和文章发布时间失败:"+bean.getUrl()+"|"+e.getMessage());
				}else{
					logger.error("["+bean.getId()+"]更新记录状态为11[其他异常情况]:"+bean.getUrl());
				}
				continue;
			}
		}
		
	}
	
   /**
     * 获取作者区域数据
     * @param url
     * @return
     * @throws Exception
     */
    static String author3(String url) throws Exception{
        Parser parser = new Parser();
        parser.setURL(url);
        parser.setEncoding("GB2312");

        NodeFilter fileter = new NodeClassFilter(Div.class);
        NodeList list = parser.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
                new HasAttributeFilter("class", "cen01"));
        String author = null;
        if (list != null && list.size() > 0) {
            Div div = (Div)list.elementAt(0);
            String tmp = div.getStringText();
            author = tmp;
        }
        return author;
    }
    
}
