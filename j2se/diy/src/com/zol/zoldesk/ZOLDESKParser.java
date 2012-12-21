package com.zol.zoldesk;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.Article;
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
import com.chinamilitary.htmlparser.ChinaTUKUParser;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.test.TestHttpClient;
import com.chinamilitary.util.CacheUtils;
import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.common.Constants;
import com.utils.FileUtils;

public class ZOLDESKParser {

	static Log log = LogFactory.getLog(ZOLDESKParser.class);

	static String URL_ = "http://desk.zol.com.cn/";

	static String URL = "http://desk.zol.com.cn";

	static String IMAGE_URL = "http://image6.tuku.cn/";
	
	static int D_PARENT_ID = 701;

	static String VISTA_URL = "http://vista.zol.com.cn";
	
	final static String FILE_SERVER = "O:\\fileserver\\image\\";

	static String PIC_SAVE_PATH = Constants.FILE_SERVER;

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
		parser.setEncoding("UTF-8");
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "logoMenu"));

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
						log.debug(link.getLinkText());
						tmp = new WebsiteBean();
						tmp.setName(link.getLinkText());
						if (!link.getLink().startsWith("http://")) {
							log.debug(URL + link.getLink() + "\n");
							tmp.setUrl(URL + link.getLink());
						} else {
							log.debug(link.getLink() + "\n");
							tmp.setUrl(link.getLink());
						}
						tmp.setParentId(D_PARENT_ID);
						// boolean b = webSiteDao.insert(tmp);
						// if (b) {
						// client.add(tmp.getUrl(), tmp.getUrl());
						// log.debug("成功");
						// } else {
						// log.debug("失败");
						// }
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
			}else{
				LinkBean l1 = new LinkBean();;
				l1.setLink(url);
				l1.setTitle(url);
				result.put(url, l1);
				result.setBool(true);
			}
			if (null != p2)
				p2 = null;
		} else {
			LinkBean l1 = new LinkBean();;
			l1.setLink(url);
			l1.setTitle(url);
			result.put(url, l1);
			result.setBool(true);
		}
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
					log.debug("单条耗时:" + (end1 - start1) + "长度："
							+ content.getBytes().length);
				}
			} catch (Exception e) {
				log.debug("Exception:" + e.getMessage());
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
				.extractAllNodesThatMatch(new HasAttributeFilter("class", "lb"));
		if (list != null && list.size() > 0) {
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
							log.debug(imgTag.getAttribute("alt")
									+ "|[" + url + "]");
							if(imgTag.getImageURL().startsWith("http://")){
								article.setActicleXmlUrl(imgTag.getImageURL());
							}else{
								article.setActicleXmlUrl(URL+imgTag.getImageURL());
							}
							article.setTitle(imgTag.getAttribute("alt")); // NT:No
																			// Title
						}
						if(null == article.getTitle() || article.getTitle().equals("")){
							article.setTitle("NT");
						}
						int key = articleDao.insert(article);
						if (key > 0) {
							log.info(" >> " +article.toString());
							System.out.print(ltmp.getLinkText() + "\t|" + url);
							client.add(url, url);
						}
					} else {
						log.error(">> 已存在相同的内容 [" + ltmp.getLinkText()
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
		boolean b = false;
		ResultBean result = hasPaging(article.getArticleUrl(), "class",
				"page f14b");
		if (result.isBool()) {
			Iterator it = result.getMap().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				log.debug("需要解析的URL:"+key);
				LinkBean link = result.getMap().get(key);
				b = getImage(link, article.getId());
			}
			b = true;
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
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser
				.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("class", "lb"));
		if (null != list && list.size() > 0) {
			Parser p2 = null;
			String length = "0";
			int size = 0;
			for (int i = 0; i < list.size(); i++) {
				Div div = (Div) list.elementAt(i);
				p2 = new Parser();
				p2.setInputHTML(div.toHtml());

				NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
				NodeList list2 = p2.extractAllNodesThatMatch(filter2);

				ImageBean imgBean = null;
				String url = null;
				String imgSrc = null;
				if (null != list2 && list2.size() > 0) {
					LinkTag ltmp = (LinkTag) list2.elementAt(0);
					if (!ltmp.getLink().startsWith("http://")) {
						url = URL + ltmp.getLink();
					} else {
						url = ltmp.getLink();
					}

					imgSrc = getImageURL(url);
					if (null != imgSrc) {
						if (null == client.get(url)) {
							imgBean = new ImageBean();
							imgBean = new ImageBean();
							imgBean.setArticleId(artId);
							imgBean.setHttpUrl(imgSrc);
							NodeList tmp = ltmp.getChildren();
							if (tmp != null && tmp.size() > 0) {
								ImageTag imgTag = (ImageTag) tmp.elementAt(0);
								if (null != imgTag.getImageURL())
									imgBean.setImgUrl(imgTag.getImageURL());
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
							imgBean.setLink("NED");
							try {
								size = Integer.parseInt(length);
								imgBean.setFileSize(Long.valueOf(size));
								imgBean.setStatus(3);
							} catch (Exception e) {
								e.printStackTrace();
								log.error(">> IMAGE SIZE ERROR");
								size = 0;
								imgBean.setFileSize(0l);
								imgBean.setStatus(1);
							}
							int key = imageDao.insert(imgBean);
							if (key > 0) {
								log.debug(imgBean.getTitle() + "\t|"
										+ url);
								client.add(url, url);
							}
						} else {
							log.error(">> 已存在相同的内容 ["
									+ ltmp.getLinkText() + "]");
						}
					} else {
						resultB = false;
						break;
					}
				}

				if (null != p2)
					p2 = null;
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

			NodeFilter filter = new NodeClassFilter(CompositeTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "a_fen12bi"));

			if (null != list && list.size() == 1) {
				LinkTag link = (LinkTag) list.elementAt(0);
				result = link.getLink();
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
								String url = IMAGE_URL
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
										log.debug(">> add article["
												+ articleId + "] image id["
												+ result + "] to DB");
										imgBean.setId(result);
										client.add(url, url);
									} else {
										log.error(">> 未添加[" + url
												+ "]到数据库中");
									}
								} else {
									log.error(">> 缓存中已存在相同的内容 ["
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
			if (!bean.getUrl().equalsIgnoreCase(
					"http://vista.zol.com.cn/desk.html")) {
				String lastModify = HttpClientUtils.getLastModifiedByUrl(bean.getUrl());
				if(null != bean.getLastModifyTime() && !"".equals(bean.getLastModifyTime()) && bean.getLastModifyTime().equals(lastModify)){
					continue;
				}
				
				ResultBean result = hasPaging(bean.getUrl(), "class","page f14b");
				if (result.isBool()) {
					Iterator it = result.getMap().keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						LinkBean link = result.getMap().get(key);
						try {
							secondURL(link, bean.getId());
						} catch (Exception e) {
							e.printStackTrace();
							log.debug("key:" + key);
							continue;
						}
					}
				}
				
				if(lastModify != null && !"".equals(lastModify) ){
					if(null == bean.getLastModifyTime() || "".equals(bean.getLastModifyTime()) || !bean.getLastModifyTime().equals(lastModify)){
						bean.setLastModifyTime(lastModify);
						if(webSiteDao.update(bean)){
							log.info(" >> 更新网站["+bean.getName()+"|"+bean.getUrl()+"]最后时间["+lastModify+"]成功!");
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		// init();
		try {
			// catalog(URL);
//			 update();
//			 vistDesk();
//			threadParser();
//			loadImg();
//			imgDownload();
//			new Thread(new Runnable(){
//				private boolean isRun = true;
//				public void run() {
//					while(isRun){
//						try{
							 update();
							loadImg();
							imgDownload();
							//休眠半小时
//							Thread.sleep(80000000);
//						}catch(Exception e){
//							e.printStackTrace();
//							isRun = false;
//						}
//					}
//				}
//			 }).start();
			
//			movefile();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void movefile() throws Exception{
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		PicfileBean bean = null;
		for(WebsiteBean website:webList){
			log.debug(website.getId()+"|"+website.getName()+"|"+website.getUrl());
			List<Article> artList = articleDao.findByWebId(website.getId(), "FD");
			log.debug("文章数量:"+artList.size());
			for(Article article:artList){
				List<ImageBean> list = imageDao.findImage(article.getId());
				for(ImageBean img:list){
					bean = picFiledao.findByImgIdAndArticleId(img.getId(), article.getId());
					if(null != bean){
						if(moveFile(bean)){
							log.debug(bean.getId()+"|"+bean.getArticleId()+"|"+bean.getImageId());
							log.debug("after move file name:"+bean.getName());
							log.debug("after move file smallName:"+bean.getSmallName());
							log.debug("-------------------------------------------------------------");
						}
					}
//					break;
				}
//				break;
			}
//			break;
		}
	}
	
	static boolean moveFile(PicfileBean bean) {
		String tmpUrl = "P:\\share\\file\\zol\\";
		boolean isBig = false;
		boolean isSmall = false;
		int start = bean.getName().lastIndexOf(File.separator)+1;
		int smnStart = bean.getSmallName().lastIndexOf(File.separator)+1;
		String prx = StringUtils.gerDir(String.valueOf(bean.getArticleId()));
		String fileName = prx+bean.getArticleId()+File.separator+bean.getName().substring(start);
		String smallFileName = prx+bean.getArticleId()+File.separator+bean.getSmallName().substring(smnStart);
		String source = tmpUrl + bean.getName();
		String smgSource = tmpUrl + bean.getSmallName();
		String target = FILE_SERVER+fileName;
		String smgTarget = FILE_SERVER + smallFileName;
		bean.setUrl(FILE_SERVER);
		if(FileUtils.copyFile(source, target)){
			log.debug(">> 大图成功!!!");
			if(FileUtils.deleteFile(source)){
				log.debug(">> 删除源大图["+source+"]成功");
			}
			bean.setName(fileName);
			isBig = true;
		}
		
		if(FileUtils.copyFile(smgSource, smgTarget)){
			log.debug(">> 小图成功!!!");
			if(FileUtils.deleteFile(smgSource)){
				log.debug(">> 删除源小图["+smgSource+"]成功");
			}
			bean.setSmallName(smallFileName);
			isSmall = true;
		}
		if(isBig){
			if(isBig || isSmall){
				try{
					if(picFiledao.update(bean)){
						log.debug(">> 更新图片文件["+bean.getId()+"]记录成功!");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return isBig;
	}
	
	static void loadImg() throws Exception {
		// WebsiteBean bean = webSiteDao.findById(702);
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : webList) {
			List<Article> list = articleDao.findByWebId(bean.getId(),"NED");
			for (Article art : list) {
				List<ImageBean> imgList = imageDao.findImage(art.getId());
//				log.debug(">> zol.com.cn 记录[" + art.getTitle() + "|"+art.getId()+"]下的图片数量["+imgList.size()+"]");
				if(imgList.size() == 0 ){
					log.debug(">> zol.com.cn 记录[" + art.getTitle() + "|"+art.getId()+"]下的图片数量["+imgList.size()+"] ");
					if (!art.getArticleUrl().startsWith("http://vista.zol.com.cn")) {
						if (getImage(art)) {
							art.setText("FD");
							if (articleDao.update(art)) {
								log.info(">> zol.com.cn 更新记录[" + art.getTitle() + "|"+art.getId()+"]成功");
							}
						}
					}
				}
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

			// log.debug(list.toHtml());
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
						url = VISTA_URL + ltmp.getLink();
					} else {
						url = ltmp.getLink();
					}

					log.debug(ltmp.getLinkText() + "|[" + url + "]");

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
								log.debug(">> URL:" + url);
								client.add(url, url);
							}
						} else {
							log.error(">> 已存在相同的内容 ["
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
			log.debug(website.getId()+"|"+website.getName()+"|"+website.getUrl());
			List<Article> artList = articleDao.findByWebId(website.getId(), "FD");
			log.debug("文章数量:"+artList.size());
			for(Article article:artList){
				List<ImageBean> list = imageDao.findImage(article.getId());
				log.debug(">> 图片数量:["+article.getTitle()+"["+article.getId()+"]]"+list.size()+"\n**************************");
				if(list.size() == 0){
					article.setText("NED");
					if(articleDao.update(article)){
						log.debug(">> 更新图片记录数据为0的文章成功");
					}
				}else{
					for(ImageBean bean : list) {
						if(bean.getLink().equalsIgnoreCase("NED")){
							if(download(bean)){
								bean.setStatus(1);
								bean.setLink("FD");
								if(imageDao.update(bean)){
									log.info(">> 更新图片对象["+bean.getId()+"]成功");
								}
							}
						}
					}
				}
		}
		}
	}

	/**
	 * 
	 * @param imgBean
	 * @return
	 */
	static boolean download(ImageBean imgBean) {
		PicfileBean bean = null;
		bean = new PicfileBean();
		String s_fileName = imgBean.getImgUrl().substring(
				imgBean.getImgUrl().lastIndexOf("/") + 1,
				imgBean.getImgUrl().length()).replace(".", "_s.");
		String fileName = imgBean.getHttpUrl().substring(
				imgBean.getHttpUrl().lastIndexOf("/") + 1,
				imgBean.getHttpUrl().length());
		String length = "0";
		try {
			byte[] big = null;
			big = HttpClientUtils.getResponseBodyAsByte(imgBean.getCommentshowurl(), null, imgBean.getHttpUrl());
			if(null == big)
				return false;
			length = String.valueOf(big.length);
			if(length.equalsIgnoreCase("20261")){
				return false;
			}
			//小图
			if (client.get(CacheUtils.getShowImgKey(PIC_SAVE_PATH
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ s_fileName)) == null) {
				IOUtil.createPicFile(imgBean.getImgUrl(), PIC_SAVE_PATH
						+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
						+ imgBean.getArticleId() + File.separator
						+ s_fileName);
			}
			
			//大图
			if (client.get(CacheUtils.getBigPicFileKey(PIC_SAVE_PATH
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ fileName)) == null) {
				IOUtil.createFile(big, PIC_SAVE_PATH
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
				}else{
					return false;
				}
			} catch (Exception e) {
				log.error("数据库异常");
				e.printStackTrace();
				return false;
			}
		} catch (IOException e) {
			log.error("网络连接，文件IO异常");
			return false;
		}
		return true;
	}
	
}