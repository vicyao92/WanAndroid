package com.vic.wanandroid.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vic.wanandroid.R;
import com.vic.wanandroid.http.HttpManage;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment {
    /*protected Disposable disposable;*/
    protected View rootView;
    protected HttpManage httpManage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(getResId(),container, false);
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
/*        if (disposable!=null){
            disposable.dispose();
        }*/
        super.onDestroy();
    }

    public abstract int getResId();
}
