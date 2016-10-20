package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.constants.Constant;
import com.wmx.android.wrstar.mvp.presenters.LivingPagePresenter;
import com.wmx.android.wrstar.mvp.presenters.MainPresenter;
import com.wmx.android.wrstar.mvp.views.Complete;
import com.wmx.android.wrstar.mvp.views.IMainView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.SocketUtils;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.base.AbsBaseFragment;
import com.wmx.android.wrstar.views.base.AppStatusConstant;
import com.wmx.android.wrstar.views.dialog.PromptDialog;
import com.wmx.android.wrstar.views.fragements.HomeFragment2;
import com.wmx.android.wrstar.views.fragements.LivingPageFragment;
import com.wmx.android.wrstar.views.fragements.MyPageFragment;
import com.wmx.android.wrstar.views.fragements.VideoPageFragment;
import com.wmx.android.wrstar.views.fragements.ZhiBoFragment;
import com.wmx.android.wrstar.views.widgets.BottomBar;
import com.wmx.android.wrstar.views.widgets.FragementViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wubiao on 2016/1/18.
 * <p/>
 * Des: 主页面.
 */
public class MainActivity extends AbsBaseActivity implements BottomBar.OnBottomBarSelectedListener, IMainView {

    public static final String TAG = "MainActivity";

    /**
     * 用户连续点击两次返回键可以退出应用的时间间隔.
     */
    public static final long EXIT_INTERVAL = 1000;

    private List<AbsBaseFragment> mFragments;

    private FragmentPagerAdapter mPagerAdapter;


    @Bind(R.id.vp)
    public FragementViewPager mViewPager;

//    @Bind(R.id.bottom_bar)
    public BottomBar mBottomBar;


    HomeFragment2 homeFragment;
    LivingPageFragment livingPageFragment;
    ZhiBoFragment zhiBoFragment;
    VideoPageFragment videoPageFragment;
    MyPageFragment myPageFragment;

    MainPresenter presenter;


    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this, this);
        Constant.netType = SysUtil.getAPNType(WRStarApplication.getContext());
        LivingPagePresenter livingPagePresenter = new LivingPagePresenter(null, null);
        livingPagePresenter.getGiftList();


        autoLogin();


        //初始化蒲公英自动更新
        PgyUpdateManager.register(MainActivity.this,
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        if (Constant.isAotuUpdate){
                            new PromptDialog(MainActivity.this, "有新版本，是否更新?", new Complete() {
                                @Override
                                public void complete() {
                                    startDownloadTask(MainActivity.this, appBean.getDownloadURL());
                                }
                            }).show();
                        }

                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });


    }

    private void autoLogin() {
        LogUtil.i("autoLogin()");
        String token = PreferenceUtils.getToken(MainActivity.this);
        if (WRStarApplication.getUser() == null && !token.equals(PreferenceUtils.STRING_DEFAULT)) {
            //TODO  用TOKEN登录
            LogUtil.i("autoLogin()---符合自动登陆条件");
            presenter.autoLoginByToken(token);
        }
    }


    @Override
    public void protectApp() {
        // Toast.makeText(getApplicationContext(),"应用被回收重启走流程",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
        switch (action) {
            case AppStatusConstant.ACTION_RESTART_APP:
                protectApp();
                break;
            case AppStatusConstant.ACTION_KICK_OUT:
                break;
            case AppStatusConstant.ACTION_BACK_TO_HOME:
                break;
        }
    }


    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initExtraData() {

    }


    public void show(View v){
        if (WRStarApplication.getUser() != null) {
            Intent ite = new Intent(this, CreateLiveActivity.class);
            startActivity(ite);
            overridePendingTransition(R.anim.fade_in2,R.anim.fade_out2);
        } else {
            login();
        }
    }
    @Override
    protected void initVariables() {
        mFragments = new ArrayList<>();
        mFragments.add(homeFragment = new HomeFragment2());
        mFragments.add(livingPageFragment = new LivingPageFragment());
        mFragments.add(zhiBoFragment = new ZhiBoFragment());
        mFragments.add(videoPageFragment = new VideoPageFragment());
        mFragments.add(myPageFragment = new MyPageFragment());

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
    }

    @Override
    protected void initViews() {

        mBottomBar=(BottomBar) findViewById(R.id.bottom_bar);
        mViewPager.setAdapter(mPagerAdapter);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomBar.selected(position);

                /**
                 *  在其他模块运行时关闭Socket 聊天
                 */
                if (!(mFragments.get(position) instanceof LivingPageFragment)) {
                    if (SocketUtils.getInstance().isOpenSocket()) {
                        SocketUtils.getInstance().closeSocket();
                        LogUtil.i("在其他模块运行时关闭Socket 聊天");
                    }
                }

                /**
                 * 各模块加载数据
                 */
                AbsBaseFragment abs = mFragments.get(position);
                boolean haveLoad = abs.isLoad();


                if (!haveLoad) {

                    mFragments.get(position).loadData();
                    abs.setLoad(true);

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomBar.setBottomBarSelectedListener(this);
        // 初始化时，选中"我的"页面
        mViewPager.setCurrentItem(4);
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected String getPageTag() {
        return TAG;
    }

    @Override
    public void onSelected(int selectedIndex) {
        mViewPager.setCurrentItem(selectedIndex);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event != null && event.getRepeatCount() == 0) {
            if (!MainActivity.this.isFinishing()) {
                new PromptDialog(MainActivity.this).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void autoLoginSuccess() {

        if (myPageFragment != null) {
            myPageFragment.refreshUserInfo();
        }


    }


}
