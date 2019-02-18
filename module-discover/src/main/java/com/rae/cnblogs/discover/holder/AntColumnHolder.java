package com.rae.cnblogs.discover.holder;

import android.widget.ImageView;
import android.widget.TextView;

import com.antcode.sdk.model.AntColumnInfo;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AntColumnHolder {

    public static int TYPE_NORMAL = 0;
    public static int TYPE_MINE = 1;

    @BindView(R2.id.img_logo)
    ImageView logoView;
    @BindView(R2.id.tv_title)
    TextView titleView;
    @BindView(R2.id.tv_article_num)
    TextView numberView;
    @BindView(R2.id.tv_author)
    TextView authorView;
    @BindView(R2.id.tv_sub_num)
    TextView subNumView;
    @BindView(R2.id.tv_recommendation)
    TextView recommendationView;

    private int itemType;


    public AntColumnHolder(BaseViewHolder holder) {
        this(holder, TYPE_NORMAL);
    }

    public AntColumnHolder(BaseViewHolder holder, int itemType) {
        ButterKnife.bind(this, holder.itemView);
        this.itemType = itemType;
    }

    public void bindData(AntColumnInfo data) {
        if (itemType == TYPE_MINE) {
            AppImageLoader.display(data.getAvatar(), logoView);
        } else {
            AppImageLoader.display(data.getLogo(), logoView);
        }
        titleView.setText(data.getTitle());
        numberView.setText(numberView.getResources().getString(R.string.article_num_format, data.getArticleNum()));
        authorView.setText(String.format("%s %s", data.getAntAuthor().getNickname(), data.getAntAuthor().getTitle()));
        recommendationView.setText(data.getRecommendation());
        subNumView.setText(numberView.getResources().getString(R.string.sub_num_format, data.getSubnum()));
    }
}
