package com.chenxulu.pm;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;

public class ForeGuardService extends Service {
    private static final int NOTIFICATION_ID = 114441;
    private static final int TIME = 1000 * 60;

    private Handler mHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, getNotification());
        record();
    }

    private void record() {
        mHandler = new Handler(getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mHandler.sendEmptyMessageDelayed(0, TIME);
                System.out.println("---a");
                return true;
            }
        });

        mHandler.sendEmptyMessageDelayed(0, TIME);
    }

    /**
     * create a notification
     *
     * @return
     */
    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.mipmap.sym_def_app_icon);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.app_name));
        return builder.build();
    }
}
