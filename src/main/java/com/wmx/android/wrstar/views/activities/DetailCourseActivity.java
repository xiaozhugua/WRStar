package com.wmx.android.wrstar.views.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.netease.neliveplayer.NELivePlayer;
import com.umeng.socialize.UMShareAPI;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.DetailsCourseResponse;
import com.wmx.android.wrstar.biz.response.ShareSuccessResponse;
import com.wmx.android.wrstar.mvp.presenters.LiveDetailsPresenter;
import com.wmx.android.wrstar.mvp.presenters.ShareSuccessPresenter;
import com.wmx.android.wrstar.mvp.views.IDetailsCourse;
import com.wmx.android.wrstar.mvp.views.ShareSuccessView;
import com.wmx.android.wrstar.player.NEMediaController;
import com.wmx.android.wrstar.player.NEVideoView;
import com.wmx.android.wrstar.store.DBHandler;
import com.wmx.android.wrstar.store.DBManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.utils.TimeUtils;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.dialog.BookDialog;
import com.wmx.android.wrstar.views.dialog.ShareCourseDetailsDialog;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.widgets.MyCountDownTimer;
import com.wmx.android.wrstar.views.widgets.RatingBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/19.
 */
public class DetailCourseActivity extends AbsBaseActivity implements IDetailsCourse,ShareSuccessView {
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.iv_course)
    ImageView ivCourse;
    @Bind(R.id.iv_play)
    ImageView ivPlay;

    @Bind(R.id.iv1)
    ImageView iv1;
    @Bind(R.id.tv_dec)
    TextView tvDec;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.iv_book)
    ImageView ivBook;
    @Bind(R.id.iv_author)
    ImageView ivAuthor;
    @Bind(R.id.tv_author)
    TextView tvAuthor;
    @Bind(R.id.ratingbar)
    RatingBar ratingbar;
    @Bind(R.id.tv_author_dec)
    TextView tvAuthorDec;
    @Bind(R.id.tv_focus)
    TextView tvFocus;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    @Bind(R.id.tv_course_name)
    TextView tvCourseName;
    @Bind(R.id.view_trailer)
    NEVideoView videoView;
    @Bind(R.id.scrollview2)
    ScrollView scrollview2;

    String local;

    DetailsCourseResponse.BodyEntity.LiveEntity live;
    public final String TAG = "DetailCourseActivity";
    public final int STATE_PRE = 0;
    public final int STATE_OPEN = 1;
    public final int STATE_END = 2;

    public int Cuttent_STATE = 0;
    public final int UP_MESSAGE = 0;
    /**
     * 直播已经开始多少秒
     */
    long baseSecond;


    BookDialog bookDialog;
    ShareCourseDetailsDialog mShare;

    LiveDetailsPresenter presenter;
    String liveId;
    String type; //  1是电脑，2是手机
    String livePullURL;
    DetailsCourseResponse.BodyEntity.LiveEntity.AuthorEntity author;

    /**
     * 播放器配置
     */
    private NEMediaController mMediaController; //用于控制播放
    private String mVideoPath; //文件路径
    private String mDecodeType;//解码类型，硬解或软解
    private String mMediaType; //媒体类型

    private boolean pauseInBackgroud = true;
    private boolean mHardware = true;


    private DBManager dbManager;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_details_course;
    }

    @Override
    protected void initExtraData() {
        videoView.setBufferStrategy(1); //直播低延时
        mMediaType = "videoondemand";
        liveId = getIntent().getStringExtra("liveid");
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initVariables() {
        dbManager = new DBManager(this);
    }

    @Override
    protected void initViews() {
        presenter = new LiveDetailsPresenter(this, this);
        bookDialog = new BookDialog(this);
        mShare = new ShareCourseDetailsDialog(this,this);
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


        setFocusView(tvFocus, false);


    }


    @Override
    protected void onResume() {
        super.onResume();
//        loadData();
    }

    private void initVideoView() {
        videoView.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer neLivePlayer) {
                hideDialog();
                //  videoThumb.setVisibility(View.INVISIBLE);
                //  initController();
            }
        });


        videoView.setMediaType(mMediaType);
        videoView.setHardwareDecoder(mHardware);
        videoView.setPauseInBackground(pauseInBackgroud);
    }

    private void initController() {

        NEMediaController controller = new NEMediaController(this);
        mMediaController = controller;
        mMediaController.setChangeModeListener(new NEMediaController.OnChangeModeListener() {

            @Override
            public void ChangeMode() {
                // TODO Auto-generated method stub
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);

                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                }
            }
        });

        mMediaController.setOnShownListener(mOnShowListener); //监听mediacontroller是否显示
        mMediaController.setOnHiddenListener(mOnHiddenListener); //监听mediacontroller是否隐藏
        videoView.setMediaController(mMediaController);

    }

    private void playUrlVideo(String url) {
        // videoThumb.setVisibility(View.VISIBLE);
        LogUtil.i("new1", "开始切换视频1");
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            videoView.release_resource();
        }

        videoView.seekAndChangeUrl(0, url);

        showDialog("正在加载...");
        //   setFileName(name);
