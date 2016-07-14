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

		public NewsTabData() {
			super();
		}

		public NewsTabData(String title, String author_name, String date,
				String thumbnail_pic_s, String url) {
			super();
			this.author_name = author_name;
			this.date = date;
			this.thumbnail_pic_s = thumbnail_pic_s;
			this.title = title;
			this.url = url;
		}

		public String getAuthor_name() {
			return author_name;
		}

		public void setAuthor_name(String author_name) {
			this.author_name = author_name;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getThumbnail_pic_s() {
			return thumbnail_pic_s;
		}

		public void setThumbnail_pic_s(String thumbnail_pic_s) {
			this.thumbnail_pic_s = thumbnail_pic_s;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

}
