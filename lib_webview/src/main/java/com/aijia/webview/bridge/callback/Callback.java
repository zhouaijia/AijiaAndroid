package com.aijia.webview.bridge.callback;

public interface Callback<T> {
    void onSuccess(T data);

    void onError(int code,String message);
}
