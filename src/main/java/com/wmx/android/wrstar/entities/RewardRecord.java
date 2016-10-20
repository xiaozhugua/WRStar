package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/6.
 */
public class RewardRecord {
    @SerializedName("name")
    public String name;
    @SerializedName("summary")
    public String summary;
    @SerializedName("anchor")
    public String anchor;
    @SerializedName("time")
    public String time;
    @SerializedName("money")
    public String money;
    @SerializedName("type")
    public int type;
    @SerializedName("bean")
    public int bean;
}
