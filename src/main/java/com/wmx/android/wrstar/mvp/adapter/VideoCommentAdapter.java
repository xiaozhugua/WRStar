package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.VideoComment;
import com.wmx.android.wrstar.mvp.presenters.VideoDetailPresenter;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.widgets.CirImageView;

/**
 * Created by Administrator on 2016/5/9.
 */
public class VideoCommentAdapter extends RecyclerView.Adapter<VideoCommentAdapter.CommentHolder>{
    private OnRecyclerItemClickListener OnRecyclerItemClickListener;
    private Context context;
    VideoComment videoComment ;

    VideoDetailPresenter presenter;

    public VideoCommentAdapter(Context context,VideoComment videoComment,VideoDetailPresenter presenter){
        this.context =context;
        this.videoComment = videoComment;

        this.presenter = presenter ;

    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentHolder holder = new CommentHolder(LayoutInflater.from(context).inflate(R.layout.item_video_comment,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final CommentHolder holder,final int position) {


        final VideoComment.BodyEntity.CommentsEntity cmt =  videoComment.body.comments.get(position);


        holder.name.setText(cmt.userinfo.username);
        holder.commentNum.setText("0");
        holder.goodNum.setText(cmt.like+"");
        holder.comment.setText(cmt.comment);
//        LogUtil.e("time  :"+cmt.time+"");
//        LogUtil.e("timeStampe  :"+cmt.timestamp+"");
//        LogUtil.e("curretime  :"+System.currentTimeMillis()+"");
        if(!TextUtils.isEmpty(cmt.time+"")){
//           String curreTime= TimeUtils.getRelativeTime(context.getResources(),cmt.timestamp,true);
//           String curreTime2= TimeUtils.getRelativeTimeWithSingleUnit(context.getResources(),cmt.timestamp,true);
//            LogUtil.e("curretime  :"+curreTime);
//            LogUtil.e("curretime2  :"+curreTime2);
            holder.time.setText(cmt.time);
        }



        holder.iv_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SysUtil.isFastClick()){
                    return;
                }
                if (WRStarApplication.getUser() != null) {
                    presenter.addCommentLike(WRStarApplication.getUser().mobnum, cmt.courseid, cmt.id, v, holder.goodNum);
                } else {
                    presenter.mCommonView.login();
                }


            }
        });
         WRStarApplication.imageLoader.displayImage(cmt.userinfo.userLogo, holder.avator, WRStarApplication.getAvatorOptions());


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
        LogUtil.i("comment", "comment size:" + videoComment.body.comments.size());
        return videoComment.body.comments.size();
    }

    public void addLikeSuccess(View imageview,View textview,int count){
        ((ImageView)imageview.findViewById(R.id.iv_good)).setImageDrawable(context.getResources().getDrawable(R.mipmap.img_zan_select));
        ((TextView)textview.findViewById(R.id.cmt_good)).setText(count+"");


    }


    class CommentHolder  extends  RecyclerView.ViewHolder{


        TextView name;
        TextView comment;
        TextView commentNum;
        TextView goodNum;

        CirImageView avator ;

        ImageView iv_good ;


        TextView time;

        public CommentHolder(View itemView) {
            super(itemView);
            name =(TextView)itemView.findViewById(R.id.tv_user_name_comm);
            comment =(TextView)itemView.findViewById(R.id.cmt_comment);
            commentNum =(TextView)itemView.findViewById(R.id.cmt_num);
            goodNum =(TextView)itemView.findViewById(R.id.cmt_good);

            avator =(CirImageView)itemView.findViewById(R.id.cmt_avator);

            iv_good=(ImageView)itemView.findViewById(R.id.iv_good);
            time =(TextView)itemView.findViewById(R.id.tv_user_time_comm);

        }
    }
    public interface OnRecyclerItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        OnRecyclerItemClickListener = onRecyclerItemClickListener;
    }
}
