package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/10.
 */
public class SendVideoCommentResponse {


    @SerializedName("body")
    public BodyEntity body;
    @SerializedName("result")
    public String result;
    @SerializedName("resultdesc")
    public Object resultdesc;
    @SerializedName("transactionid")
    public Object transactionid;
    @SerializedName("msgname")
    public Object msgname;
    @SerializedName("timestamp")
    public Object timestamp;

    public static class BodyEntity {
        @SerializedName("comments")
        public Object comments;
        @SerializedName("addresult")
        public int addresult;
    }
}
