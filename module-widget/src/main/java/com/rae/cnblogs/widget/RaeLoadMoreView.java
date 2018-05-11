package com.rae.cnblogs.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;

/**
 * 加载更多
 * Created by ChenRui on 2016/12/3 17:56.
 */
public class RaeLoadMoreView extends LoadingMoreFooter {

    private TextView mTextView;
    private String mNoMoreText;
    private View mProgressBar;

    public RaeLoadMoreView(Context context) {
        this(context, null);
    }

    public RaeLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 32, 0, 32);
    }

    @Override
    public void initView() {
        super.initView();
        mProgressBar = getChildAt(0);
        mTextView = (TextView) getChildAt(1);
        mTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.ph3));
        mNoMoreText = getResources().getString(R.string.no_more_tips);
    }

    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    public void setNoMoreText(String text) {
        mNoMoreText = text;
    }

    @Override
    public void setState(int state) {
        if (state == STATE_NOMORE) {
            mTextView.setText(mNoMoreText);
            mProgressBar.setVisibility(GONE);
            this.setVisibility(VISIBLE);
            return;
        }
        super.setState(state);
    }
}
