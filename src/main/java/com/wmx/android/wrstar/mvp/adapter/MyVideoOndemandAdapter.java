package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.MyVideoOndemandResponse;
import com.wmx.android.wrstar.entities.Video;
import com.wmx.android.wrstar.utils.LogUtil;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MyVideoOndemandAdapter extends RecyclerView.Adapter<MyVideoOndemandAdapter.OndemandHolder> {


    private OnRecyclerItemClickListener OnRecyclerItemClickListener;
    private Context context;
    MyVideoOndemandResponse video;

    public MyVideoOndemandAdapter(Context context, MyVideoOndemandResponse Video) {
        this.context = context;
        this.video = Video;

    }

    @Override
    public OndemandHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OndemandHolder holder = new OndemandHolder(LayoutInflater.from(context).inflate(R.layout.item_myvideo_ondemand, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(OndemandHolder holder, final int position) {
        LogUtil.i("ondemand", "onBindViewHolder");
        Video vi = video.body.items.get(position);

        holder.title.setText(vi.name);
        holder.playNum.setText(vi.watch + "");
        holder.goodNum.setText(vi.collect + "");
        holder.author.setText(vi.teachername);

        // holder.time.setText(vi.createtime);

        WRStarApplication.imageLoader.displayImage(vi.imgurl, holder.picture, WRStarApplication.getListOptions());
        WRStarApplication.imageLoader.displayImage(vi.teacherimgurl, holder.avator, WRStarApplication.getListOptions());

        if (OnRecyclerItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnRecyclerItemClickListener.onItemClick(v, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        LogUtil.i("ondemand", "videoPageResponse.body.items.size() :" + video.body.items.size());
        return video.body.items.size();
    }

    class OndemandHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView time;
        TextView author;
        TextView playNum;
        TextView goodNum;

        ImageView avator;
        ImageView picture;

        public OndemandHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            playNum = (TextView) itemView.findViewById(R.id.tv_play_num);
            goodNum = (TextView) itemView.findViewById(R.id.tv_good_num);


            avator = (ImageView) itemView.findViewById(R.id.iv_author);
            picture = (ImageView) itemView.findViewById(R.id.iv_video);


        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        OnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

}
