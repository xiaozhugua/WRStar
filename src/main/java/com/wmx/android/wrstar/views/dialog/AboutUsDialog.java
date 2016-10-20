package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/11.
 */
public class AboutUsDialog extends Dialog {

    public static final int TYPE_ABOUT_US =0 ;
    public static final int TYPE_AGREEMENT =1 ;
    int type ;

    Context context;
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.ly_agreement)
    RelativeLayout lyAgreement;
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.ly_about)
    RelativeLayout lyAbout;

    public AboutUsDialog(Context context,int type) {
        super(context, R.style.dialog);
        this.type = type ;
        setContentView(R.layout.dialog_aboutus);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        init();
        windowDeploy();
    }


    // 设置窗口显示
    public void windowDeploy() {
        Window win = getWindow(); // 得到对话框
        win.setWindowAnimations(R.style.speakdialog_bottom); // 设置窗口弹出动画
        win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

    }

    private void init() {

        switch (type){
            case  TYPE_ABOUT_US:
                lyAbout.setVisibility(View.VISIBLE);
                lyAgreement.setVisibility(View.GONE);
                WRStarApplication.imageLoader.displayImage("drawable://" + R.mipmap.ic_about_logo, ivLogo, WRStarApplication.getListOptions());
                break;

            case TYPE_AGREEMENT:
                lyAbout.setVisibility(View.GONE);
                lyAgreement.setVisibility(View.VISIBLE);
                break;
        }





        actionBar.setActionBarListener(new ActionBarPrimary.ActionBarPrimaryListener() {
            @Override
            public void onLeftBtnClick() {
                dismiss();
            }

            @Override
            public void onRightTextClick() {

            }

            @Override
            public void onRightBtnClick() {

            }
        });


    }

}
