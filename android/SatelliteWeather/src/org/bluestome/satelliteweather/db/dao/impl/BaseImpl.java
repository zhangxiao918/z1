package org.bluestome.satelliteweather.db.dao.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import org.bluestome.satelliteweather.db.DBException;
import org.bluestome.satelliteweather.db.DatabaseHelper;
import org.bluestome.satelliteweather.db.dao.BaseDAO;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: BaseImpl
 * @Description: 公共类
 * @author Sean.Xie
 * @date 2012-2-9 上午11:37:02
 */
public class BaseImpl implements BaseDAO {
	protected String TAG = this.getClass().getSimpleName();
	protected static final String TIME_TAG = "Time-consuming";
	protected Context context;
	protected ContentResolver resolver;

	protected SQLiteDatabase getSQLiteDatabase() {
		return DatabaseHelper.getInstance(this.context).getSQLiteDatabase();
	}

	public BaseImpl(Context context) {
		this.context = context;
		resolver = context.getContentResolver();
	}

	/**
	 * 关闭游标
	 */
	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			try {
				cursor.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 执行 SQL 语句
	 * 
	 * @param sql
	 */
	protected void executeSQL(String sql) {
		getSQLiteDatabase().execSQL(sql);
	}

	/**
	 * 执行 SQL 语句
	 * 
	 * @param sql
	 * @param bindArgs
	 */
	protected void executeSQL(String sql, String[] bindArgs) {
		getSQLiteDatabase().execSQL(sql, bindArgs);
	}

	/**
	 * 查询总条数
	 * 
	 * @param table
	 * @param whereClause
	 * @return
	 */
	protected int queryCount(String table, String whereClause) {
		int count = 0;
		Cursor cursor = null;
		StringBuilder sql = new StringBuilder("select (_id) from ");
		sql.append(table);
		if (!TextUtils.isEmpty(whereClause)) {
			sql.append(" where ");
			sql.append(whereClause);
		}
		try {
			cursor = getSQLiteDatabase().rawQuery(sql.toString(), null);
			if (null != cursor) {
				count = cursor.getCount();
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			closeCursor(cursor);
		}
		return count;
	}

	/**
	 * 查询
	 * 
	 * @param beanClass
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	protected <T> ArrayList<T> queryWithSort(Class<T> beanClass, String sql,
			String[] selectionArgs) {
		Cursor cursor = null;
		ArrayList<T> list = null;
		try {
			cursor = getSQLiteDatabase().rawQuery(sql, selectionArgs);
			list = cursorToList(cursor, beanClass);
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			closeCursor(cursor);
		}
		return list;
	}

	/**
	 * 倒序查询
	 * 
	 * @param beanClass
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	protected <T> ArrayList<T> queryWithSortDESC(Class<T> beanClass,
			String sql, String[] selectionArgs) {
		Cursor cursor = null;
		ArrayList<T> list = null;
		try {
			cursor = getSQLiteDatabase().rawQuery(sql, selectionArgs);
			list = cursorToListDESC(cursor, beanClass);
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			closeCursor(cursor);
		}
		return list;
	}

	/**
	 * 查询后返回对象
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	protected <T> T queryForObject(Class<T> beanClass, String sql,
			String[] selectionArgs) {
		T instance = null;
		ArrayList<T> list = queryWithSort(beanClass, sql, selectionArgs);
		if (list.size() > 0) {
			instance = list.get(0);
		}
		return instance;
	}

	/**
	 * 查询后返回对象
	 * 
	 * @param beanClass
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected <T> T queryForObject(Class<T> beanClass, String sql) {
		return queryForObject(beanClass, sql, null);
	}

	/**
	 * 取单列
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	protected ArrayList<String> query(String sql, String[] selectionArgs) {
		Cursor cursor = null;
		ArrayList<String> list = new ArrayList<String>();

		try {
			cursor = getSQLiteDatabase().rawQuery(sql, selectionArgs);
			while (cursor.moveToNext()) {
				String value = cursor.getString(0);
				list.add(value);
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			closeCursor(cursor);
		}
		return list;
	}

	/**
	 * 取双列
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	protected Map<String, String> queryMap(String sql, String[] selectionArgs) {
		Cursor cursor = null;
		Map<String, String> map = new HashMap<String, String>();

		try {
			cursor = getSQLiteDatabase().rawQuery(sql, selectionArgs);
			while (cursor.moveToNext()) {
				String key = cursor.getString(0);
				String value = cursor.getString(1);
				map.put(key, value);
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			closeCursor(cursor);
		}

		return map;
	}

	/**
	 * 无序查询
	 * 
	 * @param beanClass
	 * @param keyColumn
	 * @param sql
	 * @param selectionArgs
	 * @return
	 * @throws Exception
	 */
	protected <T> Map<String, T> query(Class<T> beanClass, String keyColumn,
			String sql, String[] selectionArgs) {
		Cursor cursor = null;
		Map<String, T> map = null;
		try {
			cursor = getSQLiteDatabase().rawQuery(sql, selectionArgs);
			map = cursorToMap(cursor, beanClass, keyColumn);
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			closeCursor(cursor);
		}
		return map;
	}

	/**
	 * 无序查询
	 * 
	 * @param beanClass
	 * @param keyColumn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected <T> Map<String, T> query(Class<T> beanClass, String keyColumn,
			String sql) {
		return query(beanClass, keyColumn, sql, null);
	}

	/**
	 * 查询
	 * 
	 * @param beanClass
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected <T> ArrayList<T> queryWithSort(Class<T> beanClass, String sql) {
		return queryWithSort(beanClass, sql, null);
	}

	/**
	 * 倒序查询
	 * 
	 * @param beanClass
	 * @param sql
	 * @return
	 */
	protected <T> ArrayList<T> queryWithSortDESC(Class<T> beanClass, String sql) {
		return queryWithSortDESC(beanClass, sql, null);
	}

	/**
	 * Cursot 转化为List
	 * 
	 * @param cursor
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	private <T> ArrayList<T> cursorToList(Cursor cursor, Class<T> beanClass) {

		// 取得Method
		Method[] methods = beanClass.getMethods();

		ArrayList<T> list = new ArrayList<T>();
		String[] columnNames = cursor.getColumnNames();
		T instence = null;
		while (cursor.moveToNext()) {
			instence = cursorToObject(cursor, beanClass, methods, columnNames);
			list.add(instence);
		}
		return list;
	}

	/**
	 * Cursot 倒序转化为List
	 * 
	 * @param cursor
	 * @param beanClass
	 * @return
	 */
	private <T> ArrayList<T> cursorToListDESC(Cursor cursor, Class<T> beanClass) {

		// 取得Method
		Method[] methods = beanClass.getMethods();

		ArrayList<T> list = new ArrayList<T>();
		String[] columnNames = cursor.getColumnNames();
		T instence = null;
		while (cursor.moveToNext()) {
			instence = cursorToObject(cursor, beanClass, methods, columnNames);
			list.add(0, instence);
		}
		return list;
	}

	/**
	 * Cursot 转化为 Map
	 * 
	 * @param cursor
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	private <T> Map<String, T> cursorToMap(Cursor cursor, Class<T> beanClass,
			String keyColumn) {

		// 取得Method
		Method[] methods = beanClass.getMethods();
		Map<String, T> map = new HashMap<String, T>();
		String[] columnNames = cursor.getColumnNames();
		T instence = null;
		while (cursor.moveToNext()) {
			try {
				instence = cursorToObject(cursor, beanClass, methods,
						columnNames);
				Method method = instence.getClass()
						.getMethod("get" + keyColumn);
				method.setAccessible(true);
				Object result = method.invoke(instence);
				map.put(String.valueOf(result), instence);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 记录转化对象
	 * 
	 * @param cursor
	 * @param beanClass
	 * @param methods
	 * @param columnNames
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("rawtypes")
	private <T> T cursorToObject(Cursor cursor, Class<T> beanClass,
			Method[] methods, String[] columnNames) {
		T instence;
		try {
			instence = beanClass.newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}

		for (String columnName : columnNames) {
			String setMethodName = "set" + columnName;

			// 遍历Method
			for (int j = 0; j < methods.length; j++) {
				if (methods[j].getName().equalsIgnoreCase(setMethodName)) {
					setMethodName = methods[j].getName();
					Class[] clazzes = methods[j].getParameterTypes();
					Object value = null;
					// 获取当前位置的值
					if (clazzes[0] == Short.class || clazzes[0] == short.class) {
						value = cursor.getShort(cursor
								.getColumnIndex(columnName));
					} else if (clazzes[0] == Integer.class
							|| clazzes[0] == int.class) {
						value = cursor
								.getInt(cursor.getColumnIndex(columnName));
					} else if (clazzes[0] == Long.class
							|| clazzes[0] == long.class) {
						value = cursor.getLong(cursor
								.getColumnIndex(columnName));
					} else if (clazzes[0] == Float.class
							|| clazzes[0] == float.class) {
						value = cursor.getFloat(cursor
								.getColumnIndex(columnName));
					} else if (clazzes[0] == Double.class
							|| clazzes[0] == double.class) {
						value = cursor.getDouble(cursor
								.getColumnIndex(columnName));
					} else {
						value = cursor.getString(cursor
								.getColumnIndex(columnName));
						if (value != null
								&& TextUtils.isDigitsOnly((String) value)) {
							value = ((String) value).replaceAll("-", "");
						}
					}

					if (value == null) {
						continue;
					}

					// 实行Set方法
					try {
						methods[j].invoke(instence, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		return instence;
	}

	/**
	 * 新增
	 * 
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @return rowID
	 */
	protected long insert(String table, String nullColumnHack,
			ContentValues values) {
		long rowID = getSQLiteDatabase().insertOrThrow(table, nullColumnHack,
				values);
		return rowID;
	}

	/**
	 * 修改
	 * 
	 * @param sql
	 * @param bindArgs
	 */
	protected int update(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		int rows = getSQLiteDatabase().update(table, values, whereClause,
				whereArgs);
		return rows;
	}

	/**
	 * 删除
	 * 
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	protected int delete(String table, String whereClause, String[] whereArgs) {
		int rows = getSQLiteDatabase().delete(table, whereClause, whereArgs);
		return rows;
	}

	/**
	 * 关闭 DB Helper protected void closeDBHelper() { if (sqLiteDatabase != null
	 * && sqLiteDatabase.isOpen()) { sqLiteDatabase.close(); } if
	 * (databaseHelper != null) { databaseHelper.close(); } }
	 */

	/**
	 * 在事务中执行
	 * 
	 * @param sqls
	 */
	protected boolean executeWithTransaction(ArrayList<String> sqls) {
		beginTransaction();
		try {
			if (sqls != null)
				for (String sql : sqls) {
					executeSQL(sql);
				}
			getSQLiteDatabase().setTransactionSuccessful();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			endTransaction(false);
		}
	}

	/**
	 * 开始事务
	 */
	@Override
	public void beginTransaction() {
		getSQLiteDatabase().beginTransaction();
	}

	/**
	 * 结束事务
	 * 
	 * @param isSuccess
	 */
	@Override
	public void endTransaction(boolean isSuccess) {
		if (isSuccess)
			getSQLiteDatabase().setTransactionSuccessful();
		getSQLiteDatabase().endTransaction();
	}

	/**
	 * 判断是否需要添加到数据库
	 * 
	 * @param value
	 * @return
	 */
	protected boolean isNotZero(Object value) {
		return (Short) value == 0 ? false : true;
	}
}
