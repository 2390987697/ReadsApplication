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
	private ViewPager viewPager;// 可滑动的页面
	private Button btn_start;// 立即体验按钮
	private LinearLayout ll_content;// 灰色点的线性布局
	private ImageView iv_redpoint; // 小红点
	private int ivpointDes; // 小红点距离
	private ArrayList<ImageView> mImageViews; // 图片集合

	private int mImageIds[] = { R.drawable.huaban1, R.drawable.huaban2, R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3 };// 图片数组

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		// 绑定控件
		viewPager = (ViewPager) findViewById(R.id.vp_guide);
		btn_start = (Button) findViewById(R.id.btn_start);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		iv_redpoint = (ImageView) findViewById(R.id.iv_redpoint);

		initData();
		// 这里注意:先初始化数据后,在设置适配器.
		viewPager.setAdapter(new GuidePager());

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {// 设置ViewPager的页面改变监听事件

			@Override
			public void onPageSelected(int position) {
				if (position == mImageViews.size() - 1) { // 如果是最后一张图片
					btn_start.setVisibility(View.VISIBLE);// 显示立即体验按钮
				} else {
					btn_start.setVisibility(View.INVISIBLE);// 不显示立即体验按钮
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// 页面滑动的过程中计算小红点移动的距离 =当前 小红点距离*滑动百分比+当前小红点距离*当前索引值
				int leftMagin = (int) (ivpointDes * positionOffset) + ivpointDes * position;
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_redpoint.getLayoutParams();// 获得小红点的布局参数
				layoutParams.leftMargin = leftMagin; // 设置左边距
				iv_redpoint.setLayoutParams(layoutParams);// 设置小红点的布局参数
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		/**
		 * 小红点的视图树
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
				PrefUtils.setBoolean(getApplicationContext(), "is_First", false);// 这里是设置值,不是获取值,不要搞错了
				startActivity(new Intent(getApplicationContext(), MainActivity.class));// 点击后进入主页面
				finish();
			}
		});
	}

	public void initData() {// 初始化数据
		mImageViews = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]);
			mImageViews.add(imageView);

			ImageView point = new ImageView(this);
			point.setImageResource(R.drawable.sharp_normal_point);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);// 定义一个布局参数宽高都是包裹内容
			if (i > 0) {
				params.leftMargin = DensityUtils.dip2px(10, this);// 这里采用的是屏幕适配
			}
			point.setLayoutParams(params);// 重新设置圆点的参数.
			ll_content.addView(point);// 将圆点添加到线性布局容易中.
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
			ImageView imageView = mImageViews.get(position);// 拿到对应索引的图片
			container.addView(imageView);// 添加到容器中
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
