package com.pocket.reader.utils;

import android.content.Context;
import android.content.Intent;

import com.pocket.reader.model.bean.Link;

/**
 * Created by tony on 5/14/18.
 */

public class ShareUtils {
    public static void share(Context context, Link link){
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, link.getUrl());
        context.startActivity(Intent.createChooser(textIntent, "分享"));
    }
}
