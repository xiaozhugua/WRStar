package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.CreateLiveBiz;
import com.wmx.android.wrstar.biz.response.CreateLiveResponse;
import com.wmx.android.wrstar.mvp.views.CreateLiveView;
import com.wmx.android.wrstar.mvp.views.ICommonView;

/**
 * Created by Administrator on 2016/5/5.
 */
public class CreateLivePresenter extends BasePresenter {

    public final String Tag = "CreateLivePresenter";

    private CreateLiveView mCreateLiveView;

    private CreateLiveBiz mCreateLiveBiz;
    private ICommonView   commonView;

    public CreateLivePresenter(ICommonView commonView, CreateLiveView mCreateLiveView) {
        super(commonView);
        this.mCreateLiveView = mCreateLiveView;
        this.commonView = commonView;
        mCreateLiveBiz = CreateLiveBiz.getInstance(WRStarApplication.getContext());
    }

    public void createLive(String token,String name,String picture,JsonArray tagIdParams) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
//        mCommonView.showDialog("正在创建直播...");

        mCreateLiveBiz.createLive(token, name, picture,tagIdParams, new Response.Listener<CreateLiveResponse>(){
            @Override
            public void onResponse(CreateLiveResponse response) {
                mCreateLiveView.createLiveSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mCreateLiveView.createLiveFail(error.toString());
                commonView.netError();
            }
        }, Tag);

    }
}
