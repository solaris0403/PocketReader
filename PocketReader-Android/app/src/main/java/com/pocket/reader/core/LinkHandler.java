package com.pocket.reader.core;

import android.util.Log;

/**
 * Created by tony on 5/11/18.
 * 对传进来的链接进行处理，结果是DOM
 */

public class LinkHandler extends AbsHandler{
    @Override
    public void handleRequest(ShareBean shareBean) {
        Log.d(TAG, "LinkHandler handleRequest");
        ShareHelper.getHtml(shareBean, iconCallback);
    }

    private ShareHelper.IconCallback iconCallback = new ShareHelper.IconCallback() {
        @Override
        public void onStart() {

        }

        @Override
        public void onFinish(ShareBean shareBean) {
            if (getSuccessor() != null) {
                getSuccessor().handleRequest(shareBean);
            } else {
                ShareHelper.processShareBean(shareBean);
            }
        }
    };
}
