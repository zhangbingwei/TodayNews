package com.wesley.todaynews.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.wesley.todaynews.R;
import com.wesley.todaynews.db.TodayNewsDB;
import com.wesley.todaynews.domain.NewsData.NewsTabData;
import com.wesley.todaynews.view.SlideCutListView;
import com.wesley.todaynews.view.SlideCutListView.RemoveDirection;
import com.wesley.todaynews.view.SlideCutListView.RemoveListener;

/**
 * 收藏页面
 * 
 * @author zhangbingwei
 * 
 */
public class FavouriteActivity extends Activity implements RemoveListener {

	private TodayNewsDB mTodayNewsDB;
	private SlideCutListView lvFavourite;
	private List<NewsTabData> favouriteList;// 收藏的文章列表
	private FavouriteAdapter adapter;// 收藏列表适配器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_favourite);
		mTodayNewsDB = TodayNewsDB.getInstance(this);
		initView();
		initData();
	}

	private void initView() {
		lvFavourite = (SlideCutListView) findViewById(R.id.lv_favourite);
	}

	private void initData() {
		lvFavourite.setRemoveListener(this);// 设置滑动监听

		favouriteList = mTodayNewsDB.loadFavourite();
		adapter = new FavouriteAdapter();

		if (favouriteList != null) {
			lvFavourite.setAdapter(adapter);
		}

		lvFavourite.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				NewsTabData newsTabData = favouriteList.get(position);

				// 收藏列表跳转到新闻详情页，把新闻链接url传递过去
				Intent intent = new Intent(FavouriteActivity.this,
						NewsDetailActivity.class);
				intent.putExtra("newsUrl", newsTabData.url);
				intent.putExtra("newsTitle", newsTabData.title);
				intent.putExtra("newsSource", newsTabData.author_name);
				intent.putExtra("newsImage", newsTabData.thumbnail_pic_s);
				intent.putExtra("newsDate", newsTabData.date);
				startActivity(intent);
			}
		});

	}

	/**
	 * 收藏列表适配器
	 * 
	 * @author zhangbingwei
	 * 
	 */
	class FavouriteAdapter extends BaseAdapter {
		private BitmapUtils bitmap;

		// 在适配器初始化的时候给他设置默认的图片
		public FavouriteAdapter() {
			bitmap = new BitmapUtils(FavouriteActivity.this);
			bitmap.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return favouriteList.size();
		}

		@Override
		public NewsTabData getItem(int position) {
			return favouriteList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(FavouriteActivity.this,
						R.layout.item_fav_news, null);
				holder = new ViewHolder();
				holder.ivPhoto = (ImageView) convertView
						.findViewById(R.id.iv_photo);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvFrom = (TextView) convertView
						.findViewById(R.id.tv_from);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			NewsTabData item = getItem(position);

			holder.tvTitle.setText(item.title);
			holder.tvDate.setText(item.date);

			if (!TextUtils.isEmpty(item.author_name)) {
				holder.tvFrom.setText(item.author_name);
			} else {
				holder.tvFrom.setText("...");
			}
			bitmap.display(holder.ivPhoto, item.thumbnail_pic_s);

			return convertView;
		}

	}

	static class ViewHolder {
		ImageView ivPhoto;
		TextView tvTitle;
		TextView tvFrom;
		TextView tvDate;
	}

	// 实现删除item
	@Override
	public void removeItem(RemoveDirection direction, int position) {
		// adapter.getItem(position);
		Toast.makeText(this, "成功删除收藏文章", Toast.LENGTH_SHORT).show();
		mTodayNewsDB.deleteFavourite(favouriteList.get(position).url);
		favouriteList = mTodayNewsDB.loadFavourite();
		adapter.notifyDataSetChanged();
	}

}
