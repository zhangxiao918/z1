package com.chinamilitary.htmlparser;

import java.io.File;
import java.io.IOException;
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
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.CacheUtils;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.chinamilitary.xmlparser.XMLParser;
import com.common.Constants;

public class ChinaTUKUAutoParser {

	private static final String PIC_SAVE_PATH = Constants.FILE_SERVER;
	
	static final int D_PARENT_ID = 1633;
	
	static final int D_PARENT_ID_CHINA = 1664;

	static final int D_PARENT_ID_WORLD = 1665;
	
	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	static List<Article> ARTICLELIST = new ArrayList<Article>();

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	static MemcacheClient client = MemcacheClient.getInstance();

	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();
	
	static String CATALOGS_URL = "http://rtuku.club.china.com/user/channelPageTypeIndexAjaxAction.do?channelId=baobao&pageNum=1&eachRowNum=4&eachPageNum=10000&typeId={id}&tagId=0";
	static String CATALOGS_AUTO_URL = "http://rtuku.club.china.com/user/channelPageTypeIndexAjaxAction.do?channelId=auto&pageNum=1&eachRowNum=4&eachPageNum=10000&typeId={id}&tagId=0";

	/**
	 * 获取国内车展列表
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void catalog(String url,int webid) throws Exception { // WebsiteBean bean
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "sortBar"));

		if (null != list && list.size() > 0) {
			Div table = (Div) list.elementAt(0);
			Parser p2 = new Parser();
			p2.setInputHTML(table.getChildrenHTML());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p2.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					System.out.println(link.getLinkText()+"|"+link.getLink());
					tmp = new WebsiteBean();
					tmp.setName(link.getLinkText());
					if (!link.getLink().startsWith("http://")) {
						System.out.println(link.getLink() + "\n");
						continue;
					} else {
						System.out.println(link.getLink() + "\n");
						tmp.setUrl(link.getLink());
					}
					tmp.setParentId(webid);
					boolean b = webSiteDao.insert(tmp);
					if (b) {
						client.add(tmp.getUrl(), tmp.getUrl());
						System.out.println("成功");
					} else {
						System.out.println("失败");
					}
				}
			}
			if (null != p2)
				p2 = null;
		}
		if (null != parser)
			parser = null;
	}
	
	/**
	 * 获取国内车展列表
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void catalogChina(String url) throws Exception { // WebsiteBean bean
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "sortBar"));

		if (null != list && list.size() > 0) {
			Div table = (Div) list.elementAt(0);
			Parser p2 = new Parser();
			p2.setInputHTML(table.getChildrenHTML());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p2.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					System.out.println(link.getLinkText()+"|"+link.getLink());
					tmp = new WebsiteBean();
					tmp.setName(link.getLinkText());
					if (!link.getLink().startsWith("http://")) {
						System.out.println(link.getLink() + "\n");
						continue;
					} else {
						System.out.println(link.getLink() + "\n");
						tmp.setUrl(link.getLink());
					}
//					tmp.setParentId(D_PARENT_ID_CHINA);
//					boolean b = webSiteDao.insert(tmp);
//					if (b) {
//						client.add(tmp.getUrl(), tmp.getUrl());
//						System.out.println("成功");
//					} else {
//						System.out.println("失败");
//					}
				}
			}
			if (null != p2)
				p2 = null;
		}
		if (null != parser)
			parser = null;
	}
	
	/**
	 * 获取国外车展列表
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void catalogWorld(String url) throws Exception { // WebsiteBean bean
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "sortBar"));

		if (null != list && list.size() > 0) {
			Div table = (Div) list.elementAt(0);
			Parser p2 = new Parser();
			p2.setInputHTML(table.getChildrenHTML());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p2.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					System.out.println(link.getLinkText()+"|"+link.getLink());
					tmp = new WebsiteBean();
					tmp.setName(link.getLinkText());
					if (!link.getLink().startsWith("http://")) {
						System.out.println(link.getLink() + "\n");
						continue;
					} else {
						System.out.println(link.getLink() + "\n");
						tmp.setUrl(link.getLink());
					}
					tmp.setParentId(D_PARENT_ID_WORLD);
//					boolean b = webSiteDao.insert(tmp);
//					if (b) {
//						client.add(tmp.getUrl(), tmp.getUrl());
//						System.out.println("成功");
//					} else {
//						System.out.println("失败");
//					}
				}
			}
			if (null != p2)
				p2 = null;
		}
		if (null != parser)
			parser = null;
	}
	
	static void getImage(Integer id, String url) throws Exception {
		Article article = null;
		boolean isDownload = false;
		String aKey = CacheUtils.getArticleKey(id);
		try {
			if (null != client.get(aKey)) {
				System.out.println(">> 从缓存中获取文章对象[" + id + "] ");
				article = (Article) client.get(aKey);
			}
			if (null == article) {
				System.out.println(">> 从缓存中未获取文章对象，从数据库中获取文章对象[" + id + "] ");
				article = articleDao.findById(id);
				if (null != article)
					client.add(aKey, article);
			}
			if (article != null) {
				String tmpUrl = url;
				if(url.indexOf("/html") != -1){
					tmpUrl = url.substring(0, url.lastIndexOf("/html") + 1)+"html/";
					
				}
				System.out.println(" >> article.getArticleUrl():"+article.getArticleUrl());
				if ((article.getArticleUrl().startsWith(tmpUrl))) {
					if (article.getActicleXmlUrl() != null
							|| !article.getActicleXmlUrl().equalsIgnoreCase("")) {
						List<ImageBean> imgList = XMLParser
								.readXmlFromURL(article.getActicleXmlUrl());
						int offset = 0;
						for (ImageBean bean : imgList) {
							String length = "0";
							System.out.println(">> Content-Length:" + length);
							if (null != length) {
								bean.setFileSize(Long.valueOf(length));
								bean.setStatus(1);
							}
							if (client.get(bean.getHttpUrl()) == null) {
								bean.setArticleId(article.getId());
								// HttpClientUtils
								int result = imageDao.insert(bean);
								if (result == -1) {
									System.out.println("图片标题为："
											+ bean.getTitle() + " 已存在，未添加到数据库");
								} else {
									offset++;
								}
							}
						}
						//判断获取的数据和列表中的数据是否一致，如果数据量一致，则表示下载成功，可以更新文章
						if(offset == imgList.size()){
							isDownload = true;
						}
						
						if (isDownload) {
							System.out.println(" >> 更新文章["+article.getId()+"|"+article.getTitle()+"]状态为'已完成'状态!");
							article.setText("FD");
							articleDao.update(article);
						}
//						else{
//							System.out.println(" >> 更新文章["+article.getId()+"|"+article.getTitle()+"]状态为'无更新'状态!");
//							article.setText("NNN");
//							articleDao.update(article);
//						}
					}
				}
			} else {
				System.out.println("未查询到ARTICLE记录");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	static boolean imgDownload(ImageBean imgBean, Integer webId) {
		PicfileBean bean = null;
		bean = new PicfileBean();
		String s_fileName = imgBean.getImgUrl().substring(
				imgBean.getImgUrl().lastIndexOf("/") + 1,
				imgBean.getImgUrl().length());
		String fileName = imgBean.getHttpUrl().substring(
				imgBean.getHttpUrl().lastIndexOf("/") + 1,
				imgBean.getHttpUrl().length());
		String length = "0";
		try {
			byte[] big = null;
			big = HttpClientUtils.getResponseBodyAsByte(imgBean
					.getCommentshowurl(), null, imgBean.getHttpUrl());
			if (null == big)
				return false;
			length = String.valueOf(big.length);
			if (length.equalsIgnoreCase("20261")
					|| length.equalsIgnoreCase("3267")
					|| length.equalsIgnoreCase("4096")) {
				System.out.println(">>> 尝试使用Referer来获取图片");
				big = HttpClientUtils.getResponseBodyAsByte(imgBean
						.getHttpUrl(), null, imgBean.getHttpUrl());
				if (null == big)
					return false;
				length = String.valueOf(big.length);
				if (length.equalsIgnoreCase("20261")
						|| length.equalsIgnoreCase("3267")
						|| length.equalsIgnoreCase("4096")) {
					System.err.println("下载被屏蔽，未突破盗链系统...");
					return false;
				}
			}
			// 小图
			if (client.get(CacheUtils.getShowImgKey(PIC_SAVE_PATH
					+ StringUtils
							.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator + s_fileName)) == null) {
				IOUtil.createPicFile(imgBean.getImgUrl(), PIC_SAVE_PATH
						+ StringUtils.gerDir(String.valueOf(imgBean
								.getArticleId())) + imgBean.getArticleId()
						+ File.separator + s_fileName);
			}

			// 大图
			if (client.get(CacheUtils.getBigPicFileKey(PIC_SAVE_PATH
					+ StringUtils
							.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator + fileName)) == null) {
				IOUtil.createFile(big, PIC_SAVE_PATH
						+ StringUtils.gerDir(String.valueOf(imgBean
								.getArticleId())) + imgBean.getArticleId()
						+ File.separator + fileName);
			}
			bean.setArticleId(imgBean.getArticleId());
			bean.setImageId(imgBean.getId());
			bean.setTitle(imgBean.getTitle());
			bean.setSmallName(File.separator
					+ StringUtils
							.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator + s_fileName);
			bean.setName(File.separator
					+ StringUtils
							.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator + fileName);
			bean.setUrl(PIC_SAVE_PATH);
			try {
				if (null != bean.getImageId()) {
					boolean b = picFiledao.insert(bean);
					if (b) {
						imgBean.setFileSize(Long.valueOf(length));
						imgBean.setLink("FD");
						if (imageDao.update(imgBean)) {
							System.out.println(">> 保存图片["
									+ imgBean.getHttpUrl() + "]成功");
						}
						client.add(CacheUtils.getBigPicFileKey(bean.getUrl()
								+ bean.getName()), bean);
						client.add(CacheUtils.getSmallPicFileKey(bean.getUrl()
								+ bean.getSmallName()), bean);
					} else {
						return false;
					}
				} else {
					return false;
				}
			} catch (Exception e) {
				System.out.println("数据库异常");
				e.printStackTrace();
				return false;
			}
		} catch (IOException e) {
			System.out.println("网络连接，文件IO异常");
			return false;
		}
		return true;
	}

	/**
	 * 处理有子菜单的页面
	 * @param bean
	 * @param content
	 * @throws Exception
	 */
	private static void doProcessSub(WebsiteBean bean,String content) throws Exception {
		try {
			getLinkByContent(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Iterator it = LINKHASH.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
//			System.out.println(" >> " + key);
			if(false){
				continue;
			}
			try {
				LinkBean link = (LinkBean) LINKHASH.get(key);
				Article acticle = null;
				if (null != client.get(link.getLink())) {
					System.err.println("缓存中已存在相同地址[" + link.getLink() + "]");
					continue;
				}
				acticle = new Article();
				acticle.setArticleUrl(link.getLink());
				acticle.setTitle(link.getName());
				acticle.setWebId(bean.getId());
				acticle.setText("NED");
					if (link.getLink().indexOf("_") == -1) {
						String xmlUrl = link.getLink().replace(".htm",
								".xml");
						if (xmlUrl != null) {
							acticle.setActicleXmlUrl(xmlUrl);
							acticle.setActicleRealUrl(link.getLink());
						} else {
							acticle.setActicleXmlUrl(null);
							acticle.setActicleRealUrl(null);
						}
					} else {
						// 新版本图片播放器解析方式，不需要在解析真实的URL地址，直接将地址中的XML文件解析并且从中获取必要的数据即可，
						String xmlUrl = HTMLParser.getXmlUrl2(link.getLink());
						if (xmlUrl != null) {
							acticle.setActicleXmlUrl(xmlUrl);
							acticle.setActicleRealUrl("2");
						} else {
							acticle.setActicleXmlUrl(null);
							acticle.setActicleRealUrl(null);
						}
					}
				int result = articleDao.insert(acticle);
				if (result > 0) {
					client.add(link.getLink(), link.getLink());
					acticle.setId(result);
					String aKey = CacheUtils.getArticleKey(result);
					if (null == client.get(aKey)) {
						client.add(aKey, acticle);
					}
				}else{
					acticle = articleDao.findByUrl(key);
					if(null != acticle){
						System.out.println(" >> "+acticle.getTitle()+"|"+acticle.getWebId());
						if(bean.getId() != acticle.getWebId()){
							acticle.setWebId(bean.getId());
							boolean b = articleDao.update(acticle);
							if(b){
								getImage(acticle.getId(), bean.getUrl());
								System.out.println(" >>  更新文章["+acticle.getTitle()+"]所属站点为["+bean.getId()+"|"+bean.getName()+"] 成功!");
							}
						}
					}
				}
			} catch (Exception e) {
				continue;
			}
		}

		LINKHASH.clear();
	}
	
	private static void doProcess(WebsiteBean bean) throws Exception {
		System.out.println(" >> url.id[" + bean.getId() + "] url.url["
				+ bean.getUrl() + "]");
		try {
			getLink(bean.getUrl());
		} catch (Exception e) {
		}
		Iterator it = LINKHASH.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			try {
				String tmpUrl = bean.getUrl().substring(0,
						bean.getUrl().lastIndexOf("/") + 1);
				LinkBean link = (LinkBean) LINKHASH.get(key);
				if (!link.getLink().startsWith(tmpUrl)) {
					System.out.println(" >> [" + link.getLink() + "] 不是以["
							+ tmpUrl + "]开头的");
					continue;
				}
				Article acticle = null;
				if (null != client.get(link.getLink())) {
					System.err.println("缓存中已存在相同地址[" + link.getLink() + "]");
					continue;
				}
				acticle = new Article();
				acticle.setArticleUrl(link.getLink());
				acticle.setTitle(link.getName());
				acticle.setWebId(bean.getId());
				acticle.setText("NED");
				System.out.println(" >> tmpUrl:" + tmpUrl);
				if (link.getLink().startsWith(tmpUrl)) {
					if (link.getLink().indexOf("_") == -1) {
						if (bean.getId() == 1633 || bean.getParentId() == 1636
								|| bean.getParentId() == 1644) {
							String xmlUrl = link.getLink().replace(".htm",
									".xml");
							if (xmlUrl != null) {
								acticle.setActicleXmlUrl(xmlUrl);
								acticle.setActicleRealUrl(link.getLink());
							} else {
								acticle.setActicleXmlUrl(null);
								acticle.setActicleRealUrl(null);
							}
						} else {
							// 老版本图片播放器解析方式
							String realUrl = HTMLParser.getRealUrl(link
									.getLink());
							String xmlUrl = HTMLParser.getXmlUrl(realUrl);
							if (xmlUrl != null) {
								acticle.setActicleXmlUrl(xmlUrl);
								acticle.setActicleRealUrl(realUrl);
							} else {
								acticle.setActicleXmlUrl(null);
								acticle.setActicleRealUrl(null);
							}
						}
					} else {
						// 新版本图片播放器解析方式，不需要在解析真实的URL地址，直接将地址中的XML文件解析并且从中获取必要的数据即可，
						String xmlUrl = HTMLParser.getXmlUrl2(link.getLink());
						if (xmlUrl != null) {
							acticle.setActicleXmlUrl(xmlUrl);
							acticle.setActicleRealUrl("2");
						} else {
							acticle.setActicleXmlUrl(null);
							acticle.setActicleRealUrl(null);
						}
					}
				} else {
					acticle.setActicleXmlUrl(null);
					acticle.setActicleRealUrl(null);
				}
				int result = articleDao.insert(acticle);
				if (result > 0) {
					 client.add(link.getLink(), link.getLink());
					acticle.setId(result);
					String aKey = CacheUtils.getArticleKey(result);
					 if (null == client.get(aKey)) {
						 client.add(aKey, acticle);
					 }
//					getImage(result, bean.getUrl());
				}
			} catch (Exception e) {
				continue;
			}
		}

		LINKHASH.clear();
	}
	
