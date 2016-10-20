package com.wmx.android.wrstar.mvp.presenters;

import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.GongxianBiz;
import com.wmx.android.wrstar.mvp.views.GongxianView;
import com.wmx.android.wrstar.mvp.views.ICommonView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class GongxianPresenter extends BasePresenter {

    public final String Tag = "GongxianPresenter";

    private GongxianView GongxianView;

    private GongxianBiz GongxianBiz;
    private ICommonView commonView;

    public GongxianPresenter(ICommonView commonView, GongxianView GongxianView) {
        super(commonView);
        this.GongxianView = GongxianView;
        this.commonView = commonView;
        GongxianBiz = com.wmx.android.wrstar.biz.GongxianBiz.getInstance(WRStarApplication.getContext());
    }


    public void getGongxianList() {

        mCommonView.showDialog("正在加载...");
        String mobnum ="";
        if (WRStarApplication.getUser() != null) {
            //  mCommonView.login();
            mobnum= getNum() ;
        }


        String token = "" ;

//        GongxianBiz.getGongxianList(mobnum, index+"" ,token, new Response.Listener<FirstPageResponse>() {
//            @Override
//            public void onResponse(FirstPageResponse response) {
//
//                if (response.getResult().equals(ServerCodes.SUCCESS)) {
//                    GongxianView.getHomeListSuccess(response);
//
//                } else {
//                   // mCommonView.netError();
//                }
//                mCommonView.hideDialog();
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                commonView.netError();
//                GongxianView.getHomeListFailed();
//            }
//        }, Tag);

    }
}
