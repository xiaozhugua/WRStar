package com.wmx.android.wrstar.mvp.views;

import android.view.View;

import com.wmx.android.wrstar.biz.response.LivePageResponse;

/**
 * Created by Administrator on 2016/5/5.
 */
public interface ILivePageView {

        void getLiveListSuccess(LivePageResponse response);
        void getLiveListFailed(String dec);


        void bookLiveSuccess(View tv);
        void bookLiveFailed();

}
