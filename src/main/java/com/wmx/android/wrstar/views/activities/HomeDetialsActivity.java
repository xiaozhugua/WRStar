package com.wmx.android.wrstar.views.activities;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.HomePageResponse;
import com.wmx.android.wrstar.mvp.adapter.VideoZuiReAdapter;
import com.wmx.android.wrstar.mvp.adapter.VideoZuiXinAdapter;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import butterknife.Bind;

public class HomeDetialsActivity extends AbsBaseActivity {

    public static final String TAG = "HomeDetialsActivity";
    @Bind(R.id.rv_allsee)
    EasyRecyclerView rvAllsee;

    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;

    @Bind(R.id.tv_retry)
    TextView tvRetry;
    @Bind(R.id.net_error)
    LinearLayout lyNetError;

    VideoZuiReAdapter adapter;
    VideoZuiXinAdapter mZuiXinAdapter;
    private String type;

    HomePageResponse.BodyEntity.HeatsEntity entity;
    HomePageResponse.BodyEntity.NewestsEntity newestsEntity;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_home_detials;
    }

    @Override
    protected void initExtraData() {

        type=getIntent().getStringExtra("type");
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {

        if(type.equals("zuire")){  // 加载最热
            actionBar.setTitle("最热门");

            GridLayoutManager manager = new GridLayoutManager(this, 2);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    /**
                     *  分两列，普通的只需要1，而headerview则需要2
                     */
                    if (position == 0) {
                        return 1;
                    } else if (position == adapter.getAllData().size() + 1) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            });

            rvAllsee.setLayoutManager(manager);
//        rvAllsee.setRefreshListener(this);

            adapter = new VideoZuiReAdapter(this);

            adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (WRStarApplication.getUser() == null) {
                        login();
                        return;
                    }
                    entity = adapter.getAllData().get(position);
                    Intent it = new Intent(HomeDetialsActivity.this, DetailVideoActivity2.class);
                    it.putExtra("courseid", entity.courseid);
                    startActivity(it);
                }
            });

            rvAllsee.setAdapter(adapter);

        }else if(type.equals("zuixin")){ // 加载最新
            actionBar.setTitle("最新上");

            GridLayoutManager manager = new GridLayoutManager(this, 2);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    /**
                     *  分两列，普通的只需要1，而headerview则需要2
                     */
                    if (position == 0) {
                        return 1;
                    } else if (position == mZuiXinAdapter.getAllData().size() + 1) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            });

            rvAllsee.setLayoutManager(manager);

            mZuiXinAdapter = new VideoZuiXinAdapter(this);

            mZuiXinAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (WRStarApplication.getUser() == null) {
                        login();
                        return;
                    }
                    newestsEntity = mZuiXinAdapter.getAllData().get(position);
                    Intent it = new Intent(HomeDetialsActivity.this, DetailVideoActivity2.class);
                    it.putExtra("courseid", newestsEntity.courseid);
                    startActivity(it);
                }
            });

            rvAllsee.setAdapter(mZuiXinAdapter);
        }

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
    protected void loadData() {
        if(type.equals("zuire")){  // 加载最热
            adapter.addAll(WRStarApplication.heats);
            adapter.notifyDataSetChanged();
        }else if(type.equals("zuixin")){ // 加载最新
            mZuiXinAdapter.addAll(WRStarApplication.newests);
            mZuiXinAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected String getPageTag() {
        return TAG;
    }
}
