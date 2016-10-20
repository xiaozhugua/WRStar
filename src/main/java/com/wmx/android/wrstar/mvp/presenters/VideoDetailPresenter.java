package com.wmx.android.wrstar.mvp.presenters;

import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.DetailsVideoBiz;
import com.wmx.android.wrstar.biz.response.AddCommentLikeResponse;
import com.wmx.android.wrstar.biz.response.AddVideoLikeResponse;
import com.wmx.android.wrstar.biz.response.SendVideoCommentResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.entities.DetailViewInfo;
import com.wmx.android.wrstar.entities.VideoComment;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.IDetailView;
import com.wmx.android.wrstar.utils.LogUtil;

/**
 * Created by Administrator on 2016/5/10.
 */
public class VideoDetailPresenter extends BasePresenter {

    public final String Tag = "videoDetails";

    private IDetailView detailView;

    private DetailsVideoBiz detailsVideoBiz;

    public VideoDetailPresenter(ICommonView commonView, IDetailView detailView) {
        super(commonView);
        this.detailView = detailView;
        detailsVideoBiz = DetailsVideoBiz.getInstance(WRStarApplication.getContext());
    }


    /**
     * @param courseId  课程id
     * @param epicodeId 单独某集id
     *                  参数二选一
     */
    public void getVideoDetails(String mobnum,String courseId, String epicodeId) {


        detailsVideoBiz.getDetailsVideoInfo(mobnum,courseId, epicodeId, new Response.Listener<DetailViewInfo>() {
            @Override
            public void onResponse(DetailViewInfo response) {

                detailView.getDetailViewSuccess(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, Tag);

    }


    public void getComment(String courseId) {


        detailsVideoBiz.getComment(courseId, new Response.Listener<VideoComment>() {
            @Override
            public void onResponse(VideoComment response) {
                LogUtil.i(Tag, "getComment success:");
                LogUtil.i(Tag, "sss:" + response.body.comments.size());
                detailView.getCommentSuccess(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i(Tag, "getComment error:" + error.toString());
            }
        }, Tag);
    }

    public void sendComment(String mobnub,String courseId, String comment) {


        detailsVideoBiz.sendComment(mobnub,courseId, comment, new Response.Listener<SendVideoCommentResponse>() {
            @Override
            public void onResponse(SendVideoCommentResponse response) {

                if (response.body.addresult == 1) {
                    detailView.sendCommentSuccess();
                } else {
                    detailView.sendCommentFailed();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                detailView.sendCommentFailed();
                LogUtil.i(Tag, "sendComment error:" + error.toString());
            }
        }, Tag);
    }

    public void addCommentLike(String mobnub,String courseId, String commentId, final View imageview, final View textview) {
        LogUtil.i("videoDetails", "courseId:" + courseId + "---- commentId:" + commentId);
        detailsVideoBiz.addCommentLike(mobnub,courseId, commentId, new Response.Listener<AddCommentLikeResponse>() {
            @Override
            public void onResponse(AddCommentLikeResponse response) {

                if (response.result.equals(ServerCodes.SUCCESS)) {
                    detailView.addCommentLikeSuccess(imageview, textview, response.body.commentlikecount);

                } else {

                    detailView.addCommentLikeFaild((String)response.resultdesc);
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i(Tag, "addCommentLike error:" + error.toString());
                mCommonView.netError();

            }
        }, Tag);


    }


    public void addVideoLike(String mobnub,String courseId) {
        LogUtil.i("videoDetails", "courseId:" + courseId);
        detailsVideoBiz.addVideoLike(mobnub,courseId, new Response.Listener<AddVideoLikeResponse>() {
            @Override
            public void onResponse(AddVideoLikeResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    detailView.addVideoLikeSuccess();

                } else {
                    detailView.addVideoLikeFaild();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i(Tag, "addVideoLike error:" + error.toString());
                detailView.addVideoLikeFaild();
            }
        }, Tag);


    }

}
