package com.wesley.todaynews.pager;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wesley.todaynews.R;

public class SettingPager extends BasePager {

	private View settingView;

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		settingView = View.inflate(mActivity, R.layout.activity_settings, null);
		return settingView;
	}

	@Override
	public void initData() {
		TextView tvSafe = (TextView) settingView.findViewById(R.id.tv_safe);
		tvSafe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(mActivity, "测试用的", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
