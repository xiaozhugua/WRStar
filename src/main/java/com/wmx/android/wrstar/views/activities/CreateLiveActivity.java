package com.wmx.android.wrstar.views.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.CreateLiveResponse;
import com.wmx.android.wrstar.biz.response.ManagerLiveResponse;
import com.wmx.android.wrstar.biz.response.UpLoadLivePicResponse;
import com.wmx.android.wrstar.constants.Constant;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.mvp.presenters.CreateLivePresenter;
import com.wmx.android.wrstar.mvp.presenters.ManagerLivePresenter;
import com.wmx.android.wrstar.mvp.views.CreateLiveView;
import com.wmx.android.wrstar.mvp.views.ManagerLiveInfoView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.capturePhoto.CapturePhoto;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateLiveActivity extends AbsBaseActivity implements View.OnClickListener, CreateLiveView,ManagerLiveInfoView {

    @Bind(R.id.tv_live_title)
    EditText mTvLiveTitle;
    @Bind(R.id.tv_live_tags)
    TextView tv_live_tags;
    @Bind(R.id.tv_add_tag)
    TextView mTvAddTag;
    @Bind(R.id.tv_life_showimg)
    TextView mTvLifeShowimg;
    @Bind(R.id.tv_life_show)
    TextView mTvLifeShow;
    @Bind(R.id.tv_taobao_showimg)
    TextView mTvTaobaoShowimg;
    @Bind(R.id.tv_taobao_show)
    TextView mTvTaobaoShow;
    @Bind(R.id.re_tr)
    RelativeLayout mReTr;
    @Bind(R.id.ll_start_live)
    Button mLlStartLive;

    Context context;
    @Bind(R.id.line_shenghuoxiu)
    LinearLayout lineShenghuoxiu;
    @Bind(R.id.line_taobaoxiu)
    LinearLayout lineTaobaoxiu;

    CreateLivePresenter mCreateLivePresenter;

    ManagerLivePresenter managerLivePresenter;
    String type = null;

    Dialog dialog;
    CapturePhoto capture;

    String picture;
    String action="4"; //获取直播信息

    private  JsonArray array;
    private  String mUrlMedia;
    private String mVideoResolution = "SD";

//    @Bind(R.id.ed_createtitle)
//    EditText edCreatetitle;
//    @Bind(R.id.tv_go)
//    TextView tvGo;
//    @Bind(R.id.ll_createtitle)
//    LinearLayout llCreatetitle;
    @Bind(R.id.img_live_title)
    ImageView imgLiveTitle;
    @Bind(R.id.tv_genggai_pic)
    TextView tvGenggaiPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.new_page1_create_live;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

         /* type  1、代表生活秀    2、代表淘宝秀*/
        String type1= PreferenceUtils.getLiveType(this);
        if(!TextUtils.isEmpty(type1)&&type1.equals("1")){
            mTvLifeShowimg.setBackgroundResource(R.mipmap.shenghuoxiu_select);
            mTvLifeShow.setTextColor(Color.parseColor("#ffcf0d"));
            mTvTaobaoShowimg.setBackgroundResource(R.mipmap.live_taobaoxiu);
            mTvTaobaoShow.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            mTvLifeShowimg.setBackgroundResource(R.mipmap.shenghuoxiu);
            mTvLifeShow.setTextColor(Color.parseColor("#FFFFFF"));
            mTvTaobaoShowimg.setBackgroundResource(R.mipmap.live_taobaoxiu_select);
            mTvTaobaoShow.setTextColor(Color.parseColor("#ffcf0d"));
        }
    }

    @Override
    protected void initViews() {

        capture = new CapturePhoto(this);
        dialog = new Dialog(this, R.style.dialog);
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.speakdialog_bottom);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialog.getWindow().setAttributes(lp);

        mCreateLivePresenter = new CreateLivePresenter(this, this);
        managerLivePresenter = new ManagerLivePresenter(this, this);

//        tv_live_tags.setOnClickListener(this);
        mTvAddTag.setOnClickListener(this);
//        mTvLiveTitle.setOnClickListener(this);
        lineShenghuoxiu.setOnClickListener(this);
        lineTaobaoxiu.setOnClickListener(this);
        mLlStartLive.setOnClickListener(this);
        imgLiveTitle.setOnClickListener(this);
        tvGenggaiPic.setOnClickListener(this);


    }
    @Override
    protected void loadData() {
        managerLivePresenter.managerLiveInfo(action,PreferenceUtils.getToken(CreateLiveActivity.this), null, null, null);
    }

    @Override
    protected String getPageTag() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_live_title:
