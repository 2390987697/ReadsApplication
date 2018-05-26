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
 * 5����ǩҳ�ĸ���
 * 
 */
public class BasePager {
	public TextView tvtitle;// ����������
	public ImageButton open_left_fragment;// �������ť
	public Activity mActivity;
	public FrameLayout flcontent;// �յ�֡���ֶ���, Ҫ��̬��Ӳ���
	public View mRootView;// ���岼��

	public ImageButton btnphoto;// ��ͼ�л���ť

	public BasePager(Activity Activity) { // ͨ�����췽����ʼ��5����ǩҳ�Ĳ���
		mActivity = Activity;
		mRootView = initView();
	}

	// ��ʼ������(������ڸ����г�ʼ���ؼ�,��ô����ֱ�ӾͿ�������ʹ����,�Ͳ����ٴγ�ʼ���ؼ���)
	public View initView() {
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		flcontent = (FrameLayout) view.findViewById(R.id.fl_content);
		tvtitle = (TextView) view.findViewById(R.id.tv_title);
		btnphoto = (ImageButton) view.findViewById(R.id.btn_photo);
		open_left_fragment = (ImageButton) view.findViewById(R.id.open_left_fragment);
		open_left_fragment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();// �����ť�򿪻�رղ����
			}
		});

		return view;
	}

	public void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// �����ǰ״̬�ǿ�, ���ú�͹�; ��֮,���ú�Ϳ�
	}

	public void initData() {

	}
}
