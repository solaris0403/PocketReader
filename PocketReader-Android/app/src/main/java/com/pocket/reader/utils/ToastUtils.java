package com.pocket.reader.utils;

import android.widget.Toast;

import com.pocket.reader.AppContext;

/**
 * Created by tony on 5/14/18.
 */

public class ToastUtils {
    private static Toast sToast;

    public static void show(CharSequence charSequence) {
        cancel();
        sToast = Toast.makeText(AppContext.getContext(), charSequence, Toast.LENGTH_SHORT);
        sToast.show();
    }

    private static void cancel() {
        if (sToast != null) {
            sToast.cancel();
        }
    }
}
