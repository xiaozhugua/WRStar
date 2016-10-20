package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.wmx.android.wrstar.net.RequestManager;

/**
 * Created by Administrator on 2016/5/23.
 */


/**
 * Created by Administrator on 2016/5/10.
 */
public class GoodsBiz {


    /**
     * TAG.
     */
    public static final String TAG = "GoodsBiz";

    private static volatile GoodsBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private GoodsBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static GoodsBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (GoodsBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new GoodsBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    /**********没有接口**********/
    public void getGoodsInfo(Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

//        final String url = Urls.DETAILS_LIVE;
//
//        final Map<String, Object> params = ParamsUtil.getBasicInfo();
//
//        Map<String, String> bodyParams = new HashMap<>();
//
//        bodyParams.put("userid", mobnum);
//        bodyParams.put("liveid", liveid);
//        params.put("body", bodyParams);
//        Gson gson =  new Gson();
//        final GsonRequest<DetailsCourseResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
//                DetailsCourseResponse.class, responseListener, errorListener);
//        LogUtil.i("getDetailsCourseInfo:"+gson.toJson(params).toString());
//        sRequestManager.add(gsonRequest, tag);
    }
}
