package com.vic.wanandroid.module.chat.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.adapter.MyFragmentPagerAdapter;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.chat.bean.AccountBean;
import com.vic.wanandroid.module.project.bean.ProjectChapter;
import com.vic.wanandroid.module.project.fragment.ProjectArticlesFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment {


    @BindView(R.id.tab_chat)
    TabLayout tabChat;
    @BindView(R.id.vp_chat)
    ViewPager vpChat;
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
    public void onStart() {
        super.onStart();
        httpManage = HttpManage.init(getContext());
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

}
