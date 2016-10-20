package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.GetAppPaiResponse;
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
public class GetAppPaiBiz {


    /**
     * TAG.
     */
    public static final String TAG = "GetAppPaiBiz";

    private static volatile GetAppPaiBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private GetAppPaiBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static GetAppPaiBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (GetAppPaiBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new GetAppPaiBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getAppPaiInfo(String token, String index, String action,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.GETAPPPAI;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("token", token);
        bodyParams.put("index", index);
        bodyParams.put("action", action);//action  //1是推荐 2是邀约

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<GetAppPaiResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                GetAppPaiResponse.class, responseListener, errorListener);
        LogUtil.i("排行榜  =========:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
