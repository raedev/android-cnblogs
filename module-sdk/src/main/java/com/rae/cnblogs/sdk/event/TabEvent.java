package com.rae.cnblogs.sdk.event;

/**
 * 选项卡事件
 * Created by ChenRui on 2017/7/20 0020 0:40.
 */
public class TabEvent {
    private int mPosition;

    public TabEvent(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }
}
