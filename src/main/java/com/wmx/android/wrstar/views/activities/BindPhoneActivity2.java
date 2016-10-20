package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.constants.IntentExtra;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.presenters.SocialLoginPresenter;
import com.wmx.android.wrstar.mvp.views.ISocialLoginView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wubiao on 2016/1/30.
 *
 * Des: 第三方账号绑定手机号第二步：校验手机号.
 */
public class BindPhoneActivity2 extends AbsBaseActivity implements ISocialLoginView, ActionBarPrimary.ActionBarPrimaryListener {

    public static final String TAG = "BindPhoneActivity2";

    private SocialLoginPresenter mSocialLoginPresenter;

    @Bind(R.id.et_password)
    public EditText mEtPassword;

    @Bind(R.id.et_auth_code)
    public EditText mEtAuthCode;

    @Bind(R.id.tv_get_auth_code)
    public TextView mTvGetAuthCode;

    @Bind(R.id.btn_next)
    public Button mBtnNext;

    @Bind(R.id.rlyt_password)
    public RelativeLayout mRlytPass;

    /**标记当前账号是否注册*/
    private boolean mIsRegistered;

    /**用户将要绑定的账号*/
    private String mAccount;

    /**第三方账号的openid*/
    private String mOpenId;

    /**第三方账号的类*/
    private String mSocialAccountType;

    /**
     * 计时器.
     */
    private int mTimeClock;

    private Runnable mCountDownThread = new Runnable() {
        @Override
        public void run() {
            if (mTimeClock > 0) {
                mTvGetAuthCode.setText(
                        mResources.getString(R.string.get_auth_code_again_after_seconds, String.valueOf(mTimeClock)));
                mTimeClock--;
                mHandler.postDelayed(mCountDownThread, 1000);
            } else {
                mTvGetAuthCode.setEnabled(true);
                mTvGetAuthCode.setText(mResources.getString(R.string.get_auth_code_again));
                mHandler.removeCallbacks(mCountDownThread);
            }
        }
    };

    public static void actionStart(Activity activity, String account, String openid, String socialAccountType, boolean isRegistered) {
        Intent intent = new Intent(activity, BindPhoneActivity2.class);
        intent.putExtra(IntentExtra.EXTRA_ACCOUNT, account);
        intent.putExtra(IntentExtra.EXTRA_SOCIAL_ACCOUNT_OPENID, openid);
        intent.putExtra(IntentExtra.EXTRA_SOCIAL_ACCOUNT_TYPE, socialAccountType);
        intent.putExtra(IntentExtra.EXTRA_IS_REGISTERED, isRegistered);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_bind_phone2;
    }

    @Override
    protected void initExtraData() {
        Bundle bundle = getIntent().getExtras();
        mAccount = bundle.getString(IntentExtra.EXTRA_ACCOUNT);
        mOpenId = bundle.getString(IntentExtra.EXTRA_SOCIAL_ACCOUNT_OPENID);
        mSocialAccountType = bundle.getString(IntentExtra.EXTRA_SOCIAL_ACCOUNT_TYPE);
        mIsRegistered = bundle.getBoolean(IntentExtra.EXTRA_IS_REGISTERED);
    }

    @Override
    protected void initVariables() {
        mSocialLoginPresenter = new SocialLoginPresenter(this, this, mResources);
    }

    @Override
    protected void initViews() {

        ((ActionBarPrimary) findViewById(R.id.action_bar)).setActionBarListener(this);

        /*手机号未被注册的情况下，需要设置密码*/
        if(!mIsRegistered){
            mRlytPass.setVisibility(View.VISIBLE);
        }

        /*获取验证码*/
        mTvGetAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocialLoginPresenter.getAuthCode(mAccount);
            }
        });

        /*绑定账号*/
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod();

                /*如果当前账号未被注册，则需要设置密码，否则只需绑定即可*/
                String pasword = null;
                if (!mIsRegistered) {
                    pasword = mEtPassword.getText().toString().trim();
                }
                mSocialLoginPresenter.bindPhoneNum(mAccount, mOpenId, pasword, mSocialAccountType,
                        mEtAuthCode.getText().toString().trim(), mIsRegistered,LoginActivity.temp_NICKNAME,LoginActivity.temp_IMAGEURL);
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
    public void getAuthCodeSuccess() {
        mProgressDialog.dismiss();
        mTvGetAuthCode.setEnabled(false);
        mToastUtils.showShort(getString(R.string.get_auth_code_successfully));
        countDown();
    }

    @Override
    public void getAuthCodeFailure(String code, String msg) {
        mProgressDialog.dismiss();

        //ToDo:需要对不同的code做相应的处理，等服务端出文档
        mToastUtils.showShort(getString(R.string.net_error));
    }

    @Override
    public void checkPhoneNumSuccess(String state) {
    }

    @Override
    public void checkPhoneNumFailure(String code, String msg) {
    }

    @Override
    public void bindPhoneNumSuccess() {
        mProgressDialog.dismiss();
        mToastUtils.showShort(getString(R.string.bind_phone_num_success));
        MainActivity.actionStart(this);

        LoginActivity.temp_NICKNAME = "";
        LoginActivity.temp_IMAGEURL = "";

    }

    @Override
    public void bindPhoneNumFailure(String code, String msg) {
        mProgressDialog.dismiss();
        if (ServerCodes.AUTH_CODE_ERROR.equals(code)) {
            mToastUtils.showShort(getString(R.string.auth_code_error));
        } else if (ServerCodes.AUTH_CODE_TIMEOUT.equals(code)) {
            mToastUtils.showShort(getString(R.string.auth_code_timeout));
        }
    }


    @Override
    public void onLeftBtnClick() {
        finish();
    }

    @Override
    public void onRightTextClick() {

    }

    @Override
    public void onRightBtnClick() {

    }

    private void countDown() {
        mTimeClock = 60;
        mTvGetAuthCode.setText(
                mResources
                        .getString(R.string.get_auth_code_again_after_seconds, String.valueOf(mTimeClock)));
        mHandler.post(mCountDownThread);

        mToastUtils.showShort(getString(R.string.get_auth_code_success));
    }
}
