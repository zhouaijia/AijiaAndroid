package com.aijia.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebSettings;

import com.aijia.webview.bridge.Message;
import com.aijia.webview.bridge.WebViewJavascriptBridge;
import com.aijia.webview.bridge.callback.CallBackFunction;
import com.aijia.webview.bridge.handler.BridgeHandler;
import com.aijia.webview.bridge.handler.DefaultHandler;
import com.aijia.webview.bridge.utils.BridgeUtil;
import com.aijia.webview.webkit.AWebViewClient;
import com.aijia.webview.webkit.view.IWebView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义 BridgeWebView
 * 支持各种WebView库的插拔、更换
 * 支持JSBridge
 */
@SuppressLint("SetJavaScriptEnabled")
public class BridgeWebView extends WebView implements WebViewJavascriptBridge {
	private final String TAG = "BridgeWebView";
	public static final String toLoadJs = "WebViewJavascriptBridge.js";
	Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();
	Map<String, BridgeHandler> messageHandlers = new HashMap<String, BridgeHandler>();
	BridgeHandler defaultHandler = new DefaultHandler();
	private List<Message> startupMessage = new ArrayList<Message>();
	private long uniqueId = 0;

	public BridgeWebView(Context context) {
		this(context, null);
	}

	public BridgeWebView(@NotNull Context context, @Nullable AttributeSet attr) {
		this(context, attr, 0);
	}

	public BridgeWebView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		init();
	}

	public List<Message> getStartupMessage() {
		return startupMessage;
	}

	public void setStartupMessage(List<Message> startupMessage) {
		this.startupMessage = startupMessage;
	}

	/**
	 * 
	 * @param handler
	 *            default handler,handle messages send by js without assigned handler name,
     *            if js message has handler name, it will be handled by named handlers registered by native
	 */
	public void setDefaultHandler(BridgeHandler handler) {
       this.defaultHandler = handler;
	}

    private void init() {
	    //webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
	    setVerticalScrollBarEnabled(false);
	    setHorizontalScrollBarEnabled(false);
	    getSettings().setAllowFileAccess(true);
	    getSettings().setAllowContentAccess(true);
	    getSettings().setAppCacheEnabled(true);
	    getSettings().setDatabaseEnabled(true);
		// 开启 localStorage
	    getSettings().setDomStorageEnabled(true);
		// 设置支持javascript
	    getSettings().setJavaScriptEnabled(true);
		// 进行缩放
	    getSettings().setBuiltInZoomControls(true);
		// 设置UserAgent
	    getSettings().setUserAgentString("Aijia Apps" + getSettings().getUserAgentString());
	    // 提高渲染的优先级
	    getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
	    //getSettings().setLoadsImagesAutomatically(true);
	    // 把图片的加载和渲染放在最后
	    //getSettings().setBlockNetworkImage(true);
	    //getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

	    setWebViewClient(generateBridgeWebViewClient());
	}

    protected AWebViewClient generateBridgeWebViewClient() {
        return new BridgeWebViewClient();
    }

	void handlerReturnData(String url) {
		String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
		CallBackFunction f = responseCallbacks.get(functionName);
		String data = BridgeUtil.getDataFromReturnUrl(url);
		if (f != null) {
			f.onCallBack(data);
			responseCallbacks.remove(functionName);
		}
	}

	@Override
	public void send(String data) {
		send(data, null);
	}

	@Override
	public void send(String data, CallBackFunction responseCallback) {
		doSend(null, data, responseCallback);
	}

	private void doSend(String handlerName, String data, CallBackFunction responseCallback) {
		Message m = new Message();
		if (!TextUtils.isEmpty(data)) {
			m.setData(data);
		}
		if (responseCallback != null) {
			String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
			responseCallbacks.put(callbackStr, responseCallback);
			m.setCallbackId(callbackStr);
		}
		if (!TextUtils.isEmpty(handlerName)) {
			m.setHandlerName(handlerName);
		}
		queueMessage(m);
	}

	private void queueMessage(Message m) {
		if (startupMessage != null) {
			startupMessage.add(m);
		} else {
			dispatchMessage(m);
		}
	}

	void dispatchMessage(Message m) {
        String messageJson = m.toJson();
        //escape special characters for json string
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
	        loadUrl(javascriptCommand);
        }
    }

	void flushMessageQueue() {
		if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
			loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, new CallBackFunction() {

				@Override
				public void onCallBack(String data) {
					// deserializeMessage
					List<Message> list = null;
					try {
						list = Message.toArrayList(data);
					} catch (Exception e) {
                        e.printStackTrace();
						return;
					}
					if (list == null || list.size() == 0) {
						return;
					}
					for (int i = 0; i < list.size(); i++) {
						Message m = list.get(i);
						String responseId = m.getResponseId();
						// 是否是response
						if (!TextUtils.isEmpty(responseId)) {
							CallBackFunction function = responseCallbacks.get(responseId);
							String responseData = m.getResponseData();
							function.onCallBack(responseData);
							responseCallbacks.remove(responseId);
						} else {
							CallBackFunction responseFunction = null;
							// if had callbackId
							final String callbackId = m.getCallbackId();
							if (!TextUtils.isEmpty(callbackId)) {
								responseFunction = new CallBackFunction() {
									@Override
									public void onCallBack(String data) {
										Message responseMsg = new Message();
										responseMsg.setResponseId(callbackId);
										responseMsg.setResponseData(data);
										queueMessage(responseMsg);
									}
								};
							} else {
								responseFunction = new CallBackFunction() {
									@Override
									public void onCallBack(String data) {
										// do nothing
									}
								};
							}
							BridgeHandler handler;
							if (!TextUtils.isEmpty(m.getHandlerName())) {
								handler = messageHandlers.get(m.getHandlerName());
							} else {
								handler = defaultHandler;
							}
							if (handler != null){
								handler.handler(m.getData(), responseFunction);
							}
						}
					}
				}
			});
		}
	}

	public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
		loadUrl(jsUrl);
		responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
	}

	/**
	 * register handler,so that javascript can call it
	 *
	 * @param handlerName
	 * @param handler
	 */
	public void registerHandler(String handlerName, BridgeHandler handler) {
		if (handler != null) {
			messageHandlers.put(handlerName, handler);
		}
	}

	/**
	 * call javascript registered handler
	 *
	 * @param handlerName
	 * @param data
	 * @param callBack
	 */
	public void callHandler(String handlerName, String data, CallBackFunction callBack) {
		doSend(handlerName, data, callBack);
	}

	/**
	 * 自定义 WebViewClient
	 *
	 * 与前端数据的交互处理
	 * 向H5页面中注入本地js文件：WebViewJavascriptBridge.js
	 */
	private class BridgeWebViewClient extends AWebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(IWebView view, String url) {
			try {
				url = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
				handlerReturnData(url);
				return true;
			} else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) {
				flushMessageQueue();
				return true;
			} else {
				return super.shouldOverrideUrlLoading(view, url);
			}
		}

		@Override
		public void onPageStarted(IWebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(IWebView view, String url) {
			super.onPageFinished(view, url);

			BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);

			if (getStartupMessage() != null) {
				for (Message m : getStartupMessage()) {
					dispatchMessage(m);
				}
				setStartupMessage(null);
			}
		}

		@Override
		public void onReceivedError(IWebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
}
