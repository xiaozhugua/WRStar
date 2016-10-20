package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * 点播课程
 * Created by Administrator on 2016/6/3.
 */
public class OndemandCourse {
    @SerializedName("name")
    public String name;
    @SerializedName("courseid")
    public String courseid;
    @SerializedName("watch")
    public int watch;
    @SerializedName("collect")
    public int collect;
    @SerializedName("teacherimgurl")
    public String teacherimgurl;
    @SerializedName("teachername")
    public String teachername;
    @SerializedName("time")
    public String time;
    @SerializedName("imgurl")
    public String imgurl;
}
