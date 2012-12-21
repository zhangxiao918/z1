package com.chinamilitary.htmlparser;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.TitleTag;
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
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.test.TestHttpClient;
import com.chinamilitary.threadpool.GetImageUrlThread;
import com.chinamilitary.threadpool.RequestRecordThread;
import com.chinamilitary.threadpool.ThreadPoolManager;
import com.chinamilitary.util.CacheUtils;
import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.HTMLDecoder;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.chinamilitary.xmlparser.XMLParser;
import com.common.Constants;
import com.message.RequestRecordQuene;
import com.thread.ResourceQueneInsert;

public class ShowimgParser2 {

	private static Log log = LogFactory.getLog(ShowimgParser2.class);
	
	private static final String URL = "http://www.showimg.com/";
	
	private static final String URL_ = "http://www.showimg.com/htm/";

	private static final String PIC_SAVE_PATH =  Constants.FILE_SERVER;
	
	private static HashMap<String,LinkBean> LINKHASH = new HashMap<String,LinkBean>();
	
	private static HashMap<String,LinkBean> SECONDLINKHASH = new HashMap<String,LinkBean>();
	
	private static HashMap<String,ResultBean> PAGELINKHASH = new HashMap<String,ResultBean>();
	
	private static int COUNT = 0;
	
	static int D_PARENT_ID = 36;
	
	private static ThreadPoolManager manager = null;
	
	private static ImageDao imageDao = DAOFactory.getInstance().getImageDao();
	
	private static ArticleDao  articleDao = DAOFactory.getInstance().getArticleDao();
	
	private static WebSiteDao wesiteDao = DAOFactory.getInstance().getWebSiteDao();
	
	private static PicFileDao picFileDao = DAOFactory.getInstance().getPicFileDao();
	
	private static MemcacheClient client = MemcacheClient.getInstance();
	
	public ShowimgParser2(){
		try{
			
		}catch(Exception e){
			
		}
	}
	
	public static void getParentLink() throws Exception{
		Parser parser = new Parser();
		parser.setURL(URL);
//		ResultBean result = null;
		NodeFilter filter1 = new NodeClassFilter(LinkTag.class);
		NodeList nodes = parser.extractAllNodesThatMatch(filter1);
		LinkBean linkBean = null;
		for(int i=0;i<nodes.size();i++){
			LinkTag link = (LinkTag)nodes.elementAt(i);
			if(link.getLink().startsWith(URL_) && link.getLinkText().length() > 0){ //&& link.getLinkText().length() < 3 
				linkBean = new LinkBean();
				linkBean.setLink(link.getLink());
				linkBean.setName(link.getLinkText());
				LINKHASH.put(link.getLink(), linkBean);
				
				//判断分页
//				result = hasPaging(linkBean,"id","damulu-page");
//				if(result.isBool()){
//					PAGELINKHASH.put(linkBean.getName(), result);
//				}
			}
		}	
		parser = null;
	}
	
