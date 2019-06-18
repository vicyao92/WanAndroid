package com.vic.wanandroid.module.project.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_project_articles, container, false);
        }
        ButterKnife.bind(this, rootView);
        refresh.setRefreshHeader(new WaveSwipeHeader(getContext()));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        httpManage = HttpManage.init(getContext());
        requestArticleData(currentPage);
        initRv();
    }

    private void requestArticleData(int page) {
        httpManage.getProjectArticles(new Observer<BaseResultBean<ProjectArticles>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseResultBean<ProjectArticles> projectArticlesBaseResultBean) {
                projectArticles = projectArticlesBaseResultBean.getData();
                articleDatas.addAll(projectArticles.getDatas());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, currentPage, cid);
    }

    private void initRv() {
        mAdapter = new HomeAdapter(R.layout.item_rv_articles, articleDatas, getContext());
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(() -> {
            currentPage += 1;
            requestArticleData(currentPage);
            mAdapter.loadMoreComplete();
        }, rvProjectArticles);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String targetUrl = articleDatas.get(position).getLink().trim();
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("TargetAdress", targetUrl);
                startActivity(intent);
            }
        });
        rvProjectArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProjectArticles.setAdapter(mAdapter);
    }
}
