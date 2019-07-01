package com.vic.wanandroid.module.navigate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.vic.wanandroid.R;
import com.vic.wanandroid.module.navigate.bean.NavigationBean;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NavigationAdapter extends BaseQuickAdapter<NavigationBean,BaseViewHolder> {

    private Queue<TextView> mFlexTextItems = new LinkedList<>();
    private NavigationAdapter.OnItemClickListener mOnItemClickListener = null;
    private FlexboxLayout fbl;
    private LayoutInflater mInflater = null;
    public NavigationAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    private TextView getTextViewFromCache(FlexboxLayout flexboxLayout){
        TextView tv = mFlexTextItems.poll();
        if (tv != null) {
            return tv;
        }
        return createFlexItemTextView(fbl);
    }

    private TextView createFlexItemTextView(FlexboxLayout flexboxLayout){
        if (mInflater == null) {
            mInflater = LayoutInflater.from(fbl.getContext());
        }
        return (TextView) mInflater.inflate(R.layout.item_fbl, fbl, false);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationBean item) {
        helper.setText(R.id.tv_name,item.getName());
        fbl = helper.getView(R.id.fbl);
        for (int i= 0;i<item.getArticles().size();i++){
            TextView textView = getTextViewFromCache(fbl);
            textView.setText(item.getArticles().get(i).getTitle());
            int position = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(item,position);
                    }
                }
            });
            fbl.addView(textView);
        }
    }

    public interface OnItemClickListener {
        void onClick(NavigationBean bean, int pos);
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        FlexboxLayout fbl = holder.getView(R.id.fbl);
        for (int i = 0; i < fbl.getChildCount(); i++) {
            mFlexTextItems.offer((TextView) fbl.getChildAt(i));
        }
        fbl.removeAllViews();
    }

    public void setOnItemClickListener(NavigationAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
