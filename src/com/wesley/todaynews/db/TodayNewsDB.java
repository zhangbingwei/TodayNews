package com.wesley.todaynews.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wesley.todaynews.domain.NewsData;
import com.wesley.todaynews.domain.NewsData.NewsTabData;

public class TodayNewsDB {

	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private static TodayNewsDB mTodayNewsDB;

	private TodayNewsDB(Context context) {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
		// 清除数据库中的数据
		// dbHelper.onUpgrade(db, 1, 1);
	}

	// 同步：创建数据库实例
	public synchronized static TodayNewsDB getInstance(Context context) {
		if (mTodayNewsDB == null) {
			mTodayNewsDB = new TodayNewsDB(context);
		}
		return mTodayNewsDB;
	}

	/**
	 * 把阅读过的新闻url保存到数据库的news_read表
	 * 
	 * @param news_id
	 */
	public void saveReadURL(String news_url) {
		if (news_url != null) {
			ContentValues values = new ContentValues();
			values.put("news_url", news_url);
			db.insert("news_read", null, values);
		}
	}

	/**
	 * 获取度过的所有新闻的id
	 */
	public List<String> loadAllReadURL() {
		List<String> newsUrl_list = new ArrayList<String>();
		Cursor cursor = db.query("news_read", null, null, null, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				String newsUrl = cursor.getString(1);
				newsUrl_list.add(newsUrl);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return newsUrl_list;
	}

	/**
	 * 保存收藏的文章数据到数据库
	 * 
	 * @param news
	 */
	public void saveFavourite(NewsTabData data) {
		ContentValues values = new ContentValues();
		values.put("news_title", data.title);
		values.put("news_image", data.thumbnail_pic_s);
		values.put("news_date", data.date);
		values.put("news_source", data.author_name);
		values.put("news_url", data.url);
		db.insert("news_fav", null, values);
	}

	/**
	 * 获取所有保存的新闻对象列表
	 * 
	 * @return
	 */
	public List<NewsTabData> loadFavourite() {
		List<NewsTabData> favouriteList = new ArrayList<NewsTabData>();
		Cursor cursor = db
				.query("news_fav", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				NewsTabData news = new NewsData().new NewsTabData();
				news.setTitle(cursor.getString(cursor
						.getColumnIndex("news_title")));
				news.setThumbnail_pic_s(cursor.getString(cursor
						.getColumnIndex("news_image")));
				news.setDate(cursor.getString(cursor
						.getColumnIndex("news_date")));
				news.setAuthor_name(cursor.getString(cursor
						.getColumnIndex("news_source")));
				news.setUrl(cursor.getString(cursor.getColumnIndex("news_url")));
				favouriteList.add(news);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return favouriteList;
	}

	/**
	 * 根据新闻链接判断新闻是否为已收藏
	 * 
	 * @param news
	 * @return
	 */
	public boolean isFavourite(String url) {
		Cursor cursor = db.query("news_fav", null, "news_url = ?",
				new String[] { url }, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除收藏的文章
	 * 
	 * @param news
	 */
	public void deleteFavourite(String url) {
		if (url != null) {
			db.delete("news_fav", "news_url = ?", new String[] { url });
		}
	}

	public synchronized void closeDB() {
		if (mTodayNewsDB != null) {
			db.close();
		}
	}

	/**
	 * 保存账号到数据库的user表中
	 * 
	 * @param news
	 */
	public void saveUser(String name, String pass) {
		ContentValues values = new ContentValues();
		values.put("user_name", name);
		values.put("user_pass", pass);
		db.insert("user", null, values);
	}

	/**
	 * 判断账号是否存在
	 * 
	 * @param news
	 * @return
	 */
	public boolean isNameExists(String name) {
		Cursor cursor = db.query("user", null, "user_name = ?",
				new String[] { name }, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 只允许一个账号
	 * 
	 * @param news
	 * @return
	 */
	public boolean isOnlyOne(String name) {
		Cursor cursor = db.query("user", null, null, null, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取密码
	 * 
	 * @param name
	 * @return
	 */
	public String getPass() {
		Cursor cursor = db.query("user", null, null, null, null, null, null);
		if (cursor.moveToNext()) {
			String pass = cursor.getString(cursor.getColumnIndex("user_pass"));
			return pass;
		} else {
			return null;
		}

	}

	/**
	 * 获取账号
	 * 
	 * @return
	 */
	public String getName() {
		Cursor cursor = db.query("user", null, null, null, null, null, null);
		if (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("user_name"));
			return name;
		} else {
			return null;
		}
	}

}
