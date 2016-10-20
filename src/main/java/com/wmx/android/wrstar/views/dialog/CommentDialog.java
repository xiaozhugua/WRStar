package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.utils.InputMethodUtis;

/**
 * Created by Administrator on 2016/5/11.
 */
public class CommentDialog extends Dialog  implements View.OnClickListener{
    public interface ICommentListener{
        void send(String comment);
    }
    private ICommentListener commentListener;

    private EditText et;
    private Handler handler = new Handler();

    private long PUBLISH_COMMENT_TIME;

    Context context;
    public CommentDialog(Context context) {
        super(context, R.style.dialog);
        this.context =context;

        setContentView(R.layout.dialog_speak2);
      setCanceledOnTouchOutside(false);
        init();
        windowDeploy();
    }


    public void setCommentListener(ICommentListener listener){
        this.commentListener = listener ;
    }



    // 设置窗口显示
    public void windowDeploy() {

        Window win = getWindow(); // 得到对话框
        win.setWindowAnimations(R.style.speakdialog_bottom); // 设置窗口弹出动画
        win.setGravity(Gravity.BOTTOM);
        win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

    }

    private  Button btn_speak;
    private void init() {

//        findViewById(R.id.img_speak_fanhui).setOnClickListener(this);
        btn_speak=(Button)findViewById(R.id.btn_speak);
        btn_speak.setOnClickListener(this);
        et = (EditText) findViewById(R.id.et_speak_msg);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>0){
                    btn_speak.setBackgroundResource(R.drawable.bg_btn_yellow);
                }else{
                    btn_speak.setBackgroundResource(R.drawable.bg_btn_send);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodUtis.ShowKeyboard(et);
            }
        }, 400);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.img_speak_fanhui:
//
//                this.dismiss();
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        InputMethodUtis.hideInputDialog(context);
//                    }
//                }, 200);
//                break;
            case R.id.btn_speak:

//                if (System.currentTimeMillis() - PUBLISH_COMMENT_TIME < 10 * 1000) { // 10秒发言时间间隔
//                    Toast.makeText(context, "太快了，休息下吧", Toast.LENGTH_LONG).show();
//                    return;
//                }

                String s = et.getText().toString().trim();
                if (TextUtils.isEmpty(s) || s.equals("")) {
                    Toast.makeText(context, "请输入评论内容", Toast.LENGTH_LONG).show();
                    return;
                }

                if(commentListener!=null){
                    PUBLISH_COMMENT_TIME = System.currentTimeMillis() ;
                    commentListener.send(s);

                    this.dismiss();
//                    handler.postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            InputMethodUtis.hideInputDialog(context);
//                        }
//                    }, 200);
                }
                break;
            default:
                break;
        }
    }
}
