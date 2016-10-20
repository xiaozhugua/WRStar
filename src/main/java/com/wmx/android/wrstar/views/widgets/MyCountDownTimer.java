package com.wmx.android.wrstar.views.widgets;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.wmx.android.wrstar.utils.TimeUtils;

/**
 * Created by Administrator on 2016/5/20.
 */
public class MyCountDownTimer extends CountDownTimer {

    TextView tv;
    private String text = "";

    private OnCountFinish countFinish;

    public void setCountFinish(OnCountFinish countFinish) {
        this.countFinish = countFinish;
    }

    /**
     * @param millisInFuture    表示以毫秒为单位 倒计时的总数
     *                          <p/>
     *                          例如 millisInFuture=1000 表示1秒
     * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
     *                          <p/>
     *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
     */

    public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView tv) {
        super(millisInFuture, countDownInterval);
        this.tv = tv;
    }


    public interface OnCountFinish {
        void finish();
    }


    @Override
    public void onFinish() {
        //    tv.setText("done");
        if (countFinish != null) {
            countFinish.finish();
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {

        //    timeListener.getTime(millisUntilFinished);
        myText(millisUntilFinished);
    }


    public void myText(long millisUntilFinished) {
        tv.setText(text + TimeUtils.formatTime(millisUntilFinished));
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
