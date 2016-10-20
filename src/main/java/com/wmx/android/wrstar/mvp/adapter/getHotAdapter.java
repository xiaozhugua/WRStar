package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.entities.TagsEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class getHotAdapter extends BaseAdapter{
    LayoutInflater inflater;

    private List<TagsEntity> list;
    private Context context;

    public getHotAdapter(Context context, List<TagsEntity> list){
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    class Holder {
        TextView text_tag ;
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
            convertView = inflater.inflate(R.layout.item_tag,null);
            holder.text_tag = (TextView) convertView.findViewById(R.id.text_tag);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }

        TagsEntity tags =list.get(position);

        if(tags!=null){
            holder.text_tag.setText(tags.name);
        }
//        WRStarApplication.imageLoader.displayImage(tags.logourl,holder.iv_picture,WRStarApplication.getAvatorOptions());
        return convertView;
    }
}
