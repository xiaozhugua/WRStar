package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.BaseResponse;
import com.wmx.android.wrstar.biz.response.DetailsCourseResponse;
import com.wmx.android.wrstar.biz.response.LiveGiftResponse;
import com.wmx.android.wrstar.biz.response.LivePageResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.mvp.presenters.BasePresenter;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.net.CharstStringRequest;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ParamsUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 */
public class LivePageBiz  {



    /**
     * TAG.
     */
    public static final String TAG = "LivePageBiz";

    private static volatile LivePageBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private LivePageBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static LivePageBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LivePageBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new LivePageBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getLiveList(String mobnum,int index, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.LIVE_HALL;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("mobnum", mobnum);
        bodyParams.put("token", "rayisbigsb");
        bodyParams.put("index", index+"");

        params.put("body", bodyParams);
        Gson gson =  new Gson();
        final GsonRequest<LivePageResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                LivePageResponse.class, responseListener, errorListener);
        LogUtil.i("直播会堂请求json:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }



    public void bookLive(String mobnum,String liveid,boolean book,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.LIVE_BOOK;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("userid", mobnum);
        bodyParams.put("liveid",liveid );

        bodyParams.put("action", book?"1":"2"); //预约1，取消预约2
        bodyParams.put("token", "raysb");

        params.put("body", bodyParams);
        Gson gson =  new Gson();
        final GsonRequest<BaseResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                BaseResponse.class, responseListener, errorListener);
        LogUtil.i( "直播会堂请求json:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }



    public void getGiftList(Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.LIVE_REWARD;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        params.put("body", "body");
        Gson gson =  new Gson();
        final GsonRequest<LiveGiftResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                LiveGiftResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);
    }




}
