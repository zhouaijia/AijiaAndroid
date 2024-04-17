package com.aijia.webview.bridge;


import com.aijia.webview.bridge.callback.CallBackFunction;

public interface WebViewJavascriptBridge {
	
	void send(String data);

	void send(String data, CallBackFunction responseCallback);

}
