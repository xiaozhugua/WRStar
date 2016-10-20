package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.biz.response.CheckPhoneNumResponse;
import com.wmx.android.wrstar.constants.IntentExtra;
import com.wmx.android.wrstar.constants.SocialAccountInfo;
import com.wmx.android.wrstar.mvp.presenters.SocialLoginPresenter;
import com.wmx.android.wrstar.mvp.views.ISocialLoginView;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.dialog.BindPhoneDialog;

import butterknife.Bind;

/**
 * Created by wubiao on 2016/1/30.
 *
 * Des: 第三方账号绑定手机号.
 */
public class BindPhoneActivity extends AbsBaseActivity implements ISocialLoginView, ActionBarPrimary.ActionBarPrimaryListener {

    public static final String TAG = "BindPhoneActivity";

    private SocialLoginPresenter mSocialLoginPresenter;

    @Bind(R.id.et_account)
    public EditText mEtAccount;

    @Bind(R.id.btn_next)
    public Button mBtnNext;

    @Bind(R.id.tv_hint)
    public TextView mTvHint;

    /**第三方账号的openId.*/
    private String mOpenId;

    /**第三方账号类型.*/
    private String mSocialAccountType;

    /**提示文字*/
    private String mHintText;

    public static void actionStart(Activity activity, String openid, String socialAccountType) {
        Intent intent = new Intent(activity, BindPhoneActivity.class);
        intent.putExtra(IntentExtra.EXTRA_SOCIAL_ACCOUNT_OPENID, openid);
        intent.putExtra(IntentExtra.EXTRA_SOCIAL_ACCOUNT_TYPE, socialAccountType);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initExtraData() {
        mOpenId = getIntent().getStringExtra(IntentExtra.EXTRA_SOCIAL_ACCOUNT_OPENID);
        mSocialAccountType = getIntent().getStringExtra(IntentExtra.EXTRA_SOCIAL_ACCOUNT_TYPE);
    }

    @Override
    protected void initVariables() {

        mSocialLoginPresenter = new SocialLoginPresenter(this, this, mResources);

        /*根据不同的第三方账户类型设置相应的提示文字*/
        if(SocialAccountInfo.TYPE_QQ.equals(mSocialAccountType)){
            mHintText = getString(R.string.account_already_bind_qq);
        }else if(SocialAccountInfo.TYPE_WEIBO.equals(mSocialAccountType)){
            mHintText = getString(R.string.account_already_bind_weibo);
        }else if(SocialAccountInfo.TYPE_WECHAT.equals(mSocialAccountType)){
            mHintText = getString(R.string.account_already_bind_wechat);
        }
    }

    @Override
    protected void initViews() {

        ((ActionBarPrimary) findViewById(R.id.action_bar)).setActionBarListener(this);

        /*校验手机号*/
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocialLoginPresenter.checkPhoneNum(mEtAccount.getText().toString().trim(), mSocialAccountType);

            }
        });

        mEtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvHint.setVisibility(View.GONE);
            }
        });

    }

    private void showBindPhoneDialog() {
        final BindPhoneDialog dialog = new BindPhoneDialog(this);
        dialog.setBindPhoneListenner(new BindPhoneDialog.IBindPhoneListenner() {
            @Override
            public void onBind() {
                 /*手机号已经注册，但未绑定第三方账号*/
                BindPhoneActivity2.actionStart(BindPhoneActivity.this, mEtAccount.getText().toString().trim(), mOpenId,
                        mSocialAccountType, true);
                dialog.dismiss();
            }

            @Override
            public void onUnbind() {
                mEtAccount.setText("");
                dialog.dismiss();

                
            }
        });
        dialog.show();
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

    }

    @Override
    public void getAuthCodeFailure(String code, String msg) {

    }

    @Override
    public void checkPhoneNumSuccess(String state) {

        mProgressDialog.dismiss();

        if (CheckPhoneNumResponse.UNREGISTER.equals(state)) {
            /*手机号未注册*/
            BindPhoneActivity2.actionStart(this, mEtAccount.getText().toString().trim(), mOpenId,
                    mSocialAccountType, false);
        } else if (CheckPhoneNumResponse.UNBIND.equals(state)) {
            showBindPhoneDialog();
        } else if (CheckPhoneNumResponse.BINDED.equals(state)) {
            /*手机号已绑定第三方账号*/
            mTvHint.setVisibility(View.VISIBLE);
            mTvHint.setText(mHintText);
        }
    }

    @Override
    public void checkPhoneNumFailure(String code, String msg) {
        mProgressDialog.dismiss();
        //Todo: code需要后台给出文档
    }

    @Override
    public void bindPhoneNumSuccess() {

    }

    @Override
    public void bindPhoneNumFailure(String code, String msg) {

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
}
