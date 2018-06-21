package com.desibreedsindia.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.desibreedsindia.BaseActivity;
import com.desibreedsindia.R;

/**
 * This BroadcastReceiver to update network connections is Available/Not.
 */
public class NetworkStatus extends BroadcastReceiver
{
	public Context mContext;
	private String message;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		mContext = context;
		try
		{
			if (getConnectivityStatus(mContext))
			{
				message = " Network connection is Enable!!";
				/*if (!CommonData.current_act.equals("SplashAct"))
//					generatNotification(message);
                BaseActivity.isConnect(CommonData.sContext, true);*/
				if (CommonData.sContext != null)
					BaseActivity.isConnect(CommonData.sContext, true);
			}
			else
			{
				message = " Network connection is Disable!!";/*
				if (!CommonData.current_act.equals("SplashAct"))
//					generatNotification(message);
                BaseActivity.isConnect(CommonData.sContext, false);*/
				if (CommonData.sContext != null)
					BaseActivity.isConnect(CommonData.sContext, false);
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void generatNotification(String message2)
	{
		// TODO Auto-generated method stub
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher_background, message, System.currentTimeMillis());
		String title = mContext.getString(R.string.app_name);
		Intent notificationIntent = new Intent(mContext, BaseActivity.class);
		notificationIntent.putExtra("message", message);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
		//notification.setLatestEventInfo(mContext, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}

	public static boolean isOnline(Context mContext2)
	{
		ConnectivityManager connectivity = (ConnectivityManager) mContext2.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
		}
		return false;
	}

	public static boolean getConnectivityStatus(Context context)
	{
		boolean conn = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork)
		{
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
			{
				if (activeNetwork.isConnected())
				{
					conn = true;
				}
				else
				{
					conn = false;
				}
			}
		}
		else
		{
			conn = false;
		}
		return conn;
	}
}
