package com.chinamilitary.htmlparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
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
import com.chinamilitary.util.CacheUtils;
import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.common.Constants;
import com.message.RequestRecordQuene;
import com.utils.FileUtils;

public class QWPPHtmlParser {

	public static final String _URL = "http://www.qwpp.net";
	
	static String PIC_SAVE_PATH = Constants.FILE_SERVER;
	
	public static String[] URL_PREX = new String[]{
		"http://www.qwpp.net/lomo/",
		"http://www.qwpp.net/man/",
		"http://www.qwpp.net/girl/",
		"http://www.qwpp.net/love/",
		"http://www.qwpp.net/mv/",
		"http://www.qwpp.net/chahua/",
		"http://www.qwpp.net/fengjing/",
		"http://www.qwpp.net/star/",
		"http://www.qwpp.net/gif/",
		"http://www.qwpp.net/kgj/",
		"http://www.qwpp.net/dzh/",
		"http://www.qwpp.net/fzl/",
		"http://www.qwpp.net/240x400/",
		"http://www.qwpp.net/320x240/",
		"http://www.qwpp.net/sc/"
	};
	
	static int D_PARENT_ID = 148;
	
	final static String FILE_SERVER = Constants.FILE_SERVER;
	
	public static final String SAVE_DIR = Constants.FILE_SERVER;
	
	private static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();
	
	static int COUNT = 0;
	
	private static HashMap<String,LinkBean> LINKHASH = new HashMap<String,LinkBean>();
	
	private static HashMap<String,LinkBean> SECONDLINKHASH = new HashMap<String,LinkBean>();

	private static HashMap<String,LinkBean> PICLINKHASH = new HashMap<String,LinkBean>();
	
	private static ImageDao imageDao = DAOFactory.getInstance().getImageDao();
	
	private static ArticleDao  articleDao = DAOFactory.getInstance().getArticleDao();
	
	private static WebSiteDao wesiteDao = DAOFactory.getInstance().getWebSiteDao();
	
	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();
	
	private static int _COUNT = 0;
	
	private static MemcacheClient client = MemcacheClient.getInstance();
	
