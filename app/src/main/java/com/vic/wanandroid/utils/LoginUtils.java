package com.vic.wanandroid.utils;

import android.content.Context;

import com.vic.wanandroid.module.account.LoginActivity;
import com.vic.wanandroid.module.account.bean.LoginBean;

public class LoginUtils {
    private static final String LOGIN_URL = "https://www.wanandroid.com/user/login";
    private static LoginUtils instance;
    private final String COOKIE_NAME = "user_cookies";
    private LoginBean loginBean;
    private String loginCache = null;
    private LoginUtils() {
    }

    public static LoginUtils getInstance() {
        if (instance != null){
            return instance;
        }else {
            instance = new LoginUtils();
        }
        return instance;
    }

    public String getLoginCookie() {
        if (loginCache == null) {
            loginCache = SpUtils.getInstance().getString(COOKIE_NAME);
        }
        return loginCache;
    }

    public void login(String cache) {
        SpUtils.getInstance().putString(COOKIE_NAME,cache);
    }

/*    public void login(LoginBean loginBean) {
        SpUtils.getInstance().putString(COOKIE_NAME,loginBean);
    }*/

    public void logout() {
        loginCache = null;
        SpUtils.getInstance().clear();
    }

    public void update(String cache) {
        SpUtils.getInstance().putString(COOKIE_NAME,cache);
    }

    public boolean isLogin() {
        String cookie = getLoginCookie();
        if (cookie == null) {
            return false;
        }
        return true;
    }

    public boolean isAlreradyLogin(Context context) {
        if (isLogin()) {
            return true;
        } else {
            LoginActivity.start(context);
            return false;
        }
    }

    public String getLoginUrl(){
        return LOGIN_URL;
    }

}
