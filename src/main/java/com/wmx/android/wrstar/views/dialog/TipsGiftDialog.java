package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.Gift;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.activities.MyStarBeanActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/11.
 */
public class TipsGiftDialog extends Dialog {
    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.iv_gift)
    ImageView ivGift;
    @Bind(R.id.tv_gift_name)
    TextView tvGiftName;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.et_star)
    EditText etStar;
    @Bind(R.id.rd_star)
    RadioButton rdStar;

    @Bind(R.id.btn_tip)
    Button btnTip;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_star)
    TextView tvStar;
    @Bind(R.id.ly3)
    LinearLayout ly3;
    @Bind(R.id.tv_classifier)
    TextView tvClassifier;
    @Bind(R.id.tv_go_recharge)
    TextView tvGoRecharge;

    @OnClick({R.id.iv_close, R.id.iv_gift, R.id.tv_gift_name, R.id.tv1, R.id.et_star, R.id.rd_star,  R.id.btn_tip,R.id.tv_go_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                this.dismiss();
                break;
            case R.id.iv_gift:
                break;
            case R.id.tv_gift_name:
                break;
            case R.id.tv1:
                break;
            case R.id.et_star:
                break;

            case R.id.btn_tip:
                if (SysUtil.isFastClick()){
                    return;
                }
                if (giftCount==0){
                    Toast.makeText(context  , "礼物数量至少为1", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (clickListener != null && !isEmpty()) {
                    clickListener.send(gift.id + "", giftCount + "");
                }
                break;

            case R.id.tv_go_recharge:

                Intent it = new Intent(context, MyStarBeanActivity.class);
                context.startActivity(it);


              break;

        }
    }



    public interface IClickListener {
        void send(String giftId, String count);
    }

    private IClickListener clickListener;


    Context context;
    private Gift gift;
    private long giftCount;

    public TipsGiftDialog(Context context, Gift gift) {
        super(context, R.style.dialog);
        this.context = context;
        this.gift = gift;
        setContentView(R.layout.dialog_tip_gift);
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

        WRStarApplication.imageLoader.displayImage(gift.imgurl, ivGift, WRStarApplication.getListOptions());
        tvGiftName.setText(gift.name);

        String money = context.getResources().getString(R.string.total_moeny, gift.bean);
        tvMoney.setText(money);

        String star = context.getResources().getString(R.string.total_star, WRStarApplication.getUser().starcoins);
        tvStar.setText(star);

        etStar.setText("1");
        setEditTextPosition();
        giftCount = 1;

        tvClassifier.setText(gift.classifier + "给TA");


        etStar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isEmpty()) {
                    giftCount = Integer.valueOf(etStar.getText() + "");
                    String money = context.getResources().getString(R.string.total_moeny, gift.bean * giftCount + "");
                    tvMoney.setText(money);
                } else {
                    String money = context.getResources().getString(R.string.total_moeny, gift.bean + "");
                    tvMoney.setText(money);
                    giftCount = 1;
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
