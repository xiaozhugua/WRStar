package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.ShareSuccessResponse;
import com.wmx.android.wrstar.constants.Urls;
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
public class ShareSuccessBiz {


    /**
     * TAG.
     */
    public static final String TAG = "ShareSuccessBiz";

    private static volatile ShareSuccessBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private ShareSuccessBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static ShareSuccessBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (ShareSuccessBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new ShareSuccessBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void shareSuccess(String liveid, String token,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.SHARESUCCESS;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("liveid", liveid);
        bodyParams.put("token", token);
        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<ShareSuccessResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                ShareSuccessResponse.class, responseListener, errorListener);
        LogUtil.i("分享  =========:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
