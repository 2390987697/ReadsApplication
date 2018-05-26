package com.Reads.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScollViewPager extends ViewPager {

	/**
	 * 重写两个构造方法
	 * 
	 */

	public NoScollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScollViewPager(Context context) {
		super(context);
	}

	// 事件拦截
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;// 为false不拦截子控件的事件,为true拦截子控件的事件
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 这个是对滑动事件的禁用,为false是不禁用
		return true;
	}

}
