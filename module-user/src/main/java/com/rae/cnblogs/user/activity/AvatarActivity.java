package com.rae.cnblogs.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;
import com.rae.cnblogs.user.personal.UserAvatarContract;
import com.rae.cnblogs.user.personal.UserAvatarPresenterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_AVATAR)
public class AvatarActivity extends SwipeBackBasicActivity implements UserAvatarContract.View {

    private static final int REQUEST_CODE_CROP = 1230;
    private static final int REQUEST_CODE_CROP_FILE_PROVIDER = 1231;
    @BindView(R2.id.img_avatar)
    ImageView mAvatarView;
    @BindView(R2.id.img_user_avatar)
    ImageView mAvatarLoadingView;

    @BindView(R2.id.ll_loading)
    ViewGroup mLoadingView;

    @BindView(R2.id.btn_preview)
    Button mPreviewButton;

    @BindView(R2.id.btn_media_selected)
    Button mSelectedButton;

    private UserInfoBean mUser;
    private String mSelectedPath; // 选择的图片
    private UserAvatarContract.Presenter mPresenter;

    @Override
    public String getUploadPath() {
        return mSelectedPath;
    }

    @Override
    public void onUploadSuccess() {
        dismissLoading();
        UICompat.toastInCenter(this, "头像更新成功");
    }

    @Override
    public void onUploadFailed(String message) {
        dismissLoading();
        UICompat.failed(this, message);
        // 还原默认状态
        mSelectedPath = null;
        AppImageLoader.displayAvatar(mUser.getAvatar(), mAvatarView);
        AppImageLoader.displayAvatar(mUser.getAvatar(), mAvatarLoadingView);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);
        mUser = UserProvider.getInstance().getLoginUserInfo();
        if (mUser == null) {
            finish();
            return;
        }
        mPresenter = new UserAvatarPresenterImpl(this);
        AppImageLoader.displayAvatar(mUser.getAvatar(), mAvatarView);
    }

    @OnClick({R2.id.btn_preview, R2.id.img_avatar, R2.id.img_user_avatar})
    public void onPreviewClick() {
        if (mSelectedPath != null) {
            AppRoute.routeToImagePreview(this, mSelectedPath);
        } else {
            AppRoute.routeToImagePreview(this, mUser.getAvatar());
        }
    }

    @OnClick(R2.id.btn_media_selected)
    public void onMediaClick() {
        AppRoute.routeToImageSelection(this, (String) null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AppRoute.REQ_IMAGE_SELECTION && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra("selectedImages");
            if (images != null && images.size() > 0) {
                String path = images.get(0);
                onAvatarImageChanged(path);
                // 跳转图片处理
                routeToCrop(path);
            }
        }

        // 图片裁剪返回
        if (resultCode == RESULT_OK && (requestCode == REQUEST_CODE_CROP || requestCode == REQUEST_CODE_CROP_FILE_PROVIDER) && data != null) {
            String path = getResultImageData(data);
            if (TextUtils.isEmpty(path)) {
                UICompat.failed(this, "头像获取失败");
                return;
            }
            Log.i("rae", "路径为：" + path);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                path = handleContentUri(Uri.parse(path));
            }

            onAvatarImageChanged(path);
            handleUpload();
        }

    }

    @Nullable
    private String getResultImageData(Intent data) {
        Uri uri = data.getData();
        if (uri != null) return uri.toString();
        if (data.getExtras() == null) return null;
        Object objData = data.getExtras().get("data");
        if (objData instanceof String) return objData.toString();
        if (objData instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) objData;
            File file = new File(getExternalCacheDir(), "avatar-upload-" + System.currentTimeMillis() + ".png");
            OutputStream fileStream = null;
            try {
                fileStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileStream);
                return file.getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fileStream != null) {
                    try {
                        fileStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private String handleContentUri(Uri uri) {
        try {
            ParcelFileDescriptor fileDescriptor = this.getContentResolver().openFileDescriptor(uri, "r");
            if (fileDescriptor == null) return uri.toString();
            File file = new File(getExternalCacheDir(), "avatar-upload-" + System.currentTimeMillis() + ".png");
            OutputStream fileStream = new FileOutputStream(file);
            BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor()).compress(Bitmap.CompressFormat.PNG, 100, fileStream);
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String segment = uri.getLastPathSegment();
        if (segment == null) return uri.getPath();
        File file = new File(getExternalCacheDir(), segment);
        return file.getPath();
    }

    /**
     * 头像路径发生改变
     *
     * @param url
     */
    private void onAvatarImageChanged(String url) {
        mSelectedPath = url;
        AppImageLoader.displayAvatar(url, mAvatarView);
        AppImageLoader.displayAvatar(url, mAvatarLoadingView);
    }

    private void routeToCrop(String url) {
        Uri uri = Uri.fromFile(new File(url)); // 源头像位置
        String fileName = String.valueOf(("cnblogs-face-cropped" + System.currentTimeMillis()).hashCode());
        Uri outputUri = Uri.fromFile(new File(getExternalCacheDir(), fileName)); // 输出头像位置

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uri = resolveUrl(url);
            outputUri = resolveUrl(new File(getExternalCacheDir(), fileName).getPath());
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null) {

            // 授权访问
            this.grantUriPermission(resolveInfo.activityInfo.packageName, outputUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            startActivityForResult(intent, REQUEST_CODE_CROP);
        } else {
            handleUpload();
        }
    }

    /**
     * 解决android 4.4 以上系统，要使用FileProvider来共享文件
     *
     * @param path 文件路径
     * @return
     */
    private Uri resolveUrl(String path) {
        //  保存文件
        File file = new File(path);
        return FileProvider.getUriForFile(this, "share.cnblogs.com", file);
    }

    /**
     * 上传处理
     */
    private void handleUpload() {
        if (!new File(getUploadPath()).exists()) {
            UICompat.failed(this, "无法读取上传的头像文件");
            return;
        }
        showLoading();
        mPresenter.upload();
    }


    private void showLoading() {
        UICompat.fadeIn(mLoadingView);
        mLoadingView.setVisibility(View.VISIBLE);
        mPreviewButton.setVisibility(View.GONE);
        mSelectedButton.setVisibility(View.GONE);
    }

    private void dismissLoading() {
        UICompat.fadeOut(mLoadingView);
        mLoadingView.setVisibility(View.GONE);
        mPreviewButton.setVisibility(View.VISIBLE);
        mSelectedButton.setVisibility(View.VISIBLE);
    }

}
