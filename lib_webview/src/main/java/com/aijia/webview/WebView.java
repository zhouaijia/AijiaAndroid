package com.aijia.webview;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ValueCallback;

import com.aijia.webview.webkit.AWebChromeClient;
import com.aijia.webview.webkit.AWebSettings;
import com.aijia.webview.webkit.AWebViewClient;
import com.aijia.webview.webkit.WebViewHelper;
import com.aijia.webview.webkit.view.IWebView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义 WebView，底层的WebView库可插拔、可更换，如腾讯 X5 WebView、原生 WebView。
 * 除了腾讯 X5 WebView和原生 WebView，若想添加其它WebView库，则需要在目录webkit/view
 * 下面增加相应的类实现。
 * 不可用于xml布局文件中，因为本WebView的父类不是View或ViewGroup
 */
public class WebView implements IWebView {

    private final String TAG = "WebView";

    private IWebView webView;

    public WebView(Context context) {
        this(context, null);
    }

    public WebView(@NotNull Context context, @Nullable AttributeSet attr) {
        this(context, attr, 0);
    }

    public WebView(Context context, AttributeSet attr, int defStyle) {
        // 通过反射判断腾讯X5 WebView sdk是否存在，若存在，则再用反射创建X5 WebView实例。
        // 若X5不存在，则直接使用Android原生的WebView
        webView = WebViewHelper.makeWebView(context, attr, defStyle);
    }

    @Override
    public View getView() {
        return webView.getView();
    }

    @Override
    public Context getContext() {
        return webView.getContext();
    }

    @Override
    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void destroy() {
        webView.destroy();
    }

    @Override
    public void reload() {
        webView.reload();
    }

    @Override
    public void clearCache(boolean b) {
        webView.clearCache(b);
    }

    @Override
    public void clearHistory() {
        webView.clearHistory();
    }

    @Override
    public void setLayerType(int layerType, Paint paint) {
        webView.setLayerType(layerType, paint);
    }

    @Override
    public void setWebChromeClient(AWebChromeClient webChromeClient) {
        webView.setWebChromeClient(webChromeClient);
    }

    @Override
    public void setWebViewClient(AWebViewClient webViewClient) {
        webView.setWebViewClient(webViewClient);
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
        return webView.getSettings();
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
    public void setWebContentsDebuggingEnabled(boolean enabled) {
        webView.setWebContentsDebuggingEnabled(enabled);
    }

    @Override
    public void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        webView.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
    }

    @Override
    public IX5WebViewExtension getX5WebViewExtension() {
        return webView.getX5WebViewExtension();
    }

}
