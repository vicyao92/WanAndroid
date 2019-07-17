package com.vic.wanandroid.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vic.wanandroid.http.HttpManage;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    public BaseActivity activity;
    protected HttpManage httpManage;
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getResId(),container, false);
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public abstract int getResId();

    /*    public abstract BaseActivity getParentActivity();*/

    public void getParentActivity(BaseActivity activity) {
        this.activity = activity;
    }
    public void logging(String msg){
        Log.d("debugData",msg);
    }


}
