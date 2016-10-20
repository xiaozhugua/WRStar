package com.wmx.android.wrstar.mvp.presenters;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.CreateTagBiz;
import com.wmx.android.wrstar.biz.response.CreateTagResponse;
import com.wmx.android.wrstar.mvp.views.CreateTagView;
import com.wmx.android.wrstar.mvp.views.ICommonView;

/**
 * Created by Administrator on 2016/5/5.
 */
public class CreateTagPresenter extends BasePresenter {

    public final String Tag = "CreateTagPresenter";

    private CreateTagView mCreateTagView;

    private CreateTagBiz mCreateTagBiz;
    private ICommonView  commonView;

    public CreateTagPresenter(ICommonView commonView, CreateTagView mCreateTagView) {
        super(commonView);
        this.mCreateTagView = mCreateTagView;
        this.commonView = commonView;
        mCreateTagBiz = CreateTagBiz.getInstance(WRStarApplication.getContext());
    }

    public void createTag(String tagName) {

        if (WRStarApplication.getUser() == null) {
            mCommonView.login();
            return;
        }
        mCommonView.showDialog("正在创建标签...");

        mCreateTagBiz.createTag(tagName, new Response.Listener<CreateTagResponse>(){
            @Override
            public void onResponse(CreateTagResponse response) {
                mCreateTagView.createTagSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mCreateTagView.createTagFail(error.toString());
                commonView.netError();
            }
        }, Tag);
    }
}
