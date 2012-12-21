package com.verycd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.ArticleDoc;
import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.ResultBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ArticleDocDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.common.Constants;

public class VeryCDParser {

	private static Log log = LogFactory.getLog(VeryCDParser.class);
	
	static final String PIC_SAVE_PATH = Constants.FILE_SERVER;
	
	final static Integer D_PARENT_ID = 1300;

	final static Integer _D_PARENT_ID = 1350;
	
	private static final String URL = "http://www.verycd.com";

	private static final String URL_ = "http://www.verycd.com/";

	private static final String URL_ARCHIVES = "http://www.verycd.com/archives/";
	
	private static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	private static HashMap<String, String> CATALOGHASH = new HashMap<String, String>();
	
	private static HashMap<String, LinkBean> SECONDLINKHASH = new HashMap<String, LinkBean>();

	private static HashMap<String, ResultBean> PAGELINKHASH = new HashMap<String, ResultBean>();

	private static int COUNT = 0;
	
	final static int PAGE_SIZE = 40;

	private static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	private static ArticleDao articleDao = DAOFactory.getInstance()
			.getArticleDao();

	private static WebSiteDao wesiteDao = DAOFactory.getInstance()
			.getWebSiteDao();

	private static PicFileDao picFileDao = DAOFactory.getInstance()
			.getPicFileDao();

	private static MemcacheClient client = MemcacheClient.getInstance();

	static ArticleDocDao articleDocDao = DAOFactory.getInstance().getArticleDocDao();	
	
	final static String VERYCD="WEB_VERYCD";
	
