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
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
			showShare();
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

	/**
	 * 分享功能
	 */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		// oks.setTitle(getString(R.string.share));
		oks.setTitle(getString(R.string.app_name));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
}
