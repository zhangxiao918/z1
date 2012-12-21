package com.chinamilitray.service;

import com.chinamilitary.bean.ArticleDoc;

public interface IArticleDocService {

	/**
	 * 
	 * @param doc
	 * @return
	 */
	boolean add(ArticleDoc doc);
	
	/**
	 * 处理文章作者
	 * @return
	 */
	void processAuthor() throws Exception;
}
