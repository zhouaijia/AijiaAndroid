package com.aijia.webview.webkit;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.aijia.webview.webkit.view.IWebView;
import com.aijia.webview.webkit.view.NativeWebView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


public class WebViewHelper {
	/**
	 * 标志性的类
	 * 通过检查这些类即可判断相应的sdk是否存在
	 */
	private static final String FLAG_CLASS_X5_WEB = "com.tencent.smtt.sdk.WebView";
	/**
	 * 各种WebView类所在的包路径
	 */
	private static final String CLASS_PACKAGE = "com.aijia.webview.view.";
	/**
	 * 各个WebView类的全路径名
	 */
	private static final String CLASS_X5_WEB_VIEW = CLASS_PACKAGE + "X5WebView";

	/**
	 * 如果腾讯X5已被引入，则使用X5，否则使用原生WebView
	 * @param context 上下文
	 * @return 返回IWebView类型的WebView，目前有X5WebView和NativeWebView两种
	 */
	public static IWebView makeWebView(Context context, AttributeSet attr, int defStyle) {
		IWebView webView = null;

		try {
			if (checkFlagClassExist(FLAG_CLASS_X5_WEB)) {
				webView = (IWebView) getConstructor(CLASS_X5_WEB_VIEW, Context.class,
						AttributeSet.class, Integer.class).newInstance(context, attr, defStyle);
			} else {
				webView = new NativeWebView(context);
			}
			Log.d("WebViewManager", "--makeWebView--->web view class name-->" + webView.getClass().getName());
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return webView;
	}

	public static void initX5WebView(Context applicationContext) {
		try {
			Class<?> cl = Class.forName(CLASS_X5_WEB_VIEW);
			Method method = cl.getMethod("initX5WebView", Context.class);
			method.invoke(null, applicationContext);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 获取类的构造器
	 * @param className 类的全路径名
	 * @param parameterTypes 传入构造器中的参数所对应的class
	 * @return 构造器
	 */
	private static Constructor<?> getConstructor(String className, Class<?>... parameterTypes) {
		Constructor<?> cons = null;
		try {
			Class<?> cl = Class.forName(className);
			cons = cl.getConstructor(parameterTypes);
			if (!Modifier.isPublic(cl.getModifiers())) {
				cons.setAccessible(true);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return cons;
	}

	/**
	 * 检查标识类是否存在
	 * @param className 类的全路径名
	 * @return 该类是否存在
	 */
	private static boolean checkFlagClassExist(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}
}
