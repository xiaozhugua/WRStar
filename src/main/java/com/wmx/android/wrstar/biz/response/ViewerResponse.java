package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.ChatUser;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ViewerResponse extends  BaseResponse{


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("count")
        public int count;


    }
}
