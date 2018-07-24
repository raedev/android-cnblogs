package com.rae.cnblogs.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.widget.R;
import com.rae.cnblogs.widget.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 写评论
 * Created by ChenRui on 2017/1/31 23:17.
 */
public class EditCommentDialogFragment extends BasicDialogFragment {

    /**
     * 来自博客
     */
    public static final int FROM_TYPE_BLOG = 0;

    /**
     * 来自闪存
     */
    public static final int FROM_TYPE_MOMENT = 1;

    public static EditCommentDialogFragment newInstance(int fromType, @Nullable Entry entry) {

        Bundle args = new Bundle();
        args.putInt("fromType", fromType);
        args.putParcelable("entry", entry);
        EditCommentDialogFragment fragment = new EditCommentDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        }
    }

    public static class Entry<T extends Parcelable> implements Parcelable {

        private String mAuthorName;
        private String mContent;

        private T mSource;

        public String getAuthorName() {
            return mAuthorName;
        }

        public String getContent() {
            return mContent;
        }

        public T getSource() {
            return mSource;
        }

        public void setSource(T source) {
            mSource = source;
        }

        public void setAuthorName(String authorName) {
            mAuthorName = authorName;
        }

        public void setContent(String content) {
            mContent = content;
        }

        public Entry() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mAuthorName);
            dest.writeString(this.mContent);
            dest.writeParcelable(this.mSource, flags);
        }

        protected Entry(Parcel in) {
            this.mAuthorName = in.readString();
            this.mContent = in.readString();
            this.mSource = in.readParcelable(Parcelable.class.getClassLoader());
        }

        public static final Creator<Entry> CREATOR = new Creator<Entry>() {
            @Override
            public Entry createFromParcel(Parcel source) {
                return new Entry(source);
            }

            @Override
            public Entry[] newArray(int size) {
                return new Entry[size];
            }
        };
    }

    public interface OnEditCommentListener {

        /**
         * 当发布按钮点击的时候出发
         *
         * @param content     发表内容
         * @param entry       引用评论
         * @param isReference 是否引用评论内容
         */
        void onPostComment(EditCommentDialogFragment dialog, String content, @Nullable Entry entry, boolean isReference);
    }

    @BindView(R2.id.btn_send_comment)
    Button mSendButton;

    @BindView(R2.id.et_edit_comment_body)
    EditText mBodyView;

    @BindView(R2.id.cb_ref_comment)
    CheckBox mReferenceView;

    @BindView(R2.id.ll_content_comment)
    View mContentLayout;

    @BindView(R2.id.ll_comment_loading)
    View mLoadingLayout;


    @Nullable
    private Entry mEntry; // 引用的实体
    private int mFromType; // 来源类型
    private OnEditCommentListener mOnEditCommentListener;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_blog_comment_edit;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mFromType = arguments.getInt("fromType");
            mEntry = arguments.getParcelable("entry");
        }
    }

    @Override
    protected void onLoadWindowAttr(@NonNull Window window) {
        super.onLoadWindowAttr(window);
        window.setDimAmount(0.5f);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            window.setElevation(0f);
        }
    }

    @Override
    protected void onLoadData(Bundle arguments) {
        super.onLoadData(arguments);
        dismissLoading();
        mReferenceView.setVisibility(View.GONE);
        mBodyView.setText("");
        mBodyView.setHint(R.string.edit_comment_hint);

        // 回调
        if (getActivity() instanceof OnEditCommentListener) {
            setOnEditCommentListener((OnEditCommentListener) getActivity());
        }
        if (getParentFragment() instanceof OnEditCommentListener) {
            setOnEditCommentListener((OnEditCommentListener) getParentFragment());
        }

        // 来自博客
        if (mEntry != null && mFromType == FROM_TYPE_BLOG) {
            mReferenceView.setVisibility(View.VISIBLE);
            mReferenceView.setChecked(true);
            mReferenceView.setText("引用@" + mEntry.getAuthorName() + "的评论");
            mBodyView.setHint("回复：“" + subString(mEntry.getContent()) + "”");
        }

        // 来自闪存
        if (mEntry != null && mFromType == FROM_TYPE_MOMENT) {
            mBodyView.setHint("回复：“@" + mEntry.getAuthorName() + " " + subString(mEntry.getContent()) + "”");
        }

    }

    private String subString(String text) {
        if (TextUtils.isEmpty(text)) return text;
        if (text.length() < 20) return text;
        return text.substring(0, 20) + "...";
    }

    public void setOnEditCommentListener(OnEditCommentListener onEditCommentListener) {
        mOnEditCommentListener = onEditCommentListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dismissLoading();
        mBodyView.setText("");
        mEntry = null;
    }

    /**
     * 显示加载中
     */
    public void showLoading() {
        // 取消外部点击
        setCanceledOnTouchOutside(false);
        mContentLayout.setVisibility(View.INVISIBLE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mSendButton.setEnabled(false);
        mSendButton.setVisibility(View.INVISIBLE);
        UICompat.fadeIn(mLoadingLayout);
    }

    public void dismissLoading() {
        setCanceledOnTouchOutside(true);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.INVISIBLE);
        mSendButton.setEnabled(true);
        mSendButton.setVisibility(View.VISIBLE);
    }

    public boolean enableReferenceComment() {
        return mReferenceView.isChecked();
    }

    public String getCommentContent() {
        return mBodyView.getText().toString().trim();
    }

    @OnClick(R2.id.btn_send_comment)
    void onSendClick() {
        if (TextUtils.isEmpty(getCommentContent())) {
            return;
        }

        if (mOnEditCommentListener != null) {
            mOnEditCommentListener.onPostComment(this, getCommentContent(), mEntry, mReferenceView.isChecked());
            showLoading();
        }
    }

    @OnClick(R2.id.btn_cancel)
    void onCancelClick() {
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new SlideDialog(getContext());
    }
}
