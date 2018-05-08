package com.pocketreader.pocketreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pocketreader.pocketreader.PLog;
import com.pocketreader.pocketreader.R;
import com.pocketreader.pocketreader.bean.Bookmark;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BookmarkFragment extends BaseFragment {

    private BookmarkAdapter mBookmarkAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mBookmarkAdapter = new BookmarkAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mBookmarkAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BmobQuery<Bookmark> query = new BmobQuery<>();
        query.setLimit(50);
        query.findObjects(new FindListener<Bookmark>() {
            @Override
            public void done(List<Bookmark> list, BmobException e) {
                if (e == null) {
                    mBookmarkAdapter.update(list);
                    PLog.d("查询成功：共" + list.size() + "条数据。");
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
