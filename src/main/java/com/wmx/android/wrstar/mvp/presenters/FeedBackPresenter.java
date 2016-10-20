package com.wmx.android.wrstar.mvp.presenters;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.FeedBackBiz;
import com.wmx.android.wrstar.biz.response.MyBuyRewardResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.IFeedBackView;

/**
 * Created by Administrator on 2016/6/7.
 */
public class FeedBackPresenter  extends BasePresenter  {

    public final String Tag = "FeedBackPresenter";



    private FeedBackBiz feedBackBiz;
    private IFeedBackView feedBackView;

    public FeedBackPresenter(ICommonView commonView, IFeedBackView feedBackView) {
        super(commonView);
        this.feedBackView = feedBackView;

        feedBackBiz = FeedBackBiz.getInstance(WRStarApplication.getContext());
    }


    public void sendFeedBack(String content) {
        String mobnum ="";
        if (WRStarApplication.getUser() != null) {
            //  mCommonView.login();
            mobnum = getNum();
        }

        if (TextUtils.isEmpty(content) ||content.trim().equals("")){
            mCommonView.showToast("请输入反馈内容");
            return;
        }

        feedBackBiz.sendFeedBack(mobnum, content, new Response.Listener<MyBuyRewardResponse>() {
            @Override
            public void onResponse(MyBuyRewardResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    feedBackView.sendFeedBackSuccess();
                    mCommonView.showToast(response.getResultDesc());
                } else {
                    // mCommonView.netError();
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
