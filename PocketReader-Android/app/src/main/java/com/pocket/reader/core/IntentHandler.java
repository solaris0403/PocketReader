package com.pocket.reader.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by tony on 5/11/18.
 * 处理传送进来的Intent,结果是一个文本
 */

public class IntentHandler extends AbsHandler {
    @Override
    public void handleRequest(ShareBean shareBean) {
        if (shareBean.getType() == ShareBean.TYPE_INVALID) {
            ShareHelper.processShareBean(shareBean);
        } else {
            if (getSuccessor() != null) {
                getSuccessor().handleRequest(shareBean);
            } else {
                ShareHelper.processShareBean(shareBean);
            }
        }
    }

    public void handleRequest(Intent intent) {
        Log.d(TAG, "IntentHandler handleRequest");
        ShareBean shareBean = new ShareBean();
        shareBean.setType(ShareBean.TYPE_INVALID);
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null) { //分享单个
                if (type.startsWith("text/")) {//有可能是txt文件
                    if (!isFileType(intent)) {
                        shareBean.setType(ShareBean.TYPE_TEXT);
                        shareBean.setExtra(getExtra(intent));
                    }
                } else if ("message/rfc822".equals(type)) {
                    shareBean.setType(ShareBean.TYPE_TEXT);
                    shareBean.setExtra(getExtra(intent));
                }
            }
        }
        handleRequest(shareBean);
    }

    public static String getExtra(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (TextUtils.isEmpty(sharedText)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object bundleExt = bundle.get(Intent.EXTRA_TEXT);
                if (bundleExt != null) {
                    if (bundleExt instanceof String) {
                        sharedText = (String) bundleExt;
                    } else if (bundleExt instanceof Spannable) {
                        Spannable spannable = (Spannable) bundleExt;
                        sharedText = spannable.toString();
                    }
                }
            }
        }
        return sharedText;
    }

    private static boolean isFileType(Intent intent) {
        boolean isFile = false;
        Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (fileUri != null && !TextUtils.isEmpty(String.valueOf(fileUri))) {
            isFile = true;
        }
        return isFile;
    }
}
