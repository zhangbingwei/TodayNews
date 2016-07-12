package com.wesley.todaynews.domain;

import java.util.ArrayList;

public class NewsData {

	public int error_code;
	public String reason;
	public Result result;

	public class Result {
		public String stat;
		public ArrayList<NewsTabData> data;

	}

	public class NewsTabData {
		public String author_name; // 新闻来源：趣新闻，成长快乐等
		public String date; // 新闻时间
		public String thumbnail_pic_s;// 新闻图片链接
		public String thumbnail_pic_s03;// 新闻大图片链接
		public String title;// 新闻标题
		public String url;// 新闻链接

	}

}
