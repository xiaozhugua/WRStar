package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.VideoPageResponse;

/**
 * Created by Administrator on 2016/5/9.
 */
public interface IVideoView {

    void getVideoIndexSuccess(VideoPageResponse videoPageResponse);

    void getVideoIndexFailed();
}
