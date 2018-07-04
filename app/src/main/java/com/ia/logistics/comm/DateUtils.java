package com.ia.logistics.comm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static String DATE_FORMAT_YYMMDDHHMM = "yyMMddHHmm";

	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

	public static final String DATE_FORMAT_YYYYMM = "yyyyMM";

	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

	public static Date getFirstDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	public static Date getLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getMaximum(Calendar.DATE));
		return cal.getTime();
	}

	public static Date toDate(String date, String pattern) throws Exception {
		if (date == null || "".equals(date.trim())) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.parse(date);
	}

	public static String toString(Date date, String pattern)  {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	public static java.sql.Date toSqlDate(java.util.Date date) {
		if (date == null)
			return null;
		else
			return new java.sql.Date(date.getTime());
	}

	public static java.sql.Timestamp toSqlTimestamp(java.util.Date date) {
		if (date == null)
			return null;
		else
			return new java.sql.Timestamp(date.getTime());
	}

	/**
	 * 两个日期相差的天数
	 * @param date1 小的日期
	 * @param date2 大的日期
	 * @return
	 */
	public static long diffDays(Date date1,Date date2) {
		if(date1 == null)
			return 1000000;
		if(date2 == null)
			return 1000000;
		long time0=date1.getTime();
		long time1=date2.getTime();
		return (time1-time0)/(1000*60*60*24);
	}

	public static double diffMonths(Date date1, Date date2){
		if(date1 == null)
			return 1000000.00;
		if(date2 == null)
			return 1000000.00;
		int time0=date1.getMonth();
		CommSet.d("baosight","start_date:" + time0);
		int time1=date2.getMonth();
		CommSet.d("baosight","end_date:" + time1);
//		System.out.println("hi:" + (1000*60*60*24*7*(52/12)));
//		double result = (time1-time0)/(1000*60*60*24*7*(52/12));
//		System.out.println("diffMonths:" + result);
		return time1 - time0;
	}
	/**
	 * 在某个日期上加上几天
	 * @param date
	 * @param day 加的天数，为负数时向后退
	 * @return
	 */
	public static Date addDay(Date date,int day) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(Calendar.DAY_OF_MONTH, day);

		return cd.getTime();
	}

	/**
	 * 在某个日期时间点上增加几个小时
	 */
	public static Date addHour(Date date, int hour){

		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(Calendar.HOUR_OF_DAY, hour);

		return cd.getTime();
	}
}
