package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.Home;
import com.wmx.android.wrstar.entities.Tags;

import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class FirstPageResponse extends  BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("next")
        public int next;
        @SerializedName("items")
        public List<Home> items;

    }
}
