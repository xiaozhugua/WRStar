package com.wmx.android.wrstar.biz;

import android.content.Context;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.CheckPhoneNumResponse;
import com.wmx.android.wrstar.biz.response.GetAuthCodeResponse;
import com.wmx.android.wrstar.biz.response.GetUserInfoResponse;
import com.wmx.android.wrstar.biz.response.LoginResponse;
import com.wmx.android.wrstar.biz.response.OtherUserInfoResponse;
import com.wmx.android.wrstar.biz.response.RegisterResponse;
import com.wmx.android.wrstar.biz.response.ResetPasswordResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.MD5;
import com.wmx.android.wrstar.utils.ParamsUtil;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by wubiao on 2015/12/29
 * <p/>
 * Des: 账号相关业务
 */
public class AccountBiz {

    /**
     * TAG.
     */
    public static final String TAG = "AccountBiz";

    private static volatile AccountBiz sInstance; //被设计用来修饰被不同线程访问和修改的变量

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private AccountBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static AccountBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AccountBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new AccountBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取验证码.
     *
     * @param account          账号.
     * @param responseListener 监听器.
     * @param errorListener    错误监听器.
     * @param tag              tag.
     */
    public void getAuthCode(String account, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.GET_DYNAMIC_PASS;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("mobnum", account);
        bodyParams.put("mail", "");
        bodyParams.put("channel", "");

        //ToDo:类型需要整理
        // "passwdtype":"0:验证码;1:动态密码（只支持手机用户）;2:修改密码邮件;3:账号激活邮件
        bodyParams.put("passwdtype", "0");

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<GetAuthCodeResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                GetAuthCodeResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);
    }

    /**
     * 注册用户.
     *
     * @param account          账号.
     * @param password         密码.
     * @param authCode         验证码.
     * @param responseListener 监听器.
     * @param errorListener    错误监听器.
     * @param tag              tag.
     */
    public void register(String account, String password, String authCode,
                         Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.REGISTER;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("mobnum", account);
        bodyParams.put("mail", "");
        bodyParams.put("username", "");
        bodyParams.put("passwd", MD5.getMD5Code(password));
        //TODO:详情在接口文档，需要整理
        // "regchannel":"0：web；1：wap；2：ios client；3：Android client；4：微信；99：其他",
        bodyParams.put("regchannel", "3");
        //  bodyParams.put("userinfo", "");
        bodyParams.put("validnum", authCode);

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<RegisterResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                RegisterResponse.class, responseListener, errorListener);
        LogUtil.i("register:" + gson.toJson(params).toString());


        sRequestManager.add(gsonRequest, tag);
    }

    /**
     * 登陆.
     *
     * @param account          账号.
     * @param password         密码.
     * @param responseListener 监听器.
     * @param errorListener    错误监听器.
     * @param tag              tag.
     */
    public void login(String account, String password,
                      Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.LOGIN;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("account", account);
        bodyParams.put("passwd",  MD5.getMD5Code(password));
        bodyParams.put("tgcticket", "");

        //ToDo:需要整理
        //"密码类型：0：静态密码;1：动态密码;2：第三方认证登录;默认是0",
        bodyParams.put("passwdtype", "0");

        //ToDo:需要整理
        //":"0：web；1：wap；2：ios client；3：Android client 4：微信；99：其他"
        bodyParams.put("portaltype", "3");

        params.put("body", bodyParams);
        params.put("terminal", ParamsUtil.getTerminalInfo());

        Gson gson = new Gson();


//        final GsonRequest<LoginResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
//                LoginResponse.class, responseListener, errorListener);
        final GsonRequest<LoginResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                LoginResponse.class, responseListener, errorListener);
        LogUtil.i(gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }

    /**
     * 社会化登陆.
     *
     * @param openid
     * @param nickName
     * @param headPortrait
     * @param socialAccountType
     */
    public void socialLogin(String openid, String nickName, String headPortrait, String socialAccountType,
                            Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.SOCIAL_LOGIN;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("openid", openid);
        bodyParams.put("nickname", nickName);
        bodyParams.put("headportrait", headPortrait);
        bodyParams.put("portaltype", socialAccountType);

        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<LoginResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                LoginResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);
        LogUtil.i("sociallogin:" + gson.toJson(params).toString());
    }

    /**
     * 第三方登录校验手机号.
     *
     * @param account
     * @param socialAccountType
     * @param responseListener
     * @param errorListener
     * @param tag
     */
    public void checkPhoneNum(String account, String socialAccountType, Response.Listener responseListener,
                              Response.ErrorListener errorListener, String tag) {

        final String url = Urls.CHECK_PHONE_NUM;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("mobnum", account);
        bodyParams.put("type", socialAccountType);

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<CheckPhoneNumResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                CheckPhoneNumResponse.class, responseListener, errorListener);
        LogUtil.i("第三方验证:" + gson.toJson(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }

    /**
     * 注册用户.
     *
     * @param account          账号.
     * @param password         密码.
     * @param authCode         验证码.
     * @param responseListener 监听器.
     * @param errorListener    错误监听器.
     * @param tag              tag.
     */
    public void resetPassword(String account, String password, String authCode,
                              Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.RESET_PASSWORD;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("mobnum", account);
        bodyParams.put("newpasswd",  MD5.getMD5Code(password));
        bodyParams.put("validnum", authCode);

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<ResetPasswordResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                ResetPasswordResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);

    }

    /**
     * 第三方账号绑定手机号.
     *
     * @param account
     * @param openid
     * @param password
     * @param socialAccountType
     * @param authCode
     * @param isRegistered
     * @param responseListener
     * @param errorListener
     * @param tag
     */
    public void bindPhoneNum(String account, String openid, String password, String socialAccountType,
                             String authCode, boolean isRegistered, String nickname, String imageurl, Response.Listener responseListener,
                             Response.ErrorListener errorListener, String tag) {

        final String url = Urls.BIND_PHONE_NUM;
        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("openid", openid);
        bodyParams.put("bindtype", "0");
        bodyParams.put("bindtypesource", socialAccountType);
        bodyParams.put("bindaccount", account);


        bodyParams.put("nickname", nickname);
        bodyParams.put("headportrait", imageurl);


        if (!isRegistered) {
            bodyParams.put("passwd",  MD5.getMD5Code(password));
        }
        bodyParams.put("validnum", authCode);

        params.put("body", bodyParams);

        Gson gson = new Gson();
        final GsonRequest<LoginResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                LoginResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);
        LogUtil.i("第三方绑定手机号" + gson.toJson(params).toString());
    }

    /**
     *  自动登录
     * @param token  登录后保存的token用作之后自动登录.
     * @param responseListener
     * @param errorListener
     * @param tag
     */
    public void autoLoginByToken(String token, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.TOKEN_LOGIN;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("token", token);
        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<LoginResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                LoginResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);

    }

    public void getUserInfo(String token, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.GET_USER_INFO;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("token", token);
        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<GetUserInfoResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                GetUserInfoResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);

    }

    public void getOtherUserInfo(String userid,String otherUSerid, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.GET_OTHER_USER_INFO;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("otherid", otherUSerid);
        bodyParams.put("userid", userid);
        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<OtherUserInfoResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                OtherUserInfoResponse.class, responseListener, errorListener);

        sRequestManager.add(gsonRequest, tag);

    }

}
