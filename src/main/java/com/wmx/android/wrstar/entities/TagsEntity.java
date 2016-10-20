package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/23.
 */
public class TagsEntity {
    @SerializedName("tagid")
    public String tagid;
    @SerializedName("name")
    public String name;
    @SerializedName("status")
    public int    status;
}
