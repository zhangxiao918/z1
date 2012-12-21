package com.chinamilitary.threadpool;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.CacheUtils;
import com.message.RequestRecordQuene;

public class RequestRecordThread extends Thread {
	
	private static Log logger = LogFactory.getLog(RequestRecordThread.class);
	
	private int count = 0;
	
	private boolean isRunning = true;
	
	private long sleeptime = 0;
	
	private ArticleDao  articleDao = null;
	
	private ImageDao imageDao = null;
	
	private static MemcacheClient client = MemcacheClient.getInstance();
	
	public RequestRecordThread(){
		logger.debug("获取队列入库记录线程启动!!!");
		sleeptime = Integer.valueOf(1000);
		init();
	}
	
	void init(){
		articleDao = DAOFactory.getInstance().getArticleDao();
		imageDao = DAOFactory.getInstance().getImageDao();
	}
	
	public synchronized void run() {
		try {
			while(isRunning) {
				count = RequestRecordQuene.getInstance().getQuene().size();
				if(count > 0){
					execute();
				}else if( count % 400 == 0 ){
					pause();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("RecordThread异常:"+e.getMessage());
			shutdown();
		}
	}
	
	public void pause() throws Exception{
		isRunning = false;
		Thread.sleep(sleeptime);
		isRunning = true;
	}
	
	public void shutdown(){
		isRunning = false;
	}

	/**
	 * 获取队列中的入库记录
	 * @throws Exception
	 */
	public synchronized void execute() throws Exception { //Article
		Object  obj = RequestRecordQuene.getInstance().getElement();
		if(obj instanceof Article){
			System.out.println("文章");
			Article bean = (Article)obj;
			if(bean != null){
				int key = articleDao.insert(bean);
				if(key > 0){
					client.add(CacheUtils.getHTMLKey(bean.getArticleUrl()), bean);
					System.out.println("文章记录添加成功!!!");
				}else{
					System.out.println("文章记录添加失败!!!");
				}
			}
			return;
		}
		
		if(obj instanceof ImageBean){
			ImageBean bean = (ImageBean)obj;
			System.out.println("图片");
			if(bean != null){
				int key = imageDao.insert(bean);
				if(key > 0){
//					client.add(CacheUtils.getImgKey(bean.getImgUrl()), bean.getImgUrl());
					System.out.println("图片记录添加成功!!!");
				}else{
					System.out.println("图片记录添加失败!!!");
				}
			}
			return;
		}
		
		if(obj instanceof PicfileBean){
			System.out.println("图片下载资源");
			return;
		}
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public boolean isRunning() {
		return isRunning;
	}


	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}


	public long getSleeptime() {
		return sleeptime;
	}


	public void setSleeptime(long sleeptime) {
		this.sleeptime = sleeptime;
	}

	public static void main(String args[]){
		Thread th = new Thread(new RequestRecordThread(),"recordintodb");
		th.start();
	}
	
}
