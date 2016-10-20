package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.views.base.AppStatusConstant;
import com.wmx.android.wrstar.views.base.AppStatusManager;

/**
 * Created by wubiao on 2016/1/5.
 *
 * Des: 启动界面
 */
public class SplashActivity extends Activity {

    public static final String TAG = "SplashActivity";

    public String guide_version = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL); //进入应用初始化设置成未登录
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_splash);


        String guide  =  PreferenceUtils.getGuide(this);

        if (guide.equals(PreferenceUtils.STRING_DEFAULT) || !guide.equals(guide_version)){
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            PreferenceUtils.setGuide(this,guide_version);
        }else{
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    }




    public Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SplashActivity.this.finish();
                }
            }, 500);
        }
    };

}
