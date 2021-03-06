package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.wmx.android.wrstar.biz.response.CreateLiveResponse;
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
public class CreateLiveBiz {



    /**
     * TAG.
     */
    public static final String TAG = "CreateLiveBiz";

    private static volatile CreateLiveBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private CreateLiveBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static CreateLiveBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (CreateLiveBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new CreateLiveBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void createLive(String token, String name, String picture, JsonArray tagIdParams, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.CREATELiVE;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, Object> bodyParams = new HashMap<>();

//        LogUtil.e("tagIdParams eee "+tagIdParams);

        bodyParams.put("tags", tagIdParams);
        bodyParams.put("token", token);
        bodyParams.put("name", name);
        bodyParams.put("picture", picture);

        params.put("body",  bodyParams);

        Gson gson =  new Gson();
        final GsonRequest<CreateLiveResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                CreateLiveResponse.class, responseListener, errorListener);
        LogUtil.i("创建直播请求json:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
