package com.Reads.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScollViewPager extends ViewPager {

	/**
	 * ��д�������췽��
	 * 
	 */

	public NoScollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScollViewPager(Context context) {
		super(context);
	}

	// �¼�����
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;// Ϊfalse�������ӿؼ����¼�,Ϊtrue�����ӿؼ����¼�
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// ����ǶԻ����¼��Ľ���,Ϊfalse�ǲ�����
		return true;
	}

}
