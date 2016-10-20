package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.entities.Home;
import com.wmx.android.wrstar.mvp.holder.HomeHolder;

/**
 * Created by Administrator on 2016/6/21.
 */
public class HomeAdapter extends RecyclerArrayAdapter<Home> {
    public HomeAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeHolder(parent);
    }


}
