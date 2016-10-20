package com.wmx.android.wrstar.mvp.views;


import com.wmx.android.wrstar.biz.response.GongxianResponse;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface GongxianView {

    void getGongxinListSuccess(GongxianResponse response);


    void getGongxianListFailed();
}
