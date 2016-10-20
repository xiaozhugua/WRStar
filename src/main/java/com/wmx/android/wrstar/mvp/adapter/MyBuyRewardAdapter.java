package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.OndemandCourse;
import com.wmx.android.wrstar.entities.RewardRecord;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MyBuyRewardAdapter  extends RecyclerView.Adapter<MyBuyRewardAdapter.MyBuyRewardHolder> {

    private OnRecyclerItemClickListener OnRecyclerItemClickListener;
    private Context context;
    private List<RewardRecord> list;
    public MyBuyRewardAdapter(Context context,List<RewardRecord> list){
        this.context =context;
        this.list = list;

    }

    @Override
    public MyBuyRewardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyBuyRewardHolder holder = new MyBuyRewardHolder(LayoutInflater.from(context).inflate(R.layout.item_mybuy_reward,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyBuyRewardHolder holder,final int position) {

        RewardRecord record = list.get(position);

        holder.title.setText(record.name);
        holder.time.setText(record.time);


        if (record.type==1){
            holder.money.setText("+"+record.money);
            holder.money.setTextColor(ContextCompat.getColor(context,R.color.green));
            holder.summary.setVisibility(View.GONE);
        }else if (record.type==2){
            holder.money.setText("-"+record.money);
            holder.summary.setText(record.summary);
            holder.money.setTextColor(ContextCompat.getColor(context,R.color.red));
            holder.summary.setVisibility(View.VISIBLE);
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

    class MyBuyRewardHolder  extends  RecyclerView.ViewHolder{


        TextView title;
        TextView time;
        TextView summary;
        TextView money;

        public MyBuyRewardHolder(View itemView) {
            super(itemView);
            title =(TextView)itemView.findViewById(R.id.tv_title);
            time =(TextView)itemView.findViewById(R.id.tv_time);
            summary =(TextView)itemView.findViewById(R.id.tv_summary);
            money =(TextView)itemView.findViewById(R.id.tv_money);


        }
    }
    public interface OnRecyclerItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        OnRecyclerItemClickListener = onRecyclerItemClickListener;
    }
}
