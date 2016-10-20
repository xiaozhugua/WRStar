package com.wmx.android.wrstar.mvp.presenters;

import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.AccountBiz;
import com.wmx.android.wrstar.biz.FansBiz;
import com.wmx.android.wrstar.biz.LivePageBiz;
import com.wmx.android.wrstar.biz.LiveRoomBiz;
import com.wmx.android.wrstar.biz.response.BaseResponse;
import com.wmx.android.wrstar.biz.response.FansResponse;
import com.wmx.android.wrstar.biz.response.LiveCourseWareResponse;
import com.wmx.android.wrstar.biz.response.LivePageResponse;
import com.wmx.android.wrstar.biz.response.OtherUserInfoResponse;
import com.wmx.android.wrstar.biz.response.RandomResponse;
import com.wmx.android.wrstar.biz.response.SendGiftResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.ILivePageView;
import com.wmx.android.wrstar.mvp.views.ILiveRoomView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class LiveRoomPresenter extends BasePresenter {

    public final String Tag = "LiveRoomPresenter";

    private ILiveRoomView liveRoomView;

    private LiveRoomBiz liveRoomBiz;
    private ICommonView commonView;

    private FansBiz fansBiz;
    private AccountBiz accountBiz;

    public LiveRoomPresenter(ICommonView commonView, ILiveRoomView liveRoomView) {
        super(commonView);
        this.liveRoomView = liveRoomView;
        this.commonView = commonView;
        liveRoomBiz = LiveRoomBiz.getInstance(WRStarApplication.getContext());
        fansBiz= FansBiz.getInstance(WRStarApplication.getContext());
        accountBiz =AccountBiz.getInstance(WRStarApplication.getContext());
    }


    public void getLiveCourseWare(String liveid) {

        if (WRStarApplication.getUser() == null) {
            //  mCommonView.login();
            return;
        }

        String mobnum = getNum();

        liveRoomBiz.getCourseWare(mobnum, liveid, "token", new Response.Listener<LiveCourseWareResponse>() {
            @Override
            public void onResponse(LiveCourseWareResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    liveRoomView.getCourseWareSuccess(response.body.coursewares);

                } else {
                    mCommonView.netError();
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

    public void getRandomBean() {


        liveRoomBiz.getRandomBean(new Response.Listener<RandomResponse>() {
            @Override
            public void onResponse(RandomResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    liveRoomView.getRandomBeanSuccess(response.body.count);

                } else {
                    mCommonView.netError();
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


    public void sendGift(String liveid, String roomid, String giftid, String count, final String starbean) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }

        String mobnum = getNum();
        mCommonView.showDialog("正在加载..,");
        liveRoomBiz.sendGift(mobnum, liveid, roomid, giftid, count, starbean, new Response.Listener<SendGiftResponse>() {
                    @Override
                    public void onResponse(SendGiftResponse response) {

                        if (response.getResult().equals(ServerCodes.SUCCESS)) {
                            mCommonView.showToast("打赏成功");
                            if (starbean == null) {
                                liveRoomView.sendGiftSuccess();
                                WRStarApplication.getUser().starcoins = response.body.starbean;
                            } else {
                                liveRoomView.sendStarBeanSuccess();
                            }

                        } else if (response.getResult().equals(ServerCodes.NO_ENOUGHT_STARBEAN)) {
                            mCommonView.showToast(response.getResultDesc());
                        } else {
                            mCommonView.netError();
                        }


                        mCommonView.hideDialog();

                    }
                }

                , new Response.ErrorListener()

                {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        commonView.netError();
                    }
                }

                , Tag);

    }


    public void getFans(String liveManId,int index) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
        mCommonView.showDialog("正在加载...");

        fansBiz.getMyFans(liveManId, getToken(), index,new Response.Listener<FansResponse>() {
            @Override
            public void onResponse(FansResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    liveRoomView.getFansSuccess(response);
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
                        liveRoomView.setfocuseSuccess();
                    }else{
                        liveRoomView.setfocuseCancelSuccess();
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
    public void getOtherUserInfo(String otherUserId) {


        mCommonView.showDialog("正在加载...");

        accountBiz.getOtherUserInfo(getNum(),otherUserId ,new Response.Listener<OtherUserInfoResponse>() {
            @Override
            public void onResponse(OtherUserInfoResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    liveRoomView.getOtherUserInfoSuccess(response);
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
