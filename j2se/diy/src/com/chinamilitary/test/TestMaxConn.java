package com.chinamilitary.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;

public class TestMaxConn {
	public static void main(String args[]) {
		int count = 0;
		Connection[] conn = new Connection[1000];
		Statement[] stmt = new Statement[1000];
		ResultSet[] rs = new ResultSet[1000];
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			for (count = 0; count < 1001; count++) {
				conn[count] = DriverManager
						.getConnection(
								"jdbc:mysql://127.0.0.1:3306/filecollection?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false",
								"root", "123456");
				stmt[count] = conn[count].createStatement();
				rs[count] = stmt[count]
						.executeQuery("select * from tbl_web_site limit 1");
				while (rs[count].next()) {
					System.out.println(rs[count].getInt("d_id") + "\t"
							+ rs[count].getInt("d_parent_id") + "\t"
							+ rs[count].getString("d_web_name"));
					System.out.print(count + "\t");
				}
			}
		} catch (SQLException ex1) {
			System.out.println("\n" + ex1.toString());
		} catch (InstantiationException ex2) {
			System.out.println("\n" + ex2.toString());
		} catch (ClassNotFoundException ex3) {
			System.out.println("\n" + ex3.toString());
		} catch (IllegalAccessException ex4) {
			System.out.println("\n" + ex4.toString());
		} finally {
			try {
				System.out
						.println("\nSystem has opened "
								+ count--
								+ " Mysql connections.\nPress Enter key to close the connections");
				System.in.read();
				System.out.println("\nClose the Connections:");
				for (; count >= 0; count--) {
					rs[count].close();
					stmt[count].close();
					conn[count].close();
					System.out.print(count + "\t");
				}
			} catch (SQLException ex) {
				System.out.println("\n Close connection exception:"
						+ ex.toString());
			} catch (IOException io_ex) {
				System.out.println("\n Close connection exception:"
						+ io_ex.toString());
			}
		}// end the first "try"
	}
}
