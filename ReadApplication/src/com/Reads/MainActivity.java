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
 * 主页面
 * 
 */
public class MainActivity extends SlidingFragmentActivity {

	// 标记fragment
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		setContentView(R.layout.activity_main);

		setBehindContentView(R.layout.left_menu);// 引用左侧布局
		SlidingMenu slidingMenu = getSlidingMenu();// 获得侧边栏
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸


		// 以屏幕适配的方式设置侧边栏
		WindowManager wm = getWindowManager();// 获得屏幕窗口管理器
		int width = wm.getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(width * 200 / 320);

		initFragment();

	}

	public void initFragment() {
		FragmentManager fm = getSupportFragmentManager();// 获得Fragment管理器
		FragmentTransaction transaction = fm.beginTransaction();// 通过管理器来开启事物
		// 将MainActivity分成两个Fragment分别替换成两个布局
		transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
		transaction.replace(R.id.left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
		// 事物提交
		transaction.commit();
	}

	public ContentFragment getContentFragment() {// 主页面的Fragment
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
		return fragment;
	}

	public LeftMenuFragment getLeftMenuFragment() {// 侧边栏的Fragment
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
		return fragment;
	}
}
