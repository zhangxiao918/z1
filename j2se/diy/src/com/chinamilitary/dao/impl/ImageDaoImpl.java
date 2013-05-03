
package com.chinamilitary.dao.impl;

import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.db.CommonDB;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageDaoImpl extends CommonDB implements ImageDao {

    private final static String INSERT_SQL = "insert into tbl_image (d_article_id,d_title,d_name,d_imgurl,d_httpurl,d_orderid,d_time,d_intro,d_commentsuburl,d_commentshowurl,d_filesize,d_status,d_link) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private final static String QUERY_SQL = "select * from tbl_image ";

    private final static String COUNT_SQL = "select count(*) from tbl_image ";

    private final static String UPDATE_SQL = "update tbl_image ";

    private final static String FIND_BY_ARTICLE_ID = "select * from tbl_image where d_article_id = ?";

    private final static String DELETE_SQL = "DELETE FROM tbl_image";

    public ImageDaoImpl() throws Exception {
        super();
    }

    /**
     * 根据文章ID查找图片
     * 
     * @param articleId
     * @return
     * @throws Exception
     */
    public List<HashMap<String, String>> findImageByArticle(Integer articleId) throws Exception {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = null;
        pstmt = conn.prepareStatement(FIND_BY_ARTICLE_ID);
        pstmt.setInt(1, articleId);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            map = new HashMap<String, String>();
            map.put("id", String.valueOf(rs.getInt("d_id")));
            map.put("imgurl", rs.getString("d_imgurl"));
            list.add(map);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return list;
    }

    /**
     * 根据文章ID查找图片
     * 
     * @param articleId
     * @return
     * @throws Exception
     */
    public List<ImageBean> findImage(Integer articleId) throws Exception {
        List<ImageBean> list = new ArrayList<ImageBean>();
        ImageBean bean = null;
        pstmt = conn.prepareStatement(FIND_BY_ARTICLE_ID + " order by d_id ");
        pstmt.setInt(1, articleId);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            bean = new ImageBean();
            bean.setId(rs.getInt("d_id"));
            bean.setArticleId(rs.getInt("d_article_id"));
            bean.setTitle(rs.getString("d_title"));
            bean.setName(rs.getString("d_name"));
            bean.setImgUrl(rs.getString("d_imgurl"));
            bean.setHttpUrl(rs.getString("d_httpurl"));
            bean.setOrderId(rs.getInt("d_orderid"));
            bean.setTime(rs.getString("d_time"));
            bean.setIntro(rs.getString("d_intro"));
            bean.setCommentsuburl(rs.getString("d_commentsuburl"));
            bean.setCommentshowurl(rs.getString("d_commentshowurl"));
            bean.setLink(rs.getString("d_link"));
            bean.setCreatetime(rs.getDate("d_createtime"));
            bean.setStatus(rs.getInt("d_status"));
            bean.setFileSize(rs.getLong("d_filesize"));
            list.add(bean);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return list;
    }

    /**
     * 根据文章ID查找图片
     * 
     * @param articleId
     * @param text 资源状态
     * @return
     * @throws Exception
     */
    public List<ImageBean> findImage(Integer articleId, String text) throws Exception {
        List<ImageBean> list = new ArrayList<ImageBean>();
        ImageBean bean = null;
        pstmt = conn.prepareStatement(FIND_BY_ARTICLE_ID + " and d_link = ? order by d_id ");
        pstmt.setInt(1, articleId);
        pstmt.setString(2, text);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            bean = new ImageBean();
            bean.setId(rs.getInt("d_id"));
            bean.setArticleId(rs.getInt("d_article_id"));
            bean.setTitle(rs.getString("d_title"));
            bean.setName(rs.getString("d_name"));
            bean.setImgUrl(rs.getString("d_imgurl"));
            bean.setHttpUrl(rs.getString("d_httpurl"));
            bean.setOrderId(rs.getInt("d_orderid"));
            bean.setTime(rs.getString("d_time"));
            bean.setIntro(rs.getString("d_intro"));
            bean.setCommentsuburl(rs.getString("d_commentsuburl"));
            bean.setCommentshowurl(rs.getString("d_commentshowurl"));
            bean.setLink(rs.getString("d_link"));
            bean.setCreatetime(rs.getDate("d_createtime"));
            bean.setStatus(rs.getInt("d_status"));
            bean.setFileSize(rs.getLong("d_filesize"));
            list.add(bean);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return list;
    }

    public List<ImageBean> findAll() throws Exception {
        List<ImageBean> list = new ArrayList<ImageBean>();
        ImageBean bean = null;
        pstmt = conn.prepareStatement(QUERY_SQL);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            bean = new ImageBean();
            bean.setId(rs.getInt("d_id"));
            bean.setArticleId(rs.getInt("d_article_id"));
            bean.setTitle(rs.getString("d_title"));
            bean.setName(rs.getString("d_name"));
            bean.setImgUrl(rs.getString("d_imgurl"));
            bean.setHttpUrl(rs.getString("d_httpurl"));
            bean.setOrderId(rs.getInt("d_orderid"));
            bean.setTime(rs.getString("d_time"));
            bean.setIntro(rs.getString("d_intro"));
            bean.setCommentsuburl(rs.getString("d_commentsuburl"));
            bean.setCommentshowurl(rs.getString("d_commentshowurl"));
            bean.setLink(rs.getString("d_link"));
            bean.setCreatetime(rs.getDate("d_createtime"));
            list.add(bean);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return list;
    }

    /**
     * 根据父站点
     */
    public List<ImageBean> findImage(int webParentId, String status) throws Exception {
        List<ImageBean> list = new ArrayList<ImageBean>();
        ImageBean bean = null;
        pstmt = conn.prepareStatement("select distinct * from tbl_image where d_article_id in (" +
                "select distinct d_id from tbl_article where d_web_id in (" +
                "select distinct d_id from tbl_web_site where d_parent_id = ? " + // "+webParentId+"
                ")) and d_link = ?");
        pstmt.setInt(1, webParentId);
        pstmt.setString(2, status);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            bean = new ImageBean();
            bean.setId(rs.getInt("d_id"));
            bean.setArticleId(rs.getInt("d_article_id"));
            bean.setTitle(rs.getString("d_title"));
            bean.setName(rs.getString("d_name"));
            bean.setImgUrl(rs.getString("d_imgurl"));
            bean.setHttpUrl(rs.getString("d_httpurl"));
            bean.setOrderId(rs.getInt("d_orderid"));
            bean.setTime(rs.getString("d_time"));
            bean.setIntro(rs.getString("d_intro"));
            bean.setCommentsuburl(rs.getString("d_commentsuburl"));
            bean.setCommentshowurl(rs.getString("d_commentshowurl"));
            bean.setLink(rs.getString("d_link"));
            bean.setCreatetime(rs.getDate("d_createtime"));
            bean.setStatus(rs.getInt("d_status"));
            bean.setFileSize(rs.getLong("d_filesize"));
            list.add(bean);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return list;
    }

    /**
     * 根据父站点查找对应下的资源
     */
    public List<ImageBean> findImage(int webId) throws Exception {
        List<ImageBean> list = new ArrayList<ImageBean>();
        ImageBean bean = null;
        pstmt = conn.prepareStatement("select distinct * from tbl_image where d_article_id in (" +
                "select distinct d_id from tbl_article where d_web_id = ?) and d_link = 'NED' ");
        pstmt.setInt(1, webId);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            bean = new ImageBean();
            bean.setId(rs.getInt("d_id"));
            bean.setArticleId(rs.getInt("d_article_id"));
            bean.setTitle(rs.getString("d_title"));
            bean.setName(rs.getString("d_name"));
            bean.setImgUrl(rs.getString("d_imgurl"));
            bean.setHttpUrl(rs.getString("d_httpurl"));
            bean.setOrderId(rs.getInt("d_orderid"));
            bean.setTime(rs.getString("d_time"));
            bean.setIntro(rs.getString("d_intro"));
            bean.setCommentsuburl(rs.getString("d_commentsuburl"));
            bean.setCommentshowurl(rs.getString("d_commentshowurl"));
            bean.setLink(rs.getString("d_link"));
            bean.setCreatetime(rs.getDate("d_createtime"));
            bean.setStatus(rs.getInt("d_status"));
            bean.setFileSize(rs.getLong("d_filesize"));
            list.add(bean);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return list;
    }

    /**
     * 查找某站点下的所有数据
     * 
     * @param webParentId
     * @return
     * @throws Exception
     */
    public List<String> findImageURL(int webParentId) throws Exception {
        List<String> list = new ArrayList<String>();
        pstmt = conn
                .prepareStatement("select distinct d_httpurl from tbl_image where d_article_id in ("
                        +
                        "select distinct d_id from tbl_article where d_web_id in ("
                        +
                        "select distinct d_id from tbl_web_site where d_parent_id = "
                        + webParentId
                        + "" +
                        "))");
        rs = pstmt.executeQuery();
        String url = "none";
        while (rs.next()) {
            url = rs.getString("d_httpurl");
            list.add(url);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return list;
    }

    /**
     * @param link
     * @return
     * @throws Exception
     */
    public ImageBean findByHttpUrl(String httpurl) throws Exception {
        ImageBean bean = null;
        pstmt = conn
                .prepareStatement(QUERY_SQL + " where d_httpurl = ? order by d_id desc limit 1");
        pstmt.setString(1, httpurl);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            bean = new ImageBean();
            bean.setId(rs.getInt("d_id"));
            bean.setArticleId(rs.getInt("d_article_id"));
            bean.setTitle(rs.getString("d_title"));
            bean.setName(rs.getString("d_name"));
            bean.setImgUrl(rs.getString("d_imgurl"));
            bean.setHttpUrl(rs.getString("d_httpurl"));
            bean.setOrderId(rs.getInt("d_orderid"));
            bean.setTime(rs.getString("d_time"));
            bean.setIntro(rs.getString("d_intro"));
            bean.setCommentsuburl(rs.getString("d_commentsuburl"));
            bean.setCommentshowurl(rs.getString("d_commentshowurl"));
            bean.setLink(rs.getString("d_link"));
            bean.setCreatetime(rs.getDate("d_createtime"));
            bean.setStatus(rs.getInt("d_status"));
            bean.setFileSize(rs.getLong("d_filesize"));
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return bean;
    }

    /**
     * @param link
     * @return
     * @throws Exception
     */
    public ImageBean findByLink(String link, Integer webId) throws Exception {
        ImageBean bean = null;
        pstmt = conn
                .prepareStatement(QUERY_SQL
                        + " where d_link = ? and d_article_id in (select distinct d_id from tbl_article where d_web_id > ? order by d_id desc ) order by d_id desc limit 1");
        pstmt.setString(1, "NED");
        pstmt.setInt(2, webId);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            bean = new ImageBean();
            bean.setId(rs.getInt("d_id"));
            bean.setArticleId(rs.getInt("d_article_id"));
            bean.setTitle(rs.getString("d_title"));
            bean.setName(rs.getString("d_name"));
            bean.setImgUrl(rs.getString("d_imgurl"));
            bean.setHttpUrl(rs.getString("d_httpurl"));
            bean.setOrderId(rs.getInt("d_orderid"));
            bean.setTime(rs.getString("d_time"));
            bean.setIntro(rs.getString("d_intro"));
            bean.setCommentsuburl(rs.getString("d_commentsuburl"));
            bean.setCommentshowurl(rs.getString("d_commentshowurl"));
            bean.setLink(rs.getString("d_link"));
            bean.setCreatetime(rs.getDate("d_createtime"));
            bean.setStatus(rs.getInt("d_status"));
            bean.setFileSize(rs.getLong("d_filesize"));
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return bean;
    }

    public synchronized int insert(ImageBean bean) throws Exception {
        int key = -1;
        if (bean.getHttpUrl() == null || bean.getHttpUrl().equalsIgnoreCase("")) {
            System.out.println("空");
        }
        String sql = "select count(*) from tbl_image where d_httpurl = ?";
        if (getCount(sql, bean.getHttpUrl()) > 0) {
            return -1;
        }
        pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, bean.getArticleId());
        pstmt.setString(2, bean.getTitle());
        pstmt.setString(3, bean.getName());
        pstmt.setString(4, bean.getImgUrl());
        pstmt.setString(5, bean.getHttpUrl());
        pstmt.setInt(6, bean.getOrderId() == null ? -1 : bean.getOrderId());
        pstmt.setString(7, bean.getTime());
        pstmt.setString(8, bean.getIntro());
        pstmt.setString(9, bean.getCommentsuburl());
        pstmt.setString(10, bean.getCommentshowurl());
        if (null == bean.getFileSize()) {
            bean.setFileSize(0l);
        }
        pstmt.setLong(11, bean.getFileSize());
        if (null == bean.getStatus())
            bean.setStatus(-1);
        pstmt.setInt(12, bean.getStatus());
        pstmt.setString(13, bean.getLink());
        if (pstmt.executeUpdate() == 1) {
            rs = pstmt.getGeneratedKeys();
            while (rs.next()) {
                key = rs.getInt(1);
            }
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return key;
    }

    /**
     * 根据ID查找记录
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public ImageBean findById(int id) throws Exception {
        ImageBean bean = null;
        pstmt = conn.prepareStatement(QUERY_SQL + " where d_id = ? ");
        pstmt.setInt(1, id);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            bean = new ImageBean();
            bean.setId(rs.getInt("d_id"));
            bean.setArticleId(rs.getInt("d_article_id"));
            bean.setTitle(rs.getString("d_title"));
            bean.setName(rs.getString("d_name"));
            bean.setImgUrl(rs.getString("d_imgurl"));
            bean.setHttpUrl(rs.getString("d_httpurl"));
            bean.setOrderId(rs.getInt("d_orderid"));
            bean.setTime(rs.getString("d_time"));
            bean.setIntro(rs.getString("d_intro"));
            bean.setCommentsuburl(rs.getString("d_commentsuburl"));
            bean.setCommentshowurl(rs.getString("d_commentshowurl"));
            bean.setLink(rs.getString("d_link"));
            bean.setCreatetime(rs.getDate("d_createtime"));
            bean.setStatus(rs.getInt("d_status"));
            bean.setFileSize(rs.getLong("d_filesize"));
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return bean;
    }

    public int getCount() throws Exception {
        int count = 0;
        pstmt = conn.prepareStatement(COUNT_SQL);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            count = rs.getInt(1);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return count;
    }

    public int getCount(String sql) throws Exception {
        int count = 0;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            count = rs.getInt(1);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return count;
    }

    public int getCount(String sql, String httpUrl) throws Exception {
        int count = 0;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, httpUrl);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            count = rs.getInt(1);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return count;
    }

    /**
     * @param id
     * @return
     * @throws Exception
     */
    public boolean updateLinkStatus(Integer id) throws Exception {
        boolean b = false;
        pstmt = conn.prepareStatement(UPDATE_SQL + "set d_link = 'FD' where d_id = ?");
        pstmt.setInt(1, id);
        int key = pstmt.executeUpdate();
        if (key == 1) {
            b = true;
        }
        if (pstmt != null)
            pstmt.close();
        return b;
    }

    /**
     * @param bean
     * @return
     * @throws Exception
     */
    public synchronized boolean update(ImageBean bean) throws Exception {
        boolean b = false;
        StringBuffer sql = new StringBuffer();
        sql.append(UPDATE_SQL)
                .append(" SET d_article_id=?,d_title=?,d_name=?,d_imgurl=?,d_httpurl=?,d_orderid=?,d_time=?,d_intro=?,");
        sql.append("d_commentsuburl=?,d_commentshowurl=?,d_link=?,d_status=?,d_filesize=? where d_id=?");
        pstmt = conn.prepareStatement(sql.toString());
        pstmt.setInt(1, bean.getArticleId());
        pstmt.setString(2, bean.getTitle());
        pstmt.setString(3, bean.getName());
        pstmt.setString(4, bean.getImgUrl());
        pstmt.setString(5, bean.getHttpUrl());
        pstmt.setInt(6, bean.getOrderId());
        pstmt.setString(7, bean.getTime());
        pstmt.setString(8, bean.getIntro());
        pstmt.setString(9, bean.getCommentsuburl());
        pstmt.setString(10, bean.getCommentshowurl());
        pstmt.setString(11, bean.getLink());
        pstmt.setInt(12, bean.getStatus());
        pstmt.setLong(13, bean.getFileSize());
        pstmt.setInt(14, bean.getId());
        int key = pstmt.executeUpdate();
        if (key == 1) {
            b = true;
        }
        releaseSLink();
        // if(pstmt != null)
        // pstmt.close();
        return b;
    }

    /**
     * 根据图片大小分页获取数据
     * 
     * @param x 文件大小
     * @param offset 偏移量
     * @param limit 最大数量
     * @return
     * @throws Exception
     */
    public List<ImageBean> findImageByFilesizeEqualX(Integer x, Integer offset, Integer limit)
            throws Exception {
        List<ImageBean> list = new ArrayList<ImageBean>();
        ImageBean bean = null;
        pstmt = conn.prepareStatement(QUERY_SQL + " where d_filesize = ? order by d_id limit ?,?");
        pstmt.setInt(1, x);
        pstmt.setInt(2, offset);
        pstmt.setInt(3, limit);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            bean = new ImageBean();
            bean.setId(rs.getInt("d_id"));
            bean.setArticleId(rs.getInt("d_article_id"));
            bean.setTitle(rs.getString("d_title"));
            bean.setName(rs.getString("d_name"));
            bean.setImgUrl(rs.getString("d_imgurl"));
            bean.setHttpUrl(rs.getString("d_httpurl"));
            bean.setOrderId(rs.getInt("d_orderid"));
            bean.setTime(rs.getString("d_time"));
            bean.setIntro(rs.getString("d_intro"));
            bean.setCommentsuburl(rs.getString("d_commentsuburl"));
            bean.setCommentshowurl(rs.getString("d_commentshowurl"));
            bean.setLink(rs.getString("d_link"));
            bean.setCreatetime(rs.getDate("d_createtime"));
            bean.setStatus(rs.getInt("d_status"));
            bean.setFileSize(rs.getLong("d_filesize"));
            list.add(bean);
        }
        if (pstmt != null)
            pstmt.close();
        if (rs != null)
            rs.close();
        return list;
    }

    /**
     * 根据ID删除记录
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    public synchronized boolean delete(int id) throws SQLException {
        try {
            pstmt = conn.prepareStatement(DELETE_SQL + " WHERE d_id=?");
            pstmt.setInt(1, id);
            int r = pstmt.executeUpdate();
            if (r > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pstmt != null)
                pstmt.close();
        }
        return true;
    }

}
