package com.aijia.framework.widgets.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * @author Aijia
 * @date 2024/4/14
 * @desc 自定义WebView，用于加载图片多、复杂的 WebView
 */
public class MyWebView extends WebView {

    private WebSettings mWebSettings;

    public MyWebView(Context context) {
        super(context);
        initView();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @SuppressLint({"ObsoleteSdkInt", "SetJavaScriptEnabled"})
    private void initView() {
        mWebSettings = getSettings();
        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultFontSize(16);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setGeolocationEnabled(true);   //允许访问地址

        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setFocusable(true);
        setHorizontalScrollBarEnabled(false);
        setDrawingCacheEnabled(true);

        //加载https的兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以
            mWebSettings.setMixedContentMode(mWebSettings.getMixedContentMode());
            //mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //先加载页面再加载图片，这里先禁止图片加载
        mWebSettings.setLoadsImagesAutomatically(Build.VERSION.SDK_INT >= 19);

        setWebViewClient(mWebViewClient);
        setWebChromeClient(mWebChromeClient);
    }


    WebViewClient mWebViewClient = new WebViewClient() {
        //https ssl证书问题，如果没有https的问题可以注释掉
/*      @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // 接受所有网站的证书,Google不通过
//            handler.proceed();

            //使用下面的兼容写法
            final SslErrorHandler mHandler;
            mHandler= handler;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("SSL validation failed");
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mHandler.proceed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mHandler.cancel();
                }
            });
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        mHandler.cancel();
                        dialog.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }*/

        //页面加载完成，展示图片
        @Override
        public void onPageFinished(WebView view, String url) {
            if (!mWebSettings.getLoadsImagesAutomatically()) {
                mWebSettings.setLoadsImagesAutomatically(true);
            }
        }

        //在当前的WebView中跳转到新的url
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (mListener != null) mListener.onInnerLinkChecked();

            if (!TextUtils.isEmpty(url)) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (mListener != null) mListener.onWebLoadError();
        }
    };


    private boolean isNeedExe = true;

    WebChromeClient mWebChromeClient = new WebChromeClient() {
        //获取html的title标签
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mListener != null) mListener.titleChange(title);
            super.onReceivedTitle(view, title);
        }

        //获取页面加载的进度
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mListener != null) mListener.progressChange(newProgress);
            super.onProgressChanged(view, newProgress);

            if (newProgress > 95 && isNeedExe) {
                isNeedExe = !isNeedExe;

                if (newProgress == 100) {
                    //注入js代码测量webview高度
                    loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
                }
            }


        }

        // 指定源的网页内容在没有设置权限状态下尝试使用地理位置API。
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            boolean allow = true;   // 是否允许origin使用定位API
            boolean retain = false; // 内核是否记住这次制授权
            callback.invoke(origin, true, false);
        }

        // 之前调用 onGeolocationPermissionsShowPrompt() 申请的授权被取消时，隐藏相关的UI。
        @Override
        public void onGeolocationPermissionsHidePrompt() {

        }
    };


    private OnWebChangeListener mListener;

    public interface OnWebChangeListener {
        void titleChange(String title);

        void progressChange(int progress);

        void onInnerLinkChecked();

        void onWebLoadError();
    }

    public void setOnWebChangeListener(OnWebChangeListener listener) {
        mListener = listener;
    }

    /**
     * 暴露方法，是否滑动到底部
     */
    public boolean isScrollBottom() {
        if (getContentHeight() * getScale() == (getHeight() + getScrollY())) {
            //说明已经到底了
            return true;
        } else {
            return false;
        }
    }

}
