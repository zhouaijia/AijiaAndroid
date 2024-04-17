package com.aijia.webview.webkit;

import android.webkit.WebSettings;

public abstract class AWebSettings {
	/**
	 * 设置WebView缓存模式，
	 * 这里的mode就是android.webkit.WebSettings里面的类型值，腾讯X5默认使用的就是这些类型值
	 * @param mode 缓存模式类型
	 */
	public abstract void setCacheMode(int mode);
	public abstract int getCacheMode();

	public abstract void setSupportZoom(boolean supportZoom);

	public abstract void setLayoutAlgorithm(WebSettings.LayoutAlgorithm layoutAlgorithm);

	public abstract void setUseWideViewPort(boolean useWideViewPort);
	public abstract boolean getUseWideViewPort();

	public abstract WebSettings.LayoutAlgorithm getLayoutAlgorithm();

	public abstract void setAllowFileAccess(boolean allow);

	public abstract boolean getAllowFileAccess();

	public abstract void setAllowContentAccess(boolean allow);

	public abstract boolean getAllowContentAccess();

	public abstract void setLoadWithOverviewMode(boolean overview);

	public abstract boolean getLoadWithOverviewMode();

	public abstract void setGeolocationDatabasePath(String databasePath);

	public abstract void setAppCacheEnabled(boolean flag);

	public abstract void setAppCachePath(String appCachePath);

	public abstract void setDatabaseEnabled(boolean flag);

	public abstract void setDomStorageEnabled(boolean flag);

	public abstract boolean getDomStorageEnabled();

	public abstract void setGeolocationEnabled(boolean flag);

	public abstract boolean getJavaScriptEnabled();

	public abstract void setLoadsImagesAutomatically(boolean flag);

	public abstract void setBlockNetworkImage(boolean flag);

	public abstract void setMixedContentMode(int mode);
	public abstract int getMixedContentMode();

	public abstract boolean getBlockNetworkImage();

	public abstract void setBlockNetworkLoads(boolean flag);

	public abstract boolean getBlockNetworkLoads();

	public abstract void setJavaScriptEnabled(boolean flag);

	public abstract void setJavaScriptCanOpenWindowsAutomatically(boolean flag);
	public abstract boolean getJavaScriptCanOpenWindowsAutomatically();

	public abstract void setDefaultTextEncodingName(String defaultTextEncodingName);
	public abstract String getDefaultTextEncodingName();

	public abstract void setBuiltInZoomControls(boolean enabled);

	public abstract boolean getBuiltInZoomControls();

	public abstract void setDisplayZoomControls(boolean enabled);

	public abstract boolean getDisplayZoomControls();

	public abstract void setUserAgentString(String ua);

	public abstract String getUserAgentString();

	@Deprecated
	public abstract void setAppCacheMaxSize(long maxSize);

	@Deprecated
	public abstract void setRenderPriority(WebSettings.RenderPriority priority);
}
