package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.entities.User;

/**
 * Created by Administrator on 2016/5/16.
 */
public interface IPersonalInfoView {

    void modifyNickNameSuccess(String newNickName);
    void modifyNickNameFailed(String resultDec);

    void modifyAvatorSuccess();
    void modifyAvatorFailed();

    void modifySexSuccess(String sex);
    void modifySexFailed();

    void bindPhoneSuccess();
    void bindPhoneFailed();

    void modifySignatureSuccess(String signature);
    void modifySignatureFailed();

    void modifyLocationSuccess(String location);


    void refreshUserInfo(User user);

}
