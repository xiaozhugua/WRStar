package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.MyVideoBiz;
import com.wmx.android.wrstar.biz.response.MyVideoLiveResponse;
import com.wmx.android.wrstar.biz.response.MyVideoOndemandResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.IMyVideo;
import com.wmx.android.wrstar.utils.LogUtil;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MyVideoPresenter extends BasePresenter {

    MyVideoBiz myVideoBiz;
    IMyVideo myVideo;

    public MyVideoPresenter(ICommonView commonView, IMyVideo myVideo) {
        super(commonView);
        this.myVideo = myVideo;
        myVideoBiz = MyVideoBiz.getInstance(WRStarApplication.getContext());

    }


    public void getLiveVideo(int subaction, int index) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }

        String mobnum = getNum();

        myVideoBiz.getLive(mobnum, index+"", subaction+"", new Response.Listener<MyVideoLiveResponse>() {
            @Override
            public void onResponse(final MyVideoLiveResponse response) {


                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    myVideo.getLiveVideoSuccess(response);
                }else if(response.getResult().equals(ServerCodes.NO_COLLECT_LIVE)) {
                    myVideo.getLiveVideoNull();
                        mCommonView.showToast(response.getResultDesc());
                }
                mCommonView.hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.showToast("检查网络链接:" + error.toString());
                LogUtil.i("ondemand", "error:" + error.toString());
            }
        }, "videoBiz");
    }

    public void getMyVideo(int index) {
        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }

        String mobnum = getNum();

        myVideoBiz.getMyVideo(mobnum, index + "", new Response.Listener<MyVideoOndemandResponse>() {
            @Override
            public void onResponse(final MyVideoOndemandResponse response) {


                LogUtil.i("ondemand", "response.getResult():" + response.getResult());

                if (response.getResult().equals(ServerCodes.SUCCESS)) {

                    LogUtil.i("ondemand", "myVideo.getOndemandVideoSuccess:" + response.body.items.size());
                    myVideo.getOndemandVideoSuccess(response);

                }
                mCommonView.hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.showToast("检查网络链接:" + error.toString());
                LogUtil.i("ondemand", "error:" + error.toString());
            }
        }, "videoBiz");

    }


}
