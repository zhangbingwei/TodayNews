package com.wesley.todaynews.domain;

import java.util.ArrayList;

public class NewsSearchData {

	public int error_code;
	public String reason;
	public ArrayList<NewsTabData> result;

	public class NewsTabData {
		public String title;
		public String pdate_src;
		public String src;
		public String content;
		public String url;

	}

}
