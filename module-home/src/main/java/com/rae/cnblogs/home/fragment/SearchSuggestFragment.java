package com.rae.cnblogs.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rae.cnblogs.basic.ApplicationCompat;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.home.R;
import com.rae.cnblogs.home.R2;
import com.rae.cnblogs.home.adapter.SearchSuggestionAdapter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.event.SearchEvent;
import com.rae.cnblogs.user.friends.ISearchListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * 搜索建议
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SearchSuggestFragment extends BasicFragment implements ISearchListener {

    @BindView(R2.id.rec_search)
    RecyclerView mRecyclerView;
    SearchSuggestionAdapter mAdapter;

    private ISearchApi mSearchApi;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_search_suggest;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchApi = CnblogsApiFactory.getInstance(getContext()).getSearchApi();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SearchSuggestionAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        // 搜索建议上档点击
        mAdapter.setSelectedClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = (String) v.getTag();
                EventBus.getDefault().post(new SearchEvent(text));
            }
        });

        // 执行搜索
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<String>() {
            @Override
            public void onItemClick(Context context, String item) {
                EventBus.getDefault().post(new SearchEvent(item, true));
            }
        });
    }

    @Override
    public void onSearch(CharSequence text) {
        if (ApplicationCompat.checkFragmentIsActive(this)) {
            loadData(text.toString());
        }
    }

    private void loadData(String text) {
        if (mSearchApi == null) return;
        AndroidObservable.create(mSearchApi.getSuggestion(text))
                .with(this)
                .subscribe(new ApiDefaultObserver<List<String>>() {
                    @Override
                    protected void onError(String message) {
                        // 不处理
                    }

                    @Override
                    protected void accept(List<String> data) {
                        onLoadData(data);
                    }
                });
    }

    private void onLoadData(List<String> data) {
        mAdapter.setDataList(data);
        mAdapter.notifyDataSetChanged();
    }

}
