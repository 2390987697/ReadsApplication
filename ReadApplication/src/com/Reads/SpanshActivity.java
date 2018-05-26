package com.Reads;

import com.Reads.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import utils.PrefUtils;

public class SpanshActivity extends Activity {
	private CountDownTimer tm;// 倒计时
	private TextView tv;
	private RelativeLayout root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		setContentView(R.layout.activity_splash);
		root = (RelativeLayout) findViewById(R.id.root);
		tv = (TextView) findViewById(R.id.tv_time);

		tv.setOnClickListener(new OnClickListener() {// TextView的点击事件 //单机跳过
			@Override
			public void onClick(View v) {
				boolean isFirst = PrefUtils.getBoolean(SpanshActivity.this, "is_First", true);// 标识是不是第一次进入
				if (!isFirst) {// 不是第一次进入,单机跳过
					tm.cancel();
					Intent intent = new Intent(SpanshActivity.this, MainActivity.class);
					startActivity(intent);
					SpanshActivity.this.finish();
				}
			}
		});

		// 缩放动画
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);

		// 旋转动画
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);

		// 渐变动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);

		// 动画集合
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(alphaAnimation);
		set.addAnimation(rotateAnimation);
		set.addAnimation(scaleAnimation);

		root.startAnimation(set);// 开启动画

		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {// 动画结束
				tm = new MyCountDownTimer(6000, 1000);// 设置时间
				tm.start();// 开启倒计时
			}
		});

	}

	class MyCountDownTimer extends CountDownTimer {

		int startTime = 6;

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {

			boolean isFirst = PrefUtils.getBoolean(SpanshActivity.this, "is_First", true);
			tv.setVisibility(View.VISIBLE);// 显示倒计时
			if (isFirst) {
				tv.setText("倒计时" + "  " + (startTime - 1) + "s");
			} else {
				tv.setText("跳过" + "  " + (startTime - 1) + "s");
			}
			startTime--;
		}

		@Override
		public void onFinish() {
			boolean isFirst = PrefUtils.getBoolean(SpanshActivity.this, "is_First", true);// 标识是不是第一次进入
			Intent intent;
			if (isFirst) {
				intent = new Intent(getApplicationContext(), GuideActivity.class);// 跳新手引导页
			} else {
				intent = new Intent(getApplicationContext(), MainActivity.class);// 跳主页
			}
			startActivity(intent);// 跳转
			SpanshActivity.this.finish();// 销毁当前Activity
		}
	}
}
