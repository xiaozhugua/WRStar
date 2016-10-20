package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.BaseResponse;
import com.wmx.android.wrstar.biz.response.FansResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ParamsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/7.
 */
public class FansBiz {
    public final static int MY_FOLLOW = 1;  //我关注的
    public final static int MY_FANS = 2;    //我的粉丝
    public final static int FOCUS = 3;      //关注
    public final static int FOCUS_CANCEL = 4;      //取消关注
    /**
     * TAG.
     */
    public static final String TAG = "FansBiz";

    private static volatile FansBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private FansBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static FansBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (FansBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new FansBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }


    public void setFocus(String mobNum, String focusNum, String token,boolean isFocus, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.FANS;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("userid", mobNum);

        bodyParams.put("otherid", focusNum);

        bodyParams.put("token", token);


        bodyParams.put("action", isFocus?FOCUS + "":FOCUS_CANCEL+"");
        bodyParams.put("token", token);

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<BaseResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                BaseResponse.class, responseListener, errorListener);
        LogUtil.i(gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }

    public void getMyFollow(String mobNum, String token, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.FANS;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("userid", mobNum);
        bodyParams.put("token", token);


        bodyParams.put("action", MY_FOLLOW + "");
        bodyParams.put("token", token);

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<BaseResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                BaseResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);
    }

    public void getMyFans(String mobNum, String token,int index, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.FANS;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("userid", mobNum);

        bodyParams.put("token", token);

        bodyParams.put("index", index+"");
        bodyParams.put("action", MY_FANS + "");
        bodyParams.put("token", token);
        bodyParams.put("need", 8+"");

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<FansResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                FansResponse.class, responseListener, errorListener);
        LogUtil.i(gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
