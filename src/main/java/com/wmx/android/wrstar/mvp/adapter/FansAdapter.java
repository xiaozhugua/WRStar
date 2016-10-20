package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.Fans;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class FansAdapter extends BaseAdapter{
    LayoutInflater inflater;

    private List<Fans> list;

    public FansAdapter(Context context,List<Fans> list){
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    class Holder {
        TextView tv_name ;
        ImageView iv_picture;

    }
    Holder  holder ;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return  list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_liveinteraction,null);

//            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            holder.iv_picture =(ImageView)convertView.findViewById(R.id.iv_picture);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }

        Fans fans =list.get(position);

//        holder.tv_name.setText(fans.username);
        WRStarApplication.imageLoader.displayImage(fans.logourl,holder.iv_picture,WRStarApplication.getAvatorOptions());



        return convertView;
    }
}
