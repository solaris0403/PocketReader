package com.pocket.reader;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import cn.bmob.v3.Bmob;

/**
 * Created by tony on 5/7/18.
 */

public class AppContext extends Application {
    private static AppContext sInstance;

    public static AppContext getContext() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initBmob();
        initUMeng();

    }

    private void initBmob() {
        Bmob.initialize(this, "96e3c08f314113c10ed6dfbc747155ad");
    }

    private void initUMeng() {
        UMConfigure.init(this, "5af141d3f29d98753800001b", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.setLogEnabled(true);
    }
}
