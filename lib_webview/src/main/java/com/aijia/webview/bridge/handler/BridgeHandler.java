package com.aijia.webview.bridge.handler;


import com.aijia.webview.bridge.callback.CallBackFunction;

public interface BridgeHandler {
	void handler(String data, CallBackFunction function);
}
