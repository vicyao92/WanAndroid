package com.vic.wanandroid.module.collect.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexboxLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseActivity;
import com.vic.wanandroid.base.WebActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.collect.bean.WebsiteCollectBean;
import com.vic.wanandroid.module.collect.fragment.CustomDialog;
import com.vic.wanandroid.module.collect.fragment.NormalDialog;
import com.vic.wanandroid.module.collect.fragment.OptionDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebsiteCollectActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fbl_content)
    FlexboxLayout fblContent;
    private List<WebsiteCollectBean> websiteList;
    private List<TextView> tagList = new ArrayList<>();
    private String[] options = new String[]{"编辑", "删除"};
    private Context context = WebsiteCollectActivity.this;

    public static void start(Context context) {
        Intent intent = new Intent(context, WebsiteCollectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_collect);
        ButterKnife.bind(this);
        httpManage = HttpManage.init(WebsiteCollectActivity.this);
        initToolbar();
        createProgressBar(this);
        showProgressBar();
        loadData();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
            actionBar.setTitle(R.string.collect_website);
            toolbar.inflateMenu(R.menu.menu_toolbar_home);
        }
    }

    private void loadData() {
        httpManage.getWebsiteCollect(new BaseObserver<List<WebsiteCollectBean>>(WebsiteCollectActivity.this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgressBar();
            }

            @Override
            protected void onHandleSuccess(List<WebsiteCollectBean> websiteCollectBeans) {
                websiteList = websiteCollectBeans;
                hideProgressBar();
                initFbl(websiteList);
            }
        });
    }

    private void initFbl(List<WebsiteCollectBean> data) {
        for (int i = 0; i < data.size(); i++) {
            final int pos = i;
            WebsiteCollectBean bean = data.get(i);
            int id = bean.getId();
            String name = bean.getName();
            String link = bean.getLink();
            TextView textView = (TextView) LayoutInflater
                    .from(WebsiteCollectActivity.this)
                    .inflate(R.layout.item_website_collect, fblContent, false);
            textView.setText(data.get(i).getName());
            textView.setTextColor(Color.WHITE);
            GradientDrawable myGrad = (GradientDrawable) textView.getBackground();
            myGrad.setColor(randomColor());
            textView.setOnClickListener(v -> WebActivity.start(WebsiteCollectActivity.this, id, name, link, 1));
            textView.setOnLongClickListener(v -> {
                OptionDialog optionDialog = new OptionDialog(options);
                optionDialog.setOnItemClickListener(pos1 -> {
                    if (pos1 == 0) {
                        CustomDialog editDialog = new CustomDialog(bean);
                        editDialog.setOnConfirmClickListener((id1, name1, link1) ->
                                httpManage.updateWebsite(new BaseObserver<WebsiteCollectBean>(context) {
                                    @Override
                                    protected void onHandleSuccess(WebsiteCollectBean websiteCollectBean) {
                                        Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
                                        editDialog.dismiss();
                                    }
                                }, id1, name1, link1));
                        editDialog.show(getSupportFragmentManager(), "EditDialog");
                    } else {
                        NormalDialog dialog = new NormalDialog("确定要删除吗？");
                        dialog.setPositiveClickListener(() -> uncollect(id, pos1));
                        dialog.show(getSupportFragmentManager(), "DeleteDialog");
                    }
                    optionDialog.dismiss();
                });
                optionDialog.show(getSupportFragmentManager(), "OptionDialog");
                return false;
            });
            tagList.add(textView);
            fblContent.addView(textView);
        }
    }

    private int randomColor() {
        int[] randomColors = getResources().getIntArray(R.array.tvBackgroundColor);
        int color = randomColors[new Random().nextInt(randomColors.length)];
        return color;
    }

    private void uncollect(int id, int pos) {
        httpManage.unCollectWebsite(new BaseObserver<WebsiteCollectBean>(WebsiteCollectActivity.this) {
            @Override
            protected void onHandleSuccess(WebsiteCollectBean websiteCollectBean) {
                fblContent.removeView(tagList.get(pos));
            }
        }, id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

}
