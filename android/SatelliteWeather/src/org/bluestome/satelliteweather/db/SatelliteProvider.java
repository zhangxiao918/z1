package org.bluestome.satelliteweather.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.bluestome.satelliteweather.db.Satellite.FY2Columns;

/**
 * 卫星云图内容提供器
 * 
 * @author bluestome
 * 
 */
public class SatelliteProvider extends ContentProvider {

	private static String TAG = SatelliteProvider.class.getSimpleName();

	private static SQLiteDatabase database = null;
	private static final UriMatcher sMatcher;

	static {
		sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// 风云2号卫星数据库路径
		sMatcher.addURI(Satellite.AUTHORITY, FY2Columns.TABLE_NAME,
				FY2Columns.FY2S_CODE);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "delete mothed running");
		String tableName = getTableName(uri);
		if (tableName == null)
			return 0;
		int count = database.delete(tableName, selection, selectionArgs);
		return count;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "insert mothed running");
		String tableName = getTableName(uri);
		if (tableName == null)
			return null;
		long newID = database.insert(tableName, null, values);
		if (newID == -1) {
			return null;
		} else {
			return ContentUris.withAppendedId(uri, newID);
		}
	}

	@Override
	public boolean onCreate() {
		database = DatabaseHelper.getInstance(getContext()).getSQLiteDatabase();
		if (database != null)
			Log.d(TAG, "SatelliteProvider中获取db连接成功");
		else {
			Log.d(TAG, "SatelliteProvider中获取db连接失败!");
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "query mothed running");
		String tableName = getTableName(uri);
		if (tableName == null)
			return null;
		return database.query(tableName, projection, selection, selectionArgs,
				null, null, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.d(TAG, "update mothed running");
		String tableName = getTableName(uri);
		if (tableName == null)
			return 0;
		int count = database
				.update(tableName, values, selection, selectionArgs);
		return count;
	}

	/**
	 * 取出表名
	 * 
	 * @param uri
	 * @return
	 */
	private String getTableName(Uri uri) {
		String tableName = null;
		int code = sMatcher.match(uri);
		switch (code) {
		case FY2Columns.FY2S_CODE:
			tableName = FY2Columns.TABLE_NAME;
			break;
		}
		return tableName;
	}

}
