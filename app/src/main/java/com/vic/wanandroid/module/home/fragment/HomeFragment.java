package com.vic.wanandroid.module.home.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.http.ApiManage;
import com.vic.wanandroid.module.home.adapter.ArticleRvAdapter;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.module.home.bean.HomeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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
    private ArticleRvAdapter adapter;
    private Retrofit retrofit;
    private List<ArticleBean> articleLists = new ArrayList<>();
    private int page = 0;
    private Unbinder unbinder;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiManage.BASE_URL)
                .build();

        ApiManage apiService = retrofit.create(ApiManage.class);

        apiService.getArticleList(page)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Observer<BaseResultBean<HomeBean>>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onNext(BaseResultBean<HomeBean> baseResultBean) {
                        articleLists = baseResultBean.getData().getDatas();
                        adapter = new ArticleRvAdapter(articleLists, getContext());
                        rvArticles.setLayoutManager(new LinearLayoutManager(getContext()));
                        rvArticles.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        disposable.dispose();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            //rootView = getRootView(inflater,container,R.layout.fragment_home);
        }
        unbinder = ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
