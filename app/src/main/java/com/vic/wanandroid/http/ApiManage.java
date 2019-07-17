package com.vic.wanandroid.http;

import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.module.account.bean.LoginBean;
import com.vic.wanandroid.module.chat.bean.AccountBean;
import com.vic.wanandroid.module.chat.bean.ChatArticlesBean;
import com.vic.wanandroid.module.collect.bean.ArticlesCollection;
import com.vic.wanandroid.module.collect.bean.CollectBean;
import com.vic.wanandroid.module.collect.bean.WebsiteCollectBean;
import com.vic.wanandroid.module.home.bean.BannerBean;
import com.vic.wanandroid.module.home.bean.HomeBean;
import com.vic.wanandroid.module.home.bean.HotkeyBean;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeArticleBean;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeSystemBean;
import com.vic.wanandroid.module.navigate.bean.NavigationBean;
import com.vic.wanandroid.module.project.bean.ProjectArticles;
import com.vic.wanandroid.module.project.bean.ProjectChapter;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    /**
     * 获取导航列表
     *
     * @return
     */
    @GET("navi/json")
    Observable<BaseResultBean<List<NavigationBean>>> getNavigationList();

    /**
     * 获取知识体系列表
     *
     * @return
     */
    @GET("tree/json")
    Observable<BaseResultBean<List<KnowledgeSystemBean>>> getKnowledgeSystem();

    /**
     * 获取知识体系对应子item文章
     * @param page 页数
     * @param cid item的id
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<BaseResultBean<KnowledgeArticleBean>> getKnowledgeArticle(@Path("page") int page, @Query("cid") int cid);

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<BaseResultBean<LoginBean>> login(@Field("username") String username,
                                                @Field("password") String password);

    /**
     * 注册
     *
     * @param username   用户名
     * @param password   密码
     * @param repassword 再次输入的密码，用来验证两次输入是否相同
     * @return
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<BaseResultBean<LoginBean>> register(@Field("username") String username
            ,@Field("password") String password,@Field("repassword") String repassword);

    /**
     * 收藏站内文章
     *
     * @param id
     * @return
     */
    @POST("lg/collect/{id}/json")
    Observable<BaseResultBean<CollectBean>> collect(@Path("id") int id);

    /**
     * 收藏站外文章（会重复添加，暂不使用）
     *
     * @param title
     * @param author
     * @param link
     * @return
     */
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    Observable<BaseResultBean<CollectBean>> collect(@Field("title") String title, @Field("author") String author,
                                                    @Field("link") String link);

    /**
     * 取消收藏
     *
     * @param id
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseResultBean> uncollect(@Path("id") int id);

    /**
     * 取消收藏
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    Observable<BaseResultBean> uncollect(@Path("id") int id, @Field("originId") int originID);


    /**
     * 我的收藏文章
     *
     * @param page
     * @return
     */
    @GET("lg/collect/list/{page}/json")
    Observable<BaseResultBean<ArticlesCollection>> getArticleCollections(@Path("page") int page);


    /**
     * 收藏常用网站
     *
     * @param name 网站名
     * @param link 网址
     * @return
     */
    @FormUrlEncoded
    @POST("lg/collect/addtool/json")
    Observable<BaseResultBean<WebsiteCollectBean>> collectWebsite(@Field("name") String name,
                                                                  @Field("link") String link);

    /**
     * 删除收藏网站
     *
     * @param id 收藏网站id
     * @return
     */
    @FormUrlEncoded
    @POST("lg/collect/deletetool/json")
    Observable<BaseResultBean<WebsiteCollectBean>> deleteWebsite(@Field("id") int id);

    /**
     * 获取收藏网站列表
     *
     * @return
     */
    @GET("lg/collect/usertools/json")
    Observable<BaseResultBean<List<WebsiteCollectBean>>> getWebsiteCollect();

    @FormUrlEncoded
    @POST("lg/collect/updatetool/json")
    Observable<BaseResultBean<WebsiteCollectBean>> updateWebsite(@Field("id") int id,
                                                                 @Field("name") String name,
                                                                 @Field("link") String link);

    /**
     * 搜索热词
     *
     * @return
     */
    @GET("hotkey/json")
    Observable<BaseResultBean<List<HotkeyBean>>> getHotkey();


    /**
     * 常用网站
     *
     * @return
     */
    @GET("friend/json")
    Observable<BaseResultBean<List<WebsiteCollectBean>>> getFriendWebsite();

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    Observable<BaseResultBean<HomeBean>> search(@Path("page") int page, @Field("k") String k);
}
