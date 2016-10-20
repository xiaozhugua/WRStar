package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.RewardRecord;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MyBuyRewardResponse extends  BaseResponse{

    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("next")
        public int next;
        @SerializedName("rewards")
        public List<RewardRecord> rewards;


    }
}
