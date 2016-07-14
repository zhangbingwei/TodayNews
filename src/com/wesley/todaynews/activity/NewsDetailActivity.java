package com.wesley.todaynews.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wesley.todaynews.R;
import com.wesley.todaynews.db.TodayNewsDB;
import com.wesley.todaynews.domain.NewsData;
import com.wesley.todaynews.domain.NewsData.NewsTabData;
import com.wesley.todaynews.utils.PrefUtils;

/**
 * 新闻详情页
 * 
 * @author zhangbingwei
 * 
 */
public class NewsDetailActivity extends Activity implements OnClickListener {

	private TodayNewsDB mtodayNewsDB;

	private WebView wvNewsDetail;
	private ProgressBar pbProgress;
	private ImageButton ibBack;
	private ImageButton ibSize;
	private ImageButton ibShare;
	private ImageButton ibFavourite;

	private int mCurrentChooseItem;// 记录当前选中的item, 点击确定前
	private int mCurrentItem;// 记录当前选中的item, 点击确定后

	private NewsTabData newsTabData;// 新闻对象

	private String newsUrl;// 新闻链接

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		wvNewsDetail = (WebView) findViewById(R.id.wv_news_detail);
		pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
		ibBack = (ImageButton) findViewById(R.id.ib_back);
		ibSize = (ImageButton) findViewById(R.id.ib_size);
		ibShare = (ImageButton) findViewById(R.id.ib_share);
		ibFavourite = (ImageButton) findViewById(R.id.ib_favourite);

		mtodayNewsDB = TodayNewsDB.getInstance(this);

		ibBack.setOnClickListener(this);
		ibFavourite.setOnClickListener(this);
		ibSize.setOnClickListener(this);
		ibShare.setOnClickListener(this);

		newsUrl = getIntent().getStringExtra("newsUrl");
		String title = getIntent().getStringExtra("newsTitle");
		String author_name = getIntent().getStringExtra("newsSource");
		String thumbnail_pic_s = getIntent().getStringExtra("newsImage");
		String date = getIntent().getStringExtra("newsDate");
		newsTabData = new NewsData().new NewsTabData(title, author_name, date,
				thumbnail_pic_s, newsUrl);

		if (mtodayNewsDB.isFavourite(newsUrl)) {
			// 收藏
			ibFavourite.setBackgroundResource(R.drawable.save_press);
		} else {
			ibFavourite.setBackgroundResource(R.drawable.save);
		}

		wvNewsDetail.loadUrl(newsUrl);
		wvNewsDetail.getSettings().setJavaScriptEnabled(true);

		// 进来根据保存的字体大小来设置字体
		int position = PrefUtils.getPosition(this, "text_size", 2);
		mCurrentItem = position;
		getSize(position);

		wvNewsDetail.loadUrl(newsUrl);// 加载网页
		wvNewsDetail.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				pbProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pbProgress.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});

	}

	/**
	 * 处理新闻底部的按钮事件
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 返回到主页
		case R.id.ib_back:
			finish();
			break;
		// 收藏功能
		case R.id.ib_favourite:
			if (!mtodayNewsDB.isFavourite(newsUrl)) {
				// 收藏
				mtodayNewsDB.saveFavourite(newsTabData);
				ibFavourite.setBackgroundResource(R.drawable.save_press);
				Toast.makeText(this, "收藏文章成功", Toast.LENGTH_SHORT).show();
			} else {
				mtodayNewsDB.deleteFavourite(newsUrl);
				ibFavourite.setBackgroundResource(R.drawable.save);
				Toast.makeText(this, "删除收藏成功", Toast.LENGTH_SHORT).show();
			}

			break;
		// 字体设置
		case R.id.ib_size:
			changeTextSize();
			break;
		// 分享功能
		case R.id.ib_share:

			break;

		default:
			break;
		}
	}

	/**
	 * 选择字体大小
	 */
	public void changeTextSize() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };
		builder.setTitle("字体设置");

		builder.setSingleChoiceItems(items, mCurrentItem,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mCurrentChooseItem = which;
						System.out.println("选择的位置是：" + mCurrentChooseItem);
					}
				});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				getSize(mCurrentChooseItem);

				mCurrentItem = mCurrentChooseItem;
				// 把选择的字体保存到sp中
				PrefUtils.setPosition(NewsDetailActivity.this, "text_size",
						mCurrentItem);
			}
		});

		builder.setNegativeButton("取消", null);

		builder.show();
	}

	/**
	 * 根据位置获取对应的字体大小
	 * 
	 * @param position
	 */
	public void getSize(int position) {
		WebSettings settings = wvNewsDetail.getSettings();
		switch (position) {
		case 0:
			settings.setTextSize(TextSize.LARGEST);
			break;
		case 1:
			settings.setTextSize(TextSize.LARGER);
			break;
		case 2:
			settings.setTextSize(TextSize.NORMAL);
			break;
		case 3:
			settings.setTextSize(TextSize.SMALLER);
			break;
		case 4:
			settings.setTextSize(TextSize.SMALLEST);
			break;

		default:
			break;
		}
	}
}
