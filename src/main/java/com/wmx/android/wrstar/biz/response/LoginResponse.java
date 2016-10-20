/*
 * -----------------------------------------------------------------
 * Copyright (C) 2015, by Peanut Run, Shenzhen, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: LoginResponse
 * Author: Mark
 * Version: 1.0
 * Create: 2015/10/11 0011
 *
 * Changes (from 2015/10/11 0011)
 * -----------------------------------------------------------------
 * 2015/10/11 0011 : 创建 LoginResponse (Mark);
 * -----------------------------------------------------------------
 */
package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.User;

/**
 * 登录.
 */
public class LoginResponse extends BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("usessionid")
        public Object usessionid;
        @SerializedName("userinfo")
        public User userinfo;
        @SerializedName("action")
        public String action;


    }
}
