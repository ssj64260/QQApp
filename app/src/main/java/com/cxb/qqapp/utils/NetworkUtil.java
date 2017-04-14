package com.cxb.qqapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态工具类
 */

public class NetworkUtil {

    public static final int NETWORK_NONE = -1;//没有连接网络
    public static final int NETWORK_MOBILE = 0;//移动网络
    public static final int NETWORK_WIFI = 1;//无线网络

    public static final int NETWORK_TYPE_CMNET = 101;//CMNET
    public static final int NETWORK_TYPE_CMWAP = 102;//CMWAP

    //判断是否有网络连接
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //判断网络类型
    public static int checkNetWorkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

    //判断移动网络类型
    public static int checkAPNType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            String typeName = activeNetworkInfo.getExtraInfo().toLowerCase();

            if (typeName.equals("cmnet")) {
                return NETWORK_TYPE_CMNET;
            } else if (typeName.equals("cmwap")) {
                return NETWORK_TYPE_CMWAP;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

}
