package com.pocket.reader.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pocket.reader.R;
import com.pocket.reader.data.LinkManager;
import com.pocket.reader.fragment.adpter.LinkAdapter;
import com.pocket.reader.model.CategoryDao;
import com.pocket.reader.model.bean.Category;
import com.pocket.reader.model.bean.Link;
import com.pocket.reader.model.dao.LinkDao;
import com.pocket.reader.ui.CategoryDialog;
import com.pocket.reader.ui.LinkDialogFragment;
import com.pocket.reader.utils.ToastUtils;
import com.pocket.reader.webview.BrowserActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class LinkFragment extends BaseFragment {
    private static final String TAG = LinkFragment.class.getSimpleName();
    private LinkAdapter mLinkAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        initRecyclerView(recyclerView);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        mLinkAdapter = new LinkAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mLinkAdapter.setOnItemClickListener(new LinkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Link link) {
                Intent intent = new Intent(getActivity(), BrowserActivity.class);
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
                CategoryDao.findCategory(new FindListener<Category>() {
                    @Override
                    public void done(final List<Category> list, BmobException e) {
                        if (e != null) {
                            ToastUtils.show(e.toString());
                        }else{
                            CategoryDialog.showCategoryDialog(getActivity(), list, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Category category = list.get(i);
                                    LinkDao.collectLink(link.getObjectId(), category.getId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                mLinkAdapter.delete(link);
                                            } else {
                                                ToastUtils.show("收藏失败");
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onDeleteClick(View view, int position, final Link link) {
                LinkDao.deleteLink(link.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            mLinkAdapter.delete(link);
                        } else {
                            ToastUtils.show("删除失败");
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
        LinkDao.queryLinks();
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
        List<Link> links = new ArrayList<>();
        for (Link link : LinkManager.getInstance().getLinks()) {
            if (link.getCategory() == 0) {
                links.add(link);
            }
        }
        mLinkAdapter.update(links);
    }
}
