package com.wmx.android.wrstar.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.os.Environment;

import java.io.File;

/**
 * 缓存工具.
 */
public final class CacheUtils {

    /**
     * 缓存工具.
     */
    private CacheUtils() {
    }

    /**
     * 获取可用的缓存目录(外部目录可用时用外部, 否则用内部的).
     *
     * @param context Context
     * @return 缓存目录
     */
    public static File getDiskCacheDir(final Context context) {
        if (context == null) {
            throw new NullPointerException("context不能为空");
        }
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? context.getExternalCacheDir()
                : context.getCacheDir();
    }
}
