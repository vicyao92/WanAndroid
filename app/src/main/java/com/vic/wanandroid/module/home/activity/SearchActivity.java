package com.vic.wanandroid.module.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseActivity;
import com.vic.wanandroid.module.home.fragment.SearchHistoryFragment;
import com.vic.wanandroid.module.home.fragment.SearchResultFragment;
import com.vic.wanandroid.utils.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    private static final String TAG_HISTORY = "historyFragment";
    private static final String TAG_RESULT = "resultFragment";
    private final Context mContext = SearchActivity.this;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    private SearchView mSearchView;
    private EditText editText;
    private SearchHistoryFragment mSearchHistoryFragment;
    private SearchResultFragment mSearchResultFragment;
    private FragmentManager fm;
    private boolean isResultFragment = false;
    private DatabaseHelper helper;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        fm = getSupportFragmentManager();
        initToolbar();
        initView();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        Fragment historyFragment = fm.findFragmentByTag(TAG_HISTORY);
        Fragment resultFragment = fm.findFragmentByTag(TAG_RESULT);
        FragmentTransaction transaction = fm.beginTransaction();
        if (historyFragment == null) {
            mSearchHistoryFragment = new SearchHistoryFragment();
            transaction.add(R.id.fl_container, mSearchHistoryFragment, TAG_HISTORY);
        } else {
            mSearchHistoryFragment = (SearchHistoryFragment) historyFragment;
        }

        if (resultFragment == null) {
            mSearchResultFragment = new SearchResultFragment();
            transaction.add(R.id.fl_container, mSearchResultFragment, TAG_RESULT);
        } else {
            mSearchResultFragment = (SearchResultFragment) resultFragment;
        }

        transaction.hide(mSearchResultFragment);
        transaction.show(mSearchHistoryFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (isResultFragment) {
            showHistory();
        } else {
            super.onBackPressed();
        }
    }

    private void showHistory() {
        isResultFragment = false;
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.show(mSearchHistoryFragment);
        transaction.hide(mSearchResultFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) menuItem.getActionView();
        initSearchView(mSearchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isResultFragment) {
                showHistory();
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    private void initSearchView(SearchView searchView) {
        searchView.onActionViewExpanded();
        //设置搜索框展开时是否显示提交按钮，可不显示
        searchView.setSubmitButtonEnabled(true);
        //让键盘的回车键设置成搜索
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //设置输入框文字颜色
        editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.text_normal));
        editText.setTextColor(ContextCompat.getColor(this, R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 0) {
                    showHistory();
                } else {
                    SearchHistoryFragment fragment = (SearchHistoryFragment) fm.findFragmentByTag(TAG_HISTORY);
                    fragment.refreshHistroy(query);
                    fragment.saveHistory(query);
                    search(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void search(String key) {
        showResult();
        mSearchResultFragment.requestData(key);
    }

    private void showResult() {
        isResultFragment = true;
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.show(mSearchResultFragment);
        transaction.hide(mSearchHistoryFragment);
        transaction.commit();
    }

    public void updateSearchText(String text) {
        editText.setText(text);
    }

}
