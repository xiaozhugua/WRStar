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
import com.wmx.android.wrstar.entities.OndemandCourse;
import com.wmx.android.wrstar.views.widgets.CirImageView;

import java.util.List;


/**
 * Created by Administrator on 2016/5/9.
 */
public class AllSeeAdapter  extends RecyclerView.Adapter<AllSeeAdapter.AllseeHolder>{

    private OnRecyclerItemClickListener OnRecyclerItemClickListener;
    private Context context;
    private List<OndemandCourse> list;
    public AllSeeAdapter(Context context,List<OndemandCourse> list){
        this.context =context;
        this.list = list;

    }

    @Override
    public AllseeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AllseeHolder holder = new AllseeHolder(LayoutInflater.from(context).inflate(R.layout.item_allsee,null));
        return holder;
    }


    @Override
    public void onBindViewHolder(AllseeHolder holder,final int position) {

        OndemandCourse course = list.get(position);

        holder.title.setText(course.name);
        holder.playNum.setText(course.watch+"");
        holder.goodNum.setText(course.collect+"");
        holder.author.setText(course.teachername);
        holder.time.setText(course.time);

        WRStarApplication.imageLoader.displayImage(course.imgurl,holder.picture,WRStarApplication.getListOptions());
        WRStarApplication.imageLoader.displayImage(course.teacherimgurl,holder.avator,WRStarApplication.getListOptions());

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

    class AllseeHolder  extends  RecyclerView.ViewHolder{


        TextView title;
        TextView time;
        TextView author;
        TextView playNum;
        TextView goodNum;

        CirImageView avator ;
        ImageView  picture;

        public AllseeHolder(View itemView) {
            super(itemView);
            title =(TextView)itemView.findViewById(R.id.tv_title);
            time =(TextView)itemView.findViewById(R.id.tv_time);
            author =(TextView)itemView.findViewById(R.id.tv_author);
            playNum =(TextView)itemView.findViewById(R.id.tv_play_num);
            goodNum =(TextView)itemView.findViewById(R.id.tv_good_num);


            avator =(CirImageView)itemView.findViewById(R.id.iv_author);
            picture =(ImageView)itemView.findViewById(R.id.iv_video);


        }
    }
    public interface OnRecyclerItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        OnRecyclerItemClickListener = onRecyclerItemClickListener;
    }


}
