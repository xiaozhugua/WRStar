package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**课件
 * Created by Administrator on 2016/5/27.
 */
public class CourseWare {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("liveid")
    public String liveid;
    @SerializedName("courseid")
    public String courseid;
    @SerializedName("type")
    public int type;
    @SerializedName("status")
    public int status;
    @SerializedName("coverimgurl")
    public String coverimgurl;
    @SerializedName("summary")
    public String summary;
    @SerializedName("createtime")
    public String createtime;
    @SerializedName("url")
    public String url;
}
