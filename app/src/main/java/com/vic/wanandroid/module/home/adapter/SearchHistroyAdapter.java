package com.vic.wanandroid.module.home.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vic.wanandroid.R;

import java.util.List;

public class SearchHistroyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private OnDeleteClickListener listener = null;

    public SearchHistroyAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_history, item);
        helper.getView(R.id.btn_delete_histroy).setOnClickListener(v -> {
            if (listener != null) {
                int pos = helper.getLayoutPosition();
                listener.onClick(item, pos);
            }
        });
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }

    public interface OnDeleteClickListener {
        void onClick(String item, int pos);
    }
}
