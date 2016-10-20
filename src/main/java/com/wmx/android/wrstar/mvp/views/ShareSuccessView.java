package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.ShareSuccessResponse;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface ShareSuccessView {

    void ShareSuccess(ShareSuccessResponse response);


    void ShareFailed(String dec);
}
