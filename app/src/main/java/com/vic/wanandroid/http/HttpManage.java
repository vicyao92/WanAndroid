package com.vic.wanandroid.http;

import android.content.Context;

import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.module.project.bean.ProjectArticles;
import com.vic.wanandroid.module.project.bean.ProjectChapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManage {
    private static String baseUrl=ApiManage.BASE_URL;
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private ApiManage request;
    private Context mContext;
    private static class SingleHolder{
        private static  HttpManage httpManager=null;
        public static HttpManage getInstance(Context context){
            if(httpManager==null){
                httpManager=new HttpManage(context);
            }
            return httpManager;
        }
    }

    private HttpManage(Context context){
        this.mContext=context;
        retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory( GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        request = retrofit.create(ApiManage.class);
    }

    public static HttpManage init(Context context){
        return SingleHolder.getInstance(context);
    }
    /**
     * 创建okhttp的构建
     * @return
     */
    private OkHttpClient.Builder initOkhttpClien(){
        //日志显示级别
        OkHttpClient.Builder httpclient=new OkHttpClient().newBuilder();
        httpclient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return httpclient;
    }

    /**
     * 获取项目类别
     * @param observer
     */
    public void getProjectChapter(Observer<BaseResultBean<List<ProjectChapter>>> observer){
        request.getProjectChapters()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取当前项目当前页面文章数据
     * @param observer
     * @param page 当前页码
     * @param cid 项目类别
     */
    public void getProjectArticles(Observer<BaseResultBean<ProjectArticles>> observer, int page, int cid){
        request.getProjectArticles(page, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

