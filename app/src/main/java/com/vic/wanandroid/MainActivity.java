package com.vic.wanandroid;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.vic.wanandroid.adapter.MyFragmentPagerAdapter;
import com.vic.wanandroid.base.BaseActivity;
import com.vic.wanandroid.fragment.BookFragment;
import com.vic.wanandroid.fragment.ChatFragment;
import com.vic.wanandroid.module.home.fragment.HomeFragment;
import com.vic.wanandroid.fragment.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.drawer)
    DrawerLayout drawer;

    private List<Fragment> pagerList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pagerList.add(new HomeFragment());
        pagerList.add(new BookFragment());
        pagerList.add(new ProjectFragment());
        pagerList.add(new ChatFragment());
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),pagerList));
        viewPager.setCurrentItem(0);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return true;
            }
        });
    }


}
