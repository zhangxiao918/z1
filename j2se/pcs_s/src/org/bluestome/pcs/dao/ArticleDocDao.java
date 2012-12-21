package org.bluestome.pcs.dao;

import java.util.List;

import org.bluestome.pcs.bean.ArticleDoc;
import org.bluestome.pcs.common.db.ICommonDao;

public interface ArticleDocDao extends ICommonDao {

	/**
	 * 添加文章
	 * @param ArticleDoc
	 * @return
	 * @throws Exception
	 */
	int insert(ArticleDoc doc) throws Exception;
	
	/**
	 * 查找所有文章
	 * @return
	 * @throws Exception
	 */
	List<ArticleDoc> findAll() throws Exception;
	
	/**
	 * 根据记录状态查找记录
	 * @param status
	 * @return
	 * @throws Exception
	 */
	List<ArticleDoc> findAll(Integer status) throws Exception;
	
	/**
	 * 根据记录状态查找记录
	 * @param status
	 * @return
	 * @throws Exception
	 */
	List<ArticleDoc> findAll(Integer status,Integer webId) throws Exception;
	/**
	 * 根据ID查找记录
	 * @param id
	 * @return
	 * @throws Exception
	 */
	ArticleDoc findById(Integer id) throws Exception;
	
	/**
	 * 更新记录
	 * @param ArticleDoc
	 * @return
	 * @throws Exception
	 */
	boolean update(ArticleDoc doc) throws Exception;
	
	/**
	 * 根据网站ID以及记录ID查找记录
	 * @param webId
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<ArticleDoc> find(Integer webId,Integer id) throws Exception;
	
	/**
	 * 根据网站ID和状态码获取文章列表数据
	 * @param webId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	List<ArticleDoc> findByWebId(Integer webId,Integer status) throws Exception;
	
	/**
	 * 根据WEBID查找记录
	 * @param webId
	 * @return
	 */
	List<ArticleDoc> findByWebId(Integer webId) throws Exception;
	
	/**
	 * 根据网站ID和
	 * @param webId
	 * @param text
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<ArticleDoc> findDoc(Integer webId,Integer status,Integer id) throws Exception;
	
	/**
	 * 
	 * @param webId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	List<ArticleDoc> findDoc(Integer webId, Integer status) throws Exception;
	
	/**
	 * 
	 * @param webId
	 * @return
	 * @throws Exception
	 */
	List<ArticleDoc> findDoc(Integer webId) throws Exception;
	
	/**
	 * 获取某网站下的前多少的文章
	 * @param webId
	 * @param desc
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	List<ArticleDoc> find(Integer webId, int status,boolean desc,Integer offset) throws Exception;
	
}
