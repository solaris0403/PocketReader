package com.pocketreader.pocketreader.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pocketreader.pocketreader.ColorUtils;
import com.pocketreader.pocketreader.R;
import com.pocketreader.pocketreader.bean.Bookmark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 5/7/18.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {

    private Context mContext;
    private List<Bookmark> mData;

    public BookmarkAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    public void update(List<Bookmark> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bookmark, parent, false);
        BookmarkViewHolder holder = new BookmarkViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        Bookmark bookmark = mData.get(position);
        holder.mItemView.setBackgroundColor(ColorUtils.randomColor());
        holder.mTvTitle.setText(bookmark.getTitle());
        holder.mTvUrl.setText(bookmark.getUrl());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class BookmarkViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mItemView;
        private TextView mTvTitle;
        private TextView mTvUrl;

        public BookmarkViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView.findViewById(R.id.lyt_bookmark);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvUrl = itemView.findViewById(R.id.tv_url);
        }
    }
}
