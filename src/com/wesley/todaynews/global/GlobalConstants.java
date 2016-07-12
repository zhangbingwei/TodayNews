package com.wesley.todaynews.global;

public class GlobalConstants {

	public static final String SERVER_URL = "http://v.juhe.cn/toutiao/index";
	public static final String KEY = "&key=9765053dfd77749dca80138c8eb5323a";

	public static String getURL(String type) {
		String URL = SERVER_URL + "?type=" + type + KEY;
		return URL;
	}
}
