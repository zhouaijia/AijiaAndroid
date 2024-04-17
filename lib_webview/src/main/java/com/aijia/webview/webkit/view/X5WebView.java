package com.aijia.webview.webkit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.aijia.webview.bridge.handler.BridgeHandler;
import com.aijia.webview.webkit.AConsoleMessage;
import com.aijia.webview.webkit.AValueCallback;
import com.aijia.webview.webkit.AWebChromeClient;
import com.aijia.webview.webkit.AWebResourceError;
import com.aijia.webview.webkit.AWebResourceRequest;
import com.aijia.webview.webkit.AWebResourceResponse;
import com.aijia.webview.webkit.AWebSettings;
import com.aijia.webview.webkit.AWebViewClient;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;
import java.util.Map;

public class X5WebView implements IWebView {

	private final WebView webView;

	public X5WebView(Context context) {
		webView = new WebView(context);
	}

	public X5WebView(Context context, AttributeSet attr) {
		webView = new WebView(context, attr);
	}

	public X5WebView(Context context, AttributeSet attr, int defStyle) {
		webView = new WebView(context, attr, defStyle);
	}

	/**
	 * 初始化腾讯X5 web内核
	 */
	public static void initX5WebView(Context applicationContext) {
		// 在调用TBS初始化、创建WebView之前进行如下配置
		Map<String, Object> map = new HashMap<>();
		map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
		map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
		QbSdk.initTbsSettings(map);

		QbSdk.setDownloadWithoutWifi(true);

		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
			@Override
			public void onCoreInitFinished() {
			}

			@Override
			public void onViewInitFinished(boolean succeed) {
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(applicationContext, cb);

        /*Looper.myQueue().addIdleHandler(() -> {
            //移到MainActivity中初始化，否则会报错：
            //同时从多个进程使用具有相同数据目录的WebView
            WebView.setWebContentsDebuggingEnabled(isDebug);
            return false;
        });*/
	}

	@Override
	public View getView() {
		return webView;
	}

	@Override
	public void reload() {
		webView.reload();
	}

	@Override
	public void clearCache(boolean includeDiskFiles) {
		webView.clearCache(includeDiskFiles);
	}

	@Override
	public void clearHistory() {
		webView.clearHistory();
	}

	@Override
	public void loadUrl(String url) {
		webView.loadUrl(url);
	}

	@Override
	public void setLayerType(int layerType, Paint paint) {
		webView.setLayerType(layerType, paint);
	}

	@Override
	public void setWebChromeClient(AWebChromeClient webChromeClient) {
		webView.setWebChromeClient(new MyWebChromeClient(webChromeClient));
	}

	@Override
	public void setWebViewClient(AWebViewClient webViewClient) {
		webView.setWebViewClient(new MyWebViewClient(webViewClient));
	}

	@Override
	public void evaluateJavascript(String script, android.webkit.ValueCallback<String> resultCallback) {
		if (resultCallback != null) {
			webView.evaluateJavascript(script, resultCallback::onReceiveValue);
		} else {
			webView.evaluateJavascript(script, null);
		}
	}

	@Override
	public void goBack() {
		webView.goBack();
	}

	@Override
	public boolean canGoBack() {
		return webView.canGoBack();
	}

	@Override
	public AWebSettings getSettings() {
		return new MyWebSettings(webView.getSettings());
	}

	@Override
	public void setOverScrollMode(int scrollMode) {
		webView.setOverScrollMode(scrollMode);
	}

	@Override
	public int getOverScrollMode() {
		return webView.getOverScrollMode();
	}

	@Override
	public void setVerticalScrollBarEnabled(boolean enabled) {
		webView.setVerticalScrollBarEnabled(enabled);
	}

	@Override
	public void setHorizontalScrollBarEnabled(boolean enabled) {
		webView.setHorizontalScrollBarEnabled(enabled);
	}

	@Override
	public void setWebContentsDebuggingEnabled(boolean isEnabled) {
		WebView.setWebContentsDebuggingEnabled(isEnabled);
	}

	@Override
	public void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
		webView.super_onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
	}

	@Override
	public IX5WebViewExtension getX5WebViewExtension() {
		return new X5WebViewExtension(webView.getX5WebViewExtension());
	}

	@Override
	public Context getContext() {
		return webView.getContext();
	}

	@Override
	public void destroy() {
		webView.destroy();
	}

