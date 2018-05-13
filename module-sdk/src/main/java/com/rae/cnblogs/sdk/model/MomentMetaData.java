package com.rae.cnblogs.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 闪存的上传参数
 * Created by ChenRui on 2017/10/31 0031 14:38.
 */
public class MomentMetaData implements Parcelable {

    public String content;

    public List<ImageMetaData> images;

    // 公开私有标志
    public int flag = 1;

    public MomentMetaData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeTypedList(this.images);
    }

    protected MomentMetaData(Parcel in) {
        this.content = in.readString();
        this.images = in.createTypedArrayList(ImageMetaData.CREATOR);
    }

    public static final Creator<MomentMetaData> CREATOR = new Creator<MomentMetaData>() {
        @Override
        public MomentMetaData createFromParcel(Parcel source) {
            return new MomentMetaData(source);
        }

        @Override
        public MomentMetaData[] newArray(int size) {
            return new MomentMetaData[size];
        }
    };
}
