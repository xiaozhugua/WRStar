package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.MyVideoLiveResponse;
import com.wmx.android.wrstar.biz.response.MyVideoOndemandResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ParamsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MyVideoBiz {

    /**
     * TAG.
     */
    public static final String TAG = "MyVideoBiz";

    private static volatile MyVideoBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private MyVideoBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static MyVideoBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (MyVideoBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new MyVideoBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }


    public void getMyVideo(String mobnum, String index, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.MY_VIDEO;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        if (mobnum != null) {
            bodyParams.put("mobnum", mobnum);
        }
        bodyParams.put("index", index);
        bodyParams.put("action", "1");           // 1录播 2直播
        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<MyVideoOndemandResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                MyVideoOndemandResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);
    }


    public void getLive(String mobnum, String index, String subaction, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.MY_VIDEO;
        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        if (mobnum != null) {
            bodyParams.put("mobnum", mobnum);
        }
        bodyParams.put("index", index);
        bodyParams.put("action", "2");           // 1录播 2直播
        bodyParams.put("subaction", subaction);


        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<MyVideoLiveResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                MyVideoLiveResponse.class, responseListener, errorListener);
        sRequestManager.add(gsonRequest, tag);
        LogUtil.i("getLive:"+gson.toJson(params).toString());
    }


}
