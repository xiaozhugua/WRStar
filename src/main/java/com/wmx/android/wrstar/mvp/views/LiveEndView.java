package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.LiveEndResponse;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface LiveEndView {

    void LiveEndSuccess(LiveEndResponse response);


    void LiveEndFailed(String dec);
}
