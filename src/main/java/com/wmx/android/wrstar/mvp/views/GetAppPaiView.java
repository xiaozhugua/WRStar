package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.GetAppPaiResponse;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface GetAppPaiView {

    void getAppPaiListSuccess(GetAppPaiResponse response);


    void getAppPaiWareListFailed(String dec);
}
