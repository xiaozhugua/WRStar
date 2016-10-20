package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.LiveCollect;

import java.util.List;

/**
 * Created by Administrator on 2016/6/7.
 */
public class MyVideoLiveResponse extends  BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("items")
        public Object items;
        @SerializedName("next")
        public int next;
        @SerializedName("index")
        public int index;
        @SerializedName("ismore")
        public boolean ismore;
        @SerializedName("type")
        public String type;
        @SerializedName("beforestart")
        public int beforestart;
        @SerializedName("run")
        public int run;
        @SerializedName("afterend")
        public int afterend;
        @SerializedName("lives")
        public List<LiveCollect> lives;

    }
}
