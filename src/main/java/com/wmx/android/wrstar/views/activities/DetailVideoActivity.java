package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.neliveplayer.NELivePlayer;
import com.netease.neliveplayer.NEMediaPlayer;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.ShareSuccessResponse;
import com.wmx.android.wrstar.constants.Constant;
import com.wmx.android.wrstar.entities.DetailViewInfo;
import com.wmx.android.wrstar.entities.User;
import com.wmx.android.wrstar.entities.VideoComment;
import com.wmx.android.wrstar.mvp.adapter.VideoCommentAdapter;
import com.wmx.android.wrstar.mvp.presenters.ShareSuccessPresenter;
import com.wmx.android.wrstar.mvp.presenters.VideoDetailPresenter;
import com.wmx.android.wrstar.mvp.views.IDetailView;
import com.wmx.android.wrstar.mvp.views.ShareSuccessView;
import com.wmx.android.wrstar.player.NEMediaController;
import com.wmx.android.wrstar.player.NEVideoView;
import com.wmx.android.wrstar.store.DBHandler;
import com.wmx.android.wrstar.store.DBManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.dialog.CommentDialog;
import com.wmx.android.wrstar.views.dialog.ShareDialog;
import com.wmx.android.wrstar.views.widgets.FullyLinearLayoutManager;
import com.wmx.android.wrstar.views.widgets.MyScrollview;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/5/5.
 */
public class DetailVideoActivity extends AbsBaseActivity implements IDetailView,ShareSuccessView {
    public String courseId;

    public static int FitVideoHeight = SysUtil.dp2px(WRStarApplication.getContext(), 200); //视频高150dp,controllerbar 50dp


    public final String Tag = "DetailVideoActivity";

    VideoDetailPresenter presenter;

    LayoutInflater inflater;

    public static void actionStart(Activity activity, String courseid, String mode) {
        Intent intent = new Intent(activity, DetailVideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("courseid", courseid);
        intent.putExtra("mode", mode);
        activity.startActivity(intent);
    }

    /*
            测试点播地址
     */

    // public final static String ondemandURL = "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/qkl%2Fvideos%2F1462799452995%2F030008010057209A527979003E88037AEC9A43-3778-B535-BC17-CC965EA2FFA9.mp4";
    public final static String ondemandURL = "http://vla-photo.oss-cn-hangzhou.aliyuncs.com/vedioout/ld/34d2ec55c51447408645eab316a91730/%E8%B6%8A%E7%8B%B1%E7%AC%AC%E5%9B%9B%E5%AD%A3%E7%AC%AC1%E9%9B%86.flv";


    // public final static String ondemandURL = "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/qkl%2Fpic%2F1462867183297%2F1m1%E5%8E%9F%E5%AD%90.mov";


    public static final String TAG = "DetailVideoActivity";

    public static final int NELP_LOG_UNKNOWN = 0; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEFAULT = 1; //!< log输出模式：输出详细
    public static final int NELP_LOG_VERBOSE = 2; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEBUG = 3; //!< log输出模式：输出调试信息
    public static final int NELP_LOG_INFO = 4; //!< log输出模式：输出标准信息
    public static final int NELP_LOG_WARN = 5; //!< log输出模式：输出警告
    public static final int NELP_LOG_ERROR = 6; //!< log输出模式：输出错误
    public static final int NELP_LOG_FATAL = 7; //!< log输出模式：一些错误信息，如头文件找不到，非法参数使用
    public static final int NELP_LOG_SILENT = 8; //!< log输出模式：不输出
//    @Bind(R.id.buffering_prompt)
//    LinearLayout bufferingPrompt;

    @Bind(R.id.otherview)
    RelativeLayout otherview;

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.play_num)
    TextView playNum;
    @Bind(R.id.cen)
    RelativeLayout cen;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.iv_good)
    ImageView ivGood;
    @Bind(R.id.cmt_num)
    TextView cmtNum;
    @Bind(R.id.detail_info)
    RelativeLayout detailInfo;
    @Bind(R.id.color2)
    View color2;
    @Bind(R.id.episode_num)
    TextView episodeNum;

    @Bind(R.id.layout_episode)
    LinearLayout layoutEpisode;


    @Bind(R.id.iv_share)
    ImageView ivShare;


    @Bind(R.id.ly_episode_tips)
    RelativeLayout lyEpisode;
    @Bind(R.id.color3)
    View color3;
    @Bind(R.id.layout_interested)
    LinearLayout layoutInterested;
    @Bind(R.id.ly_interested)
    RelativeLayout lyInterested;
    @Bind(R.id.color4)
    View color4;
    @Bind(R.id.ly_comment)
    RelativeLayout lyComment;
    @Bind(R.id.video_view)
    NEVideoView videoView;
    //    @Bind(R.id.player_exit)
