package com.aijia.webview.bridge.handler;


import android.text.TextUtils;

import com.aijia.webview.bridge.BridgeResult;
import com.aijia.webview.bridge.callback.CallBackFunction;
import com.aijia.webview.bridge.callback.Callback;
import com.aijia.webview.bridge.utils.JsonUtils;

public abstract class AbstractBridgeHandler<InputParameter, OutputParameter> implements BridgeHandler {
    protected static final int ERROR_CODE = -1;

    @Override
    public void handler(String data, CallBackFunction function) {
        InputParameter inputParameter = null;

        if (!TextUtils.isEmpty(data)) {
            try {
                inputParameter = JsonUtils.fromJson(data, getInputParameterType());
            } catch (Throwable t) {
                t.printStackTrace();
                BridgeResult<String> res = BridgeResult.error(ERROR_CODE,
                        "--非法json数据格式--"+t.getMessage());
                function.onCallBack(JsonUtils.toJsonString(res));
                return;
            }
        }

        handle(inputParameter, new Callback<OutputParameter>() {
            @Override
            public void onSuccess(OutputParameter output) {
                BridgeResult<OutputParameter> res = BridgeResult.ok(output);

                function.onCallBack(JsonUtils.toJsonString(res));
            }

            @Override
            public void onError(int code, String message) {
                BridgeResult<String> res = BridgeResult.error(code, message);

                function.onCallBack(JsonUtils.toJsonString(res));
            }
        });
    }

    public abstract Class<InputParameter> getInputParameterType();

    public abstract void handle(InputParameter input, Callback<OutputParameter> callback);
}
