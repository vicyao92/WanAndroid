package com.vic.wanandroid.http;

import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.module.home.bean.HomeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiManage {
    public static final String BASE_URL = "https://www.wanandroid.com/";

    @GET("article/list/{page}/json")
    Observable<BaseResultBean<HomeBean>> getArticleList(@Path("page") int page);
}
