package com.wmx.android.wrstar.views.activities;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.constants.Constant;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.dialog.AboutUsDialog;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/13.
 */
public class AboutActivity extends AbsBaseActivity {
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.ly_about)
    RelativeLayout lyAbout;
    @Bind(R.id.ly_agreement)
    RelativeLayout lyAgreement;
    @Bind(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        tvVersion.setText("当前版本"+Constant.appVersion);

        actionBar.setActionBarListener(new ActionBarPrimary.ActionBarPrimaryListener() {
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
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected String getPageTag() {
        return null;
    }


    @OnClick({R.id.ly_about, R.id.ly_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_about:
                new AboutUsDialog(AboutActivity.this, AboutUsDialog.TYPE_ABOUT_US).show();
                break;
            case R.id.ly_agreement:
                new AboutUsDialog(AboutActivity.this, AboutUsDialog.TYPE_AGREEMENT).show();
                break;
        }
    }
}
