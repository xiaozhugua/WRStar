package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.KeJianResponse;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface  KeJianView {

    void getKeJianListSuccess(KeJianResponse response);


    void getKeJianWareListFailed();
}
