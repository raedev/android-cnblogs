package com.rae.cnblogs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * 带文本的滑杆
 * Created by ChenRui on 2017/10/13 0013 12:50.
 */
public class RaeSeekBar extends AppCompatSeekBar {

    private String[] mTickMarkTitles = new String[]{
            "A",
            "标准",
            "",
            "",
            "A"
    };
    private int[] mTextSize = new int[]{
            16,
            18,
            24,
            26,
            28
    };

    private final Paint mTickMarkTitlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private float mTickMarkTitleTextSize = 18;
    private float mOffsetY = 40;
    private int mLineHeight = 10; // 刻度高度
    private int mThumbWidth;
    private final Rect mRect = new Rect();
    private int mThumbHeight;

    public RaeSeekBar(Context context) {
        super(context);
        init();
    }

    public RaeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RaeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        mTickMarkTitleTextSize = getSize(mTickMarkTitleTextSize);
        mOffsetY = getSize(mOffsetY);
        mLineHeight = getSize(mLineHeight);
        mTickMarkTitlePaint.setTextAlign(Paint.Align.CENTER);
        mTickMarkTitlePaint.setColor(ContextCompat.getColor(getContext(), R.color.ph1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int max = getMax();
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int h2 = height / 2;

//        final int saveCount = canvas.save();
//        canvas.translate(getPaddingLeft() - getThumbOffset(), getPaddingTop() + h2 - mThumbHeight / 2);
//        getThumb().draw(canvas);
//        canvas.restoreToCount(saveCount);

        // 画刻度背景
        mRect.left = getPaddingLeft();
        mRect.right = width - getPaddingRight();
        mRect.top = h2 - getSize(1);
        mRect.bottom = mRect.top + getSize(1.5f);
        canvas.drawRect(mRect, mTickMarkTitlePaint);
        int cw = mRect.right - mRect.left; // 总画线的长度 = 右边坐标 - 左边坐标
        for (int i = 0; i <= max; i++) {
            // 每个间隔的大小
            int thumbPos = getPaddingLeft() + (cw * i / max);
            // 画分割线
            mRect.top = h2 - mLineHeight / 2;
            mRect.bottom = h2 + mLineHeight / 2;
            mRect.left = thumbPos;
            mRect.right = thumbPos + getSize(1.5f);
            canvas.drawRect(mRect, mTickMarkTitlePaint);

            // 画刻度文本
            String title = mTickMarkTitles[i % mTickMarkTitles.length];
            mTickMarkTitlePaint.getTextBounds(title, 0, title.length(), mRect);
            mTickMarkTitlePaint.setTextSize(getSize(mTextSize[i]));
            canvas.drawText(title, thumbPos, getSize(mTextSize[mTextSize.length - 1]), mTickMarkTitlePaint);
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mThumbWidth = getThumb().getIntrinsicWidth();
        mThumbHeight = getThumb().getIntrinsicHeight();
        // 加上字体大小
        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        h += getSize(mTextSize[mTextSize.length - 1]); // 最大的字体
        h += mOffsetY;
        // 保存
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(w, wm), MeasureSpec.makeMeasureSpec(h, hm));

    }

    protected int getSize(float size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }

    public int getRawTextSize(int progress) {
        return mTextSize[progress % mTextSize.length];
    }

    public int getTextSize(int progress) {
        return getSize(getRawTextSize(progress));
    }

    public void setTextSize(int size) {
        for (int i = 0; i < mTextSize.length; i++) {
            int textSize = getSize(mTextSize[i]);
            if (textSize == size) {
                setProgress(i);
                break;
            }
        }
    }

}
