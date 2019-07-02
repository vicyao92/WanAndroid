package com.vic.wanandroid.module.chat.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.adapter.MyFragmentPagerAdapter;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.chat.bean.AccountBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment {


    @BindView(R.id.tab_chat)
    TabLayout tabChat;
    @BindView(R.id.vp_chat)
    ViewPager vpChat;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ChatArticlesFragment articlesFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private List<AccountBean> accounts = new ArrayList<>();

    public ChatFragment() {
    }

    @Override
    public int getResId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        httpManage = HttpManage.init(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolbar();
    }


    @Override
    public void onStart() {
        super.onStart();
        httpManage.getAccountBean(new BaseObserver<List<AccountBean>>(getContext()) {
            @Override
            protected void onHandleSuccess(List<AccountBean> accountBeans) {
                accounts = accountBeans;
                initViewPager(accounts);
                initTab(accounts);
            }
        });
    }

    private void initTab(List<AccountBean> datas) {
        tabChat.setupWithViewPager(vpChat);
        for (int i = 0; i < datas.size(); i++) {
            tabChat.getTabAt(i).setText(datas.get(i).getName());
        }
        Log.d("module_project", tabChat.getTabCount() + "");
        //tabChat.addOnTabSelectedListener(tabSelectedListener);
    }

    private void initViewPager(List<AccountBean> datas) {
        int cid;
        if (fragments.size() == 0) {
            for (int i = 0; i < accounts.size(); i++) {
                cid = accounts.get(i).getId();
                articlesFragment = new ChatArticlesFragment(cid);
                fragments.add(articlesFragment);
            }
            vpChat.setAdapter(new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments));
            vpChat.setCurrentItem(0);
        }
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setTitle(R.string.wechat);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer);
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }
}
