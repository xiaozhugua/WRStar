package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
public class DetailVideoActivity2 extends AbsBaseActivity implements IDetailView,ShareSuccessView ,View.OnClickListener{
    public String courseId;

    public static int FitVideoHeight = SysUtil.dp2px(WRStarApplication.getContext(), 200); //视频高150dp,controllerbar 50dp


    public final String Tag = "DetailVideoActivity";

    VideoDetailPresenter presenter;

    LayoutInflater inflater;

    public static void actionStart(Activity activity, String courseid, String mode) {
        Intent intent = new Intent(activity, DetailVideoActivity2.class);
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
    LinearLayout otherview;
    @Bind(R.id.img_backd)
    ImageView img_backd;
    @Bind(R.id.img_vediodetials_recommend_share)
    ImageView img_share;
    @Bind(R.id.img_vediodetials_recommend_collected)
    ImageView ivGood;

    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.play_num)
    TextView playNum;
    @Bind(R.id.tv_img_detials_vedios_name)
    TextView tv_username;
    @Bind(R.id.img_detials_vedios_avatar)
    ImageView img_userAvator;
    @Bind(R.id.cen)
    RelativeLayout cen;
    @Bind(R.id.content_cen)
    TextView content;

    @Bind(R.id.detail_info)
    RelativeLayout detailInfo;
//    @Bind(R.id.color2)
//    View color2;
//    @Bind(R.id.episode_num)
//    TextView episodeNum;
    @Bind(R.id.tv_readmore)
    TextView tv_readmore;

    @Bind(R.id.layout_episode)
    LinearLayout layoutEpisode;

    @Bind(R.id.ly_episode_tips)
    RelativeLayout lyEpisode;
//    @Bind(R.id.color3)
//    View color3;

//    @Bind(R.id.include_buttom)
//    LinearLayout include_buttom;
//    @Bind(R.id.ly_interested)
//    RelativeLayout lyInterested;
//    @Bind(R.id.color4)
//    View color4;
//    @Bind(R.id.ly_comment)
//    RelativeLayout lyComment;
    @Bind(R.id.video_view)
    NEVideoView videoView;

    private boolean flag=true;
//    private String connn;
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

    @Bind(R.id.edit_vediodetials_recommend)
    TextView tv_speak;
    @Bind(R.id.tv_vediodetials_recommend_num)
    TextView tv_vediodetials_recommend_num;


    @Bind(R.id.iv_play)
    ImageView iv_play;


    @Bind(R.id.ly_video)
    RelativeLayout lyVideo;
    @Bind(R.id.ly_vediodetials_recommend_num)
    RelativeLayout ly_vediodetials_recommend_num;


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

    Handler handler=new Handler();


    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_details_video2;
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
    private InputMethodManager inputManager;

    @Override
    protected void initViews() {


        inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        img_backd.setOnClickListener(this);
        img_share.setOnClickListener(this);
        ly_vediodetials_recommend_num.setOnClickListener(this);


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
                    dialog = new CommentDialog(DetailVideoActivity2.this);
                    dialog.setCommentListener(new CommentDialog.ICommentListener() {
                        @Override
                        public void send(String comment) {
                            presenter.sendComment(user.mobnum, courseEntity.courseid, comment);
                        }
                    });
                    dialog.show();
//                    include_buttom.setVisibility(View.GONE);
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

        ly_vediodetials_recommend_num.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView1.scrollTo(0,detailInfo.getHeight()+330);
                    }
                },200);
            }
        });

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEpisode != null)
                    playUrlVideo(currentEpisode);
            }
        });

        tv_readmore.setOnClickListener(this);

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


                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 200);
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
//        showDialog("正在加载...");


        String url = episode.playurl;

        if(!TextUtils.isEmpty(url)){
            videoView.seekAndChangeUrl(0, url);
        }

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
        if(handler!=null){
            handler=null;
        }
        super.onDestroy();
    }


    ArrayList<View> episodeViews = new ArrayList<View>();
    int tempEpisode = 0;
    private  ImageView img_avatar;

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

        WRStarApplication.imageLoader.displayImage(detailViewInfo.body.course.member.logourl, img_userAvator, WRStarApplication.getListOptions());
        tv_username.setText(detailViewInfo.body.course.member.username);
        playNum.setText(detailViewInfo.body.course.watch + "人看过该视频");
        listEpisode = detailViewInfo.body.course.episodes;
        this.detailViewInfo = detailViewInfo;

        courseEntity = detailViewInfo.body.course;
        LogUtil.i("videoDetails", "请求id" + courseEntity.courseid);
        presenter.getComment(courseEntity.courseid);

//        tvTitle.setText(courseEntity.name);


