package com.wmx.android.wrstar.mvp.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.Comment;
import com.wmx.android.wrstar.entities.OndemandCourse;
import com.wmx.android.wrstar.entities.Video;
import com.wmx.android.wrstar.mvp.presenters.VideoDetailPresenter;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.widgets.CirImageView;

/**
 * Created by Administrator on 2016/6/21.
 */
public class VideoFragmentHolder extends BaseViewHolder<Video> {



    ImageView iv_video;
    TextView title,tvAuthor;

    TextView tvCollectNum,tvWacthNum,tvTime;
    CirImageView authorAvator;


    public VideoFragmentHolder(ViewGroup parent) {
        super(parent, R.layout.item_gv_video);


        tvCollectNum = (TextView) itemView.findViewById(R.id.tv_collect_num);
        tvWacthNum = (TextView) itemView.findViewById(R.id.tv_play_num);
        tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        authorAvator = (CirImageView) itemView.findViewById(R.id.iv_author);
        tvAuthor  = (TextView) itemView.findViewById(R.id.tv_author);

        title = (TextView) itemView.findViewById(R.id.title);

        iv_video = (ImageView) itemView.findViewById(R.id.iv_video);

    }


    @Override
    public void setData(final Video video) {
        //   super.setData(data);

        title.setText(video.name);

        WRStarApplication.imageLoader.displayImage(video.imgurl, iv_video, WRStarApplication.getListOptions());

        tvCollectNum.setText(video.collect+"");
        tvWacthNum.setText(video.watch+"");
        tvTime.setText(video.time);


        tvAuthor.setText(video.teachername);
        WRStarApplication.imageLoader.displayImage(video.teacherimgurl, authorAvator, WRStarApplication.getListOptions());


    }
}
