package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.LoginResponse;

/**
 * Created by wubiao on 2016/1/16.
 *
 * Des: 登陆页面view操作接口.
 */
public interface ILoginView{
    void loginSuccess(LoginResponse loginResponse);
    void loginFailure(String code, String msg);
    void socialLoginSuccess(boolean isBinded, String openid, String socialAccountType);
    void socialLoginFailure(String code, String msg);
}
