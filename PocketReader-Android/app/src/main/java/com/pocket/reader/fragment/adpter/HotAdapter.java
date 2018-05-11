package com.pocket.reader.fragment.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocket.reader.R;
import com.pocket.reader.model.bean.Link;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tony on 5/10/18.
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.HotViewHolder> {
    private Context mContext;
    private List<Link> mData;

    public HotAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
    }

    public void update(List<Link> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        HotViewHolder holder = new HotViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HotViewHolder holder, int position) {
        final Link link = mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class HotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.img_picture)
        ImageView imgPicture;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_summary)
        TextView tvSummary;
        @BindView(R.id.img_collect)
        ImageView imgCollect;
        @BindView(R.id.tv_collect)
        TextView tvCollect;
        @BindView(R.id.img_like)
        ImageView imgLike;
        @BindView(R.id.img_comment)
        ImageView imgComment;
        @BindView(R.id.img_forward)
        ImageView imgForward;

        HotViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
