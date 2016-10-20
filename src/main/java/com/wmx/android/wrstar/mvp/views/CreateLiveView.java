package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.CreateLiveResponse;

/**
 * Created by Administrator on 2016/7/19.
 */
public interface CreateLiveView {

    void createLiveSuccess(CreateLiveResponse mCreateLiveResponse);
    void createLiveFail(String dec);
}
