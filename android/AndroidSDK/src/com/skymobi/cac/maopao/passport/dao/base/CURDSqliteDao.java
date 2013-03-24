package com.skymobi.cac.maopao.passport.dao.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CURDSqliteDao extends BaseSqliteDao {
	
	protected final boolean  readOnly =true;
	
	public CURDSqliteDao(Context context) {
		super(context);
	}
	
	protected SQLiteDatabase getSQLite(){
		return getSQLite(false);
	}

	protected SQLiteDatabase getSQLite(boolean  isReadOnly){
		SQLiteDatabase db = isReadOnly?getReadableDatabase():getWritableDatabase();
		return db;
	}
	
/*	protected Cursor query(String tableName,String []columns,String selection,String []selectionArgs,String orderby,String limit){
		return sqliteDB.query(tableName, columns, selection, selectionArgs, null, null, orderby,limit);
	}
	
	protected Cursor query(String tableName,String []columns,String selection,String []selectionArgs,String orderby){
		return query(tableName, columns, selection, selectionArgs, orderby,null);
	}
	
	protected Cursor query(String tableName,String []columns,String selection,String []selectionArgs){
		return query(tableName, columns, selection, selectionArgs, null);
	}
	
	protected Cursor query(String tableName,String []columns){
		return query(tableName,columns,null,null,null);
	}
	
	protected Cursor query(String tableName,String []columns,String orderby){
		return query(tableName,columns,null,null,orderby);
	}
    
	protected Cursor query(String tableName,String []columns,String orderby,String limit){
		return query(tableName, columns, null, null, orderby,limit);
	}*/
	
	
	protected void close(Cursor rs,SQLiteDatabase db){
		if(rs!= null && (!rs.isClosed()))
			rs.close();
		if(db!=null && db.isOpen())
			db.close();
    }
	
	protected void close(Cursor rs){
		close(rs,null);
    }
	
	protected void close(SQLiteDatabase db){
		close(null,db);
	}
    

}
