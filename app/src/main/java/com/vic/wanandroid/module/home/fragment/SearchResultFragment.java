package com.vic.wanandroid.module.home.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.home.adapter.HomeAdapter;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.module.home.bean.HomeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends BaseFragment {

    private static final int PAGE_START = 0;
    @BindView(R.id.rv_result)
    RecyclerView rvResult;
    private List<ArticleBean> articleLists = new ArrayList<>();
    private int currentPage = PAGE_START;
    private boolean isOver = false;
    private HttpManage httpManage;
    private HomeAdapter adapter;
    private String keywords;

    public SearchResultFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpManage = HttpManage.init(getContext());
    }

    @Override
    public int getResId() {
        return R.layout.fragment_search_result;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRv();
    }

    private void initRv() {
        adapter = new HomeAdapter(R.layout.item_rv_articles, articleLists, getContext());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isOver) {
                    adapter.loadMoreEnd();
                } else {
                    loadDatas(currentPage);
                }
            }
        }, rvResult);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleBean article = articleLists.get(position);
                int id = article.getId();
                String title = article.getTitle();
                String targetUrl = article.getLink().trim();
                WebActivity.start(getContext(), id, title, targetUrl, 0);
            }
        });
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rvResult.setLayoutManager(new LinearLayoutManager(getContext()));
        rvResult.setAdapter(adapter);
    }

    private void loadDatas(int page) {
        httpManage.search(new BaseObserver<HomeBean>(getContext()) {
            @Override
            protected void onHandleSuccess(HomeBean homeBean) {
                articleLists.addAll(homeBean.getDatas());
                isOver = homeBean.isOver();
                if (currentPage == PAGE_START) {
                    adapter.setNewData(homeBean.getDatas());
                } else {
                    adapter.addData(homeBean.getDatas());
                }
                currentPage += 1;
                adapter.loadMoreComplete();
            }
        }, page, keywords);
    }

    public void requestData(String key) {
        keywords = key;
        articleLists.clear();
        currentPage = PAGE_START;
        loadDatas(currentPage);
    }
}
