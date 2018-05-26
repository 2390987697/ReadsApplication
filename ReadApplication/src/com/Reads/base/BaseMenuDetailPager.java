package com.Reads.base;

import android.app.Activity;
import android.view.View;

/**
 * �˵�����ҳ����
 */
public abstract class BaseMenuDetailPager {

	public Activity mActivity;
	public View mRootView;

	public BaseMenuDetailPager(Activity mActivity) {
		this.mActivity = mActivity;
		mRootView = initView();
	}

	public abstract View initView();

	public void initData() {}
}
