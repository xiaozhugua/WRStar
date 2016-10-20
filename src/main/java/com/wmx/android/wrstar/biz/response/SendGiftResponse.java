package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/1.
 */
public class SendGiftResponse extends  BaseResponse {

    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("starbean")
        public double starbean;
    }
}
