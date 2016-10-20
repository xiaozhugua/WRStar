package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.Gongxian;

import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class GetAppPaiResponse extends  BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("next")
        public int next;
        @SerializedName("recommendurl")
        public String recommendurl;
        @SerializedName("recommendtitle")
        public String recommendtitle;
        @SerializedName("recommendicon")
        public String recommendicon;
        @SerializedName("items")
        public List<Gongxian> items;
    }
}
