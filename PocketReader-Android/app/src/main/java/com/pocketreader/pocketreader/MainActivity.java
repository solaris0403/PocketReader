package com.pocketreader.pocketreader;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pocketreader.pocketreader.account.AccountUtils;
import com.pocketreader.pocketreader.fragment.BaseFragment;
import com.pocketreader.pocketreader.fragment.HotFragment;
import com.pocketreader.pocketreader.fragment.LinkFragment;
import com.pocketreader.pocketreader.fragment.MineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.ic_portrait)
    CircleImageView icPortrait;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ic_search)
    SearchView icSearch;

    private LinkFragment mLinkFragment;
    private HotFragment mHotFragment;
    private MineFragment mMineFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!AccountUtils.check()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar(toolbar);
    }


    private void initViews() {
        mLinkFragment = new LinkFragment();
        mHotFragment = new HotFragment();
        mMineFragment = new MineFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.content, mLinkFragment)
                .add(R.id.content, mHotFragment)
                .add(R.id.content, mMineFragment)
                .hide(mHotFragment)
                .hide(mMineFragment)
                .show(mLinkFragment)
                .commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void onShowFragment(BaseFragment fragment) {
        mFragmentManager.beginTransaction()
                .hide(mLinkFragment)
                .hide(mHotFragment)
                .hide(mMineFragment)
                .show(fragment)
                .commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    onShowFragment(mLinkFragment);
                    return true;
                case R.id.navigation_dashboard:
                    onShowFragment(mHotFragment);
                    return true;
                case R.id.navigation_notifications:
                    onShowFragment(mMineFragment);
                    return true;
            }
            return false;
        }
    };

    @OnClick({R.id.ic_portrait, R.id.drawer_layout, R.id.ic_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_portrait:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.ic_search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }
}
