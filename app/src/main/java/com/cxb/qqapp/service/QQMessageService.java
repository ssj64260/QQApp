package com.cxb.qqapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.cxb.qqapp.R;
import com.cxb.qqapp.ui.QQMainActivity;
import com.cxb.qqapp.utils.ThreadPoolUtil;

import java.util.concurrent.TimeUnit;

/**
 * qq消息服务
 */

public class QQMessageService extends Service {
    private final int REQUEST_CODE_QQ_MESSAGE = 0;//QQ消息通知

    String[] messages = {
            "我在睡觉",
            "我在打龙之谷地狱巢穴",
            "我在写Android代码",
            "私はワンパンマンアニメーションを見ていました"
    };
    private int messageNumber = 0;//消息数量
    private int startId;

    private NotificationManager mNotificationManager;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        ThreadPoolUtil.getInstache().scheduledRate(new Runnable() {
            @Override
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendQQMessageNotification();
                    }
                });
            }
        }, 5, 20, TimeUnit.SECONDS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        messageNumber = 0;
        this.startId = startId;
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationManager.cancel(REQUEST_CODE_QQ_MESSAGE);
        ThreadPoolUtil.getInstache().scheduledShutDown(0);
    }

    private void sendQQMessageNotification() {
        if (messageNumber > 3) {
//            stopSelf(startId);
            return;
        }

        messageNumber++;
        String title = "COKU";
        if (messageNumber > 1) {
            title += " (" + messageNumber + "条新消息)";
        }
        String message = messages[(int) (Math.random() * 10) % 4];

        Intent intent = new Intent();
        intent.setClass(this, QQMainActivity.class);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setTicker("收到了一条消息")
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_qq_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_coku_avatar))
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setLights(0xff00ff00, 2000, 2000)
                .setVibrate(new long[]{0, 300, 200, 300})
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.qq_message));

        Notification notification = mBuilder.build();
        mNotificationManager.notify(REQUEST_CODE_QQ_MESSAGE, notification);
    }
}
