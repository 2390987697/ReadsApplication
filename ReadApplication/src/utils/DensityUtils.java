package utils;

import android.content.Context;

public class DensityUtils {
	// ��doת��px
	public static int dip2px(float dip, Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().density;
		int px = (int) (dip * density + 0.5f);// 4.9->4 4.1>4, ��������
		return px;
	}

	public static float px2dip(int px, Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
	}
}
