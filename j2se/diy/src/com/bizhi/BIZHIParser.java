package com.bizhi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.ResultBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.HttpClientUtils;
import com.common.Constants;
import com.parser.tag.ULTag;

public class BIZHIParser {

	private static Logger logger = LoggerFactory.getLogger(BIZHIParser.class);

	static String URL_ = "http://www.6188.com/"; // http://www.bizhi.com/

	static String URL = "http://www.6188.com"; // http://www.bizhi.com

	static String PIC_SAVE_PATH = Constants.FILE_SERVER;

	final static String FILE_SERVER = Constants.FILE_SERVER;

	static int D_PARENT_ID = 1200;

	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	static List<Article> ARTICLELIST = new ArrayList<Article>();

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	static MemcacheClient client = MemcacheClient.getInstance();

	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();

	/**
	 * 获取分类链接
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void catalog(String url) throws Exception { // WebsiteBean bean
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "mainNav"));

		if (null != list && list.size() > 0) {
			// 主页中的第7个table
			Div table = (Div) list.elementAt(0);
			Parser p2 = new Parser();
			p2.setInputHTML(table.getChildrenHTML());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p2.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					if (link.getLink().endsWith(".html")) {
						tmp = new WebsiteBean();
						tmp.setName(link.getLinkText());
						if (!link.getLink().startsWith("http://")) {
							logger.debug(URL + link.getLink() + "\n");
							tmp.setUrl(URL + link.getLink());
						} else {
							logger.debug(link.getLink() + "\n");
							tmp.setUrl(link.getLink());
						}
						tmp.setParentId(D_PARENT_ID);
						boolean b = webSiteDao.insert(tmp);
						if (b) {
							client.add(tmp.getUrl(), tmp.getUrl());
							logger.debug("成功");
						} else {
							logger.debug("失败");
						}
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
	 * 获取分类下的分页信息
	 * 
	 * @param url
	 * @param attribute
	 * @param value
	 * @return
	 * @throws Exception
	 */
	static ResultBean hasPaging(String url, String cls, String value)
			throws Exception {
		boolean b = false;
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);

		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter(cls, value));
		if (list != null && list.size() > 0) {
			Parser p2 = new Parser();
			p2.setInputHTML(list.toHtml());

			NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
			NodeList list2 = p2.extractAllNodesThatMatch(filter2);
			if (null != list && list2.size() > 0) {
				String tmp = null;
				LinkBean l1 = null;
				for (int i = 0; i < list2.size(); i++) {
					l1 = new LinkBean();
					LinkTag link2 = (LinkTag) list2.elementAt(i);
					if (!link2.getLink().startsWith("http://")) {
						tmp = URL_ + link2.getLink();
					} else {
						tmp = link2.getLink();
					}
					tmp = tmp.replace("&amp;", "&");
					l1.setLink(tmp);
					l1.setTitle(link2.getLinkText());
					result.put(tmp, l1);
				}
				result.setBool(true);
			}
			if (null != p2)
				p2 = null;
		} else {
			result.setBool(b);
		}
		return result;
	}

	/**
	 * 获取分类下的分页信息
	 * 
	 * @param url
	 * @param attribute
	 * @param value
	 * @return
	 * @throws Exception
	 */
	static ResultBean hasPaging2(String url) throws Exception {
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("UTF-8");
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "page"));
		int pageNum = 1;
		if (list != null && list.size() > 0) {
			Parser p2 = new Parser();
			p2.setInputHTML(list.toHtml());
			NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
			NodeList list2 = p2.extractAllNodesThatMatch(filter2);
			if (null != list && list2.size() > 0) {
				String tmp = null;
				LinkBean l1 = null;
				if (list2.size() > 10) {
					LinkTag tmpLink = (LinkTag) list2.elementAt(8);
					try {
						pageNum = Integer.valueOf(tmpLink.getLinkText());
					} catch (Exception e) {
						throw new Exception("分页出现异常");
					}
					for (int i = 1; i < pageNum + 1; i++) {
						tmp = url.replace("1.html", i + ".html");
						l1 = new LinkBean();
						l1.setLink(tmp);
						l1.setTitle("" + i);
						result.put(tmp, l1);
					}
				} else {
					for (int i = 0; i < list2.size(); i++) {
						l1 = new LinkBean();
						LinkTag link2 = (LinkTag) list2.elementAt(i);
						if (!link2.getLink().equalsIgnoreCase("#")) {
							if (!link2.getLink().startsWith("http://")) {
								tmp = URL + link2.getLink();
							} else {
								tmp = link2.getLink();
							}
						}
						l1.setLink(tmp);
						l1.setTitle(link2.getLinkText());
						result.put(tmp, l1);
					}
				}

			}
			if (null != p2)
				p2 = null;
		}
		LinkBean l1 = new LinkBean();
		l1.setLink(url);
		l1.setTitle(url);
		result.put(url, l1);
		result.setBool(true);
		return result;
	}

	/**
	 * 获取指定URL下的源码
	 * 
	 * @param url1
	 * @return
	 */
	public static String ViewSourceFrame(String url1) throws Exception {
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
	 * 获取指定列表中页面源码
	 * 
	 * @param webList
	 */
	static void initHTML(List<WebsiteBean> webList) {
		Long start = System.currentTimeMillis();
		for (WebsiteBean bean : webList) {
			try {
				Long start1 = System.currentTimeMillis();
				String content = ViewSourceFrame(bean.getUrl());
				if (null != content && !"".equalsIgnoreCase(content)) {
					// processWithDoc(bean.getId(), content);
					Long end1 = System.currentTimeMillis();
					logger.debug("单条耗时:" + (end1 - start1) + "长度："
							+ content.getBytes().length);
				}
			} catch (Exception e) {
				logger.debug("Exception:" + e.getMessage());
				continue;
			}
		}
		Long end = System.currentTimeMillis();
		System.out.print("总耗时:" + (end - start));
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
		parser.setEncoding("UTF-8");

		PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
		factory.registerTag(new ULTag());

		parser.setNodeFactory(factory);
		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(ULTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "uip uip_250"));
		if (list != null && list.size() > 0) {
			Parser p2 = null;
			CompositeTag div = (CompositeTag) list.elementAt(0);
			p2 = new Parser();
			p2.setInputHTML(div.toHtml());

			NodeFilter filter2 = new NodeClassFilter(Span.class);
			NodeList list2 = p2.extractAllNodesThatMatch(filter2);
			Article article = null;
			String url = null;
			if (null != list2 && list2.size() > 0) {
				for (int i = 0; i < list2.size(); i++) {
					Span span = (Span) list2.elementAt(i);
					NodeList tmpNode = span.getChildren();
					if (null != tmpNode && tmpNode.size() > 0) {
						LinkTag linkTag = (LinkTag) tmpNode.elementAt(0);
						logger.info(linkTag.getLinkText() + "|"
								+ linkTag.getLink());

						if (!linkTag.getLink().startsWith("http://")) {
							url = URL + linkTag.getLink();
						} else {
							url = linkTag.getLink();
						}

						if (null == client.get(url)) {
							article = new Article();
							article.setWebId(webId);
							article.setArticleUrl(url);
							article.setText("NED"); // NED_WALLCOO
							article.setIntro("");
							NodeList tmpNode2 = linkTag.getChildren();
							if (null != tmpNode2 && tmpNode2.size() > 0) {
								ImageTag imgTag = (ImageTag) tmpNode2
										.elementAt(0);
								logger.info(imgTag.getAttribute("alt") + "|["
										+ url + "]");
								if (!imgTag.getImageURL().startsWith("http://")) {
									article.setActicleXmlUrl(URL
											+ imgTag.getImageURL());
								} else {
									article.setActicleXmlUrl(imgTag
											.getImageURL());
								}
								article.setTitle(imgTag.getAttribute("alt")); // NT:No
							}
							int key = articleDao.insert(article);
							if (key > 0) {
								logger.info("添加记录[" + article.getTitle()
										+ "]成功");
								client.add(url, url);
								article.setId(key);
								if (getImage(article)) {
									article.setText("FD");
									if (articleDao.update(article)) {
										logger.info("更新记录["
												+ article.getTitle() + "]成功");
									}
								}
							}
						}
					}
				}
			}

			if (null != p2) {
				p2 = null;
			}
		}
		if (null != parser)
			parser = null;
	}

	/**
	 * 获取图片地址下数据
	 * 
	 * @param link
	 * @param webId
	 * @throws Exception
	 */
	public static boolean getImage(Article article) throws Exception {
		boolean b = true;
		ResultBean result = hasPaging2(article.getArticleUrl());
		if (result.isBool()) {
			Iterator<String> it = result.getMap().keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				LinkBean link = result.getMap().get(key);
				try {
					b = getImage(link, article.getId());
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}

		return b;
	}

	/**
	 * 获取分类下数据
	 * 
	 * @param link
	 * @param webId
	 * @throws Exception
	 */
	public static boolean getImage(LinkBean link, int artId) throws Exception {
		Parser p2 = new Parser();
		p2.setURL(link.getLink());
		p2.setEncoding("UTF-8");
		int size = 0;
		boolean b = true;
		String length = "0";
		NodeFilter filter = new NodeClassFilter(CompositeTag.class);
		NodeList list = p2.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "listimg"));

		if (null != list && list.size() > 0) {
			Parser p3 = new Parser();
			p3.setInputHTML(list.toHtml());
			p3.setEncoding("UTF-8");

			NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
			NodeList list2 = p3.extractAllNodesThatMatch(filter2)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("target", "_blank"));
			String imgSrc = null;
			if (null != list2 && list2.size() > 0) {
				ImageBean imgBean = null;
				String url = null;
				logger.debug(">> 列表数量:" + list2.size());
				for (int i = 0; i < list2.size(); i++) {
					LinkTag linkTag = (LinkTag) list2.elementAt(i);

					if (!linkTag.getLink().startsWith("http://")) {
						url = URL + linkTag.getLink();
					} else {
						url = linkTag.getLink();
					}

					imgSrc = getImageURL(url);
					// logger.debug("imgSrc:" + imgSrc);
					if (null != imgSrc) {
						if (null == client.get(imgSrc)) {
							imgBean = new ImageBean();
							imgBean.setArticleId(artId);
							imgBean.setHttpUrl(imgSrc);
							NodeList tmpNode2 = linkTag.getChildren();
							if (null != tmpNode2 && tmpNode2.size() > 0) {
								if (tmpNode2.elementAt(0) instanceof ImageTag) {
									ImageTag imgTag = (ImageTag) tmpNode2
											.elementAt(0);
									if (null != imgTag.getImageURL()) {
										if (!imgTag.getImageURL().startsWith(
												"http://")) {
											imgBean.setImgUrl(URL_
													+ imgTag.getImageURL());
										} else {
											imgBean.setImgUrl(imgTag
													.getImageURL());
										}
									}
									if (null != imgTag.getAttribute("alt"))
										imgBean.setTitle(imgTag
												.getAttribute("alt"));
									else
										imgBean.setTitle("NT:"
												+ CommonUtil
														.getDateTimeString());
								}
							}
							imgBean.setLink("NED");
							imgBean.setCommentshowurl(link.getLink());
							try {
								size = Integer.parseInt(length);
								imgBean.setFileSize(Long.valueOf(size));
								imgBean.setStatus(3);
							} catch (Exception e) {
								e.printStackTrace();
								logger.error(">> IMAGE SIZE ERROR");
								size = 0;
								imgBean.setFileSize(0l);
								imgBean.setStatus(1);
							}
							try {
								int key = imageDao.insert(imgBean);
								if (key > 0) {
									logger.info(imgBean.getTitle() + "\t|"
											+ imgBean.getHttpUrl());
									// client.add(imgBean.getHttpUrl(), imgBean
									// .getHttpUrl());
								}
							} catch (Exception e) {
								b = false;
								break;
							}
						}
					} else {
						imgBean = new ImageBean();
						imgBean.setArticleId(artId);
						NodeList tmpNode2 = linkTag.getChildren();
						if (null != tmpNode2 && tmpNode2.size() > 0) {
							if (tmpNode2.elementAt(0) instanceof ImageTag) {
								ImageTag imgTag = (ImageTag) tmpNode2
										.elementAt(0);
								if (null != imgTag.getImageURL()) {
									if (!imgTag.getImageURL().startsWith(
											"http://")) {
										imgBean.setImgUrl(URL_
												+ imgTag.getImageURL());
										imgBean.setHttpUrl(URL_
												+ imgTag.getImageURL());
									} else {
										imgBean.setImgUrl(imgTag.getImageURL());
										imgBean.setHttpUrl(imgTag.getImageURL());
									}
								}
								if (null != imgTag.getAttribute("alt"))
									imgBean.setTitle(imgTag.getAttribute("alt"));
								else
									imgBean.setTitle("NT:"
											+ CommonUtil.getDateTimeString());
							}
						}
						imgBean.setLink("NED");
						imgBean.setCommentshowurl(link.getLink());
						try {
							size = Integer.parseInt(length);
							imgBean.setFileSize(Long.valueOf(size));
							imgBean.setStatus(3);
						} catch (Exception e) {
							e.printStackTrace();
							logger.error(">> IMAGE SIZE ERROR");
							size = 0;
							imgBean.setFileSize(0l);
							imgBean.setStatus(1);
						}
						try {
							int key = imageDao.insert(imgBean);
							if (key > 0) {
								logger.debug(imgBean.getTitle() + "\t|"
										+ imgBean.getHttpUrl());
							}
						} catch (Exception e) {
							b = false;
							break;
						}
					}
				}
			}

			if (null != p3)
				p3 = null;
		}

		if (null != p2)
			p2 = null;

		return b;
	}

	/**
	 * 获取图片实际地址
	 * 
	 * @param url
	 * @return
	 */
	static String getImageURL(String url) {
		String result = null;
		try {
			Parser p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("utf-8");

			NodeFilter filter = new NodeClassFilter(ImageTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter);

			if (null != list && list.size() == 1) {
				ImageTag link = (ImageTag) list.elementAt(0);
				result = link.getImageURL();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取图片地址下数据
	 * 
	 * @param link
	 * @param webId
	 * @throws Exception
	 */
	public static void getImage(LinkBean link, int webId, int articleId)
			throws Exception {
		Parser parser = new Parser();
		parser.setURL(link.getLink());
		parser.setEncoding("utf-8");
		String length = "0";
		int size = 0;
		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(TableTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "DataList1"));
		if (list != null && list.size() > 0) {
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
			OrFilter lastFilter = new OrFilter();
			lastFilter
					.setPredicates(new NodeFilter[] { linkFilter, imageFilter });
			Parser p2 = new Parser();
			p2.setInputHTML(list.toHtml());
			p2.setEncoding("utf-8");
			NodeList list4 = p2.parse(lastFilter);
			if (list4 != null || list4.size() > 0) {
				for (int i = 0; i < list4.size(); i++) {
					// 地址
					if (list4.elementAt(i) instanceof LinkTag) {
						LinkTag nl = (LinkTag) list4.elementAt(i);
						NodeList cnl = nl.getChildren();
						if (cnl != null && cnl.size() > 0) {
							// 小图 可能存在部分图片无法访问，需要判断
							ImageBean imgBean = null;
							if (cnl.elementAt(0) instanceof ImageTag) {
								ImageTag it = (ImageTag) cnl.elementAt(0);
								String url = "" + getImageUrl(nl.getLink());
								if (null == client.get(url)) {
									length = HttpClientUtils
											.getHttpHeaderResponse(url,
													"Content-Length");
									imgBean = new ImageBean();
									imgBean.setArticleId(articleId);
									imgBean.setHttpUrl(url);
									imgBean.setImgUrl(it.getImageURL());
									imgBean.setTitle(it.getAttribute("alt"));
									try {
										size = Integer.parseInt(length);
										imgBean.setFileSize(Long.valueOf(size));
										imgBean.setStatus(3);
									} catch (Exception e) {
										e.printStackTrace();
										System.err
												.println(">> IMAGE SIZE ERROR");
										size = 0;
										imgBean.setFileSize(0l);
										imgBean.setStatus(1);
									}
									imgBean.setLink("NED");
									imgBean.setOrderId(i);
									imgBean.setArticleId(articleId);
									// HttpClientUtils
									int result = imageDao.insert(imgBean);
									if (result > 0) {
										logger.debug(">> add article["
												+ articleId + "] image id["
												+ result + "] to DB");
										imgBean.setId(result);
										client.add(url, url);
									} else {
										logger.error(">> 未添加[" + url + "]到数据库中");
									}
								} else {
									logger.error(">> 缓存中已存在相同的内容 ["
											+ nl.getLink() + "]");
								}
							}
						}
					}
				}
			}
		}
	}

	static String getImageUrl(String link) {
		int start = link.indexOf("=");
		int end = link.length();
		String imgUrl = link.substring(start + 1, end);
		return imgUrl;
	}

	static String getTitle(String title, String defaultTitle) {
		if (null == title || "".equalsIgnoreCase(title)) {
			return defaultTitle + ":" + System.currentTimeMillis();
		}
		return title;
	}

	static void init() {
		try {
			List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
			for (WebsiteBean bean : webList) {
				List<Article> articleList = articleDao
						.findByWebId(bean.getId());
				for (Article article : articleList) {
					if (null == client.get(article.getArticleUrl())) {
						logger.info("add url[" + article.getArticleUrl()
								+ "] to cache");
						client.add(article.getArticleUrl(),
								article.getArticleUrl());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void update() throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : webList) {

			ResultBean result = hasPaging2(bean.getUrl());
			if (result.isBool()) {
				Iterator<String> it = result.getMap().keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					// if (HttpClientUtils.validationURL(key)) {
					LinkBean link = result.getMap().get(key);
					try {
						secondURL(link, bean.getId());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					// } else {
					// continue;
					// }
				}

			}
		}
	}

	static void loadImg() throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : webList) {
			List<Article> list = articleDao.findByWebId(bean.getId(), "NED");
			for (Article art : list) {
				if (getImage(art)) {
					art.setText("FD");
					if (articleDao.update(art)) {
						logger.info("更新记录[" + art.getTitle() + "]成功");
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			update();
			loadImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
