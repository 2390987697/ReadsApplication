package com.Reads.base.impl.menu;

import java.util.ArrayList;
import java.util.List;
import com.Reads.MainActivity;
import com.Reads.base.BaseMenuDetailPager;
import com.Reads.domian.NewMenuData.NewsMenuDataTag;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.Reads.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {

	private ViewPager mViewPager;// 中间的小ViewPager,帧布局
	private List<NewsMenuDataTag> mTabData;// 页签网络数据
	private List<TabDetailPager> mPagers;// 页签页面集合
	private TabPageIndicator mIndicator;// 顶部tab导航栏的切换,用到开源框架

	public NewsMenuDetailPager(Activity mActivity, List<NewsMenuDataTag> children) {
		super(mActivity);
		mTabData = children;// 通过构造方法将页签数据传递过来
	}

	@Override
	public View initView() {// 初始化布局
		// 绑定控件
		View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_news_menu_detail);
		mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		ViewUtils.inject(this, view);

		return view;

	}

	@Override
	public void initData() {// 初始化页签数据

		mPagers = new ArrayList<TabDetailPager>();
		for (int i = 0; i < mTabData.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity, mTabData.get(i));// 把这个对应的对象拿过来
			mPagers.add(pager);// 往集合中添加页面
		}
		mViewPager.setAdapter(new NewsMenuDetailAdpater());
		mIndicator.setViewPager(mViewPager);// 将ViewPager与指示器绑定在一起(两者关联),注意:必须在ViewPager设置完数据之后才能绑定

		// 设置页面滑动监听
		mIndicator.setOnPageChangeListener(this);// 此处必须给mIndicator设置页面监听事件,不能给mViewPager设置页面监听事件

	}

	class NewsMenuDetailAdpater extends PagerAdapter {

		// 指定指示器的标题,页面上指示器的标题在这里展现
		@Override
		public CharSequence getPageTitle(int position) {
			NewsMenuDataTag data = mTabData.get(position);// 拿到对应的页签
			return data.title;
		}

		@Override
		public int getCount() {
			return mPagers.size();// 返回页签页面的个数,可以将页面当做一个对象来封装,因为他们的结构一致
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mPagers.get(position);
			pager.initData();
			View view = pager.mRootView;
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public void onPageScrollStateChanged(int position) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {

		if (position == 0) {// 如果是第一个页面,侧边栏不禁用
			setSlidMenuEnable(true);
		} else {
			setSlidMenuEnable(false);// 其它页面禁用侧边栏
		}

	}

	/**
	 * 开启和禁用侧边栏
	 */
	public void setSlidMenuEnable(boolean enable) {// 设置侧边栏开启和禁用的方法
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	@OnClick(R.id.btn_next)
	public void nextPage(View view) {// 注意:关于点击事件的方法里面必须有个试图参数
		// 跳到下一个页面
		int currentItem = mViewPager.getCurrentItem();// 拿到当前页面
		currentItem++;
		mViewPager.setCurrentItem(currentItem);
	}

	// --------------框架搭建到此结束---------------------

}
