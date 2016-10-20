package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.entities.Live;
import com.wmx.android.wrstar.mvp.holder.DetailsVideoCommentHolder;
import com.wmx.android.wrstar.mvp.holder.LiveFragmentHolder;
import com.wmx.android.wrstar.mvp.presenters.LivingPagePresenter;
import com.wmx.android.wrstar.mvp.presenters.VideoDetailPresenter;

/**
 * Created by Administrator on 2016/6/21.
 */
public class DetailsVideoCommentAdapter extends RecyclerArrayAdapter<Live>{
    VideoDetailPresenter presenter;
    public DetailsVideoCommentAdapter(Context context, VideoDetailPresenter presenter) {
        super(context);
        this.presenter = presenter;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailsVideoCommentHolder(parent,presenter);
    }
}
