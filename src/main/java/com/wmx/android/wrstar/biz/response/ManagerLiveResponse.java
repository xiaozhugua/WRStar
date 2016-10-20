package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ManagerLiveResponse extends BaseResponse{


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("liveid")
        public String liveid;
        @SerializedName("name")
        public String name;
        @SerializedName("picture")
        public String picture;
        @SerializedName("pushurl")
        public Object pushurl;
        @SerializedName("pullurl")
        public String pullurl;
        @SerializedName("tags")
        public List<TagsEntity> tags;

        public static class TagsEntity {
            @SerializedName("tagid")
            public String tagid;
            @SerializedName("name")
            public String name;
            @SerializedName("status")
            public int status;
        }
    }
}
