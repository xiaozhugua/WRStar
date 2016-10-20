package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.VideoPageResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.ParamsUtil;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/9.
 */
public class VideoBiz {

    /**
     * TAG.
     */
    public static final String TAG = "VideoBiz";

    private static volatile VideoBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 精彩视频相关业务.
     */
    private VideoBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static VideoBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (VideoBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new VideoBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getAllSeeVideo(Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.GET_ALLSEEVIDEO;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

//
        Gson gson =  new Gson();
        final GsonRequest<VideoPageResponse> gsonRequest = new GsonRequest<>(sContext, url,  gson.toJson(params),
                VideoPageResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);





    }



}
