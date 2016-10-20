package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/18.
 */
public class CreateLiveResponse extends  BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("live")
        public LiveEntity live;
        @SerializedName("anchor")
        public AnchorEntity anchor;

        public static class LiveEntity {
            @SerializedName("liveid")
            public String liveid;
            @SerializedName("starttime")
            public String starttime;
            @SerializedName("livingtime")
            public Object livingtime;
            @SerializedName("endtime")
            public String endtime;
            @SerializedName("createtime")
            public String createtime;
            @SerializedName("status")
            public String status;
            @SerializedName("courseid")
            public Object courseid;
            @SerializedName("course")
            public Object course;
            @SerializedName("listindex")
            public int listindex;
            @SerializedName("booking")
            public int booking;
            @SerializedName("hits")
            public int hits;
            @SerializedName("runstatus")
            public int runstatus;
            @SerializedName("type")
            public String type;
            @SerializedName("interval")
            public int interval;
            @SerializedName("pullurl")
            public String pullurl;
            @SerializedName("introducevideourl")
            public Object introducevideourl;
            @SerializedName("introduceimgurl")
            public String introduceimgurl;
            @SerializedName("mainimgurl")
            public String mainimgurl;
            @SerializedName("name")
            public String name;
            @SerializedName("summary")
            public String summary;
            @SerializedName("tags")
            public Object tags;
            @SerializedName("anchorid")
            public String anchorid;
            @SerializedName("pushurl")
            public Object pushurl;
        }

        public static class AnchorEntity {
            @SerializedName("logourl")
            public String logourl;
            @SerializedName("description")
            public Object description;
            @SerializedName("star")
            public int star;
            @SerializedName("username")
            public String username;
            @SerializedName("userid")
            public String userid;
            @SerializedName("local")
            public Object local;
        }
    }
}
