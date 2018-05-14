package com.pocket.reader.fragment;

import android.support.v4.app.Fragment;

import com.pocket.reader.data.LinkManager;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by tony on 5/7/18.
 */

public class BaseFragment extends Fragment implements Observer{
    @Override
    public void onResume() {
        super.onResume();
        LinkManager.getInstance().addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LinkManager.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
