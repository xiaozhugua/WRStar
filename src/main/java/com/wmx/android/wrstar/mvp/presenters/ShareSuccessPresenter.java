package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.ShareSuccessBiz;
import com.wmx.android.wrstar.biz.response.ShareSuccessResponse;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.ShareSuccessView;

/**
 */
public class ShareSuccessPresenter extends BasePresenter {

    public final String Tag = "ShareSuccessPresenter";

    private ShareSuccessView mShareSuccessView;

    private ShareSuccessBiz  mShareSuccessBiz;
    private ICommonView commonView;

    public ShareSuccessPresenter(ICommonView commonView, ShareSuccessView mShareSuccessView) {
        super(commonView);
        this.mShareSuccessView = mShareSuccessView;
        this.commonView = commonView;
        mShareSuccessBiz = ShareSuccessBiz.getInstance(WRStarApplication.getContext());
    }


    public void share(String liveid,String token) {

//        mCommonView.showDialog("分享中...");
        if (WRStarApplication.getUser() == null) {
              mCommonView.login();
        }

        mShareSuccessBiz.shareSuccess( liveid, token,new Response.Listener<ShareSuccessResponse>() {
            @Override
            public void onResponse(ShareSuccessResponse response) {
                mShareSuccessView.ShareSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                commonView.netError();
                mShareSuccessView.ShareFailed(error.toString());
            }
        }, Tag);
    }
}
