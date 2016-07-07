package com.wesley.todaynews.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.wesley.todaynews.R;

public class NewsPager extends BasePager {

	private View newsView;// View对象
	private ViewPager vpNews; // 首页新闻下面嵌入的ViewPager对象
	private ArrayList<TabNewsPager> newsPagerList;// 页签集合

	private String[] tabTitle = new String[] { "头条", "社会", "国内", "国际", "娱乐",
			"体育", "军事", "科技", "财经", "时尚" };

	public NewsPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		newsView = View.inflate(mActivity, R.layout.activity_news, null);
		vpNews = (ViewPager) newsView.findViewById(R.id.vp_news);
		return newsView;
	}

	// 初始化页签数据
	@Override
	public void initData() {
		newsPagerList = new ArrayList<TabNewsPager>();
		for (int i = 0; i < tabTitle.length; i++) {
			TabNewsPager pager = new TabNewsPager(mActivity, tabTitle[i]);
			newsPagerList.add(pager);
		}

		vpNews.setAdapter(new NewsPagerAdapter());

	}

	/**
	 * 内嵌的ViewPager适配器
	 * 
	 * @author zhangbingwei
	 * 
	 */
	class NewsPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return newsPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			TabNewsPager pager = newsPagerList.get(position);
			((ViewGroup) container).addView(pager.mRootView);
			pager.initData();
			return pager.mRootView;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
	}

}