package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/23.
 */
public class OtherUserInfoResponse extends  BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("user")
        public UserEntity user;

        public static class UserEntity {
            @SerializedName("mobile")
            public String mobile;
            @SerializedName("fans")
            public int fans;
            @SerializedName("attention")
            public int attention;
            @SerializedName("username")
            public String username;
            @SerializedName("sex")
            public String sex;
            @SerializedName("cityname")
            public String cityname;
            @SerializedName("proname")
            public String proname;
            @SerializedName("logo")
            public String logo;
            @SerializedName("signature")
            public String signature;
            @SerializedName("isattention")
            public boolean isattention;
        }
    }
}
