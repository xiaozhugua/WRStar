package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.CourseWare;

import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class KeJianResponse extends  BaseResponse {

    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("coursewares")
        public List<CourseWare> coursewares;

    }

}
