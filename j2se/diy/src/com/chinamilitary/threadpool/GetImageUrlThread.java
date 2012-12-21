package com.chinamilitary.threadpool;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamilitary.bean.Article;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.impl.ArticleDaoImpl;
import com.chinamilitary.htmlparser.ShowimgParser;

public class GetImageUrlThread extends Thread {

	private static Log log = LogFactory.getLog(GetImageUrlThread.class);
	private ArticleDao articleDao;
	private boolean isRunn = false;
	private int webId;
	
	public GetImageUrlThread(int webId,String name){
		this.webId = webId;
		init();
	}
	
	public void init() {
		log.debug("启动");
		try{
			articleDao = new ArticleDaoImpl();
		}catch(Exception e){
			log.error("init ERROR:"+e);
		}
	}
	
	/**
	 * 
	 */
	public synchronized void run(){
		while(true){
			try{
				if(isRunn){
					List<Article> alist = articleDao.findShowImg(webId);	
					System.out.println(webId+"\tsize:"+alist.size());
					for(Article article:alist){
						ShowimgParser.getPicUrl(article);
						Thread.sleep(50);
					}
				}else{
					this.wait();
					log.debug("等待启动");
				}
			}catch(Exception e){
				e.printStackTrace();
				log.error(e);
			}
		}
	}

	public ArticleDao getArticleDao() {
		return articleDao;
	}

	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	public boolean isRunn() {
		return isRunn;
	}

	public void setRunn(boolean isRunn) {
		this.isRunn = isRunn;
	}

	public int getWebId() {
		return webId;
	}

	public void setWebId(int webId) {
		this.webId = webId;
	}
	
	public synchronized void setRunning(boolean flag){
		isRunn = flag;
		if(flag){
//			this.notify();
			this.notifyAll();
		}
	}
}
