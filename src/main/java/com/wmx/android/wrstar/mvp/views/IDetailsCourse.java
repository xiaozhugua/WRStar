package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.DetailsCourseResponse;

/**
 * Created by Administrator on 2016/5/20.
 */
public interface IDetailsCourse  {
    void getCourseDetailsSuccess(DetailsCourseResponse detailsCourseResponse);
    void getCourseDetailsFailed(String dec);

    void setfocuseSuccess();
    void setfocuseCancelSuccess();

    void bookLiveSuccess();





}
