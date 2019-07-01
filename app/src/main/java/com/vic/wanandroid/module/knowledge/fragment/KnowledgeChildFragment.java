package com.vic.wanandroid.module.knowledge.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
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
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.module.home.bean.HomeBean;
import com.vic.wanandroid.module.knowledge.activity.KnowledgeArticlesActivity;
import com.vic.wanandroid.module.knowledge.adapter.KnowledgeAdapter;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeSystemBean;
import com.vic.wanandroid.module.project.bean.ProjectArticles;
import com.vic.wanandroid.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeChildFragment extends BaseFragment {


    @BindView(R.id.rv_knowledge_child)
    RecyclerView rvKnowledgeChild;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<KnowledgeSystemBean> knowledgeSystemBeanList = new ArrayList<>();
    private KnowledgeAdapter adapter;
    public KnowledgeChildFragment() {

    }

    @Override
    public int getResId() {
        return R.layout.fragment_knowledge_child;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpManage = HttpManage.init(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        requestDataFromWeb();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRv();
    }

    private void requestDataFromWeb() {
        httpManage.getKnowledgeSystem(new BaseObserver<List<KnowledgeSystemBean>>(getContext()) {
            @Override
            protected void onHandleSuccess(List<KnowledgeSystemBean> knowledgeSystemBeans) {
                knowledgeSystemBeanList.addAll(knowledgeSystemBeans);
                adapter.setNewData(knowledgeSystemBeans);
            }
        });
    }

    private void initRv() {
        adapter = new KnowledgeAdapter(R.layout.item_rv_knowledge_child,knowledgeSystemBeanList,getContext());
        adapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener(){
            @Override
            public void onClick(KnowledgeSystemBean bean, int pos) {
                KnowledgeArticlesActivity.start(getActivity(),pos,bean);
            }

        });
        adapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener(){
            @Override
            public void onClick(KnowledgeSystemBean bean, int pos) {
                KnowledgeArticlesActivity.start(getActivity(),pos,bean);
            }
        });
        rvKnowledgeChild.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKnowledgeChild.setAdapter(adapter);

    }

}
