package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.GoodsBiz;
import com.wmx.android.wrstar.biz.response.GoodsResponse;
import com.wmx.android.wrstar.mvp.views.GoodsView;
import com.wmx.android.wrstar.mvp.views.ICommonView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class GoodsPresenter extends BasePresenter {

    public final String Tag = "GoodsPresenter";

    private GoodsView mGoodsView;

    private GoodsBiz   mGoodsBiz;
    private ICommonView commonView;

    public GoodsPresenter(ICommonView commonView, GoodsView mGoodsView) {
        super(commonView);
        this.mGoodsView = mGoodsView;
        this.commonView = commonView;
        mGoodsBiz = GoodsBiz.getInstance(WRStarApplication.getContext());
    }


    public void getGoodsList() {

        mCommonView.showDialog("正在加载...");
        String mobnum ="";
        if (WRStarApplication.getUser() != null) {
            //  mCommonView.login();
            mobnum= getNum() ;
        }

        mGoodsBiz.getGoodsInfo(new Response.Listener<GoodsResponse>() {
            @Override
            public void onResponse(GoodsResponse response) {
                mGoodsView.getGoodsListSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                commonView.netError();
                mGoodsView.getGoodsWareListFailed();
            }
        }, Tag);
    }
}
