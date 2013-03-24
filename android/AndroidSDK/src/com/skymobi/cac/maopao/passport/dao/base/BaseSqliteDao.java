package com.skymobi.cac.maopao.passport.dao.base;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库操作
 * SKY NETWORK TECHNOLOGIES CO.,LTD
 * 
 * @author Wing.Hu
 * @version 1.0
 * 
 * */
class BaseSqliteDao extends SQLiteOpenHelper {
	
	private static final String TAG = "BaseSqlite";
	
	public static final String DATABASE_NAME = "skymobi_pp.db"; //数据库名
	
	protected static final String USER_INFO_TABLE = "tbl_user_info";
	
	//if not exists
	private final String CREATE_TABLE_USERINFO = "create table  " + USER_INFO_TABLE 
		+ "(" 
		+" id integer primary key autoincrement,"
		+ "loginname varchar(50) not null," 
	    + "is_auto_login integer  default 0,"
	    + "is_remember_pwd integer default 0," 
	    + "logintime varchar(30)," 
	    + "password varchar(100) not null,"
	    + "cryptpwd BLOB,"
	    + "pwdlength integer default 6"
	    + ")";
	
	
	private static final int DATABASE_VERSION = 1; //数据库版本号
	

	/**初始化数据库
	 * @param context 应用环境上下文
	 * */
	public BaseSqliteDao(Context context) { 	
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i(TAG, "db context init!");
	}
	
	/**
	 * 创建数据库表，初始化数据库时调用
	 * 
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(TAG, "db onCreate...");
		db.execSQL(CREATE_TABLE_USERINFO);
	}

	/**
	 * 更新数据库，
	 * 
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "db onUpgrade...");
	}

	
}
