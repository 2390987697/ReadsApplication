package com.Reads.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 头条新闻自定义ViewPager(设置该ViewPager的滑动事件)
 */
public class TopNewsViewPager extends ViewPager {

	// 移动之前的坐标
	private int startX;
	private int startY;

	// 习惯重写多个构造方法
	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}

	/**
	 * 1.上下滑动 需要拦截 2.左右滑动 向右滑动并且当前是第一个页面,需要拦截 3.并且当前是最后一个页面,需要拦截
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);// 刚开始请求不拦截事件
		// 怎么判断是上下滑动,还是左右滑动?看水平方向和竖直方向的哪个偏移量大
		switch (ev.getAction()) {// 获得事件的动作
		case MotionEvent.ACTION_DOWN:// 按下

			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:// 移动
			// 移动之后的坐标
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();

			// 计算偏移量
			int dx = endX - startX;// 水平方向的偏移量
			int dy = endY - startY;// 竖直方法的偏移量

			if (Math.abs(dy) < Math.abs(dx)) { // 按绝对值来判断偏移量,水平方向的偏移量大表示左右滑动

				int currentItem = getCurrentItem();// 获得ViewPager的当前页面
				// 怎么判断是向左滑动,还是向右滑动呢?只需要判断dx>0或者dx<0就行了
				// dx>0向右滑动 dx<0向左滑动
				if (dx > 0) {
					// 向右滑动
					if (currentItem == 0) {
						// 第一个页面,需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// 向左滑
					int count = getAdapter().getCount();// (item总数)
														// 图片不是固定值,先要获得item的个数,才能确定最后一个图片
					if (currentItem == count - 1) {
						// 最后一个页面,需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {// 上下滑动,需要拦截
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
