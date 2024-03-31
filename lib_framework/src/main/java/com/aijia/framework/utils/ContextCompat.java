package com.aijia.framework.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

public class ContextCompat {
	private static final String TAG = "ContextCompat";

	public static boolean checkSelfPermission(Context context, String permission) {
		if (context != null && permission != null) {
			boolean hasPermission = false;

			try {
				hasPermission = context.checkPermission(permission, Process.myPid(), Process.myUid()) == 0;
			} catch (RuntimeException var4) {
				Log.e(TAG, "dealType rethrowFromSystemServer:", var4);
			}

			return hasPermission;
		} else {
			Log.w(TAG, "param is null");
			return false;
		}
	}

	public static Context getProtectedStorageContext(Context context) {
		if (context == null) {
			Log.w(TAG, "context is null");
			return null;
		} else {
			return Build.VERSION.SDK_INT < 24 ? context : context.createDeviceProtectedStorageContext();
		}
	}

	public static Intent registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter filter) {
		if (context == null) {
			Log.w(TAG, "context is null");
			return null;
		} else {
			Intent intent = null;

			try {
				intent = context.registerReceiver(receiver, filter);
			} catch (RuntimeException re) {
				Log.e(TAG, "dealType rethrowFromSystemServer:", re);
			}

			return intent;
		}
	}

	public static Intent registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
		if (context == null) {
			Log.w(TAG, "context is null");
			return null;
		} else {
			Intent intent = null;

			try {
				intent = context.registerReceiver(receiver, filter, broadcastPermission, scheduler);
			} catch (RuntimeException re) {
				Log.e(TAG, "dealType rethrowFromSystemServer:", re);
			}

			return intent;
		}
	}

	public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
		if (context == null) {
			Log.w(TAG, "context is null");
		} else {
			try {
				context.unregisterReceiver(receiver);
			} catch (RuntimeException re) {
				Log.e(TAG, "SystemServer error:", re);
			}

		}
	}

	public static ComponentName startService(Context context, Intent intent) {
		if (context == null) {
			Log.w(TAG, "context is null");
			return null;
		} else {
			ComponentName componentName = null;

			try {
				componentName = context.startService(intent);
			} catch (RuntimeException re) {
				Log.e(TAG, "SystemServer error:", re);
			}

			return componentName;
		}
	}

	public static Object getSystemService(Context context, String name) {
		if (context == null) {
			Log.w(TAG, "context is null");
			return null;
		} else {
			Object object = null;

			try {
				object = context.getSystemService(name);
			} catch (RuntimeException re) {
				Log.e(TAG, "SystemServer error:", re);
			}

			return object;
		}
	}
}
