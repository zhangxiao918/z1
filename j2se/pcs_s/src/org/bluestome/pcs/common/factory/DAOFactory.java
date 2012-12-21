package org.bluestome.pcs.common.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bluestome.pcs.dao.ArticleDao;
import org.bluestome.pcs.dao.ArticleDocDao;
import org.bluestome.pcs.dao.ImageDao;
import org.bluestome.pcs.dao.WebSiteDao;
import org.bluestome.pcs.dao.impl.ArticleDaoImpl;
import org.bluestome.pcs.dao.impl.ArticleDocDaoImpl;
import org.bluestome.pcs.dao.impl.ImageDaoImpl;
import org.bluestome.pcs.dao.impl.WebSiteDaoImpl;

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
	public WebSiteDao getWebSiteDao(){
		WebSiteDao dao = null;
		try{
			dao = new WebSiteDaoImpl();
		}catch(Exception e){
			log.error(e);
		}
		return dao;
	}
	
}
