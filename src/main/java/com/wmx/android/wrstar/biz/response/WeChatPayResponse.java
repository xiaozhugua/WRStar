package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/18.
 */
public class WeChatPayResponse extends  BaseResponse{


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("appid")
        public String appid;
        @SerializedName("noncestr")
        public String noncestr;
        @SerializedName("packagename")
        public String packagename;
        @SerializedName("partnerid")
        public String partnerid;
        @SerializedName("prepayid")
        public String prepayid;
        @SerializedName("timestamp")
        public String timestamp;
        @SerializedName("sign")
        public String sign;
    }
}
