package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.CreateTagResponse;
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
public class CreateTagBiz {
    /**
     * TAG.
     */
    public static final String TAG = "CreateTagBiz";

    private static volatile CreateTagBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private CreateTagBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static CreateTagBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (CreateTagBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new CreateTagBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }


    public void createTag(String tagName, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.CreateTAG;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("name", tagName);

        params.put("createtagreqdto", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<CreateTagResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                CreateTagResponse.class, responseListener, errorListener);
        LogUtil.i("创建标签请求json:"+gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }
}
