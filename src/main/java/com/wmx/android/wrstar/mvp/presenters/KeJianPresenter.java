package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.KejianBiz;
import com.wmx.android.wrstar.biz.response.KeJianResponse;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.KeJianView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class KeJianPresenter extends BasePresenter {

    public final String Tag = "KeJianPresenter";

    private KeJianView mKeJianView;

    private KejianBiz  mKejianBiz;
    private ICommonView commonView;

    public KeJianPresenter(ICommonView commonView, KeJianView mKeJianView) {
        super(commonView);
        this.mKeJianView = mKeJianView;
        this.commonView = commonView;
        mKejianBiz = KejianBiz.getInstance(WRStarApplication.getContext());
    }


    public void getKeJianList(String liveid) {

        mCommonView.showDialog("正在加载...");
        String mobnum ="";
        if (WRStarApplication.getUser() != null) {
            //  mCommonView.login();
            mobnum= getNum() ;
        }

        mKejianBiz.getKeJianInfo(mobnum, liveid, "token",new Response.Listener<KeJianResponse>() {
            @Override
            public void onResponse(KeJianResponse response) {
                mKeJianView.getKeJianListSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                commonView.netError();
                mKeJianView.getKeJianWareListFailed();
            }
        }, Tag);

    }
}
