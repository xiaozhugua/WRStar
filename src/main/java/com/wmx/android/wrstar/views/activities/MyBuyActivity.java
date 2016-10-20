package com.wmx.android.wrstar.views.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.entities.RewardRecord;
import com.wmx.android.wrstar.mvp.adapter.MyBuyRewardAdapter;
import com.wmx.android.wrstar.mvp.presenters.MyBuyPresenter;
import com.wmx.android.wrstar.mvp.views.IMyBuyView;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.widgets.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MyBuyActivity extends AbsBaseActivity implements IMyBuyView {
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.rv_reward)
    RecyclerView rvReward;
    MyBuyRewardAdapter adapter;
    MyBuyPresenter presenter;

    int index = 0;
    ArrayList<RewardRecord> rewardList = new ArrayList<RewardRecord>();

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_mybuy;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
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
        presenter = new MyBuyPresenter(this, this);
        adapter = new MyBuyRewardAdapter(this, rewardList);
        rvReward.setLayoutManager(new FullyLinearLayoutManager(this));
        rvReward.setAdapter(adapter);
        rvReward.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                FullyLinearLayoutManager manager = (FullyLinearLayoutManager) rvReward.getLayoutManager();
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                // 判断是否滚动到底部
                if (lastVisibleItem == (totalItemCount - 1)) {
                    //加载更多功能的代码
                    if (index != -1)
                        loadData();

                }
            }
        });
    }

    @Override
    protected void loadData() {

        presenter.getRewardRecord(index);

    }

    @Override
    protected String getPageTag() {
        return null;
    }

    @Override
    public void getMyRewardSuccess(List<RewardRecord> records, int index) {
        this.index = index;
        rewardList.addAll(records);
        adapter.notifyDataSetChanged();
    }
}
