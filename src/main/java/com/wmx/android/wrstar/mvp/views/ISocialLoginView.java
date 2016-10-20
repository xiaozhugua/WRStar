package com.wmx.android.wrstar.mvp.views;

/**
 * Created by wubiao on 2016/1/30.
 *
 * Des:
 */
public interface ISocialLoginView{
    void getAuthCodeSuccess();
    void getAuthCodeFailure(String code, String msg);
    void checkPhoneNumSuccess(String state);
    void checkPhoneNumFailure(String code, String msg);
    void bindPhoneNumSuccess();
    void bindPhoneNumFailure(String code, String msg);
}
