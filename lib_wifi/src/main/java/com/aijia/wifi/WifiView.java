package com.aijia.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.Objects;

public class WifiView extends ImageView implements LifecycleObserver {
    private static final String TAG = WifiView.class.getName();

    private WifiManager mWifiManager;
    private WifiState mWifiState = WifiState.WIFI_OFF;
    private WifiInfo mConnectionInfo;
    private String mStatusText = "";

    private Lifecycle mLifecycle;

    public WifiView(Context context) {
        this(context, null);
    }

    public WifiView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WifiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setImageResource(R.drawable.list_wifi_status);

        if (getDrawable() != null) {
            mWifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager == null) {
                throw new RuntimeException("WifiManager is not supposed to be null");
            }
            updateWifiState();
        }
    }

    public void setLifecycleOwner(@NonNull LifecycleOwner owner) {
        if (mLifecycle != null) mLifecycle.removeObserver(this);
        mLifecycle = owner.getLifecycle();
        mLifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void registerWifiReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        getContext().registerReceiver(broadcastReceiver, filter);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void unRegisterWifiReceiver() {
        getContext().unregisterReceiver(broadcastReceiver);
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Objects.equals(action, WifiManager.RSSI_CHANGED_ACTION)
                    || Objects.equals(action, WifiManager.WIFI_STATE_CHANGED_ACTION)
                    || Objects.equals(action, WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                updateWifiState();
            }
        }
    };

    private void updateWifiState() {
        mConnectionInfo = mWifiManager.getConnectionInfo();

        if (mConnectionInfo != null) {
            // 计算当前的信号强度
            int signalLevel = WifiManager.calculateSignalLevel(mConnectionInfo.getRssi(), 5);
            mWifiState = WifiState.getByValue(signalLevel);
            mStatusText ="连接至:" + mConnectionInfo.getSSID();
        } else {
            if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                mWifiState = WifiState.WIFI_DISCONNECT;
                mStatusText = "WIFI无连接";
            }
        }
        switch (mWifiManager.getWifiState()) {
            case WifiManager.WIFI_STATE_UNKNOWN:
            case WifiManager.WIFI_STATE_DISABLED:
            case WifiManager.WIFI_STATE_DISABLING:
                mWifiState = WifiState.WIFI_OFF;
                mStatusText = "WIFI已关闭";
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                mWifiState = WifiState.WIFI_DISCONNECT;
                mStatusText = "WIFI无连接";
                break;
            default:
                break;
        }
        Log.d(TAG, "wifi level = " + mWifiState.ordinal() + ", state: "
                +mStatusText+", WifiState: "+mWifiManager.getWifiState());

        // 设置图片
        getDrawable().setLevel(mWifiState.ordinal());
    }

}
