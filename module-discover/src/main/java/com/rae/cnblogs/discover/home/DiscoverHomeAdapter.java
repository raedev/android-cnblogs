package com.rae.cnblogs.discover.home;

import android.widget.TextView;

import com.antcode.sdk.model.AntColumnInfo;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.holder.AntColumnHolder;
import com.rae.cnblogs.discover.holder.DiscoverItem;

public class DiscoverHomeAdapter extends BaseMultiItemQuickAdapter<DiscoverItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public DiscoverHomeAdapter() {
        super(null);
        addItemType(DiscoverItem.TYPE_SESSION, R.layout.item_discover_home_section);
        addItemType(DiscoverItem.TYPE_CONTENT_VERTICAL, R.layout.item_discover_home_column);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverItem item) {

        switch (helper.getItemViewType()) {
            case DiscoverItem.TYPE_SESSION:
                onBindSection(helper, item);
                break;
            case DiscoverItem.TYPE_CONTENT_VERTICAL:
                onBindColumn(helper, item);
                break;
        }

    }

    private void onBindColumn(BaseViewHolder helper, DiscoverItem item) {
        AntColumnInfo data = (AntColumnInfo) item.getData();
        AntColumnHolder holder = new AntColumnHolder(helper);
        holder.bindData(data);
    }

    private void onBindSection(BaseViewHolder helper, DiscoverItem item) {
        TextView textView = helper.itemView.findViewById(R.id.tv_title);
        textView.setText(item.header);
    }
}
