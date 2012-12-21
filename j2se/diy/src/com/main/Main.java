package com.main;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autohome.Callback;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;


public class Main {
	
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	final static ExecutorService taskPool = Executors.newFixedThreadPool(3);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Integer[] ids = {154910,155543,155542,155801,155902,156111,157134,159294};
		for (Integer id : ids) {
			downloadImages(id);
		}
	}
	
	private static void downloadImages(Integer articleId){
		if (articleId > 0) {
			ImageDao dao = DAOFactory.getInstance().getImageDao();
			if (null != dao) {
				List<ImageBean> list = null;
				try {
					list = dao.findImage(articleId);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				if(null != list && list.size() > 0){
					int i=0;
					while(i<list.size()){
						final ImageBean img = list.get(i);
						final String dir = StringUtils.gerDir2(""+articleId,0);
						logger.info(img.getHttpUrl()+"\t"+dir+"\r\n");
						timeout(img.getHttpUrl(), new Callback() {
							@Override
							public void work(final byte[] body) {
								taskPool.submit(new Runnable() {
									public void run() {
										String fileName = String.valueOf(Long.valueOf(System.currentTimeMillis()))+".zh";
										if (img.getHttpUrl().lastIndexOf("/") != -1) {
											fileName = img
													.getHttpUrl()
													.substring(
															img
																	.getHttpUrl()
																	.lastIndexOf(
																			"/")+1);
										}
										logger.info("\t"+fileName);
										// TODO 写入本地文件
										IOUtil.saveFile(System.getProperty("user.dir")+File.separator+dir+fileName, body, false);
									}
								});
							}
						});
						i++;
					}
					logger.info("i:"+i+"|list.size:"+list.size());
				}
			}
		}
	}

	
	static void timeout(String webSite, Callback call) {
		byte[] body = null;
		body = HttpClientUtils.getBody(webSite);
		if(null != body && body.length > 0){
			call.work(body);
		}
	}

}
