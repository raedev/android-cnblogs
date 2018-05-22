package com.rae.cnblogs.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.adapter.ImageAdapter;
import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.basic.GlideApp;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.middleware.R;
import com.rae.cnblogs.middleware.R2;
import com.rae.cnblogs.theme.ThemeCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片预览
 * Created by ChenRui on 2017/2/6 0006 15:48.
 */
@Route(path = AppRoute.PATH_IMAGE_PREVIEW)
public class ImagePreviewActivity extends BasicActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @BindView(R2.id.vp_image_preview)
    ViewPager mViewPager;

    @BindView(R2.id.tv_image_preview_count)
    TextView mCountView;

    @BindView(R2.id.ll_bottom)
    View mBottomLayout;

    @BindView(R2.id.img_back)
    View mCloseView;

    @BindView(R2.id.view_holder)
    View mNightView;

    @BindView(R2.id.rl_content)
    View mContentView;
    @BindView(R2.id.rl_checkbox)
    View mCheckBoxLayout;
    @BindView(R2.id.cb_checkbox)
    CheckBox mCheckBox;
    @BindView(R2.id.tv_position)
    TextView mPositionView;
    @BindView(R2.id.btn_selected)
    Button mSelectedButton;
    @BindView(R2.id.img_download)
    View mDownLoadView;
    private int mMaxCount = 6; // 最大选择数量

    ImageAdapter mAdapter;
    private ArrayList<String> mSelectedImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.image_scale_in, R.anim.keep_status);
        setContentView(R.layout.activity_image_preview);

        ArrayList<String> images = getIntent().getStringArrayListExtra("images");
        mSelectedImages = getIntent().getStringArrayListExtra("selectedImages");
        mMaxCount = getIntent().getIntExtra("maxCount", mMaxCount);
        mCheckBoxLayout.setVisibility(mSelectedImages == null ? View.GONE : View.VISIBLE);
        mDownLoadView.setVisibility(mSelectedImages == null ? View.VISIBLE : View.GONE);
        mSelectedButton.setVisibility(mCheckBoxLayout.getVisibility());

        if (images == null || images.size() <= 0) {
            finish();
            return;
        }

        mAdapter = new ImageAdapter(this, images);
        mAdapter.setOnItemClickListener(this);
        mViewPager.setAdapter(mAdapter);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox buttonView = (CheckBox) v;
                boolean isChecked = buttonView.isChecked();
                int position = mViewPager.getCurrentItem();
                String url = mAdapter.getItem(position);
                // 不超过6张图片
                if (isChecked && mSelectedImages.size() >= mMaxCount) {
                    buttonView.setChecked(false);
                    UICompat.failed(buttonView.getContext(), "最多选择" + mMaxCount + "张图片");
                    return;
                }

                mSelectedImages.remove(url);
                if (isChecked) {
                    if (!mSelectedImages.contains(url)) {
                        mSelectedImages.add(url);
                    }
                } else {
                    mSelectedImages.remove(url);
                }

                // 重新
                onPageSelected(position);
            }
        });

        int position = getIntent().getIntExtra("position", mViewPager.getCurrentItem());

        if (mViewPager.getAdapter().getCount() > 1) {
            mViewPager.addOnPageChangeListener(this);
        }

        mViewPager.setCurrentItem(position);


        if (position <= 0)
            onPageSelected(position);

        if (ThemeCompat.isNight()) {
            mNightView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.keep_status, R.anim.image_scale_out);
    }

    @OnClick(R2.id.img_back)
    public void onBackClick() {
        finish();
    }

    @OnClick(R2.id.btn_selected)
    public void onSelectedButtonClick() {
        // 选择图片模式返回数据
        if (mSelectedImages != null) {
            Intent data = new Intent();
            data.putStringArrayListExtra("selectedImages", mSelectedImages);
            setResult(RESULT_OK, data);
        }
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCountView.setText(String.format(Locale.getDefault(), "%d/%d", (position + 1), mViewPager.getAdapter().getCount()));

        // 处理选中状态
        if (Rx.isEmpty(mSelectedImages)) return;
        String item = mAdapter.getItem(position);
        if (mSelectedImages.contains(item)) {
            mCheckBox.setChecked(true);
            mPositionView.setText(String.valueOf(mSelectedImages.indexOf(item) + 1));
        } else {
            mCheckBox.setChecked(false);
            mPositionView.setText("");
        }
        mSelectedButton.setText(getContext().getString(R.string.button_text_image_post, mSelectedImages.size(), mMaxCount));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        // 选择图片模式
        if (mSelectedImages != null) return;
        this.finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onDownloadClick();
        }
    }

    @OnClick(R2.id.img_download)
    public void onDownloadClick() {
        // 检查权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }
        int item = mViewPager.getCurrentItem();
        String url = mAdapter.getItem(item);
        UICompat.loading(this, "正在保存图片，请稍候..");
        GlideApp.with(this)
                .downloadOnly()
                .load(url)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<File> target, boolean b) {
                        UICompat.dismiss();
                        UICompat.failed(getContext(), "保存图片失败");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File file, Object o, Target<File> target, DataSource dataSource, boolean b) {
                        Observable.just(file)
                                .subscribeOn(Schedulers.io())
                                .map(new Function<File, File>() {
                                    @Override
                                    public File apply(File file) throws Exception {
                                        //  插入系统图库
                                        MediaStore.Images.Media.insertImage(getContentResolver(), file.getPath(), file.getName(), null);
                                        return copy(file);
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .doFinally(new Action() {
                                    @Override
                                    public void run() {
                                        UICompat.dismiss();
                                        UICompat.toastInCenter(getContext(), "图片保存成功，请前往相册查看");
                                    }
                                })
                                .subscribe(new DefaultObserver<File>() {
                                    @Override
                                    public void onNext(@io.reactivex.annotations.NonNull File file) {
                                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file))); // 通知列表刷新
                                    }

                                    @Override
                                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });


                        return false;
                    }
                })
                .preload();

    }

    /**
     * 复制图片到图片库中去
     *
     * @param file 源图片
     */
    private File copy(File file) {
        FileOutputStream outputStream = null;
        FileInputStream stream = null;
        try {
            File target = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), file.getName());
            outputStream = new FileOutputStream(target);
            stream = new FileInputStream(file);
            int len = 0;
            byte[] buffer = new byte[128];
            while ((len = stream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
