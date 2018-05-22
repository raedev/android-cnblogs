package com.rae.cnblogs.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.rae.cnblogs.sdk.event.FontChangedEvent;
import com.rae.cnblogs.widget.RaeSeekBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * 字体设置
 * Created by ChenRui on 2017/10/12 0012 23:30.
 */
@Route(path = AppRoute.PATH_FONT_SETTING)
public class FontSettingActivity extends SwipeBackBasicActivity {

    @BindView(R.id.tv_message)
    TextView mMessage;

    @BindView(R.id.seekBar)
    RaeSeekBar mSeekBar;

    private boolean mFontChanged;
    private CnblogAppConfig mConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_setting);
        mConfig = CnblogAppConfig.getInstance(this);
        int size = mConfig.getPageTextSize();
        if (size > 0) {
            mSeekBar.setTextSize(size);
            mMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                int size = mSeekBar.getRawTextSize(value);
                mMessage.setTextSize(size);
                mFontChanged = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFontChanged) {
            // 保存设置
            int size = mSeekBar.getTextSize(mSeekBar.getProgress());
            mConfig.setPageTextSize(size);
            // 通知
            EventBus.getDefault().post(new FontChangedEvent(size));
        }
    }
}
