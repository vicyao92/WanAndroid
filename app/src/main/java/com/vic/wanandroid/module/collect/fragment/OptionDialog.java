package com.vic.wanandroid.module.collect.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class OptionDialog extends DialogFragment {
    private OnItemClickListener listener = null;
    private String[] items;

    public OptionDialog(String[] items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(getActivity());
        listDialog.setItems(items, (dialog, which) -> {
            if (listener != null) {
                listener.onClick(which);
            }
        });
        return listDialog.create();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(int pos);
    }
}