//    ImageButton playerExit;
//    @Bind(R.id.file_name)
//    TextView fileName;
//    @Bind(R.id.play_toolbar)
//    RelativeLayout playToolbar;
    @Bind(R.id.scrollView1)
    MyScrollview scrollView1;

    @Bind(R.id.rv_comment)
    RecyclerView rv_comment;
    @Bind(R.id.tv_speak)
    TextView tv_speak;


    @Bind(R.id.iv_play)
    ImageView iv_play;


    @Bind(R.id.ly_video)
    RelativeLayout lyVideo;


    private View mBuffer; //用于指示缓冲状态
    private NEMediaController mMediaController; //用于控制播放

    private String mVideoPath; //文件路径
    private String mDecodeType;//解码类型，硬解或软解
    private String mMediaType; //媒体类型

    private boolean pauseInBackgroud = true;
    private boolean mHardware = true;

    private String mTitle;

    NEMediaPlayer mMediaPlayer = new NEMediaPlayer();

    DetailViewInfo detailViewInfo;
    DetailViewInfo.BodyEntity.CourseEntity courseEntity;
    VideoCommentAdapter adapter;
    CommentDialog dialog;
    User user;

    private DBManager dbManager;


    List<DetailViewInfo.BodyEntity.CourseEntity.EpisodesEntity> listEpisode;


    private DetailViewInfo.BodyEntity.CourseEntity.EpisodesEntity currentEpisode;


    AudioManager manager;


    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_details_video;
    }


    @Override
    protected void initExtraData() {
        mMediaType = "videoondemand"; //媒体类型，默认点播

        courseId = getIntent().getStringExtra("courseid");
        mVideoPath = ondemandURL;
        videoView.setBufferStrategy(1); //点播抗抖动

    }

    @Override
    protected void initVariables() {
        user = WRStarApplication.getUser();
    }

    int index = 0;

    @Override
    protected void initViews() {


        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

//        ivShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                index+=10;
//                manager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
//            }
//        });


        dbManager = new DBManager(this);
        presenter = new VideoDetailPresenter(this, this);
        inflater = LayoutInflater.from(this);


        scrollView1.setOnTouchListener(mScrollviewListener);


        //  playerExit.getBackground().setAlpha(0);


        initController();

        initVideoView();
        mMediaPlayer.setLogLevel(NELP_LOG_UNKNOWN); //设置log级别
        //  playUrlVideo("name",mVideoPath);
        //    LogUtil.d(TAG, "Version = " + mMediaPlayer.getVersion()); //获取解码库版本号
        //   playerExit.setOnClickListener(mOnClickEvent); //监听退出播放的事件响应


        tv_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (courseEntity != null) {
                    dialog = new CommentDialog(DetailVideoActivity.this);
                    dialog.setCommentListener(new CommentDialog.ICommentListener() {
                        @Override
                        public void send(String comment) {
                            presenter.sendComment(user.mobnum, courseEntity.courseid, comment);
                        }
                    });
                    dialog.show();
                }
            }
        });
        ivGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SysUtil.isFastClick()) {
                    return;
                }
                showDialog("正在加载...");
                presenter.addVideoLike(user.mobnum, courseId);
            }
        });

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEpisode != null)
//                    playUrlVideo(entity);
                    playUrlVideo(currentEpisode);
            }
        });

    }

    private void initVideoView() {
        videoView.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer neLivePlayer) {
                lyVideo.setVisibility(View.VISIBLE);
                hideDialog();
                iv_play.setVisibility(View.INVISIBLE);
                initController();
            }
        });


        videoView.setBufferPrompt(mBuffer);
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

                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    getWindow().setAttributes(attrs);
                    getWindow().addFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


                    RelativeLayout.LayoutParams layoutParams =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    videoView.setLayoutParams(layoutParams);
                    otherview.setVisibility(View.GONE);

                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().setAttributes(attrs);
                    getWindow().clearFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 240);
                    videoView.setLayoutParams(lp);
                    otherview.setVisibility(View.VISIBLE);
                }
            }
        });

        mMediaController.setOnShownListener(mOnShowListener); //监听mediacontroller是否显示
        //    mMediaController.setOnHiddenListener(mOnHiddenListener); //监听mediacontroller是否隐藏
        videoView.setMediaController(mMediaController);

    }


    private void playUrlVideo(DetailViewInfo.BodyEntity.CourseEntity.EpisodesEntity episode) {

        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            videoView.release_resource();
        }
        showDialog("正在加载...");


        String url = episode.playurl;

        videoView.seekAndChangeUrl(0, url);

        //      setFileName(name);
