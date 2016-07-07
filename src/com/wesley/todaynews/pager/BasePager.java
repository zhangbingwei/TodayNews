package com.wesley.todaynews.pager;

import android.app.Activity;
import android.view.View;

/**
 * 主页下面3个子页面的基类
 * 
 * @author zhangbingwei
 * 
 */
public abstract class BasePager {

	public Activity mActivity;

	public View mRootView;// 布局对象

	public BasePager(Activity activity) {
		mActivity = activity;

		mRootView = initViews();
	}

	/**
	 * 初始化布局
	 */
	public abstract View initViews();

	/**
	 * 初始化数据
	 */
	public void initData() {

	}

}
