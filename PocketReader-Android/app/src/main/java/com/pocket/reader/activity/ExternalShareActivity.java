package com.pocket.reader.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.pocket.reader.BaseActivity;
import com.pocket.reader.core.ShareHelper;


/**
 * Created by tony on 5/9/18.
 */

public class ExternalShareActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setWindow();
        super.onCreate(savedInstanceState);
        ShareHelper.processIntent(getIntent());
//        setWindow();
    }

    // 设置状态栏
    protected void setWindow() {
        Window window=getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.flags=WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        wl.alpha=0f;//这句就是设置窗口里控件的透明度的．０.０全透明．１.０不透明．
        window.setAttributes(wl);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
    }
}
