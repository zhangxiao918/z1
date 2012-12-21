package com.chinamilitary.threadpool;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.htmlparser.ShowimgParser;

public class ShowImgArticleThread extends Thread {

	private static Log log = LogFactory.getLog(ShowImgArticleThread.class);
	private boolean runningFlag;
	private WebsiteBean bean;
	public ShowImgArticleThread(int threadNumber){
		runningFlag = false;
		System.out.println("第"+threadNumber+"个开始运行");
	}
	
	public synchronized void run(){
		while(true){
			if(!runningFlag){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try{
					ShowimgParser.getSecondLink(bean);
					Thread.sleep(2000);
				}catch(Exception e){
					log.debug("ShowImgArticleThread.Exception"+e);
				}
			}
		}
	}
	
	public synchronized WebsiteBean getBean() {
		return bean;
	}

	public synchronized void setBean(WebsiteBean bean) {
		this.bean = bean;
	}

	public synchronized boolean isRunningFlag() {
		return runningFlag;
	}

	public void setRunningFlag(boolean runningFlag) {
		this.runningFlag = runningFlag;
	}
	
	public synchronized boolean isRunning(){
		return runningFlag;
	}
	
	public synchronized void setRunning(boolean flag){
		runningFlag = flag;
		if(flag){
			this.notify();
		}
	}
	
	public static void main(String args[]){
		WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
		ThreadPoolManager manager = null;
		int COUNT = 0;
		try {
			List<WebsiteBean> list = dao.findByParentId(36);
			if (list != null && list.size() > 0) {
				manager = new ThreadPoolManager(list.size());
				for (WebsiteBean bean : list) {
					manager.process(bean);
					COUNT ++;
				}
			}
			log.debug("COUNT:"+COUNT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