//                final EditText inputNickName = new EditText(this);
//                inputNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("为直播创建一个标题吧!").setView(inputNickName).setNegativeButton("取消", null);
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if(!TextUtils.isEmpty( inputNickName.getText().toString().trim())){
//                            final String newNickName = inputNickName.getText().toString().trim();
//                            mTvLiveTitle.setText(newNickName);
//                        }
//                    }
//                });
//                builder.show();
//                break;
//            case R.id.tv_live_tags:
            case R.id.tv_add_tag:
                Intent ite = new Intent(this, AddTagActivity.class);
                startActivityForResult(ite, Constant.REQUESTCODE);
                break;
            case R.id.img_live_title:  //更改封面
            case R.id.tv_genggai_pic:  //更改封面

                showSelectDialog();

                break;
            case R.id.tv_life_showimg:
                break;
            case R.id.tv_life_show:
                break;
            case R.id.tv_taobao_showimg:

                break;
            case R.id.tv_taobao_show:
                break;
            case R.id.re_tr:
                break;
            case R.id.ll_start_live:  //开始直播
                /*用戶修改題目*/
                String name = mTvLiveTitle.getText().toString().trim();
                String str[]=name.split("#");


                if(str.length>0){
                    action="5";
                    if(!TextUtils.isEmpty(str[0])){
                        oldTitleName=str[0];
                    }
                    if(!TextUtils.isEmpty(picture)){
                        oldPicUrl=picture;
                    }

                    if(array!=null&&array.size()>0){
                        oldArray=array;
                    }
                    managerLivePresenter.managerLiveInfo(action,PreferenceUtils.getToken(CreateLiveActivity.this), oldTitleName, oldPicUrl, oldArray);
                    mCreateLivePresenter.createLive(PreferenceUtils.getToken(CreateLiveActivity.this), oldTitleName, oldPicUrl, oldArray);
                }
                break;
            case R.id.line_shenghuoxiu:  /* type  1、代表生活秀    2、代表淘宝秀*/
                mTvLifeShowimg.setBackgroundResource(R.mipmap.shenghuoxiu_select);
                mTvLifeShow.setTextColor(Color.parseColor("#ffcf0d"));
                mTvTaobaoShowimg.setBackgroundResource(R.mipmap.live_taobaoxiu);
                mTvTaobaoShow.setTextColor(Color.parseColor("#FFFFFF"));
                type = "1";
                PreferenceUtils.setLiveType(this,type);
                break;
            case R.id.line_taobaoxiu:  /* 1、代表生活秀    2、代表淘宝秀*/
                mTvLifeShowimg.setBackgroundResource(R.mipmap.shenghuoxiu);
                mTvLifeShow.setTextColor(Color.parseColor("#FFFFFF"));
                mTvTaobaoShowimg.setBackgroundResource(R.mipmap.live_taobaoxiu_select);
                mTvTaobaoShow.setTextColor(Color.parseColor("#ffcf0d"));
                type = "2";
                PreferenceUtils.setLiveType(this,type);
                break;
        }
    }


    /**
     * 关闭输入法.
     */
    protected void hideInputMethod(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == Constant.REQUESTCODE && resultCode == Constant.RPSPONSECODE) {
            ArrayList<String> datasTag = data.getStringArrayListExtra("result");
            ArrayList<String> TagIds = data.getStringArrayListExtra("resultTagIds");


//                    List<String> set= WRStarApplication.datasTag;
            /*********************************************/
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < datasTag.size(); i++) {
                LogUtil.i("data.size()====" + datasTag.size());
                LogUtil.i(i + "data====" + datasTag.get(i));
                buffer.append("#" + datasTag.get(i));
            }

            String tt=mTvLiveTitle.getText().toString().trim();
            String str[]=tt.split("#");
            if(str.length>0) {
                mTvLiveTitle.setText(str[0] + buffer);
            }
            /*********************************************/
            array = new JsonArray();
            for (int i = 0; i < TagIds.size(); i++) {
                LogUtil.i("TagIds.size()====" + TagIds.size());
                LogUtil.i(i + "data====" + TagIds.get(i));
                array.add(TagIds.get(i)+"");
            }

