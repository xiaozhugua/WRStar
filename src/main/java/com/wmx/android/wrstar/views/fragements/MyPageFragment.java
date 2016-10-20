package com.wmx.android.wrstar.views.fragements;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.User;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.activities.AboutActivity;
import com.wmx.android.wrstar.views.activities.BuyVIPAcitivty;
import com.wmx.android.wrstar.views.activities.FeedBackActivity;
import com.wmx.android.wrstar.views.activities.MyBuyActivity;
import com.wmx.android.wrstar.views.activities.MyRecommend;
import com.wmx.android.wrstar.views.activities.MyStarBeanActivity;
import com.wmx.android.wrstar.views.activities.MyVideoAcitivity;
import com.wmx.android.wrstar.views.activities.PersonalActivity;
import com.wmx.android.wrstar.views.activities.SettingActivity;
import com.wmx.android.wrstar.views.activities.WatchRecordActivity;
import com.wmx.android.wrstar.views.base.AbsBaseFragment;
import com.wmx.android.wrstar.views.widgets.CirImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wubiao on 2016/2/15
 * <p/>
 * Des: 我的页面.
 */
public class MyPageFragment extends AbsBaseFragment  {

    public static final String TAG = "MyPageFragment";

    @Bind(R.id.rlyt_video)
    public RelativeLayout mRlytVideo;

    @Bind(R.id.rlyt_buying)
    public RelativeLayout mRlytBuying;

    @Bind(R.id.rlyt_star)
    public RelativeLayout mRlytStar;

    @Bind(R.id.rlyt_recommendation)
    public RelativeLayout mRlytRecommendation;

    @Bind(R.id.rlyt_browsing_history)
    public RelativeLayout mRlytBrowsingHistory;

    @Bind(R.id.rlyt_feedback)
    public RelativeLayout mRlytFeedBack;

    @Bind(R.id.rlyt_about_wrstar)
    public RelativeLayout mRlytAboutWrstar;

    @Bind(R.id.rlyt_cutomer_service)
    public RelativeLayout mRlytCustomerService;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.iv_avatar)
    CirImageView ivAvatar;
    @Bind(R.id.divider)
    View divider;
    @Bind(R.id.tv_following)
    TextView tvFollowing;
    @Bind(R.id.tv_follower)
    TextView tvFollower;


    @Bind(R.id.iv_join_vip)
    ImageView ivJoinVIP;
    @Bind(R.id.iv_vip)
    ImageView ivVIP;

    @Bind(R.id.iv_setting2)
    ImageView ivSetting2;


    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_my_page;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {

    }

    private List<String> list = new ArrayList<>();

    @Override
    public void loadData() {


//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LogUtil.d("left: " + mTvTitle.getLeft());
//                LogUtil.d("right: " + mTvTitle.getRight());
//                LogUtil.d("top: " + mTvTitle.getTop());
//                LogUtil.d("bottom: " + mTvTitle.getBottom());
//                LogUtil.d("x: " + mTvTitle.getX());
//                LogUtil.d("y: " + mTvTitle.getY());
//                LogUtil.d("translationX: " + mTvTitle.getTranslationX());
//                LogUtil.d("translationY: " + mTvTitle.getTranslationY());
//            }
//        }, 2000);

    }

    @Override
    protected String getPageTag() {
        return TAG;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (WRStarApplication.getUser() != null) {
            refreshUserInfo();


        } else {
            tvNickname.setText("我的");
            WRStarApplication.imageLoader.displayImage("drawable://" + R.mipmap.ic_avatar_default, ivAvatar, WRStarApplication.getAvatorOptions());


        }
    }

    public void refreshUserInfo() {
        User user = WRStarApplication.getUser();
        if(user!=null){

            tvNickname.setText(user.username);

            WRStarApplication.imageLoader.displayImage(user.userLogo, ivAvatar, WRStarApplication.getAvatorOptions());


            tvFollowing.setText("关注 : " + user.attention);
            tvFollower.setText("粉丝 : " + user.fans);
        }


        if (user.isvip) {
            ivJoinVIP.setVisibility(View.GONE);
            ivVIP.setVisibility(View.VISIBLE);
        } else {
            ivJoinVIP.setVisibility(View.VISIBLE);
            ivVIP.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_nickname, R.id.iv_setting, R.id.iv_setting2,R.id.iv_avatar, R.id.iv_join_vip, R.id.divider, R.id.tv_following, R.id.tv_follower, R.id.rlyt_video, R.id.rlyt_buying, R.id.rlyt_star, R.id.rlyt_recommendation, R.id.rlyt_browsing_history, R.id.rlyt_feedback, R.id.rlyt_about_wrstar, R.id.rlyt_cutomer_service})
    public void onClick(View view) {

        if (SysUtil.isFastClick()) {
            return;
        }

        switch (view.getId()) {
            case R.id.tv_nickname:
            case R.id.iv_avatar:
                if (WRStarApplication.getUser() == null) {
                    login();
                    return;
                }

                Intent it = new Intent(mParentActivity, PersonalActivity.class);
                startActivity(it);

                break;
            case R.id.iv_setting:

                Intent is = new Intent(mParentActivity, SettingActivity.class);
                startActivity(is);

                break;
            case R.id.iv_setting2:

//                Intent ite = new Intent(mParentActivity, CreateLiveActivity.class);
//                startActivity(ite);
//                CreateLiveDialog dialog=new CreateLiveDialog(getActivity());
//                dialog.show();
                break;

            case R.id.iv_join_vip:

                Intent ib = new Intent(mParentActivity, BuyVIPAcitivty.class);
                startActivity(ib);
                break;

            case R.id.divider:
                break;
            case R.id.tv_following:
                break;
            case R.id.tv_follower:
                break;
            case R.id.rlyt_video:
                if (WRStarApplication.getUser() == null) {
                    login();
                    return;
                }
                Intent tv = new Intent(mParentActivity, MyVideoAcitivity.class);
                startActivity(tv);


                break;
            case R.id.rlyt_buying:

                if (WRStarApplication.getUser() == null) {
                    login();
                    return;
                }
                Intent tm = new Intent(mParentActivity, MyBuyActivity.class);
                startActivity(tm);


                break;
            case R.id.rlyt_star:

                if (WRStarApplication.getUser() == null) {
                    login();
                    return;
                }
                Intent iit = new Intent(mParentActivity, MyStarBeanActivity.class);
                startActivity(iit);

                break;
            case R.id.rlyt_recommendation:
                if (WRStarApplication.getUser() == null) {
                    login();
                    return;
                }
                Intent imw = new Intent(mParentActivity, MyRecommend.class);
                startActivity(imw);
                break;
            case R.id.rlyt_browsing_history:
                Intent im = new Intent(mParentActivity, WatchRecordActivity.class);
                startActivity(im);
                break;
            case R.id.rlyt_feedback:
                Intent tf = new Intent(mParentActivity, FeedBackActivity.class);
                startActivity(tf);
                break;
            case R.id.rlyt_about_wrstar:

                Intent ta = new Intent(mParentActivity, AboutActivity.class);
                startActivity(ta);

                break;
            case R.id.rlyt_cutomer_service:
                break;
        }
    }
}
