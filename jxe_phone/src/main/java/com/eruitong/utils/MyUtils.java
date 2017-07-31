// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.utils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MyUtils {

	public MyUtils() {
	}

	/**
	 * 设备id
	 * 
	 * @param context
	 * @return
	 */
	public static String deviceId(Context context) {
		return ((TelephonyManager) context.getSystemService("phone"))
				.getDeviceId();
	}

	public static String getLine1Num(Context context) {
		return ((TelephonyManager) context.getSystemService("phone"))
				.getLine1Number();
	}

	/**
	 * 序列号
	 * 
	 * @param context
	 * @return
	 */
	public static String getSerialNum(Context context) {
		return ((TelephonyManager) context.getSystemService("phone"))
				.getSimSerialNumber();
	}

	public static boolean isEmpty(String s) {
		return null == s || "".equals(s) || s.length() == 0 || "null".equals(s);
	}

	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	/**
	 * 获取版本号
	 */
	public static int getVersionCode(Context context) {
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			int version = packInfo.versionCode;
			return version;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	/**
	 * 检查当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnect(Context activity) {
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 四舍五入
	 */
	public static String round(Double i) {
		return new BigDecimal(i).setScale(0, BigDecimal.ROUND_HALF_UP)
				.toString();
	}

	/**
	 * 向上取整
	 */
	public static String ceil(Double i) {
		return String.valueOf(Math.ceil(i));
	}

	public static String isNull(String s) {
		String s1;
		label0: {
			if (s != null && !"".equals(s)) {
				s1 = s;
				if (!s.equals("null")) {
					break label0;
				}
			}
			s1 = "";
		}
		return s1;
	}

	public static void showText(Context context, String s) {
		Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Map转get请求
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected String mapToGet(Map map) {
		StringBuilder stringbuilder = new StringBuilder();
		Iterator iterator = map.entrySet().iterator();
		do {
			if (!iterator.hasNext()) {
				return stringbuilder.toString();
			}
			@SuppressWarnings("rawtypes")
			java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
			stringbuilder.append((String) entry.getKey());
			stringbuilder.append("=");
			try {
				stringbuilder.append(URLEncoder.encode(
						(String) entry.getValue(), "utf-8"));
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			stringbuilder.append("&");
		} while (true);
	}

	/**
	 * 是否是河南
	 * @param str
	 * @return
	 */
	public static boolean isHenan(String str) {
		if (MyUtils.isEmpty(str)) {
			str = "";
		}
		if ("Henansheng".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
}
