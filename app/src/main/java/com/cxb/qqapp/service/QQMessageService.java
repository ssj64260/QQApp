package com.cxb.qqapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cxb.qqapp.R;
import com.cxb.qqapp.db.LiteOrmHelper;
import com.cxb.qqapp.model.QQMessageBean;
import com.cxb.qqapp.ui.message.ChatPageActivity;
import com.cxb.qqapp.utils.DateTimeUtils;
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
    private LiteOrmHelper liteOrmHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        liteOrmHelper = new LiteOrmHelper(this);

        ThreadPoolUtil.getInstache().scheduledRate(new Runnable() {
            @Override
            public void run() {
                sendQQMessageNotification();
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
        liteOrmHelper.closeDB();
    }

    private void sendQQMessageNotification() {
        if (messageNumber > 3) {
            return;
        }

        messageNumber++;
        String qqNumber = "79953676";
        String username = "COKU";
        int qqAvatar = "79953676".equals(qqNumber) ? R.drawable.ic_coku_avatar : R.drawable.ic_co_avatar;
        String message = messages[(int) (Math.random() * 10) % 4];

        final QQMessageBean qqMessage = new QQMessageBean();
        qqMessage.setQqNumber(qqNumber);
        qqMessage.setAvatarRes(qqAvatar);
        qqMessage.setName(username);
        qqMessage.setContent(message);
        qqMessage.setTime(DateTimeUtils.getEnShortTime());

        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                liteOrmHelper.save(qqMessage);
            }
        });

        Intent intent = new Intent();
        intent.setClass(this, ChatPageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(QQMessageBean.TAG_QQ_MESSAGE, qqMessage);
        intent.putExtras(bundle);

        String title = username;
        if (messageNumber > 1) {
            title += " (" + messageNumber + "条新消息)";
        }

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setTicker("收到了一条消息")
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_qq_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), qqAvatar))
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
