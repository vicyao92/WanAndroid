package com.vic.wanandroid.module.collect.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vic.wanandroid.R;
import com.vic.wanandroid.http.HttpManage;
import com.vic.wanandroid.module.collect.bean.WebsiteCollectBean;

public class CustomDialog extends DialogFragment {
    private EditText etName;
    private EditText etUrl;
    private Button btnEditConfirm;
    private WebsiteCollectBean websiteBean;
    private HttpManage httpManage;
    private DialogFragment dialog;
    private OnConfirmClickListener listener;

    public CustomDialog(WebsiteCollectBean websiteBean) {
        this.websiteBean = websiteBean;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_website_edit, container, false);
        etName = view.findViewById(R.id.et_name);
        etUrl = view.findViewById(R.id.et_link);
        btnEditConfirm = view.findViewById(R.id.btn_edit_confirm);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpManage = HttpManage.init(getContext());
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        etName.setText(websiteBean.getName());
        etUrl.setText(websiteBean.getLink());
        btnEditConfirm.setOnClickListener(v -> {
            int id = websiteBean.getId();
            String name = etName.getText().toString();
            String link = etUrl.getText().toString();
            if (name.length() != 0 && link.length() != 0) {
                if (listener != null) {
                    listener.onClick(id, name, link);
                }
            } else {
                Toast.makeText(getContext(), "不能为空！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateInput(String input) {
        if (input.length() == 0) {
            Toast.makeText(getContext(), "不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        this.listener = listener;
    }

    public interface OnConfirmClickListener {
        void onClick(int id, String name, String link);
    }
}
