package com.wmx.android.wrstar.mvp.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.Author;
import com.wmx.android.wrstar.entities.Live;
import com.wmx.android.wrstar.mvp.presenters.LivingPagePresenter;
import com.wmx.android.wrstar.views.widgets.CirImageView;
import com.wmx.android.wrstar.views.widgets.RatingBar;

/**
 * Created by Administrator on 2016/6/21.
 */
public class LiveFragmentHolder extends BaseViewHolder<Live> {
    LivingPagePresenter presenter;

    TextView tvTitle;
    TextView tvOpentiem;
    TextView tvBookNum;
    ImageView ivPicture;
    CirImageView ivAvator;
    TextView tvAuthorName;
    RatingBar ratingbar;

    TextView[] tvs;

    Button btnBook, btnState;

    ImageView iv_type;
    TextView type;


    public LiveFragmentHolder(ViewGroup parent, LivingPagePresenter presenter) {
        super(parent, R.layout.item_live);
        this.presenter = presenter;
        tvBookNum = (TextView) itemView.findViewById(R.id.tv_book_num);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvOpentiem = (TextView) itemView.findViewById(R.id.tv_opentiem);
        tvAuthorName = (TextView) itemView.findViewById(R.id.tv_author_name);
        ivPicture = (ImageView) itemView.findViewById(R.id.iv_picture);
        ivAvator = (CirImageView) itemView.findViewById(R.id.iv_avator);
        ratingbar = (RatingBar) itemView.findViewById(R.id.ratingbar);


        iv_type = (ImageView) itemView.findViewById(R.id.iv_type);
        type = (TextView) itemView.findViewById(R.id.type);

        tvs = new TextView[3];
        tvs[0] = (TextView) itemView.findViewById(R.id.tv1);
        tvs[1] = (TextView) itemView.findViewById(R.id.tv2);
        tvs[2] = (TextView) itemView.findViewById(R.id.tv3);

        btnState = (Button) itemView.findViewById(R.id.btn_state);
        btnBook = (Button) itemView.findViewById(R.id.btn_book);

    }

    @Override
    public void setData(final Live live) {


        Author author = live.author;

        /**
         *  直播状态标志
         */
        if (live.runstate == 0) {
            btnBook.setVisibility(View.VISIBLE);
            btnState.setVisibility(View.GONE);
            type.setText("未开始");
        } else {
            btnBook.setVisibility(View.GONE);
            btnState.setVisibility(View.VISIBLE);
            if (live.runstate == 1) {
                type.setText("直播中");
                btnState.setBackgroundResource(R.drawable.bg_btn_orange);
                btnState.setTextColor(getContext().getResources().getColor(R.color.white));
                btnState.setText("直播中");
            } else if (live.runstate == 2) {
                type.setText("已结束");
                btnState.setBackgroundResource(R.drawable.sl_btn_send);
                btnState.setTextColor(getContext().getResources().getColor(R.color.white));
                btnState.setText("已结束");
            }
        }
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.bookLive(live.id, !((boolean) v.getTag()), v);

            }
        });


        tvTitle.setText(live.name);
        tvOpentiem.setText("开讲时间:" + live.starttime);
        tvBookNum.setText("已有" + live.bookings + "人已约");

        WRStarApplication.imageLoader.displayImage(live.mainImgUrl, ivPicture, WRStarApplication.getListOptions());


        setBookView(btnBook, live.isBook);

        if (author != null) {
            tvAuthorName.setText(author.username);
            ratingbar.setStar(author.star);
            WRStarApplication.imageLoader.displayImage(author.logourl, ivAvator, WRStarApplication.getAvatorOptions());
        }


        /**
         * 标签Tag
         */
        for (int i = 0; i < tvs.length; i++) {
            if (live.tags != null) {
                if (i < live.tags.size() && live.tags.get(i) != null) {
                    tvs[i].setVisibility(View.VISIBLE);
                    tvs[i].setText("#" + live.tags.get(i).name + "#");
                } else {
                    tvs[i].setVisibility(View.INVISIBLE);
                }
            } else {
                tvs[i].setVisibility(View.INVISIBLE);
            }
        }


    }

    private void setBookView(View v, boolean isBook) {
        Button btn = (Button) v;
        btn.setTag(isBook);
        if ((boolean) v.getTag()) {
            btn.setTextColor(getContext().getResources().getColor(R.color.text_orange));
            btn.setBackgroundResource(R.mipmap.ic_book_bg);
            btn.setText("已预约");
        } else {
            btn.setTextColor(getContext().getResources().getColor(R.color.text_gray_deep));
            btn.setBackgroundResource(R.drawable.bg_btn_yellow);
            btn.setText("立即预约");
        }
    }

}
