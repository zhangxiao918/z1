package com.tuku.htmlparser;

import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.ResultBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;

public class TUKUThread extends Thread  {
	
	static Log log = LogFactory.getLog(TUKUThread.class);

	MemcacheClient client = MemcacheClient.getInstance();

	ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();
	
	boolean isRun = false;
	
	private int threadId = -1;
	
	private int webId;
	
	List<Article> list = null;
	
	public TUKUThread(int webId) {
//		this.threadId = threadId;
//		,int threadId
		this.webId = webId;
	}

	@Override
	public void run() {
		System.out.println(">> Start Parser TUKU Image By webId["+webId+"]");
		while(true){
			try{
				Thread.sleep(1000);
				list = articleDao.findByWebId(webId, "NED");
				if(null != list && list.size() > 0){
					for(Article article:list){
						try{
							ResultBean result = TUKUParser.hasPaging(article.getArticleUrl(), "id", "lblPageCount");
							if(result.isBool()){
								Iterator it = result.getMap().keySet().iterator();
								while(it.hasNext()){
									String key = (String)it.next();
									LinkBean link = result.getMap().get(key);
									try{
										log.debug(">> getImage from link["+link+"]");
										TUKUParser.getImage(link,article.getWebId(),article.getId());
									}catch(Exception e){
										e.printStackTrace();
										System.err.println("key:"+key);
										continue;
									}
								}
								article.setText("FD");
							}else{
								article.setText("NED");
							}
						}catch(Exception e){
							article.setText("NED");
						}finally{
							if(!articleDao.update(article)){
								System.err.println(">> 更新记录["+article.getArticleUrl()+"]失败");
							}
						}
						log.debug(">> Thread sleep for 1 second");
						Thread.sleep(1000);
					}
				}
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
			}
		}
	}
	
	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public int getWebId() {
		return webId;
	}

	public void setWebId(int webId) {
		this.webId = webId;
	}

	public List<Article> getList() {
		return list;
	}

	public void setList(List<Article> list) {
		this.list = list;
	}

	
	
}