	/**
	 * 获取二级菜单中的链接
	 * @param bean
	 * @throws Exception
	 */
	public static void getSecondLink(LinkBean bean) throws Exception{
		Parser parser = new Parser();
		parser.setURL(bean.getLink());
		
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "left"));
		if(list != null && list.size() > 0){
			
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			
			NodeFilter filter1 = new NodeClassFilter(LinkTag.class);
			NodeList nodes = p1.extractAllNodesThatMatch(filter1);
			
			if(nodes != null && nodes.size() > 0){
				LinkBean linkBean = null;
				for(int i=0;i<nodes.size();i++){
					
					LinkTag link = (LinkTag)nodes.elementAt(i);
					linkBean = new LinkBean();
					linkBean.setLink(URL+link.getLink().replace("/", ""));
					linkBean.setName(link.getLinkText());
					ResultBean result = hasPaging(bean.getLink(), "", "");
					if(result.isBool()){
						for(LinkBean lbean:result.getList()){
							getPicUrl(lbean);
						}
					}else{
						getPicUrl(linkBean);
					}
				}
			}
		}
		
	}
	
	/**
	 * 获取二级菜单中的链接
	 * @param bean
	 * @throws Exception
	 */
	public static void getSecondLink(WebsiteBean bean) throws Exception{
//		ResultBean result = hasPaging(bean.getUrl(),"id","damulu-page"); //version 1.0
		ResultBean result = hasPaging(bean.getUrl(),"class","pages");
		if(result.isBool()){
			result.getMap();
			Iterator it = result.getMap().keySet().iterator();
			while(it.hasNext()){
				String lkey = (String)it.next();
				if(null == client.get(lkey)){
					try{
				System.out.println("key:"+lkey);
				LinkBean lb = (LinkBean)result.getMap().get(lkey);

				Parser parser = new Parser();
				parser.setURL(lb.getLink());
				
				// 获取指定ID的DIV内容
				NodeFilter filter = new NodeClassFilter(Div.class);
				NodeList list = parser.extractAllNodesThatMatch(filter)
						.extractAllNodesThatMatch(
//								new HasAttributeFilter("class", "damulu-left2")); //version 2.0
								new HasAttributeFilter("id", "left"));
				if(list != null && list.size() > 0){
					Parser p1 = new Parser();
					p1.setInputHTML(list.toHtml());
					
					NodeFilter filter1 = new NodeClassFilter(LinkTag.class);
					NodeList nodes = p1.extractAllNodesThatMatch(filter1);
					
					if(nodes != null && nodes.size() > 0){
						Article article = null;
						for(int i=0;i<nodes.size();i++){
								LinkTag link = (LinkTag)nodes.elementAt(i);
								if(link.getLink() != null && !link.getLink().equalsIgnoreCase("") && link.getLink().startsWith("/tabulation.php")){
									String url = URL+link.getLink().replace("/", "");
									NodeList tmp = link.getChildren();
									if(tmp != null && tmp.size() > 0){
										ImageTag imgTag = (ImageTag)tmp.elementAt(0);
										if(null == client.get(url)){
											article = new Article();
											article.setWebId(bean.getId());
											article.setArticleUrl(url);
											article.setTitle(imgTag.getAttribute("alt"));
											article.setText("NED"); // No Execute Download
											int key = articleDao.insert(article);
											if (key > 0) {
												log.debug("添加" + imgTag.getAttribute("alt")+ ",成功");
												COUNT++;
											} else {
												log.debug("添加" + imgTag.getAttribute("alt") + "失败,已存在相同标题的内容");
											}
										}
									}
									Thread.sleep(50);
								}
						}
					}
				}
				}catch(Exception e){
					client.remove(lkey);
					log.error(e);
					e.printStackTrace();
					continue;
				}
			}else{
				System.out.println(">> 已存在地址["+lkey+"]");
			}
				
						}
//			for(LinkBean lbean:result.getList()){}
		}else{
			Parser parser = new Parser();
			parser.setURL(bean.getUrl());
			
			// 获取指定ID的DIV内容
			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(filter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "damulu-left2"));
			if(list != null && list.size() > 0){
				Parser p1 = new Parser();
				p1.setInputHTML(list.toHtml());
				
				NodeFilter filter1 = new NodeClassFilter(LinkTag.class);
				NodeList nodes = p1.extractAllNodesThatMatch(filter1);
				
				if(nodes != null && nodes.size() > 0){
					Article article = null;
					for(int i=0;i<nodes.size();i++){
						try{
							article = new Article();
							LinkTag link = (LinkTag)nodes.elementAt(i);
							
							if(link.getLinkText() != null && !link.getLinkText().equalsIgnoreCase("")){
								String url_ = URL+link.getLink().replace("../", "");
								if(null == client.get(url_)){
									article.setWebId(bean.getId());
									article.setArticleUrl(url_);
									article.setTitle(StringUtils.illageString(link.getLinkText()));
									article.setText("NED"); // No Execute Download
									int key = articleDao.insert(article);
									if (key > 0) {
										log.debug("添加" + link.getLinkText()+ ",成功");
										COUNT++;
									} else {
										log.debug("添加" + link.getLinkText() + "失败");
									}
									Thread.sleep(50);
								}
							}
						}catch(Exception e){
							log.error(e);
							continue;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取二级菜单中的链接
	 * @param bean
	 * @throws Exception
	 */
	public static void getArticleIcon(WebsiteBean bean) throws Exception{
//		ResultBean result = hasPaging(bean.getUrl(),"id","damulu-page"); //version 1.0
		ResultBean result = hasPaging(bean.getUrl(),"class","pages");
		if(result.isBool()){
			result.getMap();
			Iterator it = result.getMap().keySet().iterator();
			while(it.hasNext()){
				String lkey = (String)it.next();
				if(null == client.get(lkey)){
					try{
				System.out.println("key:"+lkey);
				LinkBean lb = (LinkBean)result.getMap().get(lkey);

				Parser parser = new Parser();
				parser.setURL(lb.getLink());
				
				// 获取指定ID的DIV内容
				NodeFilter filter = new NodeClassFilter(Div.class);
				NodeList list = parser.extractAllNodesThatMatch(filter)
						.extractAllNodesThatMatch(
								new HasAttributeFilter("id", "left"));
				if(list != null && list.size() > 0){
					Parser p1 = new Parser();
					p1.setInputHTML(list.toHtml());
					
					NodeFilter filter1 = new NodeClassFilter(LinkTag.class);
					NodeList nodes = p1.extractAllNodesThatMatch(filter1);
					
					if(nodes != null && nodes.size() > 0){
						Article article = null;
						for(int i=0;i<nodes.size();i++){
								LinkTag link = (LinkTag)nodes.elementAt(i);
								if(link.getLink() != null && !link.getLink().equalsIgnoreCase("") && link.getLink().startsWith("/tabulation.php")){
									String url = URL+link.getLink().replace("/", "");
									NodeList tmp = link.getChildren();
									if(tmp != null && tmp.size() > 0){
										ImageTag imgTag = (ImageTag)tmp.elementAt(0);
										if(null == client.get(url)){
											article = new Article();
											if(imgTag.getImageURL().startsWith("http://")){
												article.setActicleXmlUrl(imgTag.getImageURL());
											}else{
												article.setActicleXmlUrl(URL+imgTag.getImageURL());
											}
											article.setArticleUrl(url);
											article.setTitle(imgTag.getAttribute("alt"));
											Article atmp = articleDao.findByUrl(article.getArticleUrl(),bean.getId());
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
									Thread.sleep(50);
								}
						}
					}
				}
				}catch(Exception e){
					client.remove(lkey);
					log.error(e);
					e.printStackTrace();
					continue;
				}
			}else{
				System.out.println(">> 已存在地址["+lkey+"]");
			}
				
						}
//			for(LinkBean lbean:result.getList()){}
		}
	}
	/**
	 * 图片页面分页
	 * @param linkBean
	 * @return
	 * @throws Exception
	 */
	private static ResultBean hasPagingTable(LinkBean linkBean) throws Exception{
		ResultBean result = new ResultBean();
		Parser myParser = new Parser();
		myParser.setURL(linkBean.getLink());
		NodeFilter selectFilter = new NodeClassFilter(SelectTag.class);
		NodeList selectList = myParser.extractAllNodesThatMatch(selectFilter);
		if(selectList != null && selectList.size() > 0){
			for(int i=0;i<selectList.size();i++){
				SelectTag select = (SelectTag)selectList.elementAt(i);
				if(select.toHtml().contains("<select name=\'menu1\' onChange=\"MM_jumpMenu(\'parent\',this,0)\">")){
					Parser p1 = new Parser();
					p1.setInputHTML(select.toHtml());
					NodeFilter optionFilter = new NodeClassFilter(OptionTag.class);
					NodeList optionList = p1.extractAllNodesThatMatch(optionFilter);
					if(optionList != null && optionList.size() > 1){
						result.setBool(true);
						LinkBean oplink = null;
						for(int j=0;j<optionList.size();j++){
							OptionTag option = (OptionTag)optionList.elementAt(j);
							oplink = new LinkBean();
							oplink.setLink(URL_+option.getValue());
							oplink.setName(linkBean.getName());
							result.add(oplink);
						}
					}
					break;
				}
			}
			
		}
		return result;
	}

	/**
	 * 获取分页中的图片路径
	 * @param bean
	 * @throws Exception
	 */
	public static void getPicUrl(Article article) throws Exception{
		System.out.println("319.getPicUrl:"+article.getArticleUrl());
		ResultBean resultBean = hasPaging(article.getArticleUrl(),"class","pages");
		boolean b = true;
		if(resultBean.isBool()){
			System.out.println("有分页");
			Iterator it = resultBean.getMap().keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				System.out.println("key:"+key);
				LinkBean bean = (LinkBean)resultBean.getMap().get(key);
				boolean result = getPicURL(bean.getLink(),article);
				if(result){
					continue;
				}else{
					b = false;
					break;
				}
			}
			//TODO STOP
			if(b){
				article.setText("FD");
				boolean result1 = articleDao.update(article);
				if(result1){
					log.debug("更新第"+article.getId()+"条记录成功!!!");
				}else{
					log.debug("更新第"+article.getId()+"条记录失败!!!");
				}
			}
		}else{
			System.out.println("没有分页");
//			log.debug("文档编号："+article.getId()+"\t链接名称："+article.getTitle()+"\t没有分页内容");
			boolean result = getPicURL(article.getArticleUrl(),article);
			//TODO STOP
			if(result){
				article.setText("FD");
				boolean result1 = articleDao.update(article);
				if(result1){
					log.debug("更新第"+article.getId()+"条记录成功!!!");
				}else{
					log.debug("更新第"+article.getId()+"条记录失败!!!");
				}
			}
		}
	}
	
	private static boolean getPicURL(String link, Article article)  throws Exception{
		Parser myParser = new Parser();
		myParser.setURL(link);
		myParser.setEncoding("GBK");
		NodeFilter div = new NodeClassFilter(Div.class);
		NodeList list = myParser.extractAllNodesThatMatch(div).extractAllNodesThatMatch(new HasAttributeFilter("id", "left"));
		if(list != null && list.size() > 0){
	        NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
	        OrFilter lastFilter = new OrFilter();
	        lastFilter.setPredicates(new NodeFilter[] { linkFilter}); //imageFilter
	        Parser p2 = new Parser();
	        p2.setEncoding("GBK");
	        p2.setInputHTML(list.toHtml());
	        NodeList list4 = p2.parse(lastFilter);
	        if(list4 != null && list4.size() > 0){
        		//获取实际图片的地址
        		for(int i=0;i<list4.size();i++){
        			LinkTag imgTag = (LinkTag)list4.elementAt(i);
        			NodeList imgNode = imgTag.getChildren();
        			if(null != imgNode && imgNode.size() == 1){
        				if(imgNode.elementAt(0) instanceof ImageTag){
        					ImageTag img = (ImageTag)imgNode.elementAt(0);
        					ImageBean imageBean = null;
        					imageBean = new ImageBean();
        					imageBean.setArticleId(article.getId());
                			imageBean.setImgUrl(URL+img.getImageURL());
                			imageBean.setTitle(article.getTitle());
                			imageBean.setHttpUrl(getImageURL(URL+imgTag.getLink()));
            				imageBean.setLink("NED");
            				System.out.println("第"+i+"条记录");
//            				//TODO 需要增加缓存判断
            				if(null == client.get(imageBean.getHttpUrl())){
	            				int size = imageDao.insert(imageBean);
	            				if(size > 0){
	            					client.add(imageBean.getHttpUrl(), imageBean.getHttpUrl());
	            					System.out.println("添加图片"+imageBean.getHttpUrl()+"\tarticle.getTitle():"+article.getTitle()+"\t:"+"成功!");
//	            					return true;
	            				}else{
	            					System.out.println(">> 数据库中存在该图片["+imageBean.getHttpUrl()+"]地址 <<");
//	            					return false;
	            				}
            				}else{
            					System.out.println(">> 缓存中存在该图片["+imageBean.getHttpUrl()+"]地址 <<");
//            					return false;
            				}
        				}
        			}
        		}
	        }
		}
		return true;
	}

	public static boolean getImageURL(String url,Integer articleId) throws Exception{
		boolean b = false;
		Parser myParser = new Parser();
		myParser.setURL(url);
		NodeFilter div = new NodeClassFilter(Div.class);
		NodeList list = myParser.extractAllNodesThatMatch(div).extractAllNodesThatMatch(new HasAttributeFilter("id", "contentimg"));
		if(list != null && list.size()>0){
			System.out.println("html:"+list.toHtml());
	        NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
	        NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
	        OrFilter lastFilter = new OrFilter();
	        lastFilter.setPredicates(new NodeFilter[] { linkFilter, imageFilter });
	        Parser p2 = new Parser();
	        p2.setInputHTML(list.toHtml());
	        NodeList list4 = p2.parse(lastFilter);
	        if(list4 != null && list4.size() > 0){
        		ImageBean imageBean = null;
        		imageBean = new ImageBean();
        		imageBean.setArticleId(articleId);
        		if(list4.elementAt(0) instanceof ImageTag){
        			ImageTag imgTag = (ImageTag)list4.elementAt(0);
        			imageBean.setTitle(imgTag.getAttribute("alt"));
        			imageBean.setImgUrl(URL+imgTag.getImageURL());
        			imageBean.setHttpUrl(URL+imgTag.getImageURL());
    				imageBean.setLink("NED");
//    				int size = imageDao.insert(imageBean);
//    				if(size > 0){
//    					System.out.println("添加图片"+imageBean.getTitle()+"成功!");
//    				}else{
//    					System.out.println("添加图片"+imageBean.getTitle()+"失败!");
//    				}
       		}
	        }
			b = true;
		}
		return b;
		
	}
	
	static String getImageURL(String input) throws Exception{
		String imgULR = "";
		Parser myParser = new Parser();
		myParser.setURL(input);
		NodeFilter div = new NodeClassFilter(Div.class);
		NodeList list = myParser.extractAllNodesThatMatch(div).extractAllNodesThatMatch(new HasAttributeFilter("id", "contentimg"));
		if(list != null && list.size()>0){
	        NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
	        NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
	        OrFilter lastFilter = new OrFilter();
	        lastFilter.setPredicates(new NodeFilter[] { linkFilter, imageFilter });
	        Parser p2 = new Parser();
	        p2.setInputHTML(list.toHtml());
	        NodeList list4 = p2.parse(lastFilter);
	        if(list4 != null && list4.size() > 0){
        		if(list4.elementAt(0) instanceof LinkTag){
        			LinkTag linkTag = (LinkTag)list4.elementAt(0);
        			if(null != linkTag.getChildren()){
        				NodeList tmp = linkTag.getChildren();
        				if(null != tmp){
        					ImageTag imgTag = (ImageTag)tmp.elementAt(0);
        					imgULR = URL+imgTag.getImageURL() ;
        				}
        			}
        		}
	        }
		}
		return imgULR;
	}
	
	/**
	 * 获取图片路径
	 * @param url
	 * @param articleId
	 * @throws Exception
	 */
	public static boolean getPicUrl1(String url,Integer articleId) throws Exception{
		boolean resultb = false;
		Parser myParser = new Parser();
		myParser.setURL(url);
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		NodeList tableList = myParser.extractAllNodesThatMatch(tableFilter); // .extractAllNodesThatMatch(new
		if(tableList != null && tableList.size() > 0){
			try{
				//获取TABLE中其他图片的URL
				TableTag table = (TableTag)tableList.elementAt(8);
				if(table != null){
			        NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			        NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
			        OrFilter lastFilter = new OrFilter();
			        AndFilter andFilter = new AndFilter();
			        lastFilter.setPredicates(new NodeFilter[] { linkFilter, imageFilter });
			        andFilter.setPredicates(new NodeFilter[] { linkFilter, imageFilter });
			        Parser p2 = new Parser();
			        p2.setInputHTML(table.toHtml());
			        NodeList list4 = p2.parse(lastFilter);
			        if(list4 != null || list4.size() > 0){
		        		ImageBean imageBean = null;
		        		//遍历TABLE中的图片链接
		        		int count=0;
			        	for(int i=0;i<list4.size();i++){
//			        		log.debug("遍历TABLE中的图片链接");
			        		imageBean = new ImageBean();
			        		imageBean.setArticleId(articleId);
			        		//地址
			        		if(list4.elementAt(i) instanceof LinkTag){
				        		LinkTag nl = (LinkTag)list4.elementAt(i);

				        		//解析每个图片地址的HTML内容
			        			if(nl.getLink() != null && !nl.getLink().equalsIgnoreCase("")){
			        				//指定解析规则和过滤器
			        				Parser p3 = new Parser();
			        				p3.setURL(URL_+nl.getLink());
			        				NodeFilter filter = new NodeClassFilter(Div.class);
			        				NodeList list = p3.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(
			        								new HasAttributeFilter("id", "imgCont"));
			        				if(list != null){
			        					
						        		NodeList cnl = nl.getChildren();
						        		if(cnl != null && cnl.size() > 0){
						        			//小图
						        			if(cnl.elementAt(0) instanceof ImageTag){
							        			ImageTag it = (ImageTag)cnl.elementAt(0);
	    				        				imageBean.setTitle(it.getAttribute("alt"));
//							        			log.debug("title:"+it.getAttribute("alt")+":"+URL+it.getImageURL().replace("../", ""));
							        			imageBean.setImgUrl(URL+it.getImageURL().replace("../", ""));
						        			}
						        		}
						        		
			        					//得到特定的节点内容
			        					Parser parser4 = new Parser();
			        					parser4.setInputHTML(list.toHtml());
			        					NodeList list3 = parser4.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));//parse(lastFilter);//
			        					if(list3 != null){
			        						if(list3.elementAt(0) instanceof LinkTag){
			    				        		LinkTag nl1 = (LinkTag)list3.elementAt(0);
			    				        		NodeList list5 = nl1.getChildren();
			    				        		if(list5 != null && list5.size() > 0){
			    				        			if(list5.elementAt(0) instanceof ImageTag){
			    				        				ImageTag it2 = (ImageTag)list5.elementAt(0);
			    				        				String httpUrl = URL+it2.getImageURL().replace("../", "");
//			    				        				if(client.get(CacheUtils.getImgKey(httpUrl)) == null){
			    				        					if(httpUrl != null && !httpUrl.equalsIgnoreCase("")){
				    				        					int size = imageDao.getCount("select count(*) from tbl_image where d_httpurl = '" +httpUrl+"'");
//				    				        					if(size > 0){
//				    				        						try{
//				    				        						COUNT ++;
//				    				        						ImageBean tmp = imageDao.findByHttpUrl(httpUrl);
//					    				        						if(tmp != null){
//					    				        							client.replace(CacheUtils.getImgKey(httpUrl), tmp);
//					    				        						}
//				    				        						}catch(Exception e){
//				    				        							log.error(e);
//				    				        						}
//				    				        					}else{
				    				        						//findByLink(URL+it2.getImageURL().replace("../", ""));
						    				        				imageBean.setHttpUrl(httpUrl);
						    				        				imageBean.setOrderId(count);
						    				        				imageBean.setLink("NED");
						    				        				imageBean.setIntro(it2.getAttribute("alt"));
//						    				        				RequestRecordQuene.getInstance().setElement(imageBean);
						    				        				int key = imageDao.insert(imageBean);
						    				        				if(key > 0){
//						    				        					client.add(CacheUtils.getImgKey(imageBean.getImgUrl()), imageBean.getImgUrl());
						    				        					log.debug(imageBean.getTitle()+"添加成功!!!");
						    				        				}else{
						    				        					log.debug(imageBean.getTitle()+"添加失败!!!");
						    				        				}
						    				        				log.debug("count:"+count);
						    				        				count ++;
				    				        					}			    				        					
//			    				        					}
//			    				        				}else{
//			    				        					log.debug("缓存中存在相同图片地址的数据");
//			    				        				}
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
				resultb =  true;
 			}catch(ArrayIndexOutOfBoundsException e){
 				e.printStackTrace();
 				log.error(e);
				return false;
			}catch(NullPointerException e){
				e.printStackTrace();
 				log.error(e);
				return false;
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return resultb;
	}
	
	/**
	 * 获取图片路径
	 * @param bean
	 * @throws Exception
	 */
	static void getPicUrl(LinkBean bean) throws Exception{
		Parser myParser = new Parser();
		myParser.setURL(bean.getLink());
		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
		NodeList tableList = myParser.extractAllNodesThatMatch(tableFilter); // .extractAllNodesThatMatch(new
		
		if(tableList != null && tableList.size() > 0){
			try{
				TableTag table = (TableTag)tableList.elementAt(8);
				if(table != null){
			        NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			        NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
			        OrFilter lastFilter = new OrFilter();
			        AndFilter andFilter = new AndFilter();
			        lastFilter.setPredicates(new NodeFilter[] { linkFilter, imageFilter });
			        andFilter.setPredicates(new NodeFilter[] { linkFilter, imageFilter });
			        Parser p2 = new Parser();
			        p2.setInputHTML(table.toHtml());
			        NodeList list4 = p2.parse(lastFilter);
			        if(list4 != null || list4.size() > 0){
			        	for(int i=0;i<list4.size();i++){
			        		//地址
			        		if(list4.elementAt(i) instanceof LinkTag){
				        		LinkTag nl = (LinkTag)list4.elementAt(i);
				        		NodeList cnl = nl.getChildren();
				        		if(cnl != null && cnl.size() > 0){
				        			//小图
				        			if(cnl.elementAt(0) instanceof ImageTag){
				        				log.debug("text:"+nl.getLinkText()+":link:"+nl.getLink());
					        			ImageTag it = (ImageTag)cnl.elementAt(0);
					        			log.debug("title:"+it.getAttribute("alt")+":"+it.getImageURL());
				        			}
				        		}
			        		}
			        	}
			        }
//					log.debug(bean.getName()+",图片TABLE:\n"+table.toHtml()+"\n");
				}
 			}catch(ArrayIndexOutOfBoundsException e){
				
			}
//			for(int i=0;i<tableList.size();i++){
//				TableTag table = (TableTag)tableList.elementAt(i);
//				log.debug(bean.getName()+":"+i+",图片TABLE:\n"+table.toHtml()+"\n");
//			}
		}
	}
	
	static ResultBean hasPaging(LinkBean bean,String attribute,String value) throws Exception{
		boolean b = false;
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(bean.getLink());
		
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter(attribute, value));
		if(list != null  && list.size() > 0){
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(SelectTag.class));
			if(nodes != null && nodes.size() > 0){
				result.setBool(true);
				for(int i=0;i<nodes.size();i++){
					SelectTag select  = (SelectTag)nodes.elementAt(i);
					if(select != null){
						NodeList options = select.getChildren();
						if(options != null && options.size() > 0 ){
							LinkBean l1 = null;
							for(int j=0;j<options.size();j++){
								l1 = new LinkBean();
								OptionTag option = (OptionTag)options.elementAt(j);
								l1.setLink(URL_+option.getValue());
								result.add(l1);
								COUNT ++;
							}
						}
					}
				}
			}
			b = true;
		}
		
		return result;
	}
	
	static ResultBean hasPaging(String url,String attribute,String value) throws Exception{
		boolean b = false;
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);
		
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter(attribute, value));
		if(list != null  && list.size() > 0){
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
//			NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(SelectTag.class)); //Version1.0
			NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class)); //version2.0
			if(nodes != null && nodes.size() > 0){
				result.setBool(true);
				result.put(url, new LinkBean(url,url));
				for(int i=0;i<nodes.size();i++){
					LinkTag link = (LinkTag)nodes.elementAt(i);
					LinkBean l1 = null;
					if(link != null){
						String url_ = URL+link.getLink();//"htm/"+
						boolean isTrue = TestHttpClient.urlValidation(url_);
						if(isTrue){
							l1 = new LinkBean();
							l1.setLink(url_);//"htm/"+
							l1.setName(link.getLinkText());//link.getLinkText()
							result.put(url_, l1);
							COUNT ++;
						}else{
							url_ = URL+"htm/"+link.getLink();
							boolean isTrue2 = TestHttpClient.urlValidation(url_);
							if(isTrue2){
								l1 = new LinkBean();
								l1.setLink(url_);//"htm/"+
								l1.setName(link.getLinkText());//link.getLinkText()
								result.put(url_, l1);
								COUNT ++;
							}
						}
					}
				}
			}
		}
		LinkBean l1 = null;
		l1 = new LinkBean();
		l1.setLink(url);//"htm/"+
		l1.setName(url);//link.getLinkText()
		result.put(url, l1);
		result.setBool(true);
		return result;
	}
	
	static ResultBean hasPaging(WebsiteBean bean,String attribute,String value) throws Exception{
		boolean b = false;
		ResultBean result = null;
		Parser parser = new Parser();
		parser.setURL(bean.getUrl());
		
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter(attribute, value));
		if(list != null  && list.size() > 0){
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(SelectTag.class));
			if(nodes != null && nodes.size() > 0){
				result = new ResultBean();
				result.setBool(true);
				for(int i=0;i<nodes.size();i++){
					SelectTag select  = (SelectTag)nodes.elementAt(i);
					if(select != null){
						NodeList options = select.getChildren();
						if(options != null && options.size() > 0 ){
							LinkBean l1 = null;
							for(int j=0;j<options.size();j++){
								l1 = new LinkBean();
								OptionTag option = (OptionTag)options.elementAt(j);
								l1.setLink(URL_+option.getValue());
								l1.setName(bean.getName());
								result.add(l1);
								COUNT ++;
							}
						}
					}
				}
			}
			b = true;
		}
		
		return result;
	}
	
	static void clear(){
		if(LINKHASH.size() > 0 ){
			LINKHASH.clear();
		}
		
		if(SECONDLINKHASH.size() > 0){
			SECONDLINKHASH.clear();
		}
		
		if(PAGELINKHASH.size() > 0){
			PAGELINKHASH.clear();
		}
	}
	
	public static int[] getCount(String url) throws Exception{
		int[] count = new int[2];
		Parser parser = new Parser();
		parser.setURL(url);
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "damulu-left-top-l"));
		if(list != null && list.size() > 0){
			Div div = (Div)list.elementAt(0);
			count[0] = Integer.valueOf(div.getStringText().substring(div.getStringText().indexOf("有")+1,div.getStringText().indexOf("个")));
			count[1] = Integer.valueOf(div.getStringText().substring(div.getStringText().indexOf(",")+1,div.getStringText().indexOf("张")));
			
//			System.out.println("类别："+div.getStringText().substring(div.getStringText().indexOf("有")+1,div.getStringText().indexOf("个")));
//			System.out.println("壁纸数量："+div.getStringText().substring(div.getStringText().indexOf(",")+1,div.getStringText().indexOf("张")));
		}
		return count;
		
	}
	
	public static void getImage(Integer id) throws Exception{
		ImageDao imageDao = DAOFactory.getInstance().getImageDao();
		ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();
		try{
			if(imageDao.getCount("select count(*) from tbl_image where d_article_id = "+id) > 0){
				System.out.println("跳过");
				return;
			}
			Article article = articleDao.findById(id);
			if(article != null){
				if(article.getArticleUrl().startsWith("http://tuku.military.china.com/") 
						|| article.getArticleUrl().startsWith("http://tuku.new.china.com/")){
					if(article.getActicleXmlUrl() != null || !article.getActicleXmlUrl().equalsIgnoreCase("")){
						List<ImageBean> imgList = XMLParser.readXmlFromURL(article.getActicleXmlUrl());
						for(ImageBean bean:imgList){
							if(client.get(CacheUtils.getImgKey(bean.getHttpUrl())) == null){
								bean.setArticleId(article.getId());
								int result = imageDao.insert(bean);
								if(result == -1){
									System.out.println("图片标题为："+bean.getTitle()+",未添加到数据库");
								}else{
									bean.setId(result);
									client.add(CacheUtils.getImgKey(bean.getHttpUrl()), bean);
									imgDownload(bean);
									System.out.println((bean.getTitle() == null ? "无标题" : bean.getTitle()));
								}
							}
						}
					}
				}
			}else{
				System.out.println("未查询到ARTICLE记录");
			}
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
	
	static void imgDownload(ImageBean imgBean){
		PicFileDao dao = null;
		PicfileBean bean = null;
		dao = DAOFactory.getInstance().getPicFileDao();
		bean = new PicfileBean();
		String s_fileName = imgBean.getImgUrl().substring(imgBean.getImgUrl().lastIndexOf("/")+1,imgBean.getImgUrl().length());
		String fileName = imgBean.getHttpUrl().substring(imgBean.getHttpUrl().lastIndexOf("/")+1,imgBean.getHttpUrl().length());
		s_fileName = s_fileName.replace(".", "_s.");
		try{
			if(client.get(CacheUtils.getShowImgKey(PIC_SAVE_PATH+CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName.replace(".", "_s."))) == null){
				IOUtil.createPicFile(imgBean.getImgUrl(), PIC_SAVE_PATH+CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName.replace(".", "_s."));
			}
			
			if(client.get(CacheUtils.getBigPicFileKey(PIC_SAVE_PATH+CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName)) == null){
				IOUtil.createPicFile(imgBean.getHttpUrl(), PIC_SAVE_PATH+CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName);
			}
			bean.setArticleId(imgBean.getArticleId());
			bean.setImageId(imgBean.getId());
			bean.setTitle(imgBean.getTitle());
			bean.setSmallName(CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+s_fileName);
			bean.setName(CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName);
			bean.setUrl(PIC_SAVE_PATH);
			try{
				boolean  b = dao.insert(bean);
				if(b){
					client.add(CacheUtils.getBigPicFileKey(bean.getUrl()+bean.getName()), bean);
					client.add(CacheUtils.getSmallPicFileKey(bean.getUrl()+bean.getSmallName()), bean);
					System.out.println("成功！");
				}else{
					System.out.println("失败");
				}
			}catch(Exception e){
				System.out.println("数据库异常");
				e.printStackTrace();
			}
		}catch(IOException e){
			System.out.println("网络连接，文件IO异常");
			e.printStackTrace();
		}
}
	
	static void init(){
		try{
			List<String> articleURLlist = articleDao.findAllArticleURL(36);
			for(String key:articleURLlist){
				Object obj = client.get(key);
				if(null == obj){
					client.add(key, key);
				}
			}
			
//			List<String> list = imageDao.findImageURL(36);
//			for(String url:list){
//				if(null == client.get(url)){
//					client.add(url, url);
//				}
//			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		try{
//			init();
			
//			index();
			
			updateArticleFromSource(36);
			
//			ResultBean result = hasPaging("http://www.showimg.com/tabulation.php?mid=8230","class","pages");
//			if(result.isBool()){
//				Iterator it = result.getMap().keySet().iterator();
//				while(it.hasNext()){
//					String key = (String)it.next();
//					LinkBean bean = (LinkBean)result.getMap().get(key);
//					System.out.println("link:"+bean.getLink());
//				}
//				result.clearHash();
//			}
			
//			Object obj = client.get("http://www.showimg.com/cg/zdy20100608/big/Toy_Story_309.jpg");
//			if(null != obj){
//				System.out.println((String)obj);
//			}else{
//				System.out.println("缓存为空");
//			}
			
			
			try {
				List<WebsiteBean> list = wesiteDao.findByParentId(36);
				if (list != null && list.size() > 0) {
					for (WebsiteBean bean : list) {
						if(bean != null){
							List<Article> alist = articleDao.findByWebId(bean.getId(),"NED");//47 48 49 50 51 52 
							for(Article article:alist){
								try{
									int count = imageDao.findImage(article.getId()).size();
									if(count == 0){
										getPicUrl(article);
									}
								}catch(Exception e){
									article.setText("FNFE");
									articleDao.update(article);
									System.out.println(">> Article["+article.getId()+"],Exception:"+e.getMessage());
									continue;
								}finally{
									article.setText("FD");
									articleDao.update(article);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			imgDownload();
			
//			patchIcon();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			clear();
		}
	}
	
	private static void patchIcon() throws Exception {
		try {
			List<WebsiteBean> lists = wesiteDao.findByParentId(36);
			System.out.println("查找web菜单："+lists.size());
			if (lists != null && lists.size() > 0) {
				for (WebsiteBean bean : lists) {
					System.out.println("\t"+bean.getName()+"\t"+bean.getUrl());
					getArticleIcon(bean);
//					//更新菜单列表时间
//					wesiteDao.update(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新某网站中的文章数
	 * @throws Exception
	 */
	static void updateArticleFromSource(int parentId) throws Exception{
		try {
			List<WebsiteBean> lists = wesiteDao.findByParentId(parentId);
			System.out.println("查找web菜单："+lists.size());
			if (lists != null && lists.size() > 0) {
				for (WebsiteBean bean : lists) {
					String lastModify = HttpClientUtils.getLastModifiedByUrl(bean.getUrl());
					if(null != bean.getLastModifyTime() && !"".equals(bean.getLastModifyTime()) && bean.getLastModifyTime().equals(lastModify)){
						continue;
					}
					
					
					System.out.println("\t"+bean.getName()+"\t"+bean.getUrl());
					getSecondLink(bean);

					if(lastModify != null && !"".equals(lastModify) ){
						if(null == bean.getLastModifyTime() || "".equals(bean.getLastModifyTime()) || !bean.getLastModifyTime().equals(lastModify)){
							bean.setLastModifyTime(lastModify);
							if(wesiteDao.update(bean)){
								System.out.println(" >> 更新网站["+bean.getName()+"|"+bean.getUrl()+"]最后时间["+lastModify+"]成功!");
							}
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取首页数据
	 * @throws Exception
	 */
	static void index() throws Exception{
		Parser parser = new Parser();
		parser.setURL(URL);
		
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(LinkTag.class);
		NodeList nodes = parser.extractAllNodesThatMatch(filter);
		
		if(nodes != null && nodes.size() > 0){
			Article article = null;
			for(int i=0;i<nodes.size();i++){
				try{
				LinkTag link = (LinkTag)nodes.elementAt(i);
				if(null != link.getLink() && !link.getLink().equalsIgnoreCase("") && link.getLink().startsWith("http://www.showimg.com/tabulation.php?mid=")){
						if(null == client.get(link.getLink())){
							if(null != link.getLinkText() && !"".equalsIgnoreCase(link.getLinkText())){
								article = new Article();
								article.setWebId(36);
								article.setArticleUrl(link.getLink());
								article.setTitle(link.getLinkText());
								article.setText("NED"); // No Execute Download
								int key = articleDao.insert(article);
								if (key > 0) {
									log.debug("添加" + link.getLinkText()+ ",成功");
									COUNT++;
								} else {
									log.debug("添加" + link.getLinkText() + "失败,已存在相同标题的内容");
								}
							}
						}
//					}
					Thread.sleep(50);
				}
				}catch(Exception e){
					System.out.println(">> Index.Exception:"+e.getMessage());
					continue;
				}
			}
		}
		
	}
	
	
	static void add2Cache() throws Exception{
		
		ArticleDao dao = articleDao;
		List<Article> list = dao.findAll();
		for(Article bean:list){
			if(client.get(CacheUtils.getHTMLKey(bean.getArticleUrl())) != null){
				client.replace(CacheUtils.getHTMLKey(bean.getArticleUrl()), bean);
			}else{
				client.add(CacheUtils.getHTMLKey(bean.getArticleUrl()), bean);
			}
		}
		
		ImageDao imgDao = imageDao;
		List<ImageBean> list1 = imgDao.findAll();
		for(ImageBean bean:list1){
			if(client.get(CacheUtils.getImgKey(bean.getHttpUrl())) != null){
				client.replace(CacheUtils.getImgKey(bean.getHttpUrl()), bean);
			}else{
				client.add(CacheUtils.getImgKey(bean.getHttpUrl()), bean);
			}
		}
		
//		//图片缓存
//		List<PicfileBean> list2 = picFileDao.findAll();
//		for(PicfileBean bean:list2){
//			
//			if(client.get(CacheUtils.getBigPicFileKey(bean.getUrl()+bean.getName())) != null){
//				client.replace(CacheUtils.getBigPicFileKey(bean.getUrl()+bean.getName()), bean.getName());
//			}else{
//				client.add(CacheUtils.getBigPicFileKey(bean.getUrl()+bean.getName()), bean.getName());
//			}
//			
//			if(client.get(CacheUtils.getSmallPicFileKey(bean.getUrl()+bean.getSmallName())) != null){
//				client.replace(CacheUtils.getSmallPicFileKey(bean.getUrl()+bean.getSmallName()), bean.getSmallName());
//			}else{
//				client.add(CacheUtils.getSmallPicFileKey(bean.getUrl()+bean.getSmallName()), bean.getSmallName());
//			}
//			
//			if(client.get(CacheUtils.getPicFileKey(bean.getImageId(), bean.getArticleId())) != null){
//				client.replace(CacheUtils.getPicFileKey(bean.getImageId(), bean.getArticleId()), bean);
//			}else{
//				client.add(CacheUtils.getPicFileKey(bean.getImageId(), bean.getArticleId()), bean);
//			}
//		}
		
	}

	static void imgDownload() throws Exception {
		List<WebsiteBean> webList = wesiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : webList) {
//			List<WebsiteBean> subList = wesiteDao.findByParentId(bean.getId());
//			for(WebsiteBean website:subList){
				List<Article> list = articleDao.findByWebId(bean.getId(),"FD");
				System.out.println(">> 网站["+bean.getId()+"|"+bean.getName()+"|"+bean.getUrl()+"]\t下文章数量"+list.size());
				for (Article art : list) {
					List<ImageBean> imgList = imageDao.findImage(art.getId());
					System.out.println(">> 文章["+art.getId()+"|"+art.getTitle()+"]\t下的图片数量"+imgList.size());
//					ARTICLE_COM_URL = art.getArticleUrl();
					for (ImageBean img : imgList) {
						if(img.getLink().equals("NED")){
							if (download(img,art.getArticleUrl())) {
								img.setStatus(-1);
								img.setLink("FD");
//								ResourceQuene.getInstance().setElement(img);
								if (imageDao.update(img)) {
									System.out.println(">> 更新图片对象["+art.getTitle()+"|" + img.getId() + "]成功!");
								}
							}
						}
					}
//					ARTICLE_COM_URL = null;
//				}
			}
		}
	}
	
	static boolean download(ImageBean imgBean,String url) {
		PicfileBean bean = null;
		bean = new PicfileBean();
		if(null == imgBean.getImgUrl() || "".equalsIgnoreCase(imgBean.getImgUrl())){
			System.out.println(">> 小图片地址为空");
			return false;
		}
		String s_fileName = imgBean.getImgUrl().substring(
				imgBean.getImgUrl().lastIndexOf("/") + 1,
				imgBean.getImgUrl().length());
		if(null == imgBean.getHttpUrl() || "".equalsIgnoreCase(imgBean.getHttpUrl())){
			System.out.println(">> 大图片地址为空");
			return false;
		}
		String fileName = imgBean.getHttpUrl().substring(
				imgBean.getHttpUrl().lastIndexOf("/") + 1,
				imgBean.getHttpUrl().length());
		s_fileName = s_fileName.replace(".", "_s.");
		String length = "0";
		String bigUrl = null;
		try {
			byte[] big = null;
			bigUrl = imgBean.getHttpUrl().replace("[", URLEncoder.encode("[", "utf-8")).replace("]", URLEncoder.encode("]", "utf-8"));
//			.replace(" (", URLEncoder.encode(" (", "utf-8"))
//										 .replace(" )", URLEncoder.encode(" )", "utf-8"));
			System.out.println(" >> "+bigUrl);
			big = HttpClientUtils.getResponseBodyAsByte(imgBean.getCommentshowurl(), null, bigUrl);
			if(null == big)
				return false;
			length = String.valueOf(big.length);
			if(length.equalsIgnoreCase("20261")){
				System.out.println(">> 大图为防盗链图，返回");
				return false;
			}

			if(s_fileName.toLowerCase().endsWith(".jpg") || s_fileName.toLowerCase().endsWith(".gif") 
					|| s_fileName.toLowerCase().endsWith(".png") || s_fileName.toLowerCase().endsWith(".jpeg")){
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
			}else{
				System.out.println(">> 判断小图片类别失败，图片类型不受支持["+fileName+"]");
				return false;
			}
			if(fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".gif") 
					|| fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpeg")){
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
			}else{
				System.out.println(">> 判断大图片类别失败，图片类型不受支持["+fileName+"]");
				return false;
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
					boolean b = picFileDao.insert(bean);
					if (b) {
						client.add(CacheUtils.getBigPicFileKey(bean.getUrl()
								+ bean.getName()), bean);
						client.add(CacheUtils.getSmallPicFileKey(bean.getUrl()
								+ bean.getSmallName()), bean);
					} else {
						System.out.println(">> 添加图片数据失败");
						return false;
					}
				}else{
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
	
}
