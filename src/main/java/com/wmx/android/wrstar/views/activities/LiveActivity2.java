package com.wmx.android.wrstar.views.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.neliveplayer.NEMediaPlayer;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.FansResponse;
import com.wmx.android.wrstar.biz.response.LiveGiftResponse;
import com.wmx.android.wrstar.biz.response.OtherUserInfoResponse;
import com.wmx.android.wrstar.chat.LiveChat2;
import com.wmx.android.wrstar.chat.view.DropdownListView;
import com.wmx.android.wrstar.entities.CourseWare;
import com.wmx.android.wrstar.entities.DetailViewInfo;
import com.wmx.android.wrstar.entities.Fans;
import com.wmx.android.wrstar.entities.Gift;
import com.wmx.android.wrstar.entities.User;
import com.wmx.android.wrstar.mvp.adapter.CoursewareAdapter;
import com.wmx.android.wrstar.mvp.adapter.FansAdapter;
import com.wmx.android.wrstar.mvp.presenters.LiveRoomPresenter;
import com.wmx.android.wrstar.mvp.views.ILiveRoomView;
import com.wmx.android.wrstar.player.NEMediaController;
import com.wmx.android.wrstar.player.NEVideoView;
import com.wmx.android.wrstar.testemoji.emoji.EmojiEditText;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.dialog.CommentDialog;
import com.wmx.android.wrstar.views.dialog.DetailsInfoDialog;
import com.wmx.android.wrstar.views.dialog.GoodsDialog;
import com.wmx.android.wrstar.views.dialog.NetErrorDialog;
import com.wmx.android.wrstar.views.dialog.ShareCourseDetailsDialog;
import com.wmx.android.wrstar.views.dialog.TipsGiftDialog;
import com.wmx.android.wrstar.views.dialog.TipsStarDialog;
import com.wmx.android.wrstar.views.widgets.CirImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/24.
 */
public class LiveActivity2 extends AbsBaseActivity implements ILiveRoomView, View.OnClickListener {

    public final static String TAG = "LiveActivity";

    @Bind(R.id.image_kejian)
    public ImageView image_kejian;

    @Bind(R.id.image_guanzhu)
    public TextView tvFocus;

    @Bind(R.id.image_close)
    public ImageView    image_close;
    @Bind(R.id.image_share)
    public ImageView    image_share;
    @Bind(R.id.face_dots_container)
    public LinearLayout face_dots_container;

    @Bind(R.id.image_msg)
    public ImageView image_msg;
    @Bind(R.id.image_good)
    public ImageView image_good;

    @Bind(R.id.chat_face_container)
    public LinearLayout chat_face_container;
    @Bind(R.id.edd)
    public RelativeLayout edd;

    @Bind(R.id.video_view)
    public NEVideoView      videoView;
    @Bind(R.id.input_sms)
    public EmojiEditText etMessage;
    @Bind(R.id.send_sms)
    public Button           btnSend;
    @Bind(R.id.message_chat_listview)
    public DropdownListView mListView;
    @Bind(R.id.image_face)
    public ImageView        imageFace;
    @Bind(R.id.face_viewpager)
    public ViewPager        faceViewpager;
    @Bind(R.id.bottom)
    public LinearLayout     input_bottom;
    @Bind(R.id.rg_live)
    public RadioGroup       rgLive;
    @Bind(R.id.ly_chat)
    public RelativeLayout   lyChat;
    @Bind(R.id.gv_courseware)
    public GridView         gvCourseware;
    @Bind(R.id.ly_courseware)
    public RelativeLayout   lyCourseware;
    @Bind(R.id.gv_fans)
    public GridView         gvFans;
    @Bind(R.id.ly_fans)
    public LinearLayout     lyFans;
    @Bind(R.id.tv_num)
    public TextView         tvNum;
    @Bind(R.id.iv_send_gift)
    public ImageView        ivSendGift;

    @Bind(R.id.rey_buttom)
    public RelativeLayout rey_buttomm;
    @Bind(R.id.otherview)
    RelativeLayout otherview;
    @Bind(R.id.ly_video)
    RelativeLayout lyVideo;
    @Bind(R.id.ly_black)
    View           lyBlack;
    @Bind(R.id.ly_no_courseware)
    LinearLayout   lyNoCourseware;


    ShareCourseDetailsDialog mshare;

    GoodsDialog mGoodsDialog;

