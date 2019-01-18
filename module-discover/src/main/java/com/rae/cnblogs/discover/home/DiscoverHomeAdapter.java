package com.rae.cnblogs.discover.home;

import android.widget.ImageView;
import android.widget.TextView;

import com.antcode.sdk.model.AntColumnInfo;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.discover.DiscoverItem;
import com.rae.cnblogs.discover.R;

import java.util.List;

public class DiscoverHomeAdapter extends BaseSectionQuickAdapter<DiscoverItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public DiscoverHomeAdapter(int layoutResId, int sectionHeadResId, List<DiscoverItem> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverItem item) {
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

    @Override
    protected void convertHead(BaseViewHolder helper, DiscoverItem item) {
        TextView view = helper.itemView.findViewById(R.id.tv_title);
        view.setText(item.header);
    }
}
