package com.Reads.base.impl;

import com.Reads.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class SmartServerPager extends BasePager {

	public SmartServerPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public void initData() {
		TextView aView = new TextView(mActivity);
		aView.setText("分类");
		aView.setTextColor(Color.RED);
		aView.setGravity(Gravity.CENTER);
		aView.setTextSize(22);

		flcontent.addView(aView);

		tvtitle.setText("分类");
		
		open_left_fragment.setVisibility(View.VISIBLE);
	}
}
