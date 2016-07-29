package com.wesley.todaynews.pager;

import android.app.Activity;
import android.view.View;

import com.etsy.android.grid.StaggeredGridView;
import com.wesley.todaynews.R;

public class PhotosPager extends BasePager {

	private View photosView;// View对象

	public PhotosPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		photosView = View.inflate(mActivity, R.layout.activity_photos, null);
		// 瀑布流GridView
		StaggeredGridView mStaggeredGridView = (StaggeredGridView) photosView
				.findViewById(R.id.stagger_view);
		return photosView;
	}

	@Override
	public void initData() {
		super.initData();
	}

}
