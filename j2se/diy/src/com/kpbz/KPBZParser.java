package com.kpbz;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.ArticleDoc;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.bean.ResultBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.test.TestHttpClient;
import com.chinamilitary.util.CacheUtils;
import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.common.Constants;
import com.utils.FileUtils;

public class KPBZParser {

	static String URL_ = "http://www.kpbz.net/";

	static String URL = "http://www.kpbz.net";

	static String PIC_SAVE_PATH = Constants.FILE_SERVER;
	
	final static String FILE_SERVER = Constants.FILE_SERVER;
	
	static Integer D_PARENT_ID = 1100;

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
		NodeFilter fileter = new NodeClassFilter(CompositeTag.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "class_menu"));

		if (null != list && list.size() > 0) {
			// 主页中的第7个table
			CompositeTag table = (CompositeTag) list.elementAt(1);
			System.out.println(table.toHtml());
			Parser p2 = new Parser();
			p2.setInputHTML(table.toHtml());
			
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p2.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					System.out.println(link.getLinkText());
					tmp = new WebsiteBean();
					tmp.setName(link.getLinkText());
					if (!link.getLink().startsWith("http://")) {
						System.out.println(URL + link.getLink() + "\n");
						tmp.setUrl(URL + link.getLink());
					} else {
						System.out.println(link.getLink() + "\n");
						tmp.setUrl(link.getLink());
					}
					tmp.setParentId(1100);
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
			String tmp = null;
			LinkBean l1 = null;
			if (null != list && list2.size() > 0) {
				String vurl = url.substring(0,url.lastIndexOf("/")+1);
				for (int i = 0; i < list2.size(); i++) {
					l1 = new LinkBean();
					LinkTag link2 = (LinkTag) list2.elementAt(i);
					if(link2.getLink().equalsIgnoreCase("#")){
						tmp = url;
					}else{
						if (!link2.getLink().startsWith("http://")) {
							tmp = vurl + link2.getLink();
						} else {
							tmp = link2.getLink();
						}
					}
					
//					if(!HttpClientUtils.urlValidation(tmp)){
//						return result;
//					}
					
					l1.setLink(tmp);
					l1.setTitle(link2.getLinkText());
					result.put(tmp, l1);
				}
				result.setBool(true);
			}else{
				l1 = new LinkBean();
				l1.setLink(url);
				l1.setTitle("Title");
				result.put(tmp, l1);
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
	static ResultBean hasPaging2(String url)
			throws Exception {
		boolean b = false;
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);

		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(SelectTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("name", "sldd"));
		String tmp = null;
		LinkBean l1 = null;
		if (list != null && list.size() > 0) {
			SelectTag select = (SelectTag)list.elementAt(0);
			OptionTag[] options = select.getOptionTags();
			for(OptionTag option:options){
				l1 = new LinkBean();
				if (!option.getValue().startsWith("http://")) {
					tmp = url + option.getValue();
				} else {
					tmp = option.getValue();
				}
				l1.setLink(tmp);
				l1.setTitle("Title");
				result.put(tmp, l1);
			}
			result.setBool(true);
		}
		l1 = new LinkBean();
		l1.setLink(url);
		l1.setTitle("Title");
		result.put(tmp, l1);
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
					System.out.println("单条耗时:" + (end1 - start1) + "长度："
							+ content.getBytes().length);
				}
			} catch (Exception e) {
				System.out.println("Exception:" + e.getMessage());
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

		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(CompositeTag.class);
		NodeList list = parser
				.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("class", "pimg"));
		if (list != null && list.size() > 0) {
			Parser p2 = null;
			for (int i = 0; i < list.size(); i++) {
				Article article = null;
				String url = null;
				if (null != list && list.size() > 0) {
					LinkTag ltmp = (LinkTag) list.elementAt(i);
					if (!ltmp.getLink().startsWith("http://")) {
						url = URL + ltmp.getLink();
					} else {
						url = ltmp.getLink();
					}

					if (null == client.get(url)) {
						article = new Article();
						article.setWebId(webId);
						article.setArticleUrl(url);
						article.setText("NED"); // NED_WALLCOO
						article.setIntro("");
						NodeList tmp = ltmp.getChildren();
						if (tmp != null && tmp.size() > 0) {
							ImageTag imgTag = (ImageTag) tmp.elementAt(0);
							System.out.println(imgTag.getAttribute("alt")
									+ "|[" + url + "]");
							article.setActicleXmlUrl(imgTag.getImageURL());
							article.setTitle(imgTag.getAttribute("alt")); // NT:No
																			// Title
						}
						
//						System.out.println("*****************Start***************");
//						System.out.println("ArticleUrl:"+article.getArticleUrl());
//						System.out.println("ActicleXmlUrl:"+article.getActicleXmlUrl());
//						System.out.println("Title:"+article.getTitle());
//						System.out.println("Webid:"+article.getWebId());
//						System.out.println("Text:"+article.getText());
//						System.out.println("*****************End***************\n");
						
						int key = articleDao.insert(article);
						if (key > 0) {
							System.out.print(ltmp.getLinkText() + "\t|" + url);
							client.add(url, url);
						}
					} else {
						System.err.println(">> 已存在相同的内容 [" + ltmp.getLinkText()
								+ "]");
					}
				}

				if (null != p2)
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
		ResultBean result = hasPaging(article.getArticleUrl(),"id","pagelist");
		if (result.isBool()) {
			Iterator it = result.getMap().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				LinkBean link = result.getMap().get(key);
				b = getImage(link, article.getId());
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

		Parser parser = new Parser();
		parser.setURL(link.getLink());
		parser.setEncoding("UTF-8");
		boolean resultB = true;
		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(CompositeTag.class);
		NodeList list = parser
				.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("id", "imglist"));
		if (null != list && list.size() > 0) {
			Parser p3 = new Parser();
			p3.setInputHTML(list.toHtml());

			NodeFilter filter3 = new NodeClassFilter(LinkTag.class);
			NodeList list3 = p3.extractAllNodesThatMatch(filter3);
			String length = "0";
			int size = 0;
			
			if(null == list3 && list3.size() == 0){
				return false;
			}
			
			for (int i = 0; i < list3.size(); i++) {
				ImageBean imgBean = null;
				String url = null;
				String imgSrc = null;
					LinkTag ltmp = (LinkTag) list3.elementAt(i);
					if (!ltmp.getLink().startsWith("http://")) {
						url = URL + ltmp.getLink();
					} else {
						url = ltmp.getLink();
					}

					imgSrc = getImageURL(url);
					if (null != imgSrc) {
						if (null == client.get(imgSrc)) {
							imgBean = new ImageBean();
							imgBean = new ImageBean();
							imgBean.setArticleId(artId);
							imgBean.setHttpUrl(imgSrc);
							NodeList tmp = ltmp.getChildren();
							if (tmp != null && tmp.size() > 0) {
								ImageTag imgTag = (ImageTag) tmp.elementAt(0);
								if (null != imgTag.getImageURL())
									imgBean.setImgUrl(URL+imgTag.getImageURL());
								if (null != imgTag.getAttribute("alt"))
									imgBean
											.setTitle(imgTag
													.getAttribute("alt"));
								else
									imgBean.setTitle("NT:"
											+ CommonUtil.getDateTimeString());
							}
//							length = HttpClientUtils.getHttpHeaderResponse(
//									imgSrc, "Content-Length");
//							System.out.println("Title:"+imgBean.getTitle());
//							System.out.println("ArticleId:"+imgBean.getArticleId());
//							System.out.println("大图地址:"+imgBean.getHttpUrl());
//							System.out.println("小图地址:"+imgBean.getImgUrl());
							imgBean.setLink("NED");
							try {
								size = Integer.parseInt(length);
								imgBean.setFileSize(Long.valueOf(size));
								imgBean.setStatus(3);
							} catch (Exception e) {
								e.printStackTrace();
								System.err.println(">> IMAGE SIZE ERROR");
								size = 0;
								imgBean.setFileSize(0l);
								imgBean.setStatus(1);
							}
							imgBean.setCommentshowurl(link.getLink());
							int key = imageDao.insert(imgBean);
							if (key > 0) {
								System.out.println(imgBean.getTitle()+"\t|"+ imgSrc);
								client.add(imgSrc, imgSrc);
							}
						} else {
							System.err.println(">> 已存在相同的内容 ["
									+ ltmp.getLinkText() + "]");
						}
					} else {
						resultB = false;
						break;
					}
				if (null != p3)
					p3 = null;
			}
		}

		return resultB;
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

			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = p1.extractAllNodesThatMatch(filter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("id", "pic"));

			if (null != list && list.size() == 1) {
				Div div = (Div) list.elementAt(0);
				
				Parser p3 = new Parser();
				p3.setInputHTML(div.toHtml());

				NodeFilter filter3 = new NodeClassFilter(LinkTag.class);
				NodeList list3 = p3.extractAllNodesThatMatch(filter3);
				
				if(null == list3){
					return null;
				}
				LinkTag link = (LinkTag)list3.elementAt(0);
				if(!link.getLink().startsWith("http://")){
					result = URL+link.getLink();
				}else{
					result = link.getLink();
				}
				
				if(null != p3 )
					p3 = null;
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
		parser.setEncoding("UTF-8");
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
			p2.setEncoding("UTF-8");
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
								String url = ""
										+ getImageUrl(nl.getLink());
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
										System.out.println(">> add article["
												+ articleId + "] image id["
												+ result + "] to DB");
										imgBean.setId(result);
										client.add(url, url);
									} else {
										System.err.println(">> 未添加[" + url
												+ "]到数据库中");
									}
								} else {
									System.err.println(">> 缓存中已存在相同的内容 ["
											+ nl.getLink() + "]");
								}
							}
						}
					}
					Thread.sleep(1000);
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
						client.add(article.getArticleUrl(), article
								.getArticleUrl());
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
			
			String lastModify = HttpClientUtils.getLastModifiedByUrl(bean.getUrl());
			if(null != bean.getLastModifyTime() && !"".equals(bean.getLastModifyTime()) && bean.getLastModifyTime().equals(lastModify)){
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
						e.printStackTrace();
						System.out.println("key:" + key);
						continue;
					}
				}
				
				
				if(lastModify != null && !"".equals(lastModify) ){
					if(null == bean.getLastModifyTime() || "".equals(bean.getLastModifyTime()) || !bean.getLastModifyTime().equals(lastModify)){
						bean.setLastModifyTime(lastModify);
						if(webSiteDao.update(bean)){
							System.out.println(" >> 更新网站["+bean.getName()+"|"+bean.getUrl()+"]最后时间["+lastModify+"]成功!");
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		// init();
		try {
//			 catalog(URL);
			 update();
			loadImg();
			imgDownload();
//			 movefile();
			
//			ResultBean result = hasPaging("http://www.kpbz.net/1920x1200/kuanping576.html","id","pagelist");
//			if (result.isBool()) {
//				Iterator it = result.getMap().keySet().iterator();
//				while (it.hasNext()) {
//					String key = (String) it.next();
//					LinkBean link = result.getMap().get(key);
//					System.out.println(link.getTitle()+":"+link.getLink());
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void loadImg() throws Exception {
		// WebsiteBean bean = webSiteDao.findById(702);
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : webList) {
			List<Article> list = articleDao.findByWebId(bean.getId(),"NED");
			for (Article art : list) {
//				List<ImageBean> imgList = imageDao.findImage(art.getId());
//				if(imgList.size() == 0){
					if (getImage(art)) {
						art.setText("FD");
						if (articleDao.update(art)) {
							System.out
									.println("更新记录[" + art.getTitle() + "]成功");
						}
					}
//				}
			}
		}
	}

	static void vistDesk() throws Exception {
		Parser parser = new Parser();
		parser.setURL("http://vista.zol.com.cn/desk.html");
		parser.setEncoding("GB2312");

		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(CompositeTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "deskP1"));
		if (list != null && list.size() > 0) {

			// System.out.println(list.toHtml());
			Parser p2 = null;
			for (int i = 0; i < list.size(); i++) {
				Div div = (Div) list.elementAt(i);
				p2 = new Parser();
				p2.setInputHTML(div.toHtml());

				NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
				NodeList list2 = p2.extractAllNodesThatMatch(filter2);

				Article article = null;
				String url = null;
				if (null != list2 && list2.size() > 0) {
					LinkTag ltmp = (LinkTag) list2.elementAt(0);
					if (!ltmp.getLink().startsWith("http://")) {
						url = URL_ + ltmp.getLink();
					} else {
						url = ltmp.getLink();
					}

					System.out.println(ltmp.getLinkText() + "|[" + url + "]");

					if (null == client.get(url)) {
						article = new Article();
						article.setWebId(713);
						article.setArticleUrl(url);
						article.setText("NED"); // NED_WALLCOO
						article.setIntro("");
						NodeList tmp = ltmp.getChildren();
						if (tmp != null && tmp.size() > 0) {
							ImageTag imgTag = (ImageTag) tmp.elementAt(0);
							if (null != imgTag.getImageURL())
								article.setActicleXmlUrl(imgTag.getImageURL());
							if (null == imgTag.getAttribute("alt")) {
								article.setTitle("NT:"
										+ CommonUtil.getDateTimeString());
							} else {
								article.setTitle(imgTag.getAttribute("alt"));
							}

							int key = articleDao.insert(article);
							if (key > 0) {
								System.out.print(">> 连接名称:"
										+ ltmp.getLinkText());
								System.out.println(">> URL:" + url);
								client.add(url, url);
							}
						} else {
							System.err.println(">> 已存在相同的内容 ["
									+ ltmp.getLinkText() + "]");
						}
					}

					if (null != p2)
						p2 = null;
				}
			}
			if (null != parser)
				parser = null;
		}
	}

	static void imgDownload() throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for(WebsiteBean website:webList){
			System.out.println(website.getId()+"|"+website.getName()+"|"+website.getUrl());
			List<Article> artList = articleDao.findByWebId(website.getId(), "FD");
			System.out.println("文章数量:"+artList.size());
			for(Article article:artList){
				List<ImageBean> list = imageDao.findImage(article.getId());
				System.out.println(">> 图片数量:["+article.getTitle()+"["+article.getId()+"]]"+list.size()+"\n**************************");
				if(list.size() == 0){
					article.setText("NED");
					if(articleDao.update(article)){
						System.out.println(">> 更新图片记录数据为0的文章成功");
					}
				}else{
					for(ImageBean bean : list) {
						if(bean.getLink().equalsIgnoreCase("NED")){
							if(download(bean)){
								System.out.println(">> 更新图片对象["+bean.getId()+"]成功");
							}
						}
					}
				}
			}
		}
	}

	static boolean download(ImageBean imgBean) {
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
			byte[] small = null;
			small = HttpClientUtils.getResponseBodyAsByte(imgBean.getImgUrl(), null, imgBean.getImgUrl());
			if(null == small)
				return false;
			//小图
			if (client.get(CacheUtils.getShowImgKey(PIC_SAVE_PATH
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ s_fileName)) == null) {
				IOUtil.createFile(small, File.separator+PIC_SAVE_PATH
						+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
						+ imgBean.getArticleId() + File.separator
						+ s_fileName);
			}
			
			byte[] big = null;
			big = HttpClientUtils.getResponseBodyAsByte(imgBean.getCommentshowurl(), null, imgBean.getHttpUrl());
			if(null == big)
				return false;
			length = String.valueOf(big.length);
			if(length.equalsIgnoreCase("20261")){
				return false;
			}
			//大图
			if (client.get(CacheUtils.getBigPicFileKey(PIC_SAVE_PATH
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ fileName)) == null) {
				IOUtil.createFile(big, File.separator+PIC_SAVE_PATH
						+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
						+ imgBean.getArticleId() + File.separator
						+ fileName);
			}
			bean.setArticleId(imgBean.getArticleId());
			bean.setImageId(imgBean.getId());
			bean.setTitle(imgBean.getTitle());
			bean.setSmallName(File.separator
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ s_fileName);
			bean.setName(File.separator
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ fileName);
			bean.setUrl(PIC_SAVE_PATH);
			try {
				imgBean.setStatus(1);
				imgBean.setLink("FD");
				imgBean.setFileSize(Long.valueOf(length));
				if(imageDao.update(imgBean)){
					boolean b = picFiledao.insert(bean);
					if (b) {
						client.add(CacheUtils.getBigPicFileKey(bean.getUrl()
								+ bean.getName()), bean);
						client.add(CacheUtils.getSmallPicFileKey(bean.getUrl()
								+ bean.getSmallName()), bean);
					} else {
						return false;
					}
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
	
	static void movefile() throws Exception{
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		PicfileBean bean = null;
		for(WebsiteBean website:webList){
			System.out.println(website.getId()+"|"+website.getName()+"|"+website.getUrl());
			List<Article> artList = articleDao.findByWebId(website.getId(), "FD");
			System.out.println("文章数量:"+artList.size());
			for(Article article:artList){
				List<ImageBean> list = imageDao.findImage(article.getId());
				for(ImageBean img:list){
					bean = picFiledao.findByImgIdAndArticleId(img.getId(), article.getId());
					if(null != bean){
						if(moveFile(bean)){
							System.out.println(bean.getId()+"|"+bean.getArticleId()+"|"+bean.getImageId());
							System.out.println("after move file name:"+bean.getName());
							System.out.println("after move file smallName:"+bean.getSmallName());
							System.out.println("-------------------------------------------------------------");
						}
					}
				}
			}
		}
	}
	
	static boolean moveFile(PicfileBean bean) {
		boolean isBig = false;
		boolean isSmall = false;
		int start = bean.getName().lastIndexOf(File.separator)+1;
		int smnStart = bean.getSmallName().lastIndexOf(File.separator)+1;
		String prx = StringUtils.gerDir(String.valueOf(bean.getArticleId()));
		String fileName = prx+bean.getArticleId()+File.separator+bean.getName().substring(start);
		String smallFileName = prx+bean.getArticleId()+File.separator+bean.getSmallName().substring(smnStart);
		String source = bean.getUrl() + bean.getName();
		String smgSource = bean.getUrl() + bean.getSmallName();
		String target = FILE_SERVER+fileName;
		String smgTarget = FILE_SERVER + smallFileName;
		bean.setUrl(FILE_SERVER);
		if(FileUtils.copyFile(source, target)){
			System.out.println(">> 大图成功!!!");
			if(FileUtils.deleteFile(source)){
				System.out.println(">> 删除源大图["+source+"]成功");
			}
			bean.setName(fileName);
			isBig = true;
		}
		
		if(FileUtils.copyFile(smgSource, smgTarget)){
			System.out.println(">> 小图成功!!!");
			if(FileUtils.deleteFile(smgSource)){
				System.out.println(">> 删除源小图["+smgSource+"]成功");
			}
			bean.setSmallName(smallFileName);
			isSmall = true;
		}
		if(isBig){
			if(isBig || isSmall){
				try{
					if(picFiledao.update(bean)){
						System.out.println(">> 更新图片文件["+bean.getId()+"]记录成功!");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return isBig;
	}

	static void test() throws Exception{
		ResultBean result = hasPaging2("http://www.kpbz.net/bizhi/anime/");
		if (result.isBool()) {
			Iterator it = result.getMap().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				System.out.println("key:"+key);
				LinkBean link = result.getMap().get(key);
				try {
					secondURL(link, 1201);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("key:" + key);
					break;
				}
				break;
			}
		}
	}
}
