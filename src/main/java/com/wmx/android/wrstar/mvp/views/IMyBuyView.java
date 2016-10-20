package com.wmx.android.wrstar.mvp.views;

import com.wmx.android.wrstar.entities.RewardRecord;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public interface IMyBuyView {
    void getMyRewardSuccess(List<RewardRecord> records, int index);

}
