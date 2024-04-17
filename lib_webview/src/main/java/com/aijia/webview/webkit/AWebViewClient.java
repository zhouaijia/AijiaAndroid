package com.aijia.webview.webkit;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.aijia.webview.webkit.view.IWebView;

public abstract class AWebViewClient {
	public static final int ERROR_UNKNOWN = -1;

	public boolean shouldOverrideUrlLoading(IWebView view, String url) {
		return false;
	}

	public boolean shouldOverrideUrlLoading(IWebView view, AWebResourceRequest request) {
		return shouldOverrideUrlLoading(view, request.getUrl().toString());
	}

	public void onPageStarted(IWebView view, String url, Bitmap favicon) {}

	public void onPageFinished(IWebView view, String url) {}

	public void onLoadResource(IWebView view, String url) {}

	public AWebResourceResponse shouldInterceptRequest(IWebView view, String url) {
		return null;
	}

	public @Nullable AWebResourceResponse shouldInterceptRequest(@Nullable IWebView view, @Nullable AWebResourceRequest request) {
		return shouldInterceptRequest(view, request.getUrl().toString());
	}

	public void onReceivedError(IWebView view, int errorCode, String description, String failingUrl) {}

	public void onReceivedError(IWebView view, AWebResourceRequest request, AWebResourceError error) {
		if (request.isForMainFrame()) {
			onReceivedError(view,
					error.getErrorCode(), error.getDescription().toString(),
					request.getUrl().toString());
		}
	}


}
