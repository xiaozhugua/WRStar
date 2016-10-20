package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DetailsCourseResponse extends  BaseResponse{

    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("live")
        public LiveEntity live;
        @SerializedName("attention")
        public boolean attention;

        public static class LiveEntity {
            @SerializedName("id")
            public String id;
            @SerializedName("interval")
            public int interval;
            @SerializedName("runstate")
            public int runstate;
            @SerializedName("name")
            public String name;
            @SerializedName("starttime")
            public Object starttime;
            @SerializedName("endtime")
            public String endtime;
            @SerializedName("imgUrl")
            public String imgUrl;
            @SerializedName("videoUrl")
            public String videoUrl;
            @SerializedName("author")
            public AuthorEntity author;
            @SerializedName("pullUrl")
            public String pullUrl;
            @SerializedName("summary")
            public String summary;
            @SerializedName("isAppointment")
            public boolean isAppointment;
            @SerializedName("inviteurl")
            public String inviteurl;
            @SerializedName("invitetitle")
            public String invitetitle;
            @SerializedName("inviteicon")
            public String inviteicon;

            public static class AuthorEntity {
                @SerializedName("logourl")
                public String logourl;
                @SerializedName("description")
                public String description;
                @SerializedName("star")
                public int star;
                @SerializedName("username")
                public String username;
                @SerializedName("userid")
                public String userid;
                @SerializedName("local")
                public String local;
            }
        }
    }
}
