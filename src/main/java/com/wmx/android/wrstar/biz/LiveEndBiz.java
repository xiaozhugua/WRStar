package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.LiveEndResponse;
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
public class LiveEndBiz {


    /**
     * TAG.
     */
    public static final String TAG = "LiveEndBiz";

    private static volatile LiveEndBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private LiveEndBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static LiveEndBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LiveEndBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new LiveEndBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    /**********课件接口**********/
    public void liveEnd(String liveid, String token,Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.LIVEEND;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("liveid", liveid);
        bodyParams.put("token", token);
        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<LiveEndResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                LiveEndResponse.class, responseListener, errorListener);
        LogUtil.i("终止直播  =========:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
