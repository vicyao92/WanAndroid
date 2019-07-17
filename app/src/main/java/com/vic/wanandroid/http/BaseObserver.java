package com.vic.wanandroid.http;

import android.content.Context;
import android.widget.Toast;

import com.vic.wanandroid.base.BaseResultBean;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<BaseResultBean<T>> {
    private Disposable disposable;
    private Context context;
    protected BaseObserver(Context mContext) {
        this.context = mContext;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(BaseResultBean<T> value) {
        //这里对数据bean的封装
        if (value.getErrorCode() == 0) {
            T t = value.getData();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getErrorMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        disposable.dispose();
    }

    @Override
    public void onComplete() {
    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
