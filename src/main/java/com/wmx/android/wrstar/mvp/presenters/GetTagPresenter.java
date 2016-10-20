package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.GetHotTagBiz;
import com.wmx.android.wrstar.biz.response.GetHotTagResponse;
import com.wmx.android.wrstar.mvp.views.GetTagView;
import com.wmx.android.wrstar.mvp.views.ICommonView;

/**
 */
public class GetTagPresenter extends BasePresenter {

    public final String Tag = "GetTagPresenter";

    private GetTagView mGetTagView;

    private GetHotTagBiz mGetHotTagBiz;
    private ICommonView  commonView;

    public GetTagPresenter(ICommonView commonView, GetTagView mGetTagView) {
        super(commonView);
        this.mGetTagView = mGetTagView;
        this.commonView = commonView;
        mGetHotTagBiz = GetHotTagBiz.getInstance(WRStarApplication.getContext());
    }

    public void getHotTag() {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
        mCommonView.showDialog("正在获取热门标签...");

        mGetHotTagBiz.getHotTag(new Response.Listener<GetHotTagResponse>(){
            @Override
            public void onResponse(GetHotTagResponse response) {
                mGetTagView.getHotTagSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mGetTagView.getHotTagFail(error.toString());
                commonView.netError();
            }
        }, Tag);
    }
}
