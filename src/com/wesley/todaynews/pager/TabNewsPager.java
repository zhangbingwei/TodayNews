package com.wesley.todaynews.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;
import com.wesley.todaynews.R;
import com.wesley.todaynews.activity.NewsDetailActivity;
import com.wesley.todaynews.db.TodayNewsDB;
import com.wesley.todaynews.domain.NewsData;
import com.wesley.todaynews.domain.NewsData.NewsTabData;
import com.wesley.todaynews.global.GlobalConstants;
import com.wesley.todaynews.view.RefreshListView;
import com.wesley.todaynews.view.RefreshListView.OnRefreshListener;

public class TabNewsPager extends BasePager {

	private RefreshListView lvTabNews;// 页签下面的新闻ViewPager

	private String[] typeList = new String[] { "top", "shehui", "guonei",
			"guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang" }; // 网络数据对应的type

	private String type;// 根据页签传递过来的位置获取对应的页签类型

	private String url;// 根据页签类型获取对应的url

	private ArrayList<NewsTabData> dataList;// 页签具体的新闻集合

	private ArrayList<NewsTabData> dataTopList;// 页签的头条新闻集合，取页签新闻集合的后四条
	private ViewPager vpTopNews;
	private TextView tvTopTitle;
	private CirclePageIndicator mIndicator;

	private TodayNewsDB mTodayNewsDB;// 数据库

	private TabNewsAdapter newsAdapter;// 新闻列表适配器

	private Handler mHandler;

	public TabNewsPager(Activity activity, int position) {
		super(activity);
		type = typeList[position];
	}

	@Override
	public View initViews() {
		View tabNews = View
				.inflate(mActivity, R.layout.activity_tab_news, null);
		lvTabNews = (RefreshListView) tabNews.findViewById(R.id.lv_tab_news);
		// 头条新闻的ViewPager
		View topNewsView = View.inflate(mActivity,
				R.layout.activity_tab_news_top, null);
		lvTabNews.addHeaderView(topNewsView);
		vpTopNews = (ViewPager) topNewsView.findViewById(R.id.vp_top_news);
		tvTopTitle = (TextView) topNewsView.findViewById(R.id.tv_top_title);
		mIndicator = (CirclePageIndicator) topNewsView
				.findViewById(R.id.indicator);

		mTodayNewsDB = TodayNewsDB.getInstance(mActivity);

		// 设置下拉刷新监听
		lvTabNews.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				// 由于没有更多的新闻数据，所以直接不显示
				lvTabNews.onRefreshComplete(false);// 收起加载更多的布局

			}
		});

		return tabNews;
	}

	@Override
	public void initData() {
		url = GlobalConstants.getURL(type);
		getDataFromServer();

		// 给ListView设置点击事件，跳到新闻详情页
		lvTabNews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				NewsTabData newsTabData = dataList.get(position);

				// 跳转到新闻详情页，把新闻链接url传递过去
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("newsUrl", newsTabData.url);
				intent.putExtra("newsTitle", newsTabData.title);
				intent.putExtra("newsSource", newsTabData.author_name);
				intent.putExtra("newsImage", newsTabData.thumbnail_pic_s);
				intent.putExtra("newsDate", newsTabData.date);
				mActivity.startActivity(intent);

				// 在这里记录已经阅读新闻
				if (!mTodayNewsDB.loadAllReadURL().contains(newsTabData.url)) {
					mTodayNewsDB.saveReadURL(newsTabData.url);
				}

				// newsAdapter.notifyDataSetChanged();
				changeReadState(view);// 实现局部界面刷新, 这个view就是被点击的item布局对象
			}
		});
	}

	/**
	 * 改变已读新闻的颜色
	 */
	private void changeReadState(View view) {
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(Color.GRAY);
	}

	/**
	 * 从网络根据url获取数据
	 */
	public void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				parseData(result, false);

				lvTabNews.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();

				lvTabNews.onRefreshComplete(false);
			}

		});
	}

	/**
	 * 解析从网络获取的Json新闻数据
	 * 
	 * @param result
	 */
	protected void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		NewsData newsData = gson.fromJson(result, NewsData.class);

		dataList = newsData.result.data;
		newsAdapter = new TabNewsAdapter();

		if (dataList != null) {
			lvTabNews.setAdapter(newsAdapter);
		}

		if (type == typeList[0]) {
			// 取出新闻列表的最后四条新闻当做头条新闻展示在最顶部
			dataTopList = new ArrayList<NewsData.NewsTabData>();

			dataTopList.add(dataList.get(dataList.size() - 1));
			dataTopList.add(dataList.get(dataList.size() - 3));
			dataTopList.add(dataList.get(dataList.size() - 5));
			dataTopList.add(dataList.get(dataList.size() - 7));
		} else {
			// 取出新闻列表的最后2条新闻当做头条新闻展示在最顶部
			dataTopList = new ArrayList<NewsData.NewsTabData>();
			dataTopList.add(dataList.get(dataList.size() - 1));
			dataTopList.add(dataList.get(dataList.size() - 3));
		}

		if (dataTopList != null) {
			vpTopNews.setAdapter(new TopNewsAdapter());

			mIndicator.setViewPager(vpTopNews);
			mIndicator.setSnap(true);// 支持快照显示
			mIndicator.onPageSelected(0);// 让指示器重新定位到第一个
			tvTopTitle.setText(dataTopList.get(0).title);// 默认设置第一个新闻标题
			mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					// 根据滑动的页面切换标题
					tvTopTitle.setText(dataTopList.get(arg0).title);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
		}

		// 自动轮播条显示
		if (mHandler == null) {
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {

					int currentItem = vpTopNews.getCurrentItem();

					if (currentItem < dataTopList.size() - 1) {
						currentItem++;
					} else {
						currentItem = 0;
					}

					vpTopNews.setCurrentItem(currentItem);// 切换到下一个页面
					mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,形成死循环
				}
			};
		}

		mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发消息

	}

	/**
	 * 页签新闻列表适配器
	 * 
	 * @author zhangbingwei
	 * 
	 */
	class TabNewsAdapter extends BaseAdapter {
		private BitmapUtils bitmap;

		// 在适配器初始化的时候给他设置默认的图片
		public TabNewsAdapter() {
			bitmap = new BitmapUtils(mActivity);
			bitmap.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public NewsTabData getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_tab_news,
						null);
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

			if (mTodayNewsDB.loadAllReadURL().contains(item.url)) {
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}

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

	/**
	 * 头条新闻适配器
	 * 
	 * @author zhangbingwei
	 * 
	 */
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils bitmap;

		public TopNewsAdapter() {
			bitmap = new BitmapUtils(mActivity);
			bitmap.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return dataTopList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ImageView image = new ImageView(mActivity);
			image.setScaleType(ScaleType.FIT_XY);

			NewsTabData topNews = dataTopList.get(position);
			final String topNewsUrl = topNews.url;
			bitmap.display(image, topNews.thumbnail_pic_s);
			// 设置ViewPager的点击事件
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mActivity,
							NewsDetailActivity.class);
					intent.putExtra("newsUrl", topNewsUrl);
					mActivity.startActivity(intent);
				}
			});

			((ViewGroup) container).addView(image);
			return image;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewGroup) container).removeView((View) object);
		}

	}

}
