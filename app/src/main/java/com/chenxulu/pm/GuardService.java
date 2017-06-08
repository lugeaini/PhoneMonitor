package com.chenxulu.pm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GuardService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startMainService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startMainService();
    }

    private void startMainService() {
        Intent intent = new Intent(this, PhoneService.class);
        startService(intent);
    }
}
