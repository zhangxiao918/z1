package com.p163;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

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
import com.chinamilitary.util.HttpClientUtils;
import com.parser.tag.LITag;
import com.parser.tag.ULTag;

/**
 * 163新闻网首页数据
 * 
 * @author Bluestome.Zhang
 * 
 */
public class IndexParser {
	
	static byte[] LOCK = new byte[0];
	
	static int D_WEB_ID = 9884;

	static String URL = "http://news.163.com/photo/";

	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	static List<Article> ARTICLELIST = new ArrayList<Article>();

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	static MemcacheClient client = MemcacheClient.getInstance();

	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();
	
	static int COUNT = 0;

	/**
	 * 获取分类链接
	 * @param url 指定的网页
	 * @throws Exception
	 */
	static void catalog(String url) throws Exception {
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("UTF-8");
		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(new HasAttributeFilter("class","nav_sub"));

		if (null != list && list.size() > 0) {
			// 主页中的第7个table
			Div div = (Div) list.elementAt(0);
			if(null != div && div.toHtml() != null && !div.toHtml().equals("")){
				Parser p1 = new Parser();
				p1.setInputHTML(div.toHtml());
				
				fileter = new NodeClassFilter(LinkTag.class);
				list = p1.extractAllNodesThatMatch(fileter);
				
				WebsiteBean website = null;
				for(int i=0;i<list.size();i++){
					synchronized(LOCK){
						LinkTag link = (LinkTag)list.elementAt(i);
						if(null != link && !link.getLink().equals("#")){
							System.out.println(""+link.getLinkText()+"|"+link.getLink());
							if(null == webSiteDao.findByUrl(link.getLink())){
								website = new WebsiteBean();
								website.setParentId(D_WEB_ID);
								website.setName(link.getLinkText());
								website.setUrl(link.getLink());
								website.setType(1);
								website.setStatus(1);
								if(webSiteDao.insert(website)){
									System.err.println(" >> 站点["+website.getName()+"|"+website.getUrl()+"]添加成功!");
								}
							}else{
								System.err.println("站点["+link.getLinkText()+"|"+link.getLink()+"]已存在");
								ResultBean result = hasPaging(link.getLink(),"class","bar_page");
								if(result.isBool()){
									Iterator it = result.getMap().keySet().iterator();
									while(it.hasNext()){
										String key = (String)it.next();
										LinkBean value = result.getMap().get(key);
										if(null != value){
											System.out.println(key);
										}
									}
								}
							}
						}
					}
				}
				if(null != list && list.size() > 0){
					list = null;
				}
				if(null != fileter){
					fileter = null;
				}
				if(null != p1){
					p1 = null;
				}
			}
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
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter(cls, value));
		LinkBean l1 = new LinkBean();
		l1.setLink(url);
//		result.put(url, l1);
		result.getList().add(l1);
		result.setBool(true);
		if (list != null && list.size() > 0) {
			Parser p2 = new Parser();
			p2.setInputHTML(list.toHtml());
			NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
			NodeList list2 = p2.extractAllNodesThatMatch(filter2);
			if (null != list2 && list2.size() > 0) {
				for(int i=0;i<list2.size();i++){
					LinkTag lt = (LinkTag)list2.elementAt(i);
					if(null != lt && lt.getLink().startsWith("http://")){
						l1 = new LinkBean();
						l1.setLink(lt.getLink());
						l1.setTitle(lt.getLinkText());
//						result.put(lt.getLink(), l1);
						result.getList().add(l1);
					}
				}
			} else {
				l1 = new LinkBean();
				l1.setLink(url);
//				result.put(url, l1);
				result.getList().add(l1);
			}

			if (null != p2)
				p2 = null;
		}
		return result;
	}
	
