package com.vic.wanandroid.module.home.fragment;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.vic.wanandroid.module.home.bean.BannerBean;
import com.vic.wanandroid.module.home.bean.HomeBean;
import com.vic.wanandroid.utils.DatabaseHelper;
import com.vic.wanandroid.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.article_list)
    RecyclerView rvArticles;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private List<ArticleBean> articleLists = new ArrayList<>();
    private List<BannerBean> bannerBeanList = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();
    private List<String> bannerImages = new ArrayList<>();
    private HomeAdapter mAdapter;
    private Banner banner;
    private HttpManage httpManage;
    private DatabaseHelper databaseHelper;
    private final int PAGE_START = 0;
    private int currentPage = PAGE_START;
    private boolean isOver;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        httpManage = HttpManage.init(getContext());
        databaseHelper = DatabaseHelper.init(getContext());
        initToolbar();
        initBannerSetting();
        initRv();

    }

    @Override
    public void onStart() {
        super.onStart();
        loadDatas(currentPage);
        loadBanner();
    }

    @Override
    public void onPause() {
        banner.isAutoPlay(false);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer);
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_search:
                break;
        }
        return true;
    }

    @Override
    public int getResId() {
        return R.layout.fragment_home;
    }

    private void initToolbar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
    }

    private void loadBanner() {
        httpManage.getBanner(new BaseObserver<List<BannerBean>>(getContext()) {
            @Override
            protected void onHandleSuccess(List<BannerBean> bannerBeans) {
                bannerBeanList = bannerBeans;
                initBannerDatas(bannerBeanList);
                //设置标题集合（当banner样式有显示title时）
                banner.setBannerTitles(bannerTitles);
                //设置图片集合
                banner.setImages(bannerImages);
                banner.start();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initBannerDatas(List<BannerBean> bannerBeans) {
        for (int i = 0; i < bannerBeans.size(); i++) {
            bannerTitles.add(bannerBeans.get(i).getTitle());
            bannerImages.add(bannerBeans.get(i).getImagePath());
        }
    }

    private void initBannerSetting() {
        banner = new Banner(getContext());
        banner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
    }

    private void initRv() {
        mAdapter = new HomeAdapter(R.layout.item_rv_articles, articleLists, getContext());
        mAdapter.addHeaderView(banner);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isOver) {
                    mAdapter.loadMoreEnd();
                } else {
                    loadDatas(currentPage);
                }
            }
        }, rvArticles);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String targetUrl = articleLists.get(position).getLink().trim();
                WebActivity.start(getContext(), articleLists.get(position).getTitle(), targetUrl);
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rvArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvArticles.setAdapter(mAdapter);
    }

    private void loadDatas(int page) {
        httpManage.getHomeArticles(new BaseObserver<HomeBean>(getContext()) {
            @Override
            protected void onHandleSuccess(HomeBean homeBean) {
                articleLists.addAll(homeBean.getDatas());
                isOver = homeBean.isOver();
                if (currentPage == PAGE_START){
                    mAdapter.setNewData(homeBean.getDatas());
                }else {
                    mAdapter.addData(homeBean.getDatas());
                }
                currentPage += 1;
                mAdapter.loadMoreComplete();
            }
        }, page);
    }
}