	/**
	 * 首页分类
	 * 
	 * @throws Exception
	 */
	public static void catelogyArchives(String url) throws Exception {
		WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "nav"));
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			p1.setEncoding("GB2312");
			NodeFilter tagFilter = new NodeClassFilter(CompositeTag.class);
			NodeList tagList = p1.extractAllNodesThatMatch(tagFilter).extractAllNodesThatMatch(
					new HasAttributeFilter("id", "libCatalog"));
			if (tagList != null && tagList.size() > 0) {
				Parser p2 = new Parser();
				p2.setInputHTML(tagList.toHtml());
				p2.setEncoding("GB2312");
				NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
				NodeList linkList = p2.extractAllNodesThatMatch(linkFilter);
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					log.info("text:"+link.getLinkText());
					log.info("link:"+link.getLink());
					tmp = new WebsiteBean();
					int start = link.getLinkText().indexOf("d");
					int end = link.getLinkText().lastIndexOf(")");
					if(start != -1 && end != -1){
						tmp.setName(link.getLinkText().substring(0,start));
						log.info(">> tmp.getName():"+tmp.getName());
					}
					if(link.getLink().startsWith("\\")){
						tmp.setUrl(URL_ + link.getLink());
						log.info("new:"+ URL_ + link.getLink()+ "\n");
					}else{
						tmp.setUrl(URL+link.getLink());
						log.info("new:"+URL+link.getLink()+"\n");
					}
					tmp.setParentId(_D_PARENT_ID);
					boolean b = dao.insert(tmp);
					if (b) {
						log.info("成功");
					} else {
						log.info("失败");
					}
				}
			}
		}
	}
	
	
	/**
	 * 首页分类中的数据
	 * 
	 * @throws Exception
	 */
	public static void catelogy(String url) throws Exception {
//		WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");
		System.out.println("Version:"+parser.getVersion());
		System.out.println("VersionNumber:"+parser.getVersionNumber());
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "nav"));
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			p1.setEncoding("GB2312");
			NodeFilter tagFilter = new NodeClassFilter(CompositeTag.class);
			NodeList tagList = p1.extractAllNodesThatMatch(tagFilter).extractAllNodesThatMatch(
					new HasAttributeFilter("id", "libCatalog"));
			if (tagList != null && tagList.size() > 0) {
				Parser p2 = new Parser();
				p2.setInputHTML(tagList.toHtml());
				p2.setEncoding("GB2312");
				NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
				NodeList linkList = p2.extractAllNodesThatMatch(linkFilter);
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					if(link.getLink().startsWith("\\")){
						CATALOGHASH.put(URL+link.getLink(), link.getLinkText());
					}else{
						CATALOGHASH.put(URL+link.getLink(), link.getLinkText());
					}
				}
			}
		}
	}
	
	/**
	 * 首页分类
	 * 
	 * @throws Exception
	 */
	public static void catelogy(WebsiteBean bean) throws Exception {
		WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
		Parser parser = new Parser();
		parser.setURL(bean.getUrl());
		parser.setEncoding("GB2312");
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "libCatalog"));
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			p1.setEncoding("GB2312");
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p1.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					log.info(link.getLinkText());
					log.info(URL_ + link.getLink() + "\n");
					tmp = new WebsiteBean();
					tmp.setName(link.getLinkText());
					tmp.setUrl(link.getLink());
					tmp.setParentId(bean.getId());
					boolean b = dao.insert(tmp);
					if (b) {
						log.info("成功");
					} else {
						log.info("失败");
					}
				}
			}
		}
	}

	/**
	 * 分页判断
	 * 
	 * @param url
	 * @param attribute
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private static ResultBean hasPagingByCount(WebsiteBean bean, String attribute,
			String value) throws Exception {
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(bean.getUrl());
		parser.setEncoding("GB2312");

		NodeFilter filter = new NodeClassFilter(CompositeTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter(attribute, value));
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p1.extractAllNodesThatMatch(linkFilter);
			if(linkList != null && linkList.size() > 0){
				for(int i=0;i<linkList.size();i++){
					LinkTag link = (LinkTag)linkList.elementAt(i);
					LinkBean tmp = null;
					if(link.getLink().startsWith("/")){
						tmp = new LinkBean();
						if(!link.getLink().startsWith("http://")){
							tmp.setLink(URL+link.getLink());
						}else{
							tmp.setLink(link.getLink());
						}
						tmp.setName(link.getLinkText());
						result.getMap().put(tmp.getLink(), tmp);
					}
					Thread.sleep(10);
				}
			}
		}
		result.setBool(true);
		return result;
	}
	

	/**
	 * 分页判断
	 * 
	 * @param url
	 * @param attribute
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private static ResultBean hasPaging(WebsiteBean bean, String attribute,
			String value) throws Exception {
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(bean.getUrl());
		parser.setEncoding("GB2312");

		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter(attribute, value));
		if (list != null && list.size() == 1) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p1.extractAllNodesThatMatch(linkFilter);
			if(linkList != null && linkList.size() > 0){
				for(int i=0;i<linkList.size();i++){
					LinkTag link = (LinkTag)linkList.elementAt(i);
					LinkBean tmp = null;
					if(link.getLink().startsWith("/")){
						tmp = new LinkBean();
						if(!link.getLink().startsWith("http://")){
							tmp.setLink(URL+link.getLink());
						}else{
							tmp.setLink(link.getLink());
						}
						tmp.setName(link.getLinkText());
						result.getMap().put(tmp.getLink(), tmp);
					}
				}
				result.setBool(true);
			}
		}
		LinkBean l1 = null;
		l1 = new LinkBean();
		l1.setLink(bean.getUrl());
		result.add(l1);
		result.getMap().put(l1.getLink(), l1);
		result.setBool(true);
		return result;
	}

	/**
	 * 获取分类下数据
	 * 
	 * @param link
	 * @param webId
	 * @throws Exception
	 */
	public static void secondURL(LinkBean link, int webId) throws Exception {
		Parser parser = new Parser();
		parser.setURL(link.getLink());
		parser.setEncoding("GB2312");
//		System.out.println("版本号:"+parser.getVersion());

		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(CompositeTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("id", "archiveResourceList"));
		if (list != null && list.size() > 0) {
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			Parser p2 = new Parser();
			p2.setInputHTML(list.toHtml());
			p2.setEncoding("GB2312");
			NodeList list4 = p2.parse(linkFilter);
			if (list4 != null || list4.size() > 0) {
				ArticleDoc doc = null;
				for (int i = 0; i < list4.size(); i++) {
						LinkTag nl = (LinkTag) list4.elementAt(i);
						doc = new ArticleDoc();
						System.out.println(" >> doc.title:"+nl.getLinkText()+"\tdoc.link:"+nl.getLink());
						if(null == nl.getLinkText() || "".equalsIgnoreCase(nl.getLinkText())){
							doc.setTitle("NULL");
						}else{
							doc.setTitle(nl.getLinkText());
						}
						if(nl.getLink().startsWith("http://")){
							doc.setUrl(nl.getLink());
						}else{
							doc.setUrl(URL+nl.getLink());
						}
						if(null == doc.getTitle() || "".equalsIgnoreCase(doc.getTitle())){
							continue;
						}
						
						if(null == doc.getUrl() || "".equalsIgnoreCase(doc.getUrl())){
							continue;
						}
						doc.setWebId(webId);
						if(null == client.get(getKey(doc.getUrl()))){
							System.out.println(""+doc.getUrl());
//							ResourceQueneInsert.getInstance().setElement(doc);
						}else{
							log.info(">> 已存在相同的内容 ["+nl.getLinkText()+"]");
						}
						Thread.sleep(10);
				}
			}
		}
	}

	/**
	 * 获取文章下图片
	 * @param url
	 * @return
	 * @throws Exception
	 */
	static String getAlbumPicUrl(String url) throws Exception {
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");

		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "topicImg"));
		ImageTag link = null;
		if (list != null && list.size() > 0) {
			log.info("\t>> html:"+list.toHtml());
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			p1.setEncoding("GB2312");

			NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
			NodeList nodes = p1.extractAllNodesThatMatch(imageFilter);
			if (nodes != null && nodes.size() > 0) {
				log.info(">> nodes.size:"+nodes.size());
				if (nodes.elementAt(0) instanceof ImageTag) {
					link = (ImageTag) nodes.elementAt(0);
				}
			}
		}
		if (link != null) {
			return link.getImageURL();
		} else {
			return null;
		}
	}


	static void getFileDownloadUrl(String url) throws Exception {
		Parser parser = null;
		if (null != url) {
			parser = new Parser();
			parser.setURL(url);
			parser.setEncoding("GB2312");
			
			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("id","iptcomED2K"));
			if(null != list && list.size() > 0){
				Parser p1 = new Parser();
				p1.setInputHTML(list.toHtml());
				p1.setEncoding("GB2312");
				
				NodeFilter inputFilter = new NodeClassFilter(InputTag.class);
				NodeList inputList = p1.extractAllNodesThatMatch(inputFilter).extractAllNodesThatMatch(new HasAttributeFilter("class","forminput"));
				if(inputList != null && inputList.size() > 0){
					for(int i=0;i<inputList.size();i++){
						InputTag input = (InputTag)inputList.elementAt(i);
						String value = input.getAttribute("value");
						if(null != value){
							log.info("\t>> value:\t"+value);
						}
					}
				}
			}
		}
	}

	static void getFileDownloadUrl(ArticleDoc doc) throws Exception {
		Parser parser = null;
		if (doc != null) {
			parser = new Parser();
			parser.setURL(doc.getUrl());
			parser.setEncoding("GB2312");
			
			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("id","iptcomED2K"));
			if(null != list && list.size() > 0){
				log.info("\thtml:"+list.toHtml());
			}
		}
	}

	/**
	 * 更新Article介绍 Wallcoo
	 * 
	 * @param article
	 */
	static String getArticleText(String articleUrl) {
		Parser parser = null;
		String intro = "";
		try {
			parser = new Parser();
			parser.setURL(articleUrl);
			parser.setEncoding("GB2312");
			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(filter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("id", "intro2"));
			if (list != null) {
				Div div = (Div) list.elementAt(0);
				NodeList divList = div.getChildren();
				if (divList != null && divList.size() > 0) {
					intro = divList.toHtml();
				}
				/**
				 * String[] ids = div.getIds(); for(int i=0;i<ids.length;i++){
				 * log.info("ids["+i+"]:"+ids[i]); }
				 */
			}
			parser = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intro;
	}

	static void clear() {
		if (LINKHASH.size() > 0) {
			LINKHASH.clear();
		}

		if (SECONDLINKHASH.size() > 0) {
			SECONDLINKHASH.clear();
		}

		if (PAGELINKHASH.size() > 0) {
			PAGELINKHASH.clear();
		}
	}
	
	static void update() throws Exception{
		List<WebsiteBean> list = wesiteDao.findByParentId(D_PARENT_ID);
		/**
		 * 文章获取并入库 List<WebsiteBean> list = wesiteDao.findByParentId(125);
		 */
		if (list != null && list.size() > 0) {
			ResultBean result = null;
			int i = 0;
			for (WebsiteBean bean : list) {
				try{
					result = hasPaging(bean, "id", "archivePageList");
					if (result.isBool()) {
						Iterator it = result.getMap().keySet().iterator();
						while(it.hasNext()){
							String key = (String)it.next();
//							log.info("key:"+key);
//							LinkBean link = (result.getMap().get(key));
//							try {
//								secondURL(link, bean.getId());
//							} catch (org.htmlparser.util.EncodingChangeException e) {
//								e.printStackTrace();
//							}
						}
						result.getMap().clear();
					}
					i++;
					// 更新菜单列表排序
					wesiteDao.update(bean);
				}catch(Exception e){
					log.info(">> Exception :"+e);
				}
			}
		}
	}
	
	/**
	 * 更新文章
	 * @throws Exception
	 */
	static void updateArchives() throws Exception{
		List<WebsiteBean> list = wesiteDao.findByParentId(D_PARENT_ID);
		if (list != null && list.size() > 0) {
			ResultBean result = null;
			for (WebsiteBean bean : list) {
				try{
					if(bean.getId() == 1351){
						if(!needUpdate(bean)){
							log.debug(">> 首页数据相同，表明记录没有增加，不需要更新!");
							break;
						}else{
							continue;
						}
					}
					//判断是否需要进行更新记录
					if(!needUpdate(bean)){
						log.debug(">> ["+bean.getUrl()+"]数据相同，表明记录没有增加，不需要更新!");
						continue;
					}
					System.out.println("\t"+bean.getName()+"|"+bean.getUrl());
					result = hasPagingByCount(bean, "id", "archivePageList");
					if (result.isBool()) {
						Iterator it = result.getMap().keySet().iterator();
						while(it.hasNext()){
							String key = (String)it.next();
							log.info("\tkey:"+key);
							LinkBean link = (result.getMap().get(key));
							try {
								secondURL(link, bean.getId());
							} catch (org.htmlparser.util.EncodingChangeException e) {
								e.printStackTrace();
								continue;
							}
						}
						result.getMap().clear();
					}
				}catch(Exception e){
					log.info(">> Exception :"+e);
					break;
				}
			}
		}
	}
	
	/**
	 * 判断是否需要更新
	 * @param bean
	 * @return boolean true: 需要更新 false: 不需要更新
	 */
	static boolean needUpdate(WebsiteBean bean) throws Exception{
		boolean b = true;
		String value = CATALOGHASH.get(bean.getUrl().replace("/archives/", "/sto/"));
		if(null != value &&  !"".equalsIgnoreCase(value)){
			if(value.equalsIgnoreCase(bean.getName())){
				b = false;
			}else{
				int dbCount = getCatalogCount(bean.getName());
				int webCount = getCatalogCount(value);
				log.debug(">> 数据库中的分类总数为:"+dbCount);
				log.debug(">> 网站中的分类总数为:"+webCount);
				if(webCount <= dbCount){
					b = false;
				}
			}
		}
		if(b){
			String old = bean.getName();
			bean.setName(value);
			if(wesiteDao.update(bean)){
				log.debug(" >> 更新网站名称从["+old+"]为["+bean.getName()+"]");
			}
		}
		return b;
	}
	
	/**
	 * 获取分类总数
	 * @param name
	 * @return
	 */
	static int getCatalogCount(String name){
		int count = 0;
		int start = name.indexOf("(");
		int end = name.indexOf(")");
		if(start != -1 && end != -1){
			try{
				count = Integer.valueOf(name.substring(start+1,end));
			}catch(Exception e){
				System.err.println(e);
			}
		}
		return count;
	}
	public static void main(String args[]) {
		try {
			//初始化Archives文章目录
//			catelogyArchives(URL_ARCHIVES);
			
			Thread th = new Thread(new RequestRecordThread(),"verycdRecordUpdate");
			th.start();
			
			//获取一级目录
//			catelogy("http://www.verycd.com/");
			
			updateArchives();
			
//			update();
			
//			test();
			
			
			
			/*******************************华丽的分割线：测试部分**************************************/
//			String img = getAlbumPicUrl("http://www.verycd.com/topics/2870019/");
//			if(null != img){
//				log.info(">>"+img);
//			}
//			getFileDownloadUrl("http://www.verycd.com/topics/2749864/");
			
//			patch();
			
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				CATALOGHASH.clear();
				System.gc();
			}
	}
	
	static void patch() throws Exception{
		List<WebsiteBean> list = wesiteDao.findByParentId(_D_PARENT_ID);
		for(WebsiteBean bean:list){
			String key = bean.getUrl().replace("/archives","/sto");
			log.info(">> key:"+key);
			String value = CATALOGHASH.get(key);
			if(null != value  && !"".equalsIgnoreCase(value)){
				log.info(">> value:"+value);
				bean.setName(value);
				if(wesiteDao.update(bean)){
					log.info(">> update website success!");
				}else{
					log.info(">> update website failure!");
				}
			}
		}
	}
	
	static void updateCatalog() throws Exception{
		
	}
	static void test() throws Exception{
		log.info("****************************Split Line********************************");
		Iterator it = CATALOGHASH.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			log.info(">> key:"+key);
			String value = CATALOGHASH.get(key);
			log.info(">> value:"+value);
		}
	}

	//制造缓存KEY
	public static String getKey(String url){
		return VERYCD+":"+url;
	}

}
