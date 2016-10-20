package com.wmx.android.wrstar.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wubiao on 2015/12/29
 * <p>dddddddd
 * Des: 自定义Activity管理栈
 */
public class ActivityManager {



    private List<Activity> mActivities;
    private static ActivityManager sInstance;

    private ActivityManager() {
        mActivities = new ArrayList<>();
    }

    /**
     * 单一实例dddddddwe
     */
    public static ActivityManager getActivityManager() {
        if (sInstance == null) {
            synchronized (ActivityManager.class) {
                if (sInstance == null) {
                    sInstance = new ActivityManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activity != null) {
            mActivities.add(activity);
        }
    }

    /**
     * 获取当前Activity
     */
    public Activity getNowActivity() {
        return mActivities.get(this.mActivities.size() - 1);
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivities.remove(activity);
        }
    }

    /**
     * 结束所有Activity.
     */
    public void finishAllActivity() {
        for (Activity activity : mActivities) {
            if (activity != null)
                activity.finish();
        }
        mActivities.clear();
    }
}
