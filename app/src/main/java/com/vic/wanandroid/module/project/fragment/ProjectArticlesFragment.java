package com.vic.wanandroid.module.project.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.home.adapter.HomeAdapter;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.module.home.bean.HomeBean;
import com.vic.wanandroid.module.project.bean.ProjectArticles;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectArticlesFragment extends BaseFragment {
    @BindView(R.id.rv_project_articles)
    RecyclerView rvProjectArticles;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<ArticleBean> articleDatas = new ArrayList<>();
    private ProjectArticles projectArticles;
    private int cid;
    private int currentPage = 1;
    private HomeAdapter mAdapter;
    public ProjectArticlesFragment(int cid) {
        this.cid = cid;
    }

    @Override
    public int getResId() {
        return R.layout.fragment_project_articles;
    }

    @Override
    public void onStart() {
        super.onStart();
        httpManage = HttpManage.init(getContext());
        requestArticleData(currentPage);
        initRv();
    }

    private void requestArticleData(int page) {
        httpManage.getProjectArticles(new BaseObserver<ProjectArticles>(getContext()) {
            @Override
            protected void onHandleSuccess(ProjectArticles projectArticlesBean) {
                projectArticles = projectArticlesBean;
                loadMore(projectArticles);
                articleDatas.addAll(projectArticles.getDatas());
            }
        }, currentPage, cid);
    }

    private void initRv() {
        mAdapter = new HomeAdapter(R.layout.item_rv_articles, articleDatas, getContext());
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String targetUrl = articleDatas.get(position).getLink().trim();
                WebActivity.start(getContext(),articleDatas.get(position).getTitle(),targetUrl);
            }
        });
        rvProjectArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProjectArticles.setAdapter(mAdapter);
    }

    private void loadMore(ProjectArticles projectArticles){
        if (currentPage == 1){
            mAdapter.setNewData(projectArticles.getDatas());
        }
        if (projectArticles.isOver()){
            mAdapter.loadMoreEnd();
        }else {
            mAdapter.addData(projectArticles.getDatas());
            mAdapter.setEnableLoadMore(false);
            mAdapter.setOnLoadMoreListener(() -> {
                currentPage += 1;
                requestArticleData(currentPage);
                mAdapter.loadMoreComplete();
            }, rvProjectArticles);
            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        }
    }
}
