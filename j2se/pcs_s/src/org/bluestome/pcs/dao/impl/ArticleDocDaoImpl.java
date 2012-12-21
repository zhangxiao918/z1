package org.bluestome.pcs.dao.impl;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bluestome.pcs.bean.ArticleDoc;
import org.bluestome.pcs.common.db.CommonDB;
import org.bluestome.pcs.dao.ArticleDocDao;

public class ArticleDocDaoImpl extends CommonDB implements ArticleDocDao {

	//
	private final static String INSERT_SQL = "insert into tbl_article_doc (d_web_id,d_title,d_url,d_modifytime) values (?,?,?,current_timestamp)";
	//d_content = ?,
	private final static String UPDATE_SQL = "update tbl_article_doc set d_web_id = ?,d_title = ?,d_url = ?,d_author = ?,d_grade = ?,d_tag = ?,d_status=?,d_publish_time=?,d_modifytime=current_timestamp where d_id = ?";
	private final static String QUERY_SQL = "select * from tbl_article_doc ";
	private final static String COUNT_SQL = "select count(d_id) from tbl_article_doc ";
	
	public ArticleDocDaoImpl() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<ArticleDoc> find(Integer webId, Integer id) throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " where d_web_id = ? and d_id > ? order by d_id");
		pstmt.setInt(1, webId);
		pstmt.setInt(2,id);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
			bean.setGrade(rs.getInt("d_grade"));
			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
