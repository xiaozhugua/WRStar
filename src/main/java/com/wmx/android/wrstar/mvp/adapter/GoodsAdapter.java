package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.entities.Goods;

/**
 * Created by Administrator on 2016/7/15.
 */
public class GoodsAdapter extends RecyclerArrayAdapter<Goods> implements View.OnClickListener {

    Context context;
    public GoodsAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new mViewHolder(parent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_goodTitleName:
                break;
            case R.id.tv_goodPrice:
                break;
            case R.id.tv_addgood:

                break;
        }
    }


    static class mViewHolder extends BaseViewHolder<Goods> {
        TextView mTvGoodTitleName;
        TextView mTvGoodPrice;
        TextView mTvAddgood;

        mViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_goods);

            mTvGoodTitleName = (TextView) itemView.findViewById(R.id.tv_goodTitleName);
            mTvGoodPrice = (TextView) itemView.findViewById(R.id.tv_goodPrice);
            mTvAddgood = (TextView) itemView.findViewById(R.id.tv_addgood);

//            mTvGoodPrice.setOnClickListener(this);
//            mTvGoodTitleName.setOnClickListener(context);
//            mTvAddgood.setOnClickListener(context);
        }

        @Override
        public void setData(Goods data) {
            super.setData(data);
        }
    }


}
