package com.wmx.android.wrstar.views.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.fragements.GuideFragment;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/22.
 */
public class GuideActivity extends AbsBaseActivity {
    @Bind(R.id.vp_guide)
    ViewPager vpGuide;
    @Bind(R.id.ly_viewGroup)
    LinearLayout lyViewGroup;


    private ArrayList<String> uList;
    private HashMap<Integer, GuideFragment> fragmentList = new HashMap<Integer, GuideFragment>();


    private ImageView[] img = null;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initExtraData() {
        uList = new ArrayList<String>();
        uList.add("drawable://" + R.mipmap.ic_guide_0);
        uList.add("drawable://" + R.mipmap.ic_guide_1);
        uList.add("drawable://" + R.mipmap.ic_guide_2);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {

        vpGuide.setAdapter(new GuideAdapter(getSupportFragmentManager()));


        for (int i = 0; i < uList.size(); i++) {
            GuideFragment fragment = newInstance(uList.get(i));
            fragmentList.put(i, fragment);
        }

        img = new ImageView[uList.size()];
        for (int i = 0; i < uList.size(); i++) {
            img[i] = new ImageView(this);
            if (0 == i) {
                img[i].setBackgroundResource(R.mipmap.vp_now);
            } else {
                img[i].setBackgroundResource(R.mipmap.vp_other);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.width = 10;
            params.height = 10;
            lyViewGroup.addView(img[i], params);
        }


        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int page, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int page) {
                if (page==2){
                    lyViewGroup.setVisibility(View.GONE);
                }else{
                    lyViewGroup.setVisibility(View.VISIBLE);
                }
                check(page);
            }
        });

    }


    private void check(int page) {

        for (int i = 0; i < uList.size(); i++) {
            if (page == i) {
                img[i].setBackgroundResource(R.mipmap.vp_now);
            } else {
                img[i].setBackgroundResource(R.mipmap.vp_other);
            }
        }
    }

    public GuideFragment newInstance(String imagePath) {
        GuideFragment guideFragment = new GuideFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", imagePath);
        bundle.putBoolean("isFinal", imagePath.contains(R.mipmap.ic_guide_2 + "") ? true : false);
        guideFragment.setArguments(bundle);
        return guideFragment;
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
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class GuideAdapter extends FragmentPagerAdapter {
        FragmentManager fm;

        public GuideAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            // super.destroyItem(container, position, object);

            View view = (View) object;
            ((ViewPager) container).removeView(view);
            view = null;

        }

        @Override
        public Fragment getItem(int position) {
            GuideFragment fragment = fragmentList.get(position);
            return fragment;
        }


        @Override
        public int getCount() {
            return uList.size();
        }
    }
}
