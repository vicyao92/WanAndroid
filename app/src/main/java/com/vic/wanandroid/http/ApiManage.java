package com.vic.wanandroid.http;

import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.module.home.bean.BannerBean;
import com.vic.wanandroid.module.home.bean.HomeBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiManage {
    final String BASE_URL = "https://www.wanandroid.com/";

    /**
     * 获取首页文章数据
     * @param page
     *
     */
    @GET("article/list/{page}/json")
    Observable<BaseResultBean<HomeBean>> getArticleList(@Path("page") int page);

    @GET("banner/json")
    Observable<BaseResultBean<List<BannerBean>>> getBannerList();
}
