package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rae.cnblogs.theme.ThemeCompat;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Loading and Empty view
 * Created by ChenRui on 2016/11/10 0010 14:28.
 */
public class PlaceholderView extends FrameLayout implements SkinCompatSupportable {

    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_LOADING = 1;
    private static final int TYPE_LOGIN = 2;
    private SkinCompatBackgroundHelper mBackgroundTintHelper;
    private View mEmptyView;
    private View mLoginView;
    View mLoadingView;
    private ImageView mEmptyImageView;
    private TextView mEmptyMessageView;
    private Drawable mDefaultEmptyIcon;
    private View mRetryView;
    private View mContentView;
    TextView mLoadingTextView;
    String mLoadingText;
    ProgressBar mLoadingProgressBar;
    private View mLoginBtn;

    public PlaceholderView(Context context) {
        super(context);
        initView(null, 0);
    }

    public PlaceholderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0);
        initAttr(attrs);
    }

    public PlaceholderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlaceholderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs, defStyleRes);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
    }


    protected void initView(AttributeSet attrs, int defStyleAttr) {

        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);

        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.view_placeholder, this, false);
        mEmptyView = mContentView.findViewById(R.id.ll_placeholder_empty);
        mLoginView = mContentView.findViewById(R.id.ll_placeholder_login);
        mLoadingView = mContentView.findViewById(R.id.ll_placeholder_loading);
        mLoadingProgressBar = mContentView.findViewById(R.id.pb_loading);
        mLoadingTextView = mContentView.findViewById(R.id.tv_loading);
        mEmptyImageView = mContentView.findViewById(R.id.img_placeholder_empty);
        mEmptyMessageView = mContentView.findViewById(R.id.tv_placeholder_empty_message);
        mRetryView = mContentView.findViewById(R.id.btn_placeholder_retry);
        mLoginBtn = mContentView.findViewById(R.id.btn_placeholder_login);
        if (getBackground() == null) {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
        }
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PlaceholderView);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int index = a.getIndex(i);
            if (index == R.styleable.PlaceholderView_mode) {
                int mode = a.getInt(index, 1);
                switchMode(mode);

            } else if (index == R.styleable.PlaceholderView_empty_message) {
                String msg = a.getString(index);
                if (!TextUtils.isEmpty(msg)) {
                    setEmptyMessage(msg.equals("null") ? null : msg);
                }

            } else if (index == R.styleable.PlaceholderView_app_background) {
                mContentView.setBackground(a.getDrawable(index));

            } else if (index == R.styleable.PlaceholderView_empty_icon) {
                mDefaultEmptyIcon = a.getDrawable(index);
                setEmptyIcon(mDefaultEmptyIcon);

            } else if (index == R.styleable.PlaceholderView_loading_message) {
                mLoadingText = a.getString(index);
                mLoadingTextView.setText(mLoadingText);

            }
        }
        a.recycle();
    }

    /**
     * 切换显示类型
     *
     * @param mode 参考attr.xml 定义的PlaceholderView#mode 取值
     */
    private void switchMode(int mode) {
        if (mode == 0) {
            empty();
        } else {
            loading();
        }
    }

    /**
     * show loading view
     */
    public void loading() {
        show();
        switchHolderType(TYPE_LOADING);
        if (mLoadingText == null)
            mLoadingText = getContext().getString(R.string.loading);
        mLoadingTextView.setText(mLoadingText);
    }

    /**
     * 切换显示类型
     */
    private void switchHolderType(int type) {

        int emptyVisibility = GONE;
        int loadingVisibility = GONE;
        int loginVisibility = GONE;
        switch (type) {
            case TYPE_EMPTY:
                emptyVisibility = VISIBLE;
                break;
            case TYPE_LOADING:
                loadingVisibility = VISIBLE;
                break;
            case TYPE_LOGIN:
                loginVisibility = VISIBLE;
                break;
        }

        mEmptyView.setVisibility(emptyVisibility);
        mLoginView.setVisibility(loginVisibility);
        mLoadingView.setVisibility(loadingVisibility);
    }

    public void loading(String text) {
        loading();
        mLoadingTextView.setVisibility(View.VISIBLE);
        mLoadingTextView.setText(text);
    }

    /**
     * show newThread view
     */
    public void empty() {
        show();
        if (mDefaultEmptyIcon == null) {
            mDefaultEmptyIcon = ThemeCompat.getDrawable(getContext(), "ic_empty_placeholder");
        }
        setEmptyIcon(mDefaultEmptyIcon);
        switchHolderType(TYPE_EMPTY);
    }

    public void empty(int defaultEmptyIcon) {
        show();
        setEmptyIcon(defaultEmptyIcon);
        switchHolderType(TYPE_EMPTY);
    }

    public void retry(String msg) {
        empty(msg);
        mRetryView.setVisibility(View.VISIBLE);
    }

    /**
     * 网络错误
     */
    public void networkError() {
        show();
        setEmptyIcon(ThemeCompat.getDrawableId(getContext(), "ic_network_error_placeholder"));
        mRetryView.setVisibility(VISIBLE);
        switchHolderType(TYPE_EMPTY);
    }


    public void showLogin() {
        show();
        switchHolderType(TYPE_LOGIN);
    }

    /**
     * @param msg messages
     */
    public void empty(@Nullable String msg) {
        if (msg == null) {
            msg = getContext().getString(R.string.empty_message);
        }
        setEmptyMessage(msg);
        empty();
    }


    /**
     * newThread message
     *
     * @param msg
     */
    public void setEmptyMessage(String msg) {
        mEmptyMessageView.setVisibility(TextUtils.isEmpty(msg) || TextUtils.equals(msg, "@null") ? GONE : VISIBLE);
        mEmptyMessageView.setText(msg);
    }

    /**
     * 重试按钮点击
     *
     * @param listener
     */
    public void setOnRetryClickListener(final OnClickListener listener) {
        mRetryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                listener.onClick(v);
            }
        });
    }

    /**
     * 登录按钮点击
     */
    public void setOnLoginListener(OnClickListener listener) {
        mLoginBtn.setOnClickListener(listener);
    }


    public void dismiss() {
        mContentView.setVisibility(GONE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
        mContentView.setVisibility(VISIBLE);
    }

    public void setEmptyIcon(Drawable icon) {
        if (icon != null) {
            mEmptyImageView.setImageDrawable(icon);
            mDefaultEmptyIcon = mEmptyImageView.getDrawable();
        }
    }

    public void setEmptyIcon(int resId) {
        if (resId > 0) {
            mEmptyImageView.setImageResource(resId);
            mDefaultEmptyIcon = mEmptyImageView.getDrawable();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addView(mContentView);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeView(mContentView);
    }

    /**
     * 是否已经消失
     */
    public boolean isDismiss() {
        return mContentView.getVisibility() != View.VISIBLE;
    }

    /**
     * 注册adapter监听
     *
     * @param adapter
     */
    public void registerAdapterDataObserver(final RecyclerView.Adapter adapter) {
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (adapter.getItemCount() > 0) {
                    dismiss();
                } else {
                    empty();
                }
            }
        });
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        if (mContentView != null)
            mContentView.setOnClickListener(l);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        mContentView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        mContentView.setBackgroundResource(resid);
    }
}
