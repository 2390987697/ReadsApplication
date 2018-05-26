package com.Reads;

import com.Reads.Fragment.ContentFragment;
import com.Reads.Fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

/**
 * ��ҳ��
 * 
 */
public class MainActivity extends SlidingFragmentActivity {

	// ���fragment
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ������
		setContentView(R.layout.activity_main);

		setBehindContentView(R.layout.left_menu);// ������಼��
		SlidingMenu slidingMenu = getSlidingMenu();// ��ò����
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// ����ȫ������


		// ����Ļ����ķ�ʽ���ò����
		WindowManager wm = getWindowManager();// �����Ļ���ڹ�����
		int width = wm.getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(width * 200 / 320);

		initFragment();

	}

	public void initFragment() {
		FragmentManager fm = getSupportFragmentManager();// ���Fragment������
		FragmentTransaction transaction = fm.beginTransaction();// ͨ������������������
		// ��MainActivity�ֳ�����Fragment�ֱ��滻����������
		transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
		transaction.replace(R.id.left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
		// �����ύ
		transaction.commit();
	}

	public ContentFragment getContentFragment() {// ��ҳ���Fragment
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
		return fragment;
	}

	public LeftMenuFragment getLeftMenuFragment() {// �������Fragment
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
		return fragment;
	}
}
