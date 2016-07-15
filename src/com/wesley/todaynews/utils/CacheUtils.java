package com.wesley.todaynews.utils;

import android.content.Context;

public class CacheUtils {

	/**
	 * 设置缓存 key 是url, value是json
	 */
	public static void setCache(String key, String value, Context ctx) {
		PrefUtils.setString(ctx, key, value);
	}

	/**
	 * 获取缓存 key 是url
	 */
	public static String getCache(String key, Context ctx) {
		return PrefUtils.getString(ctx, key, null);
	}

}
