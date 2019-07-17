package com.vic.wanandroid.module.home.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.home.activity.SearchActivity;
import com.vic.wanandroid.module.home.adapter.SearchHistroyAdapter;
import com.vic.wanandroid.module.home.bean.HotkeyBean;
import com.vic.wanandroid.module.home.bean.SearchHistoryBean;
import com.vic.wanandroid.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchHistoryFragment extends BaseFragment {


    @BindView(R.id.fbl_hot_key)
    FlexboxLayout fblHotKey;
    @BindView(R.id.rv_search_history)
    RecyclerView rvSearchHistory;
    @BindView(R.id.tv_clear_all)
    TextView tvClearAll;
    private Context mContext;
    private SearchHistroyAdapter adapter;
    private List<String> datas = new ArrayList<>();
    private List<SearchHistoryBean> historyBeanList = new ArrayList<>();
    private DatabaseHelper helper;

    public SearchHistoryFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        httpManage = HttpManage.init(mContext);
        helper = DatabaseHelper.init(getActivity());
        datas.addAll(readHistory());
    }

    @Override
    public int getResId() {
        return R.layout.fragment_search_history;
    }

    public List<String> readHistory() {
        RealmResults<SearchHistoryBean> results = helper.findAll(SearchHistoryBean.class);
        List<String> histories = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            histories.add(results.get(i).getName());
        }
        return histories;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new SearchHistroyAdapter(R.layout.item_rv_search_histroy, datas);
        adapter.setOnDeleteClickListener(new SearchHistroyAdapter.OnDeleteClickListener() {
            @Override
            public void onClick(String item, int pos) {
                datas.remove(item);
                deleteHistory(pos);
                adapter.notifyDataSetChanged();
            }
        });
        rvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchHistory.setAdapter(adapter);
        requestData();
    }

    public void deleteHistory(int pos) {
        helper.deleteResult(SearchHistoryBean.class, pos);
    }

    private void requestData() {
        httpManage.getHotkey(new BaseObserver<List<HotkeyBean>>(mContext) {
            @Override
            protected void onHandleSuccess(List<HotkeyBean> hotkeyBeans) {
                initFblHotkey(hotkeyBeans);
            }
        });

    }

    private void initFblHotkey(List<HotkeyBean> hotkeyBeans) {
        for (int i = 0; i < hotkeyBeans.size(); i++) {
            HotkeyBean hotkey = hotkeyBeans.get(i);
            String name = hotkey.getName();
            TextView textView = (TextView) LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.item_website_collect, fblHotKey, false);
            textView.setText(name);
            textView.setTextColor(Color.WHITE);
            GradientDrawable myGrad = (GradientDrawable) textView.getBackground();
            myGrad.setColor(randomColor());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SearchActivity) getActivity()).updateSearchText(name);
                    saveHistory(name);
                    datas.add(name);
                    adapter.notifyDataSetChanged();
                    ((SearchActivity) getActivity()).search(name);
                }
            });
            fblHotKey.addView(textView);
        }
    }

    private int randomColor() {
        int[] randomColors = getResources().getIntArray(R.array.tvBackgroundColor);
        int color = randomColors[new Random().nextInt(randomColors.length)];
        return color;
    }

    public void saveHistory(String item) {
        SearchHistoryBean historyBean = new SearchHistoryBean();
        long currentTime = new Date().getTime();
        historyBean.setName(item);
        historyBean.setCurrentTime(currentTime);
        helper.add(historyBean);
        historyBeanList.add(historyBean);
    }

    public void refreshHistroy(String query) {
        datas.add(query);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_clear_all)
    public void onViewClicked() {
        helper.deleteAllResult(SearchHistoryBean.class);
        datas.clear();
        historyBeanList.clear();
        adapter.notifyDataSetChanged();
    }
}