    @Bind(R.id.tv_Live_name)
    TextView     mTvLiveName;
    @Bind(R.id.tv_Live_location)
    TextView     mTvLiveLocation;
    @Bind(R.id.img_avatar)
    CirImageView mImgAvatar;
    private View              mBuffer; //用于指示缓冲状态
    private NEMediaController mMediaController; //用于控制播放

    private String mVideoPath; //文件路径
    private String mDecodeType;//解码类型，硬解或软解
    private String mMediaType; //媒体类型

    private boolean pauseInBackgroud = true;
    private boolean mHardware = true;
    private Uri mUri;

    private String mTitle;

    NEMediaPlayer mMediaPlayer = new NEMediaPlayer();

    DetailViewInfo                         detailViewInfo;
    DetailViewInfo.BodyEntity.CourseEntity courseEntity;
    CommentDialog                          dialog;
    User                                   user;

    public String userName;
    public String userLogo;
    public String liveId;
    public String liveUserId;
    public String local;
    boolean isFocus;

    ArrayList<Fans> fansList;


    //  public final static String pullURL = "rtmp://v1.live.126.net/live/6cbc2280bbc94c0f91bbf5f5dd7cca27"; //RTMP
    // public final static String pullURL = "rtmp://v1.live.126.net/live/3a6a2588ce5e411bb52d2c32ed0062b8";


    LiveRoomPresenter presenter;


    private int fansIndex;
    private String type;


    List<CourseWare> courseWareList;

    TipsStarDialog tipsStarDialog;
    TipsGiftDialog tipsGiftDialog;
    /**
     * 聊天模块
     */
    LiveChat2 liveChat2;

    FansAdapter fansAdapter;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_live2;
    }

    @Override
    protected void initExtraData() {
        mMediaType = "livestream"; //媒体类型直播

        local = getIntent().getStringExtra("local");
        liveId = getIntent().getStringExtra("liveId");

        liveUserId = getIntent().getStringExtra("liveUserId");

        mVideoPath = getIntent().getStringExtra("livePullURL");
        userName= getIntent().getStringExtra("liveUserName");
        userLogo= getIntent().getStringExtra("liveUserLogo");
        isFocus=getIntent().getBooleanExtra("isFocus",false);

        /* type  1、代表生活秀    2、代表淘宝秀*/
        type= PreferenceUtils.getLiveType(this);

        videoView.setBufferStrategy(0); //直播低延时
    }


    @Override
    protected void initVariables() {
        mshare = new ShareCourseDetailsDialog(this, this);
        mGoodsDialog=new GoodsDialog(this,this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i("onPause()");
        if (liveChat2 != null) {
            liveChat2.exitRoom();
        }
    }

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        super.onResume();


        if (liveChat2 != null) {
            liveChat2.joinRoom();
        }
    }

    @Override
    protected void initViews() {
        setFocusView(tvFocus, isFocus);
        initPopWindow();

        image_kejian.setOnClickListener(this);
        image_good.setOnClickListener(this);
        image_msg.setOnClickListener(this);
        image_close.setOnClickListener(this);
        image_share.setOnClickListener(this);
        tvFocus.setOnClickListener(this);

        if(!TextUtils.isEmpty(type)&&type.equals("1")){
            edd.setVisibility(View.GONE);
        }

        gvFans.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            LogUtil.i("到底部啦。可以请求刷新咯~~~~~~");
                            loadFansData();
                        }
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        gvFans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                presenter.getOtherUserInfo(fansList.get(position).userid);

                liveChat2.mLvAdapter.getDetailsInfoDialog().setOnFocus(new DetailsInfoDialog.OnFocusListener() {
                    @Override
                    public void setFocus(boolean isFocus) {
                        presenter.setFocus(fansList.get(position).userid, isFocus);
                    }
                });

            }
        });


        //   etMessage.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        etMessage.setHorizontallyScrolling(false);

        ivSendGift.setOnClickListener(new popAction(ivSendGift));

        presenter = new LiveRoomPresenter(this, this);


        liveChat2 = new LiveChat2(this, liveId, presenter);


        rgLive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rg_0:
                        lyCourseware.setVisibility(View.INVISIBLE);
                        lyChat.setVisibility(View.VISIBLE);
                        lyFans.setVisibility(View.INVISIBLE);
                        ivSendGift.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rg_1:

                        if (courseWareList == null) {

                            presenter.getLiveCourseWare(liveId);
                        }

                        ivSendGift.setVisibility(View.GONE);
                        lyCourseware.setVisibility(View.VISIBLE);
                        lyChat.setVisibility(View.INVISIBLE);
                        lyFans.setVisibility(View.INVISIBLE);

                        break;
                    case R.id.rg_2:
                        ivSendGift.setVisibility(View.GONE);
                        lyCourseware.setVisibility(View.INVISIBLE);
                        lyChat.setVisibility(View.INVISIBLE);
                        lyFans.setVisibility(View.VISIBLE);

                        if (fansList == null) {
                            fansList = new ArrayList<Fans>();
                            fansList.clear();
                            fansAdapter = new FansAdapter(getBaseContext(), fansList);
                            gvFans.setAdapter(fansAdapter);
                            loadFansData();
                        }

                        break;
                    default:
                        break;
                }
            }
        });


