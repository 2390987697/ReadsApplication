package com.Reads;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import utils.PrefUtils;
import utils.DensityUtils;



public class GuideActivity extends Activity {
	private ViewPager viewPager;// �ɻ�����ҳ��
	private Button btn_start;// �������鰴ť
	private LinearLayout ll_content;// ��ɫ������Բ���
	private ImageView iv_redpoint; // С���
	private int ivpointDes; // С������
	private ArrayList<ImageView> mImageViews; // ͼƬ����

	private int mImageIds[] = { R.drawable.huaban1, R.drawable.huaban2, R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3 };// ͼƬ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		// �󶨿ؼ�
		viewPager = (ViewPager) findViewById(R.id.vp_guide);
		btn_start = (Button) findViewById(R.id.btn_start);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		iv_redpoint = (ImageView) findViewById(R.id.iv_redpoint);

		initData();
		// ����ע��:�ȳ�ʼ�����ݺ�,������������.
		viewPager.setAdapter(new GuidePager());

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {// ����ViewPager��ҳ��ı�����¼�

			@Override
			public void onPageSelected(int position) {
				if (position == mImageViews.size() - 1) { // ��������һ��ͼƬ
					btn_start.setVisibility(View.VISIBLE);// ��ʾ�������鰴ť
				} else {
					btn_start.setVisibility(View.INVISIBLE);// ����ʾ�������鰴ť
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// ҳ�滬���Ĺ����м���С����ƶ��ľ��� =��ǰ С������*�����ٷֱ�+��ǰС������*��ǰ����ֵ
				int leftMagin = (int) (ivpointDes * positionOffset) + ivpointDes * position;
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_redpoint.getLayoutParams();// ���С���Ĳ��ֲ���
				layoutParams.leftMargin = leftMagin; // ������߾�
				iv_redpoint.setLayoutParams(layoutParams);// ����С���Ĳ��ֲ���
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		/**
		 * С������ͼ��
		 */
		iv_redpoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				iv_redpoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				ivpointDes = ll_content.getChildAt(1).getLeft() - ll_content.getChildAt(0).getLeft();
			}
		});

		btn_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PrefUtils.setBoolean(getApplicationContext(), "is_First", false);// ����������ֵ,���ǻ�ȡֵ,��Ҫ�����
				startActivity(new Intent(getApplicationContext(), MainActivity.class));// ����������ҳ��
				finish();
			}
		});
	}

	public void initData() {// ��ʼ������
		mImageViews = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]);
			mImageViews.add(imageView);

			ImageView point = new ImageView(this);
			point.setImageResource(R.drawable.sharp_normal_point);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);// ����һ�����ֲ�����߶��ǰ�������
			if (i > 0) {
				params.leftMargin = DensityUtils.dip2px(10, this);// ������õ�����Ļ����
			}
			point.setLayoutParams(params);// ��������Բ��Ĳ���.
			ll_content.addView(point);// ��Բ����ӵ����Բ���������.
		}
	}

	class GuidePager extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = mImageViews.get(position);// �õ���Ӧ������ͼƬ
			container.addView(imageView);// ��ӵ�������
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