	static int getArticle(String url,int webId){
		Parser p1 = null;
		Parser p2 = null;
		int c = 0;
		try{
			PrototypicalNodeFactory factory = new PrototypicalNodeFactory ();  
			factory.registerTag(new ULTag());
			factory.registerTag(new LITag());
			
			p1 = new Parser();
			p1.setURL(url);
			p1.setNodeFactory(factory);
			
			NodeFilter filter = new NodeClassFilter(ULTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter)
			.extractAllNodesThatMatch(
					new HasAttributeFilter("class", "list_pic pic_6 clearfix"));
			if(null != list && list.size() > 0){
				for(int i=0;i<list.size();i++){
					ULTag ul = (ULTag)list.elementAt(i);
					p2 = new Parser();
					p2.setInputHTML(ul.toHtml());
					p2.setNodeFactory(factory);
					
					
					NodeFilter liFilter = new NodeClassFilter(LITag.class);
					NodeList list2 = p2.extractAllNodesThatMatch(liFilter);
					
					if(null != list2 && list2.size() > 0){
						Article article = null;
						for(int j=0;j<list2.size();j++){
							LITag li = (LITag)list2.elementAt(j);
							if(li.getChildCount() > 0){
								LinkTag node = (LinkTag)li.getChild(0);
								NodeList ln = node.getChildren();
								if (ln != null && ln.size() > 0) {
									if (ln.elementAt(0) instanceof ImageTag) {
										article = new Article();
										ImageTag imgTag = (ImageTag) ln.elementAt(0);
										if (imgTag.getImageURL().startsWith("http://")) {
											article.setActicleXmlUrl(imgTag.getImageURL());
										} else {
											article.setActicleXmlUrl(URL
													+ imgTag.getImageURL());
										}
										if (null != imgTag.getAttribute("alt")) {
											article.setTitle(imgTag.getAttribute("alt"));
										}
										article.setWebId(webId);
										if (!node.getLink().startsWith("http://")) {
											article.setArticleUrl(URL + node.getLink());
										} else {
											article.setArticleUrl(node.getLink());
										}
										article.setText("NED"); // NED_WALLCOO
										article.setIntro("NPP[NO Preview Picture]");
										if(null == article.getTitle() || article.getTitle().equals("")){
											article.setTitle(node.getLinkText());
										}
										System.out.println(article.toString());
										if(client.get(article.getArticleUrl()) == null){
											int key = articleDao.insert(article);
											if (key > 0) {
												Article art = articleDao.findById(key);
												if(art.getArticleUrl().toLowerCase().contains("photoview")){
													System.out.println("\t 使用图片解析器|\t"+article.toString());
													if(getImage(art)){
														art.setText("FD");
														if (articleDao.update(art)) {
															c++;
															client.add(article.getArticleUrl(),article.getArticleUrl());
														}
													}
												}else{
													System.out.println("\t 使用其他解析器|\t"+article.toString());
												}
											}
										}else{
											System.err.println(article.getArticleUrl()+"_已经处理过了");
										}

									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1)
				p1 = null;
			if(null != p2)
				p2 = null;
		}
		return c;
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
		String[][] result = NormalParser.execute(article.getArticleUrl());
		if(null == result || result.length == 0){
			return b;
		}
		int size = 0;
		String length = "0";
		ImageBean imgBean = null;
		try{
			int complete = 0;
			for(String[] img:result){
				if(null == img[0] || null == img[1] || null == img[2] || null == img[3] ||
						img[0].equals("") || img[1].equals("") || img[2].equals("") || img[3].equals("")){
					continue;
				}
				imgBean = new ImageBean();
				imgBean.setArticleId(article.getId());
				imgBean.setHttpUrl(img[2]);
				imgBean.setImgUrl(img[3]);
				imgBean.setTitle(article.getTitle());
				imgBean.setLink("NED");
				imgBean.setIntro(img[1]);
				imgBean.setCommentshowurl("NONE");
				try {
					length = HttpClientUtils.getHttpConentLength(img[2]);
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
						client.add(imgBean.getHttpUrl(),
								imgBean.getHttpUrl());
						b = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				complete++;
			}
			if(complete == result.length){
				b = true;
			}
		}catch(Exception e){
			e.printStackTrace();
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
	public static boolean getImage(String url, int artId) throws Exception {
		Parser p1 = null;
		Parser p2 = null;
		int size = 0;
		boolean b = false;
		String length = "0";
		ImageBean imgBean = null;
		try {
			p1 = new Parser();
			p1.setURL(url);

			NodeFilter filter = new NodeClassFilter(CompositeTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "box"));
			if (null != list && list.size() > 0) {/*
				CompositeTag tag = (CompositeTag) list.elementAt(0);
				ImageTag img = getImageFromTag(tag);
				if(null == img){
					return false;
				}
				imgBean = new ImageBean();
				imgBean.setArticleId(artId);
				imgBean.setHttpUrl(img.getImageURL());
				imgBean.setImgUrl(img.getImageURL());
				if (null != img.getAttribute("alt"))
					imgBean.setTitle(img.getAttribute("alt"));
				else
					imgBean.setTitle("NT:"
							+ CommonUtil.getDateTimeString());
				imgBean.setLink("NED");
				imgBean.setCommentshowurl("NONE");
				try {
					length = HttpClientUtils.getHttpConentLength(img.getImageURL());
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
						client.add(imgBean.getHttpUrl(),
								imgBean.getHttpUrl());
						b = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			*/}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != p1)
				p1 = null;
			if (null != p2)
				p2 = null;
		}
		return b;
	}
	
	private static void update(){
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(D_WEB_ID);
			for(WebsiteBean bean:list){
				String lastModify = HttpClientUtils.getLastModifiedByUrl(bean.getUrl());
				if(null != bean.getLastModifyTime() && !"".equals(bean.getLastModifyTime()) && bean.getLastModifyTime().equals(lastModify)){
					continue;
				}
				ResultBean result = hasPaging(bean.getUrl(),"class","bar_page");
				//TODO 因为类型不一样，需要根据不同的网站结构指定不同的页面解析器
				//TODO 例如：大事件图片报道,摄影界 页面中子内容中的界面结构就不一样，需要分别解析。
				if(result.isBool()){
					for(LinkBean lb:result.getList()){
						int c = getArticle(lb.getLink(),bean.getId());
						if(c > 0){
							System.out.println("OK");
						}
					}
				}
				if(lastModify != null && !"".equals(lastModify) ){
					if(null == bean.getLastModifyTime() || "".equals(bean.getLastModifyTime()) || !bean.getLastModifyTime().equals(lastModify)){
						bean.setLastModifyTime(lastModify);
						if(webSiteDao.update(bean)){
							System.err.println(" >> 更新网站["+bean.getName()+"|"+bean.getUrl()+"]最后时间["+lastModify+"]成功!");
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		try{
			
		}catch(Exception e){
			
		}
	}
	
	public static void test() throws Exception{
		ResultBean result = hasPaging("http://news.163.com/special/00014IHM/fastpicnews.html","class","bar_page");
		if(result.isBool()){
			for(LinkBean bean:result.getList()){
//				System.out.println(bean.getTitle()+"|"+bean.getLink());
				System.out.println("文章数量:"+getArticle(bean.getLink(),-1));
			}
			
/*			Iterator it = result.getMap().keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				LinkBean value = result.getMap().get(key);
				if(null != value){
					System.out.println("文章数量:"+getArticle(key));
				}
			}
*/		}
	}
}
