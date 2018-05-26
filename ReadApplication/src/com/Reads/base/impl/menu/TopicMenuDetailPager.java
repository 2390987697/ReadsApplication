package com.Reads.base.impl.menu;

import com.Reads.base.BaseMenuDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class TopicMenuDetailPager extends BaseMenuDetailPager {

	public TopicMenuDetailPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public View initView() {
		TextView aView = new TextView(mActivity);
		aView.setText("�˵�����ҳ-ר��");
		aView.setTextColor(Color.RED);
		aView.setGravity(Gravity.CENTER);
		aView.setTextSize(22);

		return aView;
	}

}
