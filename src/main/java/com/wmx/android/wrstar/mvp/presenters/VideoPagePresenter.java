package com.wmx.android.wrstar.mvp.presenters;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.biz.ClassifyVideBiz;
import com.wmx.android.wrstar.biz.response.ClassifyVideoResponse;
import com.wmx.android.wrstar.mvp.views.IClassifyView;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.utils.LogUtil;

/**
 * Created by Administrator on 2016/5/11.
 */
public class VideoPagePresenter extends BasePresenter {
    private ClassifyVideBiz classifyVideBiz;
    private IClassifyView classifyView;


    public VideoPagePresenter(ICommonView commonView, Context context, IClassifyView classifyView) {
        super(commonView);
        classifyVideBiz = ClassifyVideBiz.getInstance(context);
        this.classifyView = classifyView;
    }


    public void getClassifyVideo(String typeId, String sortID, boolean isLoadExtra, int index) {


        classifyVideBiz.getClassifyVideo(typeId, sortID, isLoadExtra, index,
        new Response.Listener<ClassifyVideoResponse>() {
            @Override
            public void onResponse(final ClassifyVideoResponse response) {
                classifyView.getClassifySuccess(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.showToast("检查网络链接:" + error.toString());
                LogUtil.i("videoBiz", "error:" + error.toString());
            }
        }, "videoBiz");
    }


}
