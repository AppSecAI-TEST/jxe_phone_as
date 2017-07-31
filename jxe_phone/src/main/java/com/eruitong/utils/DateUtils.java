// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static Calendar calendar = null;
	public static Date date = null;
	public static DateFormat dateFormat = null;

	public DateUtils() {
	}

	public static Date diffDate(Date date1, int i) {
		calendar = Calendar.getInstance();
		long l = getMillis(date1);
		long l1 = i;
		calendar.setTimeInMillis(l - l1 * 24L * 3600L * 1000L);
		return calendar.getTime();
	}

	public static String format(Date date1, String s) {
		String s1 = "";
		if (date1 != null) {
			try {
				dateFormat = new SimpleDateFormat(s);
				s1 = dateFormat.format(date1);
			}
			// Misplaced declaration of an exception variable
			catch (Exception e) {
				return "";
			}
		}
		return s1;
	}

	public static String getDateTime1(Date date1) {
		return format(date1, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getDateTime2(Date date1) {
		return format(date1, "yyyyMMddHHmmss");
	}

	public static String getFormatFileTime() {
		return getDateTime2(new Date());
	}

	public static String getFormatNowTime() {
		return getDateTime1(new Date());
	}

	public static long getMillis(Date date1) {
		calendar = Calendar.getInstance();
		calendar.setTime(date1);
		return calendar.getTimeInMillis();
	}

	public String getNowDate() {
		return (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
	}

}
