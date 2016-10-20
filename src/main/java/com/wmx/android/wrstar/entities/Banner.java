package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/29.
 */
public class Banner {
    @SerializedName("courseid")
    public String courseid;
    @SerializedName("imgurl")
    public String imgurl;


    public Banner(String courseid, String imgurl) {
        this.courseid = courseid;
        this.imgurl = imgurl;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
