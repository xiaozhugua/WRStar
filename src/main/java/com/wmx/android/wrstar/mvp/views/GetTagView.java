package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.GetHotTagResponse;

/**
 * Created by Administrator on 2016/7/19.
 */
public interface GetTagView {

    void getHotTagSuccess(GetHotTagResponse mGetHotTagResponse);
    void getHotTagFail(String dec);
}
