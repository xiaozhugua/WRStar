package com.wmx.android.wrstar.net;

import android.content.Context;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.wmx.android.wrstar.utils.CacheUtils;

import java.io.File;

/**
 * Created by wubiao on 2015/12/29
 *
 * Des: Volley的Request管理器.
 */
public class RequestManager {

    /**
     * 单例对象.
     */
    private static volatile RequestManager sInstance;

    /**
     * Context.
     */
    private static Context sContext;

    /**
     * 网络请求队列.
     */
    private static RequestQueue sRequestQueue;

    /**
     * Volley的Request管理器.
     */
    private RequestManager() {

        File diskCacheDir = CacheUtils.getDiskCacheDir(sContext);
        if ((diskCacheDir != null) && !diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        HttpStack  stack = new HurlStack();

        Network network = new BasicNetwork(stack);
        sRequestQueue = new RequestQueue(new DiskBasedCache(diskCacheDir), network);
        sRequestQueue.start();
    }

    /**
     * 获取Volley的Request管理器实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static RequestManager getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (RequestManager.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new RequestManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 添加网络请求.
     *
     * @param request 网络请求
     * @param tag     请求标签
     * @param <T>     请求类型
     */
    public <T> void add(final Request<T> request, final Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        sRequestQueue.add(request);
    }

    /**
     * 取消网络请求.
     *
     * @param tag 请求标签
     */
    public void cancel(final Object tag) {
        if (tag != null) {
            sRequestQueue.cancelAll(tag);
        }
    }
}
