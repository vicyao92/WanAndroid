package com.vic.wanandroid.module.knowledge.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.adapter.MyFragmentPagerAdapter;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeSystemBean;
import com.vic.wanandroid.module.knowledge.fragment.KnowledgeArticleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KnowledgeArticlesActivity extends AppCompatActivity {

    @BindView(R.id.tab_knowledge_ariticle)
    TabLayout tabKnowledgeAriticle;
    @BindView(R.id.vp_knowledge_article)
    ViewPager vpKnowledgeArticle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private KnowledgeSystemBean knowledgeSystemBean;
    private Intent intent;
    private int position = 0;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_articles);
        ButterKnife.bind(this);
        initdata();
        initToolbar();
        initViewPager();
        initTab();
    }

    public static void start(Context context, int position, KnowledgeSystemBean knowledgeSystemBean) {
        Intent intent = new Intent(context, KnowledgeArticlesActivity.class);
        intent.putExtra("result", knowledgeSystemBean);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    private void initdata() {
        position = getIntent().getIntExtra("position", 0);
        knowledgeSystemBean = (KnowledgeSystemBean) getIntent().getSerializableExtra("result");
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
            actionBar.setTitle(knowledgeSystemBean.getName());
        }
    }

    private void initTab() {
        tabKnowledgeAriticle.setupWithViewPager(vpKnowledgeArticle);
        for (int i = 0;i<knowledgeSystemBean.getChildren().size();i++){
            tabKnowledgeAriticle.getTabAt(i).setText(knowledgeSystemBean.getChildren().get(i).getName());
        }
    }

    private void initViewPager() {
        int cid;
        if (fragments.size() == 0) {
            for (int i = 0;i<knowledgeSystemBean.getChildren().size();i++){
                cid = knowledgeSystemBean.getChildren().get(i).getId();
                fragments.add(new KnowledgeArticleFragment(cid));
            }
            vpKnowledgeArticle.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
            vpKnowledgeArticle.setCurrentItem(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
