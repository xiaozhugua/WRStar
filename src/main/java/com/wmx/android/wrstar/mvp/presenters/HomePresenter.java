package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.FirstPageBiz;
import com.wmx.android.wrstar.biz.response.FirstPageResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.IFirstPageView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class HomePresenter extends BasePresenter {

    public final String Tag = "HomePresenter";

    private IFirstPageView firstPageView;

    private FirstPageBiz firstPageBiz;
    private ICommonView commonView;

    public HomePresenter(ICommonView commonView, IFirstPageView firstPageView) {
        super(commonView);
        this.firstPageView = firstPageView;
        this.commonView = commonView;
        firstPageBiz = FirstPageBiz.getInstance(WRStarApplication.getContext());
    }


    public void getHomeList(int index) {

        mCommonView.showDialog("正在加载...");
        String mobnum ="";
        if (WRStarApplication.getUser() != null) {
            //  mCommonView.login();
            mobnum= getNum() ;
        }


        String token = "" ;

        firstPageBiz.getHomeList(mobnum, index+"" ,token, new Response.Listener<FirstPageResponse>() {
            @Override
            public void onResponse(FirstPageResponse response) {

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    firstPageView.getHomeListSuccess(response);

                } else {
                   // mCommonView.netError();
                }
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
