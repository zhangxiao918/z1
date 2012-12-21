package org.bluestome.satelliteweather.db;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 类说明：
 * 
 * @author Sean.xie
 * @date 2012-1-19
 * @version 1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = DatabaseHelper.class.getSimpleName();

	private static final String name = "nmc.db";

	private static int version = 1;
	private static DatabaseHelper databaseHelper = null;
	private static SQLiteDatabase sqLiteDatabase = null;

	private DatabaseHelper(Context context) {
		super(context, name, null, version);
	}

	public synchronized static DatabaseHelper getInstance(Context context) {
		if (databaseHelper == null) {
			try {
				PackageManager pm = context.getPackageManager();
				PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(),
						PackageManager.GET_CONFIGURATIONS);
				version = pinfo.versionCode;
				if (version == 0) {
					version = 1;
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			databaseHelper = new DatabaseHelper(context);
		}
		if (sqLiteDatabase == null) {
			sqLiteDatabase = databaseHelper.getWritableDatabase();
			Log.e(TAG, "初始化SQLiteDatabase [" + (sqLiteDatabase != null)
					+ "], name [" + name + "], version [" + version + "]");
		}

		return databaseHelper;
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return sqLiteDatabase;
	}

	public synchronized void shutdown() {
		if (sqLiteDatabase != null) {
			try {
				sqLiteDatabase.close();
				sqLiteDatabase = null;
			} catch (Exception e) {
			}
		}
		if (databaseHelper != null) {
			try {
				databaseHelper.close();
				databaseHelper = null;
			} catch (Exception e) {
			}
		}
		Log.i(TAG, "数据库成功关闭");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 风云2号卫星云图
		db.execSQL("CREATE TABLE IF NOT EXISTS fy2_satellite ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, " // id
				+ "nimage TEXT, " // 小图路径
				+ "spath TEXT, " // 大图路径
				+ "bpath TEXT, " // 大图路径
				+ "pdate TEXT, "// 发布时间
				+ "status INTEGER" // 下载状态
				+ ")");

		// Index
		db.beginTransaction();
		try {
			db.execSQL("Drop Index If Exists MAIN.[fy2_satellite_index]");
			db.execSQL("CREATE  INDEX MAIN.[fy2_satellite_index] On [fy2_satellite] ( [nimage] ) ");
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade !!!! oldVersion  " + oldVersion + "  newVersion:"
				+ newVersion);
		upgradeTable(db);
	}

	/**
	 * 4.0 系统低版本升级调此接口
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, " onDowngrade ");
	}

	/**
	 * 升级安装时需要新增的数据库表
	 * 
	 * @param db
	 */
	private void upgradeTable(SQLiteDatabase db) {
	}

}
