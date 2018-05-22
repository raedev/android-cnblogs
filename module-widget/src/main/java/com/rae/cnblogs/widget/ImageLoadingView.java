package com.rae.cnblogs.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * 带loading状态的Image View
 * Created by ChenRui on 2017/1/26 0026 11:36.
 */
public class ImageLoadingView extends AppCompatImageView {

    private Paint[] mPaints = new Paint[2];
    private int[] mRadius = new int[2];
    private ValueAnimator[] mAnimators = new ValueAnimator[2];
    private int mDuration = 800;
    private ValueAnimator mLoadingValueAnimator;
    private int mLoadingRadius;

    public ImageLoadingView(@NonNull Context context) {
        super(context);
        init();
    }

    public ImageLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        mPaints[0] = new Paint();
        mPaints[0].setAntiAlias(true);
        mPaints[0].setColor(Color.parseColor("#FFC63F"));

        mPaints[1] = new Paint();
        mPaints[1].setAntiAlias(true);
        mPaints[1].setColor(Color.WHITE);
    }

    public void loading() {
        stopLoading();

        mLoadingValueAnimator = ValueAnimator.ofInt(10, getMeasuredHeight() / 3)
                .setDuration(mDuration);
        mLoadingValueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        mLoadingValueAnimator.setRepeatCount(3);
        mLoadingValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mLoadingValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLoadingRadius = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mLoadingValueAnimator.start();
    }

    public void anim() {
        anim(null);
    }

    /**
     * 动画小时
     *
     * @param callback 动画运行完成后回调
     */
    public void anim(final Runnable callback) {
        stopLoading();
        final int r = getMeasuredHeight() / 3;
        if (mAnimators[0] == null) {
            mAnimators[0] = ValueAnimator.ofInt(0, r).setDuration(mDuration);
            mAnimators[0].setInterpolator(new OvershootInterpolator());
            mAnimators[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRadius[0] = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
        }
        if (mAnimators[1] == null) {
            mAnimators[1] = ValueAnimator.ofInt(10, r).setDuration(mDuration);
            mAnimators[1].setStartDelay(200);
            mAnimators[1].setInterpolator(new OvershootInterpolator());
            mAnimators[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRadius[1] = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimators[1].addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (callback != null)
                        callback.run();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        if (isRunning()) return;

        mAnimators[0].start();
        mAnimators[1].start();
    }

    private boolean isRunning() {
        return mAnimators[0] != null && mAnimators[0].isRunning();
    }

    protected boolean isLoading() {
        return mLoadingValueAnimator != null && mLoadingValueAnimator.isRunning();
    }

    protected void stopLoading() {
        if (mLoadingValueAnimator != null) {
            mLoadingValueAnimator.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int r = canvas.getHeight() / 2; // 半径
        if (isRunning()) {
            canvas.drawCircle(r, r, mRadius[0], mPaints[0]);
            canvas.drawCircle(r, r, mRadius[1], mPaints[1]);
            canvas.save();
            float scale = Math.min(1, (mRadius[1] * 3 * 0.1f) / (canvas.getHeight() * 0.1f));
            if (scale >= 0.9) scale = 1;
            canvas.scale(scale, scale, r, r);
//            Log.i("rae", "s = " + scale);
        }
        if (isLoading()) {
            canvas.save();
            canvas.drawCircle(r, r, mLoadingRadius, mPaints[0]);
            return;
        }
        super.onDraw(canvas);


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void stop() {
        stopLoading();
        if (mAnimators[0] != null)
            mAnimators[0].cancel();
        if (mAnimators[1] != null)
            mAnimators[1].cancel();
    }

}
