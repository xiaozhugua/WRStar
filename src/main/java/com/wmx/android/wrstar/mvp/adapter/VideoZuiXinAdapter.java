package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.biz.response.HomePageResponse;
import com.wmx.android.wrstar.mvp.holder.VideoZuiXinHolder;

/**
 * Created by Administrator on 2016/6/21.
 */
public class VideoZuiXinAdapter extends RecyclerArrayAdapter<HomePageResponse.BodyEntity.NewestsEntity> {

    public VideoZuiXinAdapter(Context context) {
        super(context);

    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoZuiXinHolder(parent);
    }
}
