package com.rae.cnblogs.sdk.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片参数
 * Created by ChenRui on 2017/10/31 0031 14:39.
 */
public class ImageMetaData implements Parcelable {
    // 本地路径
    public String localPath;
    // 远程路径
    public String remoteUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.localPath);
        dest.writeString(this.remoteUrl);
    }

    public ImageMetaData() {
    }

    protected ImageMetaData(Parcel in) {
        this.localPath = in.readString();
        this.remoteUrl = in.readString();
    }

    public static final Parcelable.Creator<ImageMetaData> CREATOR = new Parcelable.Creator<ImageMetaData>() {
        @Override
        public ImageMetaData createFromParcel(Parcel source) {
            return new ImageMetaData(source);
        }

        @Override
        public ImageMetaData[] newArray(int size) {
            return new ImageMetaData[size];
        }
    };
}
