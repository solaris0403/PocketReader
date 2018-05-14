package com.pocket.reader.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.pocket.reader.model.bean.Link;
import com.pocket.reader.webview.BrowserActivity;

/**
 * Created by tony on 5/14/18.
 */

public class BrowserUtils {
    public static void openLink(Context context, Link link){
        Intent intent = new Intent(context, BrowserActivity.class);
        Uri content_url = Uri.parse(link.getUrl());
        intent.setData(content_url);
        context.startActivity(intent);
    }
}
