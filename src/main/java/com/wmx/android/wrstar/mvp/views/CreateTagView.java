package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.CreateTagResponse;

/**
 * Created by Administrator on 2016/7/19.
 */
public interface CreateTagView {

    void createTagSuccess(CreateTagResponse mCreateTagResponse);
    void createTagFail(String dec);
}
