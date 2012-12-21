package com.chinamilitary.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class CommonDB {

	protected ResultSet rs = null;
	protected PreparedStatement pstmt = null;
	protected static Connection conn = null; // 连接对象
	protected DBConnectionManager connMgr = null; // 连接池管理类
	protected final Log log = LogFactory.getLog(this.getClass());
	
	public CommonDB() throws Exception{
		try {
			connMgr = DBConnectionManager.getInstance();
			/* 创建数据库连接 */
			conn = connMgr.getConnection("zeroDay");
		} catch (Exception exception) {
			log.debug("CommonDB.init"+exception);
			throw exception;
		}
	}
	
	/* 关闭Statement */
	public void closeStmt() throws Exception {
		try {
//			log.debug("关闭pstmt");
			pstmt.close();
		} catch (Exception exception) {
			log.debug("关闭pstmt"+exception);
			throw exception;
		}
	}

	/* 关闭Rs */
	public void closeRs() throws Exception {
		try {
//			log.debug("关闭rs");
			rs.close();
		} catch (Exception exception) {
			log.debug("关闭rs"+exception);
			throw exception;
		}
	}

	/* 释放数据库连接 */
	public void freeConn() throws Exception {
//		connMgr.freeConnection("zeroDay", conn);
		connMgr.release();

	}

	/* 关闭Statement和释放数据库连接 */
	public void releaseSLink() throws Exception {
		try {
			if (pstmt != null) {
				closeStmt();
				freeConn();
			}
		} catch (Exception exception) {
			/* 出错回滚 */
			conn.rollback();
			throw exception;
		}
	}

	/* 关闭Statement、rs和释放数据库连接 */
	public void releaseLink() throws Exception {
		try {
			if (pstmt != null) {
				closeStmt();
				closeRs();
				freeConn();
			}
		} catch (Exception exception) {
			/* 出错回滚 */
			conn.rollback();
			throw exception;
		}
	}
	
	/**
	 * 错误回滚
	 * @throws Exception
	 */
	public void rollback() throws Exception{
		//暂时不需要进行回滚操作
//		 conn.rollback();
	}
	
}
