package com.chinamilitary;

import java.io.File;
import java.io.IOException;
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
import com.chinamilitary.threadpool.GetImageUrlThread;
import com.chinamilitary.threadpool.RequestRecordThread;
import com.chinamilitary.threadpool.ThreadPoolManager;
import com.chinamilitary.util.CacheUtils;
import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.HTMLDecoder;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.chinamilitary.xmlparser.XMLParser;
import com.message.RequestRecordQuene;

public class ShowimgParser {

	private static Log log = LogFactory.getLog(ShowimgParser.class);
	
	private static final String URL = "http://www.showimg.com/";
	
	private static final String URL_ = "http://www.showimg.com/htm/";

	private static final String PIC_SAVE_PATH = "H:\\showimg\\";
	
	private static HashMap<String,LinkBean> LINKHASH = new HashMap<String,LinkBean>();
	
	private static HashMap<String,LinkBean> SECONDLINKHASH = new HashMap<String,LinkBean>();
	
	private static HashMap<String,ResultBean> PAGELINKHASH = new HashMap<String,ResultBean>();
	
	private static int COUNT = 0;
	
	private static ThreadPoolManager manager = null;
	
	private static ImageDao imageDao = DAOFactory.getInstance().getImageDao();
	
	private static ArticleDao  articleDao = DAOFactory.getInstance().getArticleDao();
	
	private static WebSiteDao wesiteDao = DAOFactory.getInstance().getWebSiteDao();
	
	private static PicFileDao picFileDao = DAOFactory.getInstance().getPicFileDao();
	
	private static MemcacheClient client = MemcacheClient.getInstance();
	
