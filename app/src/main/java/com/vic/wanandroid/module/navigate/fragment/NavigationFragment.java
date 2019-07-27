package com.vic.wanandroid.module.navigate.fragment;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vic.wanandroid.MainActivity;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.navigate.adapter.NavigationAdapter;
import com.vic.wanandroid.module.navigate.bean.NavigationBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends BaseFragment {


    @BindView(R.id.rv_knowledge_child)
    RecyclerView rvKnowledgeChild;
    private List<NavigationBean> navigations = new ArrayList<>();
    private NavigationAdapter adapter;
    private MainActivity activity;
    public NavigationFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.createProgressBar(getActivity());
        activity.showProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        httpManage = HttpManage.init(getContext());
        requestDataFromWeb();
        initRv();
    }

    private void requestDataFromWeb() {
        httpManage.getNavigationList(new BaseObserver<List<NavigationBean>>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                activity.hideProgressBar();
            }

            @Override
            protected void onHandleSuccess(List<NavigationBean> navigationBeans) {
                navigations = navigationBeans;
                adapter.setNewData(navigations);
                adapter.loadMoreEnd();
                activity.hideProgressBar();
            }
        });
    }

    @Override
    public int getResId() {
        return R.layout.fragment_navigation;
    }

    private void initRv() {
        adapter = new NavigationAdapter(R.layout.item_rv_knowledge_child, navigations);
        adapter.setOnItemClickListener(new NavigationAdapter.OnItemClickListener() {
            @Override
            public void onClick(NavigationBean bean, int pos) {
                int id = bean.getArticles().get(pos).getId();
                String url = bean.getArticles().get(pos).getLink();
                String title = bean.getArticles().get(pos).getTitle();
                WebActivity.start(getActivity(), id, title, url, 1);
            }
        });
        rvKnowledgeChild.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKnowledgeChild.setAdapter(adapter);
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
