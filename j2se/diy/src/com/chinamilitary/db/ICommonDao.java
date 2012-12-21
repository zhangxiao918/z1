package com.chinamilitary.db;

public interface ICommonDao {
	/**
	 * 获得记录总数
	 * @return
	 */
	public abstract int getCount() throws Exception;
	
	/**
	 * 获得记录总数
	 * @return
	 */
	public abstract int getCount(String sql) throws Exception;

}