//			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}

	/**
	 * 获取某网站下的前多少的文章
	 * @param webId
	 * @param desc
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	public List<ArticleDoc> find(Integer webId, int status,boolean desc,Integer offset) throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " where d_web_id = ? and d_status = ? order by d_id "+(desc?"desc":"asc")+" limit ?");
		pstmt.setInt(1, webId);
		pstmt.setInt(2, status);
		pstmt.setInt(3,offset);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
			bean.setGrade(rs.getInt("d_grade"));
			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
			bean.setAuthor(rs.getString("d_author"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}
	
	public List<ArticleDoc> findAll() throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " order by d_id ");
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
			bean.setGrade(rs.getInt("d_grade"));
			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
//			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}

	public List<ArticleDoc> findAll(Integer status) throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " where d_status = ? order by d_id ");
		pstmt.setInt(1, status);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
			bean.setGrade(rs.getInt("d_grade"));
			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
//			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}
	
	public List<ArticleDoc> findAll(Integer status,Integer webid) throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " where d_status = ? and d_web_id < ? order by d_id ");
		pstmt.setInt(1, status);
		pstmt.setInt(2, webid);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
			bean.setGrade(rs.getInt("d_grade"));
			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
//			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}
	
	public ArticleDoc findById(Integer id) throws Exception {
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL+" where d_id=? order by d_id");
		pstmt.setInt(1, id);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
			bean.setGrade(rs.getInt("d_grade"));
			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
			bean.setPublishTime(rs.getString("d_publish_time"));
			bean.setCreateTime(rs.getDate("d_createtime"));
		}
		releaseLink();
		return bean;
	}

	public List<ArticleDoc> findByWebId(Integer webId) throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement("select d_id,d_web_id,d_url,d_title,d_status,d_author from tbl_article_doc where d_web_id = ? order by d_id ");
		pstmt.setInt(1, webId);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
//			bean.setGrade(rs.getInt("d_grade"));
//			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
//			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
//			bean.setCreateTime(rs.getDate("d_createtime"));
//			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}

	public List<ArticleDoc> findByWebId(Integer webId,Integer status) throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement("select d_id,d_web_id,d_url,d_title,d_status,d_author from tbl_article_doc where d_web_id = ? and d_status = ? order by d_id ");
		pstmt.setInt(1, webId);
		pstmt.setInt(2, status);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
//			bean.setGrade(rs.getInt("d_grade"));
//			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
//			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
//			bean.setCreateTime(rs.getDate("d_createtime"));
//			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}
	
	public List<ArticleDoc> findDoc(Integer webId, Integer status, Integer id) throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " where d_web_id = ? and d_status = ? and d_id = ? order by d_id");
		pstmt.setInt(1, webId);
		pstmt.setInt(2, status);
		pstmt.setInt(3,id);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
			bean.setGrade(rs.getInt("d_grade"));
			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
//			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}

	public List<ArticleDoc> findDoc(Integer webId, Integer status) throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " where d_web_id = ? and d_status = ? order by d_id");
		pstmt.setInt(1, webId);
		pstmt.setInt(2, status);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
			bean.setGrade(rs.getInt("d_grade"));
			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
//			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}
	
	public List<ArticleDoc> findDoc(Integer webId) throws Exception {
		List<ArticleDoc> list = new ArrayList<ArticleDoc>();
		ArticleDoc bean = null;
		pstmt = conn.prepareStatement(QUERY_SQL + " where d_web_id = ? order by d_id");
		pstmt.setInt(1, webId);
		rs = pstmt.executeQuery();
		while(rs.next()){
			bean = new ArticleDoc();
			bean.setId(rs.getInt("d_id"));
			bean.setWebId(rs.getInt("d_web_id"));
			bean.setUrl(rs.getString("d_url"));
			bean.setTitle(rs.getString("d_title"));
			bean.setGrade(rs.getInt("d_grade"));
			bean.setTag(rs.getString("d_tag"));
			bean.setStatus(rs.getInt("d_status"));
//			bean.setContent(rs.getString("d_content"));
			bean.setAuthor(rs.getString("d_author"));
			bean.setCreateTime(rs.getDate("d_createtime"));
			bean.setPublishTime(rs.getString("d_publish_time"));
			list.add(bean);
		}
		releaseLink();
		return list;
	}

	public int insert(ArticleDoc bean) throws Exception {
		int key = -1;
		if(null == bean.getUrl() || "".equals(bean.getUrl())){
			return key;
		}
		if(null == bean.getTitle() || "".equals(bean.getTitle())){
			log.debug("标题为空!");
			return key;
		}
		if(bean.getTitle().indexOf("：") != -1){
			if(getCountByTitle(bean.getTitle().replace("：", ":")) != -1){
				log.debug("已存在相同标题："+bean.getTitle());
				return key;
			}
		}
		
		Long start = System.currentTimeMillis();
		int urlResult = getCountByURL(bean.getUrl()); //,bean.getWebId()
		Long end = System.currentTimeMillis();
		log.debug("查询耗时："+(end-start));
		if(urlResult > 0){
			return key;
		}
		
		
//		if(checkExists(bean.getTitle(),bean.getWebId())){
//			log.debug("已存在相同标题："+bean.getTitle());
//			return key;
//		}
		
		pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, bean.getWebId());
		pstmt.setString(2, (bean.getTitle() == null ? "" : bean.getTitle()));
		pstmt.setString(3, bean.getUrl() == null ? "" : bean.getUrl());
		if(pstmt.executeUpdate()  == 1){
			rs = pstmt.getGeneratedKeys();
			while(rs.next()){
				key = rs.getInt(1);
			}
		}
		if(pstmt != null)
			pstmt.close();
		if(rs != null)
			rs.close();
		return key;
	}

	public boolean update(ArticleDoc doc) throws Exception {
		boolean b = false;
		pstmt = conn.prepareStatement(UPDATE_SQL);
		//update tbl_article_doc set 
		//d_web_id = ?,d_title = ?,d_url = ?,d_author = ?,d_grade = ?,d_tag = ?,d_content = ?,d_status=?,d_publish_time=?,d_modifytime=current_timestamp 
		//where d_id = ?
		pstmt.setInt(1, doc.getWebId());
		pstmt.setString(2, doc.getTitle());
		pstmt.setString(3, doc.getUrl());
		pstmt.setString(4, doc.getAuthor());
		pstmt.setInt(5, doc.getGrade());
		pstmt.setString(6, doc.getTag());
//		pstmt.setString(7, doc.getContent());
		pstmt.setInt(7, doc.getStatus());
		pstmt.setString(8, doc.getPublishTime());
		pstmt.setInt(9, doc.getId());
		int key  = pstmt.executeUpdate();
		if(key == 1){
			b = true;
		}
		releaseSLink();
		return b;
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

	/**
	 * 
	 */
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

	public int getCount(String url,String title) throws Exception {
		int count = 0;
		pstmt = conn.prepareStatement(COUNT_SQL+" where d_url = ?");// and d_title = ?
		pstmt.setString(1,url);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		pstmt.close();
		rs.close();
		return count;
	}

	public int getCountByURL(String url,Integer webId) throws Exception{
		int count = 0;
		pstmt = conn.prepareStatement(COUNT_SQL+" where d_url = ? and d_web_id = ? limit 1");// and d_title = ?
		pstmt.setString(1,url);
		pstmt.setInt(2, webId);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		pstmt.close();
		rs.close();
		return count;
	}

	public int getCountByURL(String url) throws Exception{
		int count = 0;
		if(null == url || "".equalsIgnoreCase(url)){
			return count;
		}
		pstmt = conn.prepareStatement(COUNT_SQL+" where d_url = ? limit 1");// and d_title = ?
		pstmt.setString(1,url);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		pstmt.close();
		rs.close();
		return count;
	}

	public int getCountByTitle(String title) throws Exception {
		int count = 0;
		pstmt = conn.prepareStatement(COUNT_SQL+" where d_title = ? limit 1");// and d_title = ?
		pstmt.setString(1,title);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		pstmt.close();
		rs.close();
		return count;
	}

	public int getCountByTitle(String title,Integer webId) throws Exception {
		int count = 0;
		pstmt = conn.prepareStatement(COUNT_SQL+" where d_title = ? and d_web_id = ?");// and d_title = ?
		pstmt.setString(1,title);
		pstmt.setInt(2, webId);
		rs = pstmt.executeQuery();
		while(rs.next()){
			count = rs.getInt(1);
		}
		pstmt.close();
		rs.close();
		return count;
	}
	
	boolean checkExists(String url,String title) throws Exception{ //,Integer webId
		boolean b = false;
		if(url == null && url.equalsIgnoreCase("")){
			return b;
		}
		if(title == null && title.equalsIgnoreCase("")){
			return b;
		}
		if(getCount(url,title) > 0){
			b = true;
		}
		return b;
	}
	
	boolean checkExists(String title,Integer webId) throws Exception{
		boolean b = false;
		if(title == null && title.equalsIgnoreCase("")){
			return b;
		}
		if(getCountByTitle(title.replace("：",":"),webId) > 0){
			b = true;
		}
		return b;
	}
	
}
