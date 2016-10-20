package com.wmx.android.wrstar.mvp.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.Comment;
import com.wmx.android.wrstar.mvp.presenters.VideoDetailPresenter;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.widgets.CirImageView;

/**
 * Created by Administrator on 2016/6/21.
 */
public class DetailsVideoCommentHolder extends BaseViewHolder<Comment> {
    VideoDetailPresenter presenter;


    TextView name;
    TextView comment;
    TextView commentNum;
    TextView goodNum;

    TextView tv_user_time_comm;

    CirImageView avator;

    ImageView iv_good;


    public DetailsVideoCommentHolder(ViewGroup parent, VideoDetailPresenter presenter) {
        super(parent, R.layout.item_video_comment);
        this.presenter = presenter;
        name = (TextView) itemView.findViewById(R.id.tv_user_name_comm);
        comment = (TextView) itemView.findViewById(R.id.cmt_comment);
        commentNum = (TextView) itemView.findViewById(R.id.cmt_num);
        goodNum = (TextView) itemView.findViewById(R.id.cmt_good);
        tv_user_time_comm = (TextView) itemView.findViewById(R.id.tv_user_time_comm);
        avator = (CirImageView) itemView.findViewById(R.id.cmt_avator);

        iv_good = (ImageView) itemView.findViewById(R.id.iv_good);

    }


    @Override
    public void setData(final Comment cmt) {
        //   super.setData(data);


        name.setText(cmt.userinfo.username);
        commentNum.setText("0");
        goodNum.setText(cmt.like + "");
        comment.setText(cmt.comment);
        tv_user_time_comm.setText(cmt.time);
        iv_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SysUtil.isFastClick()) {
                    return;
                }
                if (WRStarApplication.getUser() != null) {
                    presenter.addCommentLike(WRStarApplication.getUser().mobnum, cmt.courseid, cmt.id, v, goodNum);
                } else {
                    presenter.mCommonView.login();
                }


            }
        });
        WRStarApplication.imageLoader.displayImage( cmt.userinfo.userLogo, avator, WRStarApplication.getAvatorOptions());


    }
}
