package com.aijia.webview.webkit.view;


import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.webkit.ValueCallback;

import com.aijia.webview.bridge.handler.BridgeHandler;
import com.aijia.webview.webkit.AWebChromeClient;
import com.aijia.webview.webkit.AWebViewClient;
import com.aijia.webview.webkit.AWebSettings;


public interface IWebView {
	Context getContext();
	void destroy();
	View getView();
	void reload();
	void clearCache(boolean b);
	void clearHistory();
	void loadUrl(String url);
	void setLayerType(int layerType, Paint paint);
	void setWebChromeClient(AWebChromeClient webChromeClient);
	void setWebViewClient(AWebViewClient webViewClient);
	void evaluateJavascript(String script, ValueCallback<String> resultCallback);
	void goBack();
	boolean canGoBack();
	AWebSettings getSettings();
	void setOverScrollMode(int scrollMode);
	int getOverScrollMode();
	void setVerticalScrollBarEnabled(boolean enabled);
	void setHorizontalScrollBarEnabled(boolean enabled);
	void setWebContentsDebuggingEnabled(boolean enabled);
	void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
	IX5WebViewExtension getX5WebViewExtension();
	interface IX5WebViewExtension {
		void setVerticalScrollBarEnabled(boolean enabled);
		boolean isVerticalScrollBarEnabled();
		boolean isHorizontalScrollBarEnabled();
		void setHorizontalScrollBarEnabled(boolean enabled);
		void setScrollBarFadingEnabled(boolean enabled);
	}
}
