package com.rae.cnblogs.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.home.R;
import com.rae.cnblogs.home.holder.SearchSuggestionHolder;

/**
 * 搜索建议
 * Created by ChenRui on 2017/8/29 0029 9:56.
 */
public class SearchSuggestionAdapter extends BaseItemAdapter<String, SearchSuggestionHolder> {

    private View.OnClickListener mSelectedClickListener;

    public void setSelectedClickListener(View.OnClickListener selectedClickListener) {
        mSelectedClickListener = selectedClickListener;
    }

    @Override
    public SearchSuggestionHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new SearchSuggestionHolder(inflateView(parent, R.layout.item_search_suggestion));
    }

    @Override
    public void onBindViewHolder(SearchSuggestionHolder holder, int position, String m) {
        holder.getTitleView().setText(m);
        holder.getSelectedView().setTag(m);
        holder.getSelectedView().setOnClickListener(mSelectedClickListener);
    }
}
