package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.ChatUser;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ChatInfoResponse extends BaseResponse{


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("user")
        public ChatUser user;
        @SerializedName("msg")
        public MsgEntity msg;

        @SerializedName("atUser")
        public ChatUser atUser;

        public static class MsgEntity {
            @SerializedName("content")
            public String content;
            @SerializedName("id")
            public String id;
            @SerializedName("time")
            public long time;
            @SerializedName("userId")
            public String userId;
        }
    }
}
