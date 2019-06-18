package com.vic.wanandroid.module.project.fragment;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.adapter.MyFragmentPagerAdapter;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.project.bean.ProjectArticles;
import com.vic.wanandroid.module.project.bean.ProjectChapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;

public class ProjectFragment extends BaseFragment {
    @BindView(R.id.tab_project)
    TabLayout tabProject;
    @BindView(R.id.vp_project)
    ViewPager vpProject;
    private List<Fragment> fragments = new ArrayList<>();
    private List<ProjectChapter> projectChapters = new ArrayList<>();
    public ProjectFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_project, container, false);
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        httpManage = HttpManage.init(getContext());
        httpManage.getProjectChapter(new Observer<BaseResultBean<List<ProjectChapter>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(BaseResultBean<List<ProjectChapter>> listBaseResultBean) {
                projectChapters = listBaseResultBean.getData();
                initViewPager(projectChapters);
                initTab(projectChapters);
                disposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                disposable.dispose();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initTab(List<ProjectChapter> datas) {
        tabProject.setupWithViewPager(vpProject);
        for (int i =0;i<projectChapters.size();i++){
            tabProject.addTab(tabProject.newTab());
            tabProject.getTabAt(i).setText(projectChapters.get(i).getName());
        }
        //tabProject.addOnTabSelectedListener(tabSelectedListener);
    }

    private void initViewPager(List<ProjectChapter> datas) {
        int cid;
        for (int i =0;i<projectChapters.size();i++){
            cid = projectChapters.get(i).getId();
            fragments.add(new ProjectArticlesFragment(cid));
        }
        vpProject.setAdapter(new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(),fragments));
        vpProject.setCurrentItem(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //tabProject.removeOnTabSelectedListener(tabSelectedListener);
    }

}
