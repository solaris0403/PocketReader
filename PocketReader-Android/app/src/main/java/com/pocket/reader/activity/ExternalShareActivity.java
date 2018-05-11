package com.pocket.reader.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pocket.reader.BaseActivity;
import com.pocket.reader.core.ShareHelper;


/**
 * Created by tony on 5/9/18.
 */

public class ExternalShareActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareHelper.processIntent(getIntent());
    }
}
