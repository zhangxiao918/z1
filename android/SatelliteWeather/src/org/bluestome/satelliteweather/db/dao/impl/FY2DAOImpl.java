package org.bluestome.satelliteweather.db.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.provider.BaseColumns;
import android.util.Log;

import org.bluestome.satelliteweather.db.Satellite.FY2Columns;
import org.bluestome.satelliteweather.db.dao.FY2DAO;
import org.bluestome.satelliteweather.utils.StringUtil;

public class FY2DAOImpl extends BaseImpl implements FY2DAO {

	public FY2DAOImpl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int insert(String nImage, String sPath, String bPath, String pdate) {
		ContentValues values = new ContentValues();
		if (!StringUtil.isBlank(nImage))
			values.put(FY2Columns.NIMAGE, nImage);
		if (!StringUtil.isBlank(sPath))
			values.put(FY2Columns.SPATH, sPath);
		if (!StringUtil.isBlank(bPath))
			values.put(FY2Columns.BPATH, bPath);
		if (!StringUtil.isBlank(pdate))
			values.put(FY2Columns.PDATE, pdate);
		values.put(FY2Columns.STATUS, 1);
		long id = insert(FY2Columns.TABLE_NAME, null, values);
		return Long.valueOf(id).intValue();
	}

	@Override
	public boolean checkNImage(String nImage) {
		boolean isExists = false;
		Cursor cur = null;
		String sql = "select count(" + BaseColumns._ID + ") from "
				+ FY2Columns.TABLE_NAME + " where " + FY2Columns.NIMAGE
				+ " = ?";
		String[] selectionArgs = { nImage };
		try {
			cur = getSQLiteDatabase().rawQuery(sql, selectionArgs);
			while (cur.moveToNext()) {
				int count = cur.getInt(0);
				if (count > 0) {
					isExists = true;
				}
				break;
			}
		} catch (SQLException ex) {
			Log.e(TAG, "checkNImage failed! - " + ex.getMessage());
		} finally {
			if (cur != null)
				cur.close();
		}
		return isExists;
	}

}
