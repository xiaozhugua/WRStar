package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.ChatUser;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ChatJoinRoomResponse extends BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("roomId")
        public String roomId;
        @SerializedName("count")
        public int count;
        @SerializedName("user")
        public ChatUser user;
        @SerializedName("liveId")
        public String liveId;
        @SerializedName("lastMsg")
        public List<LastMsgEntity> lastMsg;


        public static class LastMsgEntity {
            @SerializedName("user")
            public ChatUser user;
            @SerializedName("atUser")
            public ChatUser atUser;


            @SerializedName("msg")
            public MsgEntity msg;
            @SerializedName("msgType")
            public int msgType;
            @SerializedName("time")
            public int time;



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
}
