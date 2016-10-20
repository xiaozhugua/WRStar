package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/31.
 */
public class Gift {

    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("bean")
    public int bean;
    @SerializedName("status")
    public int status;
    @SerializedName("time")
    public String time;
    @SerializedName("index")
    public int index;

    @SerializedName("imgurl")
    public String imgurl;

    @SerializedName("classifier")
    public String classifier;       //礼物量词，例 颗，枚，个




}
