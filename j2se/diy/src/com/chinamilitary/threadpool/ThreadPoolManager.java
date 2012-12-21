package com.chinamilitary.threadpool;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.factory.DAOFactory;

public class ThreadPoolManager {

	private final Log log = LogFactory.getLog(ThreadPoolManager.class);

	private int maxThread;

	public Vector vector;

	public boolean isRunn = false;
	
	public ThreadPoolManager(int threadCount) {
		setMaxThread(threadCount);
		log.info("Starting thread pool...");
		vector = new Vector();
		for (int i = 0; i <= threadCount; i++) {
//			ShowImgArticleThread sThread = new ShowImgArticleThread(i);
			DownloadPicThread threads = new DownloadPicThread(i);
//			vector.addElement(sThread);
			vector.addElement(threads);
//			sThread.start();
			threads.start();
		}
	}

	public synchronized void process(WebsiteBean bean){
		int i;
		for (i = 0; i < vector.size(); i++) {
			ShowImgArticleThread currentThread = (ShowImgArticleThread) vector.elementAt(i);
			if (!currentThread.isRunning()) {
				currentThread.setBean(bean);
				currentThread.setRunning(true);
				setRunn(currentThread.isRunning());
				return;
			}
			if (i == vector.size()) {
				log.info("pool is full, try in another time.");
			}
		}
	}

	public synchronized void process(){
		int i;
		for (i = 0; i < vector.size(); i++) {
			DownloadPicThread currentThread = (DownloadPicThread) vector.elementAt(i);
			if (!currentThread.isRunning()) {
				currentThread.setRunning(true);
				setRunn(currentThread.isRunning());
				return;
			}
			if (i == vector.size()) {
				log.info("pool is full, try in another time.");
			}
		}
	}
	
	public synchronized void process(int webId){
		int i;
		for (i = 0; i < vector.size(); i++) {
			DownloadPicThread currentThread = (DownloadPicThread) vector.elementAt(i);
			if (!currentThread.isRunning()) {
				currentThread.setRunning(true);
				currentThread.setWebId(webId);
				setRunn(currentThread.isRunning());
				return;
			}
			if (i == vector.size()) {
				log.info("pool is full, try in another time.");
			}
		}
	}
	
	public void setMaxThread(int threadCount) {
		maxThread = threadCount;
	}

	public boolean isRunn() {
		return isRunn;
	}

	public synchronized void setRunn(boolean isRunn) {
		this.isRunn = isRunn;
	}
	
	public static void main(String args[]){
		try {
			List<WebsiteBean> list = DAOFactory.getInstance().getWebSiteDao().findByParentId(36);
			if (list != null && list.size() > 0) {
				ThreadPoolManager th = new ThreadPoolManager(list.size());
				for (WebsiteBean bean : list) {
					th.process(bean.getId());
				}
			}
	} catch (Exception e) {
		e.printStackTrace();
	}
	}

}
