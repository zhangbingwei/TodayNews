package com.wesley.todaynews.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.wesley.todaynews.R;
import com.wesley.todaynews.pager.BasePager;
import com.wesley.todaynews.pager.NewsPager;
import com.wesley.todaynews.pager.PhotosPager;
import com.wesley.todaynews.pager.SettingPager;

public class MainActivity extends Activity {

	private RadioGroup rgGroup;
	private ViewPager vpContent;

	private ArrayList<BasePager> mPagerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();

		initData();
	}

	private void initView() {
		rgGroup = (RadioGroup) findViewById(R.id.rg_group);
		vpContent = (ViewPager) findViewById(R.id.vp_content);
	}

	private void initData() {
		rgGroup.check(R.id.rb_news);// 默认勾选首页

		// 初始化3个子页面
		mPagerList = new ArrayList<BasePager>();

		mPagerList.add(new NewsPager(this));
		mPagerList.add(new PhotosPager(this));
		mPagerList.add(new SettingPager(this));

		vpContent.setAdapter(new ContentAdapter());

		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_news:
					vpContent.setCurrentItem(0, false);
					break;
				case R.id.rb_photos:
					vpContent.setCurrentItem(1, false);
					break;
				case R.id.rb_settings:
					vpContent.setCurrentItem(2, false);
					break;

				default:
					break;
				}
			}
		});

		mPagerList.get(0).initData();// 初始化首页新闻的数据
		vpContent.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mPagerList.get(arg0).initData();// 获取当前被选中的页面，初始化该页面的数据

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	/**
	 * 内容ViewPager的适配器
	 * 
	 * @author zhangbingwei
	 * 
	 */
	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			BasePager pager = mPagerList.get(position);
			((ViewGroup) container).addView(pager.mRootView);
			// pager.initData();// 初始化数据.... 不要放在此处初始化数据, 否则会预加载下一个页面
			return pager.mRootView;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
	}

	private long exitTime = 0;

	/**
	 * 重写返回键功能，实现连按两次退出App
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 按返回键的时候让新闻页面的页签回到第一个页签位置
			NewsPager newsPager = (NewsPager) mPagerList.get(0);
			if (newsPager.vpNews.getCurrentItem() == 0) {
				exit();
			} else {
				newsPager.mIndicator.setCurrentItem(0);
			}

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if (System.currentTimeMillis() - exitTime > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次返回退出",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}

}
