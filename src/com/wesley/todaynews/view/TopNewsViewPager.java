package com.wesley.todaynews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);

			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getRawX();
			int endY = (int) ev.getRawY();

			if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 表示左右滑动
				if (endX - startX > 0) {
					if (getCurrentItem() == 0) {// 第一个页面需要父控件拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {// 左滑
					if (getCurrentItem() == getAdapter().getCount() - 1) {
						// 最后一个页面也需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {// 上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);// 如果是上下滑动，拦截事件父控件处理ListView
			}

			break;

		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

}
