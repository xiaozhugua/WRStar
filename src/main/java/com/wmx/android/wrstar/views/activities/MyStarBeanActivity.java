package com.wmx.android.wrstar.views.activities;

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
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.dialog.RechargeDialog;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.widgets.CirImageView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/1.
 */
public class MyStarBeanActivity extends AbsBaseActivity implements IPayView, PayCallBack {
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.iv_avator)
    CirImageView ivAvator;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_star_bean_num)
    TextView tvStarBeanNum;
    @Bind(R.id.btn_go_recharge)
    Button btnGoRecharge;

    PayPresenter presenter;

    RechargeDialog dialog ;

    private IWXAPI api;


    @Override
    protected int getContentViewLayout() {
        return R.layout.acitivty_mystarbean;
    }

    @Override
    protected void initExtraData() {
        api = WXAPIFactory.createWXAPI(MyStarBeanActivity.this, SocialAccountInfo.WECHAT_APPID);
    }

    @Override
    protected void initVariables() {

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

        String star = mResources.getString(R.string.total_star2, WRStarApplication.getUser().starcoins);
        tvStarBeanNum.setText(star);

        tvName.setText(WRStarApplication.getUser().username);

        WRStarApplication.imageLoader.displayImage(WRStarApplication.getUser().userLogo, ivAvator, WRStarApplication.getAvatorOptions());
    }

    @Override
    protected void loadData() {



    }

    @Override
    protected String getPageTag() {
        return null;
    }


    @OnClick(R.id.btn_go_recharge)
    public void onClick() {

        dialog = new RechargeDialog(MyStarBeanActivity.this);
        dialog.setClickListener(new RechargeDialog.IClickListener() {
            @Override
            public void send(String money, int type) {

                if (type==PayBiz.PAY_ALI){
                    LogUtil.i("money:"+money);
                    presenter.getALiPayInfoFromServer(money, PayBiz.ACTION_BUYBEAN);
                }else if (type==PayBiz.PAY_WECHAT){
                    LogUtil.i("money:"+money);
                    presenter.getWeChatPayInfoFromServer(money, PayBiz.ACTION_BUYBEAN);
                }


            }
        });
        dialog.show();


    }


    @Override
    public void paysuccess(String result) {
        dialog.dismiss();
        showToast(result + ",您所购买的星豆将在24小时内到账");
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

        LogUtil.i("payInfo:"+payInfo);

        AliPay.getInstance().init(MyStarBeanActivity.this).pay(payInfo);
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

    @Override
    public void refreshUserInfo(User user) {
        WRStarApplication.setUser(user);

        String star = mResources.getString(R.string.total_star2, WRStarApplication.getUser().starcoins);
        tvStarBeanNum.setText(star);


    }
}