	private WebResourceResponse createX5Response(AWebResourceResponse response) {
		if (response == null) {
			return null;
		}
		WebResourceResponse webResourceResponse = new WebResourceResponse(
				response.getMimeType(), response.getEncoding(), response.getData());
		webResourceResponse.setResponseHeaders(response.getResponseHeaders());
		webResourceResponse.setStatusCodeAndReasonPhrase(response.getStatusCode(), response.getReasonPhrase());

		return webResourceResponse;
	}

	private AConsoleMessage getMyConsoleMessage(ConsoleMessage cm) {
		return new AConsoleMessage(cm.message(), cm.sourceId(),
				cm.lineNumber(), cm.messageLevel().ordinal());
	}

	public class MyWebChromeClient extends WebChromeClient {
		private final AWebChromeClient webChromeClient;

		public MyWebChromeClient(AWebChromeClient webChromeClient) {
			this.webChromeClient = webChromeClient;
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			return webChromeClient.onConsoleMessage(getMyConsoleMessage(consoleMessage));
		}

		@Override
		public void onGeolocationPermissionsHidePrompt() {
			webChromeClient.onGeolocationPermissionsHidePrompt();
		}

		@Override
		public void onHideCustomView() {
			webChromeClient.onHideCustomView();
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			webChromeClient.onProgressChanged(X5WebView.this, newProgress);
		}

		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			webChromeClient.onReceivedIcon(X5WebView.this, icon);
		}

