package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.AddCommentLikeResponse;
import com.wmx.android.wrstar.biz.response.ClassifyVideoResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ParamsUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ClassifyVideBiz {


    /**
     * TAG.
     */
    public static final String TAG = "ClassifyVideBiz";

    private static volatile ClassifyVideBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private ClassifyVideBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static ClassifyVideBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (ClassifyVideBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new ClassifyVideBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }


    public void getClassifyVideo(String typeid, String sortid, boolean isshowextra, int index, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.WONDERFUL_VIDEO;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();


        bodyParams.put("typeid", typeid);
        bodyParams.put("sortid", sortid);
        bodyParams.put("index", index + "");

        bodyParams.put("isshowextra", isshowextra + "");
        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<ClassifyVideoResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                ClassifyVideoResponse.class, responseListener, errorListener);
        LogUtil.i(new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }

}
