package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.ALiPayResponse;
import com.wmx.android.wrstar.biz.response.WeChatPayResponse;
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
public class PayBiz {



    public final static int ACTION_BUYBEAN = 1; //买星豆
    public final static int ACTION_BUYVIP = 2;  //买会员


    public final static int PAY_ALI = 1;
    public final static int PAY_WECHAT = 2;

    /**
     * TAG.
     */
    public static final String TAG = "PayBiz";

    private static volatile PayBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private PayBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static PayBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (PayBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new PayBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }


    public void sendALiPayInfo(String mobnum, String money, int goodsType,  Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.PAY_ALI;
        //   final String url = Urls.PAY_ALI + "?uid=" + mobnum + "&money=" + money + "&product=" + PAY_PRODUCT+"&type="+type;
        final Map<String, Object> params = ParamsUtil.getBasicInfo();
        Map<String, String> bodyParams = new HashMap<>();
        Gson gson = new Gson();
        bodyParams.put("userid", mobnum);
        bodyParams.put("goods", goodsType + "");

         bodyParams.put("amount", money);
      //  bodyParams.put("amount", "0.1");
        params.put("body", bodyParams);
        final GsonRequest<ALiPayResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                ALiPayResponse.class, responseListener, errorListener);
        LogUtil.i("pay:" + gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }


    public void sendWeChatPayInfo(String mobnum, String money, int goodsType,  Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.PAY_WECHAT;
        //   final String url = Urls.PAY_ALI + "?uid=" + mobnum + "&money=" + money + "&product=" + PAY_PRODUCT+"&type="+type;
        final Map<String, Object> params = ParamsUtil.getBasicInfo();
        Map<String, String> bodyParams = new HashMap<>();
        Gson gson = new Gson();
        bodyParams.put("userid", mobnum);
        bodyParams.put("goods", goodsType + "");
     //   bodyParams.put("amount", "0.1");
       bodyParams.put("amount", money);
        params.put("body", bodyParams);
        final GsonRequest<WeChatPayResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                WeChatPayResponse.class, responseListener, errorListener);
        LogUtil.i("pay:" + gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }

}
