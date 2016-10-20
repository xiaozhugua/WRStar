package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/24.
 */
public class Author {
    @SerializedName("local")
    public String local;
    @SerializedName("logourl")
    public String logourl;
    @SerializedName("description")
    public String description;
    @SerializedName("star")
    public int star;
    @SerializedName("username")
    public String username;
    @SerializedName("userid")
    public String userid;
}
