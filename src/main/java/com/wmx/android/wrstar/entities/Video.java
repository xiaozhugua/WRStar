package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/29.
 */
public class Video {
    @SerializedName("id")
    public String id;
    @SerializedName("typeid")
    public String typeid;
    @SerializedName("courseid")
    public String courseid;
    @SerializedName("name")
    public String name;
    @SerializedName("episodes")
    public Object episodes;
    @SerializedName("watch")
    public int watch;
    @SerializedName("collect")
    public int collect;
    @SerializedName("teacher")
    public Object teacher;
    @SerializedName("authorUrl")
    public String authorUrl;
    @SerializedName("teachername")
    public String teachername;

    @SerializedName("teacherimgurl")
    public String teacherimgurl;
    @SerializedName("time")
    public String time;


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


    public Video(){

    }


}
