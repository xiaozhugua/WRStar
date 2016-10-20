package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.utils.SysUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/11.
 */
public class TipsStarDialog extends Dialog {
    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.tip_50)
    ImageView tip50;
    @Bind(R.id.tip_100)
    ImageView tip100;
    @Bind(R.id.tip_300)
    ImageView tip300;
    @Bind(R.id.tip_500)
    ImageView tip500;
    @Bind(R.id.tip_1000)
    ImageView tip1000;
    @Bind(R.id.tip_random)
    ImageView tipRandom;
    @Bind(R.id.btn_tip)
    Button btnTip;
    @Bind(R.id.et_star)
    EditText etStar;

    @OnClick({R.id.iv_close, R.id.tip_50, R.id.tip_100, R.id.tip_300, R.id.tip_500, R.id.tip_1000, R.id.tip_random, R.id.btn_tip, R.id.et_star})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                this.dismiss();
                break;
            case R.id.tip_50:
                etStar.setText("50");
                setEditTextPosition();
                break;
            case R.id.tip_100:
                etStar.setText("100");
                setEditTextPosition();
                break;
            case R.id.tip_300:
                etStar.setText("300");
                setEditTextPosition();
                break;

            case R.id.tip_500:
                etStar.setText("500");
                setEditTextPosition();
                break;
            case R.id.tip_1000:
                etStar.setText("1000");
                setEditTextPosition();
                break;
            case R.id.tip_random:

                if (randomStarBeanListener!=null){
                    randomStarBeanListener.send();
                }

                break;
            case R.id.btn_tip:

                if (SysUtil.isFastClick()){
                    return;
                }
                if (!TextUtils.isEmpty(etStar.getText())){
                    long num  = Long.valueOf(etStar.getText() +"");

                    if (num<50){
                        Toast.makeText(context, "星豆数量至少为50", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (sendStarBeanListener!=null && !isEmpty()){
                        sendStarBeanListener.send(num+"");
                    }
                }else{
                    Toast.makeText(context, "星豆数量至少为50", Toast.LENGTH_SHORT).show();
                }



                break;
            case R.id.et_star:

                break;
        }
    }


    public void setEditTextPosition(){
        CharSequence text = etStar.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    public interface ISendStarBeanListener {
        void send(String starBean);
    }

    public interface IRandomStarBeanListener {
        void send();
    }

    private IRandomStarBeanListener randomStarBeanListener;
    private ISendStarBeanListener sendStarBeanListener;

    public void setISendStarBeanListener(ISendStarBeanListener listener){
        this.sendStarBeanListener = listener ;
    }

    public void setRandomStarBeanListener(IRandomStarBeanListener randomStarBeanListener) {
        this.randomStarBeanListener = randomStarBeanListener;
    }

    private EditText et;
    private Handler handler = new Handler();


    Context context;

    public TipsStarDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;

        setContentView(R.layout.dialog_tip_star);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        init();
        windowDeploy();
    }


    public void setRandomStarBean(String count){
        etStar.setText(count);
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

    public boolean isEmpty (){
        return  TextUtils.isEmpty(etStar.getText()) ||  etStar.getText().equals("") ;
    }

}
