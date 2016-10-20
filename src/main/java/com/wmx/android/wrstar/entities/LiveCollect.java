package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/7.
 */
public class LiveCollect {

    @SerializedName("mainImgUrl")
    public String mainImgUrl;
    @SerializedName("runstate")
    public int runstate;
    @SerializedName("id")
    public String id;
    @SerializedName("interval")
    public int interval;
    @SerializedName("name")
    public String name;
    @SerializedName("istoday")
    public boolean istoday;
    @SerializedName("starttime")
    public String starttime;
    @SerializedName("endtime")
    public String endtime;
}
