package com.rae.cnblogs.env;

import com.rae.cnblogs.basic.BasicApplication;
import com.rae.cnblogs.theme.AppThemeManager;

public class CnblogsApplication extends BasicApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // 主题初始化
        AppThemeManager.init(this);
    }
}
