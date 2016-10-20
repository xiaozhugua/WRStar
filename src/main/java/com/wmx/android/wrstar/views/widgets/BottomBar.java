package com.wmx.android.wrstar.views.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmx.android.wrstar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wubiao on 2016/2/15
 *
 * Des: 底部栏.
 */
public class BottomBar extends LinearLayout implements View.OnClickListener{

    /** 底部栏的数量. */
    public static int ITEM_COUNT = 5;

    private RelativeLayout re_create_live;
//    private RelativeLayout mRootView;
    private LinearLayout mRootView2;

    private LayoutInflater mLayoutInflater;

    private List<RelativeLayout> mRelativeLayouts;

    private List<TextView> mTextViews;

    private List<ImageView> mImageViews;

    /** 背景图片. */
//    private int[] mBgImgs;

    /** 文字. */
    private String[] mTitles;

    /** 图标, 正常状态. */
    private int [] mIconsNormal;

    /** 图标, 选中状态. */
    private int [] mIconsSelected;

    /** 文字颜色， 正常状态. */
    private int mTextColorNormal;

    /** 文字颜色，选中状态. */
    private int mTextColorSelected;

    /** 被选中项的索引. */
    private int mSelectedIndex = -1;

    private OnBottomBarSelectedListener mBottomBarSelectedListener;

    public BottomBar(Context context) {
        this(context, null, 0);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
        intiViews(context);
    }

    private void initData(Context context){
        mLayoutInflater = LayoutInflater.from(context);

        Resources resources = context.getResources();

//        // 背景图片
//        mBgImgs = new int[ITEM_COUNT];
//        mBgImgs[0] = R.mipmap.bg_bottom_bar_first;
//        mBgImgs[1] = R.mipmap.bg_bottom_bar_living;
//        mBgImgs[2] = R.mipmap.bg_bottom_bar_video;
//        mBgImgs[3] = R.mipmap.bg_bottom_bar_my;
        // 文字
//        mTitles = resources.getStringArray(R.array.bottom_titles);
        mTitles = new String[ITEM_COUNT];
        mTitles[0] = "首页";
        mTitles[1] = "直播会堂";
        mTitles[2] = "创建直播";
        mTitles[3] = "精彩视频";
        mTitles[4] = "我的";
        //正常状态的图标
        mIconsNormal = new int[ITEM_COUNT];
        mIconsNormal[0] = R.mipmap.ic_bottom_bar_first;
        mIconsNormal[1] = R.mipmap.ic_bottom_bar_living;
        mIconsNormal[2] = R.mipmap.live_zhibo;
        mIconsNormal[3] = R.mipmap.ic_bottom_bar_video;
        mIconsNormal[4] = R.mipmap.ic_bottom_bar_my;
        // 被选中状态的图标
        mIconsSelected = new int[ITEM_COUNT];
        mIconsSelected[0] = R.mipmap.ic_bottom_bar_first_sl;
        mIconsSelected[1] = R.mipmap.ic_bottom_bar_living_sl;
        mIconsSelected[2] = R.mipmap.live_zhibo;
        mIconsSelected[3] = R.mipmap.ic_bottom_bar_video_sl;
        mIconsSelected[4] = R.mipmap.ic_bottom_bar_my_sl;

        mTextColorNormal = resources.getColor(R.color.text_white_50);
        mTextColorSelected = resources.getColor(R.color.text_yellow);
    }


    private void intiViews(Context context){

        mRelativeLayouts = new ArrayList<>();
        mTextViews = new ArrayList<>();
        mImageViews = new ArrayList<>();

//        mRootView = (RelativeLayout) ((RelativeLayout) mLayoutInflater.inflate(R.layout.view_bottom_bar, this)).getChildAt(0);
        mRootView2 = (LinearLayout) ( mLayoutInflater.inflate(R.layout.view_bottom_bar, this));

        RelativeLayout re_first=(RelativeLayout)mRootView2.findViewById(R.id.re_first);
        RelativeLayout re_living=(RelativeLayout)mRootView2.findViewById(R.id.re_living);
        RelativeLayout live_zhibo=(RelativeLayout)mRootView2.findViewById(R.id.live_zhibo);
        RelativeLayout re_video=(RelativeLayout)mRootView2.findViewById(R.id.re_video);
        RelativeLayout re_my=(RelativeLayout)mRootView2.findViewById(R.id.re_my);

        re_first.setTag(0);
        re_living.setTag(1);
        live_zhibo.setTag(2);
        re_video.setTag(3);
        re_my.setTag(4);

        re_first.setOnClickListener(this);
        re_living.setOnClickListener(this);
        live_zhibo.setOnClickListener(this);
        re_video.setOnClickListener(this);
        re_my.setOnClickListener(this);

        mRelativeLayouts.add(re_first);
        mRelativeLayouts.add(re_living);
        mRelativeLayouts.add(live_zhibo);
        mRelativeLayouts.add(re_video);
        mRelativeLayouts.add(re_my);

        for(int i = 0; i < mRelativeLayouts.size(); i++){
//            RelativeLayout item = (LinearLayout) mRootView2.getChildAt(i);
//            item.setTag(i);
//            item.setOnClickListener(this);
//            mRelativeLayouts.add(item);
//            if(i!=2){
                TextView tv = (TextView) mRelativeLayouts.get(i).getChildAt(0);
            if(i!=2){
                tv.setText(mTitles[i]);
            }

                mTextViews.add(tv);
                mImageViews.add((ImageView) mRelativeLayouts.get(i).getChildAt(1));
//            }
        }

        // 初始化时，选中"我的"页面
        selected(4);
    }

    public void selected(int selectedIndex) {

        if (selectedIndex == mSelectedIndex)
            return;

        mSelectedIndex = selectedIndex;

        for (int i = 0; i < ITEM_COUNT; i++) {
                // 设置图标
                mImageViews.get(i).setImageResource(mIconsNormal[i]);
                // 设置文字颜色
                mTextViews.get(i).setTextColor(mTextColorNormal);
        }

        // 设置选中状态
//        mRootView.setBackgroundResource(mBgImgs[selectedIndex]);
        mTextViews.get(selectedIndex).setTextColor(mTextColorSelected);
        mImageViews.get(selectedIndex).setImageResource(mIconsSelected[selectedIndex]);

        if (mBottomBarSelectedListener != null) {
            mBottomBarSelectedListener.onSelected(selectedIndex);
        }
    }
    @Override
    public void onClick(View v) {
        selected((Integer) v.getTag());
    }

    public void setBottomBarSelectedListener(OnBottomBarSelectedListener listener){
        mBottomBarSelectedListener = listener;
    }

    public interface OnBottomBarSelectedListener{
        void onSelected(int selectedIndex);
    }
}
