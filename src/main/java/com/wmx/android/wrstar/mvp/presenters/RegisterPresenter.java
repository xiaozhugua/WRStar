package com.wmx.android.wrstar.mvp.presenters;

import android.content.res.Resources;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.AccountBiz;
import com.wmx.android.wrstar.biz.response.GetAuthCodeResponse;
import com.wmx.android.wrstar.biz.response.RegisterResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.RegularExpressionsUtils;
import com.wmx.android.wrstar.mvp.views.IRegisterView;

/**
 * Created by wubiao on 2016/1/13.
 *
 * Des: 用户注册.
 */
public class RegisterPresenter extends BasePresenter {

    private AccountBiz mAccountBiz;

    private IRegisterView mRegisterView;

    private Resources mResources;


    public RegisterPresenter(ICommonView commonView, IRegisterView userRegisterView, Resources resources) {
        super(commonView);
        mRegisterView = userRegisterView;
        mAccountBiz = AccountBiz.getInstance(WRStarApplication.getContext());
        mResources = resources;
    }

    /**
     * 获取验证码.
     *
     * @param account
     */
    public void getAuthCode(String account) {

        if (!RegularExpressionsUtils.checkPhoneNum(account)) {
            // 手机号格式不正确
            mCommonView.showToast(mResources.getString(R.string.account_format_error));
            return;
        }


        // 显示进度
        mCommonView.showDialog(mResources.getString(R.string.get_auth_code_ing));

        mAccountBiz.getAuthCode(account, new Response.Listener<GetAuthCodeResponse>() {
            @Override
            public void onResponse(GetAuthCodeResponse response) {
                if(ServerCodes.SUCCESS.equals(response.getResult())){
                    mRegisterView.getAuthCodeSuccess();
                }else {
                    mRegisterView.getAuthCodeFailure(response.getResult(), response.getResultDesc());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
                LogUtil.e("getAuthCode", "msg:"+error.toString());
            }
        }, "getAuthCode");
    }

    /**
     * 注册.
     *
     * @param account
     * @param password
     * @param authCode
     */
    public void register(final String account, String password, String authCode) {

        if (!RegularExpressionsUtils.checkPhoneNum(account)) {
            // 手机号格式不正确
            mCommonView.showToast(mResources.getString(R.string.account_format_error));
            return;
        }

        int minimumLengthOfPassword = mResources.getInteger(R.integer.min_length_password);
        if (!RegularExpressionsUtils.checkPassword(password)) {
            // 密码长度不够
            mCommonView.showToast(mResources.getString(R.string.password_length_error, minimumLengthOfPassword));
            return;
        }

        if (!RegularExpressionsUtils.checkAuthCode(authCode)) {
            // 验证码格式不正确
            mCommonView.showToast(mResources.getString(R.string.auth_code_format_error));
            return;
        }


        // 显示进度
        mCommonView.showDialog(mResources.getString(R.string.registering));

        mAccountBiz.register(account, password, authCode, new Response.Listener<RegisterResponse>() {
            @Override
            public void onResponse(RegisterResponse response) {
                mRegisterView.registerSuccess();

                if (ServerCodes.SUCCESS.equals(response.getResult())) {
                    PreferenceUtils.setAccount(WRStarApplication.getContext(), account);
                    PreferenceUtils.setUserId(WRStarApplication.getContext(), response.getBody().getPassportId());


                    PreferenceUtils.setToken(WRStarApplication.getContext(), response.getBody().getUser().token);

                    WRStarApplication.setUser(response.getBody().getUser());

                    mRegisterView.registerSuccess();
                } else {
                    mRegisterView.registerFailure(response.getResult(), response.getResultDesc());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
                LogUtil.e("register", error.toString());
            }
        }, "register");
    }
}
