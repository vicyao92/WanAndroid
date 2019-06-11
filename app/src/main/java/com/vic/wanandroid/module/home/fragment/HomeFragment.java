package com.vic.wanandroid.module.home.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.http.ApiManage;
import com.vic.wanandroid.module.home.adapter.HomeAdapter;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.module.home.bean.BannerBean;
import com.vic.wanandroid.module.home.bean.HomeBean;
import com.vic.wanandroid.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends BaseFragment {
    View rootView;
    @BindView(R.id.article_list)
    RecyclerView rvArticles;
    private Retrofit retrofit;
    private List<ArticleBean> articleLists = new ArrayList<>();
    private List<BannerBean> bannerBeanList = new ArrayList<>();
    private List<String> bannerTitles = new ArrayList<>();
    private List<String> bannerImages = new ArrayList<>();
    private int page = 0;
    private HomeAdapter mAdapter;
    private Banner banner;
    private Disposable disposable;
    private ApiManage apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initRv();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBannerSetting();
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiManage.BASE_URL)
                .build();

        apiService = retrofit.create(ApiManage.class);

        requestArticleData(page);

        apiService.getBannerList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResultBean<List<BannerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResultBean<List<BannerBean>> listBaseResultBean) {
                        bannerBeanList = listBaseResultBean.getData();
                        initBannerDatas(bannerBeanList);
                        //设置标题集合（当banner样式有显示title时）
                        banner.setBannerTitles(bannerTitles);
                        //设置图片集合
                        banner.setImages(bannerImages);
                        banner.start();
                        mAdapter.notifyDataSetChanged();
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        initRv();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    private void requestArticleData(int page) {
        apiService.getArticleList(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResultBean<HomeBean>>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResultBean<HomeBean> baseResultBean) {
                        articleLists.addAll(baseResultBean.getData().getDatas());
                        mAdapter.notifyDataSetChanged();
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initBannerDatas(List<BannerBean> bannerBeans) {
        for (int i = 0; i < bannerBeans.size(); i++) {
            /*bannerTitles.add(i,bannerBeans.get(i).getTitle());
            bannerImages.add(i,bannerBeans.get(i).getImagePath());*/
            bannerTitles.add(bannerBeans.get(i).getTitle());
            bannerImages.add(bannerBeans.get(i).getImagePath());
        }
    }

    private void initRv() {
        mAdapter = new HomeAdapter(R.layout.item_article_lists, articleLists, getContext());
        mAdapter.addHeaderView(banner);
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(() -> {
            page += 1;
            requestArticleData(page);
            mAdapter.loadMoreComplete();
        }, rvArticles);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rvArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvArticles.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            //rootView = getRootView(inflater,container,R.layout.fragment_home);
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
