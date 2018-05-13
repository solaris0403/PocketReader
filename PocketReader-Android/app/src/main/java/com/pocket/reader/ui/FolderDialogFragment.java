package com.pocket.reader.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.pocket.reader.R;

/**
 * Created by tony on 2018/5/12.
 */

public class FolderDialogFragment extends DialogFragment {
    private DialogInterface.OnClickListener positiveCallback;

    private DialogInterface.OnClickListener negativeCallback;

    private String title;

    private String message;

    public EditText mEdtFolder;

    public EditText getmEdtFolder() {
        return mEdtFolder;
    }

    public void show(String title, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback, FragmentManager fragmentManager) {
        this.title = title;
        this.message = message;
        this.positiveCallback = positiveCallback;
        this.negativeCallback = negativeCallback;
        show(fragmentManager, "ButtonDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setView(R.layout.dialog_folder);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_folder, null);
        mEdtFolder = view.findViewById(R.id.edt_folder);
        builder.setView(view);
        builder.setPositiveButton("确定", positiveCallback);
        builder.setNegativeButton("取消", negativeCallback);
        return builder.create();
    }
}
