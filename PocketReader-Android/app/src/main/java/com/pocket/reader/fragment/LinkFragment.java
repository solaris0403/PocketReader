package com.pocket.reader.fragment;

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
import android.widget.Toast;

import com.pocket.reader.R;
import com.pocket.reader.event.MessageEvent;
import com.pocket.reader.fragment.adpter.LinkAdapter;
import com.pocket.reader.model.bean.Link;
import com.pocket.reader.model.dao.LinkDao;
import com.pocket.reader.ui.LinkDialogFragment;
import com.pocket.reader.webview.BrowserActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

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
                Intent intent= new Intent(getActivity(), BrowserActivity.class);
                Uri content_url = Uri.parse(link.getUrl());
                intent.setData(content_url);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position, Link link) {
                showDialogFragment(link);
            }

            @Override
            public void onShareClick(View view, int position, final Link link) {
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, link.getUrl());
                startActivity(Intent.createChooser(textIntent, "分享"));
            }

            @Override
            public void onCollectClick(View view, int position, final Link link) {
                LinkDao.collectLink(link.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            mLinkAdapter.delete(link);
                        }else{
                            Toast.makeText(getActivity(), "收藏失败", 0).show();
                        }
                    }
                });
            }

            @Override
            public void onDeleteClick(View view, int position, final Link link) {
                LinkDao.deleteLink(link.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            mLinkAdapter.delete(link);
                        }else{
                            Toast.makeText(getActivity(), "删除失败", 0).show();
                        }
                    }
                });
            }
        });
        recyclerView.setAdapter(mLinkAdapter);
    }

    public void showDialogFragment(final Link link) {
        LinkDialogFragment neutralDialogFragment = new LinkDialogFragment();
        neutralDialogFragment.show("删除", "删除该条", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
