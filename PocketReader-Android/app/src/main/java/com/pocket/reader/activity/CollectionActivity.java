package com.pocket.reader.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pocket.reader.BaseActivity;
import com.pocket.reader.R;
import com.pocket.reader.activity.adpter.CollectionAdapter;
import com.pocket.reader.data.LinkManager;
import com.pocket.reader.model.CategoryDao;
import com.pocket.reader.model.bean.Category;
import com.pocket.reader.model.bean.Link;
import com.pocket.reader.model.dao.LinkDao;
import com.pocket.reader.ui.CategoryDialog;
import com.pocket.reader.utils.ToastUtils;
import com.pocket.reader.webview.BrowserActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class CollectionActivity extends BaseActivity {
    private static final String TAG = CollectionActivity.class.getSimpleName();
    public static final String KEY_CATEGORY_NAME = "key_category_name";
    public static final String KEY_CATEGORY_ID = "key_category_id";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String mName;
    private Integer mId;
    private Toolbar mToolbar;

    private CollectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        initToolbar();
        initViews();
        initData();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        mAdapter = new CollectionAdapter(this);
        mAdapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Link link) {
                Intent intent = new Intent(CollectionActivity.this, BrowserActivity.class);
                Uri content_url = Uri.parse(link.getUrl());
                intent.setData(content_url);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(View view, int position, final Link link) {
                LinkDao.deleteLink(link.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e != null) {
                            ToastUtils.show("删除失败");
                        } else {
                            LinkDao.queryLinks();
                        }
                    }
                });
            }

            @Override
            public void onMoveClick(View view, int position, final Link link) {
                CategoryDao.findCategory(new FindListener<Category>() {
                    @Override
                    public void done(final List<Category> list, BmobException e) {
                        if (e != null) {
                            ToastUtils.show(e.toString());
                        } else {
                            CategoryDialog.showCategoryDialog(CollectionActivity.this, list, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Category category = list.get(i);
                                    LinkDao.collectLink(link.getObjectId(), category.getId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                LinkDao.queryLinks();
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
            public void onShareClick(View view, int position, Link link) {
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, link.getUrl());
                startActivity(Intent.createChooser(textIntent, "分享"));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        mName = intent.getStringExtra(KEY_CATEGORY_NAME);
        mId = intent.getIntExtra(KEY_CATEGORY_ID, 0);
        getSupportActionBar().setTitle(mName);
        LinkDao.queryLinks();
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
        List<Link> links = new ArrayList<>();
        for (Link link : LinkManager.getInstance().getLinks()) {
            if (mId == link.getCategory().intValue()) {
                links.add(link);
            }
        }
        mAdapter.update(links);
    }
}
