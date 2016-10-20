package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.biz.response.LoginResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.constants.SocialAccountInfo;
import com.wmx.android.wrstar.mvp.presenters.LoginPresenter;
import com.wmx.android.wrstar.mvp.views.ILoginView;

import java.util.Map;

import butterknife.Bind;

/**
 * Created by wubiao on 2016/1/5.
 * <p/>
 * Des: 登陆界面
 */
public class LoginActivity extends AbsBaseActivity implements ILoginView {

    public static final String TAG = "LoginActivity";

    private LoginPresenter mLoginPresenter;

    public static String temp_NICKNAME = "";
    public static String temp_IMAGEURL = "";
    public static String temp_TYPE = "";

    @Bind(R.id.et_account)
    public EditText mEtAccount;

    @Bind(R.id.et_password)
    public EditText mEtPassword;

    @Bind(R.id.btn_login)
    public Button mBtnLogin;

    @Bind(R.id.tv_register)
    public TextView mTvRegister;

    @Bind(R.id.iv_weibo_login)
    public ImageView mIvWeiboLogin;

    @Bind(R.id.iv_qq_login)
    public ImageView mIvQQLogin;

    @Bind(R.id.iv_wechat_login)
    public ImageView mIvWechatLogin;

    @Bind(R.id.tv_forget_password)
    public TextView mTvForgetPassword;

    UMShareAPI mShareAPI;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {
        mLoginPresenter = new LoginPresenter(this, this, mResources);

        mShareAPI = UMShareAPI.get(this);

    }

    @Override
    protected void initViews() {


//        mEtAccount.setText("13790495604");
//        mEtPassword.setText("a654321");

        /*登陆*/
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod();
                mLoginPresenter.login(mEtAccount.getText().toString().trim(), mEtPassword.getText().toString().trim());
            }
        });

        /*注册*/
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.actionStart(LoginActivity.this);
            }
        });

        /*微博登陆*/
        mIvWeiboLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weiboLogin();
            }
        });

        /*qq登陆*/
        mIvQQLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLogin();
            }
        });

        /*微信登陆*/
        mIvWechatLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wechatLogin();
            }
        });

        /*重置密码*/
        mTvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordActivity.actionStart(LoginActivity.this);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected String getPageTag() {
        return TAG;
    }


    @Override
    public void loginSuccess(LoginResponse loginResponse) {

        if(!TextUtils.isEmpty(loginResponse.body.userinfo.city)){
            PreferenceUtils.setLocal(this,loginResponse.body.userinfo.city);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                mToastUtils.showShort(getString(R.string.login_success));
                MainActivity.actionStart(LoginActivity.this);
                PreferenceUtils.isLogin(LoginActivity.this);
            }
        }, 1000);
    }

    @Override
    public void loginFailure(String code, String msg) {
        mProgressDialog.dismiss();
        //Todo:需要服务端整理文档，对每个状态码作详细说明
        if (ServerCodes.PASS_ERROR.equals(code)) {
            mToastUtils.showShort(getString(R.string.pass_error));
        } else {
            mToastUtils.showShort(msg);
        }
    }

    @Override
    public void socialLoginSuccess(boolean isBinded, String openid, String socialAccountType) {
        if (isBinded) {
            MainActivity.actionStart(this);
        } else {
            BindPhoneActivity.actionStart(LoginActivity.this, openid, socialAccountType);
        }
    }

    @Override
    public void socialLoginFailure(String code, String msg) {

    }


    private void qqLogin() {


        final SHARE_MEDIA platform = SHARE_MEDIA.QQ;
        LogUtil.i("qqLogin", "qqLogin");
        mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                final String openid = data.get("openid");
                LogUtil.i("qqLogin", "data" + data.toString());
                //  showToast("data:"+data.toString());
                mShareAPI.getPlatformInfo(LoginActivity.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        //   showToast("map:" + map.toString());
                        LogUtil.i("map.toString :" + map.toString());
                        temp_IMAGEURL = map.get("profile_image_url");
                        temp_NICKNAME = map.get("screen_name");
                        mLoginPresenter.socialLogin(openid, map.get("screen_name"), map.get("profile_image_url"), SocialAccountInfo.TYPE_QQ);


                        LogUtil.i("qqLogin", "openid:" + openid + "-nickname:" + map.get("nickname") + "--headimgurl:" + map.get("headimgurl"));
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        LogUtil.i("qqLogin", throwable.toString());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                LogUtil.i("qqLogin", t.toString());
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
            }
        });

    }

    private void weiboLogin() {
//        mEtAccount.setText("13723489082");
//        mEtPassword.setText("123456");

        final SHARE_MEDIA platform = SHARE_MEDIA.SINA;
        LogUtil.i("weibo", "weiboLogin");
        mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                final String openid = data.get("openid");
                LogUtil.i("weixin", "data" + data.toString());
                //   showToast("data:"+data.toString());
                mShareAPI.getPlatformInfo(LoginActivity.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        LogUtil.i("weixin","map"+map.toString());
                        temp_IMAGEURL = map.get("headimgurl");
                        temp_NICKNAME = map.get("nickname");

                        mLoginPresenter.socialLogin(openid, map.get("nickname"), map.get("headimgurl"), SocialAccountInfo.TYPE_WEIBO);
                        LogUtil.i("weixin", "openid:" + openid + "-nickname:" + map.get("nickname") + "--headimgurl:" + map.get("headimgurl"));


                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        LogUtil.i("weibo", throwable.toString());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                LogUtil.i("weibo", t.toString());
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
            }
        });

    }

    private void wechatLogin() {

        final SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        LogUtil.i("weixin", "wechatLogin");
        mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                final String openid = data.get("openid");
                LogUtil.i("weixin", "data" + data.toString());
                //   showToast("data:"+data.toString());
                mShareAPI.getPlatformInfo(LoginActivity.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        LogUtil.i("weixin","map"+map.toString());
                        temp_IMAGEURL = map.get("headimgurl");
                        temp_NICKNAME = map.get("nickname");

                        mLoginPresenter.socialLogin(openid, map.get("nickname"), map.get("headimgurl"), SocialAccountInfo.TYPE_WECHAT);
                        LogUtil.i("weixin", "openid:" + openid + "-nickname:" + map.get("nickname") + "--headimgurl:" + map.get("headimgurl"));


                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        LogUtil.i("weixin", throwable.toString());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                LogUtil.i("weixin", t.toString());
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("auth", "on activity re 2");
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("auth", "on activity re 3");
    }
}
