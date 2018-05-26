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
	private CountDownTimer tm;// ����ʱ
	private TextView tv;
	private RelativeLayout root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ������
		setContentView(R.layout.activity_splash);
		root = (RelativeLayout) findViewById(R.id.root);
		tv = (TextView) findViewById(R.id.tv_time);

		tv.setOnClickListener(new OnClickListener() {// TextView�ĵ���¼� //��������
			@Override
			public void onClick(View v) {
				boolean isFirst = PrefUtils.getBoolean(SpanshActivity.this, "is_First", true);// ��ʶ�ǲ��ǵ�һ�ν���
				if (!isFirst) {// ���ǵ�һ�ν���,��������
					tm.cancel();
					Intent intent = new Intent(SpanshActivity.this, MainActivity.class);
					startActivity(intent);
					SpanshActivity.this.finish();
				}
			}
		});

		// ���Ŷ���
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);

		// ��ת����
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);

		// ���䶯��
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);

		// ��������
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(alphaAnimation);
		set.addAnimation(rotateAnimation);
		set.addAnimation(scaleAnimation);

		root.startAnimation(set);// ��������

		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {// ��������
				tm = new MyCountDownTimer(6000, 1000);// ����ʱ��
				tm.start();// ��������ʱ
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
			tv.setVisibility(View.VISIBLE);// ��ʾ����ʱ
			if (isFirst) {
				tv.setText("����ʱ" + "  " + (startTime - 1) + "s");
			} else {
				tv.setText("����" + "  " + (startTime - 1) + "s");
			}
			startTime--;
		}

		@Override
		public void onFinish() {
			boolean isFirst = PrefUtils.getBoolean(SpanshActivity.this, "is_First", true);// ��ʶ�ǲ��ǵ�һ�ν���
			Intent intent;
			if (isFirst) {
				intent = new Intent(getApplicationContext(), GuideActivity.class);// ����������ҳ
			} else {
				intent = new Intent(getApplicationContext(), MainActivity.class);// ����ҳ
			}
			startActivity(intent);// ��ת
			SpanshActivity.this.finish();// ���ٵ�ǰActivity
		}
	}
}
