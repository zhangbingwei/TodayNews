package com.wesley.todaynews.pager;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wesley.todaynews.R;
import com.wesley.todaynews.activity.FavouriteActivity;
import com.wesley.todaynews.activity.LoginActivity;
import com.wesley.todaynews.activity.NewsSearchActivity;
import com.wesley.todaynews.activity.WeatherActivity;
import com.wesley.todaynews.db.TodayNewsDB;

/**
 * 设置页面
 * 
 * @author zhangbingwei
 * 
 */
public class SettingPager extends BasePager {

	private TodayNewsDB mNewsDB;

	private View settingView;
	private TextView tvFavourite;
	private TextView tvLogin;
	private TextView tvNews;
	private TextView tvWeather;
	private TextView tvSafe;
	private TextView tvAbout;
	private TextView tvWeek;
	private TextView tvUsername;

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		settingView = View.inflate(mActivity, R.layout.activity_settings, null);
		tvFavourite = (TextView) settingView.findViewById(R.id.tv_favourite);
		tvLogin = (TextView) settingView.findViewById(R.id.tv_login);
		tvNews = (TextView) settingView.findViewById(R.id.tv_news);
		tvWeather = (TextView) settingView.findViewById(R.id.tv_weather);
		tvSafe = (TextView) settingView.findViewById(R.id.tv_safe);
		tvAbout = (TextView) settingView.findViewById(R.id.tv_about);
		tvWeek = (TextView) settingView.findViewById(R.id.tv_week);
		tvUsername = (TextView) settingView.findViewById(R.id.tv_username);

		// 从数据库获取账号展示
		mNewsDB = TodayNewsDB.getInstance(mActivity);
		String name = mNewsDB.getName();
		tvUsername.setText("账号：" + name);

		return settingView;
	}

	@Override
	public void initData() {
		// 跳转到登录页面
		tvLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mActivity.startActivity(new Intent(mActivity,
						LoginActivity.class));
			}
		});

		// 跳转到收藏页面
		tvFavourite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity, FavouriteActivity.class);
				mActivity.startActivity(intent);
			}
		});

		// 跳转天气页面
		tvWeather.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mActivity.startActivity(new Intent(mActivity,
						WeatherActivity.class));
			}
		});

		tvWeek.setText(getDate() + "  " + getWeek());

		// 新闻搜索页面
		tvNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mActivity.startActivity(new Intent(mActivity,
						NewsSearchActivity.class));
			}
		});

		// 弹出修改密码对话框
		tvSafe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
				final AlertDialog dialog = builder.create();
				View changePass = View.inflate(mActivity,
						R.layout.activity_change_pass, null);
				dialog.setView(changePass, 0, 0, 0, 0);
				dialog.show();
				Button ok = (Button) changePass.findViewById(R.id.bt_login);
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Toast.makeText(mActivity, "修改密码功能", 0).show();
						dialog.dismiss();
					}
				});

			}
		});

		// 关于口袋新闻
		tvAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
				final AlertDialog dialog = builder.create();
				View changePass = View.inflate(mActivity, R.layout.about_me,
						null);
				dialog.setView(changePass, 0, 0, 0, 0);
				dialog.show();
			}
		});

	}

	// 获取星期
	private String getWeek() {
		Date date = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		return dateFm.format(date);
	}

	// 获取时间
	private String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
	}

}
