package com.pocketreader.pocketreader.bean;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by tony on 5/8/18.
 */

public interface IBmobDataListener {
    void onStart();

    void onSuccess(List<Bookmark> bookmarks);

    void onFailed(BmobException e);

    void onFinish();
}
