package com.wmx.android.wrstar.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.UpLoadAvatorResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.entities.User;
import com.wmx.android.wrstar.mvp.presenters.PersonalInfoPresenter;
import com.wmx.android.wrstar.mvp.views.IPersonalInfoView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.capturePhoto.CapturePhoto;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.widgets.CirImageView;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/12.
 */
public class PersonalActivity extends AbsBaseActivity implements IPersonalInfoView {
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.iv_avator)
    CirImageView ivAvatror;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_mobnum)
    TextView tvMobnum;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.tv_signature)
    TextView tvSignature;
    @Bind(R.id.ly_avator)
    RelativeLayout lyAvator;


    CapturePhoto capture;
    Dialog dialog;
    @Bind(R.id.ly_nickname)
    RelativeLayout lyNickname;
    @Bind(R.id.ly_sex)
    RelativeLayout lySex;
    @Bind(R.id.ly_mobnum)
    RelativeLayout lyMobnum;
    @Bind(R.id.ly_location)
    RelativeLayout lyLocation;
    @Bind(R.id.ly_signature)
    RelativeLayout lySignature;

    PersonalInfoPresenter presenter;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initExtraData() {
        refreshUser();
    }

    private void refreshUser() {
        User user = WRStarApplication.getUser();
        tvSignature.setText(user.signature);
        tvNickname.setText(user.username);
        tvSex.setText(user.sex.equals("0") ? "男" : "女");
        tvMobnum.setText(user.mobnum);
        updateAvator(user.userLogo);

        if (user.province != null && user.city != null) {
            if (user.province.equals(user.city)) {
                tvLocation.setText(user.province);
            } else {
                tvLocation.setText(user.province + user.city);
            }
        } else {
            tvLocation.setText("您暂未选择地区");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshUser();
    }

    @Override
    protected void initVariables() {
        presenter = new PersonalInfoPresenter(this, this, this);
    }

    @Override
    protected void initViews() {
        capture = new CapturePhoto(this);
        actionBar.setActionBarListener(new ActionBarPrimary.ActionBarPrimaryListener() {
            @Override
            public void onLeftBtnClick() {
                finish();
            }

            @Override
            public void onRightTextClick() {

            }

            @Override
            public void onRightBtnClick() {

            }
        });

        dialog = new Dialog(this, R.style.dialog);
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.speakdialog_bottom);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialog.getWindow().setAttributes(lp);


    }

    @Override
    protected void loadData() {
        presenter.getUserInfo();
    }

    @Override
    protected String getPageTag() {
        return null;
    }


    @OnClick({R.id.iv_avator, R.id.tv_nickname, R.id.tv_sex, R.id.tv_mobnum, R.id.tv_location, R.id.tv_signature, R.id.ly_avator, R.id.ly_nickname, R.id.ly_sex, R.id.ly_mobnum, R.id.ly_location, R.id.ly_signature})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_avator:
            case R.id.ly_avator:

                View v = View.inflate(PersonalActivity.this, R.layout.dialog_avator_select, null);
                v.findViewById(R.id.ly_cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                v.findViewById(R.id.ly_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, 0);
                    }
                });
                v.findViewById(R.id.ly_album).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        capture.dispatchTakePictureIntent(CapturePhoto.PICK_IMAGE, 1);
                    }
                });


                dialog.setContentView(v);
                dialog.show();


                break;
            case R.id.tv_nickname:
            case R.id.ly_nickname:
                presenter.setNickName(WRStarApplication.getUser().mobnum);
                break;
            case R.id.tv_sex:
            case R.id.ly_sex:

                View v_sex = View.inflate(PersonalActivity.this, R.layout.dialog_sex_select, null);
                v_sex.findViewById(R.id.ly_male).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.setSex(WRStarApplication.getUser().mobnum, "0");
                        dialog.dismiss();
                    }
                });
                v_sex.findViewById(R.id.ly_female).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.setSex(WRStarApplication.getUser().mobnum, "1");
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(v_sex);
                dialog.show();

                break;
            case R.id.ly_mobnum:
            case R.id.tv_mobnum:
                break;
            case R.id.tv_location:
            case R.id.ly_location:

                Intent ilo = new Intent(PersonalActivity.this, ChoiceCityActivity.class);
                startActivity(ilo);


                break;


            case R.id.tv_signature:
            case R.id.ly_signature:
                presenter.setSignature(WRStarApplication.getUser().mobnum);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        byte[] bs = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (capture.getActionCode() == CapturePhoto.PICK_IMAGE) {
                System.err.println(data.getData().toString());
                Uri targetUri = data.getData();
                if (targetUri != null)
                    bs = capture.handleMediaPhoto(targetUri);
            } else {
                bs = capture.handleCameraPhoto();
            }

            if (bs != null && bs.length > 0) {
                UpLoadFile upLoadFile = new UpLoadFile(bs, handler);
                upLoadFile.start();
                showDialog("正在上传...");
                //  uploadAvatar(bs);

            } else {
                showToast("获取图片信息失败，请确认重试!");
            }
        }
    }

    class UpLoadFile extends Thread {
        byte[] bs = null;
        Handler handler;

        public UpLoadFile(byte[] bs, Handler handler) {
            this.bs = bs;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();

            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            try {
                URL url = new URL(Urls.UPLOAD_AVATOR + "?userid=" + WRStarApplication.getUser().mobnum);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
          /* 允许Input、Output，不使用Cache */
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
          /* 设置传送的method=POST */
                con.setRequestMethod("POST");
          /* setRequestProperty */
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Charset", "UTF-8");
                con.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
          /* 设置DataOutputStream */
                DataOutputStream ds =
                        new DataOutputStream(con.getOutputStream());
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; " +
                        "name=\"file1\";filename=\"" +
                        "image.jpg" + "\"" + end);
                ds.writeBytes(end);
          /* 取得文件的FileInputStream */
                ByteArrayInputStream fStream = new ByteArrayInputStream(bs);
          /* 设置每次写入1024bytes */
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
          /* 从文件读取数据至缓冲区 */
                while ((length = fStream.read(buffer)) != -1) {
            /* 将资料写入DataOutputStream中 */
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
          /* close streams */
                fStream.close();
                ds.flush();
          /* 取得Response内容 */
                InputStream is = con.getInputStream();
                int ch;
                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }
          /* 将Response显示于Dialog */

                Message m = new Message();
                m.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("avator", b.toString());
                m.setData(bundle);
                handler.sendMessage(m);

          /* 关闭DataOutputStream */
                ds.close();
            } catch (Exception e) {
                handler.sendEmptyMessage(1);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    Bundle bundle = msg.getData();
                    String response = (String) bundle.get("avator");

                    hideDialog();

                    LogUtil.e( new Gson().toJson(response.toString()));
                    Gson gson = new Gson();
                    UpLoadAvatorResponse upLoadAvatorResponse = gson.fromJson(response, UpLoadAvatorResponse.class);
                    if (upLoadAvatorResponse.body != null) {
                        updateAvator(upLoadAvatorResponse.body.imgURL);
                        WRStarApplication.getUser().userLogo = upLoadAvatorResponse.body.imgURL;

                    }

                    break;
                case 1:

                    showToast("上传失败");
                    break;
            }

        }
    };


    public void updateAvator(String url) {
        WRStarApplication.imageLoader.displayImage(url, ivAvatror, WRStarApplication.getAvatorOptions());
    }

    @Override
    public void modifyNickNameSuccess(String newNickName) {
        tvNickname.setText(newNickName);
        showToast("修改成功");
        WRStarApplication.getUser().username = newNickName;
    }

    @Override
    public void modifyNickNameFailed(String resultDec) {
        showToast(resultDec);
    }

    @Override
    public void modifyAvatorSuccess() {

    }

    @Override
    public void modifyAvatorFailed() {

    }

    @Override
    public void modifySexSuccess(String sex) {
        tvSex.setText(sex);
        showToast("修改成功");
    }

    @Override
    public void modifySexFailed() {
        showToast("修改失败");
    }

    @Override
    public void bindPhoneSuccess() {

    }

    @Override
    public void bindPhoneFailed() {

    }

    @Override
    public void modifySignatureSuccess(String signature) {
        tvSignature.setText(signature);
    }

    @Override
    public void modifySignatureFailed() {

    }

    @Override
    public void modifyLocationSuccess(String location) {

    }

    @Override
    public void refreshUserInfo(User user) {
        WRStarApplication.setUser(user);
        refreshUser();
    }


}