	/**
	 * 目录
	 * 
	 * @throws Exception
	 */
	static void catelogy(WebsiteBean bean) throws Exception {
		Parser parser = new Parser();
		parser.setURL(bean.getUrl());
		parser.setEncoding("GB2312");

		NodeFilter fileter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "nav")); //id blk_lmdh_01 "class", "nav"
		if (list != null && list.size() > 0) {
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			p1.setEncoding("GB2312");
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			NodeList linkList = p1.extractAllNodesThatMatch(linkFilter);
			if (linkList != null && linkList.size() > 0) {
				WebsiteBean sub = null;
				for (int i = 0; i < linkList.size(); i++) {
					sub = new WebsiteBean();
					LinkTag link = (LinkTag) linkList.elementAt(i);
					String catelogyUrl = _URL+link.getLink();
					sub.setName(link.getLinkText());
					sub.setUrl(catelogyUrl);
					sub.setParentId(bean.getId());
					if(wesiteDao.insert(sub)){
						System.out.println(catelogyUrl);
						System.out.println("名称:"+link.getLinkText());
					}
//					hasPaging(catelogyUrl,"class","show_page");
				}
			}
		}
	}

	/**
	 * 获取分类分页数据
	 * @param url
	 * @param attribute
	 * @param value
	 * @return
	 * @throws Exception
	 */
	static ResultBean hasPaging(String url,String attribute,String value) throws Exception{
		boolean b = false;
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding("GB2312");
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter(attribute, value));
		if(list != null  && list.size() > 0){
			Parser p1 = new Parser();
			p1.setInputHTML(list.toHtml());
			p1.setEncoding("GB2312");
			NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
			if(nodes != null && nodes.size() > 0){
				result.setBool(true);
				for(int i=0;i<nodes.size();i++){
					LinkTag select  = (LinkTag)nodes.elementAt(i);
					String url_ = url+select.getLink();
					if(null == client.get(url_)){
						if(!HttpClientUtils.validationURL(url_)){
							continue;
						}
						LinkBean l1 = null;
						l1 = new LinkBean();
						l1.setLink(url_);
						l1.setName(select.getLinkText());
						result.add(l1);
					}
				}
			}
			b = true;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 */
	static void articelPerPage(String url,String attribute,String value,Integer webId) {
		try{
			Parser parser = new Parser();
			parser.setURL(url);
			parser.setEncoding("GB2312");
			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(filter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter(attribute, value));
			if(list != null && list.size() > 0){
				String content = list.toHtml();
				if(content.contains("<ul>") && content.contains("</ul>")){
					int sid = content.indexOf("<ul>");
					int eid = content.indexOf("</ul>");
					String ul = content.substring(sid, eid);
					Parser p1 = new Parser();
					p1.setInputHTML(ul);
					p1.setEncoding("GB2312");
					NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
					if(nodes != null && nodes.size() > 0){
						Article article = null;
						for(int i=0;i<nodes.size();i++){
							LinkTag select  = (LinkTag)nodes.elementAt(i);
							String url_ = _URL+select.getLink();
							if(null == client.get(url_)){
//								if(!HttpClientUtils.validationURL(url_)){
//									continue;
//								}
								article = new Article();
								article.setActicleRealUrl(url_);
								article.setArticleUrl(url_);
								article.setActicleXmlUrl(null);
								article.setTitle(select.getLinkText());
								article.setCreateTime(new Date());
								article.setWebId(webId);
								article.setText("NED");
//								RequestRecordQuene.getInstance().setElement(article);
								int articleId = articleDao.insert(article);
								if(articleId>0){
									System.out.println("articleId:"+articleId);
									client.add(url_, url_);
									_COUNT ++;
									getPicUrl(url_,"id", "piclist2",articleId);
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void getPicUrl(String articleUrl,String attribute,String value,int articleId){
		System.out.println("\t解析页面图片地址:"+articleUrl);
		try{
			Parser parser = new Parser();
			parser.setURL(articleUrl);
			parser.setEncoding("GB2312");
			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(filter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter(attribute, value));
			if(list != null && list.size() > 0){
				String content = list.toHtml();
				if(content.contains("<ul>") && content.contains("</ul>")){
					int sid = content.indexOf("<ul>");
					int eid = content.indexOf("</ul>");
					String ul = content.substring(sid, eid);
					Parser p1 = new Parser();
					p1.setInputHTML(ul);
					p1.setEncoding("GB2312");
					NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
					if(nodes != null && nodes.size() > 0){
						ImageBean imgBean = null;
						for(int i=0;i<nodes.size();i++){
							LinkTag select  = (LinkTag)nodes.elementAt(i);
							String url_ = _URL+select.getLink();
//							if(!HttpClientUtils.validationURL(url_)){
//								System.out.println(">> QWPP 连接不可用");
//								continue;
//							}
							if(null == client.get(url_)){
								imgBean = new ImageBean();
//								imgBean.setTitle(title)
								imgBean.setArticleId(articleId);
								imgBean.setHttpUrl(url_);
								imgBean.setImgUrl(url_);
								imgBean.setCreatetime(new Date());
								imgBean.setOrderId(i+1);
								imgBean.setLink("NED");
								String length = HttpClientUtils.getHttpHeaderResponse(url_, "Content-Length");
								if(null != length){
									imgBean.setFileSize(Long.valueOf(length));
									imgBean.setStatus(1);
								}
								int iid = imageDao.insert(imgBean);
								if(iid > 0){
									client.add(url_, url_);
//									download(_URL+select.getLink(),"");
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void getPicUrl(Article article,String attribute,String value){
		System.out.println("\t解析页面图片地址:"+article.getArticleUrl());
		try{
			Parser parser = new Parser();
			parser.setURL(article.getArticleUrl());
			parser.setEncoding("GB2312");
			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(filter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter(attribute, value));
			if(list != null && list.size() > 0){
				String content = list.toHtml();
				if(content.contains("<ul>") && content.contains("</ul>")){
					int sid = content.indexOf("<ul>");
					int eid = content.indexOf("</ul>");
					String ul = content.substring(sid, eid);
					Parser p1 = new Parser();
					p1.setInputHTML(ul);
					p1.setEncoding("GB2312");
					NodeList nodes = p1.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
					if(nodes != null && nodes.size() > 0){
						ImageBean imgBean = null;
						for(int i=0;i<nodes.size();i++){
							LinkTag select  = (LinkTag)nodes.elementAt(i);
							String url = _URL+select.getLink();
							if(null == client.get(url)){
//								if(!HttpClientUtils.validationURL(url)){
//									continue;
//								}
								imgBean = new ImageBean();
								imgBean.setArticleId(article.getId());
								imgBean.setHttpUrl(url);
								imgBean.setImgUrl(url);
								imgBean.setCreatetime(new Date());
								imgBean.setOrderId(i+1);
								imgBean.setTitle(article.getTitle());
								imgBean.setLink("NED");
								int iid = imageDao.insert(imgBean);
								if(iid > 0){
									System.out.println("imageId:"+iid);
									client.add(url, url);
//									download(url,article.getTitle());
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void download(String imgUrl,String title) throws Exception{
		String fileName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
		System.out.println("fileName:"+fileName);
		if(title != null && !title.equalsIgnoreCase("")){
		IOUtil.createPicFile(imgUrl, SAVE_DIR
				+ CommonUtil.getDate("") + File.separator + title+ File.separator
				+ fileName);
		}else{
			IOUtil.createPicFile(imgUrl, SAVE_DIR
					+ CommonUtil.getDate("") + File.separator
					+ fileName);
		}
		_COUNT ++ ;
		System.out.println("图片："+fileName+"\t下载成功,已下载图片总数为:"+_COUNT);
		
	}
	
	static void download(ImageBean imgBean,Article article) throws Exception{
//		PicfileBean bean = null;
		String fileName = imgBean.getImgUrl().substring(imgBean.getImgUrl().lastIndexOf("/")+1);
//		bean = new PicfileBean();
//		bean.setArticleId(article.getId());
//		bean.setImageId(imgBean.getId());
//		bean.setTitle(imgBean.getTitle() == null?article.getTitle():imgBean.getTitle()); // NO TITLE
		// webId+File.separator+
//		bean.setSmallName("NSI"); //NO SMALL ICON

		// webId+File.separator+
//		bean.setName(CommonUtil.getDate("") + File.separator
//				+ article.getTitle() + File.separator + fileName);
//		bean.setUrl(SAVE_DIR);
		try {
//			boolean b = picFileDao.insert(bean);
			if (true) {
				System.out.println("fileName:"+fileName);
				if(article.getTitle() != null && !article.getTitle().equalsIgnoreCase("")){
				IOUtil.createPicFile(imgBean.getImgUrl(), SAVE_DIR
						+ CommonUtil.getDate("") + File.separator + article.getId()+ File.separator
						+ fileName);
				}else{
					IOUtil.createPicFile(imgBean.getImgUrl(), SAVE_DIR
						+ CommonUtil.getDate("") +File.separator+article.getId()+File.separator
							+ fileName);
				}
				System.out.println("添加到tbl_pic_file成功！");
			} else {
				System.out.println("添加到tbl_pic_file失败");
			}
		} catch (Exception e) {
			System.out.println("数据库异常");
			e.printStackTrace();
		}
		
		_COUNT ++ ;
		System.out.println("图片："+fileName+"\t下载成功,已下载图片总数为:"+_COUNT);
		
	}
	
	//217 1957
	static void downloadArticleImage(){
		try{
		long start = System.currentTimeMillis();
		List<WebsiteBean> rootURL = wesiteDao.findByParentId(D_PARENT_ID);
		for(WebsiteBean bean:rootURL){
			List<Article> list  = articleDao.findShowImg(bean.getId(),"FD",1);
			for(Article art:list){
//				 List<HashMap<String,String>>  imgList = imageDao.findImageByArticle(art.getId());
//				 for(HashMap<String,String> map:imgList){
//					 String url = map.get("imgurl");
//					 download(url,art.getTitle());
//					 _COUNT ++;
//				 }
				 
				 List<ImageBean> ilist = imageDao.findImage(art.getId());
				 for(ImageBean imgBean:ilist){
					if(!HttpClientUtils.validationURL(imgBean.getImgUrl()) || !HttpClientUtils.validationURL(imgBean.getHttpUrl())){
						continue;
					}
					 download(imgBean,art);
					 _COUNT ++;
				 }
			}
		}
		long end = System.currentTimeMillis();
		
		System.out.println("下载图片耗时:"+(end-start)/1024);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	static void init2cache() {
		try{
			List<String> articleURLlist = articleDao.findAllArticleURL(D_PARENT_ID);
			long start1 = System.currentTimeMillis();
			for(String key:articleURLlist){
				Object obj = client.get(key);
				if(null == obj){
					client.add(key, key);
				}
			}
			long end1 = System.currentTimeMillis();
			
//			List<String> imageURLList = imageDao.findImageURL(D_PARENT_ID);
//			long start2 = System.currentTimeMillis();
//			for(String key:imageURLList){
//				Object obj = client.get(key);
//				if(null == obj){
//					client.add(key, key);
//				}
//			}
//			long end2 = System.currentTimeMillis();
			System.out.println("文章入缓存花费:"+(end1-start1));
//			System.out.println("图片地址入缓存花费:"+(end2-start2));
		}catch(Exception e){
			System.out.println(">> Exception:"+e.getMessage());
		}
	}
	
	public static void main(String args[]){
		try{
			
//			init2cache();
			
			update();
			
			imgDownload();
			
//			movefile();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void update() throws Exception{
		List<WebsiteBean> rootURL = wesiteDao.findByParentId(D_PARENT_ID);
		if(null != rootURL){
			//获取网站下的文章
			for(WebsiteBean bean:rootURL){
				System.out.println(">> 解析地址:["+bean.getUrl()+"]");
				
				ResultBean  result = hasPaging(bean.getUrl(),"class","show_page");
				for(LinkBean ll : result.getList()){
					//获取分页连接
					articelPerPage(ll.getLink(),"id","Channel1",bean.getId());
				}
			}
			
			//获取文章下的图片地址
			for(WebsiteBean bean:rootURL){
				List<Article> list  = articleDao.findShowImg(bean.getId(),"NED",1);
				for(Article art:list){
//						if(!HttpClientUtils.validationURL(art.getArticleUrl())){
//							System.out.println(">> QWPP Article URL["+bean.getUrl()+"] NOT OK");
//							continue;
//						}
						System.out.println("标题："+art.getTitle() + "\t\t所属类别ID:"+art.getWebId());
						getPicUrl(art, "id", "piclist2");
						art.setText("FD");
						if(articleDao.update(art)){
							System.out.println("更新文章["+art.getTitle()+"]成功");
						}
					_COUNT++;
				}
			}
		}
	}
	static void imgDownload() throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for (WebsiteBean website : webList) {
			System.out.println(website.getId() + "|" + website.getName() + "|"
					+ website.getUrl());
			List<Article> artList = articleDao.findByWebId(website.getId(),
					"FD");
			for (Article article : artList) {
				List<ImageBean> list = imageDao.findImage(article.getId());
				if (list.size() == 0) {
					article.setText("NED");
					if (articleDao.update(article)) {
						System.out.println(">> 更新图片记录数据为0的文章成功");
					}
				} else {
					for (ImageBean bean : list) {
						if (bean.getLink().equalsIgnoreCase("NED")) {
							if (download(bean)) {
								bean.setStatus(1);
								bean.setLink("FD");
								if (imageDao.update(bean)) {
									System.out.println(">> 更新图片对象["
											+ bean.getId() + "]成功");
								}
							}
						}
					}
					article.setText("FD");
					articleDao.update(article);
				}
			}
		}
	}

	static boolean download(ImageBean imgBean) {
		long start = System.currentTimeMillis();
		PicfileBean bean = null;
		String s_fileName = imgBean.getImgUrl().substring(
				imgBean.getImgUrl().lastIndexOf("/") + 1,
				imgBean.getImgUrl().length());
		String fileName = imgBean.getHttpUrl().substring(
				imgBean.getHttpUrl().lastIndexOf("/") + 1,
				imgBean.getHttpUrl().length());
		s_fileName = s_fileName.replace(".", "_s.");
		try {
			bean = new PicfileBean();
//			if (client.get(CacheUtils.getShowImgKey(PIC_SAVE_PATH + 
//					StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
//					+ imgBean.getArticleId() + File.separator
//					+ fileName.replace(".", "_s."))) == null) {
//				IOUtil.createPicFile(imgBean.getImgUrl(), PIC_SAVE_PATH + 
//						StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
//						+ imgBean.getArticleId()
//						+ File.separator + fileName.replace(".", "_s."));
//			}

			if (client.get(CacheUtils.getBigPicFileKey(PIC_SAVE_PATH + 
					StringUtils.gerDir(String.valueOf(imgBean.getArticleId())) + 
					imgBean.getArticleId() + File.separator
					+ fileName)) == null) {
				long iostart = System.currentTimeMillis();
				IOUtil.createPicFile(imgBean.getHttpUrl(), PIC_SAVE_PATH + 
						StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))+ 
						imgBean.getArticleId()
						+ File.separator + fileName);
				long ioend = System.currentTimeMillis();
				System.out.println("保存文件耗时:"+(ioend-iostart)+"ms");
			}
			bean.setArticleId(imgBean.getArticleId());
			bean.setImageId(imgBean.getId());
			if(imgBean.getTitle() == null || imgBean.getTitle().equalsIgnoreCase("")){
				bean.setTitle("无标题");
			}else{
				bean.setTitle(imgBean.getTitle());
			}
			bean.setSmallName(File.separator + StringUtils.gerDir(String.valueOf(imgBean.getArticleId())) + imgBean.getArticleId()
					+ File.separator + fileName);
			bean.setName(File.separator + StringUtils.gerDir(String.valueOf(imgBean.getArticleId())) + imgBean.getArticleId()
					+ File.separator + fileName);
			bean.setUrl(PIC_SAVE_PATH);
			try {
				long dbstart = System.currentTimeMillis();
				boolean b = picFiledao.insert(bean);
				long dbend = System.currentTimeMillis();
				System.out.println("数据入库耗时:"+(dbend-dbstart)+"ms");
				if (b) {
					client.add(CacheUtils.getBigPicFileKey(bean.getUrl()
							+ bean.getName()), bean);
					client.add(CacheUtils.getSmallPicFileKey(bean.getUrl()
							+ bean.getSmallName()), bean);
					long end = System.currentTimeMillis();
					System.out.println("耗时:"+(end-start)+"ms");
					return true;
				} else {
					long end = System.currentTimeMillis();
					System.out.println("耗时:"+(end-start)+"ms");
					return false;
				}
			} catch (Exception e) {
				long end = System.currentTimeMillis();
				System.out.println("耗时:"+(end-start)+"ms");
				System.out.println("数据库异常");
				e.printStackTrace();
				return false;
			}
		} catch (IOException e) {
			long end = System.currentTimeMillis();
			System.out.println("耗时:"+(end-start)+"ms");
			System.out.println("网络连接，文件IO异常");
			e.printStackTrace();
			return false;
		}
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
//					break;
				}
//				break;
			}
//			break;
		}
	}
	
	static boolean moveFile(PicfileBean bean) {
		String tmpUrl = "F:\\qwpp\\";
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
