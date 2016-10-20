package com.wmx.android.wrstar.views.fragements;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.HomePageResponse;
import com.wmx.android.wrstar.mvp.presenters.HomePagePresenter;
import com.wmx.android.wrstar.mvp.views.HomePageView;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.activities.DetailVideoActivity2;
import com.wmx.android.wrstar.views.activities.HomeDetialsActivity;
import com.wmx.android.wrstar.views.base.AbsBaseFragment;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.widgets.RollPager.RollPagerView;
import com.wmx.android.wrstar.views.widgets.RollPager.adapter.LoopPagerAdapter;
import com.wmx.android.wrstar.views.widgets.RollPager.hintview.IconHintView;

import java.util.List;

import butterknife.Bind;


public class HomeFragment2 extends AbsBaseFragment implements HomePageView,View.OnClickListener  {

    public static final String TAG = "HomeFragment2";
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.scrollView_home2)
    ScrollView scrollView_home2;
    @Bind(R.id.tv_wrstart_hot_numbers)
    TextView tv_wrstart_hot_numbers;
    @Bind(R.id.tv_wrstart_zuixin_numbers)
    TextView tv_wrstart_zuixin_numbers;
    @Bind(R.id.tv_retry)
    TextView tvRetry;
    @Bind(R.id.iv_wmx_recom)
    ImageView iv_wmx_recom;
    @Bind(R.id.tv_zhuanlan_contemt)
    TextView tv_zhuanlan_contemt;
    @Bind(R.id.tv_wmx_zhuanglan_number)
    TextView tv_wmx_zhuanglan_number;
    @Bind(R.id.net_error)
    LinearLayout lyNetError;
    @Bind(R.id.ll_wmx_todayrecommend)
    LinearLayout ll_wmx_todayrecommend;
    @Bind(R.id.layout_zhuanlan)
    LinearLayout layout_zhuanlan;
    @Bind(R.id.layout_wrstart_hot)
    LinearLayout layout_wrstart_hot;
    @Bind(R.id.layout_wrstart_zuixinshang)
    LinearLayout layout_wrstart_zuixinshang;
    @Bind(R.id.roll_pagerview)
    RollPagerView rollPagerView;

    TestLoopAdapter loopAdapter;

    HomePagePresenter presenter;

    private  String toDayRecomCoursId;

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_home2;

    }


    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        presenter=new HomePagePresenter(this,this);
        ll_wmx_todayrecommend.setOnClickListener(this);
        tv_wrstart_hot_numbers.setOnClickListener(this);
        tv_wrstart_zuixin_numbers.setOnClickListener(this);

        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog("正在重新加载...");
                loadData();
            }
        });

        rollPagerView.setHintPadding(0, 0, 0, SysUtil.dp2px(getContext(), 8));
        rollPagerView.setPlayDelay(2000);
        rollPagerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SysUtil.dp2px(getContext(), 200)));

        actionBar.setTitle("首页");
    }


    @Override
    public void loadData() {
        presenter.getHomeList(PreferenceUtils.getToken(getActivity()));
    }

    @Override
    protected String getPageTag() {
        return TAG;
    }


    public void testDatasZhuanlan(List<HomePageResponse.BodyEntity.WrstarsEntity> wrstars){

        for (int i = 0; i < wrstars.size(); i++) {
            final  HomePageResponse.BodyEntity.WrstarsEntity wrstarsEntity =wrstars.get(i);
            View view = View.inflate(getActivity(), R.layout.item_wrstart_zhuanlan, null);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (WRStarApplication.getUser() == null) {
                        login();
                        return;
                    }
                    Intent t = new Intent(getActivity(), DetailVideoActivity2.class);
                    t.putExtra("courseid", wrstarsEntity.courseid);
                    startActivity(t);
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SysUtil.dp2px(getActivity(), 185), SysUtil.dp2px(getActivity(), 170));
            int margin = SysUtil.dp2px(getActivity(), 10);
            lp.leftMargin = margin;
            lp.topMargin = margin;
            lp.bottomMargin = margin;
            view.setLayoutParams(lp);

            LinearLayout linner_collect = (LinearLayout) view.findViewById(R.id.linner_collect);
            ImageView iv_wmx_zhanglan = (ImageView) view.findViewById(R.id.iv_wmx_zhanglan);
            TextView tv_zhuanlan_title = (TextView) view.findViewById(R.id.tv_zhuanlan_title);
            TextView tv_zhuanlan_collect = (TextView) view.findViewById(R.id.tv_zhuanlan_collect);
            ImageView img_zhuanlan_collect = (ImageView) view.findViewById(R.id.img_zhuanlan_collect);

            WRStarApplication.imageLoader.displayImage(wrstars.get(i).imgurl, iv_wmx_zhanglan, WRStarApplication.getListOptions());
            tv_zhuanlan_title.setText(wrstars.get(i).name);
            if(wrstars.get(i).iscollect){
                img_zhuanlan_collect.setImageDrawable(mResources.getDrawable(R.mipmap.img_collect_select));
                tv_zhuanlan_collect.setText("已收藏");
                tv_zhuanlan_collect.setTextColor(getResources().getColor(R.color.bg_yellow));
                linner_collect.setBackgroundResource(R.drawable.bg_btn_rect_line_white);
            }else{
                img_zhuanlan_collect.setImageDrawable(mResources.getDrawable(R.mipmap.img_collect));
                tv_zhuanlan_collect.setText("收藏");
                tv_zhuanlan_collect.setTextColor(getResources().getColor(R.color.bg_black));
                linner_collect.setBackgroundResource(R.drawable.bg_btn_rect_yellow_line_yellow);
            }
            layout_zhuanlan.addView(view);
        }

    }
    public void testDatasZuiXin(List<HomePageResponse.BodyEntity.NewestsEntity> newests){

        if(newests.size()>3){

            WRStarApplication.newests=newests;

            for (int i = 0; i < 3; i++) {

                final  HomePageResponse.BodyEntity.NewestsEntity newestsEntity =newests.get(i);

                View view = View.inflate(getActivity(), R.layout.item_interested, null);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (WRStarApplication.getUser() == null) {
                            login();
                            return;
                        }
                        Intent t = new Intent(getActivity(), DetailVideoActivity2.class);
                        t.putExtra("courseid", newestsEntity.courseid);
                        startActivity(t);
                    }
                });
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SysUtil.dp2px(getActivity(), 180), SysUtil.dp2px(getActivity(), 185));
                int margin = SysUtil.dp2px(getActivity(), 10);
                lp.leftMargin = margin;
                lp.topMargin = margin;
                lp.bottomMargin = margin;
                view.setLayoutParams(lp);

                ImageView iv_wmx_zuixin = (ImageView) view.findViewById(R.id.iv_wmx_zuixin);
                TextView tv_wmx_zuixin_name = (TextView) view.findViewById(R.id.tv_wmx_zuixin_name);

                WRStarApplication.imageLoader.displayImage(newests.get(i).imgurl, iv_wmx_zuixin, WRStarApplication.getListOptions());
                tv_wmx_zuixin_name.setText(newests.get(i).name);

                layout_wrstart_zuixinshang.addView(view);
            }
        }else{
            for (int i = 0; i < newests.size(); i++) {

                final  HomePageResponse.BodyEntity.NewestsEntity newestsEntity =newests.get(i);

                View view = View.inflate(getActivity(), R.layout.item_interested, null);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (WRStarApplication.getUser() == null) {
                            login();
                            return;
                        }
                        Intent t = new Intent(getActivity(), DetailVideoActivity2.class);
                        t.putExtra("courseid", newestsEntity.courseid);
                        startActivity(t);
                    }
                });
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SysUtil.dp2px(getActivity(), 180), SysUtil.dp2px(getActivity(), 185));
                int margin = SysUtil.dp2px(getActivity(), 10);
                lp.leftMargin = margin;
                lp.topMargin = margin;
                lp.bottomMargin = margin;
                view.setLayoutParams(lp);

                ImageView iv_wmx_zuixin = (ImageView) view.findViewById(R.id.iv_wmx_zuixin);
                TextView tv_wmx_zuixin_name = (TextView) view.findViewById(R.id.tv_wmx_zuixin_name);

                WRStarApplication.imageLoader.displayImage(newests.get(i).imgurl, iv_wmx_zuixin, WRStarApplication.getListOptions());
                tv_wmx_zuixin_name.setText(newests.get(i).name);

                layout_wrstart_zuixinshang.addView(view);
            }
        }
    }
    public void testDatasZuiRe(final List<HomePageResponse.BodyEntity.HeatsEntity> heats){

        if(heats.size()>3){

            WRStarApplication.heats=heats;

            for (int i = 0; i < 3; i++) {

                final  HomePageResponse.BodyEntity.HeatsEntity entity =heats.get(i);

                View view = View.inflate(getActivity(), R.layout.item_interested, null);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (WRStarApplication.getUser() == null) {
                            login();
                            return;
                        }
                        Intent t = new Intent(getActivity(), DetailVideoActivity2.class);
                        t.putExtra("courseid", entity.courseid);
                        startActivity(t);
                    }
                });
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SysUtil.dp2px(getActivity(), 180), SysUtil.dp2px(getActivity(), 185));
                int margin = SysUtil.dp2px(getActivity(), 10);
                lp.leftMargin = margin;
                lp.topMargin = margin;
                lp.bottomMargin = margin;
                view.setLayoutParams(lp);

                ImageView iv_wmx_zuixin = (ImageView) view.findViewById(R.id.iv_wmx_zuixin);
                TextView tv_wmx_zuixin_name = (TextView) view.findViewById(R.id.tv_wmx_zuixin_name);

                WRStarApplication.imageLoader.displayImage(heats.get(i).imgurl, iv_wmx_zuixin, WRStarApplication.getListOptions());
                tv_wmx_zuixin_name.setText(heats.get(i).name);

                layout_wrstart_hot.addView(view);
            }
        }else{
            for ( int i = 0; i < heats.size();i++) {

               final  HomePageResponse.BodyEntity.HeatsEntity entity =heats.get(i);

                View view = View.inflate(getActivity(), R.layout.item_interested, null);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (WRStarApplication.getUser() == null) {
                            login();
                            return;
                        }
                        Intent t = new Intent(getActivity(), DetailVideoActivity2.class);
                        t.putExtra("courseid", entity.courseid);
                        startActivity(t);
                    }
                });
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SysUtil.dp2px(getActivity(), 180), SysUtil.dp2px(getActivity(), 185));
                int margin = SysUtil.dp2px(getActivity(), 10);
                lp.leftMargin = margin;
                lp.topMargin = margin;
                lp.bottomMargin = margin;
                view.setLayoutParams(lp);

                ImageView iv_wmx_zuixin = (ImageView) view.findViewById(R.id.iv_wmx_zuixin);
                TextView tv_wmx_zuixin_name = (TextView) view.findViewById(R.id.tv_wmx_zuixin_name);

                WRStarApplication.imageLoader.displayImage(heats.get(i).imgurl, iv_wmx_zuixin, WRStarApplication.getListOptions());
                tv_wmx_zuixin_name.setText(heats.get(i).name);

                layout_wrstart_hot.addView(view);
            }
        }

    }
    @Override
    public void getHomeListSuccess(HomePageResponse response) {

        scrollView_home2.setVisibility(View.VISIBLE);
        lyNetError.setVisibility(View.GONE);

        if(response!=null){
            /*********************轮播***************************/
            rollPagerView.setAdapter(loopAdapter = new TestLoopAdapter(rollPagerView, response.body.ads));
            rollPagerView.setHintView(new IconHintView(getActivity(), R.mipmap.vp_now_yellow, R.mipmap.vp_other, 20));
            loopAdapter.notifyDataSetChanged();

            /*********************今日推荐***************************/
            toDayRecomCoursId=response.body.todayrecommend.courseid;

            tv_zhuanlan_contemt.setText(response.body.todayrecommend.summary.substring(0,80)+"......");
            WRStarApplication.imageLoader.displayImage(response.body.todayrecommend.imgurl, iv_wmx_recom, WRStarApplication.getListOptions());

            /*********************微明星专栏***************************/
        if(response.body.wrstars.size()>0&&response.body.wrstars!=null){
            tv_wmx_zhuanglan_number.setText("微明星专栏（"+response.body.wrstars.size()+"）");
            testDatasZhuanlan(response.body.wrstars);
        }

            /*********************最新上***************************/
            if(response.body.newests.size()>0&&response.body.newests!=null){
                tv_wrstart_zuixin_numbers.setText("查看全部"+response.body.newests.size()+"个");
                testDatasZuiXin(response.body.newests);
            }
            /*********************最热上***************************/
            if(response.body.heats.size()>0&&response.body.heats!=null){
                tv_wrstart_hot_numbers.setText("查看全部"+response.body.heats.size()+"个");
                testDatasZuiRe(response.body.heats);
            }
        }

    }

    @Override
    public void getHomeListFailed() {
        scrollView_home2.setVisibility(View.GONE);
        lyNetError.setVisibility(View.VISIBLE);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_wrstart_hot_numbers:  // 最热门
                Intent it = new Intent(getActivity(), HomeDetialsActivity.class);
                it.putExtra("type", "zuire");
                startActivity(it);
                break;
            case R.id.tv_wrstart_zuixin_numbers: // 最新上
                Intent ite = new Intent(getActivity(), HomeDetialsActivity.class);
                ite.putExtra("type", "zuixin");
                startActivity(ite);
                break;
            case R.id.ll_wmx_todayrecommend:  // 今日推荐

                if (WRStarApplication.getUser() != null) {
                    Intent t = new Intent(getActivity(), DetailVideoActivity2.class);
                    t.putExtra("courseid", toDayRecomCoursId);
                    startActivity(t);
                } else {
                    login();
                }
                break;
        }
    }

    private class TestLoopAdapter extends LoopPagerAdapter {


        private List<HomePageResponse.BodyEntity.AdsEntity> banners;


        public TestLoopAdapter(RollPagerView viewPager, List<HomePageResponse.BodyEntity.AdsEntity> banners) {
            super(viewPager);
            this.banners = banners;

        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            WRStarApplication.imageLoader.displayImage(banners.get(position).imgurl, view, WRStarApplication.getListOptions());
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            //点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (WRStarApplication.getUser() == null) {
                        login();
                        return;
                    }
                    openByBrowser(banners.get(position).skipurl);
                }
            });


            return view;
        }

        @Override
        public int getRealCount() {
            return banners.size();
        }

    }

    private void openByBrowser(String mShareLink) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(mShareLink);
            intent.setData(uri);
            startActivity(intent);
    }
}
