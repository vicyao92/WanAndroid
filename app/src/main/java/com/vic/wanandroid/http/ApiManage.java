package com.vic.wanandroid.http;

import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.module.chat.bean.AccountBean;
import com.vic.wanandroid.module.chat.bean.ChatArticlesBean;
import com.vic.wanandroid.module.home.bean.BannerBean;
import com.vic.wanandroid.module.home.bean.HomeBean;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeArticleBean;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeSystemBean;
import com.vic.wanandroid.module.navigate.bean.NavigationBean;
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

    /**
     * 获取公众号列表
     * @return
     */
    @GET("wxarticle/chapters/json")
    Observable<BaseResultBean<List<AccountBean>>> getAccountBean();

    /**
     * 获取对应id公众号文章
     * @param id
     * @param page
     * @return
     */
    @GET("wxarticle/list/{id}/{page}/json")
    Observable<BaseResultBean<ChatArticlesBean>> getChatArticles(@Path("id") int id,@Path("page") int page);

    @GET("navi/json")
    Observable<BaseResultBean<List<NavigationBean>>> getNavigationList();

    @GET("tree/json")
    Observable<BaseResultBean<List<KnowledgeSystemBean>>> getKnowledgeSystem();

    @GET("article/list/{page}/json")
    Observable<BaseResultBean<KnowledgeArticleBean>> getKnowledgeArticle(@Path("page") int page,@Query("cid") int cid);
}
