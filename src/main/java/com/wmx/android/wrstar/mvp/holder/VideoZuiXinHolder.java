package com.wmx.android.wrstar.mvp.holder;


import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.HomePageResponse;

/**
 */
public class VideoZuiXinHolder extends BaseViewHolder<HomePageResponse.BodyEntity.NewestsEntity> {

    ImageView iv_wmx_zuixin ;
    TextView tv_wmx_zuixin_name ;

    public VideoZuiXinHolder(ViewGroup parent) {
        super(parent, R.layout.item_home_ditials);

         iv_wmx_zuixin = (ImageView) itemView.findViewById(R.id.iv_video);
         tv_wmx_zuixin_name = (TextView) itemView.findViewById(R.id.title);
    }

    @Override
    public void setData(HomePageResponse.BodyEntity.NewestsEntity newests) {
        super.setData(newests);
        WRStarApplication.imageLoader.displayImage(newests.imgurl, iv_wmx_zuixin, WRStarApplication.getListOptions());
        tv_wmx_zuixin_name.setText(newests.name);
    }
}
