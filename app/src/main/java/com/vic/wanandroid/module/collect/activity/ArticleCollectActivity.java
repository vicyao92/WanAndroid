package com.vic.wanandroid.module.collect.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseActivity;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.collect.adapter.ArticleCollectionAdapter;
import com.vic.wanandroid.module.collect.bean.ArticlesCollection;
import com.vic.wanandroid.module.collect.bean.CollectBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleCollectActivity extends BaseActivity {

    private final int PAGE_START = 0;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.article_collection)
    RecyclerView articleCollection;
    @BindView(R.id.rootView)
    CoordinatorLayout rootView;
    private ArticleCollectionAdapter adapter;
    private HttpManage httpManage;
    private List<CollectBean> collections = new ArrayList<>();
    private boolean isOver = true;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_collect);
        ButterKnife.bind(this);
        httpManage = HttpManage.init(ArticleCollectActivity.this);
        initToolbar();
        initRv();
        createProgressBar(this);
        showProgressBar();
        loadDatas(currentPage);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
            actionBar.setTitle("文章收藏");
        }
    }

    private void initRv() {
        adapter = new ArticleCollectionAdapter(R.layout.item_collect_article, collections);

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
        }, articleCollection);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CollectBean article = collections.get(position);
                int id = article.getId();
                String title = article.getTitle();
                String targetUrl = article.getLink().trim();
                WebActivity.start(ArticleCollectActivity.this, id, title, targetUrl, 0);
            }
        });
        adapter.setOnCollectButtonClickClickListener(new ArticleCollectionAdapter.OnCollectButtonClickClickListener() {
            @Override
            public void onClick(CollectBean bean) {
                int id = bean.getId();
                int originId = bean.getOriginId();
                uncollect(id, originId, bean);
            }
        });
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        articleCollection.setLayoutManager(new LinearLayoutManager(ArticleCollectActivity.this));
        articleCollection.setAdapter(adapter);
        //ToDO EmptyView位置不居中
        adapter.setEmptyView(R.layout.empty_data_view, (ViewGroup) articleCollection.getParent());
    }

    private void loadDatas(int page) {
        httpManage.getArticleCollections(new BaseObserver<ArticlesCollection>(ArticleCollectActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgressBar();
            }

            @Override
            protected void onHandleSuccess(ArticlesCollection articlesCollection) {
                collections.addAll(articlesCollection.getDatas());
                isOver = articlesCollection.isOver();
                if (page == PAGE_START) {
                    adapter.setNewData(collections);
                } else {
                    adapter.addData(collections);
                }
                currentPage += 1;
                adapter.loadMoreComplete();
                hideProgressBar();
            }
        }, currentPage);
    }

    /**
     * 取消收藏
     *
     * @param id
     */
    private void uncollect(int id, int originId, CollectBean bean) {
        httpManage.uncollect(new BaseObserver<CollectBean>(ArticleCollectActivity.this) {
            @Override
            protected void onHandleSuccess(CollectBean collectBean) {
                collections.remove(bean);
                adapter.notifyDataSetChanged();
            }
        }, id, originId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
