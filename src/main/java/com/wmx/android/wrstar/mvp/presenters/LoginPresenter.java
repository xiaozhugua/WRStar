package com.wmx.android.wrstar.mvp.presenters;

import android.content.res.Resources;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.AccountBiz;
import com.wmx.android.wrstar.biz.response.LoginResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.ILoginView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.RegularExpressionsUtils;

/**
 * Created by wubiao on 2016/1/16.
 * <p/>
 * Des: 用户登录.
 */
public class LoginPresenter extends BasePresenter {

    private AccountBiz mAccountBiz;

    private ILoginView mLoginView;

    private Resources mResources;

    public LoginPresenter(ICommonView commonView, ILoginView userLoginView, Resources resources) {
        super(commonView);
        mLoginView = userLoginView;
        mAccountBiz = AccountBiz.getInstance(WRStarApplication.getContext());
        mResources = resources;
    }

    /**
     * 登陆.
     *
     * @param account
     * @param password
     */
    public void login(final String account, String password) {
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

        // 显示进度
        mCommonView.showDialog(mResources.getString(R.string.logining));

        mAccountBiz.login(account, password, new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {



                if (ServerCodes.SUCCESS.equals(response.getResult())) {
                    LogUtil.i("User", response.body.userinfo.toString());
                    // 存储用户信息
                    WRStarApplication.setUser(response.body.userinfo);
                    PreferenceUtils.setAccount(WRStarApplication.getContext(), account);
                    PreferenceUtils.setUserId(WRStarApplication.getContext(), response.body.userinfo.passportid);


                    PreferenceUtils.setToken(WRStarApplication.getContext(), response.body.userinfo.token);

                    mLoginView.loginSuccess(response);
                } else {
                    mLoginView.loginFailure(response.getResult(), response.getResultDesc());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("login",error.toString());
                mCommonView.netError();
            }
        }, "login");
    }

    /**
     * 社会化登陆.
     *
     * @param openid
     * @param nickName
     * @param headPortrait
     * @param socialAccountType
     */
    public void socialLogin(final String openid, String nickName, String headPortrait, final String socialAccountType) {

        if(TextUtils.isEmpty(openid)){
            mCommonView.showToast(mResources.getString(R.string.get_social_account_token_failure));
            return;
        }

        mAccountBiz.socialLogin(openid, nickName, headPortrait, socialAccountType, new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {
                LogUtil.d(response.getResult());

                if (ServerCodes.SUCCESS.equals(response.getResult())) {
                    // 该账号已绑定手机号
                    mLoginView.socialLoginSuccess(true, openid, socialAccountType);

                    WRStarApplication.setUser(response.body.userinfo);
                   // PreferenceUtils.setAccount(WRStarApplication.getContext(), account);
                    PreferenceUtils.setUserId(WRStarApplication.getContext(), response.body.userinfo.passportid);


                    PreferenceUtils.setToken(WRStarApplication.getContext(), response.body.userinfo.token);

                    LogUtil.i("response.body.userinfo.toString():"+response.body.userinfo.toString());

                } else if (ServerCodes.SOCIAL_ACCOUNT_UNBIND.equals(response.getResult())) {
                    // 该账号未绑定手机号
                    mLoginView.socialLoginSuccess(false, openid, socialAccountType);
                } else {
                    // 操作失败
                    mLoginView.socialLoginFailure(response.getResult(), response.getResultDesc());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d(error.toString());
                mCommonView.netError();
            }
        }, "socialLogin");
    }
}
