package com.vic.wanandroid.module.knowledge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vic.wanandroid.R;
import com.vic.wanandroid.module.knowledge.bean.KnowledgeSystemBean;
import com.google.android.flexbox.FlexboxLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class KnowledgeAdapter extends BaseQuickAdapter<KnowledgeSystemBean,BaseViewHolder> {
    private Context mContext;
    private Queue<TextView> mFlexTextItems = new LinkedList<>();
    private OnItemClickListener mOnItemClickListener = null;
    private FlexboxLayout fbl;
    private LayoutInflater mInflater = null;
    public KnowledgeAdapter(int layoutResId, @Nullable List<KnowledgeSystemBean> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, KnowledgeSystemBean item) {
        helper.setText(R.id.tv_name,item.getName().toString());
        fbl = helper.getView(R.id.fbl);
        for (int i= 0;i<item.getChildren().size();i++){
            TextView textView = getTextViewFromCache(fbl);
            textView.setText(item.getChildren().get(i).getName());
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
        fbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(item,0);
                }
            }
        });
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

    public interface OnItemClickListener {
        void onClick(KnowledgeSystemBean bean, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
