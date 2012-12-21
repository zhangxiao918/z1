package org.bluestome.pcs.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluestome.pcs.bean.Article;
import org.bluestome.pcs.bean.ImageBean;
import org.bluestome.pcs.bean.LinkBean;
import org.bluestome.pcs.bean.WebsiteBean;
import org.bluestome.pcs.common.Constants;
import org.bluestome.pcs.common.factory.DAOFactory;
import org.bluestome.pcs.dao.ArticleDao;
import org.bluestome.pcs.dao.ImageDao;
import org.bluestome.pcs.dao.WebSiteDao;
import org.bluestome.pcs.memcache.MemcacheClient;
import org.bluestome.pcs.utils.HttpClientUtils;
import org.bluestome.pcs.utils.StringUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

public class ChinaTUKUParser {

	static Log log = LogFactory.getLog(ChinaTUKUParser.class);

	static final String URL_ = "http://tuku.china.com/";

	final static String FILE_SERVER = "D:\\fileserver\\image\\";

	static int D_PARENT_ID = 300;

	static final String[] CATALOG_URL = { "http://pic.news.china.com/news/",
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

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	static final String PIC_SAVE_PATH = Constants.FILE_SERVER;

	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	static String CATALOGS_URL = "http://rtuku.club.china.com/user/channelPageTypeIndexAjaxAction.do?channelId=baobao&pageNum=1&eachRowNum=4&eachPageNum=10000&typeId={id}&tagId=0";

	static String CATALOGS_AUTO_URL = "http://rtuku.club.china.com/user/channelPageTypeIndexAjaxAction.do?channelId=auto&pageNum=1&eachRowNum=4&eachPageNum=10000&typeId={id}&tagId=0";

	private static ScheduledExecutorService exec = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactory() {
				public Thread newThread(Runnable r) {
					return new Thread(r, "CHINAMILITARY-thread");
				}
			});

