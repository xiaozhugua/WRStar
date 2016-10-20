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
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.mvp.presenters.RegisterPresenter;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.mvp.views.IRegisterView;

import butterknife.Bind;

/**
 * Created by wubiao on 2016/1/12.
 *
 * Des: 注册页面.
 */
public class RegisterActivity extends AbsBaseActivity implements ActionBarPrimary.ActionBarPrimaryListener, IRegisterView {

    public static final String TAG = "RegisterActivity";


    private RegisterPresenter mRegisterPresenter;

    @Bind(R.id.et_account)
    public EditText mEtAccount;

    @Bind(R.id.et_password)
    public EditText mEtPassword;

    @Bind(R.id.et_auth_code)
    public EditText mEtAuthCode;

    @Bind(R.id.tv_get_auth_code)
    public TextView mTvGetAuthCode;

    @Bind(R.id.btn_register)
    public Button mBtnRegister;

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

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {
        mRegisterPresenter = new RegisterPresenter(this, this, mResources);
    }

    @Override
    protected void initViews() {

        ((ActionBarPrimary) findViewById(R.id.action_bar)).setActionBarListener(this);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideInputMethod();

                //注册账号
                mRegisterPresenter.register(mEtAccount.getText().toString().trim(), mEtPassword.getText().toString(),
                        mEtAuthCode.getText().toString().trim());
            }
        });

        mTvGetAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterPresenter.getAuthCode(mEtAccount.getText().toString().trim());
            }
        });

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkIsEnableRegister();
            }
        };

        mEtAccount.addTextChangedListener(textWatcher);
        mEtPassword.addTextChangedListener(textWatcher);
        mEtAuthCode.addTextChangedListener(textWatcher);
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
        LogUtil.i("register","code:"+code +"--msg:"+msg);
        //ToDo:需要对不同的code做相应的处理，等服务端出文档
        mToastUtils.showShort(getString(R.string.net_error));
    }


    @Override
    public void registerSuccess() {
        mProgressDialog.dismiss();
        mToastUtils.showShort(getString(R.string.register_success));
        MainActivity.actionStart(RegisterActivity.this);
        PreferenceUtils.isLogin(RegisterActivity.this);
    }

    @Override
    public void registerFailure(String code, String msg) {
        mProgressDialog.dismiss();

        if(ServerCodes.ACCOUNT_EXISTED.equals(code)){
            mToastUtils.showShort(getString(R.string.account_been_registered));
        }else if(ServerCodes.AUTH_CODE_ERROR.equals(code)){
            mToastUtils.showShort(getString(R.string.auth_code_error));
        }else if(ServerCodes.AUTH_CODE_TIMEOUT.equals(code)){
            mToastUtils.showShort(getString(R.string.auth_code_timeout));
        }
    }


    private void countDown() {
        mTimeClock = 60;
        mTvGetAuthCode.setText(
                mResources
                        .getString(R.string.get_auth_code_again_after_seconds, String.valueOf(mTimeClock)));
        mHandler.post(mCountDownThread);

        mToastUtils.showShort(getString(R.string.get_auth_code_success));
    }

    private void checkIsEnableRegister() {
        if (mEtAccount.getText().toString().trim().length() > 0 && mEtPassword.getText().toString().trim().length() > 0
                && mEtAuthCode.getText().toString().trim().length() > 0) {
            mBtnRegister.setEnabled(true);
        } else {
            mBtnRegister.setEnabled(false);
        }
    }
}
