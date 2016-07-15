package com.wesley.todaynews.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

	private static final String PREF_NAME = "config";

	// 获取整数值
	public static int getPosition(Context context, String key, int defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	// 设置整数值
	public static void setPosition(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}

	// 获取布尔值
	public static boolean getBoolean(Context context, String key,
			boolean defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	// 设置布尔值
	public static void setBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	// 获取字符串
	public static String getString(Context context, String key,
			String defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	// 设置字符串
	public static void setString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

}
