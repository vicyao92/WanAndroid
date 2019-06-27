package com.vic.wanandroid.http;

import android.content.Context;
import android.util.Log;

import com.vic.wanandroid.utils.LoginUtils;
import com.vic.wanandroid.utils.SpUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class HttpManage {
    private static String baseUrl = ApiManage.BASE_URL;
    private final int DEFAULT_TIME_OUT = 10;
    private final String COOKIE_NAME = "user_cookies";
    private Retrofit retrofit;
    private ApiManage request;
    private Context mContext;
    private OkHttpClient client;

    private HttpManage(Context context) {
        this.mContext = context;
        client = initOkhttpClien().build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        request = retrofit.create(ApiManage.class);
    }

    /**
     * 创建okhttp的构建
     *
     * @return
     */
    private OkHttpClient.Builder initOkhttpClien() {
        OkHttpClient.Builder httpclient = new OkHttpClient().newBuilder();
        httpclient.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .cache(new Cache(new File(mContext.getExternalCacheDir(), "okhttpcache"), 20 * 1024 * 1024))
                .addInterceptor(new ReadCookieIntercepter())
                .addInterceptor(new SaveCookieIntercepter());
        return httpclient;
    }

    public static HttpManage init(Context context) {
        return SingleHolder.getInstance(context);
    }

    /**
     * 获取项目类别
     *
     * @param observer
     */
    public void getProjectChapter(BaseObserver observer) {
        request.getProjectChapters()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取当前项目当前页面文章数据
     *
     * @param observer
     * @param page     当前页码
     * @param cid      项目类别
     */
    public void getProjectArticles(BaseObserver observer, int page, int cid) {
        request.getProjectArticles(page, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取公众号列表
     *
     * @param observer
     */
    public void getAccountBean(BaseObserver observer) {
        request.getAccountBean()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取指定ID公众号的文章
     *
     * @param observer
     * @param id       公众号ID
     * @param page     当前页数
     */
    public void getChatArticles(BaseObserver observer, int id, int page) {
        request.getChatArticles(id, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取导航列表
     *
     * @param observer
     */
    public void getNavigationList(BaseObserver observer) {
        request.getNavigationList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取知识体系列表
     *
     * @param observer
     */
    public void getKnowledgeSystem(BaseObserver observer) {
        request.getKnowledgeSystem()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取知识体系相应文章
     *
     * @param observer
     * @param page
     * @param cid
     */
    public void getKnowledgeArticle(BaseObserver observer, int page, int cid) {
        request.getKnowledgeArticle(page, cid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void login(BaseObserver observer, String username, String password) {
        request.login(username, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private static class SingleHolder {
        private static HttpManage httpManager = null;

        public static HttpManage getInstance(Context context) {
            if (httpManager == null) {
                httpManager = new HttpManage(context);
            }
            return httpManager;
        }
    }

    public class ReadCookieIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            Observable.just(SpUtils.getInstance().getString(COOKIE_NAME, ""))
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            //添加cookie
                            builder.addHeader("Cookie", cookie);
                            Log.d("response", "addHeader:" + cookie);
                        }
                    });
            return chain.proceed(builder.build());
        }
    }

    private class SaveCookieIntercepter implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            StringBuffer cookieBuffer = new StringBuffer();
            Observable.from(response.headers("Set-Cookie"))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            cookieBuffer.append(cookie).append(";");
                        }
                    });
            if (cookieBuffer.indexOf("loginUserName")>=0&&SpUtils.getInstance().getString(COOKIE_NAME).equals("")) {
                LoginUtils.getInstance().login(cookieBuffer.toString());
            }
            return response;
        }
    }
}

