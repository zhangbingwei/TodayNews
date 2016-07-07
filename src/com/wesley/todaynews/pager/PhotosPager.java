package com.wesley.todaynews.pager;

import android.app.Activity;
import android.view.View;

import com.wesley.todaynews.R;

public class PhotosPager extends BasePager {

	public PhotosPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		View photosView = View.inflate(mActivity, R.layout.activity_photos,
				null);
		return photosView;
	}

	@Override
	public void initData() {
	}

}
