package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.mvp.presenters.ResetPassPresenter;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.constants.IntentExtra;
import com.wmx.android.wrstar.mvp.views.IResetPassView;

import butterknife.Bind;

/**
 * Created by wubiao on 2016/1/19.
 *
 * Des: 重置密码第二步：校验手机号.
 */
public class ResetPasswordActivity2 extends AbsBaseActivity implements ActionBarPrimary.ActionBarPrimaryListener, IResetPassView {

    public static final String TAG = "ResetPasswordActivity2";

    private String mAccount;

    private String mPassword;

    private ResetPassPresenter mResetPassPresenter;

    @Bind(R.id.et_auth_code)
    public EditText mEtAuthCode;

    @Bind(R.id.tv_get_auth_code)
    public TextView mTvGetAuthCode;

    @Bind(R.id.btn_next)
    public Button mBtnNext;

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

    public static void actionStart(Activity activity, String account, String password) {
        Intent intent = new Intent(activity, ResetPasswordActivity2.class);
        intent.putExtra(IntentExtra.EXTRA_ACCOUNT, account);
        intent.putExtra(IntentExtra.EXTRA_PASSWORD, password);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_reset_password2;
    }

    @Override
    protected void initExtraData() {
        Intent intent = getIntent();
        if(intent != null){
            mAccount = intent.getStringExtra(IntentExtra.EXTRA_ACCOUNT);
            mPassword = intent.getStringExtra(IntentExtra.EXTRA_PASSWORD);
        }

        if(TextUtils.isEmpty(mAccount) || TextUtils.isEmpty(mPassword)){
            finish();
        }
    }

    @Override
    protected void initVariables() {
        mResetPassPresenter = new ResetPassPresenter(this, this, mResources);
    }

    @Override
    protected void initViews() {
        ((ActionBarPrimary) findViewById(R.id.action_bar)).setActionBarListener(this);

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod();
                //注册账号
                mResetPassPresenter.resetPassword(mAccount, mPassword,
                        mEtAuthCode.getText().toString().trim());
            }
        });

        mTvGetAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResetPassPresenter.getAuthCode(mAccount);
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
    public void onLeftBtnClick() {
        finish();
    }

    @Override
    public void onRightTextClick() {

    }

    @Override
    public void onRightBtnClick() {

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
    public void resetPasswordSuccess() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                showToast(getResources().getString(R.string.reset_pass_success));
                LoginActivity.actionStart(ResetPasswordActivity2.this);
            }
        }, 1000);
    }

    @Override
    public void resetPasswordFailure(String code, String msg) {
        mProgressDialog.dismiss();

        if(ServerCodes.ACCOUNT_NOT_EXIST.equals(code)){
            showToast(getResources().getString(R.string.account_not_exist));
        }else if(ServerCodes.AUTH_CODE_ERROR.equals(code)){
            showToast(getResources().getString(R.string.auth_code_error));
        }else if(ServerCodes.AUTH_CODE_TIMEOUT.equals(code)){
            showToast(getResources().getString(R.string.auth_code_timeout));
        }
    }


    @Override
    public void toActivity(String account, String password) {

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
