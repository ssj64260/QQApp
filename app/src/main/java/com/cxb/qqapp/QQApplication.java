package com.cxb.qqapp;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by lenovo on 17/4/14.
 */

public class QQApplication extends Application {

    private static QQApplication INStANCE;

    public QQApplication() {
        INStANCE = this;
    }

    public static QQApplication getInstance() {
        if (INStANCE == null) {
            synchronized (QQApplication.class) {
                if (INStANCE == null) {
                    INStANCE = new QQApplication();
                }
            }
        }
        return INStANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init(getString(R.string.app_name));//初始化Log显示的TAG

    }
}
