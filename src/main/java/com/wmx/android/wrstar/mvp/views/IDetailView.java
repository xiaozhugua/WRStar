package com.wmx.android.wrstar.mvp.views;

import android.view.View;

import com.wmx.android.wrstar.biz.response.SendVideoCommentResponse;
import com.wmx.android.wrstar.entities.DetailViewInfo;
import com.wmx.android.wrstar.entities.VideoComment;

/**
 * Created by Administrator on 2016/5/10.
 */
public interface IDetailView {

    void getDetailViewSuccess(DetailViewInfo detailViewInfo);
    void getCommentSuccess(VideoComment videoComment);
    void getCommentFailed();
    void sendCommentSuccess();
    void sendCommentFailed();

    void addVideoLikeSuccess();
    void addVideoLikeFaild();


    //评论 View
    void addCommentLikeSuccess(View imageview, View textview, int count);
    void addCommentLikeFaild(String resultDec);



}
