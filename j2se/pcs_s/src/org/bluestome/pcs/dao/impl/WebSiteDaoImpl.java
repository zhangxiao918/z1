package org.bluestome.pcs.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bluestome.pcs.bean.WebsiteBean;
import org.bluestome.pcs.common.db.CommonDB;
import org.bluestome.pcs.dao.WebSiteDao;

public class WebSiteDaoImpl extends CommonDB implements WebSiteDao {
	
	private final static String INSERT_SQL = "insert into tbl_web_site (d_parent_id,d_web_url,d_web_name,d_modifytime,d_createtime,d_remarks,d_status) values (?,?,?,current_timestamp,current_timestamp,?,1) ";
	private final static String UPDATE_SQL = "update tbl_web_site set d_parent_id = ?,d_web_url=?,d_web_name=?,d_modifytime=current_timestamp ,d_lastmodified_time = ?,d_status=? where d_id = ? ";
	private final static String QUERY_SQL = "select * from tbl_web_site";
	private final static String COUNT_SQL = "select count(*) from tbl_web_site where d_status = 1";

	public WebSiteDaoImpl() throws Exception {
		super();
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
	 * 添加网站记录
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public boolean insert(WebsiteBean bean) throws Exception{
		boolean b = true;
		int count = -1;
		if(checkExists(bean.getUrl(),bean.getName())){
			return false;
		}
		pstmt = conn.prepareStatement(INSERT_SQL);
		pstmt.setInt(1, bean.getParentId());
		pstmt.setString(2, bean.getUrl());
		pstmt.setString(3, bean.getName());
		pstmt.setString(4,bean.getRemarks());
		count = pstmt.executeUpdate();
		if(count == 1){
			b = true;
		}
		releaseSLink();
		return b;
	}
	
	private boolean checkExists(String url,String name) throws Exception{
		boolean b = false;
		String sql = "select count(*) from tbl_web_site where d_web_url = '"+url+"' and d_web_name='"+name+"' and d_status = 1";
		if(getCount(sql) > 0){
			b = true;
		}
		return b;
	}
	
	/**
	 * 检查站点是否被添加过了
	 * @param url 被添加的站点
	 * @return true: 添加过 false: 未被添加
	 * @throws Exception
	 */
	public boolean checkURL(String url) throws Exception{
		String sql = "select count(*) from tbl_web_site where d_web_url = '"+url+"' ";
		if(getCount(sql) > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改网站记录
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public boolean update(WebsiteBean bean) throws Exception{
		boolean b = false;
		pstmt = conn.prepareStatement(UPDATE_SQL);
		pstmt.setInt(1, bean.getParentId());
		pstmt.setString(2,bean.getUrl());
		pstmt.setString(3, bean.getName());
		pstmt.setString(4, bean.getLastModifyTime());
		pstmt.setInt(5, bean.getStatus());
		pstmt.setInt(6, bean.getId());
		int count = pstmt.executeUpdate();
		if(count > 0){
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
	public List<WebsiteBean> findAll() throws Exception{
		List<WebsiteBean> list = new ArrayList<WebsiteBean>();
		WebsiteBean bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL+" where d_status = ?");
		pstmt.setInt(1, 1);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new WebsiteBean();
			bean.setId(rs.getInt("d_id"));
			bean.setParentId(rs.getInt("d_parent_id"));
			bean.setType(rs.getInt("d_web_type"));
			bean.setName(rs.getString("d_web_name"));
			bean.setUrl(rs.getString("d_web_url"));
			bean.setCreatetme(rs.getDate("d_createtime"));
			bean.setStatus(rs.getInt("d_status"));
			bean.setModifytime(rs.getDate("d_modifytime"));
			bean.setLastModifyTime(rs.getString("d_lastmodified_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}
	
	/**
	 * 根据ID查找记录
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public WebsiteBean findById(Integer id) throws Exception{
		WebsiteBean bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL+" where d_id = ? and d_status = ?");
		pstmt.setInt(1, id);
		pstmt.setInt(2, 1);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new WebsiteBean();
			bean.setId(rs.getInt("d_id"));
			bean.setName(rs.getString("d_web_name"));
			bean.setParentId(rs.getInt("d_parent_id"));
			bean.setType(rs.getInt("d_web_type"));
			bean.setUrl(rs.getString("d_web_url"));
			bean.setCreatetme(rs.getDate("d_createtime"));
			bean.setStatus(rs.getInt("d_status"));
			bean.setModifytime(rs.getDate("d_modifytime"));
			bean.setLastModifyTime(rs.getString("d_lastmodified_time"));
		}
		releaseLink();
		return bean;
	}
	
	/**
	 * 根据ID查找记录
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<WebsiteBean> findByParentId(Integer id) throws Exception{
		List<WebsiteBean> list = new ArrayList<WebsiteBean>();
		WebsiteBean bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL+" where d_status = ? and d_parent_id = ? order by d_parent_id");
		pstmt.setInt(1, 1);
		pstmt.setInt(2, id);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new WebsiteBean();
			bean.setId(rs.getInt("d_id"));
			bean.setParentId(rs.getInt("d_parent_id"));
			bean.setName(rs.getString("d_web_name"));
			bean.setType(rs.getInt("d_web_type"));
			bean.setUrl(rs.getString("d_web_url"));
			bean.setCreatetme(rs.getDate("d_createtime"));
			bean.setStatus(rs.getInt("d_status"));
			bean.setModifytime(rs.getDate("d_modifytime"));
			bean.setLastModifyTime(rs.getString("d_lastmodified_time"));
//			bean.setChild(findByParentId(bean.getId()));
			bean.setRemarks(rs.getString("d_remarks"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}
	
	/**
	 * 根据站点URL查找web对象
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public WebsiteBean findByUrl(String url) throws Exception{
		WebsiteBean bean = null;
		List<WebsiteBean>  list = null;
		try{
			list = findAll();
			for(WebsiteBean tmp:list){
				if(url.startsWith(tmp.getUrl())){
					bean = tmp;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			rs.close();
			pstmt.close();
		}
		return bean;
	}

	/**
	 * 根据站点URL查找web对象
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public Map<String,WebsiteBean> findUrlList(String url) throws Exception{
		List<WebsiteBean>  list = null;
		Map<String,WebsiteBean> wmap = new HashMap<String,WebsiteBean>();
		try{
			list = findAll();
			for(WebsiteBean tmp:list){
				if(url.startsWith(tmp.getUrl())){
					wmap.put(tmp.getUrl(), tmp);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			rs.close();
			pstmt.close();
		}
		return wmap;
	}

}
