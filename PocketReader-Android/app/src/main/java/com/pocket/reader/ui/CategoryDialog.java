package com.pocket.reader.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.pocket.reader.model.bean.Category;

import java.util.List;

/**
 * Created by tony on 5/14/18.
 */

public class CategoryDialog {
    public static void showCategoryDialog(Context context, List<Category> categories, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选择一个分类");
        String[] items = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            items[i] = categories.get(i).getName();
        }
        builder.setItems(items, listener);
        builder.show();
    }
}
