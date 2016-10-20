package com.wmx.android.wrstar.views.activities;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.PayBiz;
import com.wmx.android.wrstar.biz.response.WeChatPayResponse;
import com.wmx.android.wrstar.constants.SocialAccountInfo;
import com.wmx.android.wrstar.entities.User;
import com.wmx.android.wrstar.mvp.presenters.PayPresenter;
import com.wmx.android.wrstar.mvp.views.IPayView;
import com.wmx.android.wrstar.pay.alipay.AliPay;
import com.wmx.android.wrstar.pay.alipay.PayCallBack;
import com.wmx.android.wrstar.utils.MD5;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/8.
 */
public class BuyVIPAcitivty extends AbsBaseActivity implements IPayView, PayCallBack {
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;

    PayPresenter presenter;
    @Bind(R.id.btn_pay_wechat)
    Button btnPayWechat;
    @Bind(R.id.btn_pay_ali)
    Button btnPayAli;

    private IWXAPI api;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_buy_vip;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {
        api = WXAPIFactory.createWXAPI(this, SocialAccountInfo.WECHAT_APPID);
    }

    @Override
    protected void initViews() {

        presenter = new PayPresenter(this, this);




        actionBar.setActionBarListener(new ActionBarPrimary.ActionBarPrimaryListener() {
            @Override
            public void onLeftBtnClick() {
                finish();
            }

            @Override
            public void onRightTextClick() {

            }

            @Override
            public void onRightBtnClick() {

            }
        });


        String vip_price = mResources.getString(R.string.vip_price, "99");
        tvPrice.setText(vip_price);

        String vip_price_total = mResources.getString(R.string.vip_price_total, "99");
        tvTotalPrice.setText(vip_price_total);



        MD5 getMD5 = new MD5();
        System.out.println(MD5.getMD5Code("a654321"));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected String getPageTag() {
        return null;
    }


    /**
     * 支付宝以及微信 支付 回调
     * @param result
     */
    @Override
    public void paysuccess(String result) {


        showToast(result + ",您所购买的微明星会员将在24小时内到账");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.getUserInfo();
            }
        }, 500);

    }

    @Override
    public void payerror(String msg) {
        showToast(msg);
    }

    @Override
    public void getALiPayInfoSuccess(String payInfo) {

        AliPay.getInstance().init(BuyVIPAcitivty.this).pay(payInfo);
    }

    @Override
    public void getWeChatPayInfoSuccess(WeChatPayResponse response) {
        PayReq req = new PayReq();

        req.appId = response.body.appid;
        req.partnerId = response.body.partnerid;
        req.prepayId = response.body.prepayid;
        req.nonceStr = response.body.noncestr;
        req.timeStamp = response.body.timestamp;
        req.packageValue = response.body.packagename;
        req.sign = response.body.sign;
        req.extData = "app data"; // optional


        api.sendReq(req);
    }


    /**
     * 支付完成更新用户信息
     * @param user
     */
    @Override
    public void refreshUserInfo(User user) {
        WRStarApplication.setUser(user);

    }

    @OnClick({R.id.btn_pay_wechat, R.id.btn_pay_ali})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay_wechat:
                presenter.getWeChatPayInfoFromServer("0.1", PayBiz.ACTION_BUYVIP);
                break;
            case R.id.btn_pay_ali:
                presenter.getALiPayInfoFromServer("0.1", PayBiz.ACTION_BUYVIP);
                break;
        }
    }
}
