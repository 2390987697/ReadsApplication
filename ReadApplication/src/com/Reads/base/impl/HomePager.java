package com.Reads.base.impl;

import com.Reads.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class HomePager extends BasePager {

	public HomePager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public void initData() {
		TextView aView = new TextView(mActivity);
		aView.setText("Ê×Ò³");
		aView.setTextColor(Color.RED);
		aView.setGravity(Gravity.CENTER);
		aView.setTextSize(22);

		flcontent.addView(aView);

		tvtitle.setText("Ê×Ò³");

		open_left_fragment.setVisibility(View.INVISIBLE);
	}

}
