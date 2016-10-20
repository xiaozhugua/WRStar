package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.FirstPageResponse;

/**
 * Created by Administrator on 2016/6/8.
 */
public interface IFirstPageView {

    void getHomeListSuccess(FirstPageResponse response);


    void getHomeListFailed();
}
