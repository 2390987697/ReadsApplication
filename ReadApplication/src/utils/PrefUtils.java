package utils;

import android.content.Context;
import android.content.SharedPreferences;

/***
 * 缓存类
 * 
 */
public class PrefUtils {
	public static boolean getBoolean(Context ctx, String key, boolean defValue) {// 這裡注意:不是在Activity中時,要通过Context对象来获得
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}

	public static void setBoolean(Context ctx, String key, boolean Value) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, Value).commit();
	}

	public static String getString(Context ctx, String key, String defValue) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	public static void setString(Context ctx, String key, String Value) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putString(key, Value).commit();
	}

	public static int getInt(Context ctx, String key, Integer defValue) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}

	public static void setInt(Context ctx, String key, Integer Value) {
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putInt(key, Value).commit();
	}
}
