package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.GetHotTagResponse;
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
public class GetHotTagBiz {
    /**
     * TAG.
     */
    public static final String TAG = "GetHotTagBiz";

    private static volatile GetHotTagBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private GetHotTagBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static GetHotTagBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (GetHotTagBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new GetHotTagBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }


    public void getHotTag(Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.GETHOTTAG;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, Object> bodyParams = new HashMap<>();

        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<GetHotTagResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                GetHotTagResponse.class, responseListener, errorListener);
        LogUtil.i("获取热门标签请求json:"+gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