////            action="2";
//            action="5";
//            managerLivePresenter.managerLiveInfo(action,PreferenceUtils.getToken(CreateLiveActivity.this), titleName, picture, array);
        }

        /*************************更改直播封面*********************************/

        byte[] bs=null;
        if (resultCode == RESULT_OK) {
            bs = new byte[1024*5];
            if (capture.getActionCode() == CapturePhoto.PICK_IMAGE) {
                System.err.println(data.getData().toString());
                Uri targetUri = data.getData();
                if (targetUri != null)
                    bs = capture.handleMediaPhotoMax(targetUri);
            } else {
                bs = capture.handleCameraPhotoMax();
            }

            if (bs != null && bs.length > 0) {
                UpLoadFile upLoadFile = new UpLoadFile(bs, handler);
                upLoadFile.start();
                bs=null;
                showDialog("正在加载中...");
            } else {
                showToast("获取图片信息失败，请确认重试!");
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
                    Gson gson = new Gson();
                    UpLoadLivePicResponse upLoadAvatorResponse = gson.fromJson(response, UpLoadLivePicResponse.class);

                    if (upLoadAvatorResponse.getBody()!=null) {
                        updateAvator(upLoadAvatorResponse.getBody().getUrl());
                        picture=upLoadAvatorResponse.getBody().getUrl();
                    }

                    break;
                case 1:
                    showToast("上传失败");
                    break;
            }
        }
    };
    @Override
    public void createLiveSuccess(CreateLiveResponse mCreateLiveResponse) {

        if (mCreateLiveResponse.body!= null) {
//            mUrlMedia=mCreateLiveResponse.body.live.pullurl;
            Intent intent = new Intent(CreateLiveActivity.this, MediaPreviewActivity.class);
            intent.putExtra("mediaPath", mCreateLiveResponse.body.live.pullurl+"");
            intent.putExtra("videoResolution", mVideoResolution);
            intent.putExtra("filter", false);
            intent.putExtra("alert", false);
            intent.putExtra("liveid", mCreateLiveResponse.body.live.liveid);

//            intent.putExtra("local", mCreateLiveResponse.body.anchor.local+"");
            intent.putExtra("userid", mCreateLiveResponse.body.anchor.userid);
            intent.putExtra("username", mCreateLiveResponse.body.anchor.username);
            intent.putExtra("logourl", mCreateLiveResponse.body.anchor.logourl);

            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            overridePendingTransition(R.anim.umeng_socialize_fade_out,R.anim.umeng_socialize_fade_in);
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void createLiveFail(String dec) {

    }

    public void showSelectDialog() {

        View v = View.inflate(CreateLiveActivity.this, R.layout.dialog_avator_select, null);
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

    }

    public void updateAvator(String url) {
        WRStarApplication.imageLoader.displayImage(url, imgLiveTitle, WRStarApplication.getListOptions());
    }

    @Override
    public void modifyTitleNameSuccess(ManagerLiveResponse response) {

//        if(response.body!=null){
//            mTvLiveTitle.setText(response.body.name);
////            showToast("标题修改成功！");
//        }
    }

    @Override
    public void modifyTitleNameFailed(String resultDec) {

    }

    @Override
    public void modifyTagSuccess(ManagerLiveResponse response) {
//         if(response.body!=null){
////        showToast("标签修改成功！");
//    }
    }

    @Override
    public void modifyTagFailed(String resultDec) {

    }

    @Override
    public void modifyPicSuccess(ManagerLiveResponse response) {

    }

    @Override
    public void modifyPicFailed(String resultDec) {

    }

    private String oldPicUrl;
    private String oldTitleName;
    private JsonArray oldArray=new JsonArray();
    private List<ManagerLiveResponse.BodyEntity.TagsEntity> tags;
    @Override
    public void getLiveInfoSuccess(ManagerLiveResponse response) {

        if(response.body!=null){
            oldTitleName=response.body.name;
            oldPicUrl=response.body.picture;
            tags=response.body.tags;

            for (int i = 0; i < tags.size(); i++) {
                oldArray.add(tags.get(i).tagid+"");
            }

            WRStarApplication.imageLoader.displayImage(response.body.picture, imgLiveTitle, WRStarApplication.getListOptions());

            StringBuffer buffer=new StringBuffer();
            for(int i=0;i<response.body.tags.size();i++){
                buffer.append("#"+response.body.tags.get(i).name);
            }

            mTvLiveTitle.setText( response.body.name+buffer);
//            tv_live_tags.setText(buffer);
            buffer=null;
        }

    }

    @Override
    public void getLiveInfoFailed(String resultDec) {
showToast(resultDec.toString());
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
                URL url = new URL(Urls.UPLOADLIVEPIC + "?token=" + PreferenceUtils.getToken(CreateLiveActivity.this));
                LogUtil.i("token     "+PreferenceUtils.getToken(CreateLiveActivity.this));
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
                        "name=\"img\";filename=\"" +
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
}