//        initController();
        initVideoView();
        // playerExit.setOnClickListener(mOnClickEvent); //监听退出播放的事件响应


    }


    /**
     * 初始化弹出的pop
     */
    private LayoutInflater inflater;
    private PopupWindow    popupWindow;
    int        height;
    List<Gift> giftList;


    private void initPopWindow() {
        inflater = LayoutInflater.from(this);
        final View popView = inflater.inflate(R.layout.item_pop_gift,
                null);


        String livegiftJson = PreferenceUtils.getLiveGift(WRStarApplication.getContext());
        Gson gson = new Gson();
        LiveGiftResponse liveGiftResponse = gson.fromJson(livegiftJson, LiveGiftResponse.class);

        giftList = liveGiftResponse.body.gifts;

        Gift star = new Gift();
        star.name = "星豆";
        star.imgurl = "drawable://" + R.mipmap.ic_gift_star;
        star.classifier = "枚";
        giftList.add(star);

        height = SysUtil.dp2px(LiveActivity2.this, (15 + 50) * giftList.size());
        LinearLayout ly_gift = (LinearLayout) popView.findViewById(R.id.ly_gift);


        tipsStarDialog = new TipsStarDialog(LiveActivity2.this);
        tipsStarDialog.setISendStarBeanListener(new TipsStarDialog.ISendStarBeanListener() {
            @Override
            public void send(String starBean) {
                presenter.sendGift(liveId, liveChat2.roomId, null, null, starBean);
            }
        });

        /**
         * 土豪随意
         */
        tipsStarDialog.setRandomStarBeanListener(new TipsStarDialog.IRandomStarBeanListener() {
            @Override
            public void send() {
                presenter.getRandomBean();
            }
        });


        for (int i = 0; i < giftList.size(); i++) {
            final Gift gift = giftList.get(i);
            final View giftView = inflater.inflate(R.layout.item_gitf,
                    null);
            ImageView iv_gift = (ImageView) giftView.findViewById(R.id.iv_gift);
            TextView tv_name = (TextView) giftView.findViewById(R.id.tv_name);
            WRStarApplication.imageLoader.displayImage(gift.imgurl, iv_gift, WRStarApplication.getListOptions());
            tv_name.setText(gift.name);
            ly_gift.addView(giftView);

            iv_gift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gift.name.equals("星豆")) {


                        tipsStarDialog.show();
                    } else {
                        tipsGiftDialog = new TipsGiftDialog(LiveActivity2.this, gift);
                        tipsGiftDialog.setClickListener(new TipsGiftDialog.IClickListener() {
                            @Override
                            public void send(String giftId, String count) {
                                presenter.sendGift(liveId, liveChat2.roomId, giftId, count, null);
                            }
                        });
                        tipsGiftDialog.show();
                    }
                }
            });


        }


        ImageView iv_back = (ImageView) popView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置popwindow出现和消失动画
        // popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
    }

    public void showPop(View parent, int x, int y, final View view) {
        // 设置popwindow显示位置

        LogUtil.i("popupWindow.getHeight() :" + popupWindow.getHeight());
        LogUtil.i("y :" + y);

//
        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, x-height/11, y - height);
        // 获取popwindow焦点
        popupWindow.setFocusable(true);
        // 设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        // 为按钮绑定事件
        // 复制

        popupWindow.update();
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_msg:  /*****輸入評論*****/
//                Toast.makeText(this,"輸入評論",Toast.LENGTH_SHORT).show();
                rey_buttomm.setVisibility(View.GONE);
                input_bottom.setVisibility(View.VISIBLE);
                break;
            case R.id.image_close:  /*****關閉當前頁面*****/
