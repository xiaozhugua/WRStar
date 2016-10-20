package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.WacthRecord;
import com.wmx.android.wrstar.store.DBHandler;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.TimeUtils;

import java.util.List;


/**
 * Created by Administrator on 2016/5/9.
 */
public class WacthRecordAdapter extends RecyclerView.Adapter<WacthRecordAdapter.WatchRecordHolder>{
//    public static SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
//    public static SimpleDateFormat format_time = new SimpleDateFormat("HH:mm:ss");



    private OnRecyclerItemClickListener OnRecyclerItemClickListener;
    private Context context;
    private List<WacthRecord> list;
    public WacthRecordAdapter(Context context, List<WacthRecord> list){
        this.context =context;
        this.list = list;

    }

    @Override
    public WatchRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WatchRecordHolder holder = new WatchRecordHolder(LayoutInflater.from(context).inflate(R.layout.item_watch_record,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(WatchRecordHolder holder,final int position) {




        WacthRecord wacthRecord = list.get(position);
        String currentDate = TimeUtils.format(Long.valueOf(wacthRecord.timeStamp), "yyyy-MM-dd") ;
        holder.time.setText(TimeUtils.format(Long.valueOf(wacthRecord.timeStamp),"HH:mm:ss"));

        holder.date.setText(currentDate);


        if (position == 0) {
            holder.toplayout.setVisibility(View.VISIBLE);
        } else {
            final WacthRecord beforeWacth = list.get(position - 1);
            if (beforeWacth != null) {
                if (!currentDate.equals(TimeUtils.format(Long.valueOf(beforeWacth.timeStamp), "yyyy-MM-dd"))) {
                    holder.toplayout.setVisibility(View.VISIBLE);
                } else {
                    holder.toplayout.setVisibility(View.GONE);
                }
            }
        }




        holder.title.setText(wacthRecord.title);

        LogUtil.i("wacthRecord.type:："+wacthRecord.type);

        if (wacthRecord.type.equals(DBHandler.TYPE_Ondemand)){


            holder.type.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_rectangle_bj_orange));


            holder.type.setText("点播");
            holder.type.setTextColor(ContextCompat.getColor(context, R.color.text_orange));
            LogUtil.i("adapter type:：点播");
        }else{
            holder.type.setText("直播");
            LogUtil.i("adapter type:：直播");
            holder.type.setTextColor(ContextCompat.getColor(context, R.color.text_yellow));
            holder.type.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_rectangle_bj_yellow));
        }



      //  WRStarApplication.imageLoader.displayImage(wacthRecord.imgURL,holder.picture,WRStarApplication.getListOptions());

        if (holder.picture.getTag()==null || !holder.picture.getTag().equals(wacthRecord.imgURL)){
            WRStarApplication.imageLoader.displayImage(wacthRecord.imgURL,holder.picture,WRStarApplication.getListOptions());
            holder.picture.setTag(wacthRecord.imgURL);
        }


        if(OnRecyclerItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnRecyclerItemClickListener.onItemClick(v,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    
    class WatchRecordHolder extends  RecyclerView.ViewHolder{


        TextView title;
        TextView time;
        TextView date;
        ImageView  picture;
        TextView type;
        RelativeLayout toplayout ;

        public WatchRecordHolder(View itemView) {
            super(itemView);
            title =(TextView)itemView.findViewById(R.id.tv_title);
            time =(TextView)itemView.findViewById(R.id.tv_time);
            type =(TextView)itemView.findViewById(R.id.tv_tag);
            date=(TextView)itemView.findViewById(R.id.tv_date);

            picture =(ImageView)itemView.findViewById(R.id.iv_picture);
            toplayout =(RelativeLayout)itemView.findViewById(R.id.toplayout);

        }
    }
    public interface OnRecyclerItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        OnRecyclerItemClickListener = onRecyclerItemClickListener;
    }
}
