package com.vic.wanandroid.module.navigate.fragment;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;
import com.vic.wanandroid.base.BaseResultBean;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.navigate.adapter.NavigationAdapter;
import com.vic.wanandroid.module.navigate.bean.NavigationBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends BaseFragment {


    @BindView(R.id.rv_knowledge_child)
    RecyclerView rvKnowledgeChild;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private List<NavigationBean> navigations = new ArrayList<>();
    private NavigationAdapter adapter;
    public NavigationFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        httpManage = HttpManage.init(getContext());
        requestDataFromWeb();
        initRv();
    }

    private void requestDataFromWeb() {
        httpManage.getNavigationList(new BaseObserver<List<NavigationBean>>(getContext()) {
            @Override
            protected void onHandleSuccess(List<NavigationBean> navigationBeans) {
                navigations = navigationBeans;
                adapter.setNewData(navigations);
                adapter.setOnItemClickListener(new NavigationAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(NavigationBean bean, int pos) {
                        String title = bean.getArticles().get(pos).getTitle();
                        String url = bean.getArticles().get(pos).getLink();
                        WebActivity.start(getActivity(),title,url);
                    }
                });
            }
        });
    }

    private void initRv() {
        adapter = new NavigationAdapter(R.layout.item_rv_knowledge_child, navigations);
        rvKnowledgeChild.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKnowledgeChild.setAdapter(adapter);
    }

    @Override
    public int getResId() {
        return R.layout.fragment_knowledge_child;
    }
}
