package org.bluestome.satelliteweather.db;

import android.content.Context;

import org.bluestome.satelliteweather.db.dao.BaseDAO;
import org.bluestome.satelliteweather.db.dao.FY2DAO;
import org.bluestome.satelliteweather.db.dao.impl.BaseImpl;
import org.bluestome.satelliteweather.db.dao.impl.FY2DAOImpl;

/**
 * DAO工厂
 * 
 * @author bluestome
 * 
 */
public class DaoFactory {

	private static BaseDAO baseDAO = null;
	private static FY2DAO fy2DAO = null;

	private static DaoFactory factory = null;

	private DaoFactory(Context context) {
		baseDAO = new BaseImpl(context);
		fy2DAO = new FY2DAOImpl(context);
	}

	public synchronized static DaoFactory getInstance(Context context) {
		if (factory == null) {
			factory = new DaoFactory(context);
		}
		return factory;
	}

	public FY2DAO getFY2DAO() {
		return fy2DAO;
	}

	public BaseDAO getBaseDAO() {
		return baseDAO;
	}
}
