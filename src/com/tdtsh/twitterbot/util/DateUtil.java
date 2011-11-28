package com.tdtsh.twitterbot.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * Utilities for Date.
 *
 * 2011-01-17 日付フォーマット指定
 * 2011-02-28 SimpleDateFormatからFastDateFormatへ変更
 * 2011-07-28 makeDateDiff追加
 *
 * Copyright(C) 2011 Nippon Telenet Co,Ltd. - All Rights Reserved.
 * @author tadatoshi_hanazaki
 * @date 2011-07-28
 */
public class DateUtil {

    DateUtil() {
    }

    private static final String DEFAULT_FORMAT = "yyyy/MM/dd";

    private static Map<String, FastDateFormat> formats = new HashMap<String, FastDateFormat>();

	private static TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");

    /**
     * 日付を"yyyy/MM/dd"にフォーマットする.
     *
     * @param date           日付
     * @return 変換済みの日付
     */
    public static String toString(Date date) {
        return toString(DEFAULT_FORMAT, date);
    }

    /**
     * 日付を指定のフォーマットに変換する.
     *
     * @param format          フォーマット
     * @param date            日付
     * @return 変換済みの日付
     */
    public static String toString(String format, Date date) {
        if (date == null) {
            return "";
        }
        FastDateFormat fdf = getFastDateFormatEx(format);
        return fdf.format(date);
    }

    public static FastDateFormat getFastDateFormat(String format) {
		return getFastDateFormatEx(format);
	}

    private static FastDateFormat getFastDateFormatEx(String format) {
        FastDateFormat fdf = (FastDateFormat) formats.get(format);
        if (fdf == null) {
            fdf = FastDateFormat.getInstance(format,
				TimeZone.getTimeZone("Asia/Tokyo"), new Locale("ja", "JP"));
            formats.put(format, fdf);
        }
        return fdf;
    }

    public static Date[] ageBetween(String start, String end) {
        return ageBetween(Integer.parseInt(start), Integer.parseInt(end));
    }

    public static Date[] ageBetween(int start, int end) {
        return ageBetween(start, end, new Date());
    }

    public static Date[] ageBetween(String start, String end, Date date) {
        return ageBetween(Integer.parseInt(start), Integer.parseInt(end), date);
    }

    public static Date[] ageBetween(int start, int end, Date date) {
        Date[] between = new Date[2];
        Calendar c = Calendar.getInstance(timeZone);
        c.setTime(date);
        c = DateUtils.truncate(c, Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.YEAR, -end - 1);
        between[0] = c.getTime();
        c.add(Calendar.YEAR, end - start + 1);
        between[1] = c.getTime();
        return between;
    }

    /**
     * 今日の日付のdef日前をyyyy-MM-dd HH:mm:ssのStringで返す.
     * @param def 何日前
     */
    public static String makeSqlDate( int def ) {
        java.util.Date date = new java.util.Date();
        String ret = makeSqlDate( def, date );
        return ret;
    }

