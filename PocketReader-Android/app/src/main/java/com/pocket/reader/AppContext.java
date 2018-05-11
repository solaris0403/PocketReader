package com.pocket.reader;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
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
        initTBS();

    }

    private void initTBS() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(this,  cb);
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
