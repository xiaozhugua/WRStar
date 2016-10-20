package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/12.
 */
public class ALiPayResponse extends  BaseResponse{


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("orderinfo")
        public String orderinfo;
        @SerializedName("paymentno")
        public String paymentno;

        @SerializedName("paymentmethod")
        public int paymentmethod;


    }



}
