package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.Banner;
import com.wmx.android.wrstar.entities.OndemandCourse;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class VideoPageResponse extends BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("items")
        public List<OndemandCourse> ondemandCourses;
        @SerializedName("types")
        public List<TypesEntity> types;
        @SerializedName("banners")
        public List<Banner> banners;


        public static class TypesEntity {
            @SerializedName("name")
            public String name;
            @SerializedName("typeid")
            public String typeid;
        }


    }
}
