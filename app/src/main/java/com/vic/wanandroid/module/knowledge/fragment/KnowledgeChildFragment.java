package com.vic.wanandroid.module.knowledge.fragment;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vic.wanandroid.MainActivity;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.knowledge.activity.KnowledgeArticlesActivity;
import com.vic.wanandroid.module.knowledge.adapter.KnowledgeAdapter;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeSystemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeChildFragment extends BaseFragment {


    @BindView(R.id.rv_knowledge_child)
    RecyclerView rvKnowledgeChild;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private List<KnowledgeSystemBean> knowledgeSystemBeanList = new ArrayList<>();
    private KnowledgeAdapter adapter;
    private MainActivity activity;
    public KnowledgeChildFragment() {

    }

    @Override
    public int getResId() {
        return R.layout.fragment_knowledge_child;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        httpManage = HttpManage.init(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        requestDataFromWeb();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolbar();
        initRv();
        activity = (MainActivity) getActivity();
        activity.createProgressBar(getActivity());
        activity.showProgressBar();
    }

    private void requestDataFromWeb() {
        httpManage.getKnowledgeSystem(new BaseObserver<List<KnowledgeSystemBean>>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                activity.hideProgressBar();
            }

            @Override
            protected void onHandleSuccess(List<KnowledgeSystemBean> knowledgeSystemBeans) {
                knowledgeSystemBeanList.addAll(knowledgeSystemBeans);
                adapter.setNewData(knowledgeSystemBeans);
                activity.hideProgressBar();
            }
        });
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setTitle(R.string.knowledge);
        }
    }

    private void initRv() {
        adapter = new KnowledgeAdapter(R.layout.item_rv_knowledge_child, knowledgeSystemBeanList, getContext());
        adapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener() {
            @Override
            public void onClick(KnowledgeSystemBean bean, int pos) {
                KnowledgeArticlesActivity.start(getActivity(), pos, bean);
            }

        });
        adapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener() {
            @Override
            public void onClick(KnowledgeSystemBean bean, int pos) {
                KnowledgeArticlesActivity.start(getActivity(), pos, bean);
            }
        });
        rvKnowledgeChild.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKnowledgeChild.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
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
