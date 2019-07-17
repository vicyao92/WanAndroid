package com.vic.wanandroid.module.collect.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class NormalDialog extends DialogFragment {
    private OnPositiveClickListener listener = null;
    private String message;

    public NormalDialog(String msg) {
        this.message = msg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> {
                    if (listener != null) {
                        listener.onClick();
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setCancelable(false);

        return builder.create();
    }

    public void setPositiveClickListener(OnPositiveClickListener listener) {
        this.listener = listener;
    }

    public interface OnPositiveClickListener {
        void onClick();
    }

}
