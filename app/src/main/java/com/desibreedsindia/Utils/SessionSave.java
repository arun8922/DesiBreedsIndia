package com.desibreedsindia.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *This common class to store the require data by using SharedPreferences.
 */
public class SessionSave
{
	private final static Object Object = new Object();
	public static void saveBoolean(String key, Boolean value, Context context)
	{
		Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
		editor.putBoolean(key,value);
		editor.commit();
	}

	public static Boolean getBoolean(String key, Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
		return prefs.getBoolean(key,false);
	}


	public static void saveSession(String key, String value, Context context)
	{
		Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getSession(String key, Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
		return prefs.getString(key, "");
	}

	public static void saveIntSession(String key, int value, Context context)
	{
		Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getIntSession(String key, Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
		return prefs.getInt(key, 0);
	}

	//	LONG
	public static void saveLongSession(String key, long value, Context context) {
		synchronized (Object) {
			Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
			editor.putLong(key, value);
			editor.apply();
		}
	}

	public static long getLongSession(String key, Context context) {
		synchronized (Object) {
			SharedPreferences prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
			return prefs.getLong(key, 0L);
		}
	}

	public static void clearSession(String key,Context context)
	{
		Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();

		editor.clear();
		editor.commit();
	}

	public static void setDistance(Float distance,Context con)
	{
		Editor editor=con.getSharedPreferences("DIS",con.MODE_PRIVATE).edit();
		editor.putFloat("DISTANCE", distance);
		editor.commit();
	}

	// FLOAT
	public static void saveFloatSession(String key, Float distance, Context context) {
		synchronized (Object) {
			SharedPreferences preferences = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putFloat(key, distance);
			editor.apply();
		}
	}

	public static float getFloatSession(String key, Context context) {
		synchronized (Object) {
			SharedPreferences preferences = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
			return preferences.getFloat(key, 0f);
		}
	}

	public static float getDistance(Context con)
	{
		SharedPreferences sharedPreferences=con.getSharedPreferences("DIS", con.MODE_PRIVATE);
		return sharedPreferences.getFloat("DISTANCE", 0);

	}

	public static void setWaitingTime(Long time,Context con)
	{
		Editor editor=con.getSharedPreferences("long",con.MODE_PRIVATE).edit();
		editor.putLong("LONG", time);
		editor.commit();
	}

	public static long getWaitingTime(Context con)
	{
		SharedPreferences sharedPreferences=con.getSharedPreferences("long", con.MODE_PRIVATE);
		return sharedPreferences.getLong("LONG", 0);

	}

}
