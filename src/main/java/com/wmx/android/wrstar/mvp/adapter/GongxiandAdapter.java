package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.Gongxian;
import com.wmx.android.wrstar.views.widgets.CirImageView;

import java.util.ArrayList;
import java.util.List;

public class GongxiandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;

    private Context context;
    private List<Gongxian> list;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    private boolean loadMore = false;

    public GongxiandAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<Gongxian>();
    }

    public boolean isLoadMore() {
        return loadMore;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里是条目布局创建的回调，可以根据类型來返回不同的布局
        View view = null;
         RecyclerView.ViewHolder holder = null;

        switch (viewType) {
            case TYPE_1:
                view = LayoutInflater.from(context).inflate(R.layout.item_mycommend2, parent, false);
                holder = new ViewHolderOne(view);
                break;
            case TYPE_2:
                view = LayoutInflater.from(context).inflate(R.layout.item_mycommend, parent, false);
                holder = new ViewHolderTwo(view);
                break;
        }
//        //itemView 的点击事件
//        if (mItemClickListener!=null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    mItemClickListener.onItemClick(holder.getAdapterPosition()-list.size());
//                }
//            });
//        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        //这里绑定数据

        switch (getItemViewType(position)) {
            case TYPE_1:
                final ViewHolderOne holderOne = (ViewHolderOne) holder;

                WRStarApplication.imageLoader.displayImage(list.get(position).iconurl, holderOne.reAvatar, WRStarApplication.getAvatorOptions());
                holderOne.tvRecomMigpaiming.setText(list.get(position).count + "");

                if(list.get(position).no==1){
                    holderOne.tvRecomMignci.setBackgroundResource(R.mipmap.re_first);
                }else
                if(list.get(position).no==2){
                    holderOne.tvRecomMignci.setBackgroundResource(R.mipmap.re_second);
                }else{
                    holderOne.tvRecomMignci.setBackgroundResource(R.mipmap.re_third);
                }

                holderOne.tvRecomMignname.setText(list.get(position).name);

                if (list.get(position).isvip) {
                    holderOne.tv_vip.setBackgroundResource(R.mipmap.ic_bigv);
                }

//                mItemClickListener.onItemClick(holderOne,position);
                break;
            case TYPE_2:
                ViewHolderTwo holderTwo = (ViewHolderTwo) holder;
//                onItemEventClick(holderTwo);
                WRStarApplication.imageLoader.displayImage(list.get(position).iconurl, holderTwo.reAvatar2, WRStarApplication.getAvatorOptions());
                holderTwo.tvRecomMigpaiming2.setText(list.get(position).count + "");

                holderTwo.tvRecomMignname2.setText(list.get(position).name);
                if (list.get(position).isvip) {
                    holderTwo.tv_vip2.setBackgroundResource(R.mipmap.ic_bigv);
                }

                holderTwo.tvRecomMignci2.setText(list.get(position).no+"");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        } else if (position == 1) {
            return TYPE_1;
        } else if (position == 2) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView tvRecomMignci;
        TextView tv_vip;
        CirImageView reAvatar;
        TextView tvRecomMignname;
        TextView tvRecomMigpaiming;
        TextView tvRecomMigreci;

        public ViewHolderOne(View itemView) {
            super(itemView);
            tv_vip = (TextView) itemView.findViewById(R.id.tv_vip);
            tvRecomMignci = (TextView) itemView.findViewById(R.id.tv_recom_mignci);
            reAvatar = (CirImageView) itemView.findViewById(R.id.re_avatar);
            tvRecomMignname = (TextView) itemView.findViewById(R.id.tv_recom_mignname);
            tvRecomMigpaiming = (TextView) itemView.findViewById(R.id.tv_recom_migpaiming);
            tvRecomMigreci = (TextView) itemView.findViewById(R.id.tv_recom_migreci);

        }
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder {

        TextView tv_vip2;
        TextView tvRecomMignci2;
        CirImageView reAvatar2;
        TextView tvRecomMignname2;
        TextView tvRecomMigpaiming2;
        TextView tvRecomMigreci2;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            tv_vip2 = (TextView) itemView.findViewById(R.id.tv_vip2);
            tvRecomMignci2 = (TextView) itemView.findViewById(R.id.tv_recom_mignci2);
            reAvatar2 = (CirImageView) itemView.findViewById(R.id.re_avatar2);
            tvRecomMignname2 = (TextView) itemView.findViewById(R.id.tv_recom_mignname2);
            tvRecomMigpaiming2 = (TextView) itemView.findViewById(R.id.tv_recom_migpaiming2);
            tvRecomMigreci2 = (TextView) itemView.findViewById(R.id.tv_recom_migreci2);
        }
    }

    public void add(Gongxian bean) {
        this.list.add(bean);
        notifyDataSetChanged();
    }

    public void addAll(List<Gongxian> bean) {
        this.list.addAll(bean);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

}
