package org.bluestome.satelliteweather.db.dao;

/**
 * DAO基类
 * 
 * @author bluestome
 * 
 */
public interface BaseDAO {

	/**
	 * 开始事务
	 */
	public void beginTransaction();

	/**
	 * 结束事务
	 * 
	 * @param isSuccess
	 */
	public void endTransaction(boolean isSuccess);

}
