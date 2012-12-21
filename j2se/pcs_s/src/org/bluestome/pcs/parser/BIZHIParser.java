package org.bluestome.pcs.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bluestome.pcs.bean.Article;
import org.bluestome.pcs.bean.ImageBean;
import org.bluestome.pcs.bean.LinkBean;
import org.bluestome.pcs.bean.ResultBean;
import org.bluestome.pcs.bean.WebsiteBean;
import org.bluestome.pcs.common.factory.DAOFactory;
import org.bluestome.pcs.dao.ArticleDao;
import org.bluestome.pcs.dao.ImageDao;
import org.bluestome.pcs.dao.WebSiteDao;
import org.bluestome.pcs.memcache.MemcacheClient;
import org.bluestome.pcs.utils.CommonUtil;
import org.bluestome.pcs.utils.HttpClientUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
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

public class BIZHIParser {
	
	private static Logger logger = LoggerFactory.getLogger(BIZHIParser.class);

	static String URL_ = "http://www.6188.com/"; //http://www.bizhi.com/

	static String URL = "http://www.6188.com"; //http://www.bizhi.com

	static int D_PARENT_ID = 1200;

	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	static List<Article> ARTICLELIST = new ArrayList<Article>();

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	static MemcacheClient client = MemcacheClient.getInstance();

	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	static long START_TIME_MILLIS = 0L;
	
