package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.ManagerLiveResponse;

/**
 * Created by Administrator on 2016/5/16.
 */
public interface ManagerLiveInfoView {

    void modifyTitleNameSuccess(ManagerLiveResponse response);
    void modifyTitleNameFailed(String resultDec);

    void modifyTagSuccess(ManagerLiveResponse response);
    void modifyTagFailed(String resultDec);

    void modifyPicSuccess(ManagerLiveResponse response);
    void modifyPicFailed(String resultDec);

    void getLiveInfoSuccess(ManagerLiveResponse response);
    void getLiveInfoFailed(String resultDec);
}
