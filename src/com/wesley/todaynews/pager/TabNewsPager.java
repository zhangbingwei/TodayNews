package com.wesley.todaynews.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class TabNewsPager extends BasePager {

	private String tabTitle;
	private TextView text;

	public TabNewsPager(Activity activity, String title) {
		super(activity);
		tabTitle = title;
	}

	@Override
	public View initViews() {
		text = new TextView(mActivity);
		text.setTextColor(Color.RED);
		text.setTextSize(22);
		text.setGravity(Gravity.TOP);
		return text;
	}

	@Override
	public void initData() {
		text.setText(tabTitle);
	}

}
