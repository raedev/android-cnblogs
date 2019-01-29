package com.rae.cnblogs.discover.home;

import android.widget.ImageView;
import android.widget.TextView;

import com.antcode.sdk.model.AntColumnInfo;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.discover.holder.DiscoverItem;
import com.rae.cnblogs.discover.R;

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
        ImageView logoView = helper.itemView.findViewById(R.id.img_logo);
        TextView titleView = helper.itemView.findViewById(R.id.tv_title);
        TextView numberView = helper.itemView.findViewById(R.id.tv_article_num);
        TextView authorView = helper.itemView.findViewById(R.id.tv_author);
        TextView subNumView = helper.itemView.findViewById(R.id.tv_sub_num);
        TextView recommendationView = helper.itemView.findViewById(R.id.tv_recommendation);
        AppImageLoader.display(data.getLogo(), logoView);
        titleView.setText(data.getTitle());
        numberView.setText(numberView.getResources().getString(R.string.article_num_format, data.getArticleNum()));
        authorView.setText(String.format("%s %s", data.getAntAuthor().getNickname(), data.getAntAuthor().getTitle()));
        recommendationView.setText(data.getRecommendation());
        subNumView.setText(numberView.getResources().getString(R.string.sub_num_format, data.getSubnum()));
    }

    private void onBindSection(BaseViewHolder helper, DiscoverItem item) {
        TextView textView = helper.itemView.findViewById(R.id.tv_title);
        textView.setText(item.header);
    }
}
