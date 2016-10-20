package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.biz.response.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class VideoComment extends BaseResponse{


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("addresult")
        public int addresult;
        @SerializedName("commentlikecount")
        public int commentlikecount;
        @SerializedName("hasmore")
        public boolean hasmore;
        @SerializedName("comments")
        public List<CommentsEntity> comments;

        public static class CommentsEntity {
            @SerializedName("id")
            public String id;
            @SerializedName("like")
            public int like;
            @SerializedName("userid")
            public String userid;
            @SerializedName("courseid")
            public String courseid;
            @SerializedName("status")
            public int status;
            @SerializedName("comment")
            public String comment;
            @SerializedName("userinfo")
            public UserinfoEntity userinfo;
            @SerializedName("time")
            public String time;
            @SerializedName("timestamp")
            public long timestamp;

            public static class UserinfoEntity {
                @SerializedName("passportid")
                public Object passportid;
                @SerializedName("mobnum")
                public String mobnum;
                @SerializedName("mail")
                public Object mail;
                @SerializedName("username")
                public String username;
                @SerializedName("userstatus")
                public Object userstatus;
                @SerializedName("name")
                public Object name;
                @SerializedName("sex")
                public String sex;
                @SerializedName("birthday")
                public Object birthday;
                @SerializedName("address")
                public Object address;
                @SerializedName("user_logo")
                public String userLogo;
                @SerializedName("citycode")
                public String citycode;
                @SerializedName("procode")
                public String procode;
                @SerializedName("signature")
                public String signature;
                @SerializedName("description")
                public Object description;
                @SerializedName("province")
                public Object province;
                @SerializedName("city")
                public Object city;
                @SerializedName("token")
                public Object token;
                @SerializedName("isvip")
                public boolean isvip;
                @SerializedName("fans")
                public int fans;
                @SerializedName("attention")
                public int attention;
                @SerializedName("userlogolist")
                public Object userlogolist;
                @SerializedName("extensioninfos")
                public ExtensioninfosEntity extensioninfos;
                @SerializedName("other_login_list")
                public Object otherLoginList;
                @SerializedName("starcoins")
                public double starcoins;

                public static class ExtensioninfosEntity {
                }
            }
        }
    }
}
