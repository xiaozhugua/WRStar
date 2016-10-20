package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.User;

/**
 * Created by Administrator on 2016/6/17.
 */
public class GetUserInfoResponse extends  BaseResponse{

    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("user")
        public User user;

    }
}
