package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.MyVideoOndemandResponse;
import com.wmx.android.wrstar.entities.LiveCollect;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.TimeUtils;
import com.wmx.android.wrstar.views.widgets.MyCountDownTimer;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MyVideoLiveAdapter extends RecyclerView.Adapter<MyVideoLiveAdapter.LiveHolder> {


    private OnRecyclerItemClickListener OnRecyclerItemClickListener;
    private Context context;
    List<LiveCollect> lists;

    public MyVideoLiveAdapter(Context context, List<LiveCollect> lists) {
        this.context = context;
        this.lists = lists;

    }

    public OnrefreshListData onrefreshListData;

    public interface OnrefreshListData {
        void refresh();
    }

    public void setOnrefreshListData(OnrefreshListData onrefreshListData) {
        this.onrefreshListData = onrefreshListData;
    }

    @Override
    public LiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LiveHolder holder = new LiveHolder(LayoutInflater.from(context).inflate(R.layout.item_myvideo_live, null));
        return holder;
    }

    public void starCountUp() {
        handler.sendEmptyMessage(1);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                for (LiveCollect collect : lists) {
                    collect.interval += 1;
                    notifyDataSetChanged();
                }
                handler.sendEmptyMessageDelayed(1, 1000);
            }

        }
    };





    @Override
    public void onBindViewHolder(LiveHolder holder, final int position) {

        LiveCollect liveCollect = lists.get(position);


        holder.title.setText(liveCollect.name);

        if (holder.picture.getTag() == null || !holder.picture.getTag().equals(liveCollect.mainImgUrl)) {
            WRStarApplication.imageLoader.displayImage(liveCollect.mainImgUrl, holder.picture, WRStarApplication.getListOptions());
            holder.picture.setTag(liveCollect.mainImgUrl);
        }

        if (liveCollect.runstate == 0) {

            holder.time.setBackgroundResource(R.drawable.bg_btn_yellow);
            holder.time.setTextColor(context.getResources().getColor(R.color.text_gray_deep));
            holder.tvTips.setText("温馨提示:可提前十分钟进入直播间哦");

            MyCountDownTimer countTime = new MyCountDownTimer(liveCollect.interval * 1000, 1000, holder.time);
            countTime.setText("距直播 : ");

            countTime.setCountFinish(new MyCountDownTimer.OnCountFinish() {
                @Override
                public void finish() {
                    if (onrefreshListData != null) {
                        onrefreshListData.refresh();
                    }
                }
            });
            countTime.start();

        } else if (liveCollect.runstate == 1) {

            holder.time.setBackgroundResource(R.drawable.bg_btn_orange);
            holder.time.setTextColor(context.getResources().getColor(R.color.text_gray_deep));
            holder.tvTips.setText("温馨提示:点击进去直播间");

            holder.time.setText("直播中" + TimeUtils.formatTime(liveCollect.interval * 1000));

        } else if (liveCollect.runstate == 2) {
            holder.time.setText("已结束直播");
            holder.tvTips.setText("温馨提示:直播已经结束了");
            holder.time.setBackgroundResource(R.drawable.sl_btn_send);
            holder.time.setTextColor(context.getResources().getColor(R.color.white));
        }

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

        return lists.size();
    }

    class LiveHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;
        TextView tvTips;
        ImageView picture;

        public LiveHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.iv_video);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            tvTips = (TextView) itemView.findViewById(R.id.tv_tips);


        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        OnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

}
