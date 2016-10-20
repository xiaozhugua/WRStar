package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.Video;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MyVideoOndemandResponse extends BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("next")
        public int next;
        @SerializedName("index")
        public int index;
        @SerializedName("ismore")
        public boolean ismore;
        @SerializedName("type")
        public String type;
        @SerializedName("items")
        public List<Video> items;


    }
}
