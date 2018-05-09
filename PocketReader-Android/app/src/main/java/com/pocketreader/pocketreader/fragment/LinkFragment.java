package com.pocketreader.pocketreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pocketreader.pocketreader.R;
import com.pocketreader.pocketreader.bean.Link;
import com.pocketreader.pocketreader.dao.LinkDao;

import java.util.List;

public class LinkFragment extends BaseFragment {
    private static final String TAG = LinkFragment.class.getSimpleName();
    private LinkAdapter mLinkAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mLinkAdapter = new LinkAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mLinkAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinkDao.queryLinks(new LinkDao.OnLinkFindListener() {
            @Override
            public void onSuccess(List<Link> list) {
                mLinkAdapter.update(list);
                Log.e(TAG, "onSuccess:" + list.size());
            }

            @Override
            public void onFail() {
                Log.e(TAG, "onFail");
            }
        });
    }
}
