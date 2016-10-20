package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.biz.response.WeChatPayResponse;
import com.wmx.android.wrstar.entities.User;

/**
 * Created by Administrator on 2016/6/12.
 */
public interface IPayView {
    void getALiPayInfoSuccess(String payInfo);
    void getWeChatPayInfoSuccess(WeChatPayResponse response);

    void refreshUserInfo(User user);

}
