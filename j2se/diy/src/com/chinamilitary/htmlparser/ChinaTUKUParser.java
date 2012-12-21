package com.chinamilitary.htmlparser;

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
import com.utils.FileUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class ChinaTUKUParser {

	static Log log = LogFactory.getLog(ChinaTUKUParser.class);
	
	static final String URL_ = "http://tuku.china.com/";

	final static String FILE_SERVER = "D:\\fileserver\\image\\";

	static int D_PARENT_ID = 300;

	// "http://tuku.tech.china.com/tech",
	static final String[] CATALOG_URL = {
			"http://pic.news.china.com/news/",
			"http://tuku.kaiyun.china.com/kaiyun/",
			"http://tuku.ent.china.com/fun/",
			"http://tuku.military.china.com/military/",
			"http://tuku.game.china.com/game/",
			"http://tuku.fun.china.com/fun/",
			"http://pic.news.china.com/social/",
			"http://pic.sports.china.com/sports/",
			"http://tuku.travel.china.com/travel/",
			"http://tuku.auto.china.com/auto/",
			"http://tuku.culture.china.com/culture/" };

	static MemcacheClient client = MemcacheClient.getInstance();

	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	static final String PIC_SAVE_PATH = Constants.FILE_SERVER;

	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	// http://rtuku.club.china.com/user/channelPageTypeIndexAjaxAction.do?channelId=baobao&pageNum=1&eachRowNum=4&eachPageNum=40&typeId=23670&tagId=0
	static String CATALOGS_URL = "http://rtuku.club.china.com/user/channelPageTypeIndexAjaxAction.do?channelId=baobao&pageNum=1&eachRowNum=4&eachPageNum=10000&typeId={id}&tagId=0";
	static String CATALOGS_AUTO_URL = "http://rtuku.club.china.com/user/channelPageTypeIndexAjaxAction.do?channelId=auto&pageNum=1&eachRowNum=4&eachPageNum=10000&typeId={id}&tagId=0";

    private static ScheduledExecutorService exec =
        Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, "CHINAMILITARY-thread");
            }
        });
    
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

	static void getImage(Integer id, String url) throws Exception {
		Article article = null;
		String aKey = CacheUtils.getArticleKey(id);
		try {
			if (null != client.get(aKey)) {
				article = (Article) client.get(aKey);
			}
			if (null == article) {
				article = articleDao.findById(id);
				if (null != article)
					client.add(aKey, article);
			}
			if (article != null) {
				String tmpUrl = url;
				if(url.indexOf("/html") != -1){
					tmpUrl = url.substring(0, url.lastIndexOf("/html") + 1)+"html/";
					
				}
				if ((article.getArticleUrl().startsWith(tmpUrl))) {
					if (article.getActicleXmlUrl() != null
							|| !article.getActicleXmlUrl().equalsIgnoreCase("")) {
						List<ImageBean> imgList = XMLParser
								.readXmlFromURL(article.getActicleXmlUrl());
						int offset = 0;
						for (ImageBean bean : imgList) {
							String length = HttpClientUtils.getHttpConentLength(bean.getHttpUrl());
							log.info(">> Content-Length:" + length);
							if (null != length) {
								bean.setFileSize(Long.valueOf(length));
								bean.setStatus(1);
							}
							if (client.get(bean.getHttpUrl()) == null) {
								bean.setArticleId(article.getId());
								int result = imageDao.insert(bean);
								if (result != -1) {
									offset++;
								} 
							}
						}
						if(offset == imgList.size()){
							article.setText("NED");
							if (articleDao.update(article)) {
								log.info("更新记录[" + article.getId() + "|"
										+ article.getTitle() + "]成功");
							}
						}
						offset = 0;
					}
				}
			} else {
				log.error("未查询到ARTICLE记录");
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
				log.debug(">>> 尝试使用Referer来获取图片");
				big = HttpClientUtils.getResponseBodyAsByte(imgBean
						.getHttpUrl(), null, imgBean.getHttpUrl());
				if (null == big)
					return false;
				length = String.valueOf(big.length);
				if (length.equalsIgnoreCase("20261")
						|| length.equalsIgnoreCase("3267")
						|| length.equalsIgnoreCase("4096")) {
					log.error("下载被屏蔽，未突破盗链系统...");
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
							log.debug(">> 保存图片["
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
				log.error("数据库异常");
				e.printStackTrace();
				return false;
			}
		} catch (IOException e) {
			log.error("网站ID["+webId+"]的网络连接错误，出现文件IO异常");
			return false;
		}
		return true;
	}
	
	public static void start() {
		index();
		try {
			update2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		start();
		/**
		final String url = "http://pic.6188.com/upload_6188s/flashAll/20120425/1335318189tdRPLz.jpg"; 
		new Thread(new Runnable(){
			public void run(){
				try {
					long start = System.currentTimeMillis();
					int length = HttpClientUtils.getBodyLength(url,
							"User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
					log.debug(" > 获取文件长度耗时:" + (System.currentTimeMillis() - start));
					if(length != -1){
						start = System.currentTimeMillis();
						log.debug(" body.length:"+length);
						byte[] body = HttpClientUtils.getBody(url);
						if(null != body){
							long spend = (System.currentTimeMillis() - start)/1000;
							log.debug(" >　耗时:" + (spend) + " 下载["+body.length+"]的文件,下载速度:"+((length/1024)/spend)+" kb/s");
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		**/
	}

	public static void index() {
		try {
			getLink("http://www.china.com");
			Iterator it = LINKHASH.keySet().iterator();
			Article acticle = null;
			while (it.hasNext()) {
				String key = (String) it.next();
				WebsiteBean tmp = webSiteDao.findByUrl(key);
				if (null != tmp) {
					LinkBean bean = LINKHASH.get(key);
					if (!bean.getLink().startsWith(tmp.getUrl())) {
						continue;
					}
					try {
						acticle = new Article();
						acticle.setArticleUrl(bean.getLink());
						acticle.setTitle(bean.getName());
						acticle.setWebId(tmp.getId());
						acticle.setText("NED");
						if (bean.getLink().startsWith(tmp.getUrl())) {
							if (bean.getLink().indexOf("_") == -1) {
								if (tmp.getId() == 1633) {
									String xmlUrl = bean.getLink().replace(
											".htm", ".xml");
									if (xmlUrl != null) {
										acticle.setActicleXmlUrl(xmlUrl);
										acticle.setActicleRealUrl(bean
												.getLink());
									} else {
										acticle.setActicleXmlUrl(null);
										acticle.setActicleRealUrl(null);
									}
								} else {
									// 老版本图片播放器解析方式
									String realUrl = HTMLParser.getRealUrl(bean
											.getLink());
									String xmlUrl = HTMLParser
											.getXmlUrl(realUrl);
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
								String xmlUrl = HTMLParser.getXmlUrl2(bean
										.getLink());
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
							log.info("\t>>> a:"+acticle.getTitle()+"|"+acticle.getArticleUrl()+"|id="+result);
							client.add(bean.getLink(), bean.getLink());
							acticle.setId(result);
							String aKey = CacheUtils.getArticleKey(result);
							if (null == client.get(aKey)) {
								client.add(aKey, acticle);
								getImage(result, tmp.getUrl());
							}
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	public static void update2() throws Exception {
		List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : list) {
			if(bean.getId() == 1633 || bean.getParentId() == 1633 || bean.getId() == 1644 || bean.getParentId() == 1644 || bean.getId() == 1636 || bean.getParentId() == 1636 || bean.getId() == 1664 || bean.getParentId() == 1664){ //|| bean.getId() == 1636 && bean.getId() == 1644
				continue;
			}
			switch(bean.getId()){
				case 1633:
				case 1636:
				case 1644:
					List<WebsiteBean> child = webSiteDao.findByParentId(bean
							.getId());
					for (WebsiteBean tmp : child) {
						if(tmp.getId() != 1663){
							continue;
						}
						List<WebsiteBean> c2 = webSiteDao.findByParentId(tmp.getId());
						if(c2.size() > 0){
							for(WebsiteBean t2 : c2){
								List<WebsiteBean> c3 = webSiteDao.findByParentId(t2.getId());
								if(c3.size() > 0){
									for(WebsiteBean t3 : c3){
										List<WebsiteBean> c4 = webSiteDao.findByParentId(t3.getId());
										if(c4.size() > 0){
											for(WebsiteBean t4 : c4){
												// 单独处理子项逻辑
												String id = getId(t4.getUrl());
												String content = HttpClientUtils.getResponseBody(
														CATALOGS_URL.replace("{id}", id), "UTF-8");
												if(null != content && !"".equals(content)){
													doProcessSub(t4,content);
												}
											}
										}else{
											// 单独处理子项逻辑
											String id = getId(t3.getUrl());
											String content = HttpClientUtils.getResponseBody(
													CATALOGS_URL.replace("{id}", id), "UTF-8");
											if(null != content && !"".equals(content)){
												doProcessSub(t3,content);
											}
										}
									}
								}else{
									// 单独处理子项逻辑
									String id = getId(t2.getUrl());
									String content = HttpClientUtils.getResponseBody(
											CATALOGS_URL.replace("{id}", id), "UTF-8");
									if(null != content && !"".equals(content)){
										doProcessSub(t2,content);
									}
								}
							}
						}else{
							// 单独处理子项逻辑
							String id = getId(tmp.getUrl());
							String content = HttpClientUtils.getResponseBody(
									CATALOGS_URL.replace("{id}", id), "UTF-8");
							if(null != content && !"".equals(content)){
								doProcessSub(tmp,content);
							}
						}
					}
					break;
				default:
					doProcess(bean);
					break;
			}
			
			if(true){
				//更新站点修改时间
				webSiteDao.update(bean);
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
			log.debug(" \t\t>> start:"+start);
			id = id.substring(start+1,id.length());
			log.debug(" \t\t>> id:"+id);
		}
		return id;
	}

	static List<String> getPage(String url) throws Exception {
		List<String> page = new ArrayList<String>();
		Parser parser = new Parser();
		parser.setURL(url);

		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "pages borTop"));
		if (list != null && list.size() > 0) {
			Div div = (Div) list.elementAt(0);
			log.debug(" >> " + div.toHtml());
		}
		page.add(url);
		if (null != parser)
			parser = null;
		return page;
	}

	private static void doProcess(WebsiteBean bean) throws Exception {
		long ss = System.currentTimeMillis();
		log.debug(" >> web.id[" + bean.getId() + "] web.name["+bean.getName()+"] web.url["+ bean.getUrl() + "]");
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
					continue;
				}
				Article acticle = null;
				if (null != client.get(link.getLink())) {
					continue;
				}
				acticle = new Article();
				acticle.setArticleUrl(link.getLink());
				acticle.setTitle(link.getName());
				acticle.setWebId(bean.getId());
				acticle.setText("NED");
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
						ss = System.currentTimeMillis();
						// 新版本图片播放器解析方式，不需要在解析真实的URL地址，直接将地址中的XML文件解析并且从中获取必要的数据即可，
						String xmlUrl = HTMLParser.getXmlUrl2(link.getLink());
						log.debug("\t>>>>> HTMLParser.getXmlUrl2 耗时:["+(System.currentTimeMillis()-ss)+"]ms");
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
				ss = System.currentTimeMillis();
				int result = articleDao.insert(acticle);
				if (result > 0) {
					log.info("\t>>> a:"+acticle.getTitle()+"|"+acticle.getArticleUrl()+"|id="+result+"\t耗时:["+(System.currentTimeMillis()-ss)+"]ms");
					client.add(link.getLink(), link.getLink());
					acticle.setId(result);
					String aKey = CacheUtils.getArticleKey(result);
					 if (null == client.get(aKey)) {
						 client.add(aKey, acticle);
						 ss = System.currentTimeMillis();
						 getImage(result, bean.getUrl());
						 log.debug("\t>>>>> getImage 耗时:["+(System.currentTimeMillis()-ss)+"]ms");
					 }
				}
			} catch (Exception e) {
				continue;
			}
		}

		LINKHASH.clear();
	}

	/**
	 * 处理有子菜单的页面
	 * @param bean
	 * @param content
	 * @throws Exception
	 */
	private static void doProcessSub(WebsiteBean bean,String content) throws Exception {
		long ss = System.currentTimeMillis();
		try {
			getLinkByContent(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Iterator it = LINKHASH.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			try {
				String tmpUrl = bean.getUrl().substring(0,
						bean.getUrl().lastIndexOf("/html") + 1)+"html/";
				log.debug(""+tmpUrl);
				LinkBean link = (LinkBean) LINKHASH.get(key);
				if (!link.getLink().startsWith(tmpUrl)) {
					log.debug(" >> [" + link.getLink() + "] 不是以["
							+ tmpUrl + "]开头的");
					continue;
				}
				Article acticle = null;
				if (null != client.get(link.getLink())) {
					log.error("缓存中已存在相同地址[" + link.getLink() + "]");
					continue;
				}
				acticle = new Article();
				acticle.setArticleUrl(link.getLink());
				acticle.setTitle(link.getName());
				acticle.setWebId(bean.getId());
				acticle.setText("NED");
				log.debug(" >> linlUrl:["+link.getLink()+"] |tmpUrl:" + tmpUrl);
				if (link.getLink().startsWith(tmpUrl)) {
					if (link.getLink().indexOf("_") == -1) {
						if (bean.getId() == 1633 || bean.getParentId() == 1636
								|| bean.getParentId() == 1644 || bean.getId() == 1664 || bean.getId() == 1665) {
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
						log.debug("\t>>>> HTMLParser.getXmlUrl2 耗时:["+(System.currentTimeMillis()-ss)+"]ms");
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
				ss = System.currentTimeMillis();
				int result = articleDao.insert(acticle);
				if (result > 0) {
					log.info("\t>>> a:"+acticle.getTitle()+"|"+acticle.getArticleUrl()+"|id="+result+"\t耗时:["+(System.currentTimeMillis()-ss)+"]");
					client.add(link.getLink(), link.getLink());
					acticle.setId(result);
					String aKey = CacheUtils.getArticleKey(result);
					 if (null == client.get(aKey)) {
						 ss = System.currentTimeMillis();
						 getImage(result, bean.getUrl());
						 log.debug("\t>>>>> getImage 耗时:["+(System.currentTimeMillis()-ss)+"]");
						 client.add(aKey, acticle);
					 }
				}
			} catch (Exception e) {
				continue;
			}
		}

		LINKHASH.clear();
	}

	/**
	 * 获取图片连接数据
	 * @throws Exception
	 */
	static void getArticleImages() throws Exception{
		List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : list) {
			log.debug(" >> 站点名称["+bean.getName()+"],站点ID["+bean.getId()+"]");
			if(bean.getId() == 1633 || bean.getParentId() == 1633 || bean.getId() == 1644 || bean.getParentId() == 1644 || bean.getId() == 1636 || bean.getParentId() == 1636 || bean.getId() == 1664 || bean.getParentId() == 1664){ //|| bean.getId() == 1636 && bean.getId() == 1644
				continue;
			}
			List<Article> alist = articleDao.findByWebId(bean.getId(), "NED");
			for(Article a:alist){
				getImage(a.getId(), bean.getUrl());
			}
			
		}
	}
	
	/**
	 * 下载图片文件
	 * @throws Exception
	 */
	static void downloadImages() throws Exception{
		List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : list) {
			log.debug(" >> 站点名称["+bean.getName()+"],站点ID["+bean.getId()+"]");
			if(bean.getId() == 1636 || bean.getParentId() == 1636 || bean.getId() == 1633 || bean.getParentId() == 1633 || bean.getId() == 1644 || bean.getParentId() == 1644 || bean.getId() == 1664 || bean.getParentId() == 1664){ //|| bean.getId() == 1636 && bean.getId() == 1644
				continue;
			}
			List<Article> alist = articleDao.findByWebId(bean.getId(), "FD");
			for(Article a:alist){
				log.debug(" >> "+a.getId()+"|"+a.getTitle());
				List<ImageBean> mlist = imageDao.findImage(a.getId());
				for(ImageBean img:mlist){
					if(img.getLink().equals("NED")){
					if(imgDownload(img, bean.getId())){
					}
					}
				}
			}
			
		}
	}

	public static void parserAuto(String url) throws Exception {
		Parser p1 = new Parser();
		p1.setURL(url);
		p1.setEncoding("UTF-8");

		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = p1.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "ImgHolder"));
		if (null != list && list.size() > 0) {
			int count = list.size();
			log.debug(" >> auto.size:" + count);
			for (int i = 0; i < count; i++) {
				Div div = (Div) list.elementAt(i);
				log.debug(" >> " + div.toHtml());
			}
		}

		if (null != p1) {
			p1 = null;
		}
	}
	
	public static void parserSubContent(String content) throws Exception {
		Parser p1 = new Parser();
		p1.setInputHTML(content);
		p1.setEncoding("UTF-8");

		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = p1.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "ImgHolder"));
		if (null != list && list.size() > 0) {
			int count = list.size();
			for (int i = 0; i < count; i++) {
				Div div = (Div) list.elementAt(i);
				log.debug(" >> " + div.toHtml());
			}
		}

		if (null != p1) {
			p1 = null;
		}

	}

	public static void update() throws Exception {
		for (String url : CATALOG_URL) {
			try {
				getLink(url);
			} catch (Exception e) {
				continue;
			}
			Iterator it = LINKHASH.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				log.debug("key:" + key);
				try {
					LinkBean bean = (LinkBean) LINKHASH.get(key);
					Article acticle = null;
					if (null != client.get(bean.getLink())) {
						System.err
								.println("缓存中已存在相同地址[" + bean.getLink() + "]");
						continue;
					}
					if (HttpClientUtils.validationURL(bean.getLink())) {
						acticle = new Article();
						acticle.setArticleUrl(bean.getLink());
						acticle.setTitle(bean.getName());
						acticle.setWebId(301);
						acticle.setText("NED");
						if (bean.getLink().startsWith(
								"http://tuku.kaiyun.china.com/kaiyun/")
								|| bean.getLink().startsWith(
										"http://tuku.ent.china.com/fun/")
								|| bean.getLink().startsWith(
										"http://tuku.news.china.com/")
								|| bean.getLink().startsWith(
										"http://tuku.military.china.com/")
								|| bean.getLink().startsWith(
										"http://tuku.fun.china.com/")
								|| bean.getLink().startsWith(
										"http://tuku.game.china.com/")
								|| bean.getLink().startsWith(
										"http://pic.news.china.com/")
								|| bean.getLink().startsWith(
										"http://tuku.tech.china.com/")
								|| bean.getLink().startsWith(
										"http://pic.news.china.com/news/")
								|| bean.getLink().startsWith(
										"http://pic.sports.china.com/")
								|| bean.getLink().startsWith(
										"http://tuku.travel.china.com/")
								|| bean
										.getLink()
										.startsWith(
												"http://tuku.auto.china.com/auto/html/")
								|| bean.getLink().startsWith(
										"http://tuku.culture.china.com/")) {
							if (bean.getLink().indexOf("_") == -1) {
								// 老版本图片播放器解析方式
								String realUrl = HTMLParser.getRealUrl(bean
										.getLink());
								log.debug("老版本图片播放器解析：" + realUrl);
								String xmlUrl = HTMLParser.getXmlUrl(realUrl);
								if (xmlUrl != null) {
									acticle.setActicleXmlUrl(xmlUrl);
									acticle.setActicleRealUrl(realUrl);
								} else {
									acticle.setActicleXmlUrl(null);
									acticle.setActicleRealUrl(null);
								}
							} else {
								// 新版本图片播放器解析方式，不需要在解析真实的URL地址，直接将地址中的XML文件解析并且从中获取必要的数据即可，
								String xmlUrl = HTMLParser.getXmlUrl2(bean
										.getLink());
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
							log.info("\t>>> a:"+acticle.getTitle()+"|"+acticle.getArticleUrl()+"|id="+result);
							client.add(bean.getLink(), bean.getLink());
							acticle.setId(result);
							String aKey = CacheUtils.getArticleKey(result);
							if (null == client.get(aKey)) {
								client.add(aKey, acticle);
							}
							getImage(result, url);
						}
					} else {
						log.error(">> getActicle 地址暂不可用 ["
								+ bean.getLink() + "]");
						continue;
					}
				} catch (Exception e) {
					continue;
				}
			}

			LINKHASH.clear();
		}

	}

	/**
	 * 更新记录
	 */
	public static void patch() throws Exception {
		List<WebsiteBean> wlist = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : wlist) {
			List<Article> list = articleDao.findByWebId(bean.getId(), "NED");
			if (null != list || list.size() > 0) {
				for (Article article : list) {
					try {
						if (article.getArticleUrl().indexOf("_") == -1) { // &&
							// html.length()
							// > 6
							// 老版本图片播放器解析方式
							String realUrl = HTMLParser.getRealUrl(article
									.getArticleUrl());
							log.debug("老版本图片播放器解析：" + realUrl);
							String xmlUrl = HTMLParser.getXmlUrl(realUrl);
							if (xmlUrl != null) {
								article.setActicleXmlUrl(xmlUrl);
								article.setActicleRealUrl(realUrl);
							} else {
								article.setActicleXmlUrl(null);
								article.setActicleRealUrl(null);
							}
						} else {
							// 新版本图片播放器解析方式，不需要在解析真实的URL地址，直接将地址中的XML文件解析并且从中获取必要的数据即可，
							String xmlUrl = HTMLParser.getXmlUrl2(article
									.getArticleUrl());
							if (xmlUrl != null) {
								article.setActicleXmlUrl(xmlUrl);
								article.setActicleRealUrl("2");
							} else {
								article.setActicleXmlUrl(null);
								article.setActicleRealUrl(null);
							}
						}
						if (articleDao.update(article)) {
							getImage(article.getId(), bean.getUrl());
						}
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}
	}

	/**
	 * 初始化文章缓存数据
	 * 
	 */
	public static void init() {
		try {
			List<Article> alist = articleDao.findAll();
			for (Article art : alist) {
				if (null == client.get(art.getArticleUrl())) {
					client.add(art.getArticleUrl(), art.getArticleUrl());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void movefile() throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		PicfileBean bean = null;
		for (WebsiteBean website : webList) {
			log.debug(website.getId() + "|" + website.getName() + "|"
					+ website.getUrl());
			List<Article> artList = articleDao.findByWebId(website.getId(),
					"FD");
			log.debug("文章数量:" + artList.size());
			for (Article article : artList) {
				List<ImageBean> list = imageDao.findImage(article.getId());
				for (ImageBean img : list) {
					bean = picFiledao.findByImgIdAndArticleId(img.getId(),
							article.getId());
					if (null != bean) {
						if (moveFile(bean)) {
							log.debug(bean.getId() + "|"
									+ bean.getArticleId() + "|"
									+ bean.getImageId());
							log.debug("after move file name:"
									+ bean.getName());
							log.debug("after move file smallName:"
									+ bean.getSmallName());
							System.out
									.println("-------------------------------------------------------------");
						}
					}
					// break;
				}
				// break;
			}
			// break;
		}
	}

	static boolean moveFile(PicfileBean bean) {
		String tmpUrl = "P:\\share\\file\\zol\\";
		boolean isBig = false;
		boolean isSmall = false;
		int start = bean.getName().lastIndexOf(File.separator) + 1;
		int smnStart = bean.getSmallName().lastIndexOf(File.separator) + 1;
		String prx = StringUtils.gerDir(String.valueOf(bean.getArticleId()));
		String fileName = prx + bean.getArticleId() + File.separator
				+ bean.getName().substring(start);
		String smallFileName = prx + bean.getArticleId() + File.separator
				+ bean.getSmallName().substring(smnStart);
		String source = tmpUrl + bean.getName();
		String smgSource = tmpUrl + bean.getSmallName();
		String target = FILE_SERVER + fileName;
		String smgTarget = FILE_SERVER + smallFileName;
		bean.setUrl(FILE_SERVER);
		if (FileUtils.copyFile(source, target)) {
			log.debug(">> 大图成功!!!");
			if (FileUtils.deleteFile(source)) {
				log.debug(">> 删除源大图[" + source + "]成功");
			}
			bean.setName(fileName);
			isBig = true;
		}

		if (FileUtils.copyFile(smgSource, smgTarget)) {
			log.debug(">> 小图成功!!!");
			if (FileUtils.deleteFile(smgSource)) {
				log.debug(">> 删除源小图[" + smgSource + "]成功");
			}
			bean.setSmallName(smallFileName);
			isSmall = true;
		}
		if (isBig) {
			if (isBig || isSmall) {
				try {
					if (picFiledao.update(bean)) {
						log.debug(">> 更新图片文件[" + bean.getId()
								+ "]记录成功!");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return isBig;
	}
}