	private static void getLink(String url) throws Exception {
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
	private static void getLinkByContent(String content) throws Exception {
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
				bean.setLink(link.getLink().replaceAll("\"", "").replaceAll(
						"\\\\", "").replace("//html", "/html"));
				String title = link.getAttribute("title");
				String text = link.getLinkText();
				String name = StringUtils
						.illageString(link.getAttribute("title") == null ? (text
								.replaceAll("\"", "").replaceAll("\\\\", "")
								.replace("//html", "/html") == null ? "无话题"
								: text.replaceAll("\"", "").replaceAll("\\\\",
										"").replace("//html", "/html"))
								: title.replaceAll("\"", "").replaceAll("\\\\",
										"").replace("//html", "/html"));
				if (name.indexOf("“") != -1 || name.indexOf("”") != -1) {
					name = name.replaceAll("“", "");
					name = name.replace("”", "");
				}
				// 判断连接中是否存在创建文件夹时的非法字符
				if (name.indexOf("\"") != -1 && name.indexOf("\"") != -1) {
					name = name.replace("\"", "");
				}
				if (null == name || "".equals(name)) {
					continue;
				}
				bean.setName(name);
				LINKHASH.put(bean.getLink(), bean);
			}
		}
		if (null != p1)
			p1 = null;
	}

	private static void getImage(Integer id, String url) throws Exception {
		Article article = null;
		boolean isDownload = false;
		try {
			if (null == article) {
				article = articleDao.findById(id);
			}
			if (article != null) {
				String tmpUrl = url;
				if (url.indexOf("/html") != -1) {
					tmpUrl = url.substring(0, url.lastIndexOf("/html") + 1)
							+ "html/";

				}
				if ((article.getArticleUrl().startsWith(tmpUrl))) {
					if (article.getActicleXmlUrl() != null
							|| !article.getActicleXmlUrl().equalsIgnoreCase("")) {
						List<ImageBean> imgList = XMLParser
								.readXmlFromURL(article.getActicleXmlUrl());
						int offset = 0;
						for (ImageBean bean : imgList) {
							if (client.get(bean.getHttpUrl()) == null) {
								bean.setArticleId(article.getId());
								// HttpClientUtils
								int result = imageDao.insert(bean);
								if (result != -1) {
									offset++;
								}
							}
						}
						// 判断获取的数据和列表中的数据是否一致，如果数据量一致，则表示下载成功，可以更新文章
						if (offset == imgList.size()) {
							isDownload = true;
						}

						if (isDownload) {
							log.info(" >> 更新文章[" + article.getId() + "|"
									+ article.getTitle() + "]状态为'已完成'状态!");
							article.setText("FD");
							articleDao.update(article);
						} else {
							log.error(" >> 更新文章[" + article.getId() + "|"
									+ article.getTitle() + "]状态为'无更新'状态!");
							article.setText("NNN");
							articleDao.update(article);
						}
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

	public static void start() {
		try{
		index();
		update2();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		start();
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
						log.info(" > a:" + acticle.getTitle() + "|"
								+ acticle.getArticleUrl());
						int result = articleDao.insert(acticle);
						if (result > 0) {
							client.add(bean.getLink(), bean.getLink());
							acticle.setId(result);
							getImage(result, tmp.getUrl());
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
			if (bean.getId() == 1633 || bean.getParentId() == 1633
					|| bean.getId() == 1644 || bean.getParentId() == 1644
					|| bean.getId() == 1636 || bean.getParentId() == 1636
					|| bean.getId() == 1664 || bean.getParentId() == 1664) {
				continue;
			}
			switch (bean.getId()) {
			case 1633:
			case 1636:
			case 1644:
				List<WebsiteBean> child = webSiteDao.findByParentId(bean
						.getId());
				for (WebsiteBean tmp : child) {
					if (tmp.getId() != 1663) {
						continue;
					}
					List<WebsiteBean> c2 = webSiteDao.findByParentId(tmp
							.getId());
					if (c2.size() > 0) {
						for (WebsiteBean t2 : c2) {
							List<WebsiteBean> c3 = webSiteDao.findByParentId(t2
									.getId());
							if (c3.size() > 0) {
								for (WebsiteBean t3 : c3) {
									List<WebsiteBean> c4 = webSiteDao
											.findByParentId(t3.getId());
									if (c4.size() > 0) {
										for (WebsiteBean t4 : c4) {
											// 单独处理子项逻辑
											String id = getId(t4.getUrl());
											String content = HttpClientUtils
													.getResponseBody(
															CATALOGS_URL
																	.replace(
																			"{id}",
																			id),
															"UTF-8");
											if (null != content
													&& !"".equals(content)) {
												doProcessSub(t4, content);
											}
										}
									} else {
										// 单独处理子项逻辑
										String id = getId(t3.getUrl());
										String content = HttpClientUtils
												.getResponseBody(CATALOGS_URL
														.replace("{id}", id),
														"UTF-8");
										if (null != content
												&& !"".equals(content)) {
											doProcessSub(t3, content);
										}
									}
								}
							} else {
								// 单独处理子项逻辑
								String id = getId(t2.getUrl());
								String content = HttpClientUtils
										.getResponseBody(CATALOGS_URL.replace(
												"{id}", id), "UTF-8");
								if (null != content && !"".equals(content)) {
									doProcessSub(t2, content);
								}
							}
						}
					} else {
						// 单独处理子项逻辑
						String id = getId(tmp.getUrl());
						String content = HttpClientUtils.getResponseBody(
								CATALOGS_URL.replace("{id}", id), "UTF-8");
						if (null != content && !"".equals(content)) {
							doProcessSub(tmp, content);
						}
					}
				}
				break;
			default:
				doProcess(bean);
				break;
			}

			if (true) {
				// 更新站点修改时间
				webSiteDao.update(bean);
			}
		}

	}

	private static String getId(String url) {
		String id = null;
		int start = url.lastIndexOf("/");
		int end = url.lastIndexOf("_");
		id = url.substring(start + 1, end);
		if (id.indexOf("-") != -1) {
			start = id.lastIndexOf("-");
			log.debug(" \t\t>> start:" + start);
			id = id.substring(start + 1, id.length());
			log.debug(" \t\t>> id:" + id);
		}
		return id;
	}

	private static void doProcess(WebsiteBean bean) throws Exception {
		log.debug(" >> web.id[" + bean.getId() + "] web.name[" + bean.getName()
				+ "] web.url[" + bean.getUrl() + "]");
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
				log.info(" > a:" + acticle.getTitle() + "|"
						+ acticle.getArticleUrl());
				int result = articleDao.insert(acticle);
				if (result > 0) {
					client.add(link.getLink(), link.getLink());
					getImage(result, bean.getUrl());
				}
			} catch (Exception e) {
				continue;
			}
		}

		LINKHASH.clear();
	}

	/**
	 * 处理有子菜单的页面
	 * 
	 * @param bean
	 * @param content
	 * @throws Exception
	 */
	private static void doProcessSub(WebsiteBean bean, String content)
			throws Exception {
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
						bean.getUrl().lastIndexOf("/html") + 1)
						+ "html/";
				log.debug("" + tmpUrl);
				LinkBean link = (LinkBean) LINKHASH.get(key);
				if (!link.getLink().startsWith(tmpUrl)) {
					log.debug(" >> [" + link.getLink() + "] 不是以[" + tmpUrl
							+ "]开头的");
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
				log.debug(" >> linlUrl:[" + link.getLink() + "] |tmpUrl:"
						+ tmpUrl);
				if (link.getLink().startsWith(tmpUrl)) {
					if (link.getLink().indexOf("_") == -1) {
						if (bean.getId() == 1633 || bean.getParentId() == 1636
								|| bean.getParentId() == 1644
								|| bean.getId() == 1664 || bean.getId() == 1665) {
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
					getImage(result, bean.getUrl());
				}
			} catch (Exception e) {
				continue;
			}
		}

		LINKHASH.clear();
	}

}
