package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class HomePageResponse extends  BaseResponse {

    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("todayrecommend")
        public TodayrecommendEntity todayrecommend;
        @SerializedName("newests")
        public List<NewestsEntity> newests;
        @SerializedName("heats")
        public List<HeatsEntity> heats;
        @SerializedName("wrstars")
        public List<WrstarsEntity> wrstars;
        @SerializedName("ads")
        public List<AdsEntity> ads;

        public static class TodayrecommendEntity {
            @SerializedName("courseid")
            public String courseid;
            @SerializedName("summary")
            public String summary;
            @SerializedName("imgurl")
            public String imgurl;
            @SerializedName("name")
            public String name;
        }

        public static class NewestsEntity {
            @SerializedName("courseid")
            public String courseid;
            @SerializedName("imgurl")
            public String imgurl;
            @SerializedName("name")
            public String name;
            @SerializedName("iscollect")
            public boolean iscollect;
        }

        public static class HeatsEntity {
            @SerializedName("courseid")
            public String courseid;
            @SerializedName("imgurl")
            public String imgurl;
            @SerializedName("name")
            public String name;
            @SerializedName("iscollect")
            public boolean iscollect;
        }

        public static class WrstarsEntity {
            @SerializedName("courseid")
            public String courseid;
            @SerializedName("imgurl")
            public String imgurl;
            @SerializedName("name")
            public String name;
            @SerializedName("iscollect")
            public boolean iscollect;
        }

        public static class AdsEntity {
            @SerializedName("imgurl")
            public String imgurl;
            @SerializedName("skipurl")
            public String skipurl;
        }
    }
}
