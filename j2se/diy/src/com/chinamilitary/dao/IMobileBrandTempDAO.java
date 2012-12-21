package com.chinamilitary.dao;

import java.util.List;

import com.chinamilitary.bean.MobileBrandTemp;

public interface IMobileBrandTempDAO {

	/**
	 * 添加手机品牌记录
	 * @param tmp
	 * @return
	 */
	Integer add(MobileBrandTemp tmp) throws Exception;
	
	/**
	 *  根据网站ID查找手机品牌 
	 * @param webid
	 * @return
	 */
	List<MobileBrandTemp> findByWebId(Integer webid) throws Exception;
	
	/**
	 * 根据ID查找记录
	 * @param id
	 * @return
	 */
	MobileBrandTemp findById(Integer id) throws Exception;
}
