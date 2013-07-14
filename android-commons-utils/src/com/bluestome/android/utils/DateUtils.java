/**
 * 
 */
package com.bluestome.android.utils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期工具类
 * 
 * @author bluestome
 * 
 */
public class DateUtils {

	public static final String MONTH_PATTERN = "yyyy-MM";

	private DateUtils() {
	}

	/**
	 * milliseconds in a second.
	 */
	public static final long SECOND = 1000;

	/**
	 * milliseconds in a minute.
	 */
	public static final long MINUTE = SECOND * 60;

	/**
	 * milliseconds in a hour.
	 */
	public static final long HOUR = MINUTE * 60;

	/**
	 * milliseconds in a day.
	 */
	public static final long DAY = 24 * HOUR;

	public static final String DEFAULT_PATTERN = "yyyyMMdd";

	public static final String FULL_PATTERN = "yyyyMMddHHmmss";

	public static final String FULL_STANDARD_PATTERN = "yyyyMMdd HH:mm:ss";

	public static final String FULL_STANDARD_PATTERN2 = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 返回中文格式的当前日期
	 * 
	 * @return [yyyy-mm-dd]
	 */
	public static String getShortNow() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date nowc = new Date();
		String pid = formatter.format(nowc);
		return pid;
	}

	/**
	 * 返回当前时间24小时制式
	 * 
	 * @return [H:mm]
	 */

	public static String getTimeBykm() {
		SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
		Date nowc = new Date();
		String pid = formatter.format(nowc);
		return pid;
	}

	/**
	 * 返回当前月份
	 * 
	 * @return [MM]
	 */

	public static String getMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		Date nowc = new Date();
		String pid = formatter.format(nowc);
		return pid;
	}

	/**
	 * 返回当前日
	 * 
	 * @return [dd]
	 */

	public static String getDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		Date nowc = new Date();
		String pid = formatter.format(nowc);
		return pid;
	}

	/**
	 * Format date as "yyyyMMdd".
	 * 
	 * @param date
	 *            日期
	 * @return 格式化后的日期字符串
	 */
	public static String formatDate(final Date date) {
		return formatDate(date, DEFAULT_PATTERN);
	}

	/*
	 * 查看前后时间是否在一定的时间范围内
	 */

	public static boolean isInTimePeriod(Date date1, Date date2, int sec) {
		if (date1 == null || date2 == null) {
			return false;
		}
		long seconds = (date2.getTime() - date1.getTime()) / 1000;
		long date = seconds / (24 * 60 * 60); // 相差的天数
		long hour = (seconds - date * 24 * 60 * 60) / (60 * 60);// 相差的小时数
		long minut = (seconds - date * 24 * 60 * 60 - hour * 60 * 60) / (60);// 相差的分钟数
		long second = (seconds - date * 24 * 60 * 60 - hour * 60 * 60 - minut * 60)
				+ minut * 60;// 相差总的秒数
		if (second > sec) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Format date as given date format.
	 * 
	 * @param date
	 *            日期
	 * @return 格式化后的日期字符串
	 */
	public static String formatDate(final Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * change the string to date
	 * 
	 * @param String
	 * 
	 * @return Date if failed return <code>null</code>
	 */
	public static Date parseDate(String date) {
		return parseDate(date, DEFAULT_PATTERN, null);
	}

	/**
	 * change the string to date
	 * 
	 * @param String
	 * @param defaultValue
	 * 
	 * @return Date
	 */
	public static Date parseDate(String date, String df) {
		return parseDate(date, df, null);
	}

	/**
	 * change the string to date
	 * 
	 * @param String
	 * @param df
	 *            DateFormat
	 * @param defaultValue
	 *            if parse failed return the default value
	 * 
	 * @return Date
	 */
	public static Date parseDate(String date, String df, Date defaultValue) {
		if (date == null) {
			return defaultValue;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(df);

		try {
			return formatter.parse(date);
		} catch (ParseException e) {
		}

		return defaultValue;
	}

	/**
	 * @return the current date without time component
	 */
	public static Date currentDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * Get start of date.
	 * 
	 * @param date
	 *            Date
	 * @return Date Date
	 */
	public static Date getStartOfDate(final Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Date(cal.getTime().getTime());
	}

	public static Date getPreviousMonday() {
		Calendar cd = Calendar.getInstance();

		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		Date date;
		if (dayOfWeek == 1) {
			date = addDays(cd.getTime(), -7);
		} else {
			date = addDays(cd.getTime(), -6 - dayOfWeek);
		}

		return getStartOfDate(date);
	}

	public static Date getMondayBefore4Week() {
		Calendar cd = Calendar.getInstance();

		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		Date date;
		if (dayOfWeek == 1) {
			date = addDays(cd.getTime(), -28);
		} else {
			date = addDays(cd.getTime(), -27 - dayOfWeek);
		}

		return getStartOfDate(date);
	}

	public static Date getCurrentMonday() {
		Calendar cd = Calendar.getInstance();

		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		Date date;
		if (dayOfWeek == 1) {
			date = cd.getTime();
		} else {
			date = addDays(cd.getTime(), 1 - dayOfWeek);
		}

		return getStartOfDate(date);
	}

	/**
	 * Get date one day before specified one.
	 * 
	 * @param date1
	 *            test date
	 * @param date2
	 *            date when
	 * @return true if date2 is before date1
	 */
	public static boolean beforeDay(final Date date1, final Date date2) {
		return getStartOfDate(date1).before(getStartOfDate(date2));
	}

	/**
	 * Get date one day after specified one.
	 * 
	 * @param date1
	 *            Date 1
	 * @param date2
	 *            Date 2
	 * @return true if after day
	 */
	public static boolean afterDay(final Date date1, final Date date2) {
		return getStartOfDate(date1).after(getStartOfDate(date2));
	}

	/**
	 * Add specified number of months to the date given.
	 * 
	 * @param date
	 *            Date
	 * @param months
	 *            Int number of months to add
	 * @return Date
	 */
	public static Date addMonths(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	/**
	 * Add specified number of days to the given date.
	 * 
	 * @param date
	 *            date
	 * @param days
	 *            Int number of days to add
	 * @return revised date
	 */
	public static Date addDays(final Date date, int days) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);

		return new Date(cal.getTime().getTime());
	}

	public static Date addMins(final Date date, int mins) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, mins);

		return new Date(cal.getTime().getTime());
	}

	/**
	 * Compare the two dates whether are in the same month.
	 * 
	 * @param date1
	 *            the first date
	 * @param date2
	 *            the second date
	 * @return whether are in the same month
	 */
	public static boolean isSameMonth(Date date1, Date date2) {
		Calendar cal1 = GregorianCalendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = GregorianCalendar.getInstance();
		cal2.setTime(date2);
		return isSameMonth(cal1, cal2);
	}

	/**
	 * Compare the two calendars whether they are in the same month.
	 * 
	 * @param cal1
	 *            the first calendar
	 * @param cal2
	 *            the second calendar
	 * @return whether are in the same month
	 */
	public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
		return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
				&& (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
	}

	/**
	 * Return the end of the month based on the date passed as input parameter.
	 * 
	 * @param date
	 *            Date
	 * @return Date endOfMonth
	 */
	public static Date getEndOfMonth(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DATE, 0);

		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * Get first day of month.
	 * 
	 * @param date
	 *            Date
	 * @return Date
	 */
	public static Date getFirstOfMonth(final Date date) {
		Date lastMonth = addMonths(date, -1);
		lastMonth = getEndOfMonth(lastMonth);
		return addDays(lastMonth, 1);
	}

	public static Date convertStringToDate(String dateString) {
		Locale locale = new Locale("en");
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy",
				DateFormatSymbols.getInstance(locale));
		Date result = null;
		try {
			result = sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		/*
		 * System.out.println(DateUtils.getFirstOfMonth(DateUtils.addMonths(
		 * DateUtils.currentDate(), -3)));
		 * System.out.println(DateUtils.getEndOfMonth(DateUtils.currentDate()));
		 * System.out.println("now:" + DateUtils.now());
		 */
		/*
		 * System.out.println("previous monday:" +
		 * DateUtils.getPreviousMonday()); System.out.println("current monday:"
		 * + DateUtils.getCurrentMonday());
		 * System.out.println("monday before 4 weeks:" +
		 * DateUtils.getMondayBefore4Week());
		 */

		String dateString = new Date().toString();
		Locale locale = new Locale("en");
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy",
				DateFormatSymbols.getInstance(locale));

		System.out.println(dateString);
		Date date;
		try {
			date = sdf.parse(dateString);

			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 检查日期的合法性
	 * 
	 * @param sourceDate
	 * @return
	 */
	public static boolean inFormat(String sourceDate, String format) {
		if (sourceDate == null) {
			return false;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			dateFormat.setLenient(false);
			dateFormat.parse(sourceDate);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * get date time as "yyyyMMddhhmmss"
	 * 
	 * @return the current date with time component
	 */
	public static String now() {
		return formatDate(new Date(), FULL_PATTERN);
	}

	/**
	 * 格式化中文日期短日期格式
	 * 
	 * @param gstrDate
	 *            输入欲格式化的日期
	 * @return [yyyy年MM月dd日]
	 */

	public static String formatShortDateC(Date gstrDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		String pid = formatter.format(gstrDate);
		return pid;
	}

	/**
	 * 返回标准格式的当前时间
	 * 
	 * @return [yyyy-MM-dd k:mm:ss]
	 */

	public static String getNow() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
		Date nowc = new Date();
		String pid = formatter.format(nowc);
		return pid;
	}

	/**
	 * 返回短日期格式
	 * 
	 * @return [yyyy-mm-dd]
	 */
	public static String formatShort(String strDate) {
		String ret = "";
		if (strDate != null && !"1900-01-01 00:00:00.0".equals(strDate)
				&& strDate.indexOf("-") > 0) {
			ret = strDate;
			if (ret.indexOf(" ") > -1)
				ret = ret.substring(0, ret.indexOf(" "));
		}
		return ret;
	}

	public static int getNumberOfSecondsBetween(final double d1, final double d2) {
		if ((d1 == 0) || (d2 == 0)) {
			return -1;
		}

		return (int) (Math.abs(d1 - d2) / SECOND);
	}

	public static int getNumberOfMonthsBetween(final Date before, final Date end) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(before);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(end);
		return (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12
				+ (cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH));
	}

	/**
	 * 根据时间解析，并且得到一个时间对象
	 * 
	 * @param value
	 * @return
	 */
	public static Date parserDate(String value) {
		if (null == value || "".equals(value)) {
			return new Date();
		}
		Date clientIfModifiedSince = new Date();
		DateFormat Gmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
				Locale.ENGLISH);
		try {
			clientIfModifiedSince = Gmt.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
		return clientIfModifiedSince;
	}

}
