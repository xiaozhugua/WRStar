package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.sina.weibo.sdk.api.share.Base;
import com.wmx.android.wrstar.entities.Banner;
import com.wmx.android.wrstar.entities.Video;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ClassifyVideoResponse extends BaseResponse{


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("sortkeys")
        public List<SortkeysEntity> sortkeys;
        @SerializedName("items")
        public List<Video> items;
        @SerializedName("coursetypes")
        public List<CoursetypesEntity> coursetypes;

        @SerializedName("banners")
        public List<Banner> banners;


        @SerializedName("next")
        public int next;


        public static class SortkeysEntity {
            @SerializedName("id")
            public String id;
            @SerializedName("name")
            public String name;
            @SerializedName("code")
            public String code;
            @SerializedName("index")
            public int index;
            @SerializedName("status")
            public int status;
        }

        public static class CoursetypesEntity {
            @SerializedName("id")
            public String id;
            @SerializedName("name")
            public String name;
            @SerializedName("typeid")
            public String typeid;
            @SerializedName("status")
            public int status;
        }
    }
}