//                Toast.makeText(this,"關閉當前頁面",Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            case R.id.image_share:  /*****分享*****/
//                Toast.makeText(this,"分享",Toast.LENGTH_SHORT).show();
                mshare.show();
                break;
            case R.id.image_guanzhu:  /*****關注*****/
//                Toast.makeText(this,"關注",Toast.LENGTH_SHORT).show();
                presenter.setFocus(liveUserId, !((boolean) tvFocus.getTag()));
                break;
            case R.id.image_good:  /*****商品*****/
//                Toast.makeText(this,"商品",Toast.LENGTH_SHORT).show();
                mGoodsDialog.show();
                break;
            case R.id.image_kejian:  /*****课件*****/
//                Toast.makeText(this,"课件",Toast.LENGTH_SHORT).show();
                Intent  it=new Intent(this,KeJianActivity.class);
                it.putExtra("liveId",liveId);
                startActivity(it);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        if (liveChat2 != null) {
            liveChat2.joinRoom();
        }
//        loadData();
//        loadFansData();
    }


    public class popAction implements View.OnClickListener {

        View view;

        public popAction(View view) {

            this.view = view;

        }


        @Override
        public void onClick(View v) {
            int[] arrayOfInt = new int[2];
            // 获取点击按钮的坐标
            v.getLocationOnScreen(arrayOfInt);
            int x = arrayOfInt[0];
            int y = arrayOfInt[1];
            // System.out.println("x: " + x + " y:" + y + " w: " +
            // v.getMeasuredWidth() + " h: " + v.getMeasuredHeight() );
            showPop(v, x, y, view);
        }
    }


    private void initVideoView() {
        videoView.setOnPreparedListener(new NELivePlayer.OnPreparedListener() {
            @Override
            public void onPrepared(NELivePlayer neLivePlayer) {
                hideDialog();
//                initController();
                lyVideo.setVisibility(View.VISIBLE);
                lyBlack.setVisibility(View.GONE);
            }
        });


        videoView.setNetErrorListener(new NEVideoView.NetErrorListener() {
            @Override
            public void error() {
                NetErrorDialog dialog = new NetErrorDialog(LiveActivity2.this);
                dialog.setClickListener(new NetErrorDialog.IClickListener() {
                    @Override
                    public void send() {
                        loadData();
                    }
                });
                dialog.show();
            }
        });

        videoView.setBufferPrompt(mBuffer);
        videoView.setMediaType(mMediaType);
        videoView.setHardwareDecoder(mHardware);
        videoView.setPauseInBackground(pauseInBackgroud);

        videoView.setOnCompletionListener(new NELivePlayer.OnCompletionListener() {
            @Override
            public void onCompletion(NELivePlayer neLivePlayer) {
                videoView.release_resource();
                lyBlack.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initController() {


        NEMediaController controller = new NEMediaController(this);
        mMediaController = controller;
        mMediaController.setmIsFullScreen(true);
        mMediaController.setChangeModeListener(new NEMediaController.OnChangeModeListener() {

            @Override
            public void ChangeMode() {
                // TODO Auto-generated method stub
                /***************************判断屏幕的方向是否是竖屏************************/
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {


                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    getWindow().setAttributes(attrs);
                    getWindow().addFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


                    RelativeLayout.LayoutParams layoutParams =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    videoView.setLayoutParams(layoutParams);
                    otherview.setVisibility(View.GONE);

                    ivSendGift.setVisibility(View.GONE);


                } else {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().setAttributes(attrs);
                    getWindow().clearFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    ivSendGift.setVisibility(View.VISIBLE);
                    videoView.setLayoutParams(lp);
                    otherview.setVisibility(View.VISIBLE);


                }
            }
        });

        mMediaController.setOnShownListener(mOnShowListener); //监听mediacontroller是否显示
        mMediaController.setOnHiddenListener(mOnHiddenListener); //监听mediacontroller是否隐藏
        videoView.setMediaController(mMediaController);

    }


    @Override
    protected void loadData() {

        mTvLiveLocation.setVisibility(View.VISIBLE);
        mTvLiveLocation.setText(local);
        mTvLiveName.setText(userName);
        WRStarApplication.imageLoader.displayImage(userLogo, mImgAvatar, WRStarApplication.getListOptions());

        playUrlVideo("Live", mVideoPath);

        if (fansList == null) {
            fansList = new ArrayList<Fans>();
            fansAdapter = new FansAdapter(getBaseContext(), fansList);
            gvFans.setAdapter(fansAdapter);
            loadFansData();
        }

    }

    private void playUrlVideo(String name, String url) {
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


    View.OnClickListener mOnClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_exit) {
                videoView.release_resource();
                onDestroy();
                finish();
            }
        }
    };

    NEMediaController.OnShownListener mOnShowListener = new NEMediaController.OnShownListener() {

        @Override
        public void onShown() {
            //    playToolbar.setVisibility(View.VISIBLE);
            //    playToolbar.requestLayout();
            videoView.invalidate();
            //    playToolbar.postInvalidate();
        }
    };

    NEMediaController.OnHiddenListener mOnHiddenListener = new NEMediaController.OnHiddenListener() {

        @Override
        public void onHidden() {
            //    playToolbar.setVisibility(View.INVISIBLE);
        }
    };

    View.OnTouchListener mScrollviewListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return mMediaController != null & mMediaController.ismIsFullScreen();
        }
    };

    @Override
    protected String getPageTag() {
        return null;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event != null && event.getRepeatCount() == 0) {
//
////            if (mMediaController != null & mMediaController.ismIsFullScreen()) {
//            //                mMediaController.scaleScreen(true);
//            //            } else {
//            //                finish();
//            //            }
//
//            finish();
//
//
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void getCourseWareSuccess(List<CourseWare> list) {

        if (list.size() <= 0) {
            showToast("暂无课件");
            lyNoCourseware.setVisibility(View.VISIBLE);
            return;
        }

        courseWareList = list;
        gvCourseware.setAdapter(new CoursewareAdapter(this, courseWareList));

    }


    public void loadFansData() {

        if (fansIndex == -1) {
            showToast("没有更多粉丝了");
            return;
        }

        presenter.getFans(liveUserId, fansIndex);

    }


    @Override
    public void getFansSuccess(FansResponse response) {
        if (response.body.fans.size() <= 0) {
            showToast("该主播暂无粉丝，快来加关注吧");
        }
        fansIndex = response.body.next;

        fansList.clear();
        fansList.addAll(response.body.fans);
        fansAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {

        tipsStarDialog.dismiss();
        videoView.release_resource();
        if (liveChat2 != null) {
            liveChat2.socketEventOff();
        }

        super.onDestroy();


    }

    @Override
    public void sendGiftSuccess() {
        tipsGiftDialog.dismiss();
    }

    @Override
    public void sendStarBeanSuccess() {
        tipsStarDialog.dismiss();
    }

    @Override
    public void getRandomBeanSuccess(final String count) {
        if (tipsStarDialog != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    tipsStarDialog.setRandomStarBean(count);
                }
            });
        }

    }

    @Override
    public void setfocuseSuccess() {
        setFocusView(tvFocus, true);
        liveChat2.mLvAdapter.setFocus(true);
    }

    @Override
    public void setfocuseCancelSuccess() {
        setFocusView(tvFocus, false);
        liveChat2.mLvAdapter.setFocus(false);
    }

    @Override
    public void getOtherUserInfoSuccess(OtherUserInfoResponse response) {

        liveChat2.mLvAdapter.showOtherUserInfoDialog(response);
//        setFocusView(tvFocus, response.body.user.isattention);
    }


    private void setFocusView(View v, boolean isFocus) {
        TextView tv = (TextView) v;
        tv.setTag(isFocus);
        if ((boolean) v.getTag()) {

            Drawable drawable = ContextCompat.getDrawable(LiveActivity2.this, R.mipmap.ic_gou);
            drawable.setBounds(0, 0, SysUtil.dp2px(getBaseContext(), 14), SysUtil.dp2px(getBaseContext(), 12));
            tv.setCompoundDrawables(drawable, null, null, null);
            tv.setTextColor(ContextCompat.getColor(LiveActivity2.this, R.color.text_orange));
            tv.setBackgroundResource(R.mipmap.ic_focus);

            tv.setText("已关注");
        } else {
            tv.setText("关注");

            tv.setTextColor(ContextCompat.getColor(LiveActivity2.this, R.color.text_gray_deep));
            tv.setBackgroundResource(R.drawable.bg_btn_yellow);
            Drawable drawable = ContextCompat.getDrawable(LiveActivity2.this, R.mipmap.img_guanzhu);
            drawable.setBounds(0, 0, SysUtil.dp2px(getBaseContext(), 12), SysUtil.dp2px(getBaseContext(), 12));
            tv.setCompoundDrawables(drawable, null, null, null);

        }
    }
}
