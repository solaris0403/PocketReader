package com.pocket.reader.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pocket.reader.BaseActivity;
import com.pocket.reader.R;
import com.pocket.reader.account.AccountUtils;
import com.pocket.reader.account.User;
import com.pocket.reader.activity.adpter.Category;
import com.pocket.reader.activity.adpter.CollectionAdapter;
import com.pocket.reader.fragment.adpter.LinkAdapter;
import com.pocket.reader.model.bean.Link;
import com.pocket.reader.model.dao.LinkDao;
import com.pocket.reader.ui.FolderDialogFragment;
import com.pocket.reader.ui.LinkDialogFragment;
import com.pocket.reader.webview.BrowserActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class CollectionActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
//    private CollectionAdapter mCollectionAdapter;
    private LinkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.string_collection);
        initToolbar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(this);
    }

    private void initViews() {
        adapter = new LinkAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new LinkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Link link) {
                Intent intent= new Intent(CollectionActivity.this, BrowserActivity.class);
                Uri content_url = Uri.parse(link.getUrl());
                intent.setData(content_url);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position, Link link) {
//                LinkDialogFragment neutralDialogFragment = new LinkDialogFragment();
//                neutralDialogFragment.show("删除", "删除该条", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                }, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        LinkDao.collectLink(link.getObjectId());
//                    }
//                }, getSupportFragmentManager());
            }

            @Override
            public void onShareClick(View view, int position, Link link) {
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, link.getUrl());
                startActivity(Intent.createChooser(textIntent, "分享"));
            }

            @Override
            public void onCollectClick(View view, int position, Link link) {

            }

            @Override
            public void onDeleteClick(View view, int position, final Link link) {
                LinkDao.deleteLink(link.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            adapter.delete(link);
                        }else{
                            Toast.makeText(CollectionActivity.this, "删除失败", 0).show();
                        }
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        LinkDao.queryCollectLink(new LinkDao.OnLinkFindListener() {
            @Override
            public void onSuccess(List<Link> list) {
                if (list != null) {
                    adapter.update(list);
                }
            }

            @Override
            public void onFail() {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create:
                final FolderDialogFragment folderDialogFragment = new FolderDialogFragment();
                folderDialogFragment.show("ddd", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = folderDialogFragment.getmEdtFolder().getText().toString();
                        createFolder(name);
                        Toast.makeText(CollectionActivity.this,name, Toast.LENGTH_SHORT).show();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, getSupportFragmentManager());

                break;
//            case R.id.action_add:
//                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_setting:
//                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
//                break;
        }
        return false;
    }
    private void createFolder(String name){
        User user = new User();
        List folder = user.getCategory();
        if (folder == null){
            folder = new ArrayList();
        }
        folder.add(name);
        user.setCategory(folder);
        AccountUtils.update(user, new AccountUtils.OnUserListener() {
            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void onFail(BmobException e) {

            }
        });
    }
}
