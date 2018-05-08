package com.pocketreader.pocketreader.bean;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by tony on 5/8/18.
 */

public abstract class AbsDataLoad implements IBmobDataListener{
    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(List<Bookmark> bookmarks) {

    }

    @Override
    public void onFailed(BmobException e) {

    }

    @Override
    public void onFinish() {

    }
}
