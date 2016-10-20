package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by Administrator on 2016/5/23.
 */
public class Live {


    @SerializedName("id")
    public String id;
    @SerializedName("interval")
    public int interval;
    @SerializedName("runstate")
    public int runstate;
    @SerializedName("name")
    public String name;
    @SerializedName("starttime")
    public String starttime;
    @SerializedName("endtime")
    public String endtime;
    @SerializedName("imgUrl")
    public String imgUrl;
    @SerializedName("videoUrl")
    public String videoUrl;
    @SerializedName("author")
    public Author author;
    @SerializedName("pullUrl")
    public String pullUrl;
    @SerializedName("summary")
    public String summary;
    @SerializedName("isAppointment")
    public boolean isBook;

    @SerializedName("mainImgUrl")
    public String mainImgUrl;

    @SerializedName("bookings")
    public String bookings;

    @SerializedName("tags")
    public List<Tags> tags;

    @SerializedName("type")
    public String type;

}
