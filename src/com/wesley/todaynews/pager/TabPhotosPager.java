package com.wesley.todaynews.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wesley.todaynews.R;
import com.wesley.todaynews.activity.PhotosDetailActivity;
import com.wesley.todaynews.domain.PhotosData;
import com.wesley.todaynews.domain.PhotosData.TabPhotos;

public class TabPhotosPager extends BasePager {

	private Handler mHandler;
	private int page = 1;
	private int size = 20;
	private int cid = 9;
	private int[] urlPosition = new int[] { 9, 32, 1, 23, 27 };
	private String photosURL;
	private ArrayList<TabPhotos> photosList;// 组图新闻集合
	private ListView lvTabPhotos;// ListView组图

	public TabPhotosPager(Activity activity, int i) {
		super(activity);
		cid = urlPosition[i];
	}

	@Override
	public View initViews() {
		View tabPhotos = View.inflate(mActivity, R.layout.activity_tab_photos,
				null);
		lvTabPhotos = (ListView) tabPhotos.findViewById(R.id.lv_tab_photos);

		return tabPhotos;
	}

	@Override
	public void initData() {
		photosURL = "http://apicloud.mob.com/wx/article/search?page=" + page
				+ "&cid=" + cid + "&key=14f82f5af6040&size=" + size;
		// System.out.println("获得的url是：" + photosURL);
		getDataFromServer();
	}

	/**
	 * 网络获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, photosURL, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				parseData(result);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(mActivity, "网络不可用，请重试", Toast.LENGTH_SHORT)
						.show();
				arg0.printStackTrace();
			}
		});

		/**
		 * ListView的点击事件，跳转到新闻详情页
		 */
		lvTabPhotos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(mActivity,
						PhotosDetailActivity.class);
				intent.putExtra("photosUrl", photosList.get(arg2).sourceUrl);
				mActivity.startActivity(intent);
			}
		});
	}

	// 解析数据
	protected void parseData(String result) {
		Gson gson = new Gson();
		PhotosData photosData = gson.fromJson(result, PhotosData.class);
		int curPage = photosData.result.curPage;
		int totalPage = photosData.result.total;
		photosList = photosData.result.list;

		// System.out.println("当前：" + curPage + "，总共：" + totalPage);
		// System.out.println("获得的数据集合:" + photosList);
		if (photosList != null) {
			lvTabPhotos.setAdapter(new tabPhotosAdapter());
		}
	}

	/**
	 * ListView适配器
	 * 
	 * @author zhangbingwei
	 * 
	 */
	class tabPhotosAdapter extends BaseAdapter {
		private BitmapUtils bitmap;

		private tabPhotosAdapter() {
			bitmap = new BitmapUtils(mActivity);
			bitmap.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return photosList.size();
		}

		@Override
		public TabPhotos getItem(int position) {
			return photosList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_tab_photos,
						null);
				holder = new ViewHolder();
				holder.ivPhoto = (ImageView) convertView
						.findViewById(R.id.iv_photo);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TabPhotos item = getItem(position);
			holder.tvTitle.setText(item.title);
			holder.tvDate.setText(item.pubTime);
			if (!TextUtils.isEmpty(item.thumbnails)) {
				String itemUrl = item.thumbnails.split("\\$")[0];
				bitmap.display(holder.ivPhoto, itemUrl);
			}

			return convertView;
		}
	}

	static class ViewHolder {
		ImageView ivPhoto;
		TextView tvTitle;
		TextView tvDate;
	}
}
