package com.wmx.android.wrstar.mvp.views;

/**
 * Created by wubiao on 2016/1/19.
 *
 * Des:
 */
public interface ICommonView  {
    void showToast(String msg);
    void showDialog(String msg);
    void netError();
    void hideDialog();

    void login();

}
