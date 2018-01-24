package com.ccx.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static final String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_PAR = "yyyy-MM-dd";

	public static final SimpleDateFormat formatAll = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat formatWithTrm = new SimpleDateFormat(
			"yyyy-MM-dd-HH:mm:ss");
	public static final SimpleDateFormat formatHour = new SimpleDateFormat(
			"yyyy-MM-dd HH");
	public static final SimpleDateFormat formatAll_1 = new SimpleDateFormat(
			"HH:mm:ss");
	public static final SimpleDateFormat formatDate = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat formatDate_1 = new SimpleDateFormat(
			"yyyyMMdd");

	/**
	 * 将短时间格式字符串yyyy-MM-dd转换为时间
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatDate.parse(strDate, pos);
		return strtodate;
	}
	/**
	 * 显示年月日 格式yyyy-MM-dd
	 * 
	 * @param dateStr时间字符串
	 *            2011-08-26 修改人：韩建新 修改内容：增加dateStr为空值处理
	 * @return
	 */
	public static String getDateFromString(Date date) {
		return formatDate.format(date);
	}
	/**
	 * 显示年月日 格式HH:mm:ss
	 * 
	 * @param dateStr时间字符串
	 *            2011-08-26 修改人：韩建新 修改内容：增加dateStr为空值处理
	 * @return
	 */
	public static String getDate1FromString(Date date) {
		return formatAll_1.format(date);
	}
	
	// 得到一个时间延后或前移几天的时间
	public static String getCountedDay(String strDate, Integer delay) {
		try {
			String mdate = "";
			Date d = strToDate(strDate);
			long myTime = (d.getTime() / 1000) + delay * 24 * 60 * 60;
			d.setTime(myTime * 1000);
			mdate = formatDate.format(d);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 得到一个时间向前或向后移动几分钟
	 * @author hhb
	 * @param date
	 * @param min
	 * @return
	 */
	public static String getCountMin(Date date,Integer min){
		return formatAll.format((date.getTime()/1000 + min * 60) * 1000); 
	}
	
	/**
	 * 判断两个日期之间差了多少天，不足一天，则按一天计算，即20.01天也算21天
	 */
	public static int dateDiff(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;
		long baseNum = 3600 * 1000 * 24;
		long absNum = Math.abs(date1.getTime() - date2.getTime());
		long mod = absNum % baseNum;
		int num = (int) (absNum / baseNum);
		if (mod > 0)
			num++;
		return num;
	}
	
	/**
	 * 判断两个日期之间差了多少天，不足一天，则按一天计算，即20.01天也算21天
	 */
	public static int dateDiff1(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;
		long baseNum = 3600 * 1000 * 24;
		long absNum = date1.getTime() - date2.getTime();
		long mod = absNum % baseNum;
		int num = (int) (absNum / baseNum);
		if (mod > 0)
			num++;
		return num;
	}

	/**
	 * 设置两个日期相差几个月
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthDeffDate(String date1, String date2) throws ParseException {
		Calendar bef = Calendar.getInstance();
		Calendar aft = Calendar.getInstance();
		bef.setTime(formatAll.parse(date1));
		aft.setTime(formatAll.parse(date2));
		int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
		int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
		int day = (aft.get(Calendar.DAY_OF_MONTH) - bef
				.get(Calendar.DAY_OF_MONTH));
		day = day > 0 ? 1 : 0;
		return month + result + day;

	}

	/**
	 * 得到一个时间延后或前移几月的时间
	 * 
	 * @param strDate
	 * @param delay
	 * @return
	 */
	public static String getCountedMonth(String strDate, Integer delay) {
		try {
			Date dd = strToDate(strDate);
			Calendar currentDate = Calendar.getInstance();
			currentDate.setTime(dd);
			currentDate.add(Calendar.MONDAY, delay);
			return formatDate.format(currentDate.getTime());
		} catch (Exception e) {
			return "";
		}
	}
	
	
	/**
	 * 得到一个时间延后或前移几月的时间
	 * 
	 * @param strDate
	 * @param delay
	 * @return
	 */
	public static String getCountedMonthAll(String strDate, Integer delay) {
		try {
			Date dd = strToDate(strDate);
			Calendar currentDate = Calendar.getInstance();
			currentDate.setTime(dd);
			currentDate.add(Calendar.MONDAY, delay);
			return formatAll.format(currentDate.getTime());
		} catch (Exception e) {
			return "";
		}
	}

	

	/**
	 * 日期转毫秒
	 * 
	 * @param expireDate
	 * @return
	 */
	public static Long getSecondsFromDateTime(String expireDate) {
		if (expireDate == null || expireDate.trim().equals(""))
			return 0l;
		Date date = null;
		try {
			date = formatAll.parse(expireDate);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 日期转毫秒
	 * 
	 * @param expireDate
	 * @return
	 */
	public static Long getSecondsFromDate(String expireDate) {
		if (expireDate == null || expireDate.trim().equals(""))
			return 0l;
		Date date = null;
		try {
			date = formatDate.parse(expireDate);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l;
		}
	}
	
	
	public static String formatDate(String date) throws ParseException {
		return formatAll.format(formatAll.parse(date));
	}
	public static String formatDate(Date date) throws ParseException {
		return formatAll.format(date);
	}
	public static synchronized String formatDateWithTrim(Date date) throws ParseException {
		return formatWithTrm.format(date);
	}
	public static String formatHour(Date date) throws ParseException {
		return formatHour.format(date);
	}
	public static String formatDateStr(Date date) throws ParseException {
		return formatDate.format(date);
	}
	public static Date parseStr2Date(String date) throws ParseException {
		return formatAll.parse(date);
	}

	/*
	* 多线程下sdf安全问题
	*/
	public static synchronized Date thSafeParseStr2Date(String date) throws ParseException {
		return formatAll.parse(date);
	}
	
	public static Date parseStr2Date1(String date) throws ParseException {
		return formatDate.parse(date);
	}
	
	
	public static String cidGetDate(String cid) throws ParseException {
		String bd = cid.substring(6, 14);
		return formatDate.format((formatDate_1.parse(bd)));
	}
	
	/**
	 * 
	 * @描述：计算日期相差天数
	 * @创建时间：  2017年8月30日
	 * @作者：武向楠
	 * @参数： @param fDate
	 * @参数： @param oDate
	 * @参数： @return      
	 * @return int     
	 * @throws
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
		if (fDate == null || oDate == null){
			return 0;
		}
		long baseNum = 3600 * 1000 * 24;
		long chashu = fDate.getTime() - oDate.getTime();
		if (chashu<0){
			return -1;
		}
		long absNum = Math.abs(chashu);
		if(absNum<0){
			return 0;
		}
//			long mod = absNum % baseNum;
		int num = (int) (absNum / baseNum);
//			if (mod > 0)
//				num++;
		return num;
    }
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String currentDate = df.format(new Date());
        String endDate = "2019-08-30";
		Date oDate = df.parse(currentDate);
		Date fDate = df.parse(endDate);
		int days = DateUtils.daysOfTwo(fDate,oDate);
		System.err.println(days);
	}

}
