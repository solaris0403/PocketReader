package com.pocket.reader.data;

import android.content.Context;

import com.pocket.reader.db.DBHelper;

/**
 * Created by tony on 5/14/18.
 */

public class LinkTableDao {
    private DBHelper helper;

    public LinkTableDao(Context context) {
        helper = DBHelper.getHelper(context);
    }
}
