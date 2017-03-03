package pers.yuhuo.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static final String FMT_DATE = "yyyy-MM-dd";

	public static final String FMT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	public static final String FMT_DATE_HOUR_H_M = "HH:mm";

	/**
	 * 获取当前时间
	 * 
	 * @return 返回当前时间与协调世界时 1970 年 1 月 1 日午夜之间的时间差（以秒为单位测量）。
	 */
	public static int getCurrentTime4Int() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	// 获取当前时间
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	// 获取当前日期
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static String fromTimeStamp2StringDate(long second) {
		SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE);
		return sdf.format(new Date(second * 1000));
	}

	public static String fromTimeStamp2StringDateTime(long second) {
		SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
		return sdf.format(new Date(second * 1000));
	}

	/**
	 * 获取下一个小时的指定分钟
	 * 
	 * @param nextMinute
	 * @return
	 */
	public static Date getNextMinuteHour(int nextMinute) {
		Calendar c = Calendar.getInstance();

		int currentMinute = c.get(Calendar.MINUTE);
		// 时间超过指定分钟，设置为小一个小时的指定分钟
		if (currentMinute >= nextMinute) {
			int currentHour = c.get(Calendar.HOUR);
			if (currentHour == 23) {
				c.add(Calendar.DATE, 1);
				c.set(Calendar.HOUR, 0);
			} else {
				c.add(Calendar.HOUR, 1);
			}
		}
		c.set(Calendar.MINUTE, nextMinute);
		c.set(Calendar.SECOND, 0);

		return c.getTime();
	}

	/**
	 * 获取为大于当前时间的整点或者半点时间，忽略秒
	 * 
	 * @return
	 */
	public static Date get0And30Time() {
		Calendar c = Calendar.getInstance();

		int minute = c.get(Calendar.MINUTE);

		if (minute >= 0 && minute < 30) {
			c.add(Calendar.MINUTE, 30 - minute);
		} else {
			c.add(Calendar.MINUTE, 60 - minute);
		}

		return c.getTime();
	}

	/**
	 * 获取为大于当前时间的整点时间
	 * 
	 * @return
	 */
	public static Date get0Time() {
		Calendar c = Calendar.getInstance();

		int minute = c.get(Calendar.MINUTE);
		c.add(Calendar.MINUTE, 60 - minute);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取为大于当前时间的整点时间1秒
	 * 
	 * @return
	 */
	public static Date get01Time() {
		Calendar c = Calendar.getInstance();

		int minute = c.get(Calendar.MINUTE);
		c.add(Calendar.MINUTE, 60 - minute);
		c.set(Calendar.SECOND, 1);
		return c.getTime();
	}

	public static int getDayOfYear() {
		return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取为大于当前时间的整点时间或半的1秒
	 * 
	 * @return
	 */
	public static Date getHmTime() {
		Calendar c = Calendar.getInstance();

		int minute = c.get(Calendar.MINUTE);
		if (minute > 50) {
			c.add(Calendar.MINUTE, 60 - minute);
		} else {
			c.add(Calendar.MINUTE, 50 - minute);
		}
		c.set(Calendar.SECOND, 1);
		return c.getTime();
	}

	public static void main(String[] args) {
		// System.out.println(getCurrentTime4Int());
		System.err.println(fromTimeStamp2StringDateTime(1471392380));
		System.err.println(getSecondByDateStr("2016-08-14 07:00:00"));
		// System.err.println(getSecondByDateStr("1970-05-01 11:00:00"));
	}

	// 获取第2天0点2分的时间
	public static Date get2Past0Time() {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 2);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	// 获取第2天0点1分的时间
	public static Date get1Past0Time() {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 1);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	// 第二天 12:01的时间
	public static Date getNext1201Time() {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, 12);
		c.set(Calendar.MINUTE, 1);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	// 当前12:01的时间
	public static Date getCurrent1201Time() {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, 12);
		c.set(Calendar.MINUTE, 1);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	// 获取当前小时的秒
	public static Date getCurrentHourSeconds() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	// 获取下一个是n倍数的小时
	public static Date getNextModNHourTime(int n) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, n - c.get(Calendar.HOUR_OF_DAY) % n);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date get2SecondPast0Time() {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 2);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	// 获取第2天0点1分的时间
	public static Date getNextDayPastMinute(int minute) {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	// 获取两个以秒为单位的时间差(单位:天)
	public static int getDiffDay(int beforeDay, int afterDay) {
		int diffSeconds = afterDay - beforeDay;
		return diffSeconds / 60 / 60 / 24;
	}

	public static String getTomorrowDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
		return sdf.format(dt);

	}

	public static String getCurrentHourAndMinute() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		return sdf.format(new Date());
	}

	public static int getCurrentIntHourAndMinute() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		return Integer.parseInt(sdf.format(new Date()));
	}

	/**
	 * 获得当前日期的总秒数
	 * 
	 * @return
	 */
	public static int getCurrentDaySecond() {
		Date date = new Date();
		SimpleDateFormat hourSdf = new SimpleDateFormat("HH");
		int hour = Integer.parseInt(hourSdf.format(date));
		SimpleDateFormat minuteSdf = new SimpleDateFormat("mm");
		int minute = Integer.parseInt(minuteSdf.format(date));
		SimpleDateFormat secondSdf = new SimpleDateFormat("ss");
		int second = Integer.parseInt(secondSdf.format(date));

		return hour * 60 * 60 + minute * 60 + second;
	}

	/**
	 * 获得当前日
	 * 
	 * @return
	 */
	public static int getCurrentDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}

	/**
	 * 今天是一年中的第几天
	 * 
	 * @return
	 */
	public static int getCurrentDayOfYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_YEAR);
	}

	public static String getYestesday() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}

	// 昨天 0点0分1秒
	public static String getYestesday0() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
		return sdf.format(c.getTime());
	}

	// 昨天 23点59分59秒
	public static String getYestesday23() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
		return sdf.format(c.getTime());
	}

	public static String getNextDate4Num(int n) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, n);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}

	// 当天的0点1秒
	public static int getTodaySecond() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		return (int) (c.getTimeInMillis() / 1000);

	}

	/**
	 * 通过格式，日期str获取秒
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static int getSecondByDateStr(String format, String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return (int) (sdf.parse(date).getTime() / 1000);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int getSecondByDateStr(String date) {
		return getSecondByDateStr(FMT_DATE_TIME, date);
	}

	public static int getPvPSecondByDateStr(String date) {
		return getSecondByDateStr(FMT_DATE_HOUR_H_M, date);
	}

	public static Date str2Data(String str) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
		return sdf.parse(str);
	}

	public static long str2Long(String str) {
		try {
			return str2Data(str).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当年的最后一天的59分59秒Date类型，如果有需要则截取转换
	 * 
	 * @return
	 */
	public static Date getLastDateOfYear() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.SECOND, -1);
		return c.getTime();
	}

	/**
	 * 获取下一年第一天的00分00秒Date类型，如果有需要则截取转换
	 * 
	 * @return
	 */
	public static Date getFirstDateOfNextYear() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	// 获取当前时间
	public static String getCurrentTimeOfStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	// 获取当前日期
	public static String getCurrentDateOfStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 获取当天剩余的时间(分钟)
	 * 
	 * @return
	 */
	public static int getDayLeftMinute() {
		Calendar c = Calendar.getInstance();
		int leftMinute = 12 * 60 - (c.get(Calendar.HOUR) * 60 + c
				.get(Calendar.MINUTE));
		if (c.get(Calendar.AM_PM) == 0) {
			leftMinute += 12 * 60;
		}
		return leftMinute;
	}

	/**
	 * 周一为1
	 * 
	 * @return
	 */
	public static int getDayOfWeek() {
		Calendar c = Calendar.getInstance();
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		if (weekDay == 1) {
			weekDay = 7;
		} else {
			weekDay--;
		}
		return weekDay;
	}

	public static int getDayOfWeek(String fmt, String str) {
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtils.getDateByStr(fmt, str));
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		if (weekDay == 1) {
			weekDay = 7;
		} else {
			weekDay--;
		}
		return weekDay;
	}

	public static Date getWeekFirstDate(String fmt, String str) {
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtils.getDateByStr(fmt, str));
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		if (weekDay == 1) {
			weekDay = 7;
		} else {
			weekDay--;
		}
		c.add(Calendar.DATE, -weekDay + 1);
		return c.getTime();
	}

	public static String addHour(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, hour);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
	}

	/**
	 * 一周168小时，获得当前时间
	 * 
	 * @return
	 */
	public static int getHourOfWeek() {
		int weekDay = getDayOfWeek();
		return (weekDay - 1) * 24 + getCurrentHour();
	}

	/**
	 * 时间是否超出指定时间
	 * 
	 * @param time
	 *            比较时间
	 * @param outimes
	 *            超出时间（秒）
	 * @return
	 */
	public static boolean isTimeout(long time, int outimes) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, -outimes);

		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println( sdf.format(c.getTimeInMillis()));
		// System.out.println( sdf.format(time));

		return time < c.getTimeInMillis();
	}

	public static int getCurrentHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	public static int getCurrentMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	public static int getCurrentSecond() {
		return Calendar.getInstance().get(Calendar.SECOND);
	}

	public static Date getDateByStr(String fmt, String dateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			return sdf.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}

	public static int getNDayBefore4Int(int n) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DAY_OF_MONTH, -n);
		return (int) (c.getTimeInMillis() / 1000);
	}

	public static String long2Date(long mill) {
		Date date = new Date(mill);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}
}
