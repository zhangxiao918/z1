package org.bluestome.satelliteweather.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class Satellite implements BaseColumns {

	public static final String AUTHORITY = "org.bluestome.satelliteweather";

	public static final Uri CONTENT_URI = Uri
			.parse("content://org.bluestome.satelliteweather");

	public static final int Satellite_CODE = 10000;

	/**
	 * 风云2号卫星云图列对象
	 * 
	 * @author bluestome
	 * 
	 */
	public static class FY2Columns implements BaseColumns {

		public static final int FY2S_CODE = Satellite_CODE + 1;

		public static final String TABLE_NAME = "fy2_satellite";

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + TABLE_NAME);
		/**
		 * 图片名称
		 */
		public static final String NIMAGE = "nimage";

		/**
		 * 小图保存路径
		 */
		public static final String SPATH = "spath";

		/**
		 * 大图保存路径
		 */
		public static final String BPATH = "bpath";
		/**
		 * 发布时间
		 */
		public static final String PDATE = "pdate";

		/**
		 * 状态
		 */
		public static final String STATUS = "status";
	}

}
