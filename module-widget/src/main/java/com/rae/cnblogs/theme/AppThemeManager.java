package com.rae.cnblogs.theme;

import android.app.Application;

import skin.support.SkinCompatManager;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * 主题管理器
 */
public final class AppThemeManager {

    public static class ThemeEvent {
        private boolean isNight;

        public ThemeEvent(boolean isNight) {
            this.isNight = isNight;
        }

        public boolean isNight() {
            return isNight;
        }

    }

    /**
     * 初始化皮肤
     *
     * @param application 应用程序
     */
    public static void init(Application application) {
        // 加载皮肤
        SkinCompatManager.init(application);
        SkinActivityLifecycleCompat.init(application);
        SkinCompatManager.getInstance()
                .addHookInflater(new ThemeCompat.CnblogsThemeHookInflater())
                .addInflater(new CnblogsLayoutInflater())
                .addInflater(new SkinMaterialViewInflater());

        SkinCompatManager.getInstance().loadSkin();


    }
}
