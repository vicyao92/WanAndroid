package com.vic.wanandroid.module.project.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.adapter.MyFragmentPagerAdapter;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.project.bean.ProjectChapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProjectFragment extends BaseFragment {
    @BindView(R.id.tab_project)
    TabLayout tabProject;
    @BindView(R.id.vp_project)
    ViewPager vpProject;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ProjectArticlesFragment articlesFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private List<ProjectChapter> projectChapters = new ArrayList<>();

    public ProjectFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        httpManage = HttpManage.init(getContext());
        httpManage.getProjectChapter(new BaseObserver<List<ProjectChapter>>(getContext()) {
            @Override
            protected void onHandleSuccess(List<ProjectChapter> projectChaptersList) {
                projectChapters = projectChaptersList;
                initViewPager(projectChapters);
                initTab(projectChapters);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        initToolbar();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setTitle(R.string.project);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer);
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //tabProject.removeOnTabSelectedListener(tabSelectedListener);
    }

    @Override
    public int getResId() {
        return R.layout.fragment_project;
    }

    private void initTab(List<ProjectChapter> datas) {
        tabProject.setupWithViewPager(vpProject);
        for (int i = 0; i < datas.size(); i++) {
            tabProject.getTabAt(i).setText(datas.get(i).getName());
        }
        Log.d("module_project", tabProject.getTabCount() + "");
        //tabProject.addOnTabSelectedListener(tabSelectedListener);
    }

    private void initViewPager(List<ProjectChapter> datas) {
        int cid;
        if (fragments.size() == 0) {
            for (int i = 0; i < projectChapters.size(); i++) {
                cid = projectChapters.get(i).getId();
                articlesFragment = new ProjectArticlesFragment(cid);
                fragments.add(articlesFragment);
            }
            vpProject.setAdapter(new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments));
            vpProject.setCurrentItem(0);
        }
    }

}
