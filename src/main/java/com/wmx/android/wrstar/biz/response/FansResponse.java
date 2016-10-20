package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.Fans;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class FansResponse extends  BaseResponse{


    @SerializedName("body")
    public BodyEntity body;



    public static class BodyEntity {
        @SerializedName("success")
        public boolean success;
        @SerializedName("users")
        public List<Fans> fans;

        @SerializedName("next")
        public int next;

    }
}
