package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.AccountBiz;
import com.wmx.android.wrstar.biz.response.LoginResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.IMainView;
import com.wmx.android.wrstar.utils.PreferenceUtils;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MainPresenter extends BasePresenter {

    public final String Tag = "MainPresenter";

    private IMainView mainView;

    private AccountBiz accountBiz;
    private ICommonView commonView;

    public MainPresenter(ICommonView commonView, IMainView mainView) {
        super(commonView);
        this.mainView = mainView;
        this.commonView = commonView;
        accountBiz = AccountBiz.getInstance(WRStarApplication.getContext());
    }


    public void autoLoginByToken(String token) {



        accountBiz.autoLoginByToken(token, new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    WRStarApplication.setUser(response.body.userinfo);
                    PreferenceUtils.setUserId(WRStarApplication.getContext(), response.body.userinfo.passportid);
                    PreferenceUtils.setToken(WRStarApplication.getContext(), response.body.userinfo.token);
                    mainView.autoLoginSuccess();

                } else if (response.getResult().equals(ServerCodes.TOKEND_DESTROY)){
                    commonView.showToast("第三方登录到期失效，请使用绑定手机号登录");
                    // mCommonView.netError();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                commonView.netError();
            }
        }, Tag);

    }



}
