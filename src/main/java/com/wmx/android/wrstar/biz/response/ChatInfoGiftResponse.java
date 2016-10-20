package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.ChatUser;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ChatInfoGiftResponse extends  BaseResponse{


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("giftId")
        public int giftId;
        @SerializedName("count")
        public int count;
        @SerializedName("type")
        public int type;
        @SerializedName("bean")
        public int bean;
        @SerializedName("simpleUserInfo")
        public ChatUser chatUser;

    }
}
