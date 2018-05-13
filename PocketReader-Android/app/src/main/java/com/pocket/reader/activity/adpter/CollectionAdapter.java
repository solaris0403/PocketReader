package com.pocket.reader.activity.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pocket.reader.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tony on 2018/5/12.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {
    private Context mContext;
    private List<Category> mData;

    public CollectionAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    public void update(List<Category> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_collection, parent, false);
        CollectionViewHolder holder = new CollectionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CollectionViewHolder holder, final int position) {
        final Category category = mData.get(position);
        holder.tvTitle.setText(category.getName());
        holder.viewCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(view, position, category);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Category category);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position, Category category);
    }

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    static class CollectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_folder)
        ImageView imgFolder;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.view_collection)
        LinearLayout viewCollection;

        CollectionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}