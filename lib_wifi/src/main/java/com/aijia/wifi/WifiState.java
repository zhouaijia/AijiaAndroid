package com.aijia.wifi;

public enum WifiState {
    //信号强度0
    WIFI_SINGAL_0,

    //信号强度1
    WIFI_SINGAL_1,

    //信号强度2
    WIFI_SINGAL_2,

    //信号强度3
    WIFI_SINGAL_3,

    //信号强度4
    WIFI_SINGAL_4,

    //WIFI关闭
    WIFI_OFF,

    //WIFI无连接
    WIFI_DISCONNECT;

    public static WifiState getByValue(int value) {
        switch (value) {
            case 0:
                return WIFI_SINGAL_0;
            case 1:
                return WIFI_SINGAL_1;
            case 2:
                return WIFI_SINGAL_2;
            case 3:
                return WIFI_SINGAL_3;
            case 4:
                return WIFI_SINGAL_4;
            case 5:
                return WIFI_OFF;
            case 6:
                return WIFI_DISCONNECT;
            default:
                break;
        }
        return WIFI_OFF;
    }
}
