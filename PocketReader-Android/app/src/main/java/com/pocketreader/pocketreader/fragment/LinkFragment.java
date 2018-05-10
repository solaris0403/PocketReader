package com.pocketreader.pocketreader.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.pocketreader.pocketreader.fragment.adpter.LinkAdapter;
import com.pocketreader.pocketreader.model.MessageEvent;
import com.pocketreader.pocketreader.ui.LinkDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class LinkFragment extends BaseFragment {
    private static final String TAG = LinkFragment.class.getSimpleName();
    private LinkAdapter mLinkAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        initRecyclerView(recyclerView);
        EventBus.getDefault().register(this);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView){
        mLinkAdapter = new LinkAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        mLinkAdapter.setOnItemClickListener(new LinkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Link link) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse(link.getUrl());
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        mLinkAdapter.setOnItemLongClickListener(new LinkAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position, Link link) {
                showDialogFragment(link);
            }
        });
        recyclerView.setAdapter(mLinkAdapter);
    }

    public void showDialogFragment(final Link link) {
        LinkDialogFragment neutralDialogFragment = new LinkDialogFragment();
        neutralDialogFragment.show("删除", "删除该条", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LinkDao.deleteLink(link, new LinkDao.OnDeleteListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, getFragmentManager());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getType() == MessageEvent.TYPE_LINK){
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
