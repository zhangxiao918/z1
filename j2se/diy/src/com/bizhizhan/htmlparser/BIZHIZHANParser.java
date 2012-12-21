package com.bizhizhan.htmlparser;

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
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.chinamilitary.util.CacheUtils;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.common.Constants;

public class BIZHIZHANParser {
	
	static Logger logger =  LoggerFactory.getLogger(BIZHIZHANParser.class);
	static Integer D_PARENT_ID = 600;

	static String PIC_SAVE_PATH = Constants.FILE_SERVER;
	
	final static String FILE_SERVER = Constants.FILE_SERVER;
	
	static String URL = "http://www.bizhizhan.com";

	static String IMAGE_URL = "http://image6.tuku.cn/";
	
	static String ARTICLE_COM_URL = "";

	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	static List<Article> ARTICLELIST = new ArrayList<Article>();

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	static MemcacheClient client = MemcacheClient.getInstance();

	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();
	
	static String[] CATALOG_URL = {
		"http://www.bizhizhan.com/fzlsjbz/","http://www.bizhizhan.com/katongshoujibizhi/","http://www.bizhizhan.com/youxidongman/",
		"http://www.bizhizhan.com/fengjingshoujibizhi/","http://www.bizhizhan.com/jierishoujibizhi/","http://www.bizhizhan.com/renwenjijiabizhi/",
		"http://www.bizhizhan.com/yingshimingxingbizhi/","http://www.bizhizhan.com/tiyubizhi/","http://www.bizhizhan.com/qicheshoujibizhi/",
		"http://www.bizhizhan.com/junshikeji/","http://www.bizhizhan.com/dongwubizhi/","http://www.bizhizhan.com/dianyinghaibao/",
		"http://www.bizhizhan.com/shoujiyuelibizhi/"
	};

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
						new HasAttributeFilter("class", "nav")); // id

		if (null != list && list.size() > 0) {

			logger.debug("" + list.size());

			Div div = (Div) list.elementAt(0);
			Parser p2 = new Parser();
			p2.setInputHTML(div.toHtml());
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p2.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean tmp = null;
				for (int i = 0; i < linkList.size(); i++) {
					LinkTag link = (LinkTag) linkList.elementAt(i);
					logger.debug(link.getLinkText() + "|" + URL
							+ link.getLink() + "\n");
					tmp = new WebsiteBean();
					tmp.setName(link.getLinkText());
					tmp.setUrl(URL + link.getLink());
					tmp.setParentId(600);
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
	static ResultBean hasPaging(String url) throws Exception {
		boolean b = false;
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);

		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(SelectTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("name", "sldd"));
		LinkBean oplink = null;
		if (list != null && list.size() > 0) {
//			logger.debug(" >> list.toHtml:"+list.toHtml());
			SelectTag select = (SelectTag)list.elementAt(0);
			OptionTag[] options = select.getOptionTags();
			for(OptionTag option:options){
				logger.debug(" >> "+option.getOptionText()+"|"+url+option.getValue());
				oplink = new LinkBean();
				String url2 = url + option.getValue();
				oplink.setLink(url + option.getValue());
				oplink.setName(option.getOptionText());
				result.put(url2, oplink);
			}
			/**
			Parser p2 = new Parser();
			p2.setInputHTML(list.elementAt(0).toHtml());

			NodeFilter optionFilter = new NodeClassFilter(OptionTag.class);
			NodeList optionList = p2.extractAllNodesThatMatch(optionFilter);
			if (optionList != null && optionList.size() > 1) {
				result.setBool(true);
				for (int j = 0; j < optionList.size(); j++) {
					OptionTag option = (OptionTag) optionList.elementAt(j);
					oplink = new LinkBean();
					String url2 = url + option.getValue();
					oplink.setLink(url + option.getValue());
					result.put(url2, oplink);
				}
			}
			b = true;
			result.setBool(b);
			**/
		}
//		oplink = new LinkBean();
//		oplink.setLink(url);
//		oplink.setTitle("Title");
//		result.put(url, oplink);
		result.setBool(true);
		return result;
	}

	public static void getArtcile(String url, int webId) throws Exception {
		Parser p1 = new Parser();
		p1.setURL(url);

		Parser p2 = new Parser();
		p2.setURL(url);

		NodeFilter filter2 = new NodeClassFilter(CompositeTag.class);
		NodeList list2 = p2.extractAllNodesThatMatch(filter2)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "cont_list"));
		;

		if (null != list2) {
			Parser p3 = new Parser();
			p3.setInputHTML(list2.toHtml());

			NodeFilter filter3 = new NodeClassFilter(LinkTag.class);
			NodeList list3 = p3.extractAllNodesThatMatch(filter3);
			Article article = null;
			for (int i = 0; i < list3.size(); i++) {
				LinkTag link = (LinkTag) list3.elementAt(i);
				NodeList tmp = link.getChildren();
				if (tmp != null && tmp.size() > 0) {
					if (tmp.elementAt(0) instanceof ImageTag) {
						article = new Article();
						ImageTag imgTag = (ImageTag) tmp.elementAt(0);
						logger.info(imgTag.getAttribute("alt") + "|["
								+ url + "]");
						if (imgTag.getImageURL().startsWith("http://")) {
							article.setActicleXmlUrl(imgTag.getImageURL());
						} else {
							article
									.setActicleXmlUrl(URL
											+ imgTag.getImageURL());
						}
						if (null != imgTag.getAttribute("alt")) {
							article.setTitle(imgTag.getAttribute("alt"));
						}

						String url2 = URL + link.getLink();
						logger.info(link.getLinkText() + "|" + url2);
						if (null == client.get(url2)) {
							article.setWebId(webId);
							article.setArticleUrl(url2);
							article.setText("NED"); // No Execute Download
							int key = articleDao.insert(article);
							if (key > 0) {
								logger.info("添加" + link.getLinkText()
										+ ",成功");
								article.setId(key);
								try{
									if(getImage(article)){
										article.setText("FD");
										if(!articleDao.update(article)){
											logger.error(">> 更新文章["+article.getTitle()+"]失败");
										}
									}
								}catch(Exception e){
									logger.error("更新图片出现异常，文章为["+article.getTitle()+"]");
								}
							} else {
								logger.debug("添加" + link.getLinkText()
										+ "失败,已存在相同标题的内容");
							}
						} else {
							System.out
									.println(">> 缓存中存在该图片[" + url2 + "]地址 <<");
						}
					}
				}
			}

		}
		// }

	}

	public static void getArtcileIconByURL(String url, int webId) throws Exception {
		Parser p1 = new Parser();
		p1.setURL(url);

		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = p1.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "list"));

		if (null != list && list.size() > 0) {
			Parser p2 = new Parser();
			p2.setInputHTML(list.toHtml());

			NodeFilter filter2 = new NodeClassFilter(CompositeTag.class);
			NodeList list2 = p2.extractAllNodesThatMatch(filter2)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "cont_list"));
			;

			if (null != list2) {
				Parser p3 = new Parser();
				p3.setInputHTML(list2.toHtml());

				NodeFilter filter3 = new NodeClassFilter(LinkTag.class);
				NodeList list3 = p3.extractAllNodesThatMatch(filter3);
				Article article = null;
				for (int i = 0; i < list3.size(); i++) {
					LinkTag link = (LinkTag) list3.elementAt(i);
					NodeList tmp = link.getChildren();
					if (tmp != null && tmp.size() > 0) {
						if(tmp.elementAt(0) instanceof ImageTag){
							article = new Article();
							ImageTag imgTag = (ImageTag) tmp.elementAt(0);
							logger.debug(imgTag.getAttribute("alt")
									+ "|[" + url + "]");
							if(imgTag.getImageURL().startsWith("http://")){
								article.setActicleXmlUrl(imgTag.getImageURL());
							}else{
								article.setActicleXmlUrl(URL+imgTag.getImageURL());
							}
							if(null != imgTag.getAttribute("alt")){
								article.setTitle(imgTag.getAttribute("alt"));
							}
							
							String url2 = URL + link.getLink();
							article.setWebId(webId);
							article.setArticleUrl(url2);
							article.setText("NED"); // No Execute Download
							Article atmp = articleDao.findByUrl(article.getArticleUrl(),webId);
							if(null != atmp){
								if(atmp.getArticleUrl().equals(article.getArticleUrl()) && atmp.getTitle().equals(article.getTitle())){
									atmp.setActicleXmlUrl(article.getActicleXmlUrl());
									if(articleDao.update(atmp)){
										logger.debug(" >> 更新"+atmp.getId()+".ActicleXmlUrl:"+atmp.getActicleXmlUrl()+"成功!");
									}
								}
							}
						}
					}
				}

			}
		}

	}
	
	
	static boolean getImage(Article article) throws Exception{
		boolean b = false;
		List<String> linkList = new ArrayList<String>();
		ResultBean result = getImagePage(article.getArticleUrl());
		//TODO 先从页面中判断是否有分页
		if(result.isBool()){
			logger.debug("分页数量:"+result.getMap().size());
			
			//TODO 有分页，则执行获取分页地址，然后记录图片地址
			Iterator it = result.getMap().keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				linkList.add(key);
			}
		}else{
			//TODO 无分页
			linkList.add(article.getArticleUrl());
		}
		Parser p1 = null;
		for(String link:linkList){
			p1 = new Parser();
			p1.setURL(link);
			
			NodeFilter filter = new NodeClassFilter(ImageTag.class);
			NodeList list3 = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("id","bigimg"));
			if(null != list3 && list3.size() > 0){
				ImageTag img = (ImageTag)list3.elementAt(0);
				logger.debug(img.getAttribute("alt")+"|"+img.getImageURL());
				ImageBean imageBean = null;
				imageBean = new ImageBean();
				imageBean.setArticleId(article.getId());
    			imageBean.setImgUrl(img.getImageURL());
    			imageBean.setTitle(img.getAttribute("alt"));
    			imageBean.setHttpUrl(img.getImageURL());
				imageBean.setLink("NED");
				imageBean.setCommentshowurl(link);
				//TODO 需要增加缓存判断
				if(null == client.get(imageBean.getHttpUrl())){
					int length = HttpClientUtils.getBodyLength(imageBean.getHttpUrl(),
							"User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
					logger.debug(">> Content-Length:" + length);
					if (-1 != length) {
						imageBean.setFileSize(Long.valueOf(length));
						imageBean.setStatus(1);
					}
    				int size = imageDao.insert(imageBean);
    				if(size > 0){
    					client.add(imageBean.getHttpUrl(), imageBean.getHttpUrl());
    					logger.debug("添加图片记录:["+imageBean.getHttpUrl()+"]"+imageBean.getArticleId()+"]\t|" +article.getTitle()+"\t:"+"成功!");
    				}else{
						ImageBean tmpImg = imageDao.findByHttpUrl(imageBean.getHttpUrl());
						if(null == tmpImg.getCommentshowurl()){
							if(null != tmpImg){
								tmpImg.setCommentshowurl(link);
								if(!imageDao.update(tmpImg)){
									logger.debug("更新图片记录:["+tmpImg.getTitle() + "|"+tmpImg.getArticleId()+"]\t|" + link+"\t成功");
									client.add(link, link);
								}
							}
						}
					}
				}else{
					ImageBean tmpImg = imageDao.findByHttpUrl(imageBean.getHttpUrl());
					if(null != tmpImg){
						if(null == tmpImg.getCommentshowurl()){
							tmpImg.setCommentshowurl(link);
							if(!imageDao.update(tmpImg)){
								logger.debug("更新图片记录:["+tmpImg.getTitle() + "|"+tmpImg.getArticleId()+"]\t|" + link+"\t成功");
								client.add(link, link);
							}
						}
					}
				}
				b = true;
			}
		}
		return b;
	}
	
	static ResultBean getImagePage(String url) throws Exception{
		LinkBean oplink = null;
		boolean b = false;
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);

		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "content-page"));
		if (list != null && list.size() > 0) {
			Parser p2 = new Parser();
			p2.setInputHTML(list.elementAt(0).toHtml());

			NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p2.extractAllNodesThatMatch(filter2);
			if (linkList != null && linkList.size() > 1) {
				String tmp = null;
				for (int j = 0; j < linkList.size(); j++) {
					LinkTag link = (LinkTag) linkList.elementAt(j);
					oplink = new LinkBean();
					tmp = url.substring(0,url.lastIndexOf("/")+1);
					tmp = tmp+link.getLink();
					oplink.setLink(tmp);
					result.put(tmp, oplink);
				}
			}
		}else{
			oplink = new LinkBean();
			oplink.setLink(url);
			result.put(url, oplink);
		}
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

	static void init(){
		try{
			List<String> articleURLlist = articleDao.findAllArticleURL(600);
			for(String key:articleURLlist){
				Object obj = client.get(key);
				if(null == obj){
					client.add(key, key);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
//			index();
			update();
//			patchImageLength();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对该站点中的图片数据长度进行处理
	 * @throws Exception
	 */
	static void patchImageLength() throws Exception{
		int count = 0;
		List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : list) {
			List<Article> alist = articleDao.findByWebId(bean.getId());
			for(Article a:alist){
				List<ImageBean> ilist = imageDao.findImage(a.getId());
				for(ImageBean i:ilist){
					count++;
					if(i.getFileSize() == 0){
						int length = HttpClientUtils.getBodyLength(i.getHttpUrl(),
								"Cookie","virtualwall=vsid=3101aaa576d049e45dbb672ab3b307cc; CNZZDATA1235385=cnzz_eid=17084099-1352625629-&ntime=1352645689&cnzz_a=9&retime=1352646834400&sin=none&ltime=1352646834400&rtime=0");
						logger.info(">> Content-Length:" + length);
						if (-1 != length) {
							i.setFileSize(Long.valueOf(length));
							i.setStatus(1);
							logger.info("\t更新图片大小:"+imageDao.update(i));
						}
					}else{
						logger.info("\t图片"+i.getId()+"大小大于0,为:"+i.getFileSize());
					}
				}
			}
		}
		logger.info("图片总数为:"+count);
	}
	static void patchTitle(int artid) throws Exception{
		Parser p1 = null;
		try{
			Article art = articleDao.findById(artid);
				if(null == art.getTitle() || "".equals(art.getTitle())){
					p1 = new Parser();
					p1.setURL(art.getArticleUrl());
					NodeFilter filter = new NodeClassFilter(TitleTag.class);
					NodeList nlist = p1.extractAllNodesThatMatch(filter);
					if(null != nlist && nlist.size() > 0){
						TitleTag title = (TitleTag)nlist.elementAt(0);
						String text = title.getTitle();
						//大维的北海道lomo非主流手机壁纸240x320,240x400,320x480,360x640-手机壁纸站
						int start = text.lastIndexOf("-");
						String t = text.substring(0,start);
						art.setTitle(t);
						if(!articleDao.update(art)){
							logger.debug(" >> update article title failure!");
						}
					}
					if(null != p1 )
						p1 = null;
				}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1 )
				p1 = null;
		}
		
	}
	
	
	static void patchTitle() throws Exception{
		List<WebsiteBean> list = webSiteDao.findByParentId(600);
		Parser p1 = null;
		try{
			for (WebsiteBean bean : list) {
				List<Article> alist = articleDao.findByWebId(bean.getId());
				for(Article art:alist){
					if(null == art.getTitle() || "".equals(art.getTitle())){
						p1 = new Parser();
						p1.setURL(art.getArticleUrl());
						NodeFilter filter = new NodeClassFilter(TitleTag.class);
						NodeList nlist = p1.extractAllNodesThatMatch(filter);
						if(null != nlist && nlist.size() > 0){
							TitleTag title = (TitleTag)nlist.elementAt(0);
							String text = title.getTitle();
							//大维的北海道lomo非主流手机壁纸240x320,240x400,320x480,360x640-手机壁纸站
							int start = text.lastIndexOf("-");
							logger.debug(" >> article.id["+art.getId()+"],title:"+text.substring(0,start));
							String t = text.substring(0,start);
							art.setTitle(t);
							if(!articleDao.update(art)){
								logger.debug(" >> update article title failure!");
							}
						}
						if(null != p1 )
							p1 = null;
					}
				}
			}
		}catch(Exception e){
			
		}finally{
			if(null != list){
				list.clear();
				list = null;
			}
			if(null != p1 )
				p1 = null;
		}
		
	}
	
	static void index() throws Exception {
		Parser p1 = new Parser();
		p1.setURL("http://www.bizhizhan.com/");

		NodeFilter filter = new NodeClassFilter(LinkTag.class);
		NodeList list = p1.extractAllNodesThatMatch(filter);
		if (null != list && list.size() > 0) {
			Article article = null;
			for (int i = 0; i < list.size(); i++) {
				String wurl = null;
				LinkTag link = (LinkTag) list.elementAt(i);
				if (link != null && link.getChildren().size() > 0) {
					if (link.getChildren().elementAt(0) instanceof ImageTag) {
						if (!link.getLink().startsWith("http://")) {
							wurl = URL + link.getLink();
						} else {
							wurl = link.getLink();
						}
						WebsiteBean tmp = webSiteDao.findByUrl(wurl);
						if (null == tmp) {
							return;
						}

						if (!wurl.startsWith(tmp.getUrl())
								|| !wurl.endsWith(".html")) {
							logger.debug("[" + wurl + "]不是以" + tmp.getUrl()
									+ "开始的! 和以 .html结束的");
							continue;
						}

						ImageTag imgTag = (ImageTag) link.getChildren()
								.elementAt(0);
						article = new Article();
						article.setWebId(601);
						if (wurl.startsWith("http://")) {
							article.setArticleUrl(wurl);
						} else {
							article.setArticleUrl(URL + wurl);
						}
						article.setText("NED"); // No Execute Download
						if (imgTag.getImageURL().startsWith("http://")) {
							article.setActicleXmlUrl(imgTag.getImageURL());
						} else {
							article
									.setActicleXmlUrl(URL
											+ imgTag.getImageURL());
						}
						article.setTitle(imgTag.getAttribute("alt"));
						int key = articleDao.insert(article);
						if (key > 0) {
							logger.debug("添加" + link.getLinkText() + ",成功");
							article.setId(key);
							try {
								if (getImage(article)) {
									article.setText("FD");
									if (!articleDao.update(article)) {
										logger.error(">> 更新文章["
												+ article.getTitle() + "]失败");
									}
								}
							} catch (Exception e) {
								logger.error("更新图片出现异常，文章为["
										+ article.getTitle() + "]");
							}
						} else {
							logger.debug("添加" + link.getLinkText()
									+ "失败,已存在相同标题的内容");
						}
					}
				}
			}
		}
	}

	/**
	 * 更新网站记录
	 * 
	 * @throws Exception
	 */
	public static void update() throws Exception{
		List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean bean : list) {
			String lastModify = HttpClientUtils.getLastModifiedByUrl(bean.getUrl());
			if(null != bean.getLastModifyTime() && !"".equals(bean.getLastModifyTime()) && bean.getLastModifyTime().equals(lastModify)){
				logger.info(" >> NOP "+bean.getName());
				continue;
			}
			ResultBean result = hasPaging(bean.getUrl());
			if (result.isBool()) {
				
				Iterator it = result.getMap().keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					logger.info("key:" + key);
					getArtcile(key, bean.getId());
				}
				
				if(lastModify != null && !"".equals(lastModify) ){
					if(null == bean.getLastModifyTime() || "".equals(bean.getLastModifyTime()) || !bean.getLastModifyTime().equals(lastModify)){
						bean.setLastModifyTime(lastModify);
						if(webSiteDao.update(bean)){
							logger.debug(" >> 更新网站["+bean.getName()+"|"+bean.getUrl()+"]最后时间["+lastModify+"]成功!");
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取图片
	 * @throws Exception
	 */
	static void image() throws Exception{
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for(WebsiteBean bean:webList){
			List<Article> artlist = articleDao.findByWebId(bean.getId(),"NED");
			logger.debug("网站["+bean.getId()+"|"+bean.getName()+"|"+bean.getUrl()+"]下的文章数"+artlist.size());
			for(Article art:artlist){
				List<ImageBean> imgList = imageDao.findImage(art.getId());
				if(imgList.size() == 0){
					try{
						if(getImage(art)){
							art.setText("FD");
							if(!articleDao.update(art)){
								logger.error(">> 更新文章["+art.getTitle()+"]失败");
							}
						}
					}catch(Exception e){
						logger.error("更新图片出现异常，文章为["+art.getTitle()+"]");
					}
				}
			}
		}
	}
	
	static void downloadImg() throws Exception{
		List<WebsiteBean> webList = webSiteDao.findByParentId(600);
		for(WebsiteBean bean:webList){
			List<Article> artlist = articleDao.findByWebId(bean.getId(),"FD");
			logger.debug("网站["+bean.getId()+"|"+bean.getName()+"|"+bean.getUrl()+"]下的文章数"+artlist.size());
			for(Article art:artlist){
				List<ImageBean> imgList = imageDao.findImage(art.getId());
				logger.debug(">> 文章["+art.getId()+"|"+art.getTitle()+"]\t下的图片数量"+imgList.size());
				ARTICLE_COM_URL = art.getArticleUrl();
				for (ImageBean img : imgList) {
					if((img.getLink().equalsIgnoreCase("NED")) || (img.getStatus() == 3)){
						if (download(img,art.getArticleUrl())) {
							img.setStatus(1);
							img.setLink("FD");
							if (imageDao.update(img)) {
								logger.debug(">> 更新图片对象["+art.getTitle()+"|" + img.getId() + "]成功!");
							}
						}
					}
				}
				ARTICLE_COM_URL = null;
			}
		}
	}
	
	private static boolean download(ImageBean imgBean, String articleUrl) {
		PicfileBean bean = null;
		bean = new PicfileBean();
		String s_fileName = imgBean.getImgUrl().substring(
				imgBean.getImgUrl().lastIndexOf("/") + 1,
				imgBean.getImgUrl().length());
		String fileName = imgBean.getHttpUrl().substring(
				imgBean.getHttpUrl().lastIndexOf("/") + 1,
				imgBean.getHttpUrl().length());
		s_fileName = s_fileName.replace(".", "_s.");
		String length = "0";
		try {
			if(null != ARTICLE_COM_URL){
			byte[] big = null;
			//不下载小图
			logger.debug("引用的地址："+imgBean.getCommentshowurl());
			if(null == imgBean.getCommentshowurl())
				return false;
			big = HttpClientUtils.getResponseBodyAsByte(imgBean.getCommentshowurl(), "rtime=9; ltime=1307670496472; cnzz_eid=5808015-1282816593-http%3A//www.tuku.cn/; virtualwall=vsid=76abb5c68280e4c3f99efa93145827a7", imgBean.getHttpUrl());
//			big = HttpClientUtils.getResponseBodyAsByte(null, "rtime=4; ltime=1283479552367; cnzz_eid=5808015-1282816593-http%3A//www.tuku.cn/; virtualwall=vsid=0c8cafa6001de309645c11edffa3aa43; cnzz_a1235385=1; sin1235385=", imgBean.getHttpUrl());
//			big = HttpClientUtils.getResponseBodyAsByte(imgBean.getHttpUrl());
			if(null == big)
				return false;
			length = String.valueOf(big.length);
			if(length.equalsIgnoreCase("20261")){
				return false;
			}
			if (client.get(CacheUtils.getBigPicFileKey(PIC_SAVE_PATH
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator + fileName)) == null) {
				IOUtil.createFile(big, PIC_SAVE_PATH
						+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
						+ imgBean.getArticleId() + File.separator + fileName);
			}
			bean.setArticleId(imgBean.getArticleId());
			bean.setImageId(imgBean.getId());
			bean.setTitle(imgBean.getTitle());
			bean.setSmallName(imgBean.getImgUrl());
			bean.setName(File.separator+PIC_SAVE_PATH
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator + fileName);
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
				}
			} catch (Exception e) {
				logger.debug("数据库异常");
				e.printStackTrace();
				return false;
			}
			}
		} catch (IOException e) {
			logger.debug("网络连接，文件IO异常");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	static void movefile() throws Exception{
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		PicfileBean bean = null;
		for(WebsiteBean website:webList){
			logger.debug(website.getId()+"|"+website.getName()+"|"+website.getUrl());
			List<Article> artList = articleDao.findByWebId(website.getId(), "FD");
			logger.debug("文章数量:"+artList.size());
			for(Article article:artList){
				List<ImageBean> list = imageDao.findImage(article.getId());
				for(ImageBean img:list){
					bean = picFiledao.findByImgIdAndArticleId(img.getId(), article.getId());
					if(null != bean){
						if(moveFile(bean)){
							logger.debug(bean.getId()+"|"+bean.getArticleId()+"|"+bean.getImageId());
							logger.debug("after move file name:"+bean.getName());
							logger.debug("after move file smallName:"+bean.getSmallName());
							logger.debug("-------------------------------------------------------------");
						}
					}
				}
			}
		}
	}
	
	static boolean moveFile(PicfileBean bean) {
		boolean isBig = true;
		boolean isSmall = true;
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
//		if(FileUtils.copyFile(source, target)){
//			logger.debug(">> 大图成功!!!");
//			if(FileUtils.deleteFile(source)){
//				logger.debug(">> 删除源大图["+source+"]成功");
//			}
//			bean.setName(fileName);
//			isBig = true;
//		}
		bean.setName(fileName);
		
//		if(FileUtils.copyFile(smgSource, smgTarget)){
//			logger.debug(">> 小图成功!!!");
//			if(FileUtils.deleteFile(smgSource)){
//				logger.debug(">> 删除源小图["+smgSource+"]成功");
//			}
//			isSmall = true;
//		}
//		bean.setSmallName(smallFileName);
		if(isBig){
			if(isBig || isSmall){
				try{
					if(picFiledao.update(bean)){
						logger.debug(">> 更新图片文件["+bean.getId()+"]记录成功!");
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return isBig;
	}
	
	/**
	 * 验证地址是否为可以请求的地址
	 * @param url
	 * @return
	 */
	static boolean validationURL(String url){
		boolean b = false;
		for(String tmp:CATALOG_URL){
			if(url.startsWith(tmp) && !url.equalsIgnoreCase(tmp)){
				b = true;
				break;
			}
		}
		return b;
	}
}
