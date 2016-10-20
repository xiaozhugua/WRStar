package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/18.
 */
public class CreateTagResponse extends  BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("tag")
        public TagEntity tag;

        public static class TagEntity {
            @SerializedName("tagid")
            public String tagid;
            @SerializedName("name")
            public String name;
            @SerializedName("status")
            public int    status;
        }
    }
}
