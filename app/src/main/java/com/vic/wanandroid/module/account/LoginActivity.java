package com.vic.wanandroid.module.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseActivity;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.account.activity.RegisterActivity;
import com.vic.wanandroid.module.account.bean.LoginBean;
import com.vic.wanandroid.utils.DatabaseHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tie_account)
    TextInputEditText tieAccount;
    @BindView(R.id.til_account)
    TextInputLayout tilAccount;
    @BindView(R.id.tie_password)
    TextInputEditText tiePassword;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private HttpManage httpManage;
    private DatabaseHelper databaseHelper;
    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initToolbar();
        httpManage = HttpManage.init(LoginActivity.this);
        databaseHelper = DatabaseHelper.init(LoginActivity.this);
/*        tieAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateAccount(s.toString());
            }
        });
        tiePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(s.toString());
            }
        });*/
    }

    /**
     * 验证账户名格式
     *
     * @param input 用户名
     * @return
     */
    private boolean validateAccount(String input) {
        if (input.length() == 0) {
            showError(tilAccount, "用户名不能为空");
        } else if (input.length() < 6) {
            showError(tilAccount, "用户名长度至少6位");
        } else if (input.length() > 50) {
            showError(tilAccount, "用户名长度不能超过50位");
        } else {
            if (inputPattern(input)) {
                tilAccount.setErrorEnabled(false);
                return true;
            } else {
                showError(tilAccount, "用户名仅能包括字母 ，数字以及下划线");
            }
        }
        return false;
    }

    /**
     * 验证密码格式
     *
     * @param input 密码
     * @return
     */
    private boolean validatePassword(String input) {
        if (input.length() == 0) {
            showError(tilPassword, "密码不能为空");
        } else if (input.length() < 6) {
            showError(tilPassword, "密码长度至少6位");
        } else if (input.length() > 50) {
            showError(tilPassword, "密码长度不能超过50位");
        } else {
            if (inputPattern(input)) {
                tilPassword.setErrorEnabled(false);
                return true;
            } else {
                showError(tilPassword, "密码仅能包括字母 ，数字以及下划线");
            }
        }
        return false;
    }

    /**
     * 显示错误提示，并获取焦点
     *
     * @param textInputLayout
     * @param error
     */
    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    private boolean inputPattern(String msg) {
        String reg = "^[a-zA-Z0-9_-]{6,50}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(msg);
        return matcher.matches();
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        String account = tieAccount.getText().toString();
        String pwd = tiePassword.getText().toString();
        if (validateAccount(account) && validatePassword(pwd)) {
            doLogin(account, pwd);
        }
    }

    private void doLogin(String username, String password) {
        httpManage.login(new BaseObserver<LoginBean>(LoginActivity.this) {
            @Override
            protected void onHandleSuccess(LoginBean loginBean) {
                //Todo 数据库保存登录用户数据
                databaseHelper.add(loginBean);
                finish();
            }
        },username,password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.btn_login);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
            toolbar.setNavigationOnClickListener(v -> finish());
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case android.R.id.home:
                            finish();
                            break;
                        case R.id.btn_register:
                            RegisterActivity.start(LoginActivity.this);
                            break;
                    }
                    return true;
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