	static void getLink(String url) throws Exception {
		Parser p1 = new Parser();
		p1.setURL(url);
		p1.setEncoding("UTF-8");

		NodeFilter filter = new NodeClassFilter(LinkTag.class);
		NodeList list = p1.extractAllNodesThatMatch(filter);
		LinkBean bean = null;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				LinkTag link = (LinkTag) list.elementAt(i);
				System.out.println(link.getLinkText() + "\t" + link.getLink());
				bean = new LinkBean();
				bean.setLink(link.getLink());
				String name = StringUtils
						.illageString(link.getAttribute("title") == null ? (link
								.getLinkText() == null ? "无话题" : link
								.getLinkText())
								: link.getAttribute("title"));
				if (name.indexOf("“") != -1 || name.indexOf("”") != -1) {
					name = name.replaceAll("“", "");
					name = name.replace("”", "");
				}
				// 判断连接中是否存在创建文件夹时的非法字符
				if (name.indexOf("\"") != -1 && name.indexOf("\"") != -1) {
					name = name.replace("\"", "");
				}

				bean.setName(name);
				LINKHASH.put(bean.getLink(), bean);
			}
		}
		p1 = null;
	}
	
	/**
	 * 根据内容获取内容中的链接
	 * 
	 * @param content
	 * @throws Exception
	 */
	static void getLinkByContent(String content) throws Exception {
		Parser p1 = new Parser();
		p1.setInputHTML(content);
		p1.setEncoding("UTF-8");

		NodeFilter filter = new NodeClassFilter(LinkTag.class);
		NodeList list = p1.extractAllNodesThatMatch(filter);
		LinkBean bean = null;
		if (list != null && list.size() > 0) {
			int count = list.size();
			for (int i = 0; i < count; i++) {
				LinkTag link = (LinkTag) list.elementAt(i);
				bean = new LinkBean();
				bean.setLink(link.getLink().replaceAll("\"","").replaceAll("\\\\","").replace("//html", "/html"));
				String title = link.getAttribute("title");
				String text = link.getLinkText();
				String name = StringUtils
						.illageString(link.getAttribute("title") == null ? (text.replaceAll("\"","").replaceAll("\\\\","").replace("//html", "/html") == null ? "无话题" : text.replaceAll("\"","").replaceAll("\\\\","").replace("//html", "/html"))
								: title.replaceAll("\"","").replaceAll("\\\\","").replace("//html", "/html"));
				if (name.indexOf("“") != -1 || name.indexOf("”") != -1) {
					name = name.replaceAll("“", "");
					name = name.replace("”", "");
				}
				// 判断连接中是否存在创建文件夹时的非法字符
				if (name.indexOf("\"") != -1 && name.indexOf("\"") != -1) {
					name = name.replace("\"", "");
				}
				if(null == name || "".equals(name)){
					continue;
				}
				bean.setName(name);
				LINKHASH.put(bean.getLink(), bean);
			}
		}
		if(null != p1)
			p1 = null;
	}
	
	/**
	 * 获取图片连接数据
	 * @throws Exception
	 */
	static void getArticleImages() throws Exception{
		List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : list) {
			getArticleImages2(bean);
		}
	}
	
	static void getArticleImages2(WebsiteBean web) throws Exception{
		List<WebsiteBean> list = webSiteDao.findByParentId(web.getId());
		if(list.size() > 0){
			for (WebsiteBean bean : list) {
				getArticleImages2(bean);
			}
		}else{ 
			System.out.println(" >> 站点名称["+web.getName()+"],站点ID["+web.getId()+"]");
			List<Article> alist = articleDao.findByWebId(web.getId()); //, "NED"
//			System.out.println(" >> 文章数量:"+alist.size());
			for(Article a:alist){
				getImage(a.getId(), web.getUrl());
			}
		}
	}
	
	public static void update2() throws Exception {
		List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : list) {
			if(bean.getId() == 1663){
				continue;
			}
			update3(bean);
		}

	}
	
	public static void update3(WebsiteBean web) throws Exception{
		List<WebsiteBean> list = webSiteDao.findByParentId(web.getId());
		if(list.size() > 0){
			for (WebsiteBean bean : list) {
				update3(bean);
			}
		}else{ 
			// if(list.size() == 0)
			System.out.println(" >> 站点:"+web.getName()+"|"+web.getUrl()+"|子站点:"+web.getChild().size());
//			if(HttpClientUtils.validationURL(web.getUrl())){
//				catalog(web.getUrl(),web.getId());
//			}
			String id = getId(web.getUrl());
			String content = HttpClientUtils.getResponseBody(
					CATALOGS_URL.replace("{id}", id), "UTF-8");
			if(null != content && !"".equals(content)){
				doProcessSub(web,content);
			}
			if(true){
				//更新站点修改时间
				webSiteDao.update(web);
			}
		}
	}
	
	private static String getId(String url) {
		String id = null;
		int start = url.lastIndexOf("/");
		int end = url.lastIndexOf("_");
		id = url.substring(start + 1, end);
		if(id.indexOf("-") != -1){
			start = id.lastIndexOf("-");
			System.out.println(" \t\t>> start:"+start);
			id = id.substring(start+1,id.length());
			System.out.println(" \t\t>> id:"+id);
		}
		return id;
	}
	
	/**
	 * 下载图片文件
	 * @throws Exception
	 */
	static void downloadImages() throws Exception{
		List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : list) {
			System.out.println(" >> 站点名称["+bean.getName()+"],站点ID["+bean.getId()+"]");
			downloadImages2(bean);
		}
	}
	
	/**
	 * 下载图片文件
	 * @throws Exception
	 */
	static void downloadImages2(WebsiteBean web) throws Exception{
		List<WebsiteBean> list = webSiteDao.findByParentId(web.getId());
		if(list.size() > 0){
			for (WebsiteBean bean : list) {
				downloadImages2(bean);
			}
		}else{ 
			System.out.println(" >> 站点名称["+web.getName()+"],站点ID["+web.getId()+"]");
			List<Article> alist = articleDao.findByWebId(web.getId(), "FD");
			for(Article a:alist){
				System.out.println(" >> "+a.getId()+"|"+a.getTitle());
				List<ImageBean> mlist = imageDao.findImage(a.getId());
				for(ImageBean img:mlist){
					if(img.getLink().equals("NED")){
						imgDownload(img, web.getId());
					}
				}
			}
		}
	}
	
	public static void  main(String args[]){
		try{
			
//			catalogChina("http://tuku.auto.china.com/auto/html/4466/4466-4467_1.html");
//			catalogWorld("http://tuku.auto.china.com/auto/html/4466/4466-4468_1.html");

			update2();
//			getArticleImages();
//			downloadImages();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
