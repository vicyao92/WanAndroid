package com.vic.wanandroid.http;

import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.module.home.bean.BannerBean;
import com.vic.wanandroid.module.home.bean.HomeBean;
import com.vic.wanandroid.module.project.bean.ProjectArticles;
import com.vic.wanandroid.module.project.bean.ProjectChapter;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiManage {
    final String BASE_URL = "https://www.wanandroid.com/";

    /**
     * 获取首页文章数据
     * @param page 当前页码
     *
     */
    @GET("article/list/{page}/json")
    Observable<BaseResultBean<HomeBean>> getArticleList(@Path("page") int page);

    /**
     * 获取首页Banner数据
     * @return
     */
    @GET("banner/json")
    Observable<BaseResultBean<List<BannerBean>>> getBannerList();

    /**
     * 获取项目类别数据
     * @return
     */
    @GET("project/tree/json")
    Observable<BaseResultBean<List<ProjectChapter>>> getProjectChapters();

    /**
     * 获取该类项目下的文章数据
     * @param page 当前页码
     * @param cid 类别id
     * @return
     */
    @GET("project/list/{page}/json")
    Observable<BaseResultBean<ProjectArticles>> getProjectArticles(@Path("page") int page , @Query("cid") int cid);
}
