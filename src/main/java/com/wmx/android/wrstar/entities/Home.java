package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class Home {
    @SerializedName("liveid")
    public String liveid;
    @SerializedName("courseid")
    public String courseid;
    @SerializedName("imgurl")
    public String imgurl;
    @SerializedName("status")
    public int status;
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public int type;
    @SerializedName("tags")
    public List<Tags> tags;
}
