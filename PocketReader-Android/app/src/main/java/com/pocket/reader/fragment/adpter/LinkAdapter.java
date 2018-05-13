package com.pocket.reader.fragment.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pocket.reader.R;
import com.pocket.reader.model.bean.Link;

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

    public void delete(Link link) {
        for (Link data : mData) {
            if (data.getObjectId().equals(link.getObjectId())) {
                mData.remove(data);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bookmark, parent, false);
        BookmarkViewHolder holder = new BookmarkViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, final int position) {
        final Link link = mData.get(position);
        holder.tvTitle.setText(link.getTitle());
        holder.tvSource.setText(link.getSource());
        holder.tvTime.setText(link.getCreatedAt());
        holder.lytBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(view, position, link);
                }
            }
        });
        holder.lytBookmark.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mListener != null) {
                    mListener.onItemLongClick(view, position, link);
                    return true;
                }
                return false;
            }
        });
        holder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onShareClick(view, position, link);
                }
            }
        });
        holder.tvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCollectClick(view, position, link);
                }
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onDeleteClick(view, position, link);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_source)
        TextView tvSource;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.lyt_bookmark)
        RelativeLayout lytBookmark;
        @BindView(R.id.tv_share)
        Button tvShare;
        @BindView(R.id.tv_collect)
        Button tvCollect;
        @BindView(R.id.tv_delete)
        Button tvDelete;

        BookmarkViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Link link);

        void onItemLongClick(View view, int position, Link link);

        void onShareClick(View view, int position, Link link);

        void onCollectClick(View view, int position, Link link);

        void onDeleteClick(View view, int position, Link link);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
