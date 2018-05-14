package com.pocket.reader.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pocket.reader.BaseActivity;
import com.pocket.reader.R;
import com.pocket.reader.activity.adpter.CategoryAdapter;
import com.pocket.reader.model.CategoryDao;
import com.pocket.reader.model.bean.Category;
import com.pocket.reader.model.dao.LinkDao;
import com.pocket.reader.ui.FolderDialogFragment;
import com.pocket.reader.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class CategoryActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private CategoryAdapter mCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        initToolbar();
        initViews();
        initData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.string_collection);
        initToolbar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(this);
    }

    private void initViews() {
        mCategoryAdapter = new CategoryAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mCategoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Category category) {
                Intent intent = new Intent(CategoryActivity.this, CollectionActivity.class);
                intent.putExtra(CollectionActivity.KEY_CATEGORY_ID, category.getId());
                intent.putExtra(CollectionActivity.KEY_CATEGORY_NAME, category.getName());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(View view, int position, Category category) {
                CategoryDao.deleteCategory(category.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e != null) {
                            ToastUtils.show("删除失败");
                        } else {
                            initData();
                        }
                    }
                });
                LinkDao.deleteLinksByCategoryId(category.getId());
            }

            @Override
            public void onRenameClick(View view, int position, final Category category) {
                final FolderDialogFragment folderDialogFragment = new FolderDialogFragment();
                folderDialogFragment.show("重命名", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = folderDialogFragment.getmEdtFolder().getText().toString();
                        rename(category, name);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, getSupportFragmentManager());
            }
        });
        recyclerView.setAdapter(mCategoryAdapter);
    }

    private void rename(Category category, String newName) {
        CategoryDao.renameCategory(category.getObjectId(), newName, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    ToastUtils.show("重命名失败");
                } else {
                    initData();
                }
            }
        });
    }


    private void initData() {
        CategoryDao.findCategory(new FindListener<Category>() {
            @Override
            public void done(List<Category> list, BmobException e) {
                if (e != null) {
                    ToastUtils.show("分类信息获取失败");
                } else {
                    mCategoryAdapter.update(list);
                }
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
                folderDialogFragment.show("创建分类", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = folderDialogFragment.getmEdtFolder().getText().toString();
                        createCategory(name);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, getSupportFragmentManager());
                break;
        }
        return false;
    }

    private void createCategory(String name) {
        CategoryDao.createCategory(name, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null) {
                    ToastUtils.show(e.toString());
                } else {
                    initData();
                }
            }
        });
    }
}
