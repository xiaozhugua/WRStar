package com.wmx.android.wrstar.net;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wmx.android.wrstar.constants.Configs;
import com.wmx.android.wrstar.utils.LogUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by wubiao on 2015/12/29
 *
 * Des: Gson解析错误.
 */
public class GsonRequest<T> extends JsonRequest<T> {

    /**
     * TAG.
     */
    public static final String TAG = "GsonRequest";

    /**
     * Gson.
     */
    private Gson mGson = new Gson();

    /**
     * Application Context, 注意不要传递Activity Context.
     */
    private Context mContext;

    /**
     * 对象类型.
     */
    private Class<T> mClazz;

    /**
     * 响应监听器.
     */
    private Response.Listener<T> mListener;

    /**
     * 构造方法.
     *
     * @param method        方法类型
     * @param url           地址
     * @param listener      响应监听器
     * @param errorListener 错误监听器
     */
    public GsonRequest(int method, String url, String jsonObject,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonObject == null) ? null : jsonObject.toString(), listener,
                errorListener);
    }

    /**
     * 构造方法, 使用POST形式提交.
     *
     * @param context       Context
     * @param url           地址
     * @param clazz         对象类型
     * @param listener      响应监听器
     * @param errorListener 错误监听器
     */
    public GsonRequest(Context context, String url, String jsonRequest, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {

        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,listener, errorListener);

        mContext = context;
        mClazz = clazz;
        mListener = listener;
        setRetryPolicy(new DefaultRetryPolicy(Configs.Net.TIMEOUT,Configs.Net.RETRY,1.0f));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String json = null;
        try {
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            LogUtil.i("json",json);
            return Response.success(mGson.fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError(e));
        } catch (JsonSyntaxException e) {
            /* JSON格式错误 */
            GsonParseError parseError = new GsonParseError(json, e);
            return Response.error(parseError);
        }
    }





    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
