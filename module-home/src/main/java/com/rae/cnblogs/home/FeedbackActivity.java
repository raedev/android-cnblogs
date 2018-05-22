package com.rae.cnblogs.home;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVPersistenceUtils;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.feedback.Comment;
import com.avos.avoscloud.feedback.Comment.CommentType;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.avos.avoscloud.feedback.FeedbackThread.SyncCallback;
import com.avos.avoscloud.feedback.Resources;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import skin.support.content.res.SkinCompatResources;

@Route(path = AppRoute.PATH_FEEDBACK)
public class FeedbackActivity extends SwipeBackBasicActivity {

    FeedbackAgent agent;
    ListView feedbackListView;
    Button sendButton;
    EditText feedbackInput;
    EditText contact;
    FeedbackThread thread;
    FeedbackListAdapter adapter;
    SyncCallback syncCallback;
    ImageView imageButton;
    AtomicBoolean animating = new AtomicBoolean(false);
    private static final int IMAGE_REQUEST = 6543;

    public static final ImageCache cache = new ImageCache(AVOSCloud.applicationContext);
    private Disposable mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Resources.layout.avoscloud_feedback_activity_conversation(this));
        agent = new FeedbackAgent(this);
        adapter = new FeedbackListAdapter(this);
        thread = agent.getDefaultThread();
        feedbackListView = (ListView) findViewById(Resources.id.avoscloud_feedback_thread_list(this));
        feedbackListView.setAdapter(adapter);
        sendButton = (Button) findViewById(Resources.id.avoscloud_feedback_send(this));
        imageButton = (ImageView) findViewById(Resources.id.avoscloud_feedback_add_image(this));
        feedbackInput = (EditText) findViewById(Resources.id.avoscloud_feedback_input(this));
        syncCallback = new SyncCallback() {

            @Override
            public void onCommentsSend(List<Comment> comments, AVException e) {
                LogUtil.avlog.d("send new comments");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCommentsFetch(List<Comment> comments, AVException e) {
                LogUtil.avlog.d("fetch new comments");
                adapter.notifyDataSetChanged();
            }

        };


        sendButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendButton.setOnClickListener(null);
                String feedbackText = feedbackInput.getText().toString();
                feedbackInput.setText("");
                if (!AVUtils.isBlankString(feedbackText)) {
                    thread.add(new Comment(feedbackText));
                    adapter.notifyDataSetChanged();
                    feedbackListView.setSelection(feedbackListView.getAdapter().getCount());
                    smoothScrollToBottom();
                    thread.sync(syncCallback);
                }
                sendButton.setOnClickListener(this);
            }
        });

        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 19) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(
                                    intent,
                                    getResources().getString(
                                            Resources.string.avoscloud_feedback_select_image(FeedbackActivity.this))),
                            IMAGE_REQUEST);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_REQUEST);
                }

            }
        });

        feedbackInput.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    feedbackListView.setSelection(feedbackListView.getAdapter().getCount());
                    smoothScrollToBottom();
                }
            }
        });

        feedbackInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (AVUtils.isBlankString(s.toString()) && !animating.get()
                        && imageButton.getVisibility() == View.INVISIBLE) {
                    Animation a1 = new TranslateAnimation(0, 0, 0, 100);
                    Animation a2 = new AlphaAnimation(1, 0.2f);
                    AnimationSet set = new AnimationSet(true);
                    set.setInterpolator(FeedbackActivity.this, android.R.interpolator.decelerate_cubic);
                    set.addAnimation(a1);
                    set.addAnimation(a2);
                    set.setDuration(300);
                    set.setRepeatMode(Animation.REVERSE);
                    set.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            animating.set(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animating.set(false);
                            sendButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    sendButton.startAnimation(set);


                    imageButton.setVisibility(View.VISIBLE);
                    Animation a3 = new TranslateAnimation(0, 0, -50, 0);
                    Animation a4 = new AlphaAnimation(0.2f, 1);
                    AnimationSet set2 = new AnimationSet(true);
                    set2.setInterpolator(FeedbackActivity.this, android.R.interpolator.accelerate_decelerate);
                    set2.addAnimation(a3);
                    set2.addAnimation(a4);
                    set2.setDuration(250);
                    set2.setRepeatMode(Animation.REVERSE);
                    set2.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            imageButton.setVisibility(View.VISIBLE);
                        }
                    });
                    imageButton.startAnimation(set2);
                } else if (!AVUtils.isBlankString(s.toString()) && !animating.get()
                        && sendButton.getVisibility() == View.INVISIBLE) {

                    Animation a1 = new TranslateAnimation(0, 0, 0, -100);
                    Animation a2 = new AlphaAnimation(1, 0.2f);
                    AnimationSet set = new AnimationSet(true);
                    set.setInterpolator(FeedbackActivity.this, android.R.interpolator.decelerate_cubic);
                    set.addAnimation(a1);
                    set.addAnimation(a2);
                    set.setDuration(300);
                    set.setRepeatMode(Animation.REVERSE);
                    set.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            animating.set(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            imageButton.setVisibility(View.INVISIBLE);
                            animating.set(false);
                        }
                    });
                    imageButton.startAnimation(set);


                    sendButton.setVisibility(View.VISIBLE);
                    Animation a3 = new TranslateAnimation(0, 0, 50, 0);
                    Animation a4 = new AlphaAnimation(0.2f, 1);
                    AnimationSet set2 = new AnimationSet(true);

                    set2.setInterpolator(FeedbackActivity.this, android.R.interpolator.accelerate_decelerate);
                    set2.addAnimation(a3);
                    set2.addAnimation(a4);
                    set2.setDuration(250);
                    set2.setRepeatMode(Animation.REVERSE);
                    set2.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            sendButton.setVisibility(View.VISIBLE);
                        }
                    });
                    sendButton.startAnimation(set2);

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        findViewById(R.id.avoscloud_feedback_btn_contact).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (contact.getVisibility() != View.VISIBLE) {
//                    contact.startAnimation(AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in));
//                    contact.setVisibility(View.VISIBLE);
//                    contact.setSelection(contact.length());
//                    contact.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(contact, 0);
//                } else {
//                    contact.startAnimation(AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_out));
//                    contact.setVisibility(View.GONE);
//                }
//            }
//        });

        contact = (EditText) findViewById(Resources.id.avoscloud_feedback_contact(this));

        contact.clearFocus();
        feedbackInput.requestFocus();

        if (agent.isContactEnabled()) {

            // 设置默认的联系方式
            if (TextUtils.isEmpty(thread.getContact()) && UserProvider.getInstance().isLogin()) {
                UserInfoBean info = UserProvider.getInstance().getLoginUserInfo();
                thread.setContact(info.getBlogApp() + "@" + info.getDisplayName());
            }
//            contact.setVisibility(TextUtils.isEmpty(thread.getContact()) ? View.VISIBLE : View.GONE);
            contact.setText(thread.getContact());
            contact.setSelection(contact.length());
            contact.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s) && UserProvider.getInstance().isLogin()) {
                        s = UserProvider.getInstance().getLoginUserInfo().getBlogApp() + "@" + UserProvider.getInstance().getLoginUserInfo().getDisplayName();
                    }

                    thread.setContact(s.toString());
                }

            });
        } else {
            contact.setVisibility(View.GONE);
        }

        thread.sync(syncCallback);

        // 定时刷新
        mSubscribe = Observable.interval(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        thread.sync(syncCallback);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mSubscribe != null) {
            mSubscribe.dispose();
            mSubscribe = null;
        }
        super.onDestroy();

    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri =
                        ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                                Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other
     * file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (IMAGE_REQUEST == requestCode && resultCode == RESULT_OK && data.getData() != null) {
            Uri uri = data.getData();
            String filePath = FeedbackActivity.getPath(this, uri);

            try {
                LogUtil.avlog.d("img picked:" + filePath);
                File attachmentFile = new File(filePath);
                thread.add(new Comment(attachmentFile));
                adapter.notifyDataSetChanged();
                feedbackListView.setSelection(feedbackListView.getAdapter().getCount());
                smoothScrollToBottom();
                thread.sync(syncCallback);
                feedbackInput.setText("");
            } catch (AVException e) {
                e.printStackTrace();
            }
        }
    }

    private void smoothScrollToBottom() {
        feedbackListView.post(new Runnable() {

            @Override
            public void run() {
                feedbackListView.smoothScrollToPosition(feedbackListView.getAdapter().getCount());
            }
        });
    }

    public class FeedbackListAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;

        public FeedbackListAdapter(Context context) {
            this.mContext = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return thread.getCommentsList().size();
        }

        @Override
        public Object getItem(int position) {
            return thread.getCommentsList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                if (this.getItemViewType(position) == 0) {
                    convertView =
                            inflater.inflate(Resources.layout.avoscloud_feedback_user_reply(FeedbackActivity.this),
                                    null);
                } else {
                    convertView =
                            inflater.inflate(Resources.layout.avoscloud_feedback_dev_reply(FeedbackActivity.this),
                                    null);
                }
                holder = new ViewHolder();
                holder.content =
                        convertView.findViewById(Resources.id
                                .avoscloud_feedback_content(FeedbackActivity.this));
                holder.timestamp =
                        convertView.findViewById(Resources.id
                                .avoscloud_feedback_timestamp(FeedbackActivity.this));
                holder.image =
                        convertView.findViewById(Resources.id
                                .avoscloud_feedback_image(FeedbackActivity.this));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.content.setTextColor(SkinCompatResources.getInstance().getColor(R.color.ph2));

            final Comment comment = (Comment) getItem(position);
            if (comment.getAttachment() != null && comment.getAttachment().getUrl() != null) {
                holder.content.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
                final OnClickListener imageOnClickListener = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // fix bugly #432 #422 #412 #382
                        File file = ImageCache.getCacheFile(comment.getAttachment().getUrl());
                        if (!file.exists()) {
                            UICompat.failed(getContext(), "图片不存在！");
                            return;
                        }

                        AppRoute.routeToImagePreview(getContext(), file.getPath());
                    }
                };
                Bitmap attachmentCache = cache.getImage(comment.getAttachment().getUrl());
                if (attachmentCache != null) {
                    holder.image.setImageBitmap(attachmentCache);
                    holder.image.setOnClickListener(imageOnClickListener);
                } else {
                    holder.image.setOnClickListener(null);
                    comment.getAttachment().getDataInBackground(new GetDataCallback() {

                        @Override
                        public void done(byte[] data, AVException e) {
                            if (e == null) {
                                Bitmap attachmentCache = cache.setImage(comment.getAttachment().getUrl(), data);
                                holder.image.setImageBitmap(attachmentCache);
                                holder.image.setOnClickListener(imageOnClickListener);
                            }
                        }
                    });
                }
            } else {
                holder.content.setVisibility(View.VISIBLE);
                holder.content.setText(comment.getContent());
                holder.image.setVisibility(View.GONE);
            }
            if (Math.abs(comment.getCreatedAt().getTime() - System.currentTimeMillis()) < 10000) {
                holder.timestamp.setText(getResources().getString(
                        Resources.string.avoscloud_feedback_just_now(FeedbackActivity.this)));
            } else {
                holder.timestamp.setText(DateUtils.getRelativeTimeSpanString(comment.getCreatedAt()
                        .getTime(), System.currentTimeMillis() - 1, 0l, DateUtils.FORMAT_ABBREV_ALL));
            }
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            Comment comment = (Comment) this.getItem(position);
            if (comment.getCommentType().equals(CommentType.USER)) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public class ViewHolder {
        TextView content;
        TextView timestamp;
        ImageView image;
    }

    public static class ImageCache {
        LruCache<String, Bitmap> bitmapCache;
        static final int cacheSize = 20;
        Context context;

        public ImageCache(Context context) {
            this.context = context;
            bitmapCache = new LruCache<String, Bitmap>(cacheSize);
        }

        static String getFileName(String fileUrl) {
            Uri uri = Uri.parse(fileUrl);
            return uri.getLastPathSegment();
        }


        public static File getCacheFile(String fileName) {
            File imgCacheDir = new File(AVOSCloud.applicationContext.getExternalCacheDir(), "img");
            if (!imgCacheDir.exists()) {
                imgCacheDir.mkdirs();
            }

            return new File(imgCacheDir, getFileName(fileName));
        }

        static File getCacheThumbnailFile(String fileName) {
            File imgCacheDir = new File(AVOSCloud.applicationContext.getExternalCacheDir(), "img");
            if (!imgCacheDir.exists()) {
                imgCacheDir.mkdirs();
            }
            return new File(imgCacheDir, getFileName(fileName) + ".tn");
        }

        public Bitmap getImage(String key) {
            Bitmap cacheBitmap = bitmapCache.get(key);
            if (cacheBitmap == null) {
                cacheBitmap = BitmapFactory.decodeFile(getCacheThumbnailFile(key).getAbsolutePath());
            }
            return cacheBitmap;
        }

        public Bitmap setImage(String key, byte[] data) {
            OutputStream os = null;
            OutputStream thumbnailOS = null;
            byte[] imageData = null;
            Bitmap thumbnail = null;
            try {
                Bitmap tempBM =
                        Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(data, 0, data.length), 150,
                                150, false);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                tempBM.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageData = baos.toByteArray();

                os = new FileOutputStream(getCacheFile(key), true);
                os.write(data);
                thumbnailOS = new FileOutputStream(getCacheThumbnailFile(key), true);
                thumbnailOS.write(imageData);

                thumbnail = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                AVPersistenceUtils.closeQuietly(os);
                AVPersistenceUtils.closeQuietly(thumbnailOS);
            }
            return thumbnail;
        }
    }

}

