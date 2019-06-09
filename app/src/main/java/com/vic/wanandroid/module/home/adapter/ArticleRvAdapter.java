package com.vic.wanandroid.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vic.wanandroid.R;
import com.vic.wanandroid.module.home.bean.ArticleBean;
import java.util.List;

public class ArticleRvAdapter extends RecyclerView.Adapter<ArticleRvAdapter.ViewHolder> {

    private List<ArticleBean> articleLists;
    private Context context;

    public ArticleRvAdapter(List<ArticleBean> articleLists, Context context) {
        this.context = context;
        this.articleLists = articleLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_article_lists, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAuthor.setText(articleLists.get(position).getAuthor());
        holder.tvUpdateTime.setText(String.valueOf(articleLists.get(position).getPublishTime()));
        holder.tvTitle.setText(articleLists.get(position).getTitle());
        holder.tvSuperChapterName.setText(articleLists.get(position).getSuperChapterName());
        holder.tvChapterName.setText(articleLists.get(position).getChapterName());
        if (articleLists.get(position).getEnvelopePic().equals("")){
            holder.ivImg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return articleLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor;
        TextView tvUpdateTime;
        ImageView ivImg;
        TextView tvTitle;
        TextView tvSuperChapterName;
        TextView tvChapterName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvUpdateTime= itemView.findViewById(R.id.tv_update_time);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSuperChapterName = itemView.findViewById(R.id.tv_super_chapter_name);
            tvChapterName = itemView.findViewById(R.id.tv_chapter_name);
        }
    }
}
