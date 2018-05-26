package com.Reads.base;

import com.Reads.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.Reads.R;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 5个标签页的父类
 * 
 */
public class BasePager {
	public TextView tvtitle;// 标题栏文字
	public ImageButton open_left_fragment;// 侧边栏按钮
	public Activity mActivity;
	public FrameLayout flcontent;// 空的帧布局对象, 要动态添加布局
	public View mRootView;// 定义布局

	public ImageButton btnphoto;// 组图切换按钮

	public BasePager(Activity Activity) { // 通过构造方法初始化5个标签页的布局
		mActivity = Activity;
		mRootView = initView();
	}

	// 初始化布局(这里放在父类中初始化控件,那么子类直接就可以拿来使用了,就不用再次初始化控件了)
	public View initView() {
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		flcontent = (FrameLayout) view.findViewById(R.id.fl_content);
		tvtitle = (TextView) view.findViewById(R.id.tv_title);
		btnphoto = (ImageButton) view.findViewById(R.id.btn_photo);
		open_left_fragment = (ImageButton) view.findViewById(R.id.open_left_fragment);
		open_left_fragment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();// 点击按钮打开或关闭侧边栏
			}
		});

		return view;
	}

	public void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之,调用后就开
	}

	public void initData() {

	}
}
