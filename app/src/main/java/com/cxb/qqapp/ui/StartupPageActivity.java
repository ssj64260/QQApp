package com.cxb.qqapp.ui;

import android.content.Intent;
import android.os.Bundle;

import com.cxb.qqapp.ui.message.QQLoginActivity;
import com.cxb.qqapp.utils.ThreadPoolUtil;

import java.util.concurrent.TimeUnit;

/**
 * 启动页
 */

public class StartupPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThreadPoolUtil.getInstache().scheduled(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(StartupPageActivity.this, QQLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onBackPressed() {

    }
}
