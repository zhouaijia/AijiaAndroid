package com.aijia.framework.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.Lifecycle;


public class Utils {

	@SuppressLint("StaticFieldLeak")
	private static Application sApp;

	private Utils() {
		throw new UnsupportedOperationException("You can't instantiate me...");
	}

	/**
	 * Init utils.
	 * <p>Init it in the class of UtilsFileProvider.</p>
	 *
	 * @param app application
	 */
	public static void init(final Application app) {
		if (app == null) {
			Log.e("Utils", "app is null.");
			return;
		}
		if (sApp == null) {
			sApp = app;
			UtilsBridge.init(sApp);
			UtilsBridge.preLoad();
			return;
		}
		if (sApp.equals(app)) return;
		UtilsBridge.unInit(sApp);
		sApp = app;
		UtilsBridge.init(sApp);
	}

	/**
	 * Return the Application object.
	 * <p>Main process get app by UtilsFileProvider,
	 * and other process get app by reflect.</p>
	 *
	 * @return the Application object
	 */
	public static Application getApp() {
		if (sApp != null) return sApp;
		init(UtilsBridge.getApplicationByReflect());
		if (sApp == null) throw new NullPointerException("reflect failed.");
		Log.i("Utils", UtilsBridge.getCurrentProcessName() + " reflect app success.");
		return sApp;
	}

	///////////////////////////////////////////////////////////////////////////
	// interface
	///////////////////////////////////////////////////////////////////////////

	public abstract static class Task<Result> extends ThreadUtils.SimpleTask<Result> {

		private Consumer<Result> mConsumer;

		public Task(final Consumer<Result> consumer) {
			mConsumer = consumer;
		}

		@Override
		public void onSuccess(Result result) {
			if (mConsumer != null) {
				mConsumer.accept(result);
			}
		}
	}

	public interface OnAppStatusChangedListener {
		void onForeground(Activity activity);

		void onBackground(Activity activity);
	}

	public static class ActivityLifecycleCallbacks {

		public void onActivityCreated(@NonNull Activity activity) {/**/}

		public void onActivityStarted(@NonNull Activity activity) {/**/}

		public void onActivityResumed(@NonNull Activity activity) {/**/}

		public void onActivityPaused(@NonNull Activity activity) {/**/}

		public void onActivityStopped(@NonNull Activity activity) {/**/}

		public void onActivityDestroyed(@NonNull Activity activity) {/**/}

		public void onLifecycleChanged(@NonNull Activity activity, Lifecycle.Event event) {/**/}
	}

	public interface Consumer<T> {
		void accept(T t);
	}

	public interface Supplier<T> {
		T get();
	}

	public interface Func1<Ret, Par> {
		Ret call(Par param);
	}

	/**判断字符串是否为数字
	 *
	 * @param str 入参字符串
	 * @return 是否为数字
	 */
	public static boolean isNumeric(String str){
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**修改drawable的颜色
	 *
	 * @param drawable 源drawable
	 * @param color 目标颜色
	 * @return 变色后到drawable
	 */
	public static Drawable changeDrawableColor(Drawable drawable, int color) {
		try {
			ColorStateList colorStateList = ColorStateList.valueOf(color);
			Drawable mutateDrawable = DrawableCompat.wrap(drawable).mutate();
			if (mutateDrawable != null) {
				DrawableCompat.setTintList(mutateDrawable, colorStateList);
				return mutateDrawable;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return drawable;
	}

	/**
	 * 读取assets下的资源文件
	 * @param fileName 文件名
	 * @return byte
	 */
	public static byte[] getAssertsFile(Context context, String fileName) {
		AssetManager assetManager = context.getAssets();
		try {
			InputStream inputStream = assetManager.open(fileName);
			if (inputStream == null) {
				return null;
			}

			BufferedInputStream bis = null;
			int length;
			try {
				bis = new BufferedInputStream(inputStream);
				length = bis.available();
				byte[] data = new byte[length];
				bis.read(data);

				return data;
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}

			return null;
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return null;
	}


	/**
	 * Android P 以及之后版本不支持同时从多个进程使用具有相同数据目录的WebView
	 * 这行代码需要在其他的SDK等等初始化之前就要调用，否则会报其他的错误
	 * @param context
	 */
	public static void webViewSetPath(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			String processName = getProcessName(context);

			if (!context.getPackageName().equals(processName)) {//判断不等于默认进程名称
				WebView.setDataDirectorySuffix(processName);
			}
		}
	}

	public static String getProcessName(Context context) {
		if (context == null) return null;
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
			if (processInfo.pid == android.os.Process.myPid()) {
				return processInfo.processName;
			}
		}
		return null;
	}

	/**
	 * 获取视频文件首帧图
	 *
	 * @param path 视频文件的路径
	 * @return Bitmap 返回获取的Bitmap
	 */
	public static Bitmap getVideoThumb(Context context, String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}

		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		try {
			//全部采用uri，因为相册中的照片和视频不能直接通过绝对路径访问
			Uri uri = PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)
					? Uri.parse(path)
					: Uri.fromFile(new File(path));
			//uri = FileProvider.getUriForFile(context, AppUtils.getPackageName(context) + ".provider", new File(path));
			mmr.setDataSource(context, uri);

			return mmr.getFrameAtTime();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				mmr.release();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 获取 视频 或 音频 时长
	 * @param path 视频 或 音频 文件路径
	 * @return 时长 毫秒值
	 */
	public static long getMediaDuration(String path) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		long duration = 0;
		try {
			if (path!= null) {
				mmr.setDataSource(path);
			}
			String time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			duration= Long.parseLong(time);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				mmr.release();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return duration;
	}

	/**
	 * 获取 视频 或 音频 时长
	 * @param path 视频 或 音频 文件路径
	 * @return 时长 毫秒值
	 */
	public static long getMediaDuration(Context context, String path) {
		if (TextUtils.isEmpty(path)) {
			return 0L;
		}

		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		long duration = 0;
		try {
			//全部采用uri，因为相册中的照片和视频不能直接通过绝对路径访问
			Uri uri = PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)
					? Uri.parse(path)
					: Uri.fromFile(new File(path));
			mmr.setDataSource(context, uri);

			String time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			duration= Long.parseLong(time);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				mmr.release();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return duration;
	}

	public static Bitmap makeTintBitmap(Bitmap inputBitmap, int tintColor) {
		if (inputBitmap == null) {
			return null;
		}

		Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
		Canvas canvas = new Canvas(outputBitmap);
		Paint paint = new Paint();
		paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(inputBitmap, 0, 0, paint);
		return outputBitmap;
	}


	/**
	 * 对字符串进行异或加、解密，最后给结果加上固定头部
	 * @param str 目标字符串
	 * @return 加、解密结果
	 */
	public static String xorEncrypt(String str) {
		return xorEncrypt(str, true);
	}

	/**
	 * 对字符串进行异或加、解密，最后给结果加上固定头部
	 * @param str 目标字符串
	 * @param isDecrypt 是否是解密
	 * @return 加、解密结果
	 */
	public static String xorEncrypt(String str, boolean isDecrypt) {
		if (TextUtils.isEmpty(str)) {
			return null;
		}

		String head = "XOR-ENCRYPT";
		if (isDecrypt && !str.startsWith(head)) {
			return str;//兼容app1.8.0以前的版本
		}

		String result;

		if (str.startsWith(head)) {
			str = str.split(head)[1];
			result = EncryptUtils.xor(str);
		} else {
			result = EncryptUtils.xor(str);
			result = head + result;
		}

		return result;
	}
}