	/**
	 * 获取分类下的分页信息
	 * 
	 * @param url
	 * @param attribute
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private static ResultBean hasPaging2(String url) throws Exception {
		long start = System.currentTimeMillis();
		ResultBean result = new ResultBean();
		try{
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("utf-8");

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
		}catch(Exception e){
			logger.error(" > error:"+url);
		}
		logger.debug("\t hasPage2耗时:"+(System.currentTimeMillis()-start));
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

		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(CompositeTag.class);
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
								if(!imgTag.getImageURL().startsWith("http://")){
									article.setActicleXmlUrl(URL
											+ imgTag.getImageURL());
								}else{
									article.setActicleXmlUrl(imgTag.getImageURL());
								}
								article.setTitle(imgTag.getAttribute("alt")); // NT:No
							}
							logger.info(" > a:"+article.getTitle() + "\t|"+ url);
							int key = articleDao.insert(article);
							if (key > 0) {
								article.setId(key);
								client.add(url, url);
								if (getImage(article)) {
									article.setText("FD");
									if (articleDao.update(article)) {
										logger.info("> a:"+article.getTitle() + "\t|"+ url+" 更新记录成功,ID为:"+key);
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
		long start = System.currentTimeMillis();
		boolean b = true;
		ResultBean result = hasPaging2(article.getArticleUrl());
		if (result.isBool()) {
			Iterator it = result.getMap().keySet().iterator();
			int i=0;
			while (it.hasNext()) {
				String key = (String) it.next();
				logger.debug(article.getTitle()+"|"+key);
				LinkBean link = result.getMap().get(key);
				b = getImage(link, article.getId());
				if(b)
					i++;
			}
			if(i>0)
				logger.info(" > a:"+article.getTitle() + " 有["+ i+"]长图片");
		}
		logger.debug("\t getImage(耗时):"+(System.currentTimeMillis()-start));
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
		long start = System.currentTimeMillis();
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

			NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
			NodeList list2 = p3.extractAllNodesThatMatch(filter2)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("target", "_blank"));
			String imgSrc = null;
			if (null != list2 && list2.size() > 0) {
				ImageBean imgBean = null;
				String url = null;
				for (int i = 0; i < list2.size(); i++) {
					LinkTag linkTag = (LinkTag) list2.elementAt(i);

					if (!linkTag.getLink().startsWith("http://")) {
						url = URL + linkTag.getLink();
					} else {
						url = linkTag.getLink();
					}

					imgSrc = getImageURL(url);
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
									if (null != imgTag.getImageURL()){
										if(!imgTag.getImageURL().startsWith("http://")){
											imgBean.setImgUrl(URL_
													+ imgTag.getImageURL());
										}else{
											imgBean.setImgUrl(imgTag.getImageURL());
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
								size = 0;
								imgBean.setFileSize(0l);
								imgBean.setStatus(1);
							}
							try {
								int key = imageDao.insert(imgBean);
								if (key > 0) {
									logger.debug("> \t" + imgBean.getTitle()+ "\t|" + imgBean.getHttpUrl());
								}
							} catch (Exception e) {
								b = false;
								break;
							}
						}
					}else{
						imgBean = new ImageBean();
						imgBean.setArticleId(artId);
						NodeList tmpNode2 = linkTag.getChildren();
						if (null != tmpNode2 && tmpNode2.size() > 0) {
							if (tmpNode2.elementAt(0) instanceof ImageTag) {
								ImageTag imgTag = (ImageTag) tmpNode2
										.elementAt(0);
								if (null != imgTag.getImageURL()){
									if(!imgTag.getImageURL().startsWith("http://")){
										imgBean.setImgUrl(URL_
												+ imgTag.getImageURL());
										imgBean.setHttpUrl(URL_
												+ imgTag.getImageURL());
									}else{
										imgBean.setImgUrl(imgTag.getImageURL());
										imgBean.setHttpUrl(imgTag.getImageURL());
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
							size = 0;
							imgBean.setFileSize(0l);
							imgBean.setStatus(1);
						}
						try {
							int key = imageDao.insert(imgBean);
							if (key > 0) {
								logger.debug(imgBean.getTitle()+ "\t|" + imgBean.getHttpUrl());
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
		logger.debug("\t getImage(link,artId)耗时:"+(System.currentTimeMillis()-start));
		return b;
	}

	/**
	 * 获取图片实际地址
	 * 
	 * @param url
	 * @return
	 */
	private static String getImageURL(String url) {
		long start = System.currentTimeMillis();
		String result = null;
		if(!HttpClientUtils.validationURL(url)){
			logger.debug("\t getImageURL 验证失败耗时："+(System.currentTimeMillis()-start));
			return null;
		}
		try {
			String body = HttpClientUtils.getResponseBody(url);
			Parser p1 = new Parser();
			p1.setInputHTML(body);
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
		logger.debug("\t getImageURL 耗时："+(System.currentTimeMillis()-start));
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
			p2.setEncoding("GB2312");
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
//									length = HttpClientUtils.getHttpConentLength(url);
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
										logger.error(">> 未添加[" + url
												+ "]到数据库中");
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

	private static String getImageUrl(String link) {
		int start = link.indexOf("=");
		int end = link.length();
		String imgUrl = link.substring(start + 1, end);
		return imgUrl;
	}

	public static void update() throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : webList) {
			logger.info("正在处理站点["+bean.getId()+"|"+bean.getName()+"|"+bean.getUrl()+"]");
			
			String lastModify = HttpClientUtils.getLastModifiedByUrl(bean.getUrl());
			if(null != bean.getLastModifyTime() && !"".equals(bean.getLastModifyTime()) && bean.getLastModifyTime().equals(lastModify)){
				logger.info("站点["+bean.getId()+"|"+bean.getName()+"|"+bean.getUrl()+"]的最后更新时间没有变更，不需要更新.");
				continue;
			}
			
			ResultBean result = hasPaging2(bean.getUrl());
			if (result.isBool()) {
				Iterator it = result.getMap().keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					LinkBean link = result.getMap().get(key);
					try {
						secondURL(link, bean.getId());
					} catch (Exception e) {
						logger.error(e.getMessage());
						continue;
					}
				}
				
				if(lastModify != null && !"".equals(lastModify) ){
					if(null == bean.getLastModifyTime() || "".equals(bean.getLastModifyTime()) || !bean.getLastModifyTime().equals(lastModify)){
						bean.setLastModifyTime(lastModify);
						if(webSiteDao.update(bean)){
							logger.info(" >> 更新网站["+bean.getId()+"|"+bean.getName()+"|"+bean.getUrl()+"]]最后时间["+lastModify+"]成功!");
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
//			 update();
			 loadImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadImg() throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : webList) {
			List<Article> list = articleDao.findByWebId(bean.getId(), "NED");
			logger.debug(bean.getName()+"\tNED size:"+list.size());
//			for (Article art : list) {
//				START_TIME_MILLIS = System.currentTimeMillis();
//				try{
//					if (getImage(art)) {
//						art.setText("FD");
//						if (articleDao.update(art)) {
//							logger.debug("更新记录[" + art.getTitle() + "]成功");
//						}
//					}
//				}catch(Exception e){
//					logger.error("loadImg.for.exception:"+e.getMessage());
//				}
//				logger.debug("before getImage(art) 耗时:"+(System.currentTimeMillis()-START_TIME_MILLIS));
//			}
		}
	}
	

}