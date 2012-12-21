package com.chinamilitary.db;
import java.sql.*;
import javax.swing.*;

/**
 * @author 张晓 2006－01－06 jdbc包装类
 */

public class Dbaccess {

	private static Connection conn; // 连接对象

	private Statement stmt; // SQL声明对象

	PreparedStatement pstmt; // SQL预处理声明对象

	ResultSet rs; // 结果集

	ResultSetMetaData rsmd; // 结果集

	int count; // 返回指定表中的记录个数

	JFrame frame = new JFrame(""); // 出错显示窗口

	private DBConnectionManager connMgr; // 连接池管理类

	/* 数据库连接操作 */
	public Dbaccess() throws Exception {
		/* 初始化连接参数 */
		try {
			connMgr = DBConnectionManager.getInstance();
			/* 创建数据库连接 */
			conn = connMgr.getConnection("zeroDay");
			//conn.setAutoCommit(true);
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(frame, exception.getMessage(),
					"数据库连接失败", JOptionPane.WARNING_MESSAGE);
			throw exception;
		}
	}

	/* 创建Statement声明 */
	public Statement createStmt() throws Exception {
		Statement stmtLOB = null;
		try {
			stmtLOB = conn.createStatement();
		} catch (Exception exception) {
			/* 出错回滚 */
			conn.rollback();
			JOptionPane.showMessageDialog(frame, exception.getMessage(),
					"创建Statement声明发生错误", JOptionPane.WARNING_MESSAGE);
			throw exception;
		}
		return stmtLOB;
	}

	/* 执行Select语句（输入参数）返回结果集 */
	public ResultSet executeQuery(String s) throws Exception {
		/* 清空结果集 */
		stmt = null;
		rs = null;
		rsmd = null;

		try {
			/* 创建SQL声明 */
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			/* 执行SQL语句，如果conn没有设置自动提交属性，此处需要进行提交 */
			rs = stmt.executeQuery(s);
			rsmd = rs.getMetaData();
		} catch (Exception exception) {
			exception.printStackTrace();
			/* 出错回滚 */
			conn.rollback();
			JOptionPane.showMessageDialog(frame, exception.getMessage(),
					"执行查询语句发生错误", JOptionPane.WARNING_MESSAGE);
			throw exception;
		}

		return rs;
	}

	/* 执行Insert,Update,Delete语句（输入参数，无返回值） */
	public boolean executeUpdate(String s) throws Exception {
		boolean b = false;
		stmt = null;
		rs = null;
		try {
			/* 创建SQL声明 */
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			stmt.execute(s);
			b = true;
		} catch (Exception exception) {
			System.out.println("exception.getMessage():"+exception.getMessage());
			/* 出错回滚 */
			//conn.rollback();
			/**
			JOptionPane.showMessageDialog(frame, exception.getMessage(),
					"执行其它SQL语句发生错误", JOptionPane.WARNING_MESSAGE);
			throw exception;
			**/
		}
		return b;
	}

	/* 获得指定表的记录个数 */
	public int getCount(String sql) throws Exception {
		/* 输入参数为SQL串 */
		count = 0;
		rs = null;

		try {
			rs = executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(frame, exception.getMessage(),
					"查询记录条数发生错误", JOptionPane.WARNING_MESSAGE);
			throw exception;
		}

		return count;
	}

	/* 关闭Statement */
	public void closeStmt() throws Exception {
		try {
			stmt.close();
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(frame, exception.getMessage(),
					"关闭Statement发生错误", JOptionPane.WARNING_MESSAGE);
			throw exception;
		}
	}

	/* 关闭Rs */
	public void closeRs() throws Exception {
		try {
			rs.close();
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(frame, exception.getMessage(),
					"关闭Rs发生错误", JOptionPane.WARNING_MESSAGE);
			throw exception;
		}
	}

	/* 释放数据库连接 */
	public void freeConn() throws Exception {

		connMgr.freeConnection("test", conn);
		connMgr.release();

	}

	/* 关闭Statement和释放数据库连接 */
	public void releaseSLink() throws Exception {
		try {
			if (stmt != null) {
				closeStmt();
				freeConn();
			}
		} catch (Exception exception) {
			/* 出错回滚 */
			conn.rollback();
			JOptionPane.showMessageDialog(frame, exception.getMessage(),
					"关闭Statement和释放数据库连接", JOptionPane.WARNING_MESSAGE);
			throw exception;
		}
	}

	/* 关闭Statement、rs和释放数据库连接 */
	public void releaseLink() throws Exception {
		try {
			if (stmt != null) {
				closeStmt();
				closeRs();
				freeConn();
			}
		} catch (Exception exception) {
			/* 出错回滚 */
			conn.rollback();
			JOptionPane.showMessageDialog(frame, exception.getMessage(),
					"关闭Statement、rs和释放数据库连接", JOptionPane.WARNING_MESSAGE);
			throw exception;
		}
	}
	
	public static void main(String args[]){
		Dbaccess db;
		try {
			db = new Dbaccess();
			int count = db.getCount("select count(*) from tbl_web_site");
			System.out.println("count:"+count);
			db.releaseSLink();
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}

}
