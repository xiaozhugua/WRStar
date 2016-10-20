package com.wmx.android.wrstar.mvp.holder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.Home;

/**
 * Created by Administrator on 2016/6/21.
 */
public class HomeHolder extends BaseViewHolder<Home> {


    public static String live = "drawable://" + R.mipmap.ic_vip;
    public static String ondemand = "drawable://" + R.mipmap.ic_vip;

    ImageView iv_picture;
    ImageView iv_type;
    TextView type;
    TextView title;
    TextView[] tvs;

    public HomeHolder(ViewGroup parent) {
        super(parent, R.layout.item_home);

        tvs = new TextView[3];
        tvs[0] = (TextView) itemView.findViewById(R.id.tv1);
        tvs[1] = (TextView) itemView.findViewById(R.id.tv2);
        tvs[2] = (TextView) itemView.findViewById(R.id.tv3);

        iv_picture = (ImageView) itemView.findViewById(R.id.iv_picture);
        iv_type = (ImageView) itemView.findViewById(R.id.iv_type);
        type = (TextView) itemView.findViewById(R.id.type);
        title = (TextView) itemView.findViewById(R.id.tv_title);
    }

    @Override
    public void setData(Home home) {
     //   super.setData(data);

     

        title.setText(home.name);

        String status="" ;
        switch (home.status){
            case 0:
                status="未开始" ;
                break;
            case 1:
                status="直播中" ;
                break;
            case 2:
                status="已结束" ;
                break;
        }
        if (!TextUtils.isEmpty(status)){
             type.setVisibility(View.VISIBLE);
             type.setText(status);
        }else{
            type.setVisibility(View.INVISIBLE);
        }


        WRStarApplication.imageLoader.displayImage(home.type == 1 ? live : ondemand, iv_type, WRStarApplication.getListOptions());



        if (iv_picture.getTag()==null || !iv_picture.getTag().equals(home.imgurl)){
            WRStarApplication.imageLoader.displayImage(home.imgurl,iv_picture,WRStarApplication.getListOptions());
            iv_picture.setTag(home.imgurl);
        }



        for (int i=0;i<tvs.length;i++){
            if(home.tags!=null){
                if (i< home.tags.size() &&home.tags.get(i)!=null){
                    tvs[i].setVisibility(View.VISIBLE);
                    tvs[i].setText("#"+home.tags.get(i).name+"#");
                }else{
                    tvs[i].setVisibility(View.GONE);
                }
            }else{
                tvs[i].setVisibility(View.GONE);
            }
        }





    }
}
