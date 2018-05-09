package com.pocketreader.pocketreader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pocketreader.pocketreader.PLog;
import com.pocketreader.pocketreader.R;
import com.pocketreader.pocketreader.dao.LinkDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HotFragment extends BaseFragment {

    @BindView(R.id.btn_add)
    Button btnAdd;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        LinkDao.saveLink("www.baidu.com", new LinkDao.OnLinkListener() {
            @Override
            public void onSuccess() {
                PLog.e("add onSuccess");
            }

            @Override
            public void onFail() {
                PLog.e("add onFail");
            }
        });
    }
}
