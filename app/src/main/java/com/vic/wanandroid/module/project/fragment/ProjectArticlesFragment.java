package com.vic.wanandroid.module.project.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vic.wanandroid.MainActivity;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.home.adapter.HomeAdapter;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.module.project.bean.ProjectArticles;
import com.vic.wanandroid.utils.SwipeRefreshUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectArticlesFragment extends BaseFragment {
    @BindView(R.id.rv_project_articles)
    RecyclerView rvProjectArticles;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    private List<ArticleBean> articleDatas = new ArrayList<>();
    private int cid;
    private final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private HomeAdapter mAdapter;
    private boolean isOver;

    public ProjectArticlesFragment(int cid) {
        this.cid = cid;
    }

    private MainActivity activity;

    @Override
    public int getResId() {
        return R.layout.fragment_project_articles;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpManage = HttpManage.init(getContext());
        activity = (MainActivity) getActivity();
        activity.createProgressBar(getActivity());
        activity.showProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestArticleData(currentPage);
        initRv();
        srlRefresh = SwipeRefreshUtils.initRefreshLayout(srlRefresh, getContext());
        srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isOver = false;
                currentPage = PAGE_START;
                articleDatas.clear();
                requestArticleData(currentPage);
            }
        });
    }

    private void requestArticleData(int page) {
        httpManage.getProjectArticles(new BaseObserver<ProjectArticles>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                activity.hideProgressBar();
                srlRefresh.finishRefresh(false);
            }

            @Override
            protected void onHandleSuccess(ProjectArticles projectArticlesBean) {
                isOver = projectArticlesBean.isOver();
                articleDatas.addAll(projectArticlesBean.getDatas());
                if (currentPage == PAGE_START) {
                    mAdapter.setNewData(projectArticlesBean.getDatas());
                } else {
                    mAdapter.addData(projectArticlesBean.getDatas());
                }
                currentPage += 1;
                mAdapter.loadMoreComplete();
                activity.hideProgressBar();
                srlRefresh.finishRefresh(2500);
            }
        }, currentPage, cid);
    }

    private void initRv() {
        mAdapter = new HomeAdapter(R.layout.item_rv_articles, articleDatas, getContext());
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleBean article = articleDatas.get(position);
                int id = article.getId();
                String title = article.getTitle();
                String targetUrl = article.getLink();
                WebActivity.start(getContext(), id, title, targetUrl, 0);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isOver) {
                    mAdapter.loadMoreEnd();
                } else {
                    requestArticleData(currentPage);
                }
            }
        }, rvProjectArticles);
        rvProjectArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProjectArticles.setAdapter(mAdapter);
    }

}
