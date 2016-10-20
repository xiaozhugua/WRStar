package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.Live;

/**
 * Created by Administrator on 2016/7/14.
 */
public class LiveInteractionResponse extends  BaseResponse {

    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("live")
        public Live    live;
        @SerializedName("attention")
        public boolean attention;


    }
}
