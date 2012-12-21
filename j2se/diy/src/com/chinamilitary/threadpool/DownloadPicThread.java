package com.chinamilitary.threadpool;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.CacheUtils;
import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.IOUtil;

public class DownloadPicThread extends Thread {
	
	private static Log log = LogFactory.getLog(DownloadPicThread.class);
	private boolean runningFlag;
	private ImageDao imageDao = null;
	private PicFileDao dao = null;
	private Integer webId;
	private Thread th = null;
	private static MemcacheClient client = MemcacheClient.getInstance();
	
	private static final String PIC_SAVE_PATH = "H:\\showimg\\";
	
	public DownloadPicThread(int threadNumber){
		System.out.println("第"+threadNumber+"个线程开始运行");
		runningFlag = false;
		init();
	}
	
	void init(){
		imageDao = DAOFactory.getInstance().getImageDao();
		dao = DAOFactory.getInstance().getPicFileDao();
	}
	
	public synchronized void run(){
		try{
			while(true){
				if(!runningFlag){
					this.wait();
				}else{
					ImageBean imgBean = imageDao.findByLink("NED",webId);
					PicfileBean bean = null;
					if(imgBean != null){
						bean = new PicfileBean();
						String s_fileName = imgBean.getImgUrl().substring(imgBean.getImgUrl().lastIndexOf("/")+1,imgBean.getImgUrl().length());
						String fileName = imgBean.getHttpUrl().substring(imgBean.getHttpUrl().lastIndexOf("/")+1,imgBean.getHttpUrl().length());
						s_fileName = s_fileName.replace(".", "_s.");
						try{
							
							if(client.get(CacheUtils.getPicFileKey(imgBean.getId(), imgBean.getArticleId())) == null){
								PicfileBean tmp = dao.findByImgIdAndArticleId(imgBean.getId(), imgBean.getArticleId());
								if(tmp != null){
									//小图
									if(client.get(CacheUtils.getSmallPicFileKey(PIC_SAVE_PATH+CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName.replace(".", "_s."))) == null){
										IOUtil.createPicFile(imgBean.getImgUrl(), PIC_SAVE_PATH+CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName.replace(".", "_s."));
									}
									//大图
									if(client.get(CacheUtils.getBigPicFileKey(PIC_SAVE_PATH+CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName)) == null){
										IOUtil.createPicFile(imgBean.getHttpUrl(), PIC_SAVE_PATH+CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName);
									}
								}else{
									client.add((CacheUtils.getPicFileKey(imgBean.getId(), imgBean.getArticleId())), tmp);
								}
							}
							imgBean.setLink("FD"); //FinishDownload
							boolean updateStatus = imageDao.updateLinkStatus(imgBean.getId());
							if(updateStatus){
								System.out.println("更新图片记录成功");
							}else{
								System.out.println("更新图片记录失败");
							}
							bean.setArticleId(imgBean.getArticleId());
							bean.setImageId(imgBean.getId());
							bean.setTitle(imgBean.getTitle());
							bean.setSmallName(CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+s_fileName);
							bean.setName(CommonUtil.getDate("")+File.separator+imgBean.getArticleId()+File.separator+fileName);
							bean.setUrl(PIC_SAVE_PATH);
							boolean  b = dao.insert(bean);
							if(b){
								client.add(CacheUtils.getBigPicFileKey(bean.getUrl()+bean.getName()), bean);
								client.add(CacheUtils.getSmallPicFileKey(bean.getUrl()+bean.getSmallName()), bean);
								System.out.println("成功！");
							}else{
								System.out.println("失败");
							}
						}catch(IOException e){
							redo();
						}
					}
					Thread.sleep(2000);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
//			redo();
			shutdown();
		}
	}
	
	void redo(){
		try{
			Thread.sleep(5*1000);
			System.out.println("重新下载");
			th = new Thread(this,getName());
			th.start();
		}catch(Exception e){
			redo();
		}
	}
	
	void shutdown(){
		runningFlag = false;
	}
	
	
	
	public Integer getWebId() {
		return webId;
	}

	public void setWebId(Integer webId) {
		this.webId = webId;
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
//			this.notify();
			this.notifyAll();
		}
	}

	public ImageDao getImageDao() {
		return imageDao;
	}

	public void setImageDao(ImageDao imageDao) {
		this.imageDao = imageDao;
	}
	
}
