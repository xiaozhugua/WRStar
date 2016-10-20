package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.MyBuyRewardResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.ParamsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/7.
 */
public class FeedBackBiz {


    /**
     * TAG.
     */
    public static final String TAG = "FeedBackBiz";

    private static volatile FeedBackBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private FeedBackBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static FeedBackBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (FeedBackBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new FeedBackBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }


    public void sendFeedBack(String mobnum, String content, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.FEED_BACK;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        if (mobnum != null) {
            bodyParams.put("userid", mobnum);
        }
        bodyParams.put("content", content);
        params.put("body", bodyParams);
        Gson gson =  new Gson();
        final GsonRequest<MyBuyRewardResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                MyBuyRewardResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);
    }
}
