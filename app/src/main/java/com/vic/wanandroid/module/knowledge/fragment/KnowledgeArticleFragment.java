package com.vic.wanandroid.module.knowledge.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.module.knowledge.activity.KnowledgeArticlesActivity;
import com.vic.wanandroid.module.knowledge.adapter.KnowledgeArticleAdapter;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeArticleBean;
import com.vic.wanandroid.utils.SwipeRefreshUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeArticleFragment extends BaseFragment {

    @BindView(R.id.rv_knowledge_articles)
    RecyclerView rvKnowledgeArticles;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    private List<ArticleBean> articles = new ArrayList<>();
    private KnowledgeArticleAdapter adapter;
    private static final int START_PAGE = 0;
    private int currentPage = START_PAGE;
    private int cid;
    private boolean isOver;

    public KnowledgeArticleFragment(int cid) {
        this.cid = cid;
    }

    private KnowledgeArticlesActivity activity;

    @Override
    public int getResId() {
        return R.layout.fragment_knowledge_article;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpManage = HttpManage.init(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRv();
        srlRefresh = SwipeRefreshUtils.initRefreshLayout(srlRefresh, getContext());
        srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = START_PAGE;
                isOver = false;
                articles.clear();
                requestDataFromWeb(currentPage, cid);
            }
        });
        activity = (KnowledgeArticlesActivity) getActivity();
        activity.createProgressBar(getActivity());
        activity.showProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestDataFromWeb(currentPage, cid);
    }

    private void initRv() {
        adapter = new KnowledgeArticleAdapter(R.layout.item_rv_articles, articles);
        adapter.setOnItemClickListener((adapter, view, position) -> WebActivity.start(getContext()
                , articles.get(position).getId(), articles.get(position).getTitle(), articles.get(position).getLink(), 0));
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> {
            if (isOver) {
                adapter.loadMoreEnd();
            } else {
                requestDataFromWeb(currentPage, cid);
            }
        }, rvKnowledgeArticles);
        rvKnowledgeArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKnowledgeArticles.setAdapter(adapter);
    }

    private void requestDataFromWeb(int page, int cid) {
        httpManage.getKnowledgeArticle(new BaseObserver<KnowledgeArticleBean>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                activity.hideProgressBar();
                srlRefresh.finishRefresh(false);
            }

            @Override
            protected void onHandleSuccess(KnowledgeArticleBean knowledgeArticleBean) {
                articles = knowledgeArticleBean.getDatas();
                isOver = knowledgeArticleBean.isOver();
                if (currentPage == START_PAGE) {
                    adapter.setNewData(articles);
                } else {
                    adapter.addData(articles);
                }
                currentPage += 1;
                adapter.loadMoreComplete();
                activity.hideProgressBar();
                srlRefresh.finishRefresh(2500);
            }
        }, page, cid);
    }

}
