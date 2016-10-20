package com.wmx.android.wrstar.mvp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/6/21.
 */
public class GongxianHolder extends RecyclerView.ViewHolder {
//
//
//    public static String live = "drawable://" + R.mipmap.ic_vip;
//    public static String ondemand = "drawable://" + R.mipmap.ic_vip;
//
//    ImageView iv_picture;
//    ImageView iv_type;
//    TextView type;
//    TextView title;
//    TextView[] tvs;
//
//    public GongxianHolder(ViewGroup parent) {
//        super(parent, R.layout.activity_my_recommend);
//
//        tvs = new TextView[3];
//        tvs[0] = (TextView) itemView.findViewById(R.id.tv1);
//        tvs[1] = (TextView) itemView.findViewById(R.id.tv2);
//        tvs[2] = (TextView) itemView.findViewById(R.id.tv3);
//
//        iv_picture = (ImageView) itemView.findViewById(R.id.iv_picture);
//        iv_type = (ImageView) itemView.findViewById(R.id.iv_type);
//        type = (TextView) itemView.findViewById(R.id.type);
//        title = (TextView) itemView.findViewById(R.id.tv_title);
//    }
//
//    @Override
//    public void setData(Gongxian Gongxian) {
//     //   super.setData(data);
//
//
//
//        title.setText(Gongxian.name);
//
//        String status="" ;
//        switch (Gongxian.status){
//            case 0:
//                status="未开始" ;
//                break;
//            case 1:
//                status="直播中" ;
//                break;
//            case 2:
//                status="已结束" ;
//                break;
//        }
//        if (!TextUtils.isEmpty(status)){
//             type.setVisibility(View.VISIBLE);
//             type.setText(status);
//        }else{
//            type.setVisibility(View.INVISIBLE);
//        }
//
//
//        WRStarApplication.imageLoader.displayImage(Gongxian.type == 1 ? live : ondemand, iv_type, WRStarApplication.getListOptions());
//
//
//
//        if (iv_picture.getTag()==null || !iv_picture.getTag().equals(Gongxian.imgurl)){
//            WRStarApplication.imageLoader.displayImage(Gongxian.imgurl,iv_picture,WRStarApplication.getListOptions());
//            iv_picture.setTag(Gongxian.imgurl);
//        }
//
//
//
//        for (int i=0;i<tvs.length;i++){
//            if(Gongxian.tags!=null){
//                if (i< Gongxian.tags.size() &&Gongxian.tags.get(i)!=null){
//                    tvs[i].setVisibility(View.VISIBLE);
//                    tvs[i].setText("#"+Gongxian.tags.get(i).name+"#");
//                }else{
//                    tvs[i].setVisibility(View.GONE);
//                }
//            }else{
//                tvs[i].setVisibility(View.GONE);
//            }
//        }
//
//
//
//
//
//    }

    public GongxianHolder(View itemView) {
        super(itemView);
    }
}
