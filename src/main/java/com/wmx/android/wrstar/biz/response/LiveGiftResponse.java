package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.Gift;

import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class LiveGiftResponse extends  BaseResponse{


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {

        @SerializedName("gifts")
        public List<Gift> gifts;

    }
}
