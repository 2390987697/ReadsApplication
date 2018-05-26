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

	private ViewPager mViewPager;// �м��СViewPager,֡����
	private List<NewsMenuDataTag> mTabData;// ҳǩ��������
	private List<TabDetailPager> mPagers;// ҳǩҳ�漯��
	private TabPageIndicator mIndicator;// ����tab���������л�,�õ���Դ���

	public NewsMenuDetailPager(Activity mActivity, List<NewsMenuDataTag> children) {
		super(mActivity);
		mTabData = children;// ͨ�����췽����ҳǩ���ݴ��ݹ���
	}

	@Override
	public View initView() {// ��ʼ������
		// �󶨿ؼ�
		View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_news_menu_detail);
		mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		ViewUtils.inject(this, view);

		return view;

	}

	@Override
	public void initData() {// ��ʼ��ҳǩ����

		mPagers = new ArrayList<TabDetailPager>();
		for (int i = 0; i < mTabData.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity, mTabData.get(i));// �������Ӧ�Ķ����ù���
			mPagers.add(pager);// �����������ҳ��
		}
		mViewPager.setAdapter(new NewsMenuDetailAdpater());
		mIndicator.setViewPager(mViewPager);// ��ViewPager��ָʾ������һ��(���߹���),ע��:������ViewPager����������֮����ܰ�

		// ����ҳ�滬������
		mIndicator.setOnPageChangeListener(this);// �˴������mIndicator����ҳ������¼�,���ܸ�mViewPager����ҳ������¼�

	}

	class NewsMenuDetailAdpater extends PagerAdapter {

		// ָ��ָʾ���ı���,ҳ����ָʾ���ı���������չ��
		@Override
		public CharSequence getPageTitle(int position) {
			NewsMenuDataTag data = mTabData.get(position);// �õ���Ӧ��ҳǩ
			return data.title;
		}

		@Override
		public int getCount() {
			return mPagers.size();// ����ҳǩҳ��ĸ���,���Խ�ҳ�浱��һ����������װ,��Ϊ���ǵĽṹһ��
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

		if (position == 0) {// ����ǵ�һ��ҳ��,�����������
			setSlidMenuEnable(true);
		} else {
			setSlidMenuEnable(false);// ����ҳ����ò����
		}

	}

	/**
	 * �����ͽ��ò����
	 */
	public void setSlidMenuEnable(boolean enable) {// ���ò���������ͽ��õķ���
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	@OnClick(R.id.btn_next)
	public void nextPage(View view) {// ע��:���ڵ���¼��ķ�����������и���ͼ����
		// ������һ��ҳ��
		int currentItem = mViewPager.getCurrentItem();// �õ���ǰҳ��
		currentItem++;
		mViewPager.setCurrentItem(currentItem);
	}

	// --------------��ܴ���˽���---------------------

}
