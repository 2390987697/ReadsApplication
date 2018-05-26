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
 * ��ҳ�� ���������� �����ǵ�ѡ��Ͽ� ������һ���ɻ�����ViewPager��ǩҳ
 * 
 */

public class ContentFragment extends BaseFragment {

	private NoScollViewPager mViewPager;// ���ɻ�����ViewPager
	private RadioGroup rp_group;// ��ѡ��Ͽ�
	private ArrayList<BasePager> mPagers;// ��ǩҳ

	@Override
	public View initView() {// ��ʼ������
		View view = View.inflate(mActivity, R.layout.content_fragment, null);// ����һ�����ַ���һ��View����
		mViewPager = (NoScollViewPager) view.findViewById(R.id.vp_content);
		rp_group = (RadioGroup) view.findViewById(R.id.rg_group);
		return view;
	}

	@Override
	public void initDate() {// ��ʼ������(ҳ��)
		mPagers = new ArrayList<BasePager>();
		mPagers.add(new HomePager(mActivity));// ��ҳ
		mPagers.add(new NewsCenterPager(mActivity));// �Ķ�
		mPagers.add(new SmartServerPager(mActivity));// ����
		mPagers.add(new GovAffarisPager(mActivity));// ����
		mPagers.add(new SetttingPager(mActivity));// ����

		mViewPager.setAdapter(new ContentAdpater());// ע��:ֻҪ�漰��ViewPager�йصĶ���Ҫ�̳�BaseAdpater������,Ȼ������������

		rp_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {// ��ѡ��Ͽ�ļ����¼�

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.shouye: // ѡ�е�ѡ��ť�л���ǩҳ setCurrentItem();���� ����1����Ӧ�ı�ǩҳ
									// ����2��false// ��ʾû��Я������Ч��
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

		// ������ǩ�л�����
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				BasePager basePager = mPagers.get(position);// �õ�ÿһ����ǩҳ
				basePager.initData();// ��ʼ����ǩҳ����
				if (position == mPagers.size() - 1 || position == 0) {// ��������û�����ҳ
					setSlidMenuEnable(false);// ���ò����
				} else {
					setSlidMenuEnable(true);// ���������

				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		mPagers.get(0).initData();// �ֶ���ʼ����һҳ����
		setSlidMenuEnable(false);// ����ҳ���ò����
	}

	public void setSlidMenuEnable(boolean enable) {// ���ò���������ͽ��õķ���
													// ���õ�MainActivity�ٴ�MainActiviy�õ������
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();// ����ͨ����ò���������ò�����Ŀ����ͽ���
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);// ���ò����
		}

	}

	class ContentAdpater extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagers.size();// ��ǩҳ�ĳ���
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
	 * �����������ҳ(�����������������ݸ��ı�ǩҳ�е�����)
	 */
	public NewsCenterPager getNewsCenterPagets() {
		NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);// �õ������������ҳ��
		return pager;
	}
}
