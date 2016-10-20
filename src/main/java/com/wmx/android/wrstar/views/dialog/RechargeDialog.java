package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.biz.PayBiz;
import com.wmx.android.wrstar.utils.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 星豆充值Dialog
 * Created by Administrator on 2016/5/11.
 */
public class RechargeDialog extends Dialog {


    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.et_star)
    EditText etStar;
    @Bind(R.id.tv_charge_star)
    TextView tvChargeStar;



    @Bind(R.id.btn_recharge)
    Button btnRecharge;
    @Bind(R.id.tv_wechat)
    TextView tvWechat;
    @Bind(R.id.rd_ali)
    RadioButton rdAli;
    @Bind(R.id.rd_wechat)
    RadioButton rdWechat;
    @Bind(R.id.tv_ali)
    TextView tvAli;

    @OnClick({R.id.iv_close, R.id.et_star, R.id.btn_recharge, R.id.tv_wechat, R.id.tv_ali, R.id.rd_wechat, R.id.rd_ali})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                this.dismiss();
                break;
            case R.id.et_star:
                break;
            case R.id.btn_recharge:
                //   Toast.makeText(context, "支付成功", Toast.LENGTH_LONG);


                if (clickListener != null && money > 0) {

                    if (rdAli.isChecked()) {
                        LogUtil.i("支付宝支付");
                        clickListener.send(money + "", PayBiz.PAY_ALI);
                    } else {
                        LogUtil.i("微信支付");
                        clickListener.send(money + "", PayBiz.PAY_WECHAT);
                    }


                } else {
                    Toast.makeText(context, "请输入大于0的数字", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.tv_wechat:
                case R.id.rd_wechat:
                    rdAli.setChecked(false);

                break;
            case R.id.tv_ali:
            case R.id.rd_ali:
                rdWechat.setChecked(false);
                break;



        }
    }


    public interface IClickListener {
        void send(String money, int type);
    }

    private IClickListener clickListener;


    Context context;
    int starRate = 10;
    long money;

    public RechargeDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;

        setContentView(R.layout.dialog_recharge);
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


        String starbean = context.getResources().getString(R.string.total_star3, 0);
        tvChargeStar.setText(starbean);


        etStar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String starbean;
                if (!isEmpty()) {
                    money = Integer.valueOf(etStar.getText() + "");
                    starbean = context.getResources().getString(R.string.total_star3, starRate * money + "");
                    tvChargeStar.setText(starbean + "");
                } else {
                    starbean = context.getResources().getString(R.string.total_moeny, 0 + "");
                    tvChargeStar.setText(starbean);
                    money = 0;
                }
            }
        });

    }

    public void setEditTextPosition() {
        CharSequence text = etStar.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(etStar.getText()) || etStar.getText().equals("");
    }


}
