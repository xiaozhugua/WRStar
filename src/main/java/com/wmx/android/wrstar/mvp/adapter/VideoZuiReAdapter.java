package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.biz.response.HomePageResponse;
import com.wmx.android.wrstar.mvp.holder.VideoZuiReHolder;

/**
 * Created by Administrator on 2016/6/21.
 */
public class VideoZuiReAdapter extends RecyclerArrayAdapter<HomePageResponse.BodyEntity.HeatsEntity> {

    public VideoZuiReAdapter(Context context) {
        super(context);

    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoZuiReHolder(parent);
    }
}
