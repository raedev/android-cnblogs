package com.rae.cnblogs.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.basic.GlideApp;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.middleware.R;
import com.rae.cnblogs.middleware.R2;
import com.rae.cnblogs.widget.AppLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 图片选择
 * Created by ChenRui on 2017/10/27 0027 14:04.
 */
@Route(path = AppRoute.PATH_IMAGE_SELECTION)
public class ImageSelectionActivity extends BasicActivity {
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R2.id.recycler_view_selected)
    RecyclerView mSelectedRecyclerView;
    @BindView(R2.id.ptr_content)
    AppLayout mPtrContentView;
    @BindView(R2.id.tv_post)
    TextView mPostView;
    private ImageSelectionAdapter mAdapter;
    private ImageSelectedAdapter mSelectedAdapter;
    private final int mMaxCount = 6; // 最大选择数量
    private ArrayList<String> mSelectedImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);
        mSelectedImages = getIntent().getStringArrayListExtra("selectedImages");
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSelectedRecyclerView.setLayoutManager(lm);
        mAdapter = new ImageSelectionAdapter();
        mAdapter.setSelectedImages(mSelectedImages);
        mAdapter.setMaxCount(mMaxCount);
        mRecyclerView.setAdapter(mAdapter);
        mSelectedAdapter = new ImageSelectedAdapter();
        mSelectedRecyclerView.setAdapter(mSelectedAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            private int sourceCount;

            @Override
            public void onChanged() {
                super.onChanged();
                mSelectedAdapter.setDataList(mAdapter.getSelectedList());
                mSelectedAdapter.notifyDataSetChanged();
                if (mSelectedAdapter.getItemCount() > 0) {
                    mPostView.setEnabled(true);
                    mPostView.setText(getString(R.string.button_text_image_post, mSelectedAdapter.getItemCount(), mMaxCount));
                    // 动画显示
                    if (sourceCount == 0) {
                        mSelectedRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom));
                    }

                } else {
                    mPostView.setEnabled(false);
                    mPostView.setText(getString(R.string.button_text_image_post_default));
                }

                sourceCount = mSelectedAdapter.getItemCount();
            }
        });
        mPtrContentView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                start();
            }
        });
        mSelectedAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = (int) v.getTag();
                mRecyclerView.smoothScrollToPosition(position);
                return true;
            }
        });
        mSelectedAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                ArrayList<String> images = (ArrayList<String>) mSelectedAdapter.getDataList();
                AppRoute.routeToImagePreview((Activity) v.getContext(), images, position, images, mMaxCount);
            }
        });
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                ArrayList<String> images = (ArrayList<String>) mAdapter.getDataList();
                ArrayList<String> selectedImages = (ArrayList<String>) mAdapter.getSelectedList();
                AppRoute.routeToImagePreview((Activity) v.getContext(), images, position, selectedImages, mMaxCount);
            }
        });
        start();
    }

    @OnClick(R2.id.tv_post)
    public void onPostClick() {
        Intent data = new Intent();
        data.putStringArrayListExtra("selectedImages", (ArrayList<String>) mAdapter.getSelectedList());
        setResult(RESULT_OK, data);
        finish();
    }


    private void start() {
        // 先检查权限
        if (requestPermissions()) {
            loadImageData();
        } else {
            mPtrContentView.refreshComplete();
        }
    }

    /**
     * 申请权限
     */
    private boolean requestPermissions() {
        // 检查权限
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            UICompat.toast(this, "请允许访问存储卡权限");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
            }
            return false;
        }

        return true;
    }

    private boolean checkPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 允许权限，重新加载
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadImageData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppRoute.REQ_CODE_IMAGE_SELECTED && data != null) {
            ArrayList<String> selectedImages = data.getStringArrayListExtra("selectedImages");
            mAdapter.setSelectedImages(selectedImages);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载系统相册的图片
     */
    private void loadImageData() {
        List<String> result = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentResolver contentResolver = this.getContentResolver();
        // 按时间倒序排列，取2000条记录
        Cursor cursor = contentResolver.query(uri, null, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC limit 0,2000");
        // 没有图片
        if (cursor == null || cursor.getCount() <= 0) {
            mPtrContentView.refreshComplete();
            return;
        }
        while (cursor.moveToNext()) {
            int index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // 文件地址
            File file = new File(path);
            if (file.exists()) {
                result.add(path);
            }
        }
        cursor.close();

        mAdapter.setImageList(result);
        mAdapter.notifyDataSetChanged();
        mPtrContentView.refreshComplete();
    }


    private static class ImageSelectionHolder extends RecyclerView.ViewHolder {
        TextView mPositionTextView;
        ImageView mImageView;
        CheckBox mCheckBox;
        View mCheckBoxLayout;

        public ImageSelectionHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.img_background);
            mCheckBox = itemView.findViewById(R.id.cb_checkbox);
            mCheckBoxLayout = itemView.findViewById(R.id.rl_checkbox);
            mPositionTextView = itemView.findViewById(R.id.tv_position);
        }
    }

    private static class ImageSelectionAdapter extends RecyclerView.Adapter<ImageSelectionHolder> implements View.OnClickListener {

        private final List<String> mUrls = new ArrayList<>();
        private List<String> mSelectedList = new ArrayList<>();
        private LayoutInflater mLayoutInflater;
        private int mMaxCount;
        private View.OnClickListener mOnClickListener;

        public ImageSelectionAdapter() {
            super();
        }

        @Override
        public ImageSelectionHolder onCreateViewHolder(ViewGroup parent, int i) {
            if (mLayoutInflater == null) {
                mLayoutInflater = LayoutInflater.from(parent.getContext());
            }
            return new ImageSelectionHolder(mLayoutInflater.inflate(R.layout.item_image_selection, parent, false));
        }

        @Override
        public void onBindViewHolder(ImageSelectionHolder holder, int position) {
            String fileName = mUrls.get(position);

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClickListener);
            holder.mCheckBox.setTag(position);
            holder.mCheckBox.setOnClickListener(this);
            holder.mCheckBoxLayout.setOnClickListener(this);

            holder.mCheckBox.setChecked(mSelectedList.contains(fileName));
            if (holder.mCheckBox.isChecked()) {
                holder.mPositionTextView.setVisibility(View.VISIBLE);
                holder.mPositionTextView.setText(String.valueOf(mSelectedList.indexOf(fileName) + 1));
            } else {
                holder.mPositionTextView.setVisibility(View.GONE);
            }
            GlideApp.with(holder.itemView.getContext()).load("file://" + fileName).into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mUrls.size();
        }

        public void remove(String fileName) {
            mUrls.remove(fileName);
        }

        public void setImageList(List<String> imageList) {
            mUrls.clear();
            mUrls.addAll(imageList);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rl_checkbox) {
                v.findViewById(R.id.cb_checkbox).performClick();
            }
            if (v.getId() == R.id.cb_checkbox) {
                onCheckBoxClick((CompoundButton) v);
            }
        }

        private void onCheckBoxClick(CompoundButton buttonView) {
            int position = (int) buttonView.getTag();
            String item = mUrls.get(position);
            if (buttonView.isChecked()) {
                // 不超过6张图片
                if (mSelectedList.size() >= mMaxCount) {
                    buttonView.setChecked(false);
                    UICompat.failed(buttonView.getContext(), "最多选择" + mMaxCount + "张图片");
                    return;
                }
                if (!mSelectedList.contains(item)) {
                    mSelectedList.add(item);
                }
            } else {
                mSelectedList.remove(item);
            }

            notifyDataSetChanged();
        }

        public List<String> getSelectedList() {
            return mSelectedList;
        }

        public void setMaxCount(int maxCount) {
            mMaxCount = maxCount;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        public List<String> getDataList() {
            return mUrls;
        }

        public void setSelectedImages(ArrayList<String> selectedImages) {
            mSelectedList = selectedImages;
        }
    }

    private static class ImageSelectedHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ImageSelectedHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.img_selected);
        }
    }

    private static class ImageSelectedAdapter extends RecyclerView.Adapter<ImageSelectedHolder> {

        private LayoutInflater mLayoutInflater;
        private List<String> mDataList;
        private View.OnLongClickListener mOnLongClickListener;
        private View.OnClickListener mOnClickListener;

        public void setDataList(List<String> dataList) {
            mDataList = dataList;
        }

        @Override
        public ImageSelectedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mLayoutInflater == null) {
                mLayoutInflater = LayoutInflater.from(parent.getContext());
            }
            return new ImageSelectedHolder(mLayoutInflater.inflate(R.layout.item_image_selected, parent, false));
        }

        @Override
        public void onBindViewHolder(ImageSelectedHolder holder, int position) {
            String item = mDataList.get(position);
            holder.itemView.setTag(position);
            holder.itemView.setOnLongClickListener(mOnLongClickListener);
            holder.itemView.setOnClickListener(mOnClickListener);
            GlideApp.with(holder.itemView.getContext()).load("file://" + item).into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return Rx.getCount(mDataList);
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            mOnLongClickListener = listener;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        public String getDataItem(int position) {
            return mDataList.get(position);
        }

        public List<String> getDataList() {
            return mDataList;
        }
    }
}
