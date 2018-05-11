package com.rae.cnblogs.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.LinearInterpolator;

/**
 * 倒计时文本
 * Created by ChenRui on 2017/10/31 0031 23:01.
 */
public class CountDownTextView extends RaeTextView {

    // 倒计时
    private int duration = 5000;

    // 扫过的角度
    private int mSweepAngle = 360;
    private ValueAnimator animator;
    private final RectF mRect = new RectF();
    private Paint mBackgroundPaint;

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void init() {
        super.init();
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.WHITE);
        mBackgroundPaint.setStrokeWidth(dp2px(2));
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 开始倒计时
     */
    public void start() {
        // 在动画中
        if (mSweepAngle != 360) return;

        mSweepAngle = 360;
        animator = ValueAnimator.ofInt(mSweepAngle).setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mSweepAngle = value;
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int padding = dp2px(4);
        mRect.top = padding;
        mRect.left = padding;
        mRect.right = canvas.getWidth() - padding;
        mRect.bottom = canvas.getHeight() - padding;

        // 画倒计时线内圆
        canvas.drawArc(mRect, //弧线所使用的矩形区域大小
                -90,  //开始角度
                mSweepAngle, //扫过的角度
                false, //是否使用中心
                mBackgroundPaint);

        super.onDraw(canvas);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void reset() {
        mSweepAngle = 360;
        animator = null;
    }

    public void stop() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }
}
