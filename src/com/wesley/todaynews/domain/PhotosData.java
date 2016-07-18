package com.wesley.todaynews.domain;

import java.util.ArrayList;

public class PhotosData {

	public String msg;
	public String retCode;
	public Result result;

	public class Result {
		public int curPage;// 当前第几页
		public int total;// 总共多少页
		public ArrayList<TabPhotos> list;

	}

	public class TabPhotos {
		public String id;// 新闻id
		public String cid;// 所属的页签id
		public String pubTime;
		public String title;
		public String thumbnails;// 图片链接
		public String sourceUrl;// 新闻链接

	}

}