//       videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.setBufferStrategy(1);
        videoView.start();
    }


//    View.OnClickListener mOnClickEvent = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.player_exit) {
//                videoView.release_resource();
//                onDestroy();
//                finish();
//            }
//        }
//    };

    NEMediaController.OnShownListener mOnShowListener = new NEMediaController.OnShownListener() {

        @Override
        public void onShown() {
//            playToolbar.setVisibility(View.VISIBLE);
//            playToolbar.requestLayout();
            videoView.invalidate();
            //   playToolbar.postInvalidate();
        }
    };

//    NEMediaController.OnHiddenListener mOnHiddenListener = new NEMediaController.OnHiddenListener() {
//
//        @Override
//        public void onHidden() {
//            playToolbar.setVisibility(View.INVISIBLE);
//        }
//    };

    View.OnTouchListener mScrollviewListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mMediaController != null & mMediaController.ismIsFullScreen();
        }
    };


//    public void setFileName(String name) { //设置文件名并显示出来
//        mTitle = name;
//        if (fileName != null)
//            fileName.setText(mTitle);
//
//        fileName.setGravity(Gravity.CENTER);
//    }

    @Override
    protected void loadData() {
        showDialog("正在加载...");
        presenter.getVideoDetails(user.mobnum, courseId, null);
    }

    @Override
    protected String getPageTag() {
        return TAG;
    }


    @Override
    protected void onResume() {
        LogUtil.d(TAG, "NEVideoPlayerActivity onResume");
//        if (pauseInBackgroud && !videoView.isPaused()) {
//            videoView.start(); //锁屏打开后恢复播放
//        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.d(TAG, "NEVideoPlayerActivity onPause");

        if (pauseInBackgroud)
            videoView.pause(); //锁屏时暂停
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        LogUtil.d(TAG, "NEVideoPlayerActivity onDestroy");
        videoView.release_resource();
        super.onDestroy();
    }


    ArrayList<View> episodeViews = new ArrayList<View>();
    int tempEpisode = 0;

    @Override
    public void getDetailViewSuccess(DetailViewInfo detailViewInfo) {
        LogUtil.i(TAG, "getDetailViewSuccess");
        hideDialog();


        PreferenceUtils.setlivdTitle(this,detailViewInfo.body.sharetitle);
        PreferenceUtils.setlivdContext(this,detailViewInfo.body.shareurl);
        PreferenceUtils.setlivdLink(this,detailViewInfo.body.shareicon);

        // showToast("getDetailViewSuccess");
        if (detailViewInfo == null) {
            netError();
            return;
        }

        dbManager.insertWatchRecord(courseId, System.currentTimeMillis() + "", detailViewInfo.body.course.imgurl, DBHandler.TYPE_Ondemand, detailViewInfo.body.course.name);

        cmtNum.setText("热门评论" + detailViewInfo.body.course.comments + "条");
        playNum.setText(detailViewInfo.body.course.watch + "人看过该视频");

        listEpisode = detailViewInfo.body.course.episodes;
        this.detailViewInfo = detailViewInfo;

        courseEntity = detailViewInfo.body.course;
        LogUtil.i("videoDetails", "请求id" + courseEntity.courseid);
        presenter.getComment(courseEntity.courseid);

        tvTitle.setText(courseEntity.name);
        content.setText(courseEntity.summary);
        episodeNum.setText("共" + listEpisode.size() + "集");
        if (detailViewInfo.body.iscollect) {
            ivGood.setImageDrawable(mResources.getDrawable(R.drawable.img_shoucang));
        } else {
            ivGood.setImageDrawable(mResources.getDrawable(R.drawable.img_weishoucang));
        }


        TextView[] tvs = new TextView[]{tv1, tv2, tv3};
        for (int i = 0; i < tvs.length; i++) {
            if (courseEntity.tags != null) {
                if (i < courseEntity.tags.size() && courseEntity.tags.get(i) != null) {
                    tvs[i].setText(courseEntity.tags.get(i).name);
                } else {
                    tvs[i].setVisibility(View.GONE);
                    break;
                }
            } else {
                tvs[i].setVisibility(View.GONE);
            }
        }

        for (int i = 0; i < listEpisode.size(); i++) {
            LogUtil.i("list.size():" + listEpisode.size());

            final DetailViewInfo.BodyEntity.CourseEntity.EpisodesEntity entity = listEpisode.get(i);
            if (entity == null) {
                break;
            }
            View view = View.inflate(this, R.layout.item_episode, null);
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    View eView = episodeViews.get(m);
                    ((TextView) eView.findViewById(R.id.num)).setTextColor(mResources.getColor(R.color.text_orange));
                    ((TextView) eView.findViewById(R.id.name)).setTextColor(mResources.getColor(R.color.text_orange));

                    View vv = episodeViews.get(tempEpisode);
                    ((TextView) vv.findViewById(R.id.num)).setTextColor(mResources.getColor(R.color.text_gray));
                    ((TextView) vv.findViewById(R.id.name)).setTextColor(mResources.getColor(R.color.text_black));

                    tempEpisode = m;

                    playUrlVideo(entity);
                    LogUtil.i(Tag, "entity.name:" + entity.name + "-entity.playurl" + entity.playurl);

                    currentEpisode = entity;

                }
            });

            LayoutParams lp = new LayoutParams(SysUtil.dp2px(this, 145), SysUtil.dp2px(this, 78));
            int margin = SysUtil.dp2px(this, 10);
            lp.leftMargin = margin / 2;
            lp.topMargin = margin;
            lp.bottomMargin = margin;
            lp.rightMargin = margin / 2;
            view.setLayoutParams(lp);

            TextView num = (TextView) view.findViewById(R.id.num);
            TextView name = (TextView) view.findViewById(R.id.name);

            name.setText(entity.name + "");
            num.setText(entity.count + "");

            layoutEpisode.addView(view);
            episodeViews.add(view);
            if (i == 0) {

                /**
                 * WIFI环境下自动播放
                 */
                if (Constant.netType == Constant.WIFI) {
                    playUrlVideo(entity);

                }

                currentEpisode = entity;
                ((TextView) view.findViewById(R.id.num)).setTextColor(mResources.getColor(R.color.text_orange));
                ((TextView) view.findViewById(R.id.name)).setTextColor(mResources.getColor(R.color.text_orange));

            }
        }


        List<DetailViewInfo.BodyEntity.GuessEntity> guessList = detailViewInfo.body.guess;

        for (int i = 0; i < guessList.size(); i++) {
            final DetailViewInfo.BodyEntity.GuessEntity guess = guessList.get(i);

            View view = View.inflate(this, R.layout.item_interested, null);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //   DetailVideoActivity.actionStart(DetailVideoActivity.this,guess.courseid,"ondemand");
                    courseId = guess.courseid;
                    layoutEpisode.removeAllViews();
                    episodeViews.clear();
                    listEpisode.clear();
                    layoutInterested.removeAllViews();
                    loadData();
                }
            });
            LayoutParams lp = new LayoutParams(SysUtil.dp2px(this, 160), SysUtil.dp2px(this, 170));
            int margin = SysUtil.dp2px(this, 10);
            lp.leftMargin = margin;
            lp.topMargin = margin;
            lp.bottomMargin = margin;
            view.setLayoutParams(lp);

            ImageView iv_interested = (ImageView) view.findViewById(R.id.iv_wmx_zuixin);
            TextView tv_interested = (TextView) view.findViewById(R.id.tv_wmx_zuixin_name);
            tv_interested.setText(guess.name + "");


            String tempUrl = "";
            if (guess.imgurl.equals("10088")) {
                tempUrl = "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/qkl%2Fpic%2F1467100630798%2Fimg_liaotian.jpg";
            } else if (guess.imgurl.equals("10085")) {
                tempUrl = "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/qkl%2Fpic%2F1467100632137%2Fimg_moshou.jpg";
            } else if (guess.imgurl.equals("10086")) {
                tempUrl = "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/qkl%2Fpic%2F1467100630205%2Fimg_biyun.jpg";
            } else if (guess.imgurl.equals("10087")) {
                tempUrl = "http://qhcdn.oss-cn-hangzhou.aliyuncs.com/qkl%2Fpic%2F1467100631414%2Fimg_meizhuang.jpg";
            } else {
                tempUrl = guess.imgurl;
            }

            WRStarApplication.imageLoader.displayImage(tempUrl, iv_interested, WRStarApplication.getListOptions());


            layoutInterested.addView(view);


        }

    }

    @Override
    public void getCommentSuccess(VideoComment videoComment) {
        LogUtil.i(TAG, "getCommentSuccess");
        adapter = new VideoCommentAdapter(getBaseContext(), videoComment, presenter);
        rv_comment.setLayoutManager(new FullyLinearLayoutManager(getBaseContext()));
        rv_comment.setAdapter(adapter);
    }

    @Override
    public void getCommentFailed() {
        showToast("获取评论失败");
    }

    @Override
    public void sendCommentSuccess() {
        showToast("发送评论成功");
        if (dialog != null) {
            dialog.dismiss();
        }

        presenter.getComment(courseEntity.courseid);
    }

    @Override
    public void sendCommentFailed() {
        showToast("发送评论失败");
    }

    @Override
    public void addVideoLikeSuccess() {
        hideDialog();
        if (detailViewInfo.body.iscollect) {
            ivGood.setImageDrawable(mResources.getDrawable(R.drawable.img_weishoucang));
            showToast("取消收藏成功");
            detailViewInfo.body.iscollect = false;
        } else {
            ivGood.setImageDrawable(mResources.getDrawable(R.drawable.img_shoucang));
            showToast("收藏成功");
            detailViewInfo.body.iscollect = true;
        }


    }

    @Override
    public void addVideoLikeFaild() {
        showToast("收藏失败");
    }

    @Override
    public void addCommentLikeSuccess(View imageview, View textview, int count) {
        if (adapter != null) {
            adapter.addLikeSuccess(imageview, textview, count);
            showToast("Like 成功");
        }
    }


    @Override
    public void addCommentLikeFaild(String resultDec) {
        showToast(resultDec);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event != null && event.getRepeatCount() == 0) {

            if (mMediaController != null & mMediaController.ismIsFullScreen()) {
                mMediaController.scaleScreen(true);
            } else {
                finish();
            }


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private ShareDialog mShareDialog;
    public void showShare(View v) {
        mShareDialog=new ShareDialog(this,this);
        mShareDialog.show();
        successPresenter=new ShareSuccessPresenter(this,this);
    }
private ShareSuccessPresenter successPresenter;

    @Override
    public void ShareSuccess(ShareSuccessResponse response) {
        successPresenter=null;
//        showToast("分享成功！");
    }

    @Override
    public void ShareFailed(String dec) {
        showToast("分享失败！"+dec);
    }
}

