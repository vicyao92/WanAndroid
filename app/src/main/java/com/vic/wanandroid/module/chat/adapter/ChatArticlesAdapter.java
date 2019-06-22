package com.vic.wanandroid.module.chat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vic.wanandroid.R;
import com.vic.wanandroid.module.home.bean.ArticleBean;

import java.util.List;

public class ChatArticlesAdapter extends BaseQuickAdapter<ArticleBean,BaseViewHolder> {
    private Context mContext;
    public ChatArticlesAdapter(int layoutResId, @Nullable List<ArticleBean> data,Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleBean item) {
        helper.setText(R.id.tv_author, item.getAuthor());
        helper.setText(R.id.tv_update_time, String.valueOf(item.getPublishTime()));
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_super_chapter_name, item.getSuperChapterName());
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
    }
}
