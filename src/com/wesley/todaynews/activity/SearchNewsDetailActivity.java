package com.wesley.todaynews.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.wesley.todaynews.R;
import com.wesley.todaynews.db.TodayNewsDB;

/**
 * 新闻详情页
 * 
 * @author zhangbingwei
 * 
 */
public class SearchNewsDetailActivity extends Activity implements
		OnClickListener {

	private TodayNewsDB mtodayNewsDB;

	private WebView wvNewsDetail;
	private ProgressBar pbProgress;
	private ImageButton ibBack;
	private ImageButton ibShare;
	private ImageButton ibFavourite;

	private String newsUrl;// 新闻链接

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_search_detail);

		wvNewsDetail = (WebView) findViewById(R.id.wv_news_detail);
		pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
		ibBack = (ImageButton) findViewById(R.id.ib_back);

		ibBack.setOnClickListener(this);

		newsUrl = getIntent().getStringExtra("newsUrl");

		wvNewsDetail.loadUrl(newsUrl);
		wvNewsDetail.getSettings().setJavaScriptEnabled(true);
		wvNewsDetail.getSettings().setBuiltInZoomControls(true);
		wvNewsDetail.getSettings().setSupportZoom(true);

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

		default:
			break;
		}
	}

}
