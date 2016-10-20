package com.wmx.android.wrstar.views.activities;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.constants.Constant;
import com.wmx.android.wrstar.constants.Flavors;
import com.wmx.android.wrstar.mvp.views.Complete;
import com.wmx.android.wrstar.utils.FileUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.dialog.PromptDialog;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/1.
 */
public class SettingActivity extends AbsBaseActivity {
    public static final String TAG = "SettingActivity";
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.rb_showmessage)
    CheckBox rbShowmessage;
    @Bind(R.id.tv_cache)
    TextView tvCache;
    @Bind(R.id.ly_clean_cache)
    RelativeLayout lyCleanCache;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.ly_update)
    RelativeLayout lyUpdate;
    @Bind(R.id.ly_change_password)
    RelativeLayout lyChangePassword;
    @Bind(R.id.ly_exit_account)
    RelativeLayout lyExitAccount;

    @Override
    protected int getContentViewLayout() {
        return R.layout.acitivity_setting;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
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
            tvVersion.setText("V"+Constant.appVersion);

        if (Flavors.isLoacal){
            tvVersion.setText("V"+Constant.appVersion+"本地");
        }




        File file = WRStarApplication.imageLoader.getDiskCache().getDirectory();
        tvCache.setText(FileUtil.getFolderSize(file)/1024 +"KB");

    }


    @Override
    protected void loadData() {

    }

    @Override
    protected String getPageTag() {
        return null;
    }



    @OnClick({R.id.rb_showmessage, R.id.tv_cache, R.id.ly_clean_cache, R.id.tv_version, R.id.ly_update, R.id.ly_change_password, R.id.ly_exit_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_showmessage:
                break;
            case R.id.tv_cache:
                break;
            case R.id.ly_clean_cache:

                WRStarApplication.imageLoader.getDiskCache().clear();
                File file = WRStarApplication.imageLoader.getDiskCache().getDirectory();
                tvCache.setText(FileUtil.getFolderSize(file) / 1024 + "KB");
                showToast("清除缓存成功");
                break;
            case R.id.tv_version:
                break;
            case R.id.ly_update:

                //初始化蒲公英自动更新
                PgyUpdateManager.register(SettingActivity.this,
                        new UpdateManagerListener() {

                            @Override
                            public void onUpdateAvailable(final String result) {

                                // 将新版本信息封装到AppBean中
                                final AppBean appBean = getAppBeanFromString(result);
                                new PromptDialog(SettingActivity.this, "有新版本，是否更新?", new Complete() {
                                    @Override
                                    public void complete() {
                                        startDownloadTask(SettingActivity.this, appBean.getDownloadURL());
                                    }
                                }).show();
                            }

                            @Override
                            public void onNoUpdateAvailable() {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("已是最新版本");
                                    }
                                });
                            }
                        });

                break;
            case R.id.ly_change_password:

                Intent itt = new Intent(SettingActivity.this,ResetPasswordActivity.class);
                itt.putExtra("type","");
                startActivity(itt);

                // ResetPasswordActivity.actionStart(SettingActivity.this);
                break;
            case R.id.ly_exit_account:
                WRStarApplication.setUser(null);
                PreferenceUtils.setToken(SettingActivity.this,PreferenceUtils.STRING_DEFAULT);
                showToast("退出登录成功");
                finish();
                break;
        }
    }
}
