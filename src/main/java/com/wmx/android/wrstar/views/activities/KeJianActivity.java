package com.wmx.android.wrstar.views.activities;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.KeJianResponse;
import com.wmx.android.wrstar.entities.CourseWare;
import com.wmx.android.wrstar.mvp.adapter.KeJianAdapter;
import com.wmx.android.wrstar.mvp.presenters.KeJianPresenter;
import com.wmx.android.wrstar.mvp.views.KeJianView;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import butterknife.Bind;

public class KeJianActivity extends AbsBaseActivity implements KeJianView,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.tv_retry)
    TextView         tvRetry;
    @Bind(R.id.rv_kejian)
    EasyRecyclerView mRvKejian;
    @Bind(R.id.net_error)
    LinearLayout  lyNetError;

    KeJianPresenter presenter;

    KeJianAdapter adapter;
    String liveid;

    private int index = 0;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_ke_jian;
    }

    @Override
    protected void initExtraData() {
        liveid = getIntent().getStringExtra("liveId");
    }

    @Override
    protected void initVariables() {


    }

    @Override
    protected void initViews() {

        actionBar.setTitle("课件");
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

        presenter=new KeJianPresenter(this,this);
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog("正在重新加载...");
                onRefresh();
            }
        });

        mRvKejian.setLayoutManager(new LinearLayoutManager(this));
        mRvKejian.setRefreshListener(this);
        mRvKejian.setAdapter(adapter = new KeJianAdapter(KeJianActivity.this));

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

//                Toast.makeText(KeJianActivity.this,"kkkkkdsadf",Toast.LENGTH_LONG).show();


                CourseWare cw = adapter.getAllData().get(position);
                if (WRStarApplication.getUser() != null) {

                    Intent it = new Intent(KeJianActivity.this, DetailVideoActivity.class);
                    it.putExtra("courseid", cw.courseid);
                    it.putExtra("mode", "ondemand");
                    startActivity(it);

//                    if (cw.type == 1) {
//
//                        Intent it = new Intent(KeJianActivity.this, DetailCourseActivity.class);
//                        it.putExtra("liveid", cw.liveid);
//                        startActivity(it);
//                    } else if (cw.type == 2) {
//                        Intent it = new Intent(KeJianActivity.this, DetailVideoActivity.class);
//                        it.putExtra("courseid", cw.courseid);
//                        it.putExtra("mode", "ondemand");
//                        startActivity(it);
//                    }
                } else {
                    login();
                }
            }
        });

        adapter.setNoMore(R.layout.view_nomore);
//        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });


//        /*******模拟数据******/
//
//        List<CourseWare> list=new ArrayList<>();
//
//        for(int i=0;i<10;i++){
//            list.add(new CourseWare());
//        }
//
//                adapter.addAll(list);
//                adapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData() {

        if (presenter == null) {
          protectApp();
        } else {

            if (index == -1) {
                adapter.stopMore();
                return;
            }


            presenter.getKeJianList(liveid);
            index++;
        }
    }

    @Override
    protected String getPageTag() {
        return "KeJianActivity";
    }

    public boolean isRefresh = false;
    @Override
    public void onRefresh() {
        index = 0;
        isRefresh = true;
        loadData();
    }

    @Override
    public void onLoadMore() {
//        loadData();
    }

    @Override
    public void getKeJianListSuccess(KeJianResponse response) {

//        List<CourseWare> list=new ArrayList<CourseWare>();
//
//        for(int i=0;i<10;i++){
//            list.add(new CourseWare());
//        }

//        adapter.addAll(response.body.items);
//        adapter.notifyDataSetChanged();

        if (lyNetError.getVisibility() == View.VISIBLE) {
            lyNetError.setVisibility(View.GONE);
        }


        if (isRefresh) {
            adapter.clear();
            isRefresh = false;
        }

        adapter.addAll(response.body.coursewares);
        adapter.notifyDataSetChanged();

        hideDialog();
    }

    @Override
    public void getKeJianWareListFailed() {
        lyNetError.setVisibility(View.VISIBLE);
    }
}
