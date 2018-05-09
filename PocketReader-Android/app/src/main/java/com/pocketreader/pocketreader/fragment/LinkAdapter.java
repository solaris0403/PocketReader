package com.pocketreader.pocketreader.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pocketreader.pocketreader.R;
import com.pocketreader.pocketreader.bean.Link;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tony on 5/7/18.
 */

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.BookmarkViewHolder> {
    private Context mContext;
    private List<Link> mData;

    public LinkAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    public void update(List<Link> data) {
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
        Link link = mData.get(position);
//        holder.lytBookmark.setBackgroundColor(ColorUtils.randomColor());
//        holder.tvTitle.setText(bookmark.getTitle());
        holder.tvSource.setText("人品日报");
        holder.tvTime.setText("昨天10:30");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class BookmarkViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_source)
        TextView tvSource;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.img_picture)
        ImageView imgPicture;
        @BindView(R.id.lyt_bookmark)
        RelativeLayout lytBookmark;

        BookmarkViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
