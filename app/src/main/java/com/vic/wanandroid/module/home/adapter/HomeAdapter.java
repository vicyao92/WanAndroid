package com.vic.wanandroid.module.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vic.wanandroid.R;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import com.vic.wanandroid.utils.TextUtils;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {
    private Context mContext;
    public HomeAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleBean item) {
        initTextWidget(helper, item, R.id.tv_author);
        initTextWidget(helper, item, R.id.tv_update_time);
        initTextWidget(helper, item, R.id.tv_title);
        initTextWidget(helper, item, R.id.tv_super_chapter_name);
        initTextWidget(helper, item, R.id.tv_chapter_name);
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

    private void initTextWidget(BaseViewHolder helper, ArticleBean item, int id) {
        switch (id) {
            case R.id.tv_author:
                helper.setText(id, item.getAuthor());
                break;
            case R.id.tv_update_time:
                helper.setText(id, item.getNiceDate());
                break;
            case R.id.tv_title:
                helper.setText(id, TextUtils.stripHtml(item.getTitle()));
                break;
            case R.id.tv_super_chapter_name:
                helper.setText(id, item.getSuperChapterName());
                break;
            case R.id.tv_chapter_name:
                helper.setText(id, item.getChapterName());
                break;
        }
    }

}
