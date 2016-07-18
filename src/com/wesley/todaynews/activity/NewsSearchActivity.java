package com.wesley.todaynews.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wesley.todaynews.R;
import com.wesley.todaynews.domain.NewsSearchData;
import com.wesley.todaynews.domain.NewsSearchData.NewsTabData;

/**
 * 新闻搜索页面
 * 
 * @author zhangbingwei
 * 
 */
public class NewsSearchActivity extends Activity {

	private ImageView ivSearch;
	private EditText etContent;
	private ListView lvContent;
	private String SERVER = "http://op.juhe.cn/onebox/news/query?key=d3b4e6d940aa7fa5a772bf86c7284e3d"
			+ "&dtype=&q=";
	private ArrayList<NewsTabData> newsList;// 搜索的新闻列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_news);

		initView();
		initData();
	}

	private void initView() {
		ivSearch = (ImageView) findViewById(R.id.iv_search);
		etContent = (EditText) findViewById(R.id.et_content);
		lvContent = (ListView) findViewById(R.id.lv_content);
	}

	private void initData() {

		ivSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String content = etContent.getText().toString();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(NewsSearchActivity.this, "关键词不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					String URL = SERVER + content;

					getDataFromServer(URL);
					etContent.setText("");
				}

			}
		});

		lvContent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				NewsTabData data = newsList.get(position);
				Intent intent = new Intent(NewsSearchActivity.this,
						SearchNewsDetailActivity.class);
				intent.putExtra("newsUrl", data.url);
				startActivity(intent);
			}
		});
	}

	/**
	 * 网络获取数据
	 */
	protected void getDataFromServer(String url) {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> response) {
				String result = response.result;
				parseData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}

		});
	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 */
	protected void parseData(String result) {
		Gson gson = new Gson();
		NewsSearchData NewsData = gson.fromJson(result, NewsSearchData.class);
		newsList = NewsData.result;
		// System.out.println("结果是：" + newsList);
		if (newsList != null) {
			lvContent.setAdapter(new NewsSearchAdapter());
		} else {
			Toast.makeText(this, "没有搜到相关新闻，请重新搜索", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 新闻列表适配器
	 * 
	 * @author zhangbingwei
	 * 
	 */
	class NewsSearchAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return newsList.size();
		}

		@Override
		public NewsTabData getItem(int position) {
			return newsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(NewsSearchActivity.this,
						R.layout.item_search_news, null);
				holder = new ViewHolder();

				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tvSource = (TextView) convertView
						.findViewById(R.id.tv_source);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_time);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			NewsTabData item = getItem(position);
			holder.tvTitle.setText(item.title);
			holder.tvContent.setText(item.content);
			holder.tvSource.setText(item.src);
			holder.tvTime.setText(item.pdate_src);

			return convertView;
		}

	}

	static class ViewHolder {
		TextView tvTitle;
		TextView tvContent;
		TextView tvSource;
		TextView tvTime;
	}
}
