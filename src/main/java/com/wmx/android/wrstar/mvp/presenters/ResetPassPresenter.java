package com.wmx.android.wrstar.mvp.presenters;

import android.content.res.Resources;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.AccountBiz;
import com.wmx.android.wrstar.biz.response.GetAuthCodeResponse;
import com.wmx.android.wrstar.biz.response.ResetPasswordResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.utils.RegularExpressionsUtils;
import com.wmx.android.wrstar.mvp.views.IResetPassView;

/**
 * Created by wubiao on 2016/1/19.
 *
 * Des: 忘记密码.
 */
public class ResetPassPresenter extends BasePresenter {

    private AccountBiz mAccountBiz;

    private IResetPassView mResetPassView;

    private Resources mResources;


    public ResetPassPresenter(ICommonView commonView, IResetPassView userForgetPasswordView, Resources resources) {
        super(commonView);
        mResetPassView = userForgetPasswordView;
        mAccountBiz = AccountBiz.getInstance(WRStarApplication.getContext());
        mResources = resources;
    }

    /**
     * 获取验证码.
     * @param account
     */
    public void getAuthCode(String account){

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
                mResetPassView.getAuthCodeSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
            }
        }, "getAuthCode");
    }

    /**
     * 下一步.
     *
     * @param account
     * @param password
     */
    public void next(String account, String password) {

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

        mResetPassView.toActivity(account, password);
    }

    /**
     * 重置密码.
     *
     * @param account
     * @param password
     * @param authCode
     */
    public void resetPassword(String account, String password, String authCode) {

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

        if(!RegularExpressionsUtils.checkAuthCode(authCode)){
            // 验证码格式不正确
            mCommonView.showToast(mResources.getString(R.string.auth_code_format_error));
            return;
        }

        // 显示进度
        mCommonView.showDialog(mResources.getString(R.string.reset_passwording));

        mAccountBiz.resetPassword(account, password, authCode, new Response.Listener<ResetPasswordResponse>() {
            @Override
            public void onResponse(ResetPasswordResponse response) {
                if(ServerCodes.SUCCESS.equals(response.getResult())){
                    mResetPassView.resetPasswordSuccess();
                }else {
                    mResetPassView.resetPasswordFailure(response.getResult(), response.getResultDesc());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
            }
        }, "resetPassword");
    }
}
