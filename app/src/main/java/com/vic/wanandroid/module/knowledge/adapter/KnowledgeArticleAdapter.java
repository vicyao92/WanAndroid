package com.vic.wanandroid.module.knowledge.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vic.wanandroid.R;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeSystemBean;

import java.util.List;

public class KnowledgeArticleAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {
    public KnowledgeArticleAdapter(int layoutResId, @Nullable List<ArticleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleBean item) {
        helper.setText(R.id.tv_author, item.getAuthor());
        helper.setText(R.id.tv_update_time, item.getNiceDate());
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
