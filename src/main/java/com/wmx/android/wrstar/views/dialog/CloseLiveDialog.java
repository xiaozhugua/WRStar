package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.wmx.android.wrstar.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 星豆充值Dialog
 * Created by Administrator on 2016/5/11.
 */
public class CloseLiveDialog extends Dialog {

    @Bind(R.id.btn_cancle)
    Button btnCancle;
    @Bind(R.id.btn_close)
    Button btnClose;

    @OnClick({ R.id.btn_cancle, R.id.btn_close})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_cancle:
                this.dismiss();
                break;
            case R.id.btn_close:

                if (clickListener!=null){
                    clickListener.send();
                }

                this.dismiss();
                break;
        }
    }

    public interface IClickListener {
        void send();
    }

    private IClickListener clickListener;


    Context context;

    public CloseLiveDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;

        setContentView(R.layout.dialog_closelive);
        ButterKnife.bind(this);

        setCanceledOnTouchOutside(true);
        init();
        windowDeploy();

    }


    public void setClickListener(IClickListener listener) {
        this.clickListener = listener;
    }


    // 设置窗口显示
    public void windowDeploy() {
        Window win = getWindow(); // 得到对话框
        win.setWindowAnimations(R.style.speakdialog_bottom); // 设置窗口弹出动画
        win.setGravity(Gravity.TOP);

        win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
        WindowManager.LayoutParams lp = win.getAttributes();

        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);


    }

    private void init() {


    }


}
