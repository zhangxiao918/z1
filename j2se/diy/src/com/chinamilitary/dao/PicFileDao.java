package com.chinamilitary.dao;

import java.util.List;

import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.db.ICommonDao;

public interface PicFileDao extends ICommonDao{

	/**
	 * 增加记录
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	 boolean insert(PicfileBean bean) throws Exception;
	
	/**
	 * 查找所有记录
	 * @return
	 * @throws Exception
	 */
	 List<PicfileBean> findAll() throws Exception;
	
	/**
	 * 根据ID查找记录
	 * @param id
	 * @return
	 * @throws Exception
	 */
	 PicfileBean findById(Integer id) throws Exception;
	
	/**
	 * 根据文章ID查找记录
	 * @param articleid
	 * @return
	 * @throws Exception
	 */
	 List<PicfileBean> findByArticleId(Integer articleid) throws Exception;
	
	/**
	 * 判断是否存在已下载的图片
	 * @param imgId
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	boolean checkExists(int imgId,int articleId) throws Exception;
	
	/**
	 * 根据图像ID和文章ID查找记录
	 * @param imagId
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	 PicfileBean findByImgIdAndArticleId(Integer imagId,Integer articleId) throws Exception;
	
	/**
	 * 根据图片ID查找记录
	 * @param imageId
	 * @return
	 * @throws Exception
	 */
	 List<PicfileBean> findByImageId(Integer imageId) throws Exception;
	 
	 /**
	  * 更新记录
	  * @param bean
	  * @return
	  * @throws Exception
	  */
	 boolean update(PicfileBean bean) throws Exception;
	
	/**
	 * 检查文件是否存在
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	boolean checkExists(String fileName) throws Exception;
}
