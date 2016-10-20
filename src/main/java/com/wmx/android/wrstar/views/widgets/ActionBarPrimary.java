package com.wmx.android.wrstar.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.utils.LogUtil;


public class ActionBarPrimary extends RelativeLayout implements OnClickListener {

    private View mRootView;

    private ImageView mIbtnLeft;

    private ImageView mIbtnRight;

    private TextView mTvRightText;

    private TextView mTvTitle;


    private String mTitle;

    private String mRightText;

    private int mLeftImageRes;

    private int mRightImageRes;


    private boolean mShowLeftImage;

    private boolean mShowRightText;

    private boolean mShowRightImage;



    private int leftDrawablePadding =0;
    private int rightDrawablePadding =0;


    /**
     * 话题页操作栏监听.
     */
    private ActionBarPrimaryListener mListener;

    public ActionBarPrimary(Context context) {
        super(context);
    }

    public ActionBarPrimary(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionBarPrimary(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        mLeftImageRes = typedArray.getResourceId(R.styleable.ActionBar_leftImage, R.mipmap.ic_back_arrow);
        mTitle = typedArray.getString(R.styleable.ActionBar_title);
        mRightText = typedArray.getString(R.styleable.ActionBar_rightText);
        mRightImageRes = typedArray.getResourceId(R.styleable.ActionBar_rightImage, R.mipmap.ic_post);

        leftDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.ActionBar_leftPadding, 0);
        rightDrawablePadding= typedArray.getDimensionPixelSize(R.styleable.ActionBar_rightPadding, 0);
        LogUtil.i("padding","leftDrawablePadding:"+leftDrawablePadding+"-rightDrawablePadding:"+rightDrawablePadding);
        mShowLeftImage = typedArray.getBoolean(R.styleable.ActionBar_showLeftImage, false);
        mShowRightText = typedArray.getBoolean(R.styleable.ActionBar_showRightText, false);
        mShowRightImage = typedArray.getBoolean(R.styleable.ActionBar_showRightImage, false);
        typedArray.recycle();

        init(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_left:
                if (mListener != null) {
                    mListener.onLeftBtnClick();
                }
                break;
            case R.id.tv_right_text:
                if (mListener != null) {
                    mListener.onRightTextClick();
                }
                break;
            case R.id.ibtn_right:
                if (mListener != null) {
                    mListener.onRightBtnClick();
                }
            default:
                break;
        }
    }

    /**
     * 设置标题.
     */
    public void setTitle(String title) {
        mTitle = title;
        mTvTitle.setText(mTitle);
    }

    public void setActionBarListener(ActionBarPrimaryListener listener) {
        mListener = listener;
    }

    /**
     * 初始�?.
     *
     * @param context Context
     */
    private void init(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mRootView = inflater.inflate(R.layout.action_bar_primary, this);

        if (isInEditMode()) {
            return;
        }

        /*标题*/
        mTvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        mTvTitle.setText(mTitle);

          /*左侧按钮*/
        mIbtnLeft = (ImageView)findViewById(R.id.ibtn_left);
        if(mShowLeftImage){
            mIbtnLeft.setVisibility(View.VISIBLE);
            mIbtnLeft.setImageResource(mLeftImageRes);
            mIbtnLeft.setOnClickListener(this);
            mIbtnLeft.setPadding(leftDrawablePadding,leftDrawablePadding,leftDrawablePadding,leftDrawablePadding);
        }

        /*右侧文本*/
        mTvRightText = (TextView) mRootView.findViewById(R.id.tv_right_text);
        if (mShowRightText) {
            mTvRightText.setVisibility(View.VISIBLE);
            mTvRightText.setText(mRightText);
            mTvRightText.setOnClickListener(this);
        } else {
            mTvRightText.setVisibility(View.GONE);
        }

        /*右侧按钮*/
        mIbtnRight = (ImageView) findViewById(R.id.ibtn_right);
        if (mShowRightImage) {
            mIbtnRight.setVisibility(View.VISIBLE);
            mIbtnRight.setImageResource(mRightImageRes);
            mIbtnRight.setOnClickListener(this);
            mIbtnRight.setPadding(rightDrawablePadding, rightDrawablePadding, rightDrawablePadding, rightDrawablePadding);
        } else {
            mIbtnRight.setVisibility(View.GONE);
        }

    }

    /**
     * 操作栏监听器.
     */
    public interface ActionBarPrimaryListener {

        /**
         * 左侧按钮点击事件.
         */
        void onLeftBtnClick();

        /**
         * 右侧文本点击事件.
         */
        void onRightTextClick();

        /**
         * 右侧按钮点击事件.
         */
        void onRightBtnClick();
    }
}
