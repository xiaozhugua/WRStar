package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.ManagetInfoLiveBiz;
import com.wmx.android.wrstar.biz.response.ManagerLiveResponse;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.ManagerLiveInfoView;

/**
 * Created by Administrator on 2016/5/5.
 */
public class ManagerLivePresenter extends BasePresenter {

    public final String Tag = "ManagerLivePresenter";

    private ManagerLiveInfoView mManagerLiveInfoView;

    private ManagetInfoLiveBiz mManagerLiveBiz;
    private ICommonView   commonView;

    public ManagerLivePresenter(ICommonView commonView, ManagerLiveInfoView mManagerLiveInfoView) {
        super(commonView);
        this.mManagerLiveInfoView = mManagerLiveInfoView;
        this.commonView = commonView;
        mManagerLiveBiz = ManagetInfoLiveBiz.getInstance(WRStarApplication.getContext());
    }

    public void managerLiveInfo(String action,String token,String name,String picture,JsonArray tagIdParams) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
//        mCommonView.showDialog("正在创建直播...");

        mManagerLiveBiz.managerLiveInfo(action,token, name, picture,tagIdParams, new Response.Listener<ManagerLiveResponse>(){
            @Override
            public void onResponse(ManagerLiveResponse response) {
                mManagerLiveInfoView.modifyTagSuccess(response);
                mManagerLiveInfoView.modifyPicSuccess(response);
                mManagerLiveInfoView.modifyTitleNameSuccess(response);
                mManagerLiveInfoView.getLiveInfoSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mManagerLiveInfoView.modifyPicFailed(error.toString());
                mManagerLiveInfoView.modifyTagFailed(error.toString());
                mManagerLiveInfoView.modifyTitleNameFailed(error.toString());
                mManagerLiveInfoView.getLiveInfoFailed(error.toString());
                commonView.netError();
            }
        }, Tag);

    }
}
