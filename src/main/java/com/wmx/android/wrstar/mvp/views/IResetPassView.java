package com.wmx.android.wrstar.mvp.views;

/**
 * Created by wubiao on 2016/1/19.
 *
 * Des: 忘记密码.
 */
public interface IResetPassView {
    void getAuthCodeSuccess();
    void getAuthCodeFailure(String code, String msg);
    void resetPasswordSuccess();
    void resetPasswordFailure(String code, String msg);
    void toActivity(String account, String password);
}
