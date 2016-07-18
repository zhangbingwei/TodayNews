package com.wesley.todaynews.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.wesley.todaynews.R;

public class PhotosPager extends BasePager {

	private View photosView;// View对象
	private RadioGroup rgGroup;
	private ViewPager vpPhotos;

	private ArrayList<TabPhotosPager> photosPagerList;

	public PhotosPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initViews() {
		photosView = View.inflate(mActivity, R.layout.activity_photos, null);
		vpPhotos = (ViewPager) photosView.findViewById(R.id.vp_photos);
		rgGroup = (RadioGroup) photosView.findViewById(R.id.rg_photos);

		return photosView;
	}

	// 初始化页签数据
	@Override
	public void initData() {
		rgGroup.check(R.id.rb_travel);
		photosPagerList = new ArrayList<TabPhotosPager>();

		// 添加5个页面到ViewPager中
		for (int i = 0; i < 5; i++) {
			TabPhotosPager pager = new TabPhotosPager(mActivity, i);
			photosPagerList.add(pager);
		}

		if (photosPagerList != null) {
			vpPhotos.setAdapter(new PhotosAdapter());
		}

		// 当RadioButton切换时切换ViewPager具体页面
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.rb_travel:
					vpPhotos.setCurrentItem(0, false);
					break;
				case R.id.rb_camera:
					vpPhotos.setCurrentItem(1, false);
					break;
				case R.id.rb_fashion:
					vpPhotos.setCurrentItem(2, false);
					break;
				case R.id.rb_beauty:
					vpPhotos.setCurrentItem(3, false);
					break;
				case R.id.rb_food:
					vpPhotos.setCurrentItem(4, false);
					break;

				default:
					break;
				}
			}
		});

		photosPagerList.get(0).initData();
		// 当ViewPager页面滑动时切换头部tab页签
		vpPhotos.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				photosPagerList.get(arg0).initData();

				switch (arg0) {
				case 0:
					rgGroup.check(R.id.rb_travel);
					break;
				case 1:
					rgGroup.check(R.id.rb_camera);
					break;
				case 2:
					rgGroup.check(R.id.rb_fashion);
					break;
				case 3:
					rgGroup.check(R.id.rb_beauty);
					break;
				case 4:
					rgGroup.check(R.id.rb_food);
					break;

				default:
					break;
				}
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
	 * ViewPager适配器
	 * 
	 * @author zhangbingwei
	 * 
	 */
	class PhotosAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return photosPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			TabPhotosPager tabPhotosPager = photosPagerList.get(position);
			((ViewGroup) container).addView(tabPhotosPager.mRootView);
			// tabPhotosPager.initData();// 初始化子页面的数据
			return tabPhotosPager.mRootView;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewGroup) container).removeView((View) object);
		}

	}
}
