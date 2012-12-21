package com.chinamilitary.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.db.CommonDB;

public class PicFileDaoImpl extends CommonDB implements PicFileDao {

	private final static String INSERT_SQL = "insert into tbl_pic_file (d_article_id,d_image_id,d_file_url,d_file_title,d_file_name,d_file_small_name) values (?,?,?,?,?,?)";
	private final static String QUERY_SQL = "select * from tbl_pic_file ";
	private final static String COUNT_SQL = "select count(*) from tbl_pic_file";
	final static String UPDATE_SQL = "update tbl_pic_file set d_file_url = ?,d_file_title = ?,d_file_name=?,d_file_small_name=? where d_id = ?";
	public PicFileDaoImpl() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCount() throws Exception {
		int count = 0;
		pstmt = conn.prepareStatement(COUNT_SQL);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		releaseLink();
		return count;
	}

	public int getCount(String sql) throws Exception {
		int count = 0;
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		releaseLink();
		return count;
	}

	/**
	 * 增加记录
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public boolean insert(PicfileBean bean) throws Exception{
		boolean b = false;
		if(null == bean.getName() || bean.getName().equals("")){
			return b;
		}
		if(null == bean.getTitle() || bean.getTitle().equals("")){
			return b;
		}
		
//		if(checkExists(bean.getName())){
//			return b;
//		}
		int count = -1;
		pstmt = conn.prepareStatement(INSERT_SQL);
		pstmt.setInt(1, bean.getArticleId());
		pstmt.setInt(2, bean.getImageId());
		pstmt.setString(3, bean.getUrl());
		pstmt.setString(4, bean.getTitle());
		pstmt.setString(5, bean.getName());
		pstmt.setString(6, bean.getSmallName());
		count = pstmt.executeUpdate();
		if(count == -1){
			b = false;
		}else{
			b = true;
		}
		releaseSLink();
		return b;
	}
	
	/**
	 * 查找所有记录
	 * @return
	 * @throws Exception
	 */
	public List<PicfileBean> findAll() throws Exception{
		List<PicfileBean> list = new ArrayList<PicfileBean>();
		PicfileBean bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new PicfileBean();
			bean.setId(rs.getInt("d_id"));
			bean.setArticleId(rs.getInt("d_article_id"));
			bean.setImageId(rs.getInt("d_image_id"));
			bean.setName(rs.getString("d_file_name"));
			bean.setTitle(rs.getString("d_file_title"));
			bean.setUrl(rs.getString("d_file_url"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setSmallName(rs.getString("d_file_small_name"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}
	
	/**
	 * 查找所有记录
	 * @return
	 * @throws Exception
	 */
	public List<PicfileBean> findPicByWebParentId(int webId) throws Exception{
		//TODO
		return null;
	}
	
	/**
	 * 根据ID查找记录
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PicfileBean findById(Integer id) throws Exception{
		List<PicfileBean> list = new ArrayList<PicfileBean>();
		PicfileBean bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL+" where d_id = ?");
		pstmt.setInt(1, id);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new PicfileBean();
			bean.setId(rs.getInt("d_id"));
			bean.setArticleId(rs.getInt("d_article_id"));
			bean.setImageId(rs.getInt("d_image_id"));
			bean.setName(rs.getString("d_file_name"));
			bean.setTitle(rs.getString("d_file_title"));
			bean.setUrl(rs.getString("d_file_url"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setSmallName(rs.getString("d_file_small_name"));
		}
		releaseLink();
		return bean;
	}
	
	/**
	 * 根据文章ID查找记录
	 * @param articleid
	 * @return
	 * @throws Exception
	 */
	public List<PicfileBean> findByArticleId(Integer articleid) throws Exception{
		List<PicfileBean> list = new ArrayList<PicfileBean>();
		PicfileBean bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " where d_article_id = ?");
		pstmt.setInt(1, articleid);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new PicfileBean();
			bean.setId(rs.getInt("d_id"));
			bean.setArticleId(rs.getInt("d_article_id"));
			bean.setImageId(rs.getInt("d_image_id"));
			bean.setName(rs.getString("d_file_name"));
			bean.setTitle(rs.getString("d_file_title"));
			bean.setUrl(rs.getString("d_file_url"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setSmallName(rs.getString("d_file_small_name"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}
	
	/**
	 * 根据图片ID查找记录
	 * @param imageId
	 * @return
	 * @throws Exception
	 */
	public List<PicfileBean> findByImageId(Integer imageId) throws Exception{
		List<PicfileBean> list = new ArrayList<PicfileBean>();
		PicfileBean bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " where d_image_id = ?");
		pstmt.setInt(1, imageId);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new PicfileBean();
			bean.setId(rs.getInt("d_id"));
			bean.setArticleId(rs.getInt("d_article_id"));
			bean.setImageId(rs.getInt("d_image_id"));
			bean.setName(rs.getString("d_file_name"));
			bean.setTitle(rs.getString("d_file_title"));
			bean.setUrl(rs.getString("d_file_url"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setSmallName(rs.getString("d_file_small_name"));
			list.add(bean);
		}
		if(rs != null)
			rs.close();
		if(pstmt != null)
			pstmt.close();
		return list;
	}
	
	public PicfileBean findByImgIdAndArticleId(Integer imagId,Integer articleId) throws Exception{
		PicfileBean bean = null;
		String sql = "select * from tbl_pic_file where d_article_id = "+articleId+" and d_image_id = "+imagId+" limit 1";
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new PicfileBean();
			bean.setId(rs.getInt("d_id"));
			bean.setArticleId(rs.getInt("d_article_id"));
			bean.setImageId(rs.getInt("d_image_id"));
			bean.setName(rs.getString("d_file_name"));
			bean.setTitle(rs.getString("d_file_title"));
			bean.setUrl(rs.getString("d_file_url"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setSmallName(rs.getString("d_file_small_name"));
		}
		if(rs != null)
			rs.close();
		if(pstmt != null)
			pstmt.close();
		return bean;
	}

	/**
	 * 检查文件是否存在
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public boolean checkExists(String fileName) throws Exception {
		int count = 0;
		String sql = "select count(*) from tbl_pic_file where d_file_name = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, fileName);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		releaseLink();
		if(count == 0)
			return false;
		else
			return true;
	}
	
	/**
	 * 判断是否存在已下载的图片
	 * @param imgId
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public boolean checkExists(int imgId,int articleId) throws Exception{
		boolean b = false;
		int count = 0;
		if(imgId < 1)
			return checkExistsByImgId(imgId);
		if(articleId < 1)
			return checkExistsByArticleId(articleId);
		
		String sql = COUNT_SQL + " where d_image_id = ? and d_article_id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, imgId);
		pstmt.setInt(2, articleId);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		releaseLink();
		if(count == 0)
			return false;
		else
			return true;
	}
	
	boolean checkExistsByImgId(int imgId) throws Exception{
		int count = 0;
		String sql = COUNT_SQL + " where d_image_id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, imgId);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		releaseLink();
		if(count == 0)
			return false;
		else
			return true;
		
	}
	
	boolean checkExistsByArticleId(int articleId) throws Exception{
		int count = 0;
		String sql = COUNT_SQL + " where d_article_id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, articleId);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		releaseLink();
		if(count == 0)
			return false;
		else
			return true;
	}
	
	 /**
	  * 更新记录
	  * @param bean
	  * @return
	  * @throws Exception
	  */
	public boolean update(PicfileBean bean) throws Exception{
		boolean b = false;
		//update tbl_pic_file set d_file_url = ?,d_file_title = ?,d_file_name=?,d_file_small_name=? where d_id = ?
		pstmt = conn.prepareStatement(UPDATE_SQL);
		pstmt.setString(1, bean.getUrl());
		pstmt.setString(2, bean.getTitle());
		pstmt.setString(3, bean.getName());
		pstmt.setString(4, bean.getSmallName());
		pstmt.setInt(5, bean.getId());
		try{
		int key = pstmt.executeUpdate();
		if(key == 1){
			b = true;
		}
		}catch(Exception e){
			System.out.println(e);
		}finally{
			if(pstmt != null)
				pstmt.close();
		}
		return b;
	}
	
}
