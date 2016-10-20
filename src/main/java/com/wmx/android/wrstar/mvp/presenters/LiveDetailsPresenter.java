package com.wmx.android.wrstar.mvp.presenters;

import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.DetailsCourseBiz;
import com.wmx.android.wrstar.biz.DetailsVideoBiz;
import com.wmx.android.wrstar.biz.FansBiz;
import com.wmx.android.wrstar.biz.response.BaseResponse;
import com.wmx.android.wrstar.biz.response.DetailsCourseResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.entities.DetailViewInfo;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.IDetailView;
import com.wmx.android.wrstar.mvp.views.IDetailsCourse;

/**
 * Created by Administrator on 2016/5/23.
 */
public class LiveDetailsPresenter extends BasePresenter {

    public final String Tag = "LiveDetailsPresenter";

    private IDetailsCourse detailsCourse;

    private DetailsCourseBiz detailsCourseBiz;
    private FansBiz fansBiz;

    public LiveDetailsPresenter(ICommonView commonView, IDetailsCourse detailsCourse) {
        super(commonView);
        this.detailsCourse = detailsCourse;
        detailsCourseBiz = DetailsCourseBiz.getInstance(WRStarApplication.getContext());
        fansBiz = FansBiz.getInstance(WRStarApplication.getContext());
    }


    /**
     * @param mobnum 手机号
     * @param liveid 直播房间号
     */
    public void getDetailsCourseInfo(String mobnum, String liveid) {


        detailsCourseBiz.getDetailsCourseInfo(mobnum, liveid, new Response.Listener<DetailsCourseResponse>() {
            @Override
            public void onResponse(DetailsCourseResponse response) {

                //   detailsCourse.getDetailViewSuccess(response);

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    detailsCourse.getCourseDetailsSuccess(response);
                } else {
                    detailsCourse.getCourseDetailsFailed(response.getResultDesc());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, Tag);

    }

    public void bookLive(String liveid, boolean book) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
        mCommonView.showDialog("正在预约...");
        String mobnum = getNum();

        detailsCourseBiz.bookLive(mobnum, liveid, book, new Response.Listener<BaseResponse>() {
            @Override
            public void onResponse(BaseResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    detailsCourse.bookLiveSuccess();

                } else if (response.getResult().equals(ServerCodes.ALREADY_BOOK)) {
                    mCommonView.showToast(response.getResultDesc());
                } else {
                    mCommonView.netError();
                }


                mCommonView.hideDialog();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
            }
        }, Tag);

    }

    public void setFocus(String otherId, final boolean isFocus) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
        mCommonView.showDialog("正在加载...");

        fansBiz.setFocus(getNum(), otherId, getToken(),isFocus, new Response.Listener<BaseResponse>() {
            @Override
            public void onResponse(BaseResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    if (isFocus){
                        detailsCourse.setfocuseSuccess();
                    }else{
                        detailsCourse.setfocuseCancelSuccess();
                    }



                } else {
                    mCommonView.showToast(response.getResultDesc());
                  //  mCommonView.netError();
                }
                mCommonView.hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
            }
        }, Tag);

    }

}
