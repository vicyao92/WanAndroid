package com.vic.wanandroid.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> pagerList ;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> pagerList) {
        super(fm);
        this.pagerList = pagerList;
    }

    @Override
    public Fragment getItem(int position) {
        return pagerList.get(position);
    }

    @Override
    public int getCount() {
        return pagerList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }
}
