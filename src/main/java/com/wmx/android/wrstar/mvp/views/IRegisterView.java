package com.wmx.android.wrstar.mvp.views;

/**
 * Created by wubiao on 2016/1/12.
 *
 * Des: 注册页面view操作接口.
 */
public interface IRegisterView{
    void getAuthCodeSuccess();
    void getAuthCodeFailure(String code, String msg);
    void registerSuccess();
    void registerFailure(String code, String msg);
}
