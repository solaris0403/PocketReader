package com.pocketreader.pocketreader;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by tony on 5/7/18.
 */

public class AppContext extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "96e3c08f314113c10ed6dfbc747155ad");
    }
}