	public ShowimgParser(){
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
						new HasAttributeFilter("class", "damulu-left2"));
		if(list != null && list.size() > 0){
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			
			NodeFilter filter1 = new NodeClassFilter(LinkTag.class);
			NodeList nodes = p1.extractAllNodesThatMatch(filter1);
			
			if(nodes != null && nodes.size() > 0){
				LinkBean linkBean = null;
				for(int i=0;i<nodes.size();i++){
					
					LinkTag link = (LinkTag)nodes.elementAt(i);
//					log.debug("名称："+link.getLinkText());
//					log.debug("链接地址："+URL+link.getLink().replace("../", ""));
					linkBean = new LinkBean();
					linkBean.setLink(URL+link.getLink().replace("../", ""));
					linkBean.setName(link.getLinkText());
//					ResultBean result = hasPagingTable(linkBean);
					ResultBean result = hasPaging(bean.getLink(), "", "");
//					if(result.isBool()){
//						for(LinkBean lbean:result.getList()){
//							getPicUrl(lbean);
//						}
//					}else{
//						getPicUrl(linkBean);
//					}
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
			for(LinkBean lbean:result.getList()){
				Parser parser = new Parser();
				parser.setURL(lbean.getLink());
				
				// 获取指定ID的DIV内容
				NodeFilter filter = new NodeClassFilter(Div.class);
				NodeList list = parser.extractAllNodesThatMatch(filter)
						.extractAllNodesThatMatch(
//								new HasAttributeFilter("class", "damulu-left2")); //version 2.0
								new HasAttributeFilter("id", "left"));
				if(list != null && list.size() > 0){
//					System.out.println("list.toHtml:"+list.toHtml());
					Parser p1 = new Parser();
					p1.setInputHTML(list.toHtml());
					
					NodeFilter filter1 = new NodeClassFilter(LinkTag.class);
					NodeList nodes = p1.extractAllNodesThatMatch(filter1);
					
					if(nodes != null && nodes.size() > 0){
						Article article = null;
						for(int i=0;i<nodes.size();i++){
							try{
								LinkTag link = (LinkTag)nodes.elementAt(i);
								if(link.getLink() != null && !link.getLink().equalsIgnoreCase("") && link.getLink().startsWith("/tabulation.php")){
									String url = URL+link.getLink().replace("/", "");
									System.out.println("title:"+link.getLinkText()+"\turl:"+url);
									NodeList tmp = link.getChildren();
									if(tmp != null && tmp.size() > 0){
										ImageTag imgTag = (ImageTag)tmp.elementAt(0);
										article = new Article();
										article.setWebId(bean.getId());
										article.setArticleUrl(url);
										article.setTitle(imgTag.getAttribute("alt"));
										article.setText("NED"); // No Execute Download
										int key = articleDao.insert(article);
										if (key > 0) {
											log.debug("title:"+imgTag.getAttribute("alt"));
											log.debug("imageURL:"+imgTag.getImageURL());
											log.debug("添加" + imgTag.getAttribute("alt")+ ",成功");
											COUNT++;
										} else {
											log.debug("添加" + imgTag.getAttribute("alt") + "失败,已存在相同标题的内容");
										}
									}
									Thread.sleep(50);
								}
							}catch(Exception e){
								log.error(e);
								continue;
							}
						}
					}
				}
				
			}
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
								article.setWebId(bean.getId());
								article.setArticleUrl(URL+link.getLink().replace("../", ""));
								article.setTitle(StringUtils.illageString(link.getLinkText()));
								article.setText("NED"); // No Execute Download
								int key = articleDao.insert(article);
								if (key > 0) {
									log.debug("添加" + link.getLinkText()+ ",成功");
									COUNT++;
								} else {
									log.debug("添加" + link.getLinkText() + "失败");
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
		LinkBean link = new LinkBean();
		link.setLink(article.getArticleUrl());
		link.setName(article.getTitle());
		ResultBean resultBean = hasPagingTable(link);
		boolean b = true;
		if(resultBean.isBool()){
			for(LinkBean bean:resultBean.getList()){
				log.debug("文档编号："+article.getId()+"\t链接名称："+article.getTitle()+"有分页内容");
				log.debug("分页内容:"+bean.getLink());
				boolean result = getPicUrl1(bean.getLink(),article.getId());
				if(result){
					continue;
				}else{
					b = false;
					break;
				}
			}
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
			log.debug("文档编号："+article.getId()+"\t链接名称："+article.getTitle()+"\t没有分页内容");
			boolean result = getPicUrl1(article.getArticleUrl(),article.getId());
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
			String content = list.toHtml();
			int start = content.indexOf("<em>");
			
//			if(start > 0 ){
//				int end = content.indexOf("</em>");
//				String count = content.substring(start+10,end-6);
//				result.setBool(true);
//				try{
//					int cout = Integer.valueOf(count);
//					int pages = cout / 15;
//					if((cout % 7) != 0)
//						pages += 1;
//					LinkBean l1 = null;
//					for(int i=1;i<pages+1;i++){
//						l1 = new LinkBean();
//						l1.setLink(URL_+url.substring(url.lastIndexOf("/")+1,url.lastIndexOf(".")-1)+i+".htm");
//						l1.setName("第"+i+"页");
//						result.add(l1);
//					}
//					System.out.println("\n");
//					b = true;
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}else{
			
			
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
//			NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(SelectTag.class)); //Version1.0
			NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class)); //version2.0
//			System.out.println("node:"+nodes.toHtml());
			if(nodes != null && nodes.size() > 0){
//				result = new ResultBean();
				result.setBool(true);
				result.put(url, new LinkBean(url,url));
				for(int i=0;i<nodes.size();i++){
					//Version 1.0
//					SelectTag select  = (SelectTag)nodes.elementAt(i);
//					if(select != null){
//						NodeList options = select.getChildren();
//						if(options != null && options.size() > 0 ){
//							LinkBean l1 = null;
//							for(int j=0;j<options.size();j++){
//								l1 = new LinkBean();
//								OptionTag option = (OptionTag)options.elementAt(j);
//								l1.setLink(URL_+option.getValue());
//								result.add(l1);
//								COUNT ++;
//							}
//						}
//					}
					
					//Version 2.0
					LinkTag link = (LinkTag)nodes.elementAt(i);
					LinkBean l1 = null;
					if(link != null){
						l1 = new LinkBean();
						l1.setLink(URL_+link.getLink());
						l1.setName(link.getLinkText());//link.getLinkText()
						result.put(l1.getLink(), l1);
						COUNT ++;
					}
				}
			}
			b = true;
		}
//		}
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
	public static void main(String args[]){
		try{
			
//			updateArticleFromSource(36);
			
			ResultBean result = hasPaging("http://www.showimg.com/tabulation.php?mid=8230","class","pages");
			if(result.isBool()){
				
				Iterator it = result.getMap().keySet().iterator();
				while(it.hasNext()){
					String key = (String)it.next();
					LinkBean bean = (LinkBean)result.getMap().get(key);
					System.out.println("link:"+bean.getLink());
					getSecondLink(bean);
//					getPicUrl(bean);
				}
				result.clearHash();
			}
			
//			getParentLink();
//			Iterator it = LINKHASH.keySet().iterator();
//			while(it.hasNext()){
//				String key = (String)it.next();
//				System.out.println("key:"+key);
//				LinkBean bean = (LinkBean)LINKHASH.get(key);
//				getSecondLink(bean);
//				getPicUrl(bean);
//			}
			
//			Iterator it1 = PAGELINKHASH.keySet().iterator();
//			while(it1.hasNext()){
//				String key = (String)it1.next();
//				ResultBean bean = (ResultBean)PAGELINKHASH.get(key);
//				for(LinkBean link:bean.getList()){
//					log.debug(key+":分页地址"+link.getLink());
//				}
//			}
			
			
//			Iterator it1 = SECONDLINKHASH.keySet().iterator();
//			int count = 0;
//			while(it1.hasNext()){
//				String key = (String)it1.next();
//				LinkBean bean = (LinkBean)SECONDLINKHASH.get(key);
//				getPicUrl(bean);
//				count ++ ;
//			}
			
//			WebsiteBean bean = wesiteDao.findById(37);
//			getSecondLink(bean);
			
			
//			ResultBean result = hasPaging("http://www.showimg.com/htm/military1.htm","id","damulu-page");
//			if(result.isBool()){
//				int i=0;
//				for(LinkBean link:result.getList()){
//					log.debug("名称："+link.getName()+"分页地址"+link.getLink());
//					if(i < 1){
//						getSecondLink(link);
//					}
//					i++;
//				}
//			}
			
//			add2Cache();
			
			//
			
			/*
			try {
				List<WebsiteBean> list = wesiteDao.findByParentId(36);
				if (list != null && list.size() > 0) {
					for (WebsiteBean bean : list) {
						if(bean != null){
							List<Article> alist = articleDao.findShowImg(bean.getId());//47 48 49 50 51 52 
							for(Article article:alist){
								getPicUrl(article);
							}
						}
					}
				}
//				COUNT = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
			
//			articleDao.fin
//			List<Article> list = articleDao.findShowImg(36,3226);
//			for(Article article:list){
//				getPicUrl(article);
//			}
//			System.out.println("COUNT:"+COUNT);
			
			//统计总数量
//			try {
//				List<WebsiteBean> lists = wesiteDao.findByParentId(36);
//				int cat = 0;
//				int size = 0;
//				if (lists != null && lists.size() > 0) {
//					for (WebsiteBean bean : lists) {
//						getSecondLink(bean);
//						int[] count = getCount(bean.getUrl());
//						cat += count[0];
//						size += count[1];
//					}
//					System.out.println("总种类："+cat);
//					System.out.println("总图片数："+size);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			clear();
		}
	}
	
	/**
	 * 更新某网站中的文章数
	 * @throws Exception
	 */
	static void updateArticleFromSource(int parentId) throws Exception{
		try {
			List<WebsiteBean> lists = wesiteDao.findByParentId(parentId);
			if (lists != null && lists.size() > 0) {
				for (WebsiteBean bean : lists) {
					getSecondLink(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
}
