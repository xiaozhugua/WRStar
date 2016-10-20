package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.DetailsCourseResponse;
import com.wmx.android.wrstar.biz.response.LiveCourseWareResponse;
import com.wmx.android.wrstar.biz.response.RandomResponse;
import com.wmx.android.wrstar.biz.response.SendGiftResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ParamsUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/27.
 */
public class LiveRoomBiz {


    /**
     * TAG.
     */
    public static final String TAG = "LiveRoomBiz";

    private static volatile LiveRoomBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;


    private LiveRoomBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static LiveRoomBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LiveRoomBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new LiveRoomBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getCourseWare(String mobnum, String liveid, String token, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.LIVE_COURSEWARE;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("userid", mobnum);
        bodyParams.put("liveid", liveid);
        bodyParams.put("token", token);
        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<LiveCourseWareResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                LiveCourseWareResponse.class, responseListener, errorListener);
        LogUtil.i(new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }


    public void sendGift(String mobnum, String liveid, String roomid ,String giftid,String count,String starbean, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.LIVE_SEND_REWARD;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("userid", mobnum);
        bodyParams.put("liveid", liveid);
        bodyParams.put("roomid", roomid);

        if (starbean==null){
            bodyParams.put("giftid", giftid);
            bodyParams.put("count", count);
        }else{
            bodyParams.put("starbean", starbean);
        }

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<SendGiftResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                SendGiftResponse.class, responseListener, errorListener);
        LogUtil.i(new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }



    public void getRandomBean(Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.GIFT_RANDOM;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();


        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<RandomResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                RandomResponse.class, responseListener, errorListener);
        LogUtil.i(new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }

}
