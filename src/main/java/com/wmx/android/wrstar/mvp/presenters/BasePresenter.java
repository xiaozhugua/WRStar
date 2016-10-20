package com.wmx.android.wrstar.mvp.presenters;

import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.utils.PreferenceUtils;

/**
 * Created by wubiao on 2016/1/20.
 *
 * Des:
 */
public abstract class BasePresenter {

    public ICommonView mCommonView;

    protected BasePresenter(ICommonView commonView){
        mCommonView = commonView;
    }


    public String getNum(){
        return WRStarApplication.getUser().mobnum;
    }


    public String getToken(){
       return  PreferenceUtils.getToken(WRStarApplication.getContext());
    }


}
