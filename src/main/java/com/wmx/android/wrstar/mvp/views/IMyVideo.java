package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.MyVideoLiveResponse;
import com.wmx.android.wrstar.biz.response.MyVideoOndemandResponse;
import com.wmx.android.wrstar.entities.LiveCollect;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public interface IMyVideo {

    void getLiveVideoSuccess(MyVideoLiveResponse response);
    void getLiveVideoNull();
    void getLiveVideoFailed();

    void getOndemandVideoSuccess(MyVideoOndemandResponse video);
    void getOndemandVideoFailed();

}
