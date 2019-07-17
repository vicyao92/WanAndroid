package com.vic.wanandroid.module.account.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.http.BaseObserver;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.account.bean.LoginBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

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
    @BindView(R.id.tie_repassword)
    TextInputEditText tieRepassword;
    @BindView(R.id.til_repassword)
    TextInputLayout tilRepassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private HttpManage httpManage;

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.register);
        httpManage = HttpManage.init(RegisterActivity.this);
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        String accountName;
        String password;
        String rePasspord;
        accountName = String.valueOf(tieAccount.getText());
        password = String.valueOf(tiePassword.getText());
        rePasspord = String.valueOf(tieRepassword.getText());
        validateInput(tilAccount, accountName, "用户名");
        validateInput(tilPassword, password, "密码");
        validateInput(tilRepassword, rePasspord, "密码");
        if (!rePasspord.equals(password)) {
            showDialog("两次输入的密码不一致");
        } else {
            doRegister(accountName, password, rePasspord);
        }
    }

    /**
     * 验证账户名格式
     *
     * @param input 用户名
     * @return
     */
    private boolean validateInput(TextInputLayout view, String input, String errorMsg) {
        if (input.length() == 0) {
            showError(view, errorMsg + "不能为空");
        } else if (input.length() < 6) {
            showError(view, errorMsg + "长度至少6位");
        } else if (input.length() > 50) {
            showError(view, errorMsg + "长度不能超过50位");
        } else {
            if (inputPattern(input)) {
                view.setErrorEnabled(false);
                return true;
            } else {
                showError(view, errorMsg + "仅能包括字母 ，数字以及下划线");
            }
        }
        return false;
    }

    private void showDialog(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
        dialog.setMessage(msg);
        dialog.setPositiveButton("确定",
                (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    private void doRegister(String name, String pwd, String rePwd) {
        httpManage.register(new BaseObserver<LoginBean>(RegisterActivity.this) {
            @Override
            protected void onHandleSuccess(LoginBean loginBean) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                dialog.setMessage("注册成功");
                dialog.setPositiveButton("确定", (dialog1, which) -> {
                    dialog1.dismiss();
                    finish();
                });
                dialog.show();
            }
        }, name, pwd, rePwd);
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
}
