package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.utils.InputMethodUtis;

/**
 * Created by Administrator on 2016/5/11.
 */
public class BookDialog extends Dialog implements View.OnClickListener {
    public interface ICommentListener {
        void send(String comment);
    }

    private ICommentListener commentListener;

    private EditText et;
    private Handler handler = new Handler();

    private long PUBLISH_COMMENT_TIME;

    Context context;

    public BookDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;

        setContentView(R.layout.dialog_book);
        setCanceledOnTouchOutside(true);
        init();
        windowDeploy();
    }


    public void setCommentListener(ICommentListener listener) {
        this.commentListener = listener;
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

        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.bg_dialog).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_close:
            case R.id.bg_dialog:

                this.dismiss();

                break;

            default:
                break;
        }
    }
}
