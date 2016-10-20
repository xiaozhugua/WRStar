package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/28.
 */
public class Comment {


    @SerializedName("id")
    public String id;
    @SerializedName("like")
    public int like;
    @SerializedName("userid")
    public String userid;
    @SerializedName("courseid")
    public String courseid;
    @SerializedName("status")
    public int status;
    @SerializedName("comment")
    public String comment;
    @SerializedName("userinfo")
    public User userinfo;
    @SerializedName("time")
    public String time;


}
