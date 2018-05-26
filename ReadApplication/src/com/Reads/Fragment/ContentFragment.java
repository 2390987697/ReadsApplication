package com.Reads.Fragment;

import java.util.ArrayList;
import com.Reads.MainActivity;
import com.Reads.base.BasePager;
import com.Reads.base.impl.GovAffarisPager;
import com.Reads.base.impl.HomePager;
import com.Reads.base.impl.NewsCenterPager;
import com.Reads.base.impl.SetttingPager;
import com.Reads.base.impl.SmartServerPager;
import com.Reads.view.NoScollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.Reads.R;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 主页面 分两个部分 底下是单选组合框 上面是一个可滑动的ViewPager标签页
 * 
 */

public class ContentFragment extends BaseFragment {

	private NoScollViewPager mViewPager;// 不可滑动的ViewPager
	private RadioGroup rp_group;// 单选组合框
	private ArrayList<BasePager> mPagers;// 标签页

	@Override
	public View initView() {// 初始化布局
		View view = View.inflate(mActivity, R.layout.content_fragment, null);// 加载一个布局返回一个View对象
		mViewPager = (NoScollViewPager) view.findViewById(R.id.vp_content);
		rp_group = (RadioGroup) view.findViewById(R.id.rg_group);
		return view;
	}

	@Override
	public void initDate() {// 初始化数据(页面)
		mPagers = new ArrayList<BasePager>();
		mPagers.add(new HomePager(mActivity));// 首页
		mPagers.add(new NewsCenterPager(mActivity));// 阅读
		mPagers.add(new SmartServerPager(mActivity));// 服务
		mPagers.add(new GovAffarisPager(mActivity));// 政务
		mPagers.add(new SetttingPager(mActivity));// 设置

		mViewPager.setAdapter(new ContentAdpater());// 注意:只要涉及到ViewPager有关的都需要继承BaseAdpater适配器,然后设置适配器

		rp_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 单选组合框的监听事件

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.shouye: // 选中单选按钮切换标签页 setCurrentItem();方法 参数1：对应的标签页
									// 参数2：false// 表示没有携带动画效果
					mViewPager.setCurrentItem(0, false);
					break;
				case R.id.news:
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.smart:
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.zhengwu:
					mViewPager.setCurrentItem(3, false);
					break;
				case R.id.setting:
					mViewPager.setCurrentItem(4, false);
					break;
				default:
					break;
				}
			}
		});

		// 底栏标签切换监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				BasePager basePager = mPagers.get(position);// 拿到每一个标签页
				basePager.initData();// 初始化标签页数据
				if (position == mPagers.size() - 1 || position == 0) {// 如果是设置或者首页
					setSlidMenuEnable(false);// 禁用侧边栏
				} else {
					setSlidMenuEnable(true);// 开启侧边栏

				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		mPagers.get(0).initData();// 手动初始化第一页数据
		setSlidMenuEnable(false);// 将首页禁用侧边栏
	}

	public void setSlidMenuEnable(boolean enable) {// 设置侧边栏开启和禁用的方法
													// 先拿到MainActivity再从MainActiviy拿到侧边栏
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();// 这里通过获得侧边栏来设置侧边栏的开启和禁用
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);// 禁用侧边栏
		}

	}

	class ContentAdpater extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagers.size();// 标签页的长度
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pagers = mPagers.get(position);
			View view = pagers.mRootView;
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * 获得新闻中心页(用作点击侧边栏的数据更改标签页中的内容)
	 */
	public NewsCenterPager getNewsCenterPagets() {
		NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);// 拿到新闻中心这个页面
		return pager;
	}
}
