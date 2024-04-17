package com.aijia.webview.webkit.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aijia.webview.bridge.handler.BridgeHandler;
import com.aijia.webview.webkit.AConsoleMessage;
import com.aijia.webview.webkit.AValueCallback;
import com.aijia.webview.webkit.AWebChromeClient;
import com.aijia.webview.webkit.AWebResourceError;
import com.aijia.webview.webkit.AWebResourceRequest;
import com.aijia.webview.webkit.AWebResourceResponse;
import com.aijia.webview.webkit.AWebSettings;
import com.aijia.webview.webkit.AWebViewClient;

import java.util.Map;


public class NativeWebView implements IWebView {
	private final NWebView webView;

	public NativeWebView(Context context) {
		webView = new NWebView(context);
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
	public void evaluateJavascript(String script, ValueCallback<String> resultCallback) {
		webView.evaluateJavascript(script, resultCallback);
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
		webView.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
	}

	@Override
	public IX5WebViewExtension getX5WebViewExtension() {
		return null;
	}

	@Override
	public Context getContext() {
		return webView.getContext();
	}

	@Override
	public void destroy() {
		webView.destroy();
	}

	private WebResourceResponse createNativeResponse(AWebResourceResponse response) {
		if (response == null) {
			return null;
		}
		WebResourceResponse webResourceResponse = new WebResourceResponse(
				response.getMimeType(), response.getEncoding(), response.getData());
		webResourceResponse.setResponseHeaders(response.getResponseHeaders());
		setStatusCodeAndReasonPhrase(webResourceResponse, response.getStatusCode(), response.getReasonPhrase());

		return webResourceResponse;
	}

	/**如果statusCode不满足条件，则不设置给WebResourceResponse对象*/
	public void setStatusCodeAndReasonPhrase(WebResourceResponse webResourceResponse,
	                                         int statusCode, String reasonPhrase) {
		if (statusCode < 100) return;       //statusCode can't be less than 100
		if (statusCode > 599) return;       //statusCode can't be greater than 599.
		if (statusCode > 299 && statusCode < 400) return;//statusCode can't be in the [300, 399] range.
		if (reasonPhrase == null) return;   //reasonPhrase can't be null.
		if (reasonPhrase.trim().isEmpty()) return;//reasonPhrase can't be empty.

		for (int i = 0; i < reasonPhrase.length(); i++) {
			int c = reasonPhrase.charAt(i);
			if (c > 0x7F) return;           //reasonPhrase can't contain non-ASCII characters.
		}

		webResourceResponse.setStatusCodeAndReasonPhrase(statusCode, reasonPhrase);
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
			webChromeClient.onProgressChanged(NativeWebView.this, newProgress);
		}

		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			webChromeClient.onReceivedIcon(NativeWebView.this, icon);
		}

		@Override
		public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
			webChromeClient.onReceivedTouchIconUrl(NativeWebView.this, url, precomposed);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			webChromeClient.onReceivedTitle(NativeWebView.this, title);
		}

		@Override
		public void onRequestFocus(WebView view) {
			webChromeClient.onRequestFocus(NativeWebView.this);
		}

		@Override
		public void onCloseWindow(WebView view) {
			webChromeClient.onCloseWindow(NativeWebView.this);
		}

		@Override
		public View getVideoLoadingProgressView() {
			return webChromeClient.getVideoLoadingProgressView();
		}

		@Override
		public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
			return webChromeClient.onShowFileChooser(NativeWebView.this, new MyValueCallback<Uri[]>(filePathCallback));
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
			return webViewClient.shouldOverrideUrlLoading(NativeWebView.this, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			return webViewClient.shouldOverrideUrlLoading(NativeWebView.this, new MyWebResourceRequest(request));
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			webViewClient.onPageStarted(NativeWebView.this, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			webViewClient.onPageFinished(NativeWebView.this, url);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			webViewClient.onLoadResource(NativeWebView.this, url);
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			return createNativeResponse(webViewClient.shouldInterceptRequest(NativeWebView.this, url));
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
			return createNativeResponse(webViewClient.shouldInterceptRequest(NativeWebView.this, new MyWebResourceRequest(request)));
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			webViewClient.onReceivedError(NativeWebView.this, errorCode, description, failingUrl);
		}

		@Override
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			webViewClient.onReceivedError(NativeWebView.this, new MyWebResourceRequest(request), new MyWebResourceError(error));
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
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				return webResourceRequest.isRedirect();
			}
			return false;
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
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				return webResourceError.getErrorCode();
			}

			return WebViewClient.ERROR_UNKNOWN;
		}

		@Override
		public CharSequence getDescription() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				return webResourceError.getDescription();
			}

			return null;
		}
	}

	public class MyWebSettings extends AWebSettings {
		private final WebSettings webSettings;

		public MyWebSettings(WebSettings webSettings) {
			this.webSettings = webSettings;
		}

		@Override
		public void setAllowFileAccess(boolean allow) {
			webSettings.setAllowFileAccess(allow);
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
		public void setLayoutAlgorithm(WebSettings.LayoutAlgorithm layoutAlgorithm) {
			webSettings.setLayoutAlgorithm(layoutAlgorithm);
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
		public WebSettings.LayoutAlgorithm getLayoutAlgorithm() {
			return webSettings.getLayoutAlgorithm();
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
			//webSettings.setAppCacheEnabled(flag);//Android api33中被移除
		}

		@Override
		public void setAppCachePath(String appCachePath) {
			//webSettings.setAppCachePath(appCachePath);//Android api33中被移除
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
			//webSettings.setAppCacheMaxSize(maxSize);//Android api33中被移除
		}

		@Override
		public void setRenderPriority(WebSettings.RenderPriority priority) {
			webSettings.setRenderPriority(priority);
		}
	}

	private Context generateContext(Context context) {
		return Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 22 ?
				context.createConfigurationContext(new Configuration()) : context;
	}

	/**
	 * 继承原生WebView，将受保护的方法 onScrollChanged 公有化
	 */
	private class NWebView extends WebView {
		public NWebView(Context context) {
			this(context, null);
		}

		public NWebView(Context context, AttributeSet attr) {
			//super(generateContext(context), attr);
			super(context, attr);
		}

		public NWebView(Context context, AttributeSet attr, int defStyleAttr) {
			//super(generateContext(context), attr);
			super(context, attr, defStyleAttr);
		}

		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			super.onScrollChanged(l, t, oldl, oldt);
		}
	}
}
