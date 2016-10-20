package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.mvp.presenters.ResetPassPresenter;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.mvp.views.IResetPassView;

import butterknife.Bind;

/**
 * Created by wubiao on 2016/1/19.
 *
 * Des:重置密码.
 */
public class ResetPasswordActivity extends AbsBaseActivity implements ActionBarPrimary.ActionBarPrimaryListener, IResetPassView {

    public static final String TAG = "ResetPasswordActivity";

    private ResetPassPresenter mResetPassPresenter;

    @Bind(R.id.et_account)
    public EditText mEtAccount;

    @Bind(R.id.et_password)
    public EditText mEtPassword;

    @Bind(R.id.btn_next)
    public Button mBtnNext;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, ResetPasswordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {
        mResetPassPresenter = new ResetPassPresenter(this, this, mResources);
    }

    @Override
    protected void initViews() {

        ((ActionBarPrimary) findViewById(R.id.action_bar)).setActionBarListener(this);
        if (getIntent().getStringExtra("type")!=null){
            ((ActionBarPrimary) findViewById(R.id.action_bar)).setTitle("修改密码");
        }


        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod();
                mResetPassPresenter.next(mEtAccount.getText().toString().trim(), mEtPassword.getText().toString());
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
                checkIsEnable();
            }
        };

        mEtAccount.addTextChangedListener(textWatcher);
        mEtPassword.addTextChangedListener(textWatcher);
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

    }

    @Override
    public void getAuthCodeFailure(String code, String msg) {

    }

    @Override
    public void resetPasswordSuccess() {

    }

    @Override
    public void resetPasswordFailure(String code, String msg) {

    }

    @Override
    public void toActivity(String account, String password) {
        ResetPasswordActivity2.actionStart(ResetPasswordActivity.this, account, password);
    }


    private void checkIsEnable() {
        if (mEtAccount.getText().toString().trim().length() > 0 && mEtPassword.getText().toString().trim().length() > 0) {
            mBtnNext.setEnabled(true);
        } else {
            mBtnNext.setEnabled(false);
        }
    }
}
