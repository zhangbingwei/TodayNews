package com.wesley.todaynews.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wesley.todaynews.R;
import com.wesley.todaynews.db.TodayNewsDB;

/**
 * 新闻详情页
 * 
 * @author zhangbingwei
 * 
 */
public class PhotosDetailActivity extends Activity {

	private TodayNewsDB mtodayNewsDB;

	private WebView wvNewsDetail;
	private ProgressBar pbProgress;

	private String photosUrl;// 新闻链接

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photos_detail);

		wvNewsDetail = (WebView) findViewById(R.id.wv_news_detail);
		pbProgress = (ProgressBar) findViewById(R.id.pb_progress);

		mtodayNewsDB = TodayNewsDB.getInstance(this);

		photosUrl = getIntent().getStringExtra("photosUrl");

		wvNewsDetail.getSettings().setJavaScriptEnabled(true);

		wvNewsDetail.loadUrl(photosUrl);// 加载网页
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

}
