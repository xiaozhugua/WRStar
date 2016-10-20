package com.wmx.android.wrstar.entities;

/**
 * Created by Administrator on 2016/6/3.
 */
public class WacthRecord {

    public String videoId;
    public String timeStamp;
    public String type;
    public String imgURL;
    public String title;

    @Override
    public String toString() {
        return "WacthRecord{" +
                "videoId='" + videoId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", type='" + type + '\'' +
                ", imgURL='" + imgURL + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
