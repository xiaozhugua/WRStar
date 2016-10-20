package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.FansResponse;
import com.wmx.android.wrstar.biz.response.OtherUserInfoResponse;
import com.wmx.android.wrstar.entities.CourseWare;

import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public interface ILiveRoomView {

    void getCourseWareSuccess(List<CourseWare> list);
    void getFansSuccess(FansResponse response);

    void sendGiftSuccess();
    void sendStarBeanSuccess();


    void getRandomBeanSuccess(String count);



    void setfocuseSuccess();
    void setfocuseCancelSuccess();


    void getOtherUserInfoSuccess(OtherUserInfoResponse response);


}
