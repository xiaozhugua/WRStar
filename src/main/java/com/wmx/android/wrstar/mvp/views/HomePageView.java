package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.HomePageResponse;

/**
 * Created by Administrator on 2016/6/8.
 */
public interface HomePageView {

    void getHomeListSuccess(HomePageResponse response);


    void getHomeListFailed();
}
