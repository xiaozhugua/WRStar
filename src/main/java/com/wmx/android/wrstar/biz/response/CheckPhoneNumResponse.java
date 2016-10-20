package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wubiao on 2016/1/30.
 *
 * Des: 第三方登录，校验手机号.
 */
public class CheckPhoneNumResponse extends BaseResponse  {

    /**
     * 手机号未注册.
     */
    public static String UNREGISTER = "1";

    /**
     * 手机号已注册但未绑定第三方账号.
     */
    public static String UNBIND = "2";

    /**
     *手机号已注册且已绑定第三方账号.
     */
    public static String BINDED = "3";

    public static class Body{

        @SerializedName("state")
        private String mState;

        public String getState() {
            return mState;
        }

        public void setState(String state) {
            mState = state;
        }
    }

    @SerializedName("body")
    private Body mBody;

    public Body getBody() {
        return mBody;
    }

    public void setBody(Body body) {
        mBody = body;
    }
}
