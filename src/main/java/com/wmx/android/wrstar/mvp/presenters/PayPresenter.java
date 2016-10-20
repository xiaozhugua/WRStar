package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.AccountBiz;
import com.wmx.android.wrstar.biz.PayBiz;
import com.wmx.android.wrstar.biz.response.GetUserInfoResponse;
import com.wmx.android.wrstar.biz.response.ALiPayResponse;
import com.wmx.android.wrstar.biz.response.WeChatPayResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.IPayView;
import com.wmx.android.wrstar.mvp.views.ICommonView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class PayPresenter extends BasePresenter {

    public final String Tag = "PayPresenter";

    private IPayView payView;

    private IWXAPI api;


    private AccountBiz accountBiz;
    private PayBiz payBiz;
    private ICommonView commonView;

    public PayPresenter(ICommonView commonView, IPayView payView) {
        super(commonView);
        this.payView = payView;
        this.commonView = commonView;
        payBiz = PayBiz.getInstance(WRStarApplication.getContext());
        accountBiz = AccountBiz.getInstance(WRStarApplication.getContext());

    }


    public void getUserInfo() {
        String token = getToken();


        accountBiz.getUserInfo(token, new Response.Listener<GetUserInfoResponse>() {
            @Override
            public void onResponse(GetUserInfoResponse response) {


                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    payView.refreshUserInfo(response.body.user);

                } else {
                    mCommonView.showToast(response.getResultDesc());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                commonView.netError();
            }
        }, Tag);

    }


    /**
     * 向微明星服务器获取订单msg
     *
     * @param money 支付多少钱
     *
     */
    public void getALiPayInfoFromServer(String money, int goodsType) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
        mCommonView.showDialog("正在加载...");

        String mobnum = getNum();

        payBiz.sendALiPayInfo(mobnum, money, goodsType, new Response.Listener<ALiPayResponse>() {
            @Override
            public void onResponse(ALiPayResponse response) {


                if (response.getResult().equals(ServerCodes.SUCCESS)) {

                    payView.getALiPayInfoSuccess(response.body.orderinfo);


                } else {
                    mCommonView.showToast(response.getResultDesc());
                }


                mCommonView.hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                commonView.netError();
            }
        }, Tag);

    }

    public void getWeChatPayInfoFromServer(String money, int goodsType) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
        mCommonView.showDialog("正在加载...");

        String mobnum = getNum();

        String token = "";

        payBiz.sendWeChatPayInfo(mobnum, money, goodsType, new Response.Listener<WeChatPayResponse>() {
            @Override
            public void onResponse(WeChatPayResponse response) {


                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    payView.getWeChatPayInfoSuccess(response);
                } else {
                    mCommonView.showToast(response.getResultDesc());
                }


                mCommonView.hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                commonView.netError();
            }
        }, Tag);

    }
}
