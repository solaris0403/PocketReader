package com.pocket.reader.activity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.pocket.reader.core.ShareHelper;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ShareHelper.processIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }
}
