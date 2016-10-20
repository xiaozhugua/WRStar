package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.LiveEndBiz;
import com.wmx.android.wrstar.biz.response.LiveEndResponse;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.LiveEndView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class LiveEndPresenter extends BasePresenter {

    public final String Tag = "LiveEndPresenter";

    private LiveEndView mLiveEndView;

    private LiveEndBiz  mLiveEndBiz;
    private ICommonView commonView;

    public LiveEndPresenter(ICommonView commonView, LiveEndView mLiveEndView) {
        super(commonView);
        this.mLiveEndView = mLiveEndView;
        this.commonView = commonView;
        mLiveEndBiz = LiveEndBiz.getInstance(WRStarApplication.getContext());
    }


    public void liveEnd(String liveid,String token) {

//        mCommonView.showDialog("正在加载...");
//        String mobnum ="";
//        if (WRStarApplication.getUser() != null) {
//            //  mCommonView.login();
//            mobnum= getNum() ;
//        }

        mLiveEndBiz.liveEnd(liveid, token,new Response.Listener<LiveEndResponse>() {
            @Override
            public void onResponse(LiveEndResponse response) {
                mLiveEndView.LiveEndSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLiveEndView.LiveEndFailed(error.toString());
            }
        }, Tag);
    }
}
