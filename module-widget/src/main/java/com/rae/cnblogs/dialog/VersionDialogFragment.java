package com.rae.cnblogs.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.widget.R;
import com.rae.cnblogs.widget.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 版本更新对话框
 */
public class VersionDialogFragment extends BasicDialogFragment {

    private String versionName;
    private String versionDesc;
    private String versionUrl;

    @BindView(R2.id.tv_message)
    TextView mContentView;

    @BindView(R2.id.tv_title)
    TextView mVersionNameView;


    public static VersionDialogFragment newInstance(String versionName, String desc, String url) {

        Bundle args = new Bundle();
        args.putString("versionName", versionName);
        args.putString("versionDesc", desc);
        args.putString("versionUrl", url);
        VersionDialogFragment fragment = new VersionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fm_dialog_version_card;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            versionName = getArguments().getString("versionName");
            versionDesc = getArguments().getString("versionDesc");
            versionUrl = getArguments().getString("versionUrl");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentView.setText(Html.fromHtml(versionDesc));
        mVersionNameView.setText(versionName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
    }

    public void show(FragmentManager manager) {
        super.show(manager, "versionUpdate");
    }

    @OnClick(R2.id.btn_ensure)
    void onUpdateClick() {
        routeToUrl(versionUrl);
        dismiss();
    }

    @OnClick(R2.id.btn_cancel)
    void onCancelClick() {
        dismiss();
    }

    public void routeToUrl(String url) {
        try {
            if (getContext() == null || TextUtils.isEmpty(url)) return;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            UICompat.failed(getContext(), "未找到关联的应用程序，请前往应用市场更新。");
        }
    }

}
