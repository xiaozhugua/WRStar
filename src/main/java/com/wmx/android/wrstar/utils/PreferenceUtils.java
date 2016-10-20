package com.wmx.android.wrstar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.wmx.android.wrstar.constants.Preferences;

import java.util.Map;
import java.util.Map.Entry;

/**
 * SharedPreferences工具.
 */
@SuppressLint("CommitPrefEdits")
public final class PreferenceUtils {

    /** 整型变量默认值. */
    public static final int INT_DEFAULT = -9999999;

    /** 长整形变量默认值. */
    public static final long LONG_DEFAULT = -9999999L;

    /** 字符串变量默认值. */
    public static final String STRING_DEFAULT = "string_default";

    /** 浮点型变量默认值. */
    public static final float FLOAT_DEFAULT = -0.999999f;

    /** 布尔型变量默认值. */
    public static final boolean BOOLEAN_DEFAULT = false;

    private PreferenceUtils() {
    }

    /**
     * 设置登录用户的帐号.
     *
     * @param context Context
     * @param account 帐号
     */
    public static void setAccount(final Context context, final String account) {
        setString(context, Preferences.ACCOUNT, account, true);
    }

    /**
     * 获取登录用户的帐号.
     *
     * @param context Context
     * @return 登录用户的帐号, 若没有则返回默认值{@link #STRING_DEFAULT}
     */
    public static String getAccount(final Context context) {
        return getString(context, Preferences.ACCOUNT, true);
    }

    /**
     * 设置登录用户的Token.
     *
     * @param context Context
     * @param token   Token
     */
    public static void setToken(final Context context, final String token) {
        setString(context, Preferences.TOKEN, token, true);
    }

    /**
     * 获取登录用户的Token.
     *
     * @param context Context
     * @return 登录用户的Token, 若没有则返回默认值{@link #STRING_DEFAULT}
     */
    public static String getToken(final Context context) {
        return getString(context, Preferences.TOKEN, true);
    }

    public static void setLocal(final Context context, final String local) {
        setString(context, Preferences.LOCAL, local, true);
    }

    public static String getLocal(final Context context) {
        return getString(context, Preferences.LOCAL, true);
    }
    public static void setLiveType(final Context context, final String LiveType) {
        setString(context, "LiveType", LiveType, true);
    }

    public static String getLiveType(final Context context) {
        return getString(context, "LiveType", true);
    }

    public static void setliveId(final Context context, final String liveId) {
        setString(context, Preferences.LIVEID, liveId, true);
    }

    public static String getliveId(final Context context) {
        return getString(context, Preferences.LIVEID, true);
    }
    public static void setlivdTitle(final Context context, final String liveTitle) {
        setString(context, "liveTitle", liveTitle, true);
    }

    public static String getliveTitle(final Context context) {
        return getString(context, "liveTitle", true);
    }
    public static void setRecommendTitle(final Context context, final String RecommendTitle) {
        setString(context, "RecommendTitle", RecommendTitle, true);
    }

    public static String getRecommendTitle(final Context context) {
        return getString(context, "RecommendTitle", true);
    }
    public static void setRecommendUrl(final Context context, final String RecommendUrl) {
        setString(context, "RecommendUrl", RecommendUrl, true);
    }

    public static String getRecommendUrl(final Context context) {
        return getString(context, "RecommendUrl", true);
    }
    public static void setRecommendIcon(final Context context, final String Recommendicon) {
        setString(context, "Recommendicon", Recommendicon, true);
    }

    public static String getRecommendIcon(final Context context) {
        return getString(context, "Recommendicon", true);
    }
    public static void setCourseDetailTitle(final Context context, final String CourseDetailTitle) {
        setString(context, "CourseDetailTitle", CourseDetailTitle, true);
    }

    public static String getCourseDetailTitle(final Context context) {
        return getString(context, "CourseDetailTitle", true);
    }
    public static void setlivdContext(final Context context, final String liveContext) {
        setString(context, "Context", liveContext, true);
    }

    public static String getliveContext(final Context context) {
        return getString(context, "Context", true);
    }
    public static void setCourseDetailContext(final Context context, final String CourseDetailContext) {
        setString(context, "CourseDetailContext", CourseDetailContext, true);
    }

    public static String getCourseDetailContext(final Context context) {
        return getString(context, "CourseDetailContext", true);
    }
    public static void setlivdLink(final Context context, final String liveLink) {
        setString(context, "liveLink", liveLink, true);
    }

