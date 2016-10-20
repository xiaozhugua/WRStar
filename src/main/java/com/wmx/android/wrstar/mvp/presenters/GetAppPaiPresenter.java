package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.GetAppPaiBiz;
import com.wmx.android.wrstar.biz.response.GetAppPaiResponse;
import com.wmx.android.wrstar.mvp.views.GetAppPaiView;
import com.wmx.android.wrstar.mvp.views.ICommonView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class GetAppPaiPresenter extends BasePresenter {

    public final String Tag = "GetAppPaiPresenter";

    private GetAppPaiView mGetAppPaiView;

    private GetAppPaiBiz mGetAppPaiBiz;
    private ICommonView commonView;

    public GetAppPaiPresenter(ICommonView commonView, GetAppPaiView mGetAppPaiView) {
        super(commonView);
        this.mGetAppPaiView = mGetAppPaiView;
        this.commonView = commonView;
        mGetAppPaiBiz = GetAppPaiBiz.getInstance(WRStarApplication.getContext());
    }


    public void getAppPaiList(String token, String index, String action) {

//        mCommonView.showDialog("正在加载...");

        mGetAppPaiBiz.getAppPaiInfo(token, index, action,new Response.Listener<GetAppPaiResponse>() {
            @Override
            public void onResponse(GetAppPaiResponse response) {
                mGetAppPaiView.getAppPaiListSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mGetAppPaiView.getAppPaiWareListFailed(error.toString());
            }
        }, Tag);
    }
}
