package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class Course {

    @SerializedName("name")
    public String name;
    @SerializedName("tags")
    public List<Tags> tags;
    @SerializedName("episodes")
    public Object episodes;
    @SerializedName("watch")
    public int watch;
    @SerializedName("collect")
    public int collect;
    @SerializedName("summary")
    public String summary;
    @SerializedName("author")
    public Author author;

}
