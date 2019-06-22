package com.vic.wanandroid.module.chat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.vic.wanandroid.module.chat.bean.ChatArticlesBean;
import com.vic.wanandroid.module.home.adapter.HomeAdapter;
import com.vic.wanandroid.module.home.bean.ArticleBean;
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
public class ChatArticlesFragment extends BaseFragment {

    @BindView(R.id.rv_chat_articles)
    RecyclerView rvChatArticles;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int id;
    private int currentPage = 1;
    private List<ArticleBean> articleDatas = new ArrayList<>();
    private ChatArticlesBean chatArticles;
    private HomeAdapter mAdapter;

    public ChatArticlesFragment(int id) {
        this.id = id;
    }

    @Override
    public int getResId() {
        return R.layout.fragment_chat_articles;
    }

    @Override
    public void onStart() {
        super.onStart();
        httpManage = HttpManage.init(getContext());
        requestArticleData(currentPage);
        initRv();
    }

    private void requestArticleData(int page) {
        httpManage.getChatArticles(new BaseObserver<ChatArticlesBean>(getContext()) {
            @Override
            protected void onHandleSuccess(ChatArticlesBean chatArticlesBean) {
                chatArticles = chatArticlesBean;
                loadMore(chatArticles);
                articleDatas.addAll(chatArticles.getDatas());
            }
        }, id, currentPage);
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
        rvChatArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChatArticles.setAdapter(mAdapter);
    }

    private void loadMore(ChatArticlesBean chatArticles) {
        if (currentPage == 1) {
            mAdapter.setNewData(chatArticles.getDatas());
        }
        if (chatArticles.isOver()) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.addData(chatArticles.getDatas());
            mAdapter.setEnableLoadMore(false);
            mAdapter.setOnLoadMoreListener(() -> {
                currentPage += 1;
                requestArticleData(currentPage);
                mAdapter.loadMoreComplete();
            }, rvChatArticles);
            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        }
    }

}
