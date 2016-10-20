package com.wmx.android.wrstar.views.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.fragements.GongxianFragment;
import com.wmx.android.wrstar.views.fragements.RecommendFragment;
import com.wmx.android.wrstar.views.fragements.YaoYueFragment;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import butterknife.Bind;

public class MyRecommend extends AbsBaseActivity {

    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;

    @Bind(R.id.pager_tabstrip)
    PagerSlidingTabStrip pagerTabstrip;
    @Bind(R.id.viewpager_tab)
    ViewPager viewpagerTab;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_my_recommend;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

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
    }

    @Override
    protected void initViews() {

        viewpagerTab.setAdapter(new MyTabStripAdapter(getSupportFragmentManager()));
        pagerTabstrip.setViewPager(viewpagerTab);

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected String getPageTag() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class MyTabStripAdapter extends FragmentPagerAdapter {

        String[] title = {"推荐榜", "邀约榜", "贡献榜"};
        RecommendFragment recommendFragment;
        YaoYueFragment yaoyueFragment;
        GongxianFragment gongxianFragment;

        public MyTabStripAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (recommendFragment == null) {
                        recommendFragment = new RecommendFragment();
//                        return recommendFragment;
                    }
                    return recommendFragment;
                case 1:
                    if (yaoyueFragment == null) {
                        yaoyueFragment = new YaoYueFragment();
//                        return yaoyueFragment;
                    }
                    return yaoyueFragment;
                case 2:
                    if (gongxianFragment == null) {
                        gongxianFragment = new GongxianFragment();
//                        return gongxianFragment;
                    }
                    return gongxianFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
