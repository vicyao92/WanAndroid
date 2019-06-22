package com.vic.wanandroid.module.knowledge.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.home.adapter.HomeAdapter;
import com.vic.wanandroid.module.knowledge.activity.KnowledgeArticlesActivity;
import com.vic.wanandroid.module.knowledge.adapter.KnowledgeAdapter;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeSystemBean;
import com.vic.wanandroid.module.project.bean.ProjectArticles;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeChildFragment extends BaseFragment {


    @BindView(R.id.rv_knowledge_child)
    RecyclerView rvKnowledgeChild;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private List<KnowledgeSystemBean> knowledgeSystemBeans = new ArrayList<>();
    private KnowledgeAdapter adapter;
    public KnowledgeChildFragment() {

    }

    @Override
    public int getResId() {
        return R.layout.fragment_knowledge_child;
    }

    @Override
    public void onStart() {
        super.onStart();
        httpManage = HttpManage.init(getContext());
        requestDataFromWeb();
        initRv();
    }

    private void requestDataFromWeb() {
        httpManage.getKnowledgeSystem(new BaseObserver<List<KnowledgeSystemBean>>(getContext()) {
            @Override
            protected void onHandleSuccess(List<KnowledgeSystemBean> knowledgeSystemBeans) {
                knowledgeSystemBeans = knowledgeSystemBeans;
                adapter.setNewData(knowledgeSystemBeans);
                adapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener(){
                    @Override
                    public void onClick(KnowledgeSystemBean bean, int pos) {
                        KnowledgeArticlesActivity.start(getActivity(),pos,bean);
                    }

                });
            }
        });
    }

    private void initRv() {
        adapter = new KnowledgeAdapter(R.layout.item_rv_knowledge_child,knowledgeSystemBeans,getContext());
        rvKnowledgeChild.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKnowledgeChild.setAdapter(adapter);
    }

}
