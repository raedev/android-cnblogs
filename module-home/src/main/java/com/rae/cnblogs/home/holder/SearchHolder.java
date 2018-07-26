package com.rae.cnblogs.home.holder;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.home.R;

/**
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SearchHolder extends RecyclerView.ViewHolder {

    private TextView mTextView;

    @Nullable
    private TextView mRankView; // 排行等级

    public SearchHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.tv_title);
        mRankView = itemView.findViewById(R.id.tv_rank);
    }

    public void setTitle(String title) {
        mTextView.setText(title);
    }

    public void setRank(int rank) {
        if (mRankView == null) return;
        mRankView.setText(String.valueOf(rank));
        switch (rank) {
            case 1:
                mRankView.setBackgroundResource(R.drawable.bg_hot_search_flag_one);
                mRankView.setTextColor(Color.WHITE);
                break;
            case 2:
                mRankView.setBackgroundResource(R.drawable.bg_hot_search_flag_two);
                mRankView.setTextColor(Color.WHITE);
                break;
            case 3:
                mRankView.setBackgroundResource(R.drawable.bg_hot_search_flag_three);
                mRankView.setTextColor(Color.WHITE);
                break;
            default:
                mRankView.setBackgroundColor(Color.TRANSPARENT);
                mRankView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.search_item_color));
                break;
        }
    }

}
