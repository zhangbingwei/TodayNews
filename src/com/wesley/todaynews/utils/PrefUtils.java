package com.wesley.todaynews.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

	private static final String PREF_NAME = "config";

	public static int getPosition(Context context, String key, int defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	public static void setPosition(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

}
