package com.vic.wanandroid.base;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vic.wanandroid.http.HttpManage;

public abstract class BaseActivity extends AppCompatActivity {
    public ProgressBar progressBar = null;
    protected HttpManage httpManage;

    public void showToast(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void createProgressBar(Activity activity) {
        if (progressBar == null) {
            progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyle);
            FrameLayout rootContainer = activity.findViewById(android.R.id.content);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置对其方式为：屏幕居中对其
            lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
            progressBar.setLayoutParams(lp);
            rootContainer.addView(progressBar);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressBar();
    }

    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar = null;
    }
}
