package com.wmx.android.wrstar.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wubiao on 2016/1/21.
 *
 * Des: 用户实体.
 */
public class User {

    @SerializedName("passportid")
    public String passportid;
    @SerializedName("mobnum")
    public String mobnum;
    @SerializedName("mail")
    public String mail;
    @SerializedName("username")
    public String username;
    @SerializedName("userstatus")
    public String userstatus;
    @SerializedName("name")
    public String name;
    @SerializedName("sex")
    public String sex;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("address")
    public String address;
    @SerializedName("user_logo")
    public String userLogo;
    @SerializedName("citycode")
    public String citycode;
    @SerializedName("procode")
    public String procode;
    @SerializedName("signature")
    public String signature;
    @SerializedName("description")
    public String description;
    @SerializedName("province")
    public String province;
    @SerializedName("city")
    public String city;
    @SerializedName("userlogolist")
    public String userlogolist;
    @SerializedName("extensioninfos")
    public ExtensioninfosEntity extensioninfos;
    @SerializedName("other_login_list")
    public Object otherLoginList;
    @SerializedName("starcoins")
    public double starcoins;
    @SerializedName("isvip")
    public boolean isvip;

    @SerializedName("token")
    public String token;

    @SerializedName("attention")
    public int attention;
    @SerializedName("fans")
    public int fans;


    @Override
    public String toString() {
        return "User{" +
                "passportid='" + passportid + '\'' +
                ", mobnum='" + mobnum + '\'' +
                ", mail='" + mail + '\'' +
                ", username='" + username + '\'' +
                ", userstatus='" + userstatus + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", userLogo='" + userLogo + '\'' +
                ", citycode='" + citycode + '\'' +
                ", procode='" + procode + '\'' +
                ", signature='" + signature + '\'' +
                ", description='" + description + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", userlogolist='" + userlogolist + '\'' +
                ", extensioninfos=" + extensioninfos +
                ", otherLoginList=" + otherLoginList +
                ", starcoins=" + starcoins +
                ", isvip=" + isvip +
                ", token='" + token + '\'' +
                ", attention=" + attention +
                ", fans=" + fans +
                '}';
    }

    public static class ExtensioninfosEntity {
    }
}
