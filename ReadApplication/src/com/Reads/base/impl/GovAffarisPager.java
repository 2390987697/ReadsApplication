package com.Reads.base.impl;

import com.Reads.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class GovAffarisPager extends BasePager {

	public GovAffarisPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		TextView aView = new TextView(mActivity);
		aView.setText("政务");
		aView.setTextColor(Color.RED);
		aView.setGravity(Gravity.CENTER);
		aView.setTextSize(22);

		flcontent.addView(aView);

		tvtitle.setText("政务");
		
		open_left_fragment.setVisibility(View.VISIBLE);
	}
}
