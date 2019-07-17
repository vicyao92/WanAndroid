package com.vic.wanandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.vic.wanandroid.adapter.MyFragmentPagerAdapter;
import com.vic.wanandroid.base.BaseActivity;
import com.vic.wanandroid.module.account.LoginActivity;
import com.vic.wanandroid.module.account.bean.LoginBean;
import com.vic.wanandroid.module.chat.fragment.ChatFragment;
import com.vic.wanandroid.module.collect.activity.ArticleCollectActivity;
import com.vic.wanandroid.module.collect.activity.WebsiteCollectActivity;
import com.vic.wanandroid.module.collect.fragment.NormalDialog;
import com.vic.wanandroid.module.home.fragment.HomeFragment;
import com.vic.wanandroid.module.knowledge.fragment.KnowledgeChildFragment;
import com.vic.wanandroid.module.navigate.fragment.NavigationFragment;
import com.vic.wanandroid.module.project.fragment.ProjectFragment;
import com.vic.wanandroid.utils.CacheUtils;
import com.vic.wanandroid.utils.DatabaseHelper;
import com.vic.wanandroid.utils.LoginUtils;

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

    private CircleImageView avatar;
    private View headerView;
    private List<Fragment> pagerList = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        databaseHelper = DatabaseHelper.init(MainActivity.this);

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
        viewPager.setOffscreenPageLimit(1);
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

        headerView = navigation.inflateHeaderView(R.layout.nav_header);
        textView = headerView.findViewById(R.id.tv_state);
        avatar = headerView.findViewById(R.id.img_avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.start(MainActivity.this);
                drawer.closeDrawers();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        MenuItem item = navigation.getMenu().findItem(R.id.nav_logout);
        if (LoginUtils.getInstance().isLogin()){
            LoginBean loginBean = LoginUtils.getInstance().getLoginBean(databaseHelper.findAll(LoginBean.class));
            textView.setText(loginBean.getUsername());
            avatar.setClickable(false);
            item.setVisible(true);
        } else {
            item.setVisible(false);
            avatar.setClickable(true);
        }

        navigation.setNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.nav_collect:
                    if (LoginUtils.getInstance().isLogin()) {
                        intent = new Intent(MainActivity.this, ArticleCollectActivity.class);
                        startActivity(intent);
                    } else {
                        LoginActivity.start(MainActivity.this);
                    }
                    break;
                case R.id.nav_logout:
                    NormalDialog dialog = new NormalDialog("确定要退出登录么？");
                    dialog.setPositiveClickListener(() -> logout(item));
                    dialog.show(getSupportFragmentManager(), "ExitConfirmDialog");
                    break;
                case R.id.nav_collect_website:
                    if (LoginUtils.getInstance().isAlreradyLogin(MainActivity.this)) {
                        WebsiteCollectActivity.start(MainActivity.this);
                    }
                    break;
                case R.id.nav_clear_cache:
                    try {
                        clearCache();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            drawer.closeDrawers();
            return true;
        });
    }

    private void logout(MenuItem item) {
        LoginUtils.getInstance().logout();
        textView.setText("未登陆");
        avatar.setClickable(true);
        item.setVisible(false);
    }

    private void clearCache() throws Exception {
        String cacheSize = CacheUtils.getTotalCacheSize(MainActivity.this);
        NormalDialog dialog = new NormalDialog("确定清除所有缓存吗（" + cacheSize + "）");
        if (cacheSize.equals("0K")) {
            dialog.setPositiveClickListener(() -> dialog.dismiss());
        } else {
            dialog.setPositiveClickListener(() -> CacheUtils.clearAllCache(MainActivity.this));
        }
        dialog.show(getSupportFragmentManager(), "ExitConfirmDialog");
    }
}
