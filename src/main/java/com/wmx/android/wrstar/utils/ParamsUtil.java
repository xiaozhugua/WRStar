package com.wmx.android.wrstar.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wubiao on 2016/1/21.
 *
 * Des: 网络参数工具.
 */
public class ParamsUtil {

    public static Map<String, Object> getBasicInfo() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("version", "1.0.0");
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("transactionid", "transactionidxxxxx");
        params.put("appid", "100001");
        params.put("appkey", "wx");

        return params;
    }

    public static Map<String, String> getTerminalInfo() {

        HashMap<String, String> params = new HashMap<>();
        params.put("client_version", "123123");
        params.put("bpid", "123");
        params.put("os_version", "123");

        return params;
    }
}