    public static String getlivdLink(final Context context) {
        return getString(context, "liveLink", true);
    }
    public static void setCourseDetailIcon(final Context context, final String setCourseDetailIcon) {
        setString(context, "setCourseDetailIcon", setCourseDetailIcon, true);
    }

    public static String getCourseDetailIcon(final Context context) {
        return getString(context, "setCourseDetailIcon", true);
    }

    public static void setliveUserId(final Context context, final String liveUserId) {
        setString(context, Preferences.LIVEUSERID, liveUserId, true);
    }

    public static String getliveUserId(final Context context) {
        return getString(context, Preferences.LIVEUSERID, true);
    }

    public static void setliveUserName(final Context context, final String liveUserName) {
        setString(context, Preferences.LIVEUSERNAME, liveUserName, true);
    }

    public static String getliveUserName(final Context context) {
        return getString(context, Preferences.LIVEUSERNAME, true);
    }

    public static void setliveUserLogo(final Context context, final String liveUserLogo) {
        setString(context, Preferences.LIVEUSERLOGO, liveUserLogo, true);
    }

    public static String getliveUserLogo(final Context context) {
        return getString(context, Preferences.LIVEUSERLOGO, true);
    }


    /**
     * 获取具体课程介绍的预告片链接地址
     *
     * @param context Context
     * @return 预告片链接地址, 若没有则返回默认值{@link #STRING_DEFAULT}
     */
    public static String getCourseTrailer(final Context context) {
        return getString(context, Preferences.DETAILS_COURSE_TRAILER, true);
    }


    /**
     * 设置 具体课程介绍的预告片链接地址
     *
     * @param context Context
     * @param url   Token
     */
    public static void setCourseTrailer(final Context context, final String url) {
        setString(context, Preferences.DETAILS_COURSE_TRAILER, url, true);
    }


    /**
     * 获取直播礼物
     *
     * @param context Context
     *
     */
    public static String getLiveGift(final Context context) {
        return getString(context, Preferences.LIVE_GIFT, true);
    }


    /**
     * 设置 直播礼物
     *
     * @param context Context
     * @param json
     */
    public static void setLiveGift(final Context context, final String json) {
        setString(context, Preferences.LIVE_GIFT, json, true);
    }



    /**
     * 获取登录用户的用户标识.
     *
     * @param context Context
     * @return 登录用户的标识, 若没有则返回默认值{@link #STRING_DEFAULT}
     */
    public static String getUserId(final Context context) {
        return getString(context, Preferences.USER_ID, true);
    }

    /**
     * 用户引导页
     *
     * @param context Context
     * @param
     */
    public static void setGuide(final Context context, final String guideWithVersion) {
        setString(context, Preferences.IS_GUIDE, guideWithVersion, true);
    }


    public static String getGuide(final Context context) {
        return getString(context, Preferences.IS_GUIDE, true);
    }

    /**
     * 设置登录用户的标识.
     *
     * @param context Context
     * @param userId  用户标识
     */
    public static void setUserId(final Context context, final String userId) {
        setString(context, Preferences.USER_ID, userId, true);
    }



    /**
     * 设置页面是否需要刷新.
     *
     * @param context       Context
     * @param page          页面标识
     * @param isNeedRefresh true - 需要刷新, false - 不需要刷新
     */
    public static void setNeedRefresh(final Context context, final String page,
            final boolean isNeedRefresh) {
        setBoolean(context, page, isNeedRefresh, false);
    }


    public static boolean isNeedRefresh(final Context context, final String isFirst) {
        return getBoolean(context, isFirst, false);
    }
    public static void isLogin(final Context context) {
        SharedPreferences sp = getSharedPreferences(context, false);
         sp.edit().putBoolean("islogin",true).commit();
    }


    public static boolean getIsLogin(final Context context) {
        SharedPreferences sp = getSharedPreferences(context, false);
        return sp.getBoolean("islogin",false);
    }

    /**
     * 清除登录信息.
     *
     * @param context Context
     */
    public static void clearLoginInfo(final Context context) {
        SharedPreferences sharedPreferences = getCommonPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.remove(Preferences.ACCOUNT);
        editor.remove(Preferences.TOKEN);
        editor.remove(Preferences.USER_ID);
        editor.commit();
    }

