package com.wesley.todaynews.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.wesley.todaynews.pager.TabNewsPager;

/**
 * 新闻详情页
 * 
 * @author zhangbingwei
 * 
 */
public class NewsDetailActivity extends Activity implements OnClickListener {

	private WebView wvNewsDetail;
	private ProgressBar pbProgress;
	private ImageButton ibBack;
	private ImageButton ibSize;
	private ImageButton ibShare;

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

		ibBack.setOnClickListener(this);
		ibSize.setOnClickListener(this);
		ibShare.setOnClickListener(this);

		// 取出传递过来的新闻链接
		String newsUrl = getIntent().getStringExtra("newsUrl");

		wvNewsDetail.loadUrl(newsUrl);
		wvNewsDetail.getSettings().setJavaScriptEnabled(true);

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
		case R.id.ib_size:

			break;
		case R.id.ib_share:

			break;

		default:
			break;
		}
	}
}
