package com.aijia.webview.bridge;

public class BridgeResult<T> {
    public int errorCode;
    public String message;
    public T data;

    public static <T> BridgeResult<T> ok(T data) {
        BridgeResult<T> res = new BridgeResult<>();
        res.errorCode = 0;
        res.message = "";
        res.data = data;

        return res;
    }

    public static BridgeResult<String> error(String errMsg) {
        BridgeResult<String> res = new BridgeResult<>();
        res.errorCode = -1;
        res.message = errMsg;
        res.data = "";

        return res;
    }

    public static BridgeResult<String> error(int errCode, String errMsg) {
        BridgeResult<String> res = new BridgeResult<>();
        res.errorCode = errCode;
        res.message = errMsg;
        res.data = "";

        return res;
    }
}