    /**
     * 今日の日付のdef日前をyyyy-MM-dd HH:mm:ssのStringで返す.
     * @param def 何日前
     * @param Date Dateオブジェクト
     */
    public static String makeSqlDate( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.DATE, def );
        date = cal.getTime();
        String ret = makeDateTime( date, "yyyy-MM-dd HH:mm:ss" );
        return ret;
    }

    /**
     * 今日の日付のdef日前の00時00分のyyyy-MM-dd HH:mm:ssのStringで返す.
     * @param def 何日前
     */
    public static String makeSqlDate00( int def ) {
        java.util.Date date = new java.util.Date();
        String ret = makeSqlDate00( def, date );
        return ret;
    }

    /**
     * 今日の日付のdefヶ月前の00時00分のyyyy-MM-dd HH:mm:ssのStringで返す.
     * @param def 何ヶ月前
     * @param date java.util.Dateオブジェクト
     */
    public static String makeSqlDateM( int def ) {
        java.util.Date date = new java.util.Date();
        String ret = makeSqlDateM( def, date );
        return ret;
    }

    /**
     * 今日の日付のdefヶ月前の00時00分のyyyy-MM-dd HH:mm:ssのStringで返す.
     * @param def 何ヶ月前
     * @param date java.util.Dateオブジェクト
     */
    public static String makeSqlDateM( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.MONTH, def );
        cal.set( cal.get( Calendar.YEAR )
            , cal.get( Calendar.MONTH )
            , cal.get( Calendar.DATE ), 0, 0, 0 );
        date = cal.getTime();
        String ret = makeDateTime( date, "yyyy-MM-dd HH:mm:ss" );
        return ret;
    }

    /**
     * 今日の日付のdef日前の00時00分のyyyy-MM-dd HH:mm:ssのStringで返す.
     * @param def 何日前
     * @param Date Dateオブジェクト
     */
    public static String makeSqlDate00( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.DATE, def );
        cal.set( cal.get( Calendar.YEAR )
            , cal.get( Calendar.MONTH )
            , cal.get( Calendar.DATE ), 0, 0, 0 );
        date = cal.getTime();
        String ret = makeDateTime( date, "yyyy-MM-dd HH:mm:ss" );
        return ret;
    }

    /**
     * 今日の日付のdef日前の00時00分のyyyy-MM-ddのStringで返す.
     * @param def 何日前
     */
    public static String makeDate( int def ) {
        java.util.Date date = new java.util.Date();
        String ret = makeDate( def, date );
        return ret;
    }

    /**
     * 今日の日付のdef日前の00時00分のyyyy-MM-ddのStringで返す.
     * @param def 何日前
     * @param date java.util.Dateオブジェクト
     */
    public static String makeDate( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.DATE, def );
        cal.set( cal.get( Calendar.YEAR )
            , cal.get( Calendar.MONTH )
            , cal.get( Calendar.DATE ), 0, 0, 0 );
        date = cal.getTime();
        String ret = makeDateTime( date, "yyyy-MM-dd" );
        return ret;
    }

    /**
     * 今日の日付のdefヶ月前の00時00分のyyyy_MMのStringで返す.
     * @param def 何ヶ月前
     * @param date java.util.Dateオブジェクト
     */
    public static String makeTableDateM( int def ) {
        java.util.Date date = new java.util.Date();
        String ret = makeTableDateM( def, date );
        return ret;
    }

    /**
     * 今日の日付のdefヶ月前の00時00分のyyyyMMのStringで返す
     * @param def 何ヶ月前
     * @param date java.util.Dateオブジェクト
     */
    public static String makeTableDateM( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.MONTH, def );
        cal.set( cal.get( Calendar.YEAR )
            , cal.get( Calendar.MONTH )
            , cal.get( Calendar.DATE ), 0, 0, 0 );
        date = cal.getTime();
        String ret = makeDateTime( date, "yyyyMM" );
        return ret;
    }

    /**
     * 今日の日付のdef日前の00時00分のyyyyMMのStringで返す.
     * @param def 何日前
     * @param date java.util.Dateオブジェクト
     */
    public static String makeTableDateD( int def ) {
        java.util.Date date = new java.util.Date();
        String ret = makeTableDateD( def, date );
        return ret;
    }

    /**
     * 今日の日付のdef日前の00時00分のyyyy_MMのStringで返す
     * @param def 何日前
     * @param date java.util.Dateオブジェクト
     */
    public static String makeTableDateD( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.DATE, def );
        cal.set( cal.get( Calendar.YEAR )
            , cal.get( Calendar.MONTH )
            , cal.get( Calendar.DATE ), 0, 0, 0 );
        date = cal.getTime();
        String ret = makeDateTime( date, "yyyyMM" );
        return ret;
    }

    /**
     * 今日の日付のdef日前をyyyy/MM/ddのStringで返す.
     * @param def 何日前
     */
    public static String makeDate2( int def ) {
        java.util.Date date = new java.util.Date();
        String ret = makeDate2( def, date );
        return ret;
    }

    /**
     * 今日の日付のdef日前をyyyy/MM/ddのStringで返す.
     * @param def 何日前
     * @param Date Dateオブジェクト
     */
    public static String makeDate2( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.DATE, def );
        date = cal.getTime();
        String ret = makeDateTime( date, "yyyy/MM/dd" );
        return ret;
    }


    /**
     * 日付(String)を任意の形式で返す.
     * @param date java.util.Dateオブジェクト
     */
    public static String makeDateTime( java.util.Date date, String format ) {
        //SimpleDateFormat sdf = new SimpleDateFormat( format );
        FastDateFormat sdf = getFastDateFormatEx(format);
        return sdf.format(date);
    }

    /**
     * 今日の日付のdef日前の00時00分のyyyyMMddのStringで返す
     * @param def 何日前
     * @param date java.util.Dateオブジェクト
     */
    public static String makeTableDateD2( int def ) {
        java.util.Date date = new java.util.Date();
        String ret = makeTableDateD2( def, date );
        return ret;
    }

    /**
     * 今日の日付のdef日前の00時00分のyyyyMMddのStringで返す
     * @param def 何日前
     * @param date java.util.Dateオブジェクト
     */
    public static String makeTableDateD2( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.DATE, def );
        cal.set( cal.get( Calendar.YEAR )
            , cal.get( Calendar.MONTH )
            , cal.get( Calendar.DATE ), 0, 0, 0 );
        date = cal.getTime();
        String ret = makeDateTime( date, "yyyyMMdd" );
        return ret;
    }

    /**
     * 今日の日付のdef日前の00時00分のyyyyMMddのStringで返す
     * @param def 何日前
     * @param date java.util.Dateオブジェクト
     */
    public static long makeDiffDay( Date date ) {
		int yyyy = Integer.parseInt(DateUtil.toString("yyyy", date));
		int mm = Integer.parseInt(DateUtil.toString("MM", date));
		int dd = Integer.parseInt(DateUtil.toString("dd", date));
		return makeDiffDay(yyyy, mm, dd );
	}

    public static long makeDiffDay( int yyyy, int mm, int dd ) {
        // Calendarインスタンス取得
        GregorianCalendar c1 = new GregorianCalendar();
        GregorianCalendar c2 = new GregorianCalendar();
        c1.clear();
        c2.clear();

        // 現在の日付をセット
        c1.setTime( new java.util.Date() );

        int yyyy2 = c1.get( Calendar.YEAR );
        int mm2 = c1.get( Calendar.MONTH );
        int dd2 = c1.get( Calendar.DATE );
        c1.set( yyyy2 , mm2 , dd2, 0, 0, 0 );

        // 比較する日付をセット
        c2.set( yyyy, mm, dd );
        c2.add( Calendar.MONTH, -1 );
        Date t1 = c1.getTime();
        Date t2 = c2.getTime();

        // 日付の差を求める
        long diff = t1.getTime() - t2.getTime();

        // 日付の差 diff はミリ秒になっているので日数に計算
		long diff2 = diff / 1000 / 60 / 60 / 24;
		return diff2;
    }


    /**
	 * 生年月日と現在時刻から年齢を求める.
     * @param dateOfBirth 生年月日
	 * @return 年齢
     */
    public static int getAge( Date dateOfBirth ) {
		int yyyy = Integer.parseInt(DateUtil.toString("yyyy", dateOfBirth));
		int mm = Integer.parseInt(DateUtil.toString("MM", dateOfBirth));
		int dd = Integer.parseInt(DateUtil.toString("dd", dateOfBirth));
		return getAge(yyyy, mm, dd );
	}

    public static int getAge( int yyyy, int mm, int dd ) {
		int age = 0;
		// 誕生日を設定
		Calendar cal = Calendar.getInstance(timeZone);
		cal.set(Calendar.YEAR, yyyy);
		cal.set(Calendar.MONTH, mm);
		cal.set(Calendar.DAY_OF_MONTH, dd);
        cal.add( Calendar.MONTH, -1 );
        FastDateFormat sdf = getFastDateFormatEx("yyyyMMdd");
		// 現在時刻と誕生日を文字列にしてintにする
		int now = Integer.parseInt(sdf.format(new Date()));
		int birth = Integer.parseInt(sdf.format(cal.getTime()));
		age = (int) ((now - birth) / 10000);
		return age;
	}

    /**
	 * 今日は誕生日か.
     * @param dateOfBirth 生年月日
	 * @return 誕生日ならtrue
     */
    public static boolean isBirthDay( Date dateOfBirth ) {
		int mm = Integer.parseInt(DateUtil.toString("MM", dateOfBirth));
		int dd = Integer.parseInt(DateUtil.toString("dd", dateOfBirth));
		return isBirthDay(mm, dd );
	}
    public static boolean isBirthDay( int mm, int dd ) {
		int ageDiff = 1;
		// 誕生日を設定
		Calendar cal = Calendar.getInstance(timeZone);
		cal.set(Calendar.MONTH, mm);
		cal.set(Calendar.DAY_OF_MONTH, dd);
        cal.add( Calendar.MONTH, -1 );
        FastDateFormat sdf = getFastDateFormatEx("MMdd");
		int now = Integer.parseInt(sdf.format(new Date()));
		int birth = Integer.parseInt(sdf.format(cal.getTime()));
		ageDiff = (int) ((now - birth) );
		if (ageDiff == 0 ) {
			return true;
		} else {
			return false;
		}
	}

    public static String getYyyy( Date date ) {
		return DateUtil.toString("yyyy", date);
	}

    public static String getMm( Date date ) {
		return DateUtil.toString("MM", date);
	}

    public static String getDd( Date date ) {
		return DateUtil.toString("dd", date);
	}

    public static Date convertAgeToDateOfBirth( int age ) {
		age = 0-age;
		// 現在日時を設定
		Calendar cal = Calendar.getInstance(timeZone);
        java.util.Date dateNow = new java.util.Date();
        cal.setTime( dateNow );
        cal.add( Calendar.YEAR, age );
		Date retDate = cal.getTime();
		return retDate;

	}

    /**
     * def分前をDateで返す.
     * @param def 何分前
     * @param Date Dateオブジェクト
     */
    public static Date makeSqlDateMinDiff( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.MINUTE, def );
        date = cal.getTime();
        return date;
    }

    /**
     * def日前をDateで返す.
     * @param def 何日前
     * @param date java.util.Dateオブジェクト
     */
    public static Date makeDateDiff( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.DATE, def );
        cal.set( cal.get( Calendar.YEAR )
            , cal.get( Calendar.MONTH )
            , cal.get( Calendar.DATE ), 0, 0, 0 );
        date = cal.getTime();
        return date;
    }

    /**
     * 今日の日付のdef日前をyyyyMMddHHmmのStringで返す.
     * @param def 何日前
     * @param Date Dateオブジェクト
     */
    public static String makeSqlDateForOid( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.DATE, def );
        date = cal.getTime();
        String ret = makeDateTime( date, "yyyyMMddHHmm" );
        return ret;
    }

	/**
	 * 日付を取得する.
	 *
	 * 西暦+先月の文字列を取得する（フォーマット：yyyyMM）
	 *
	 * @return 先月の日付
	 */
	public static String getLastMonthString() {
		Calendar c = Calendar.getInstance(timeZone);
		c.add(Calendar.MONTH, -1);
		return getFastDateFormatEx("yyyyMM").format(c.getTime());
	}

	/**
	 * 2つの日付の月数の差を求める.
	 * java.util.Date 型の日付 date1 - date2 が何ヵ月かを整数で返します。
	 * ※端数の日数は無視します。
	 *
	 * @param date1    日付1 java.util.Date
	 * @param date2    日付2 java.util.Date
	 * @return 2つの日付の月数の差
	 */
	public static int differenceMonth(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance(timeZone);
		cal1.setTime(date1);
		cal1.set(Calendar.DATE, 1);
		Calendar cal2 = Calendar.getInstance(timeZone);
		cal2.setTime(date2);
		cal2.set(Calendar.DATE, 1);
		int count = 0;
		if (cal1.before(cal2)) {
			while (cal1.before(cal2)) {
				cal1.add(Calendar.MONTH, 1);
				count--;
			}
		} else {
			count--;
			while (!cal1.before(cal2)) {
				cal1.add(Calendar.MONTH, -1);
				count++;
			}
		}
		return count;
	}

	/**
	 * 2つの日付の差を求める.
	 * java.util.Date 型の日付 date1 - date2 が何日かを返します。
	 *
	 * 計算方法は以下となります。
	 * 1.最初に2つの日付を long 値に変換します。
	 * 　※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの
	 * 　経過ミリ秒数となります。
	 * 2.次にその差を求めます。
	 * 3.上記の計算で出た数量を 1 日の時間で割ることで
	 * 　日付の差を求めることができます。
	 * 　※1 日 ( 24 時間) は、86,400,000 ミリ秒です。
	 *
	 * @param date1    日付 java.util.Date
	 * @param date2    日付 java.util.Date
	 * @return    2つの日付の差
	 */
	public static int differenceDays(Date date1,Date date2) {
		long datetime1 = date1.getTime();
		long datetime2 = date2.getTime();
		long one_date_time = 1000 * 60 * 60 * 24;
		long diffDays = (datetime1 - datetime2) / one_date_time;
		return (int)diffDays;
	}

	/**
	 * 2つの日付の時差を求める.
	 * java.util.Date 型の日付 date1 - date2 が何日かを返します。
	 *
	 * 計算方法は以下となります。
	 * 1.最初に2つの日付を long 値に変換します。
	 * 　※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの
	 * 　経過ミリ秒数となります。
	 * 2.次にその差を求めます。
	 * 3.上記の計算で出た数量を 1 日の時間で割ることで
	 * 　日付の差を求めることができます。
	 *
	 * @param date1    日付 java.util.Date
	 * @param date2    日付 java.util.Date
	 * @return    2つの日付の差
	 */
	public static int differenceHours(Date date1,Date date2) {
		long datetime1 = date1.getTime();
		long datetime2 = date2.getTime();
		long one_hour = 1000 * 60 * 60;
		long diffHours = (datetime1 - datetime2) / one_hour;
		return (int)diffHours;
	}

	/**
	 * 2つの日付の時差を求める.
	 * java.util.Date 型の日付 date1 - date2 が何日かを返します。
	 *
	 * 計算方法は以下となります。
	 * 1.最初に2つの日付を long 値に変換します。
	 * 　※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの
	 * 　経過ミリ秒数となります。
	 * 2.次にその差を求めます。
	 * 3.上記の計算で出た数量を 1 日の時間で割ることで
	 * 　日付の差を求めることができます。
	 *
	 * @param date1    日付 java.util.Date
	 * @param date2    日付 java.util.Date
	 * @return    2つの日付の差
	 */
	public static int differenceMinutes(Date date1,Date date2) {
		long datetime1 = date1.getTime();
		long datetime2 = date2.getTime();
		long one_minute = 1000 * 60;
		long diffMin = (datetime1 - datetime2) / one_minute;
		return (int)diffMin;
	}

	public static String getDifferenceTime(Date srcTime)
	{
		//SLog.log.info("getDifferenceTime srcTime = "+srcTime);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		calendar.setTime(srcTime);

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.JAPAN);
		df.setCalendar(calendar);

		Date now = new Date();

		int diffSeconds = 0;
		long miliTimeCrt = 0;
		long miliTimeNow = 0;

		miliTimeCrt = srcTime.getTime();
		miliTimeNow = now.getTime();

		// 差分計算 注：差の分数 = 1000 * 60 = 60000)
		diffSeconds = (int) ((miliTimeNow - miliTimeCrt) / 1000);

		String JstString = "";

		if (diffSeconds < 60)
		{
			JstString = String.format("%d秒前", diffSeconds);
		}
		else if (diffSeconds < 3600)
		{
			JstString = String.format("%d分前", diffSeconds / 60);
		}
		else if (diffSeconds < 86400)
		{
			JstString = String.format("%d時間前", diffSeconds / 3600);
		}
		//else if (diffSeconds < 2592000) 
		else if (diffSeconds < 604800) //TODO:30日は長いんで7日にしてみた
		{
			JstString = String.format("%d日前", diffSeconds / 86400);
		}
		else
		{
			//JstString = String.format("%dヶ月前", diffSeconds / 2592000);
			JstString = toString("yyyy/MM/dd HH:mm", srcTime);
		}

		return JstString;
	}

	public static Integer getAgeEx(Date birthDay)
	{
		Integer age = 0;

		if(birthDay==null)return age;

		Calendar now = Calendar.getInstance(timeZone);
		now.setTime(new Date());

		Calendar birth = Calendar.getInstance(timeZone);
		birth.setTime(birthDay);

		//まず西暦で年齢を求める
		age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

		//次に誕生日がきているかどうかで年齢-1を行う
		if (now.get(Calendar.MONTH) == birth.get(Calendar.MONTH))
		{
			//同月
			if (now.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))
			{
				//誕生日がまだきていないので-1
				age--;
			}
		}
		else if (now.get(Calendar.MONTH) < birth.get(Calendar.MONTH))
		{
			//誕生月がまだきていないので-1
			age--;
		}

		return age;
	}

}
