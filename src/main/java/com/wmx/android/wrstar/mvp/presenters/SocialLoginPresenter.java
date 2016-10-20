package com.wmx.android.wrstar.mvp.presenters;

import android.content.res.Resources;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.AccountBiz;
import com.wmx.android.wrstar.biz.response.CheckPhoneNumResponse;
import com.wmx.android.wrstar.biz.response.GetAuthCodeResponse;
import com.wmx.android.wrstar.biz.response.LoginResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.ISocialLoginView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.RegularExpressionsUtils;

/**
 * Created by wubiao on 2016/1/30.
 *
 * Des: 社会化登陆.
 */
public class SocialLoginPresenter extends BasePresenter {

    private ISocialLoginView mSocialLoginView;

    private AccountBiz mAccountBiz;

    private Resources mResources;

    public SocialLoginPresenter(ICommonView commonView, ISocialLoginView socialLoginView, Resources resources) {
        super(commonView);
        mSocialLoginView = socialLoginView;
        mAccountBiz = AccountBiz.getInstance(WRStarApplication.getContext());
        mResources = resources;
    }

    /**
     * 校验手机号.
     *
     * @param account
     * @param socialAccountType
     */
    public void checkPhoneNum(String account, String socialAccountType) {

        if (!RegularExpressionsUtils.checkPhoneNum(account)) {
            // 手机号格式不正确
            mCommonView.showToast(mResources.getString(R.string.account_format_error));
            return;
        }

        // 显示进度
        mCommonView.showDialog(mResources.getString(R.string.phone_number_checking));

        mAccountBiz.checkPhoneNum(account, socialAccountType, new Response.Listener<CheckPhoneNumResponse>() {
            @Override
            public void onResponse(CheckPhoneNumResponse response) {
                if (ServerCodes.SUCCESS.equals(response.getResult())) {
                    mSocialLoginView.checkPhoneNumSuccess(response.getBody().getState());
                } else {
                    mSocialLoginView.checkPhoneNumFailure(response.getResult(), response.getResultDesc());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
                LogUtil.i("验证手机错误");
            }
        }, "checkPhoneNum");
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
                if (ServerCodes.SUCCESS.equals(response.getResult())) {
                    mSocialLoginView.getAuthCodeSuccess();
                } else {
                    mSocialLoginView.getAuthCodeFailure(response.getResult(), response.getResultDesc());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
            }
        }, "getAuthCode");
    }

    /**
     * 第三方账号绑定手机号.
     *
     * @param account
     * @param openid
     * @param password
     * @param socialAccountType
     * @param authCode
     * @param isRegistered
     */
    public void bindPhoneNum(String account, String openid, String password, String socialAccountType,
                             String authCode, boolean isRegistered,String nickname,String imageurl){

        int minimumLengthOfPassword = mResources.getInteger(R.integer.min_length_password);

        if(!isRegistered){
            if (!RegularExpressionsUtils.checkPassword(password)) {
                // 密码长度不够
                mCommonView.showToast(mResources.getString(R.string.password_length_error, minimumLengthOfPassword));
                return;
            }
        }

        if (!RegularExpressionsUtils.checkAuthCode(authCode)) {
            // 验证码格式不正确
            mCommonView.showToast(mResources.getString(R.string.auth_code_format_error));
            return;
        }

        mCommonView.showDialog(mResources.getString(R.string.bind_phone_num_ing));

        mAccountBiz.bindPhoneNum(account, openid, password, socialAccountType, authCode, isRegistered,nickname,imageurl,
        new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {
                if (ServerCodes.SUCCESS.equals(response.getResult())) {
//                    PreferenceUtils.setAccount(WRStarApplication.getContext(), account);
//                    PreferenceUtils.setUserId(WRStarApplication.getContext(), response.getBody().getPassportId());
                    mSocialLoginView.bindPhoneNumSuccess();


                    WRStarApplication.setUser(response.body.userinfo);
                    // PreferenceUtils.setAccount(WRStarApplication.getContext(), account);
                    PreferenceUtils.setUserId(WRStarApplication.getContext(), response.body.userinfo.passportid);

                    PreferenceUtils.setToken(WRStarApplication.getContext(), response.body.userinfo.token);

                } else {
                    mSocialLoginView.bindPhoneNumFailure(response.getResult(), response.getResultDesc());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
            }
        }, "bindPhoneNum");
    }
}
