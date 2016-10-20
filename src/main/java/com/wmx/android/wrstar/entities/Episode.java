package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/30.
 */
public class Episode {
    @SerializedName("id")
    public String id;
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
}
