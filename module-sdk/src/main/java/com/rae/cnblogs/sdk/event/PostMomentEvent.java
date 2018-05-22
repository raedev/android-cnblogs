package com.rae.cnblogs.sdk.event;

import com.rae.cnblogs.sdk.model.MomentMetaData;

/**
 * 发布闪存事件
 * Created by ChenRui on 2017/11/3 0003 0:02.
 */
public class PostMomentEvent {
    private boolean isSuccess;
    private String message;
    private int notificationId;
    private MomentMetaData mMomentMetaData;
    private boolean isDeleted;

    public PostMomentEvent() {
    }

    public PostMomentEvent(int notificationId, boolean isSuccess, String message) {
        this.notificationId = notificationId;
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public void setMomentMetaData(MomentMetaData momentMetaData) {
        mMomentMetaData = momentMetaData;
    }

    public MomentMetaData getMomentMetaData() {
        return mMomentMetaData;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
