package com.verycd;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.ArticleDoc;
import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;

public class RequestRecordThread extends Thread {
	
	private static Log logger = LogFactory.getLog(RequestRecordThread.class);
	
	private int count = 0;
	
	private int count2 = 0;
	
	private boolean isRunning = true;
	
	private long sleeptime = 0;
	
	/**
	 * 缓存
	 */
	static MemcacheClient client = MemcacheClient.getInstance();
	
	public RequestRecordThread(){
		System.out.println("获取队列入库记录线程启动!!!");
		sleeptime = Integer.valueOf(10000);
	}
	
	
	public synchronized void run() {
		try {
			while (isRunning) {
				count = ResourceQueneInsert.getInstance().getQuene().size();
				count2 = ResourceQueneUpdate.getInstance().getQuene().size();
				System.out.println(" >> 队列数:"+count);
				if(count > 0 || count2 > 0){
					executeInsert();
					executeUpdate();
					Thread.sleep(10);
				}else if( count % 400 == 0 || count2 % 400 == 0){
					pause();
				}
			}
		} catch (Exception e) {
			System.err.println("RecordThread.run.exception:"+e.getMessage());
			try{
				ResourceQueneInsert.getInstance().getQuene().clear();
				ResourceQueneUpdate.getInstance().getQuene().clear();
				pause();
			}catch(Exception ex){
				System.err.println(" >> pause exception:"+ex);
			}
		}
	}
	
	public void pause() throws Exception{
		isRunning = false;
		Thread.sleep(sleeptime);
		isRunning = true;
	}

	/**
	 * 获取队列中的入库记录
	 * @throws Exception
	 */
	public synchronized void executeInsert() throws Exception {
		Object obj = ResourceQueneInsert.getInstance().getElement();
		if(null != obj){
			if(obj instanceof Article){
				Article art = (Article)obj;
				try{
					System.out.println(" >> Article Insert");
					DAOFactory.getInstance().getArticleDao().insert(art);
				}catch(Exception e){
					System.err.println(" >> executeInsert.Article.Exception:"+e);
				}
			}else if(obj instanceof ImageBean){
				ImageBean img = (ImageBean)obj;
				try{
					System.out.println(" >> ImageBean Insert");
					DAOFactory.getInstance().getImageDao().insert(img);
				}catch(Exception e){
					System.err.println(" >> executeInsert.ImageBean.Exception:"+e);
				}
			}else if(obj instanceof ArticleDoc){
				if(null != obj){
					ArticleDoc doc = (ArticleDoc)obj;
					try{
							if(null != doc){
								if(null != doc.getTitle() && null != doc.getUrl()){
									int result = DAOFactory.getInstance().getArticleDocDao().insert(doc);
									if(result > 0){
										if(null != doc.getUrl()){
											String key = VeryCDParser.getKey(doc.getUrl());
											client.add(key, doc);
										}
									}
									System.out.println(" >> ArticleDoc Insert ["+result+"] ");
								}
							}
					}catch(Exception e){
						e.printStackTrace();
	//					System.err.println(" >> executeInsert.ArticleDoc.Exception:"+e);
					}
				}else{
					System.err.println(" >> executeInsert.ArticleDoc.Null");
				}
			}else if(obj instanceof PicfileBean){
				PicfileBean pic = (PicfileBean)obj;
				try{
					DAOFactory.getInstance().getPicFileDao().insert(pic);
					System.out.println(" >> PicfileBean Insert");
				}catch(Exception e){
					System.err.println(" >> executeInsert.PicfileBean.Exception:"+e);
				}
			}else if(obj instanceof String){
				String key = (String)obj;
//				System.out.println(" >> Unknow Object:"+key);
				if(null != client.get(key)){
					System.out.println(">> 从缓存中获取数据:"+client.get(key));
					if(client.remove(key)){
						System.out.println( " >> 从缓存中删除数据["+key+"]成功!" );
					}
				}
			}
		}
	}

	/**
	 * 获取队列中的入库记录
	 * @throws Exception
	 */
	public synchronized void executeUpdate() throws Exception {
		Object obj = ResourceQueneUpdate.getInstance().getElement();
		if(null != obj){
			if(obj instanceof Article){
				Article art = (Article)obj;
				System.out.println(" >> Article["+art.getTitle()+"] Update!");
			}else if(obj instanceof ImageBean){
				ImageBean img = (ImageBean)obj;
				System.out.println(" >> ImageBean["+img.getHttpUrl()+"] Update!");
			}else if(obj instanceof ArticleDoc){
				ArticleDoc doc = (ArticleDoc)obj;
				try{
					if(DAOFactory.getInstance().getArticleDocDao().update(doc)){
						System.out.println(" >> ArticleDoc["+doc.getTitle()+"] Update Success!");
					}
				}catch(Exception e){
					System.err.println(" >> executeUpdate.ArticleDoc.Exception:"+e);
				}
			}else if(obj instanceof PicfileBean){
				PicfileBean pic = (PicfileBean)obj;
				System.out.println(" >> PicfileBean["+pic.getArticleId()+"] Update!");
			}else{
				System.out.println(" >> Unknow Object");
			}
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

	
}