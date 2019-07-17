package com.vic.wanandroid.module.collect.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vic.wanandroid.R;
import com.vic.wanandroid.module.collect.bean.CollectBean;

import java.util.List;

public class ArticleCollectionAdapter extends BaseItemDraggableAdapter<CollectBean, BaseViewHolder> {

    private OnCollectButtonClickClickListener listener = null;

    public ArticleCollectionAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectBean item) {
        helper.setText(R.id.tv_author, item.getAuthor());
        helper.setText(R.id.tv_update_time, String.valueOf(item.getNiceDate()));
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_chapter_name, item.getChapterName());
        String envelopePicUrl = item.getEnvelopePic();
        if (envelopePicUrl.equals("")) {
            helper.getView(R.id.iv_img).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.iv_img).setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(envelopePicUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((ImageView) helper.getView(R.id.iv_img));
        }
        helper.getView(R.id.btn_heart).setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(item);
            }
        });
    }

    public void setOnCollectButtonClickClickListener(OnCollectButtonClickClickListener listener) {
        this.listener = listener;
    }

    public interface OnCollectButtonClickClickListener {
        void onClick(CollectBean bean);
    }
}
