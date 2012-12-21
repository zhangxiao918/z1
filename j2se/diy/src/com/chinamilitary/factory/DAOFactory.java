package com.chinamilitary.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ArticleDocDao;
import com.chinamilitary.dao.IMobileBrandTempDAO;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.dao.impl.ArticleDaoImpl;
import com.chinamilitary.dao.impl.ArticleDocDaoImpl;
import com.chinamilitary.dao.impl.ImageDaoImpl;
import com.chinamilitary.dao.impl.MobileBrandTempDAOImpl;
import com.chinamilitary.dao.impl.PicFileDaoImpl;
import com.chinamilitary.dao.impl.WebSiteDaoImpl;

public class DAOFactory {
	
	private static Log log = LogFactory.getLog(DAOFactory.class);

	private static DAOFactory instance = null;
	
	public DAOFactory(){
		
	}
	
	/**
	 * 单例方法
	 * @return
	 */
	public static DAOFactory getInstance(){
		if(instance == null)
			instance = new DAOFactory();
		return instance;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArticleDao getArticleDao(){
		ArticleDao dao = null;
		try{
			dao = new ArticleDaoImpl();
		}catch(Exception e){
			log.error(e);
		}
		return dao;
	}
	
	public ArticleDocDao getArticleDocDao(){
		ArticleDocDao dao = null;
		try{
			dao = new ArticleDocDaoImpl();
		}catch(Exception e){
			log.error(e);
		}
		return dao;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public ImageDao getImageDao(){
		ImageDao dao = null;
		try{
			dao = new ImageDaoImpl();
		}catch(Exception e){
			
		}
		return dao;
	}
	
	/**
	 * 
	 * @return
	 */
	public PicFileDao getPicFileDao(){
		PicFileDao dao = null;
		try{
			dao = new PicFileDaoImpl();
		}catch(Exception e){
			log.error(e);
		}
		return dao;
	}
	
	/**
	 * 
	 * @return
	 */
	public WebSiteDao getWebSiteDao(){
		WebSiteDao dao = null;
		try{
			dao = new WebSiteDaoImpl();
		}catch(Exception e){
			log.error(e);
		}
		return dao;
	}
	
	/**
	 * 手机品牌临时DAO
	 * @return
	 */
	public IMobileBrandTempDAO getMobileBrandTempDAO(){
		IMobileBrandTempDAO dao = null;
		try{
			dao = new MobileBrandTempDAOImpl();
		}catch(Exception e){
			log.error(e);
		}
		return dao;
	}
}
