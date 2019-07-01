package com.vic.wanandroid.module.knowledge.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.module.knowledge.adapter.KnowledgeArticleAdapter;
import com.vic.wanandroid.module.knowledge.bean.ChildrenBean;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeArticleBean;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeSystemBean;
import com.vic.wanandroid.module.project.bean.ProjectArticles;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeArticleFragment extends BaseFragment {

    @BindView(R.id.rv_knowledge_articles)
    RecyclerView rvKnowledgeArticles;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<ArticleBean> articles = new ArrayList<>();
    private KnowledgeArticleAdapter adapter;
    private static final int START_PAGE = 0;
    private int currentPage = START_PAGE;
    private int cid;
    private boolean isOver;
    public KnowledgeArticleFragment(int cid) {
        this.cid = cid;
    }

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
    }

    @Override
    public void onStart() {
        super.onStart();
        requestDataFromWeb(currentPage,cid);
    }

    private void initRv() {
        adapter = new KnowledgeArticleAdapter(R.layout.item_rv_articles,articles);
        adapter.setOnItemClickListener((adapter, view, position) -> WebActivity.start(getContext()
                ,articles.get(position).getTitle(),articles.get(position).getLink()));
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> {
            if (isOver){
                adapter.loadMoreEnd();
            }else {
                requestDataFromWeb(currentPage,cid);
            }
        }, rvKnowledgeArticles);
        rvKnowledgeArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKnowledgeArticles.setAdapter(adapter);
    }

    private void requestDataFromWeb(int page,int cid){
        httpManage.getKnowledgeArticle(new BaseObserver<KnowledgeArticleBean>(getContext()) {
            @Override
            protected void onHandleSuccess(KnowledgeArticleBean knowledgeArticleBean) {
                articles = knowledgeArticleBean.getDatas();
                isOver = knowledgeArticleBean.isOver();
                if (currentPage == START_PAGE){
                    adapter.setNewData(articles);
                }else {
                    adapter.addData(articles);
                }
                currentPage += 1;
                adapter.loadMoreComplete();
            }
        },page,cid);
    }

}