    /**
     * 清除用户的所有信息.
     *
     * @param context Context
     */
    public static void clearAllInfo(final Context context) {
        SharedPreferences sharedPreferences = getAccountPreferences(context);
        if (sharedPreferences != null) {
            Editor editor = sharedPreferences.edit();
            Map<String, ?> map = sharedPreferences.getAll();
            String key;
            for (Entry<String, ?> entry : map.entrySet()) {
                key = entry.getKey();
                editor.remove(key);
            }
            editor.commit();
        }
    }

    /**
     * 保存字符串型值.
     *
     * @param context  Context
     * @param key      键
     * @param value    值
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static void setString(final Context context, final String key, final String value,
            final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(key, value).commit();
        }
    }

    /**
     * 保存布尔型值.
     *
     * @param context  Context
     * @param key      键
     * @param value    值
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static void setBoolean(final Context context, final String key, final boolean value,
            final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putBoolean(key, value).commit();
        }
    }

    /**
     * 保存整型值.
     *
     * @param context  Context
     * @param key      键
     * @param value    值
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static void setInt(final Context context, final String key, final int value,
            final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putInt(key, value).commit();
        }
    }

    /**
     * 保存浮点型值.
     *
     * @param context  Context
     * @param key      键
     * @param value    值
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static void setFloat(final Context context, final String key, final float value,
            final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putFloat(key, value).commit();
        }
    }

    /**
     * 保存长整型值.
     *
     * @param context  Context
     * @param key      键
     * @param value    值
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static void setLong(final Context context, final String key, final long value,
            final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putLong(key, value).commit();
        }
    }

    /**
     * 获取字符串型值.
     *
     * @param context  Context
     * @param key      键
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static String getString(final Context context, final String key,
            final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, STRING_DEFAULT);
        }
        return STRING_DEFAULT;
    }

    /**
     * 获取布尔型值.
     *
     * @param context  Context
     * @param key      键
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static Boolean getBoolean(final Context context, final String key,
            final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, BOOLEAN_DEFAULT);
        }
        return BOOLEAN_DEFAULT;
    }

    /**
     * 获取整型型值.
     *
     * @param context  Context
     * @param key      键
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static int getInt(final Context context, final String key, final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, INT_DEFAULT);
        }
        return INT_DEFAULT;
    }

    /**
     * 获取整型型值,默认返回1.
     *
     * @param context  Context
     * @param key      键
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static int getIntNew(final Context context, final String key, final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, 1);
        }
        return 1;
    }



    /**
     * 获取浮点型型值.
     *
     * @param context  Context
     * @param key      键
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static float getFloat(final Context context, final String key, final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            return sharedPreferences.getFloat(key, FLOAT_DEFAULT);
        }
        return FLOAT_DEFAULT;
    }

    /**
     * 获取字符串型值.
     *
     * @param context  Context
     * @param key      键
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     */
    public static long getLong(final Context context, final String key, final boolean isCommon) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException("key不能为空");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context, isCommon);
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(key, LONG_DEFAULT);
        }
        return LONG_DEFAULT;
    }

    /**
     * 根据获取合适的SharedPreferences.
     *
     * @param context  Context
     * @param isCommon 是否属于公共属性, true - 属于公共属性, false - 属于私有属性
     * @return 合适的SharedPreferences
     */
    private static SharedPreferences getSharedPreferences(Context context, boolean isCommon) {
        if (context == null) {
            throw new NullPointerException("context不能为空");
        }
        SharedPreferences sharedPreferences;
        if (isCommon) {
            sharedPreferences = getCommonPreferences(context);
        } else {
            sharedPreferences = getAccountPreferences(context);
        }
        return sharedPreferences;
    }

    /**
     * 获取公共的Preferences.
     *
     * @param context Context
     * @return 公共的Preferences
     */
    private static SharedPreferences getCommonPreferences(final Context context) {
        return context.getSharedPreferences(Preferences.COMMON, Context.MODE_PRIVATE);
    }

    /**
     * 获取登录帐号的Preferences.
     *
     * @param context Context
     * @return 登录帐号的Preferences, 若没有登录返回null
     */
    private static SharedPreferences getAccountPreferences(final Context context) {
        String account = getAccount(context);
        if (!STRING_DEFAULT.equals(account)) {
            return context.getSharedPreferences(account, Context.MODE_PRIVATE);
        }
        return null;
    }

}