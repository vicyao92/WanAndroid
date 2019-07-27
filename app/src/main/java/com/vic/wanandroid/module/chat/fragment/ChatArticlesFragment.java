package com.vic.wanandroid.module.chat.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vic.wanandroid.MainActivity;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.chat.bean.ChatArticlesBean;
import com.vic.wanandroid.module.home.adapter.HomeAdapter;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.utils.SwipeRefreshUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatArticlesFragment extends BaseFragment {

    @BindView(R.id.rv_chat_articles)
    RecyclerView rvChatArticles;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    private int id;
    private final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private List<ArticleBean> articleDatas = new ArrayList<>();
    private HomeAdapter mAdapter;
    private boolean isOver;

    public ChatArticlesFragment(int id) {
        this.id = id;
    }

    private MainActivity activity;

    @Override
    public int getResId() {
        return R.layout.fragment_chat_articles;
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
                currentPage = PAGE_START;
                articleDatas.clear();
                isOver = false;
                requestArticleData(currentPage);
            }
        });
        activity = (MainActivity) getActivity();
        activity.createProgressBar(getActivity());
        activity.showProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestArticleData(currentPage);
    }

    private void initRv() {
        mAdapter = new HomeAdapter(R.layout.item_rv_articles, articleDatas, getContext());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            int id = articleDatas.get(position).getId();
            String targetUrl = articleDatas.get(position).getLink().trim();
            WebActivity.start(getContext(), id, articleDatas.get(position).getTitle(), targetUrl, 0);
        });
        mAdapter.setOnLoadMoreListener(() -> {
            if (isOver) {
                mAdapter.loadMoreEnd();
            } else {
                requestArticleData(currentPage);
            }
        }, rvChatArticles);
        rvChatArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChatArticles.setAdapter(mAdapter);
    }

    private void requestArticleData(int page) {
        httpManage.getChatArticles(new BaseObserver<ChatArticlesBean>(getContext()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                activity.hideProgressBar();
                srlRefresh.finishRefresh(false);
            }

            @Override
            protected void onHandleSuccess(ChatArticlesBean chatArticlesBean) {
                isOver = chatArticlesBean.isOver();
                articleDatas.addAll(chatArticlesBean.getDatas());
                if (currentPage == PAGE_START) {
                    mAdapter.setNewData(chatArticlesBean.getDatas());
                } else {
                    mAdapter.addData(chatArticlesBean.getDatas());
                }
                currentPage += 1;
                mAdapter.loadMoreComplete();
                activity.hideProgressBar();
                srlRefresh.finishRefresh(2500);
            }
        }, id, currentPage);
    }

}
