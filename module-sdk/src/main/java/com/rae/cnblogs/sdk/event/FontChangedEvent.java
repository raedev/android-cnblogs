package com.rae.cnblogs.sdk.event;

/**
 * 字体大小改变
 */
public class FontChangedEvent {
    private int size;

    public FontChangedEvent(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
