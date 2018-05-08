package com.pocketreader.pocketreader;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.pocketreader.pocketreader.account.AccountUtils;
import com.pocketreader.pocketreader.fragment.BaseFragment;
import com.pocketreader.pocketreader.fragment.BookmarkFragment;
import com.pocketreader.pocketreader.fragment.HotFragment;
import com.pocketreader.pocketreader.fragment.MineFragment;

public class MainActivity extends BaseActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    onShowFragment(mBookmarkFragment);
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

    private BookmarkFragment mBookmarkFragment;
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
        mBookmarkFragment = new BookmarkFragment();
        mHotFragment = new HotFragment();
        mMineFragment = new MineFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.content, mBookmarkFragment)
                .add(R.id.content, mHotFragment)
                .add(R.id.content, mMineFragment)
                .hide(mHotFragment)
                .hide(mMineFragment)
                .show(mBookmarkFragment)
                .commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void onShowFragment(BaseFragment fragment) {
        mFragmentManager.beginTransaction()
                .hide(mBookmarkFragment)
                .hide(mHotFragment)
                .hide(mMineFragment)
                .show(fragment)
                .commit();
    }
}
