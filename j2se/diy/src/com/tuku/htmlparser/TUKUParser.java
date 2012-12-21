package com.tuku.htmlparser;

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

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
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
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.common.Constants;
import com.utils.FileUtils;

public class TUKUParser {

	static String URL = "http://www.tuku.cn/";
	
	static String IMAGE_URL = "http://image6.tuku.cn/";
	
	static Integer D_PARENT_ID = 400;
	
	final static String FILE_SERVER = Constants.FILE_SERVER;
	
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
		NodeFilter fileter = new NodeClassFilter(TableTag.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter);
		
		if(null != list && list.size() > 0){
			//主页中的第7个table
			TableTag table = (TableTag)list.elementAt(6);
			Parser p2 = new Parser();
			p2.setInputHTML(table.getChildrenHTML());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p2.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					System.out.println(link.getLinkText());
					System.out.println(URL+link.getLink() + "\n");
					tmp = new WebsiteBean();
					tmp.setName(link.getLinkText());
					tmp.setUrl(URL+link.getLink());
					tmp.setParentId(D_PARENT_ID);
					boolean b = webSiteDao.insert(tmp);
					if (b) {
						client.add(tmp.getUrl(), tmp.getUrl());
						System.out.println("成功");
					} else {
						System.out.println("失败");
					}
				}
			}
		}
		if(null != parser)
			parser = null;
	}
	
	/**
	 * 获取分类下的分页信息
	 * @param url
	 * @param attribute
	 * @param value
	 * @return
	 * @throws Exception
	 */
	static ResultBean hasPaging(String url,String attribute,String value) throws Exception{
		boolean b = false;
		int count = 1;
		String link = url;
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);
		LinkBean l1 = null;
		
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Span.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter(attribute, value));
		if(list != null  && list.size() > 0){
			Span span = (Span)list.elementAt(0);
			String tmp = span.getChildrenHTML();
			int start = tmp.indexOf("\">");
			int end = tmp.indexOf("</font>");
			String tmpCount = tmp.substring(start+2,end);
			try{
				count = Integer.parseInt(tmpCount);
			}catch(Exception e){
				e.printStackTrace();
			}
			for(int i=1;i<count+1;i++){
				if(i>1){
					link = url+"&Page="+i;
				}
//				boolean isTrue = TestHttpClient.urlValidation(link);
//				if(isTrue){
				l1 = new LinkBean();
				l1.setLink(link);//"htm/"+
				l1.setName(link);//link.getLinkText()
				result.put(link, l1);
//				}else{
//					link = url+"?Page="+i;
//					if(TestHttpClient.urlValidation(link)){
//						l1 = new LinkBean();
//						l1.setLink(link);//"htm/"+
//						l1.setName(link);//link.getLinkText()
//						result.put(link, l1);
//					}
//				}
			}
			b = true;
		}
		l1 = new LinkBean();
		l1.setLink(url);//"htm/"+
		l1.setName(url);//link.getLinkText()
		result.put(url, l1);
		result.setBool(true);
		return result;
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
//					processWithDoc(bean.getId(), content);
					Long end1 = System.currentTimeMillis();
					System.out.println("单条耗时:"+(end1-start1)+"长度："+content.getBytes().length);
				}
			}catch(Exception e){
				System.out.println("Exception:"+e.getMessage());
				continue;
			}
		}
		Long end = System.currentTimeMillis();
		System.out.print("总耗时:"+(end-start));
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
		NodeFilter filter = new NodeClassFilter(TableTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("id", "DataList1"));
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
							Article article = null;
							if (cnl.elementAt(0) instanceof ImageTag) {
								ImageTag it = (ImageTag) cnl.elementAt(0);
								String url = URL + nl.getLink();
								System.out.print(">> 连接名称:"+it.getAttribute("alt"));
								System.out.println(">> URL:"+url);
								String src = it.getImageURL();
								if(null == client.get(url)){
									article = new Article();
									article.setWebId(webId);
									article.setArticleUrl(url);
									article.setTitle(getTitle(it.getAttribute("alt"),"NT")); //NT:No Title
									article.setText("NED"); //NED_WALLCOO
									article.setIntro("");
									if(null != src){
										article.setActicleXmlUrl(src);
									}
									int key = articleDao.insert(article);
									if (key > 0) {
										System.out.print(">> 连接名称:"+it.getAttribute("alt"));
										System.out.println(">> URL:"+url);
										client.add(url, url);
									} 
								}else{
									System.err.println(">> 已存在相同的内容 ["+nl.getLink()+"]");
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 获取分类下数据
	 * 
	 * @param link
	 * @param webId
	 * @throws Exception
	 */
	public static void getArticleIconByURL(LinkBean link, int webId) throws Exception {
		Parser parser = new Parser();
		parser.setURL(link.getLink());
		parser.setEncoding("UTF-8");

		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(TableTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("id", "DataList1"));
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
							Article article = null;
							if (cnl.elementAt(0) instanceof ImageTag) {
								ImageTag it = (ImageTag) cnl.elementAt(0);
								String url = URL + nl.getLink();
								System.out.print(">> 连接名称:"+it.getAttribute("alt"));
								System.out.println(">> URL:"+url);
								String src = it.getImageURL();
								article = new Article();
								article.setWebId(webId);
								article.setArticleUrl(url);
								article.setTitle(getTitle(it.getAttribute("alt"),"NT")); //NT:No Title
								article.setText("NED"); //NED_WALLCOO
								article.setIntro("");
								if(null != src){
									article.setActicleXmlUrl(src);
								}
								Article atmp = articleDao.findByUrl(article.getArticleUrl(),webId);
								if(null != atmp){
									if(atmp.getArticleUrl().equals(article.getArticleUrl()) && atmp.getTitle().equals(article.getTitle())){
										atmp.setActicleXmlUrl(article.getActicleXmlUrl());
										if(articleDao.update(atmp)){
											System.out.println(" >> 更新"+atmp.getId()+".ActicleXmlUrl:"+atmp.getActicleXmlUrl()+"成功!");
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取图片地址下数据
	 * 
	 * @param link
	 * @param webId
	 * @throws Exception
	 */
	public static void getImage(Article article) throws Exception {
		Parser parser = new Parser();
		parser.setURL(article.getArticleUrl());
		parser.setEncoding("UTF-8");
		String length = "0";
		int size = 0;
		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(TableTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("id", "DataList1"));
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
								String url = IMAGE_URL + getImageUrl(nl.getLink());
								if(null == client.get(url)){
									length = HttpClientUtils.getHttpHeaderResponse(url,"Content-Length");
									imgBean = new ImageBean();
									imgBean.setArticleId(article.getId());
									imgBean.setHttpUrl(url);
									imgBean.setImgUrl(it.getImageURL());
									imgBean.setTitle(it.getAttribute("alt"));
									try{
										size = Integer.parseInt(length);
										imgBean.setFileSize(Long.valueOf(size));
										imgBean.setStatus(3);
									}catch(Exception e){
										e.printStackTrace();
										System.err.println(">> IMAGE SIZE ERROR");
										size = 0;
										imgBean.setFileSize(0l);
										imgBean.setStatus(1);
									}
									imgBean.setLink("NED");
									imgBean.setOrderId(i);
									imgBean.setArticleId(article.getId());
									// HttpClientUtils
									int result = imageDao.insert(imgBean);
									if (result > 0) {
										System.out.println(">> add article["+article.getId()+":"+article.getTitle()+"] image id["+result+"] to DB");
										imgBean.setId(result);
										client.add(url, url);
									} else{
										System.err.println(">> 未添加[]到数据库中");
									}
//									System.out.println("图片大图:"+url);
//									System.out.println("图片小图:"+it.getImageURL());
//									System.out.println("图片标题:"+it.getAttribute("alt"));
								}else{
									System.err.println(">> 缓存中已存在相同的内容 ["+nl.getLink()+"]");
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 获取分类下数据
	 * 
	 * @param link
	 * @param webId
	 * @throws Exception
	 */
	public static void getImage(LinkBean link, int webId) throws Exception {
		Parser parser = new Parser();
		parser.setURL(link.getLink());
		parser.setEncoding("UTF-8");

		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(TableTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("id", "DataList1"));
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
								String url = URL + nl.getLink();
								if(null == client.get(url)){
									imgBean = new ImageBean();
//									imgBean.setArticleId(article.getId());
									imgBean.setHttpUrl(it.getImageURL());
//									String length = HttpClientUtilsgetHttpHeaderResponse(bean.getHttpUrl(),
//											"Content-Length");
//									System.out.println(">> Content-Length:" + length);
									System.out.println("地址1： "+link.getLink());
									System.out.println("地址2： "+IMAGE_URL+getImageUrl(nl.getLink()));
									System.out.println("图片大图:"+it.getImageURL());
									System.out.println("图片标题:"+it.getAttribute("alt"));
									String length = HttpClientUtils.getHttpHeaderResponse(IMAGE_URL+getImageUrl(nl.getLink()),
									"Content-Length");
									System.out.println(">> Content-Length:" + length);
								}else{
									System.err.println(">> 已存在相同的内容 ["+nl.getLink()+"]");
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取图片地址下数据
	 * 
	 * @param link
	 * @param webId
	 * @throws Exception
	 */
	public static void getImage(LinkBean link, int webId,int articleId) throws Exception {
		Parser parser = new Parser();
		parser.setURL(link.getLink());
		parser.setEncoding("UTF-8");
		String length = "0";
		int size = 0;
		// 获取指定ID的TableTag内容
		NodeFilter filter = new NodeClassFilter(TableTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter("id", "DataList1"));
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
								String url = IMAGE_URL + getImageUrl(nl.getLink());
								if(null == client.get(url)){
//									length = HttpClientUtils.getHttpHeaderResponse(url,"Content-Length");
									imgBean = new ImageBean();
									imgBean.setArticleId(articleId);
									imgBean.setHttpUrl(url);
									imgBean.setImgUrl(it.getImageURL());
									imgBean.setTitle(it.getAttribute("alt"));
									try{
										size = Integer.parseInt(length);
										imgBean.setFileSize(Long.valueOf(size));
										imgBean.setStatus(3);
									}catch(Exception e){
										e.printStackTrace();
										System.err.println(">> IMAGE SIZE ERROR");
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
										System.out.println(">> add article["+articleId+"] image id["+result+"] to DB");
										imgBean.setId(result);
										client.add(url, url);
									} else{
										System.err.println(">> 未添加["+url+"]到数据库中");
									}
								}else{
									System.err.println(">> 缓存中已存在相同的内容 ["+nl.getLink()+"]");
								}
							}
						}
					}
					Thread.sleep(10);
				}
			}
		}
	}
	
	static String getImageUrl(String link){
		int start = link.indexOf("=");
		int end = link.length();
		String imgUrl = link.substring(start+1,end);
		return imgUrl;
	}
	
	static String getTitle(String title,String defaultTitle){
		if(null == title || "".equalsIgnoreCase(title)){
			return defaultTitle+":"+System.currentTimeMillis();
		}
		return title;
	}
	
	static void init() {
		try{
			List<WebsiteBean>  webList = webSiteDao.findByParentId(D_PARENT_ID);
			for(WebsiteBean bean:webList){
				List<Article> articleList = articleDao.findByWebId(bean.getId());
				for(Article article:articleList){
					if(null == client.get(article.getArticleUrl())){
						client.add(article.getArticleUrl(), article.getArticleUrl());
					}else{
						System.err.println(">> 文章["+article.getTitle()+"|"+article.getArticleUrl()+"]已存在于缓存中");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		try{
//			catalog(URL);
			List<WebsiteBean>  webList = webSiteDao.findByParentId(D_PARENT_ID);
			for(WebsiteBean bean:webList){
				ResultBean result = hasPaging(bean.getUrl(), "id", "lblPageCount");
				if(result.isBool()){
					Iterator it = result.getMap().keySet().iterator();
					while(it.hasNext()){
						String key = (String)it.next();
						LinkBean link = result.getMap().get(key);
						try{
							secondURL(link,bean.getId());
						}catch(Exception e){
							e.printStackTrace();
							System.out.println("key:"+key);
							continue;
						}
					}
				}
			}
			
//			TUKUThread[] thread = new TUKUThread[webList.size()];
//			for(int i=0;i<webList.size();i++){
//				WebsiteBean bean = (WebsiteBean)webList.get(i);
//				thread[i] = new TUKUThread(bean.getId());
//				thread[i].start();
//			}
			
			update();
			
//			movefile();
			
//			patchIcon();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void patchIcon() throws Exception {
		List<WebsiteBean>  webList = webSiteDao.findByParentId(D_PARENT_ID);
		for(WebsiteBean bean:webList){
			ResultBean result = hasPaging(bean.getUrl(), "id", "lblPageCount");
			if(result.isBool()){
				Iterator it = result.getMap().keySet().iterator();
				while(it.hasNext()){
					String key = (String)it.next();
					LinkBean link = result.getMap().get(key);
					try{
						getArticleIconByURL(link, bean.getId());
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("key:"+key);
						continue;
					}
				}
			}
		}
	}

	static void update() throws Exception{
		List<WebsiteBean>  webList = webSiteDao.findByParentId(D_PARENT_ID);
		for(WebsiteBean bean:webList){
			List<Article> articleList = articleDao.findByWebId(bean.getId(),"NED");
			for(Article article:articleList){
				List<ImageBean> imgList = imageDao.findImage(article.getId());
				if(imgList.size() == 0){
					try{
						ResultBean result = hasPaging(article.getArticleUrl(), "id", "lblPageCount");
						if(result.isBool()){
							Iterator it = result.getMap().keySet().iterator();
							boolean isFull = true;
							while(it.hasNext()){
								String key = (String)it.next();
								LinkBean link = result.getMap().get(key);
								try{
									getImage(link,article.getWebId(),article.getId());
								}catch(Exception e){
									e.printStackTrace();
									isFull = false;
									continue;
								}
							}
							if(isFull){
								article.setText("FD");
							}else{
								article.setText("NED");
							}
						}else{
							article.setText("NED");
						}
					}catch(Exception e){
						article.setText("NED");
						System.err.println(">> ["+article.getId()+"|"+article.getTitle()+"]Excepton:"+e);
					}finally{
						if(!articleDao.update(article)){
							System.err.println(">> 更新记录["+article.getArticleUrl()+"]失败");
						}
					}
				}else{
					article.setText("FD");
					if(!articleDao.update(article)){
						System.err.println(">> 更新记录["+article.getArticleUrl()+"]失败");
					}
				}
			}
		}
	}
	
	static void movefile() throws Exception{
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		PicfileBean bean = null;
		for(WebsiteBean website:webList){
			System.out.println(website.getId()+"|"+website.getName()+"|"+website.getUrl());
			List<Article> artList = articleDao.findByWebId(website.getId(), "NED");
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
//					break;
				}
//				break;
			}
//			break;
		}
	}
	
	static boolean moveFile(PicfileBean bean) {
		String tmpUrl = "P:\\share\\file\\tuku\\";
		boolean isBig = false;
		boolean isSmall = false;
		int start = bean.getName().lastIndexOf(File.separator)+1;
		int smnStart = bean.getSmallName().lastIndexOf(File.separator)+1;
		String prx = StringUtils.gerDir(String.valueOf(bean.getArticleId()));
		String fileName = prx+bean.getArticleId()+File.separator+bean.getName().substring(start);
		String smallFileName = prx+bean.getArticleId()+File.separator+bean.getSmallName().substring(smnStart);
		String source = tmpUrl + bean.getName();
		String smgSource = tmpUrl + bean.getSmallName().replace(File.separator+"s",File.separator);
		String target = FILE_SERVER+fileName;
		String smgTarget = FILE_SERVER + smallFileName.replace(File.separator+"s",File.separator);
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
	
}
