package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.HomePageResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ParamsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/8.
 */
public class HomePageBiz {


    /**
     * TAG.
     */
    public static final String TAG = "HomePageBiz";

    private static volatile HomePageBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private HomePageBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static HomePageBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (HomePageBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new HomePageBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }


    public void getHomeList(String token, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.HOMEPAGE_NEWS;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("token", token);

        params.put("body", bodyParams);
        Gson gson =  new Gson();
        final GsonRequest<HomePageResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                HomePageResponse.class, responseListener, errorListener);
        LogUtil.i("getHomeList:" + gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
