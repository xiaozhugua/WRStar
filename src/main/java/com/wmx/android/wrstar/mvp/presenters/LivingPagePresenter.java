package com.wmx.android.wrstar.mvp.presenters;

import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.LivePageBiz;
import com.wmx.android.wrstar.biz.response.BaseResponse;
import com.wmx.android.wrstar.biz.response.LiveGiftResponse;
import com.wmx.android.wrstar.biz.response.LivePageResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.ILivePageView;
import com.wmx.android.wrstar.utils.PreferenceUtils;

/**
 * Created by Administrator on 2016/5/5.
 */
public class LivingPagePresenter extends BasePresenter {

    public final String Tag = "LiveDetailsPresenter";

    private ILivePageView livePageView;

    private LivePageBiz livePageBiz;
    private ICommonView commonView;

    public LivingPagePresenter(ICommonView commonView, ILivePageView livePageView) {
        super(commonView);
        this.livePageView = livePageView;
        this.commonView = commonView;
        livePageBiz = LivePageBiz.getInstance(WRStarApplication.getContext());
    }


    /**
     *
     */
    public void getDetailsCourseInfo(int index) {

        String mobnum = WRStarApplication.getUser() != null ? WRStarApplication.getUser().mobnum : null;

        livePageBiz.getLiveList(mobnum,index, new Response.Listener<LivePageResponse>() {
            @Override
            public void onResponse(LivePageResponse response) {


                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    livePageView.getLiveListSuccess(response);
                } else {
                    commonView.netError();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                livePageView.getLiveListFailed("");
            }
        }, Tag);

    }

    public void bookLive(String liveid, boolean book, final View tv) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
        mCommonView.showDialog("正在预约...");
        String mobnum = getNum();

        livePageBiz.bookLive(mobnum, liveid, book, new Response.Listener<BaseResponse>() {
            @Override
            public void onResponse(BaseResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    livePageView.bookLiveSuccess(tv);

                } else if(response.getResult().equals(ServerCodes.ALREADY_BOOK)){
                    mCommonView.showToast(response.getResultDesc());
                }else {
                    livePageView.getLiveListFailed(response.getResultDesc());
                }
                mCommonView.hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                commonView.netError();
            }
        }, Tag);

    }

    public void getGiftList() {


        livePageBiz.getGiftList(new Response.Listener<LiveGiftResponse>() {
            @Override
            public void onResponse(LiveGiftResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {

                    Gson gson =new Gson();
                    String json = gson.toJson(response);

                    PreferenceUtils.setLiveGift(WRStarApplication.getContext(), json);
                } else {

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  commonView.netError();
            }
        }, Tag);


    }

}
