package com.wmx.android.wrstar.biz;

import android.content.Context;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.ModifyPersonalInfoResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ParamsUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/16.
 */
public class PersonalBiz {
    public static final String NICKNIME = "1";
    public static final String SEX = "2";
    public static final String LOCATION = "3";
    public static final String SIGNATURE = "4";


    /**
     * TAG.
     */
    public static final String TAG = "PersonalBiz";

    private static volatile PersonalBiz sInstance;


    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 个人相关业务.
     */
    private PersonalBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static PersonalBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (PersonalBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new PersonalBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    /**
     *
     * @param mobnum 唯一标识ID
     * @param modifyInfo 更新信息
     * @param type  更新类目
     * @param responseListener
     * @param errorListener
     * @param tag
     */
    public void setUserInfo(String mobnum, String modifyInfo, String type, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.MODIFY_USER_INFO;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();
        Map<String, String> userinfo = new HashMap<>();


        if (type.equals(SEX)) {
            userinfo.put("sex", modifyInfo);
        } else if (type.equals(NICKNIME)) {
            userinfo.put("username", modifyInfo);
        } else if (type.equals(SIGNATURE)) {
            userinfo.put("signature", modifyInfo);
        } else if (type.equals(LOCATION)) {
            String[] pro_city = modifyInfo.split("-");
            userinfo.put("procode", pro_city[0]);
            userinfo.put("citycode", pro_city[1]);
        }
        userinfo.put("mobnum", mobnum);


        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("userinfo", userinfo);

        bodyParams.put("action", type);

        params.put("body", bodyParams);


        LogUtil.i(new JSONObject(params).toString() + "");

        Gson gson = new Gson();
        final GsonRequest<ModifyPersonalInfoResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                ModifyPersonalInfoResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);
    }


}
