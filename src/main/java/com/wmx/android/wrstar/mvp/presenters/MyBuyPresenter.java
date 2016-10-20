package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.LiveRoomBiz;
import com.wmx.android.wrstar.biz.MyBuyBiz;
import com.wmx.android.wrstar.biz.response.LiveCourseWareResponse;
import com.wmx.android.wrstar.biz.response.MyBuyRewardResponse;
import com.wmx.android.wrstar.biz.response.SendGiftResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.ILiveRoomView;
import com.wmx.android.wrstar.mvp.views.IMyBuyView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MyBuyPresenter extends BasePresenter {

    public final String Tag = "MyBuyPresenter";

    private IMyBuyView myBuyView;

    private MyBuyBiz myBuyBiz;
    private ICommonView commonView;

    public MyBuyPresenter(ICommonView commonView, IMyBuyView myBuyView) {
        super(commonView);
        this.myBuyView = myBuyView;
        this.commonView = commonView;
        myBuyBiz = MyBuyBiz.getInstance(WRStarApplication.getContext());
    }


    public void getRewardRecord(int index) {

        mCommonView.showDialog("正在加载...");

        if (WRStarApplication.getUser() == null) {
            //  mCommonView.login();
            return;
        }

        String mobnum = getNum();
        String token = "" ;

        myBuyBiz.getRewardRecord(mobnum, index+"" ,token, new Response.Listener<MyBuyRewardResponse>() {
            @Override
            public void onResponse(MyBuyRewardResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    myBuyView.getMyRewardSuccess(response.body.rewards,response.body.next );

                } else {
                   // mCommonView.netError();
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



}
