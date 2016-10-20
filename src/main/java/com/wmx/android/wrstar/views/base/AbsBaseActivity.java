package com.wmx.android.wrstar.views.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.umeng.analytics.MobclickAgent;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.ActivityManager;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.mvp.views.Complete;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ToastUtils;
import com.wmx.android.wrstar.views.activities.LoginActivity;
import com.wmx.android.wrstar.views.activities.MainActivity;
import com.wmx.android.wrstar.views.dialog.PromptDialog;

import butterknife.ButterKnife;

/**
 * Created by wubiao on 2015/12/29
 *
 * Des: activity 基类
 */
public abstract class AbsBaseActivity extends AppCompatActivity implements ICommonView {

    protected Resources mResources;

    protected Handler mHandler;

    protected ProgressDialog mProgressDialog;

    protected Context mApplicationContext;

    protected ToastUtils mToastUtils;

    public int i= 0;

    /**
     * 获取内容页面的布局.
     *
     * @return 返回内容页面的布局
     */
    protected abstract int getContentViewLayout();

    /**
     * 初始化从外部传递过来的数据.
     */
    protected abstract void initExtraData();

    /**
     * 初始化子类中的变量.
     */
    protected abstract void initVariables();

    /**
     * 初始化子类中的控件.
     */
    protected abstract void initViews();

    /**
     * 加载数据.
     */
    protected abstract void loadData() ;

    /**
     * 获取页面的标签, 即子类的名称， 必须返回.
     *
     * @return 返回页面的标签
     */
    protected abstract String getPageTag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (AppStatusManager.getInstance().getAppStatus()) {
            /**
             * 应用被强杀
             */
            case AppStatusConstant.STATUS_FORCE_KILLED:
                //跳到主页,主页lauchmode SINGLETASK
                LogUtil.i("应用被强杀 跳到主页,主页lauchmode SINGLETASK");
                protectApp();
                break;
            /**
             * 用户被踢或者TOKEN失效
             */
    //            case AppStatusConstant.STATUS_KICK_OUT:
    //                //弹出对话框,点击之后跳到主页,清除用户信息,运行退出登录逻辑
    ////                Intent intent=new Intent(this,MainActivity.class);
    ////                startActivity(intent);
    //                break;
            case AppStatusConstant.STATUS_NORMAL:

                setContentView(getContentViewLayout());

                // 初始化View注入
                ButterKnife.bind(this);

                // 将该Activity加入堆栈
                ActivityManager.getActivityManager().addActivity(this);

                initExtraData();
                initBaseVariables();
                i = 1;
                initVariables();
                initViews();
                loadData();
                break;
        }


     //   initSystemBar();
    }


    public void protectApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_RESTART_APP);
        startActivity(intent);
    }



    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageTag());
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageTag());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        // 将该Activity从堆栈移除
        ActivityManager.getActivityManager().removeActivity(this);
        super.onDestroy();
    }

    /**
     * 初始化基类中的变量.
     */
    private void initBaseVariables() {
        mResources = getResources();
        mHandler = new Handler();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mApplicationContext = WRStarApplication.getContext();
        mToastUtils = ToastUtils.genToastUtils(this);
    }

    /**
     * 关闭输入法.
     */
    protected void hideInputMethod(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void showToast(String msg) {
        mToastUtils.showShort(msg);
    }

    @Override
    public void showDialog(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    @Override
    public void netError() {
        mProgressDialog.dismiss();
        mToastUtils.showShort(getString(R.string.net_error));
    }

    @Override
    public void hideDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void login() {
        if(WRStarApplication.getUser()==null){
            new PromptDialog(this, "登录获得更多功能，是否立刻登录？", new Complete() {
                @Override
                public void complete() {

                    Intent intent = new Intent(AbsBaseActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            }).show();

        }

    }
}
