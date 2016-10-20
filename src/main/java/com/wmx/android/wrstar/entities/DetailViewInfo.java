package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class DetailViewInfo {

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
        @SerializedName("one")
        public Object one;
        @SerializedName("course")
        public CourseEntity course;
        @SerializedName("iscollect")
        public boolean iscollect;
        @SerializedName("sharetitle")
        public String sharetitle;
        @SerializedName("shareurl")
        public String shareurl;
        @SerializedName("shareicon")
        public String shareicon;
        @SerializedName("guess")
        public List<GuessEntity> guess;

        public static class CourseEntity {
            @SerializedName("typeid")
            public String typeid;
            @SerializedName("courseid")
            public String courseid;
            @SerializedName("name")
            public String name;
            @SerializedName("watch")
            public int watch;
            @SerializedName("collect")
            public int collect;
            @SerializedName("member")
            public MemberEntity member;
            @SerializedName("lastupdate")
            public String lastupdate;
            @SerializedName("live")
            public Object live;
            @SerializedName("imgurl")
            public String imgurl;
            @SerializedName("createtime")
            public String createtime;
            @SerializedName("summary")
            public String summary;
            @SerializedName("addchanneltype")
            public String addchanneltype;
            @SerializedName("memberid")
            public String memberid;
            @SerializedName("bannerindex")
            public int bannerindex;
            @SerializedName("vip")
            public int vip;
            @SerializedName("comments")
            public int comments;
            @SerializedName("anchorid")
            public Object anchorid;
            @SerializedName("anchorname")
            public Object anchorname;
            @SerializedName("episodes")
            public List<EpisodesEntity> episodes;
            @SerializedName("tags")
            public List<TagsEntity> tags;

            public static class MemberEntity {
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
                public Object local;
            }

            public static class EpisodesEntity {
                @SerializedName("courseid")
                public String courseid;
                @SerializedName("episodeid")
                public String episodeid;
                @SerializedName("imgurl")
                public String imgurl;
                @SerializedName("name")
                public String name;
                @SerializedName("status")
                public String status;
                @SerializedName("count")
                public int count;
                @SerializedName("size")
                public int size;
                @SerializedName("playurl")
                public String playurl;
                @SerializedName("highplayurl")
                public String highplayurl;
            }

            public static class TagsEntity {
                @SerializedName("tagid")
                public String tagid;
                @SerializedName("name")
                public String name;
                @SerializedName("status")
                public int status;
            }
        }

        public static class GuessEntity {
            @SerializedName("courseid")
            public String courseid;
            @SerializedName("name")
            public String name;
            @SerializedName("imgurl")
            public String imgurl;
        }
    }
}
