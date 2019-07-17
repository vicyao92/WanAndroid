package com.vic.wanandroid.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.just.agentweb.AgentWeb;
import com.vic.wanandroid.R;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.collect.bean.CollectBean;
import com.vic.wanandroid.module.collect.bean.WebsiteCollectBean;
import com.vic.wanandroid.utils.LoginUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {

    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_WEBSITE = 1;
    @BindView(R.id.web_container)
    LinearLayout webContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String title = "";
    private String url = "";
    private String author = "";
    private int id = 0;
    private AgentWeb agentWeb;
    private HttpManage httpManage;
    private int type;

    public static void start(Context context, int id, String title, String url, int type) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("TargetUrl", url);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        httpManage = HttpManage.init(WebActivity.this);
        ButterKnife.bind(this);
        initData();
        initToolbar();
        loadData();
    }

    private void initData() {
        id = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        url = getIntent().getStringExtra("TargetUrl");
        type = getIntent().getIntExtra("type", 0);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
            actionBar.setTitle(title);
        }
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_menu_overflow_setting_white));
    }

    @Override
    public void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    private void loadData() {
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) webContainer, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!agentWeb.back()) {
                    finish();
                }
                break;
            case R.id.btn_open_by_self_browser:
                openWithSelfBrowser(url);
                break;
            case R.id.btn_collect:
                if (LoginUtils.getInstance().isAlreradyLogin(WebActivity.this)) {
                    switch (type) {
                        case TYPE_ARTICLE:
                            collect(id);
                            break;
                        case TYPE_WEBSITE:
                            collectWebsite(title, url);
                            break;
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 收藏站内文章
     *
     * @param id
     */
    private void collect(int id) {
        httpManage.collect(new BaseObserver<CollectBean>(WebActivity.this) {
            @Override
            protected void onHandleSuccess(CollectBean collectBean) {
                showSnackbar(id);
            }
        }, id);
    }

    /**
     * 收藏网站
     *
     * @param name
     * @param link
     */
    private void collectWebsite(String name, String link) {
        httpManage.collectWebsite(new BaseObserver<WebsiteCollectBean>(WebActivity.this) {
            @Override
            protected void onHandleSuccess(WebsiteCollectBean websiteCollectBean) {
                int collectId = websiteCollectBean.getId();
                showSnackbar(collectId);
            }
        }, name, link);
    }

    /**
     * 取消收藏文章
     *
     * @param id
     */
    private void uncollect(int id) {
        if (type == TYPE_ARTICLE) {
            httpManage.uncollect(new BaseObserver<CollectBean>(WebActivity.this) {
                @Override
                protected void onHandleSuccess(CollectBean collectBean) {

                }
            }, id);
        } else {
            httpManage.unCollectWebsite(new BaseObserver<WebsiteCollectBean>(WebActivity.this) {
                @Override
                protected void onHandleSuccess(WebsiteCollectBean websiteCollectBean) {

                }
            }, id);
        }

    }

    /**
     * 用本地浏览器打开
     *
     * @param s
     */
    private void openWithSelfBrowser(String s) {
        Uri uri = Uri.parse(s);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void showSnackbar(int id) {
        Snackbar snackbar = Snackbar.make(toolbar, "收藏成功", Snackbar.LENGTH_LONG);
        snackbar.setAction("撤销", v -> uncollect(id));
        snackbar.show();
    }

    private void showToast() {
        Toast.makeText(WebActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
    }
}
