package com.rae.cnblogs.discover.column.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antcode.sdk.model.AntArticleInfo;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.RaeBaseQuickAdapter;

import java.util.List;

public class AntUserColumnDetailAdapter extends RaeBaseQuickAdapter<AntArticleInfo, BaseViewHolder> {

    private int mViewType;

    /**
     */
    public AntUserColumnDetailAdapter(Context context, @Nullable List<AntArticleInfo> data) {
        super(context, R.layout.item_user_column_detail, data);
    }

    public void setViewType(int type) {
        mViewType = type;
    }

    public int getViewType() {
        return mViewType;
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        layoutResId = mViewType == 1 ? R.layout.item_user_column_title : R.layout.item_user_column_detail;
        return super.createBaseViewHolder(parent, layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AntArticleInfo item) {
        if (mViewType == 1) {
            helper.setText(R.id.tv_title, item.getTitle());
            return;
        }
        AppImageLoader.display(item.getImageUrl(), (ImageView) helper.getView(R.id.img_cover));
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_summary, item.getSummary());
    }
}
