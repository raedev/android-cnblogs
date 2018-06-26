package com.rae.cnblogs.discover;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.BasicFragment;

/**
 * 发现
 * Created by ChenRui on 2018/6/13 10:22.
 */
@Route(path = AppRoute.PATH_FRAGMENT_DISCOVER)
public class DiscoverFragment extends BasicFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fm_discover;
    }

}
