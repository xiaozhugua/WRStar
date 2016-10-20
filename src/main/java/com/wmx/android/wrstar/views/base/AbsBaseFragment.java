package com.wmx.android.wrstar.views.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.mvp.views.Complete;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.utils.ToastUtils;
import com.wmx.android.wrstar.views.activities.LoginActivity;
import com.wmx.android.wrstar.views.dialog.PromptDialog;

import butterknife.ButterKnife;

/**
 * Created by wubiao on 2015/12/29
 * <p/>
 * Des: fragment 基类
 */
public abstract class AbsBaseFragment extends Fragment implements ICommonView {

    public int isDestory = 0;

    public boolean haveLoad;
    protected Resources mResources;

    protected ProgressDialog mProgressDialog;

    protected Context mApplicationContext;

    protected ToastUtils mToastUtils;


    /**
     * 宿主activity.
     */
    protected Activity mParentActivity;

    /**
     * 主线程绑定的Handler.
     */
    protected Handler mHandler;

    /**
     * 页面布局.
     */
    protected View mRootView;


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
    public abstract void loadData();


    /**
     * 获取页面的标签, 即子类的名称， 必须返回.
     *
     * @return 返回页面的标签
     */
    protected abstract String getPageTag();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestory = 1 ;
        initExtraData();
        initBaseVariables();
        initVariables();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getContentViewLayout(), container, false);
            // 初始化View注入
            ButterKnife.bind(this, mRootView);

            initViews();
        } else {
            ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mRootView);
            }
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageTag());
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageTag());
    }

    /**
     * 初始化父类中的变量.
     */
    private void initBaseVariables() {
        mParentActivity = getActivity();
        mResources = getResources();
        mHandler = new Handler();


        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mApplicationContext = WRStarApplication.getContext();
        mToastUtils = ToastUtils.genToastUtils(getActivity());

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

        new PromptDialog(getContext(), "登录获得更多功能，是否立刻登录？", new Complete() {
            @Override
            public void complete() {
                Intent intent = new Intent(mParentActivity, LoginActivity.class);
                startActivity(intent);
            }
        }).show();
        return;


    }

    public boolean isLoad() {
        return haveLoad;
    }

    public void setLoad(boolean haveLoad) {
        this.haveLoad = haveLoad;
    }
}