//       videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.start();
    }

    /**
     * 设置当前直播间状态 0未开始直播-1直播中-2直播结束
     *
     * @param currentState 当前模式
     * @param sec          倒计时，直播计时 的基础时间
     */
    private void setCurrentMode(final int currentState, long sec) {

        Cuttent_STATE = currentState;

        if (Cuttent_STATE == STATE_PRE) {
            ivBook.setVisibility(View.VISIBLE);
            tvTime.setBackgroundResource(R.drawable.bg_btn_yellow);
            tvTime.setTextColor(mResources.getColor(R.color.text_gray_deep));
            tvTips.setText("温馨提示:可提前十分钟进入直播间哦");

            MyCountDownTimer countTime = new MyCountDownTimer(sec * 1000, 1000, tvTime);
            countTime.setText("距直播 : ");
            countTime.setCountFinish(new MyCountDownTimer.OnCountFinish() {
                @Override
                public void finish() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setCurrentMode(STATE_OPEN, 0);
                        }
                    });
                }
            });
            countTime.start();
        } else if (Cuttent_STATE == STATE_OPEN) {
            ivBook.setVisibility(View.INVISIBLE);
            tvTime.setBackgroundResource(R.drawable.bg_btn_orange);
            tvTime.setTextColor(mResources.getColor(R.color.text_gray_deep));
            tvTips.setText("温馨提示:点击进去直播间");
            baseSecond = sec;
            handler.sendEmptyMessage(UP_MESSAGE);


        } else if (Cuttent_STATE == STATE_END) {
            ivBook.setVisibility(View.INVISIBLE);
            tvTime.setBackgroundResource(R.drawable.sl_btn_send);
            tvTime.setTextColor(mResources.getColor(R.color.white));
            tvTips.setText("温馨提示:直播已经结束了");
            //  countTime.setText("已结束直播");
            tvTime.setText("已结束直播");
             tvTime.setEnabled(false);
        }

    }

    @Override
    protected void loadData() {
        showDialog("正在加载...");
        if (WRStarApplication.getUser() != null) {
            presenter.getDetailsCourseInfo(WRStarApplication.getUser().mobnum, liveId);
        }

    }


    private void setFocusView(View v, boolean isFocus) {
        TextView tv = (TextView) v;
        tv.setTag(isFocus);
        if ((boolean) v.getTag()) {

            Drawable drawable = ContextCompat.getDrawable(DetailCourseActivity.this, R.mipmap.ic_gou);
            drawable.setBounds(0, 0, SysUtil.dp2px(getBaseContext(), 14), SysUtil.dp2px(getBaseContext(), 12));
            tv.setCompoundDrawables(drawable, null, null, null);
            tv.setTextColor(ContextCompat.getColor(DetailCourseActivity.this, R.color.text_orange));
            tv.setBackgroundResource(R.mipmap.ic_focus);

            tv.setText("已关注");
        } else {
            tv.setText("关注");

            tv.setTextColor(ContextCompat.getColor(DetailCourseActivity.this, R.color.text_gray_deep));
            tv.setBackgroundResource(R.drawable.bg_btn_yellow);
            Drawable drawable = ContextCompat.getDrawable(DetailCourseActivity.this, R.mipmap.iv_focus_add);
            drawable.setBounds(0, 0, SysUtil.dp2px(getBaseContext(), 12), SysUtil.dp2px(getBaseContext(), 12));
            tv.setCompoundDrawables(drawable, null, null, null);

        }
    }


    @Override
    protected String getPageTag() {
        return TAG;
    }


    @OnClick({R.id.tv_time, R.id.iv_share, R.id.iv_book, R.id.tv_focus, R.id.iv_play})
    public void onClick(View view) {
        if (SysUtil.isFastClick()) {
            return;
        }

        switch (view.getId()) {
            case R.id.tv_time:
                if (livePullURL != null) {
                    if(type.equals("1")){
                        Intent ee = new Intent(DetailCourseActivity.this, LiveActivity.class);
                        ee.putExtra("liveId", liveId);
                        ee.putExtra("livePullURL", livePullURL);
                        ee.putExtra("liveUserId", author.userid);
                        startActivity(ee);
                    }else{
                          /*最新的*/
                        Intent itttt = new Intent(DetailCourseActivity.this, LiveActivity2.class);
                        itttt.putExtra("local", local);
                        itttt.putExtra("liveId", liveId);
                        itttt.putExtra("livePullURL", livePullURL);
                        itttt.putExtra("liveUserId", author.userid);
                        itttt.putExtra("liveUserName", author.username);
                        itttt.putExtra("liveUserLogo", author.logourl);
                        itttt.putExtra("isFocus",iaFocus);

//                        PreferenceUtils.setLocal(this,local);

                        PreferenceUtils.setliveUserId(this,author.userid);
                        PreferenceUtils.setliveUserName(this,author.username);
                        PreferenceUtils.setliveUserLogo(this,author.logourl);
                        startActivity(itttt);
                    }
                } else {
                    if (WRStarApplication.getUser() != null) {
                        presenter.getDetailsCourseInfo(WRStarApplication.getUser().mobnum, liveId);
                    }
                }

                break;
            case R.id.iv_share:

                mShare.show();
                successPresenter=new ShareSuccessPresenter(this,this);
                successPresenter.share(PreferenceUtils.getliveId(this),PreferenceUtils.getToken(this));
                break;
            case R.id.iv_book:

                presenter.bookLive(liveId, true);
                break;
            case R.id.tv_focus:


                presenter.setFocus(author.userid, !((boolean) tvFocus.getTag()));

                break;

            case R.id.iv_play:
                playUrlVideo(live.videoUrl);
                ivPlay.setVisibility(View.GONE);
                break;
        }
    }

    boolean iaFocus;
    private ShareSuccessPresenter successPresenter;
    @Override
    public void getCourseDetailsSuccess(DetailsCourseResponse detailsCourseResponse) {
        hideDialog();

        PreferenceUtils.setCourseDetailTitle(this,detailsCourseResponse.body.live.invitetitle);
        PreferenceUtils.setCourseDetailContext(this,detailsCourseResponse.body.live.inviteurl);
        PreferenceUtils.setCourseDetailIcon(this,detailsCourseResponse.body.live.inviteicon);


        live = detailsCourseResponse.body.live;
        livePullURL = live.pullUrl;

        dbManager.insertWatchRecord(live.id, System.currentTimeMillis() + "", live.imgUrl, DBHandler.TYPE_Live, live.name);

         iaFocus=detailsCourseResponse.body.attention;
        setFocusView(tvFocus, detailsCourseResponse.body.attention);


        tvCourseName.setText(live.name);
        tvDec.setText(live.summary);
        //TODO
        //

        LogUtil.i("live.videoUrl:" + live.videoUrl);
        if (live.videoUrl != null && !TextUtils.isEmpty(live.videoUrl)) {
            videoView.setVisibility(View.VISIBLE);
            ivPlay.setVisibility(View.VISIBLE);
            ivCourse.setVisibility(View.INVISIBLE);
            initController();
            initVideoView();
            //    playUrlVideo(live.videoUrl); 改为点击播放
            scrollview2.setOnTouchListener(mScrollviewListener);

            PreferenceUtils.setCourseTrailer(this, live.videoUrl);
        } else {
            if (live.imgUrl != null) {
                videoView.setVisibility(View.INVISIBLE);
                ivPlay.setVisibility(View.INVISIBLE);
                ivCourse.setVisibility(View.VISIBLE);
                WRStarApplication.imageLoader.displayImage(live.imgUrl, ivCourse, WRStarApplication.getListOptions());
            }
        }


        setCurrentMode(live.runstate, live.interval);

        local=detailsCourseResponse.body.live.author.local;  /**地址***/
        author = detailsCourseResponse.body.live.author;
        tvAuthor.setText(author.username);
        ratingbar.setStar(author.star);
        tvAuthorDec.setText(author.description);
        WRStarApplication.imageLoader.displayImage(author.logourl, ivAuthor, WRStarApplication.getListOptions());


    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case UP_MESSAGE:
                    baseSecond += 1;
                    tvTime.setText("直播中 : " + TimeUtils.formatTime(baseSecond * 1000));
                    msg = obtainMessage(UP_MESSAGE);
                    sendMessageDelayed(msg, 1000);

                    break;
            }


        }
    };


    @Override
    public void getCourseDetailsFailed(String dec) {

    }

    @Override
    public void setfocuseSuccess() {
        setFocusView(tvFocus, true);
    }

    @Override
    public void setfocuseCancelSuccess() {
        setFocusView(tvFocus, false);
    }

    @Override
    public void bookLiveSuccess() {
        bookDialog.show();
        ivBook.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onDestroy() {
//        mHandler.removeMessages(UP_MESSAGE);   报空指针异常
        super.onDestroy();

    }


    NEMediaController.OnShownListener mOnShowListener = new NEMediaController.OnShownListener() {

        @Override
        public void onShown() {
//            playToolbar.setVisibility(View.VISIBLE);
//            playToolbar.requestLayout();
            videoView.invalidate();
            //playToolbar.postInvalidate();
        }
    };

    NEMediaController.OnHiddenListener mOnHiddenListener = new NEMediaController.OnHiddenListener() {

        @Override
        public void onHidden() {
            // playToolbar.setVisibility(View.INVISIBLE);
        }
    };

    View.OnTouchListener mScrollviewListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mMediaController != null & mMediaController.ismIsFullScreen();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
    }

    @Override
    public void ShareSuccess(ShareSuccessResponse response) {
        successPresenter=null;
//        showToast("分享成功！");
    }

    @Override
    public void ShareFailed(String dec) {
//        showToast("分享失败！"+dec);
    }
}
