package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.User;

/**
 * 注册接口响应结果.
 */
public class RegisterResponse extends BaseResponse {
    public static class Body{

        @SerializedName("passportid")
        private String mPassportId;

        public String getPassportId() {
            return mPassportId;
        }

        public void setPassportId(String passportId) {
            mPassportId = passportId;
        }

        @SerializedName("userinfo")
        private User mUser;

        public User getUser() {
            return mUser;
        }

        public void setUser(User user) {
            mUser = user;
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
