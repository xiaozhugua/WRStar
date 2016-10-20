package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/18.
 */
public class UpLoadAvatorResponse extends  BaseResponse{

    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("imgURL")
        public String imgURL;
    }
}
