package com.chinamilitary.htmlparser;

import com.chinamilitary.bean.ArticleDoc;
import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDocDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.DateUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.common.Constants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PCPOPHtmlParser {
	
	static Log logger = LogFactory.getLog(PCPOPHtmlParser.class);

	static final String PCPOP_URL = "http://www.pcpop.com/";

	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();
	
	static HashMap<String,String> HTMLHASH = new HashMap<String,String>();

	static ArticleDocDao articleDocDao = DAOFactory.getInstance().getArticleDocDao();	
	
	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();
	
	static MemcacheClient client = MemcacheClient.getInstance();
	
	final static String PCPOP="WEB_PCPOP";
	
	static Integer UCOUNT = 0;
	
    private static ScheduledExecutorService EXEC_THREAD = Executors.newSingleThreadScheduledExecutor();
	/**
	 * 获取分类链接
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void catalog(String url) throws Exception { // WebsiteBean bean
		WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");

		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "xpn6")); // id
																	// blk_lmdh_01
																	// "class",
																	// "nav"
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p1.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					if (link.getLink().toLowerCase().startsWith("http://")) {
						logger.debug(link.getLinkText());
						logger.debug(link.getLink() + "\n");
						tmp = new WebsiteBean();
						tmp.setName(link.getLinkText());
						tmp.setUrl(link.getLink());
						tmp.setParentId(166);
						boolean b = dao.insert(tmp);
						if (b) {
							logger.debug("成功");
						} else {
							logger.debug("失败");
						}
					}
				}
			}
		}
	}

	/**
	 * 获取泡泡网全国分站
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void cityCatalog(String url) throws Exception {
		WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");

		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "classify2")); // id
																		// blk_lmdh_01
																		// "class",
																		// "nav"
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p1.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					if (link.getLink().toLowerCase().startsWith("http://")) {
						tmp = new WebsiteBean();
						tmp.setName(link.getLinkText());
						tmp.setUrl(link.getLink());
						tmp.setParentId(166);
						boolean b = dao.insert(tmp);
						if (b) {
							logger.debug("成功");
						} else {
							logger.debug("失败");
						}
					}
				}
			}
		}
	}
	
	static void html(String url) throws Exception{
		Long start = System.currentTimeMillis();
	}
	
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
	 * 获取指定列表中页面源码
	 * @param webList
	 */
	static void initHTML(List<WebsiteBean> webList){
		Long start = System.currentTimeMillis();
		for(WebsiteBean bean:webList){
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
				continue;
			}
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
	    	String url = url1;   
	        String linesep = System.getProperty("line.separator");   
	        String htmlLine;   
	        String htmlSource = "";   
            java.net.URL source = new URL(url);   
            InputStream in = new BufferedInputStream(source.openStream());   
            BufferedReader br = new BufferedReader(new InputStreamReader(in));   
            while ((htmlLine = br.readLine()) != null) {   
            	htmlSource = htmlSource + htmlLine + linesep;   
            }   
            return htmlSource;
	  
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
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	static String author(String url) throws Exception{
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");

		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
				new HasAttributeFilter("class", "title"));
		String author = null;
		if (list != null && list.size() > 0) {
			Div div = (Div)list.elementAt(0);
			String tmp = div.getStringText();
			author = tmp;
		}
		
		if(null == author){
			logger.debug(" > 早版标题格式解析");
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
				logger.debug("author:"+author);
			}
		}
		
		if(null == author){
			logger.debug(" > 新版标题格式解析");
			parser = new Parser();
			parser.setURL(url);
			parser.setEncoding("GBK");
			
			NodeFilter fileter1 = new NodeClassFilter(Div.class);
			NodeList list1 = parser.extractAllNodesThatMatch(fileter1).extractAllNodesThatMatch(
					new HasAttributeFilter("class", "cotent"));
			if(null != list1 && list1.size() > 0){
				Div div = (Div)list1.elementAt(1);
				String tmp = div.getStringText();
				author = tmp.substring(tmp.indexOf("</a>")+4);
				logger.debug("author:"+author);
			}
		}
		return author;
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
			//指定父ID下的网站列表
			final List<WebsiteBean> weblist = webSiteDao.findByParentId(166);
			for(WebsiteBean bean:weblist){
				initHTML(bean);
				try {
					processAuthor(bean.getId());
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e);
				}
			}
			logger.info(" >> End Of One Parser PCPOP " +DateUtils.getNow()+",GET "+UCOUNT+" Articles");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void processC() throws Exception{
		process();
		processAuthor();
		
	}
	
	static void downloadArticleDoc() throws Exception{
		try {
			//指定父ID下的网站列表
			List<WebsiteBean> weblist = webSiteDao.findByParentId(166);
			for(WebsiteBean web:weblist){
				System.gc();
				logger.info(" >> "+web.getName()+"|"+web.getUrl());
				List<ArticleDoc>  list = articleDocDao.find(web.getId(), 2,true, 100);
				if(null != list && list.size() > 0){
					for(ArticleDoc doc:list){
						try{
							String fileName = String.valueOf(doc.getId());
							String savePath = doc.getWebId()+File.separator + StringUtils.gerDir(String.valueOf(doc.getId()))+ File.separator + fileName+ ".html";
							logger.debug(" >> doc.url:"+doc.getUrl());
							IOUtil.createPicFile(doc.getUrl(), Constants.DOC_FILE_SERVER + savePath);
							doc.setStatus(5);
							doc.setTag(savePath);
						}catch(java.io.FileNotFoundException e){
							doc.setStatus(-1);
							logger.error(e);
							continue;
						}finally{
							boolean b = articleDocDao.update(doc);
							if(b){
								logger.info(" >> 更新文章["+doc.getId()+"|"+doc.getTitle()+"]成功!");
							}
						}
						Thread.sleep(150);
					}
					list.clear();
					System.gc();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static boolean download(ArticleDoc doc) throws Exception{
		boolean b = false;
		if(null != doc){
			String fileName = String.valueOf(doc.getId());
			IOUtil.createPicFile(doc.getUrl(), Constants.DOC_FILE_SERVER + doc.getWebId()+File.separator
					+ StringUtils.gerDir(String.valueOf(doc.getId())) 
					+ File.separator + fileName);
			b = true;
		}
		return b;
	}
	
	static void contentProcess(){
		try{
			List<ArticleDoc> list = articleDocDao.findAll(2);
			for(ArticleDoc bean : list){
				if(!bean.getUrl().startsWith("http://www.pcpop.com/doc/")){
					continue;
				}
				
				try{
//					String content = content(bean.getUrl());
//					if(null != content){
//						bean.setContent(content);
						bean.setStatus(3);
						if(!articleDocDao.update(bean)){
							logger.debug("更新作者失败:"+bean.getUrl());
						}else{
							if(null != client.get(getKey(bean.getUrl()))){
								client.replace(getKey(bean.getUrl()), bean);
							}
							logger.debug("["+bean.getId()+"]更新文章内容成功");
						}
//					}
				}catch(java.io.FileNotFoundException e){
					bean.setStatus(10);
					bean.setContent(e.getMessage());
					if(!articleDocDao.update(bean)){
						logger.debug("更新文章内容失败:"+bean.getUrl());
					}else{
						logger.debug("["+bean.getId()+"]更新记录状态为10[文件或地址查找找不到]:"+bean.getUrl());
					}
					continue;
				}catch(org.htmlparser.util.ParserException e){
					bean.setStatus(10);
					bean.setContent(e.getMessage());
					if(!articleDocDao.update(bean)){
						logger.debug("更新文章内容失败:"+bean.getUrl());
					}else{
						logger.debug("["+bean.getId()+"]更新记录状态为10[文件或地址查找找不到]:"+bean.getUrl());
					}
					continue;
				}catch(Exception e){
					bean.setStatus(11);
					bean.setContent(e.getMessage());
					if(!articleDocDao.update(bean)){
						logger.debug("更新文章内容失败:"+bean.getUrl());
					}else{
						logger.debug("["+bean.getId()+"]更新记录状态为11[其他异常情况]:"+bean.getUrl());
					}
					continue;
				}
			}
		}catch(Exception e){
			
		}
	}
	
	static void init(){
		try{
			List<WebsiteBean> weblist = webSiteDao.findByParentId(166);
			logger.debug("初始化数据库数据");
			for(WebsiteBean bean:weblist){
				List<ArticleDoc> docList = articleDocDao.findByWebId(bean.getId());
				for(ArticleDoc doc:docList){
					if(client.get(getKey(doc.getUrl())) == null){
						logger.debug("添加对象到缓存");
						client.add(getKey(doc.getUrl()), doc);
					}
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
				String author = author(bean.getUrl());

				if(null == author){
					author = author2(bean.getUrl());
				}
                if(null == author){
                    author = author3(bean.getUrl());
                }
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
                        logger.info("author.indexOf(\"：\")\t"+tmp2);
                    }
                    if(author.indexOf(":") != -1){
                        tmp2 = author.substring(0,author.indexOf(":")-2);
                        logger.info("author.indexOf(\":\")\t"+tmp2);
                    }
    				if(null == tmp1 || null ==  tmp2){
                        logger.error("文章的作者或者发布时间为空!");
    					break;
    				}
    				logger.info("老板:\t"+bean.getUrl()+"|作者:"+tmp1+"|发布时间:"+tmp2);
                }
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
//				ResourceQueneUpdate.getInstance().setElement(bean);
				e.printStackTrace();
				if(!articleDocDao.update(bean)){
					logger.error("更新作者和文章发布时间失败:"+bean.getUrl());
				}else{
					logger.error("["+bean.getId()+"]更新记录状态为11[其他异常情况]:"+bean.getUrl());
				}
				logger.error(" > Error:"+bean.getUrl());
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
	static String author2(String url) throws Exception{
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
//			logger.debug("重新解析作者栏");
			parser = new Parser();
			parser.setURL(url);
			parser.setEncoding("GB2312");
			
			NodeFilter fileter1 = new NodeClassFilter(Div.class);
			NodeList list1 = parser.extractAllNodesThatMatch(fileter1).extractAllNodesThatMatch(
					new HasAttributeFilter("class", "title"));
			if(null != list1 && list1.size() > 0){
				Div div = (Div)list1.elementAt(1);
				String tmp = div.getStringText();
				author = tmp.substring(tmp.indexOf("</a>")+4);
				logger.debug("author:"+author);
			}
		}
		return author;
	}
	
    /**
     * 
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
                    new HasAttributeFilter("class", "cen01"));
            if(null != list1 && list1.size() > 0){
                Div div = (Div)list1.elementAt(1);
                if(null == div){
                    div = (Div)list1.elementAt(0);
                }
                if(null == div){
                    return null;
                }
                String tmp = div.getStringText();
//                author = tmp.substring(tmp.indexOf("</a>")+4);
                author = tmp;
                logger.debug("author:"+author);
            }
        }
        return author;
    }
    
	/**
	 * 获取文章作者，发布时间等数据
	 *
	 */
	static void processAuthor() throws Exception{
		List<ArticleDoc> list = articleDocDao.findAll(1,1310);
		for(ArticleDoc bean : list){
			
			if(!bean.getUrl().startsWith("http://www.pcpop.com/doc/")){
				continue;
			}
//			logger.debug("获取文章数据");
			//更新文章的作者和发布时间
			try{
				String author = author(bean.getUrl());
//				logger.debug("author:"+author);
				String tmp1 = author.substring(author.lastIndexOf("作者")+3,author.lastIndexOf("编辑")-1);
				String tmp2 = author.substring(0,author.indexOf(":")-2);
				if(null == tmp1 || null ==  tmp2){
					break;
				}
				bean.setAuthor(tmp1);
				bean.setPublishTime(tmp2);
				bean.setStatus(2);
//				ResourceQueneUpdate.getInstance().setElement(bean);
				if(!articleDocDao.update(bean)){
					logger.error("更新作者失败:"+bean.getUrl());
				}else{
					logger.debug("["+bean.getId()+"]更新成功:"+bean.getUrl());
				}
			}catch(java.io.FileNotFoundException e){
				bean.setStatus(10);
//				ResourceQueneUpdate.getInstance().setElement(bean);
				if(!articleDocDao.update(bean)){
					logger.error("更新作者失败:"+bean.getUrl());
				}else{
					logger.error("["+bean.getId()+"]更新记录状态为10[文件或地址查找找不到]:"+bean.getUrl());
				}
				continue;
			}catch(org.htmlparser.util.ParserException e){
				bean.setStatus(10);
//				ResourceQueneUpdate.getInstance().setElement(bean);
				if(!articleDocDao.update(bean)){
					logger.error("更新作者失败:"+bean.getUrl());
				}else{
					logger.error("["+bean.getId()+"]更新记录状态为10[URL解析失败]:"+bean.getUrl());
				}
				continue;
			}catch(Exception e){
				bean.setStatus(11);
				bean.setContent(e.getMessage());
//				ResourceQueneUpdate.getInstance().setElement(bean);
				if(!articleDocDao.update(bean)){
					logger.error("更新作者和文章发布时间失败:"+bean.getUrl());
				}else{
					logger.error("["+bean.getId()+"]更新记录状态为11[其他异常情况]:"+bean.getUrl());
				}
				continue;
			}
		}
	}
	
	static void processWithDoc() throws Exception{
//		Iterator hit = HTMLHASH.keySet().iterator();
//		while(hit.hasNext()){
//			String key = (String) hit.next();
//			logger.debug("key:"+key);
//			String[] keys = key.split(":");
//			String content = (String) HTMLHASH.get(key);
//			try{
//				docByHTML(content, "http://www.pcpop.com/doc/");
//				Iterator it = LINKHASH.keySet().iterator();
//				ArticleDoc doc = null;
//				while (it.hasNext()) {
//					String key1 = (String) it.next();
//					if(null == client.get(getKey(key1))){
//						LinkBean link = (LinkBean) LINKHASH.get(key1);
//						doc = new ArticleDoc();
//						doc.setTitle(link.getName());
//						doc.setUrl(link.getLink());
//						doc.setWebId(Integer.valueOf(keys[0]));
//						int id = articleDocDao.insert(doc);
//						if(!(id>0)){
//							logger.debug("失败，\t链接名称：" + link.getName() + "\n链接地址："+ link.getLink());
//						}else{
//							doc.setId(id);
//							doc.setStatus(1);
//							client.add(getKey(doc.getUrl()), doc);
//							logger.debug("Memcached now store this object");
//						}
//					}
//				}
//				LINKHASH.clear();
//				}catch(Exception e){
//					logger.debug("解析异常，跳过:"+key+"\tException:"+e.getMessage());
//					continue;
//				}
//		}
		
		//1584
		List<ArticleDoc> list = articleDocDao.findAll(1);
		for(ArticleDoc bean : list){
			
			if(!bean.getUrl().startsWith("http://www.pcpop.com/doc/")){
				continue;
			}
			logger.debug("获取文章数据");
			//更新文章的作者和发布时间
			try{
				String author = author(bean.getUrl());
				String tmp1 = author.substring(author.lastIndexOf("作者")+3,author.lastIndexOf("编辑")-1);
				String tmp2 = author.substring(0,author.indexOf(":")-2);
				if(null == tmp1 || null == tmp2){
					break;
				}
				bean.setAuthor(tmp1);
				bean.setPublishTime(tmp2);
				bean.setStatus(2);
				if(!articleDocDao.update(bean)){
					logger.debug("更新作者失败:"+bean.getUrl());
				}else{
					logger.debug("["+bean.getId()+"]更新成功:"+bean.getUrl());
				}
			}catch(java.io.FileNotFoundException e){
				bean.setStatus(10);
				if(!articleDocDao.update(bean)){
					logger.debug("更新作者失败:"+bean.getUrl());
				}else{
					logger.debug("["+bean.getId()+"]更新记录状态为10[文件或地址查找找不到]:"+bean.getUrl());
				}
				continue;
			}catch(org.htmlparser.util.ParserException e){
				bean.setStatus(10);
				if(!articleDocDao.update(bean)){
					logger.debug("更新作者失败:"+bean.getUrl());
				}else{
					logger.debug("["+bean.getId()+"]更新记录状态为10[URL解析失败]:"+bean.getUrl());
				}
				continue;
			}catch(Exception e){
				bean.setStatus(11);
				bean.setContent(e.getMessage());
				if(!articleDocDao.update(bean)){
					logger.debug("更新作者和文章发布时间失败:"+bean.getUrl());
				}else{
					logger.debug("["+bean.getId()+"]更新记录状态为11[其他异常情况]:"+bean.getUrl());
				}
				continue;
			}
		}
	}
	
	static void process(){
		try {
			//指定父ID下的网站列表
			List<WebsiteBean> weblist = webSiteDao.findByParentId(166);
			//将获取的页面放入缓存
			initHTML(weblist);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
