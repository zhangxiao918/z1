package com.bluestome.pbbike.biz;

public interface Biz {

	/**
	 * 根据地点查找
	 * @param where
	 * @return
	 */
	String findByWhere(String where);
	
	/**
	 * 根据地点查找的第二步骤
	 * @param where
	 * @return
	 */
	String findByWhere2(String id);
	
	/**
	 * 根据站点查找
	 * @param id
	 * @return
	 */
	String findByStationId(String id);
	
	/**
	 * 根据站名查找
	 * @param name
	 * @return
	 */
	String findByStationName(String name);
}
