package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.HomePageBiz;
import com.wmx.android.wrstar.biz.response.HomePageResponse;
import com.wmx.android.wrstar.mvp.views.HomePageView;
import com.wmx.android.wrstar.mvp.views.ICommonView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class HomePagePresenter extends BasePresenter {

    public final String Tag = "HomePagePresenter";

    private HomePageView firstPageView;

    private HomePageBiz firstPageBiz;
    private ICommonView commonView;

    public HomePagePresenter(ICommonView commonView, HomePageView firstPageView) {
        super(commonView);
        this.firstPageView = firstPageView;
        this.commonView = commonView;
        firstPageBiz = HomePageBiz.getInstance(WRStarApplication.getContext());
    }


    public void getHomeList(String token) {

        mCommonView.showDialog("正在加载...");


        firstPageBiz.getHomeList(token, new Response.Listener<HomePageResponse>() {
            @Override
            public void onResponse(HomePageResponse response) {

//                if (response.getResult().equals(ServerCodes.SUCCESS)) {
//                    firstPageView.getHomeListSuccess(response);
//
//                } else {
//                   // mCommonView.netError();
//                }
                firstPageView.getHomeListSuccess(response);
                mCommonView.hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                commonView.netError();
                firstPageView.getHomeListFailed();
            }
        }, Tag);

    }
}
