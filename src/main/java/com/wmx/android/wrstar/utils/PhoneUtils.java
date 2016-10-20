/*
 * -----------------------------------------------------------------
 * Copyright (C) 2015, by Peanut Run, Shenzhen, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: PhoneUtils
 * Author: Mark
 * Version: 1.0
 * Create: 2015/10/11 0011
 *
 * Changes (from 2015/10/11 0011)
 * -----------------------------------------------------------------
 * 2015/10/11 0011 : 创建 PhoneUtils (Mark);
 * -----------------------------------------------------------------
 */
package com.wmx.android.wrstar.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 电话工具类.
 */
public class PhoneUtils {

    /**
     * 获取手机Imei号.
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
