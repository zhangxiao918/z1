package com.chinamilitary.dao.impl;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.chinamilitary.bean.MobileBrandTemp;
import com.chinamilitary.dao.IMobileBrandTempDAO;
import com.chinamilitary.db.CommonDB;

public class MobileBrandTempDAOImpl extends CommonDB implements IMobileBrandTempDAO{

	public MobileBrandTempDAOImpl() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	private final static String INSERT_SQL = "insert into tbl_mobile_brand_tmp (d_web_id,d_name,d_icon,d_url,d_status,d_createtime,d_modifytime) values (?,?,?,?,?,now(),now())";
	private final static String UPDATE_SQL = "update tbl_mobile_brand_tmp set d_web_id = ?,d_acticle_url = ?,d_article_real_url = ?,d_article_xml_url = ?,d_title = ?,d_text = ?,d_intro = ? where d_id = ?";
	private final static String QUERY_SQL = "select * from tbl_mobile_brand_tmp ";
	private final static String COUNT_SQL = "select count(*) from tbl_mobile_brand_tmp ";
	
	
	public Integer add(MobileBrandTemp brand) throws Exception {
		int key = -1;
		try{
		if(checkOnly(brand.getName(),brand.getUrl())){
			return -1;
		}
		pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, brand.getWebid());
		pstmt.setString(2, brand.getName());
		pstmt.setString(3, brand.getIcon());
		pstmt.setString(4, brand.getUrl());
		pstmt.setInt(5, brand.getStatus());
		if(pstmt.executeUpdate()  == 1){
			rs = pstmt.getGeneratedKeys();
			while(rs.next()){
				key = rs.getInt(1);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null)
				pstmt.close();
			if(rs != null)
				rs.close();
		}
		return key;
	}
	
	public MobileBrandTemp findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<MobileBrandTemp> findByWebId(Integer webid) throws SQLException {
		List<MobileBrandTemp> list = new ArrayList<MobileBrandTemp>();
		MobileBrandTemp mobile = null;
		try{
			pstmt = conn.prepareStatement(QUERY_SQL+" where d_web_id = ?");
			pstmt.setInt(1, webid);
			rs = pstmt.executeQuery();
			while(rs.next()){
				mobile = new MobileBrandTemp();
				mobile.setId(rs.getInt("d_id"));
				mobile.setWebid(rs.getInt("d_web_id"));
				mobile.setName(rs.getString("d_name"));
				mobile.setUrl(rs.getString("d_url"));
				mobile.setStatus(rs.getInt("d_status"));
				mobile.setIcon(rs.getString("d_icon"));
				mobile.setCreatetime(rs.getDate("d_createtime"));
				mobile.setModifytime(rs.getDate("d_modifytime"));
				list.add(mobile);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pstmt.close();
			rs.close();
		}
		return list;
	}

	public List<MobileBrandTemp> findByStatus(Integer status) throws SQLException {
		List<MobileBrandTemp> list = new ArrayList<MobileBrandTemp>();
		MobileBrandTemp mobile = null;
		try{
			pstmt = conn.prepareStatement(QUERY_SQL+" where d_status = ?");
			pstmt.setInt(1, status);
			rs = pstmt.executeQuery();
			while(rs.next()){
				mobile = new MobileBrandTemp();
				mobile.setId(rs.getInt("d_id"));
				mobile.setWebid(rs.getInt("d_web_id"));
				mobile.setName(rs.getString("d_name"));
				mobile.setUrl(rs.getString("d_url"));
				mobile.setStatus(rs.getInt("d_status"));
				mobile.setIcon(rs.getString("d_icon"));
				mobile.setCreatetime(rs.getDate("d_createtime"));
				mobile.setModifytime(rs.getDate("d_modifytime"));
				list.add(mobile);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pstmt.close();
			rs.close();
		}
		return list;
	}

	
	boolean checkOnly(String name,String url) throws SQLException{
		boolean b = false;
		int count = 0;
		try{
			pstmt = conn.prepareStatement(COUNT_SQL+" where d_name = ? and d_url = ?");// and d_title = ?
			pstmt.setString(1,name);
			pstmt.setString(2, url);
			rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pstmt.close();
			rs.close();
		}
		if(count > 0){
			b = true;
		}
		return b;
	}
}
