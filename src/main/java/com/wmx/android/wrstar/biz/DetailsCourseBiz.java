package com.wmx.android.wrstar.biz;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.AddCommentLikeResponse;
import com.wmx.android.wrstar.biz.response.AddVideoLikeResponse;
import com.wmx.android.wrstar.biz.response.BaseResponse;
import com.wmx.android.wrstar.biz.response.DetailsCourseResponse;
import com.wmx.android.wrstar.biz.response.SendVideoCommentResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.entities.DetailViewInfo;
import com.wmx.android.wrstar.entities.VideoComment;
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


/**
 * Created by Administrator on 2016/5/10.
 */
public class DetailsCourseBiz {


    /**
     * TAG.
     */
    public static final String TAG = "VideoBiz";

    private static volatile DetailsCourseBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private DetailsCourseBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static DetailsCourseBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (DetailsCourseBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new DetailsCourseBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getDetailsCourseInfo(String mobnum, String liveid, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.DETAILS_LIVE;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("userid", mobnum);
        bodyParams.put("liveid", liveid);
        params.put("body", bodyParams);
        Gson gson =  new Gson();
        final GsonRequest<DetailsCourseResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                DetailsCourseResponse.class, responseListener, errorListener);
        LogUtil.i("getDetailsCourseInfo:"+gson.toJson(params).toString());
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

        sRequestManager.add(gsonRequest, tag);
    }










}
