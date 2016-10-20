package com.wmx.android.wrstar.views.dialog;

/**
 * Created by Administrator on 2016/5/16.
 */

import com.umeng.analytics.MobclickAgent;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.ActivityManager;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.mvp.views.Complete;
import com.wmx.android.wrstar.utils.SysUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class PromptDialog extends Dialog implements OnClickListener {
    private Context context;
    private Complete complete;
    private Complete cancelComplete;

    private EditText et;

    public PromptDialog(Context context) {
        super(context, R.style.dialog);
        init("确定退出微明星 ？");
    }

    public PromptDialog(Context context, String msg, Complete complete) {
        super(context, R.style.dialog);
        this.complete = complete;
        init(msg);
    }

    public PromptDialog(Context context, String msg, Complete complete, Complete cancelComplete) {
        super(context, R.style.dialog);
        this.complete = complete;
        this.cancelComplete = cancelComplete;
        init(msg);
    }

    private void init(String msg) {
        setContentView(R.layout.dialog_exit);
        setCanceledOnTouchOutside(false);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (SysUtil.getScreenWidth(WRStarApplication.getContext()) * 0.8);
        p.height = p.width * 2 / 3;
        dialogWindow.setAttributes(p);
        ((TextView) findViewById(R.id.dialog_exit_msg)).setText(msg);
        et = (EditText) findViewById(R.id.dialog_exit_et);
        findViewById(R.id.dialog_exit_cancel).setOnClickListener(this);
        findViewById(R.id.dialog_exit_ok).setOnClickListener(this);
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (cancelComplete != null) {
                    cancelComplete.complete();
                }
            }
        });
    }

    public void showNoCancel() {
        findViewById(R.id.dialog_exit_cancel).setVisibility(View.GONE);
        findViewById(R.id.dialog_exit_fgx).setVisibility(View.GONE);
        show();
    }



    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.dialog_exit_cancel:
                if (cancelComplete != null) {
                    cancelComplete.complete();
                }
                this.dismiss();
                break;
            case R.id.dialog_exit_ok:
                this.dismiss();
                if (complete != null) {
                    complete.complete();
                    return;
                }

                if (context instanceof Activity) {
                    ((Activity) context).finish();
                    MobclickAgent.onKillProcess(context);
                }

                ActivityManager.getActivityManager().finishAllActivity();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                break;

            default:
                break;
        }
    }
}
