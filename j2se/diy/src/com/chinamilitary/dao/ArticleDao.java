package com.chinamilitary.dao;

import java.util.List;

import com.chinamilitary.bean.Article;
import com.chinamilitary.db.ICommonDao;

public interface ArticleDao extends ICommonDao{

	/**
	 * 添加文章
	 * @param article
	 * @return
	 * @throws Exception
	 */
	public int insert(Article article) throws Exception;
	
	/**
	 * 查找所有文章
	 * @return
	 * @throws Exception
	 */
	public List<Article> findAll() throws Exception;
	
	/**
	 * 查找所有文章地址
	 * @return
	 * @throws Exception
	 */
	List<String> findAllArticleURL(int parentWebId) throws Exception;
	
	/**
	 * 根据ID查找记录
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Article findById(Integer id) throws Exception;
	
	/**
	 * 更新记录
	 * @param article
	 * @return
	 * @throws Exception
	 */
	public boolean update(Article article) throws Exception;
	
	/**
	 * 根据WEBID查找记录
	 * @param webId
	 * @return
	 */
	public List<Article> findByWebId(Integer webId,String text) throws Exception;
	
	/**
	 * 根据网站ID和网址查找记录
	 * @param url
	 * @param webid
	 * @return
	 * @throws Exception
	 */
	Article findByUrl(String url,Integer webid) throws Exception;
	
	/**
	 * 根据网站ID和网址查找记录
	 * @param url
	 * @param webid
	 * @return
	 * @throws Exception
	 */
	Article findByUrl(String url) throws Exception;
	
	public List<Article> findShowImg(Integer webId,Integer id) throws Exception;
	
	/**
	 * 根据WEBID查找记录
	 * @param webId
	 * @return
	 */
	List<Article> findByWebId(Integer webId) throws Exception;
	
	/**
	 * 
	 * @param webId
	 * @param text
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Article> findShowImg(Integer webId,String text,Integer id) throws Exception;
	
	/**
	 * 
	 * @param webId
	 * @return
	 * @throws Exception
	 */
	public List<Article> findShowImg(Integer webId) throws Exception;
	
	/**
	 * 根据WEBID查找记录
	 * @param webId
	 * @return
	 */
	public List<Article> findByWebId(Integer webId,String text,boolean desc) throws Exception;
	
	/**
	 * 分页方法
	 * @param asc 排序方式
	 * @param limit 每页显示的数据量
	 * @param offset 分页偏移量
	 * @return
	 */
	List<Article> findByPage(boolean asc,int limit, int offset) throws Exception;
	
}