		@Override
		public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
			webChromeClient.onReceivedTouchIconUrl(X5WebView.this, url, precomposed);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			webChromeClient.onReceivedTitle(X5WebView.this, title);
		}

		@Override
		public void onRequestFocus(WebView view) {
			webChromeClient.onRequestFocus(X5WebView.this);
		}

		@Override
		public void onCloseWindow(WebView view) {
			webChromeClient.onCloseWindow(X5WebView.this);
		}

		@Override
		public View getVideoLoadingProgressView() {
			return webChromeClient.getVideoLoadingProgressView();
		}

		@Override
		public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
			webChromeClient.openFileChooser(new MyValueCallback<Uri>(uploadFile), acceptType, capture);
		}

		@Override
		public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
			return webChromeClient.onShowFileChooser(X5WebView.this, new MyValueCallback<Uri[]>(filePathCallback));
		}
	}

	public class MyValueCallback<T> implements AValueCallback<T> {
		private final ValueCallback<T> valueCallback;

		public MyValueCallback(ValueCallback<T> valueCallback) {
			this.valueCallback = valueCallback;
		}

		@Override
		public void onReceiveValue(T value) {
			valueCallback.onReceiveValue(value);
		}
	}


	public class MyWebViewClient extends WebViewClient {
		private final AWebViewClient webViewClient;

		public MyWebViewClient(AWebViewClient webViewClient) {
			this.webViewClient = webViewClient;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return webViewClient.shouldOverrideUrlLoading(X5WebView.this, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			return webViewClient.shouldOverrideUrlLoading(X5WebView.this, new MyWebResourceRequest(request));
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			webViewClient.onPageStarted(X5WebView.this, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			webViewClient.onPageFinished(X5WebView.this, url);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			webViewClient.onLoadResource(X5WebView.this, url);
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			return createX5Response(webViewClient.shouldInterceptRequest(X5WebView.this, url));
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
			return createX5Response(webViewClient.shouldInterceptRequest(X5WebView.this, new MyWebResourceRequest(request)));
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			webViewClient.onReceivedError(X5WebView.this, errorCode, description, failingUrl);
		}

		@Override
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			webViewClient.onReceivedError(X5WebView.this, new MyWebResourceRequest(request), new MyWebResourceError(error));
		}
	}

	public class MyWebResourceRequest implements AWebResourceRequest {
		private final WebResourceRequest webResourceRequest;

		public MyWebResourceRequest(WebResourceRequest request) {
			this.webResourceRequest = request;
		}

		@Override
		public Uri getUrl() {
			return webResourceRequest.getUrl();
		}

		@Override
		public boolean isForMainFrame() {
			return webResourceRequest.isForMainFrame();
		}

		@Override
		public boolean isRedirect() {
			return webResourceRequest.isRedirect();
		}

		@Override
		public boolean hasGesture() {
			return webResourceRequest.hasGesture();
		}

		@Override
		public String getMethod() {
			return webResourceRequest.getMethod();
		}

		@Override
		public Map<String, String> getRequestHeaders() {
			return webResourceRequest.getRequestHeaders();
		}
	}

	public class MyWebResourceError extends AWebResourceError {
		private final WebResourceError webResourceError;

		public MyWebResourceError(WebResourceError request) {
			this.webResourceError = request;
		}

		@Override
		public int getErrorCode() {
			return webResourceError.getErrorCode();
		}

		@Override
		public CharSequence getDescription() {
			return webResourceError.getDescription();
		}
	}

	public class X5WebViewExtension implements IWebView.IX5WebViewExtension {

		private final com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension x5WebViewExtension;

		public X5WebViewExtension(com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension x5WebViewExtension) {
			this.x5WebViewExtension = x5WebViewExtension;
		}

		@Override
		public void setVerticalScrollBarEnabled(boolean enabled) {
			x5WebViewExtension.setVerticalScrollBarEnabled(enabled);
		}

		@Override
		public boolean isVerticalScrollBarEnabled() {
			return x5WebViewExtension.isVerticalScrollBarEnabled();
		}

		@Override
		public boolean isHorizontalScrollBarEnabled() {
			return x5WebViewExtension.isHorizontalScrollBarEnabled();
		}

		@Override
		public void setHorizontalScrollBarEnabled(boolean enabled) {
			x5WebViewExtension.setHorizontalScrollBarEnabled(enabled);
		}

		@Override
		public void setScrollBarFadingEnabled(boolean enabled) {
			x5WebViewExtension.setScrollBarFadingEnabled(enabled);
		}
	}

	public class MyWebSettings extends AWebSettings {
		private final WebSettings webSettings;

		public MyWebSettings(WebSettings webSettings) {
			this.webSettings = webSettings;
		}

		@Override
		public void setCacheMode(int mode) {
			webSettings.setCacheMode(mode);
		}

		@Override
		public int getCacheMode() {
			return webSettings.getCacheMode();
		}

		@Override
		public void setSupportZoom(boolean supportZoom) {
			webSettings.setSupportZoom(supportZoom);
		}

		@Override
		public void setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm layoutAlgorithm) {
			WebSettings.LayoutAlgorithm tLayoutAlgorithm;
			if (layoutAlgorithm == android.webkit.WebSettings.LayoutAlgorithm.NORMAL) {
				tLayoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL;
			} else if (layoutAlgorithm == android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN) {
				tLayoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
			} else if (layoutAlgorithm == android.webkit.WebSettings.LayoutAlgorithm.NARROW_COLUMNS) {
				tLayoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
			} else {
				tLayoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL;
			}
			webSettings.setLayoutAlgorithm(tLayoutAlgorithm);
		}

		@Override
		public void setUseWideViewPort(boolean useWideViewPort) {
			webSettings.setUseWideViewPort(useWideViewPort);
		}

		@Override
		public boolean getUseWideViewPort() {
			return webSettings.getUseWideViewPort();
		}

		@Override
		public android.webkit.WebSettings.LayoutAlgorithm getLayoutAlgorithm() {
			android.webkit.WebSettings.LayoutAlgorithm layoutAlgorithm;
			WebSettings.LayoutAlgorithm tLayoutAlgorithm = webSettings.getLayoutAlgorithm();
			if (tLayoutAlgorithm == WebSettings.LayoutAlgorithm.NORMAL) {
				layoutAlgorithm = android.webkit.WebSettings.LayoutAlgorithm.NORMAL;
			} else if (tLayoutAlgorithm == WebSettings.LayoutAlgorithm.SINGLE_COLUMN) {
				layoutAlgorithm = android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
			} else if (tLayoutAlgorithm == WebSettings.LayoutAlgorithm.NARROW_COLUMNS) {
				layoutAlgorithm = android.webkit.WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
			} else {
				layoutAlgorithm = android.webkit.WebSettings.LayoutAlgorithm.NORMAL;
			}

			return layoutAlgorithm;
		}

		@Override
		public void setAllowFileAccess(boolean allow) {
			webSettings.setAllowFileAccess(allow);
		}

		@Override
		public boolean getAllowFileAccess() {
			return webSettings.getAllowFileAccess();
		}

		@Override
		public void setAllowContentAccess(boolean allow) {
			webSettings.setAllowContentAccess(allow);
		}

		@Override
		public boolean getAllowContentAccess() {
			return webSettings.getAllowContentAccess();
		}

		@Override
		public void setLoadWithOverviewMode(boolean overview) {
			webSettings.setLoadWithOverviewMode(overview);
		}

		@Override
		public boolean getLoadWithOverviewMode() {
			return webSettings.getLoadWithOverviewMode();
		}

		@Override
		public void setGeolocationDatabasePath(String databasePath) {
			webSettings.setGeolocationDatabasePath(databasePath);
		}

		@Override
		public void setAppCacheEnabled(boolean flag) {
			webSettings.setAppCacheEnabled(flag);
		}

		@Override
		public void setAppCachePath(String appCachePath) {
			webSettings.setAppCachePath(appCachePath);
		}

		@Override
		public void setDatabaseEnabled(boolean flag) {
			webSettings.setDatabaseEnabled(flag);
		}

		@Override
		public void setDomStorageEnabled(boolean flag) {
			webSettings.setDomStorageEnabled(flag);
		}

		@Override
		public boolean getDomStorageEnabled() {
			return webSettings.getDomStorageEnabled();
		}

		@Override
		public void setGeolocationEnabled(boolean flag) {
			webSettings.setGeolocationEnabled(flag);
		}

		@Override
		public boolean getJavaScriptEnabled() {
			return webSettings.getJavaScriptEnabled();
		}

		@Override
		public void setLoadsImagesAutomatically(boolean flag) {
			webSettings.setLoadsImagesAutomatically(flag);
		}

		@Override
		public void setBlockNetworkImage(boolean flag) {
			webSettings.setBlockNetworkImage(flag);
		}

		@Override
		public void setMixedContentMode(int mode) {
			webSettings.setMixedContentMode(mode);
		}

		@Override
		public int getMixedContentMode() {
			return webSettings.getMixedContentMode();
		}

		@Override
		public boolean getBlockNetworkImage() {
			return webSettings.getBlockNetworkImage();
		}

		@Override
		public void setBlockNetworkLoads(boolean flag) {
			webSettings.setBlockNetworkLoads(flag);
		}

		@Override
		public boolean getBlockNetworkLoads() {
			return webSettings.getBlockNetworkLoads();
		}

		@Override
		public void setJavaScriptEnabled(boolean flag) {
			webSettings.setJavaScriptEnabled(flag);
		}

		@Override
		public void setJavaScriptCanOpenWindowsAutomatically(boolean flag) {
			webSettings.setJavaScriptCanOpenWindowsAutomatically(flag);
		}

		@Override
		public boolean getJavaScriptCanOpenWindowsAutomatically() {
			return webSettings.getJavaScriptCanOpenWindowsAutomatically();
		}

		@Override
		public void setDefaultTextEncodingName(String defaultTextEncodingName) {
			webSettings.setDefaultTextEncodingName(defaultTextEncodingName);
		}

		@Override
		public String getDefaultTextEncodingName() {
			return webSettings.getDefaultTextEncodingName();
		}

		@Override
		public void setBuiltInZoomControls(boolean enabled) {
			webSettings.setBuiltInZoomControls(enabled);
		}

		@Override
		public boolean getBuiltInZoomControls() {
			return webSettings.getBuiltInZoomControls();
		}

		@Override
		public void setDisplayZoomControls(boolean enabled) {
			webSettings.setDisplayZoomControls(enabled);
		}

		@Override
		public boolean getDisplayZoomControls() {
			return webSettings.getDisplayZoomControls();
		}

		@Override
		public void setUserAgentString(String ua) {
			webSettings.setUserAgentString(ua);
		}

		@Override
		public String getUserAgentString() {
			return webSettings.getUserAgentString();
		}

		@Override
		public void setAppCacheMaxSize(long maxSize) {
			webSettings.setAppCacheMaxSize(maxSize);
		}

		@Override
		public void setRenderPriority(android.webkit.WebSettings.RenderPriority priority) {
			WebSettings.RenderPriority tPriority;
			if (priority == android.webkit.WebSettings.RenderPriority.HIGH) {
				tPriority = WebSettings.RenderPriority.HIGH;
			} else if (priority == android.webkit.WebSettings.RenderPriority.LOW) {
				tPriority = WebSettings.RenderPriority.LOW;
			} else {
				tPriority = WebSettings.RenderPriority.NORMAL;
			}
			webSettings.setRenderPriority(tPriority);
		}
	}
}
