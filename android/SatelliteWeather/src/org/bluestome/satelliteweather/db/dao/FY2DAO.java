package org.bluestome.satelliteweather.db.dao;

/**
 * 风云2号卫星云图DAO
 * 
 * @author bluestome
 * 
 */
public interface FY2DAO {

	/**
	 * 添加记录
	 * 
	 * @param nImage
	 * @param path
	 * @param pdate
	 * @return
	 */
	int insert(String nImage, String sPath, String bPath, String pdate);

	/**
	 * 检查是否存在相同的图片记录
	 * 
	 * @param nImage
	 * @return
	 */
	boolean checkNImage(String nImage);
}
