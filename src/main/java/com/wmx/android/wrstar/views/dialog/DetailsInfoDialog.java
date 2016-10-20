package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.OtherUserInfoResponse;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.widgets.CirImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/11.
 */
public class DetailsInfoDialog extends Dialog implements View.OnClickListener {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.iv_avatar)
    CirImageView ivAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.divider)
    View divider;
    @Bind(R.id.tv_following)
    TextView tvFollowing;
    @Bind(R.id.tv_follower)
    TextView tvFollower;
    @Bind(R.id.tv_mobnum)
    TextView tvMobnum;
    @Bind(R.id.ly_mobnum)
    RelativeLayout lyMobnum;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.ly_sex)
    RelativeLayout lySex;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.ly_location)
    RelativeLayout lyLocation;
    @Bind(R.id.tv_signature)
    TextView tvSignature;
    @Bind(R.id.ly_signature)
    RelativeLayout lySignature;
    @Bind(R.id.ly_nickname)
    RelativeLayout lyNickname;
    @Bind(R.id.tv_focus)
    TextView tvFocus;
    @Bind(R.id.ly_focus)
    RelativeLayout lyFocus;
    private OnFocusListener onFocusListener;

    public void setOnFocus(OnFocusListener onFocus) {
        this.onFocusListener = onFocus;
    }

    public interface OnFocusListener {
        void setFocus(boolean isFocus);
    }

    private OtherUserInfoResponse response;

    public void setResponse(final OtherUserInfoResponse response) {
        this.response = response;

        handler.post(new Runnable() {
            @Override
            public void run() {



                WRStarApplication.imageLoader.displayImage(response.body.user.logo, ivAvatar, WRStarApplication.getAvatorOptions());
                tvName.setText(response.body.user.username);
                tvMobnum.setText(response.body.user.mobile);

                setFocusView(response.body.user.isattention);

                tvSex.setText(response.body.user.sex.equals("0") ? "男" : "女");
                tvSignature.setText(response.body.user.signature);

                tvFollowing.setText("关注 : " + response.body.user.attention);
                tvFollower.setText("粉丝 : " + response.body.user.fans);


                if (!TextUtils.isEmpty(response.body.user.proname) && !TextUtils.isEmpty(response.body.user.cityname)) {
                    if (response.body.user.proname.equals(response.body.user.cityname)) {
                        tvLocation.setText(response.body.user.proname);
                    } else {
                        tvLocation.setText(response.body.user.proname + response.body.user.cityname);
                    }
                } else {
                    tvLocation.setText("您暂未选择地区");
                }

            }
        });

    }


    private Handler handler = new Handler();

    Context context;

    public DetailsInfoDialog(Context context) {
        super(context, R.style.dialog_with_bg);
        this.context = context;

        setContentView(R.layout.dialog_details_personal_info);
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
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

    }

    private void init() {

        findViewById(R.id.ly_focus).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        tvFocus.setOnClickListener(this);

        setFocusView(false);
    }


    @Override
    public void onClick(View v) {

        if (SysUtil.isFastClick()) {
            return;
        }


        switch (v.getId()) {
            case R.id.iv_back:

                this.dismiss();

                break;
            case R.id.tv_focus:
            case R.id.ly_focus:

                if (onFocusListener != null) {
                    onFocusListener.setFocus(!((boolean) tvFocus.getTag()));
                }

                setFocusView(!((boolean) tvFocus.getTag()));

                break;
            default:
                break;
        }
    }


    public void setFocusView(boolean isFocus) {
        TextView tv = tvFocus;
        tv.setTag(isFocus);
        if ((boolean) tvFocus.getTag()) {

            Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.ic_focus_people2);
            drawable.setBounds(0, 0, SysUtil.dp2px(context, 18), SysUtil.dp2px(context, 18));
            tv.setCompoundDrawables(drawable, null, null, null);
            tv.setTextColor(ContextCompat.getColor(context, R.color.text_gray2));


            tv.setText("已关注");
        } else {
            tv.setText("加关注");

            tv.setTextColor(ContextCompat.getColor(context, R.color.text_yellow));

            Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.ic_focus_people1);
            drawable.setBounds(0, 0, SysUtil.dp2px(context, 18), SysUtil.dp2px(context, 18));
            tv.setCompoundDrawables(drawable, null, null, null);

        }
    }
}
