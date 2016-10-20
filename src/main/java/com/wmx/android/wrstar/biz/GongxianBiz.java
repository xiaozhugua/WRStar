package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.wmx.android.wrstar.net.RequestManager;

/**
 * Created by Administrator on 2016/5/23.
 */


/**
 * Created by Administrator on 2016/5/10.
 */
public class GongxianBiz {


    /**
     * TAG.
     */
    public static final String TAG = "GongxianBiz";

    private static volatile GongxianBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private GongxianBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static GongxianBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (GongxianBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new GongxianBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }
//
//    /**********课件接口**********/
//    public void getKeJianInfo(String mobnum, String liveid, String token,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {
//
//        final String url = Urls.LIVE_COURSEWARE;
//
//        final Map<String, Object> params = ParamsUtil.getBasicInfo();
//
//        Map<String, String> bodyParams = new HashMap<>();
//
//        bodyParams.put("userid", mobnum);
//        bodyParams.put("liveid", liveid);
//        bodyParams.put("token", token);
//        params.put("body", bodyParams);
//        Gson gson = new Gson();
//        final GsonRequest<KeJianResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
//                KeJianResponse.class, responseListener, errorListener);
//        LogUtil.i("课件  =========:"+new JSONObject(params).toString());
//        sRequestManager.add(gsonRequest, tag);
//    }
}
