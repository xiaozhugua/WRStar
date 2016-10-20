package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.TagsEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class GetHotTagResponse extends  BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("tags")
        public List<TagsEntity> tags;
        }
}
