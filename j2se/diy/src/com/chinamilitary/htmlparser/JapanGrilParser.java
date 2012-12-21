package com.chinamilitary.htmlparser;

import java.util.HashMap;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.ResultBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.threadpool.ThreadPoolManager;

public class JapanGrilParser {

	private static final String URL = "http://www.japanesehottie.com/hot/";
	
	private static final String URL_INDEX = "http://www.japanesehottie.com/hot/index.php";
	
	private static final String URL_THUMBNAILS ="http://www.japanesehottie.com/hot/thumbnails.php";
	
	private static final String URL_DISPLAYIMAGE = "http://www.japanesehottie.com/hot/displayimage.php";
	
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
	
	public static void test1() throws Exception{
		Parser parser = new Parser();
		parser.setURL(URL);
		NodeFilter filter1 = new NodeClassFilter(LinkTag.class);
		NodeList nodes = parser.extractAllNodesThatMatch(filter1);
		for(int i=0;i<nodes.size();i++){
			LinkTag link = (LinkTag)nodes.elementAt(i);
			//URL_INDEX  URL_THUMBNAILS URL_DISPLAYIMAGE
			if(link.getLink().startsWith(URL_INDEX) || link.getLink().startsWith(URL_THUMBNAILS) || link.getLink().startsWith(URL_DISPLAYIMAGE)){
				System.out.println("LinkText:"+link.getLinkText());
				System.out.println("Link:"+link.getLink());
			}
		}	
	}

	public static void main(String args[]){
		try{
			test1();
		}catch(Exception e){
		}
	}
}
