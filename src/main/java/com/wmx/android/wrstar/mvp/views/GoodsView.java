package com.wmx.android.wrstar.mvp.views;


import com.wmx.android.wrstar.biz.response.GoodsResponse;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface GoodsView {

    void getGoodsListSuccess(GoodsResponse response);


    void getGoodsWareListFailed();
}
