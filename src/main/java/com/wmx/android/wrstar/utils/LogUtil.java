
package com.wmx.android.wrstar.utils;

import android.util.Log;

import com.wmx.android.wrstar.constants.Flavors;

/**
 * 日志工具.
 */
public final class LogUtil {
    private LogUtil()
    {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 正式版本，关闭Log输出.
     */
    public static boolean isDebug = !Flavors.RELEASE;
    private static final String TAG = "TestData";

    public static void i(String msg)
    {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg)
    {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg)
    {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg)
    {
        if (isDebug)
            Log.v(TAG, msg);
    }

    public static void i(String tag, String msg)
    {
        if (isDebug){



            if (msg.length() > 4000) {

                int chunkCount = msg.length() / 4000;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= msg.length()) {
                        Log.i(tag, "chunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i));
                    } else {
                        Log.i(tag, "chunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i, max));
                    }
                }
            } else {
                Log.i(tag, msg);
            }





        }

    }

    public static void d(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }
}
