package com.wmx.android.wrstar.views.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.biz.response.MyVideoLiveResponse;
import com.wmx.android.wrstar.biz.response.MyVideoOndemandResponse;
import com.wmx.android.wrstar.entities.LiveCollect;
import com.wmx.android.wrstar.mvp.adapter.MyVideoLiveAdapter;
import com.wmx.android.wrstar.mvp.adapter.MyVideoOndemandAdapter;
import com.wmx.android.wrstar.mvp.presenters.MyVideoPresenter;
import com.wmx.android.wrstar.mvp.views.IMyVideo;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.widgets.DividerLine;
import com.wmx.android.wrstar.views.widgets.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MyVideoAcitivity extends AbsBaseActivity implements IMyVideo {

    public static final String TAG = "MyVideoAcitivity";

    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.rv_live)
    RecyclerView rvLive;
    @Bind(R.id.rv_ondemand)
    RecyclerView rvOndemand;
    @Bind(R.id.channel_rg)
    RadioGroup channelRg;
    @Bind(R.id.ly_ondemand)
    RelativeLayout lyOndemand;
    @Bind(R.id.ly_live)
    RelativeLayout lyLive;

    MyVideoPresenter presenter;
    MyVideoOndemandAdapter myVideoOndemandAdapter;

    MyVideoLiveAdapter myVideoLiveAdapter;
    @Bind(R.id.ly_sub1)
    LinearLayout lySub1;
    @Bind(R.id.ly_sub0)
    LinearLayout lySub0;
    @Bind(R.id.ly_sub2)
    LinearLayout lySub2;
    @Bind(R.id.channel_rg_0)
    RadioButton channelRg0;
    @Bind(R.id.tv_num_sub1)
    TextView tvNumSub1;
    @Bind(R.id.tv_name_sub1)
    TextView tvNameSub1;
    @Bind(R.id.tv_num_sub0)
    TextView tvNumSub0;
    @Bind(R.id.tv_name_sub0)
    TextView tvNameSub0;
    @Bind(R.id.tv_num_sub2)
    TextView tvNumSub2;
    @Bind(R.id.tv_name_sub2)
    TextView tvNameSub2;


    private int index_ondemand = 0;
    private boolean ismore_ondemand = true;


    private int subaction = 1; //获取直播列表 0未开始 1 已开始 2已结束
    private int currentSub = 1;
    private int index_sub0 = 0;
    private int index_sub1 = 0;
    private int index_sub2 = 0;


    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_myvideo;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {
        presenter = new MyVideoPresenter(this, this);
    }

    @Override
    protected void initViews() {
        actionBar.setActionBarListener(listener);
        actionBar.setTitle("我的视频");


        channelRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.channel_rg_0:
                        lyOndemand.setVisibility(View.INVISIBLE);
                        lyLive.setVisibility(View.VISIBLE);
                        if (myVideoLiveAdapter == null) {
                            showDialog("正在加载点播...");
                            int index = 0;
                            if (currentSub == 0) {
                                index = index_sub0;
                            } else if (currentSub == 1) {
                                index = index_sub1;
                            } else if (currentSub == 2) {
                                index = index_sub2;
                            }
                            presenter.getLiveVideo(subaction, index);
                        }

                        break;
                    case R.id.channel_rg_1:
                        if (myVideoOndemandAdapter == null) {
                            showDialog("正在加载录播...");
                            presenter.getMyVideo(index_ondemand);
                        }
                        lyOndemand.setVisibility(View.VISIBLE);
                        lyLive.setVisibility(View.INVISIBLE);
                        break;

                }
            }
        });

    }

    @Override
    protected void loadData() {
        presenter.getLiveVideo(subaction, 0);
    }

    @Override
    protected String getPageTag() {
        return TAG;
    }

    ActionBarPrimary.ActionBarPrimaryListener listener = new ActionBarPrimary.ActionBarPrimaryListener() {
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
    };

    @Override
    public void getLiveVideoSuccess(MyVideoLiveResponse response) {
        final List<LiveCollect> lists = response.body.lives;
        myVideoLiveAdapter = new MyVideoLiveAdapter(getBaseContext(), lists);

        myVideoLiveAdapter.setOnrefreshListData(new MyVideoLiveAdapter.OnrefreshListData() {
            @Override
            public void refresh() {
                refreshList();
            }
        });

        myVideoLiveAdapter.setOnRecyclerItemClickListener(new MyVideoLiveAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent it = new Intent(MyVideoAcitivity.this, DetailCourseActivity.class);
                it.putExtra("liveid", lists.get(position).id);
//                it.putExtra("liveid", lists.get(position).id);
                startActivity(it);
            }
        });

        rvLive.setLayoutManager(new FullyLinearLayoutManager(getBaseContext()));
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL, SysUtil.dp2px(getBaseContext(), 15));
        dividerLine.setSize(SysUtil.dp2px(getBaseContext(), 15));
        dividerLine.setColor(0xFFEDEDED);
        rvLive.addItemDecoration(dividerLine);
        rvLive.setAdapter(myVideoLiveAdapter);


        tvNumSub0.setText(response.body.beforestart + "");
        tvNumSub1.setText(response.body.run + "");
        tvNumSub2.setText(response.body.afterend + "");

        if (currentSub == 0) {
            index_sub0 = response.body.index;
        } else if (currentSub == 1) {
            index_sub1 = response.body.index;
            myVideoLiveAdapter.starCountUp();
        } else if (currentSub == 2) {
            index_sub2 = response.body.index;
        }

    }

    @Override
    public void getLiveVideoNull() {
        final List<LiveCollect> lists = new ArrayList<LiveCollect>();
        myVideoLiveAdapter = new MyVideoLiveAdapter(getBaseContext(), lists);
        rvLive.setAdapter(myVideoLiveAdapter);
    }

    @Override
    public void getLiveVideoFailed() {

    }

    @Override
    public void getOndemandVideoSuccess(final MyVideoOndemandResponse video) {

        myVideoOndemandAdapter = new MyVideoOndemandAdapter(getBaseContext(), video);
        myVideoOndemandAdapter.setOnRecyclerItemClickListener(new MyVideoOndemandAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent it = new Intent(MyVideoAcitivity.this, DetailVideoActivity.class);
                Intent it = new Intent(MyVideoAcitivity.this, DetailVideoActivity2.class);
                it.putExtra("courseid", video.body.items.get(position).courseid + "");
                it.putExtra("mode", "ondemand");
                startActivity(it);
            }
        });
        LogUtil.i("ondemand", "getOndemandVideoSuccess !!!");
        ismore_ondemand = video.body.ismore;
        index_ondemand = video.body.next;
        rvOndemand.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rvOndemand.setAdapter(myVideoOndemandAdapter);
        LogUtil.i("ondemand", "rvOndemand.setAdapter");
    }

    @Override
    public void getOndemandVideoFailed() {

    }


    public void refreshList() {
        int index = 0;
        if (currentSub == 0) {
            index = index_sub0;
        } else if (currentSub == 1) {
            index = index_sub1;
        } else if (currentSub == 2) {
            index = index_sub2;
        }
        presenter.getLiveVideo(subaction, index);
    }

    @OnClick({R.id.ly_sub1, R.id.ly_sub0, R.id.ly_sub2})
    public void onClick(View view) {
        showDialog("正在加载...");
        switch (view.getId()) {
            case R.id.ly_sub1:
                subaction = 1;
                currentSub = 1;
                tvNameSub0.setTextColor(mResources.getColor(R.color.text_gray));
                tvNameSub1.setTextColor(mResources.getColor(R.color.bg_gray_deep));
                tvNameSub2.setTextColor(mResources.getColor(R.color.text_gray));

                tvNumSub0.setTextColor(mResources.getColor(R.color.text_gray));
                tvNumSub1.setTextColor(mResources.getColor(R.color.text_orange));
                tvNumSub2.setTextColor(mResources.getColor(R.color.text_gray));

                break;
            case R.id.ly_sub0:
                subaction = 0;
                currentSub = 0;

                tvNameSub0.setTextColor(mResources.getColor(R.color.bg_gray_deep));
                tvNameSub1.setTextColor(mResources.getColor(R.color.text_gray));
                tvNameSub2.setTextColor(mResources.getColor(R.color.text_gray));

                tvNumSub0.setTextColor(mResources.getColor(R.color.text_orange));
                tvNumSub1.setTextColor(mResources.getColor(R.color.text_gray));
                tvNumSub2.setTextColor(mResources.getColor(R.color.text_gray));

                break;
            case R.id.ly_sub2:
                subaction = 2;
                currentSub = 2;

                tvNameSub0.setTextColor(mResources.getColor(R.color.text_gray));
                tvNameSub1.setTextColor(mResources.getColor(R.color.text_gray));
                tvNameSub2.setTextColor(mResources.getColor(R.color.bg_gray_deep));

                tvNumSub0.setTextColor(mResources.getColor(R.color.text_gray));
                tvNumSub1.setTextColor(mResources.getColor(R.color.text_gray));
                tvNumSub2.setTextColor(mResources.getColor(R.color.text_orange));

                break;
        }
        refreshList();
    }
}
