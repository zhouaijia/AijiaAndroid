package com.aijia.webview.webkit;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import com.aijia.webview.webkit.view.IWebView;


public class AWebChromeClient {

	public AWebChromeClient() {}

	public boolean onConsoleMessage(AConsoleMessage consoleMessage) {
		return false;
	}

	public void onGeolocationPermissionsHidePrompt() {}

	public void onHideCustomView() {}

	public void onProgressChanged(IWebView webView, int newProgress) {}

	public void onReceivedIcon(IWebView webView, Bitmap icon) {}

	public void onReceivedTouchIconUrl(IWebView webView, String url, boolean precomposed) {}

	public void onReceivedTitle(IWebView webView, String title) {}

	public void onRequestFocus(IWebView webView) {}

	public void onCloseWindow(IWebView webView) {}

	public View getVideoLoadingProgressView() { return null; }

	public void openFileChooser(AValueCallback<Uri> uploadFile, String acceptType, String capture) {
		uploadFile.onReceiveValue(null);
	}

	public boolean onShowFileChooser(IWebView webView, AValueCallback<Uri[]> filePathCallback) {
		return false;
	}

}
