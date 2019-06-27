package com.vic.wanandroid;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.vic.wanandroid.adapter.MyFragmentPagerAdapter;
import com.vic.wanandroid.base.BaseActivity;
import com.vic.wanandroid.module.account.LoginActivity;
import com.vic.wanandroid.module.chat.fragment.ChatFragment;
import com.vic.wanandroid.module.knowledge.fragment.KnowledgeChildFragment;
import com.vic.wanandroid.module.navigate.fragment.NavigationFragment;
import com.vic.wanandroid.module.project.fragment.ProjectFragment;
import com.vic.wanandroid.module.home.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    private List<Fragment> pagerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        pagerList.add(new HomeFragment());
        pagerList.add(new KnowledgeChildFragment());
        pagerList.add(new NavigationFragment());
        pagerList.add(new ChatFragment());
        pagerList.add(new ProjectFragment());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigation.setSelectedItemId(R.id.menu_home);
                        break;
                    case 1:
                        bottomNavigation.setSelectedItemId(R.id.menu_knowledge);
                        break;
                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.menu_navi);
                        break;
                    case 3:
                        bottomNavigation.setSelectedItemId(R.id.menu_chat);
                        break;
                    case 4:
                        bottomNavigation.setSelectedItemId(R.id.menu_project);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), pagerList));
        viewPager.setCurrentItem(0);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.menu_knowledge:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.menu_navi:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.menu_chat:
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.menu_project:
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return true;
            }
        });

        View headerView = navigation.inflateHeaderView(R.layout.nav_header);
        CircleImageView avatar = headerView.findViewById(R.id.img_avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.start(MainActivity.this);
            }
        });
    }

}
