package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.entities.Live;
import com.wmx.android.wrstar.mvp.holder.LiveFragmentHolder;
import com.wmx.android.wrstar.mvp.presenters.LivingPagePresenter;

/**
 * Created by Administrator on 2016/6/21.
 */
public class LiveFragmentAdapter extends RecyclerArrayAdapter<Live>{
    LivingPagePresenter presenter;
    public LiveFragmentAdapter(Context context,LivingPagePresenter presenter) {
        super(context);
        this.presenter = presenter;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new LiveFragmentHolder(parent,presenter);
    }
}
