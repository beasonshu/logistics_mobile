/**
 * $Id: StringUtil.java,v 1.1 2011/11/21
 * 09:23:31 gouyh Exp $
 */
package com.ia.logistics.comm;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.widget.Toast;

public final class StringUtil {

	public static String STRING_REGEX = ",";

	public static String[] getStringToLongArray(String args, String regex) {
		if (args == null || args.length() < 1) {
			return null;
		}
		String[] temp = args.split(regex);
		return temp;
	}

	public static Long[] getStringToLong(String[] args) {
		if (args == null) {
			return null;
		}
		Long[] l_args = new Long[args.length];
		for (int i = 0; i < args.length; i++) {
			l_args[i] = Long.valueOf(args[i]);
		}
		return l_args;
	}

	public static String trimStr(String str) {
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}
	}

	public static String trimParam(String str) {
		if (str != null) {
			return str.trim();
		} else {
			return null;
		}
	}

	public static String getArrayToString(String[] args) {
		String ids = "";
		for (int i = 0; i < args.length; i++) {
			ids += args[i] + ",";
		}
		ids = ids.substring(0, ids.length() - 1);
		return ids;
	}

	public static BigDecimal getBigDecimalNumber(Double d) {
		if (null == d || "".equals(d)) {
			return new BigDecimal("0");
		} else {
			return new BigDecimal(d.toString());
		}
	}

	public static BigDecimal getBigDecimalNumber(Object o) {
		if (null == o || "".equals(o)) {
			return new BigDecimal("0");
		} else {
			return new BigDecimal(o.toString());
		}
	}

	public static Integer getIntegerNumber(Long l) {
		if (null == l || "".equals(l)) {
			return Integer.valueOf(0);
		} else {
			return  Integer.valueOf(l.toString());
		}
	}


	public static String getWarehouseList(Map map) {

		StringBuffer strBuff = new StringBuffer("(");
		if (null == map || map.size() < 1) {
			return "('')";
		}
		Set set = map.keySet();
		if (set.size() < 1) {
			return "('')";
		}
		Iterator it = map.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			String wareHouseCode = (String) it.next();
			if (i == set.size() - 1) {
				strBuff.append("'" + wareHouseCode + "')");
			} else {
				strBuff.append("'" + wareHouseCode + "',");
			}
			i++;

		}
		return strBuff.toString();
	}

	public static double round(double v, int scale) {
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 *
	 * @param input
	 *            需要处理的汉字与数字信息
	 * @param maxStrLength
	 *            最大显示汉字的数量
	 * @return 处理后的信息
	 */
	public static String limitOutput(String input, int maxStrLength) {
		// 字母或数字占一个字节，汉字占两个字节，而在JAVA中是一样的，需处理汉字与字母/数字结合显示占不满资源的情况

		// 以下两行代码用于处理汉字

		int realSize = 0;
		int i = 0;
		String output = "";

		if (input != null && input.length() > 0) {
			while (realSize < maxStrLength && i < input.length()) {

				String temp = input.substring(i, i + 1);

				if (temp.getBytes().length == temp.length()) {
					realSize++;
				} else if (temp.getBytes().length == 2 * temp.length()) {
					realSize = realSize + 2;
				}

				output += temp;
				i++;
			}
		}

		return output;

	}

	// 设置日期
	public static StringBuffer setCurrentDate(int year, int month,
											  int dayOfMonth) {
		StringBuffer date = new StringBuffer();
		StringBuffer dayOfMonthS = new StringBuffer();
		StringBuffer monthS = new StringBuffer();
		if ((month) < 10) {
			monthS = monthS.append("0" + (month));
		} else {
			monthS = monthS.append((month));
		}
		// 日
		if (dayOfMonth < 10) {
			dayOfMonthS.append("0" + dayOfMonth);
		} else {
			dayOfMonthS.append(isLeapYear(year, month, dayOfMonth));
		}
		date.append(year + "" + monthS + dayOfMonthS);
		return date;
	}

	/**
	 * 判断是否为闰年的2月份的29号
	 *
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @return
	 */
	public static int isLeapYear(int year, int month, int dayOfMonth) {
		int day = dayOfMonth;
		if (year % 4 == 0 && !(year % 100 == 0) || year % 400 == 0) { // 判断是否为闰年
			day = dayOfMonth;
		} else {
			if (month == 2 && dayOfMonth >= 29) {
				day = 28;
			} else {
				day = dayOfMonth;
			}
		}
		return day;
	}


	/**
	 * 显示消息对话框
	 *
	 * @param msg
	 * @param act
	 */
	public static void showMessage(String msg, Context act) {
		Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获取当前世界
	 */
	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();
		String time = formatter.format(cal.getTime());
		String strEnd = time.substring(0, 8);//
		return strEnd;
	}

	/**
	 * 获取某段时间前的时间
	 */
	public static String getStrStartDate(int dayOfMOnth_reduce,int month_reducce,int year_reduce) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, dayOfMOnth_reduce);
		c.add(Calendar.MONTH, month_reducce);
		c.add(Calendar.YEAR, year_reduce);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		String mDateTime = formatter.format(c.getTime());
		String strStart = mDateTime.substring(0, 8);// 2007-10-24 09:30
		return strStart;
	}

	public static String null2String(String string) {
		if (string==null) {
			return "";
		}else {
			return string;
		}

	}
}