//        showToast("len"+len.length);
        if(courseEntity.summary.length()>60){
            tv_readmore.setVisibility(View.VISIBLE);
            tv_readmore.setText("全文");
            content.setText(courseEntity.summary.substring(0,60)+"...");
        }else{
            tv_readmore.setVisibility(View.GONE);
            content.setText(courseEntity.summary);
        }

//        episodeNum.setText("共" + listEpisode.size() + "集");
        if (detailViewInfo.body.iscollect) {
            ivGood.setImageDrawable(mResources.getDrawable(R.mipmap.img_collect_select));
        } else {
            ivGood.setImageDrawable(mResources.getDrawable(R.mipmap.img_collect));
        }


        TextView[] tvs = new TextView[]{tv1, tv2, tv3};
        for (int i = 0; i < tvs.length; i++) {
            if (courseEntity.tags != null) {
                if (i < courseEntity.tags.size() && courseEntity.tags.get(i) != null) {
                    tvs[i].setText("#"+courseEntity.tags.get(i).name+"#");
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
            View view = View.inflate(this, R.layout.item_episode2, null);
            final int m = i;
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    View eView = episodeViews.get(m);
//                    ((TextView) eView.findViewById(R.id.num)).setTextColor(mResources.getColor(R.color.text_orange));
                    ((TextView) eView.findViewById(R.id.name)).setTextColor(mResources.getColor(R.color.text_orange));
                     img_avatar=(ImageView)findViewById(R.id.img_spisodetwo_prepic);

                    View vv = episodeViews.get(tempEpisode);
//                    ((TextView) vv.findViewById(R.id.num)).setTextColor(mResources.getColor(R.color.text_gray));
                    ((TextView) vv.findViewById(R.id.name)).setTextColor(mResources.getColor(R.color.text_black));

                    tempEpisode = m;

                    playUrlVideo(entity);
                    LogUtil.i(Tag, "entity.name:" + entity.name + "-entity.playurl" + entity.playurl);

                    currentEpisode = entity;

                }
            });

            LayoutParams lp = new LayoutParams(SysUtil.dp2px(this, 150), SysUtil.dp2px(this, 160));
            int margin = SysUtil.dp2px(this, 10);
            lp.leftMargin = margin / 2;
            lp.topMargin = margin;
            lp.bottomMargin = margin;
            lp.rightMargin = margin / 2;
            view.setLayoutParams(lp);

            ImageView img_spisodetwo_prepic = (ImageView) view.findViewById(R.id.img_spisodetwo_prepic);
//            TextView num = (TextView) view.findViewById(R.id.num);
            TextView name = (TextView) view.findViewById(R.id.name);

            name.setText(entity.name + "");
//            num.setText(entity.count + "");
            WRStarApplication.imageLoader.displayImage(entity.imgurl, img_spisodetwo_prepic, WRStarApplication.getListOptions());

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
//                ((TextView) view.findViewById(R.id.num)).setTextColor(mResources.getColor(R.color.text_orange));
                ((TextView) view.findViewById(R.id.name)).setTextColor(mResources.getColor(R.color.text_orange));

            }
        }



    }

    @Override
    public void getCommentSuccess(VideoComment videoComment) {
        if(videoComment!=null){
            tv_vediodetials_recommend_num.setText(videoComment.body.comments.size()+"");
            adapter = new VideoCommentAdapter(getBaseContext(), videoComment, presenter);
            rv_comment.setLayoutManager(new FullyLinearLayoutManager(getBaseContext()));
            rv_comment.setAdapter(adapter);
        }
    }

    @Override
    public void getCommentFailed() {
        showToast("获取评论失败");
    }

    @Override
    public void sendCommentSuccess() {
        showToast("发表评论成功");
        if (dialog != null) {
            dialog.dismiss();
        }
        if(inputManager.isActive()){
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
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
            ivGood.setImageDrawable(mResources.getDrawable(R.mipmap.img_collect));
            showToast("取消收藏成功");
            detailViewInfo.body.iscollect = false;
        } else {
            ivGood.setImageDrawable(mResources.getDrawable(R.mipmap.img_collect_select));
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
            showToast("点赞成功");
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
    public void showShare() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_backd:
                finish();
                break;
            case R.id.img_vediodetials_recommend_share: //分享
                showShare();
                break;
            case R.id.tv_readmore: //展开

                if(flag){
//                    content.setEllipsize(null);
//                    content.setSingleLine(false);
                    content.setText(courseEntity.summary);
                    tv_readmore.setText("收起");
                    flag=false;
                }else{
//                    content.setEllipsize(TextUtils.TruncateAt.END);
//                    content.setMaxLines(3);
                    content.setText(courseEntity.summary.substring(0,60)+"...");
                    tv_readmore.setText("全文");
                    flag=true;
                }
                break;
        }
    }
}

