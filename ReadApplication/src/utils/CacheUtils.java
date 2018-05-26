package utils;

import android.content.Context;

public class CacheUtils {
	public static void setCache(String url, String json, Context ctx) {
		PrefUtils.setString(ctx, url, json);
	}

	public static String getCache(String url, Context ctx) {
		return PrefUtils.getString(ctx, url, null);
	}
}
