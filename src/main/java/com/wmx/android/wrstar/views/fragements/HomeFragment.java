package com.wmx.android.wrstar.views.fragements;

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
import com.wmx.android.wrstar.biz.response.FirstPageResponse;
import com.wmx.android.wrstar.entities.Home;
import com.wmx.android.wrstar.mvp.adapter.HomeAdapter;
import com.wmx.android.wrstar.mvp.presenters.HomePresenter;
import com.wmx.android.wrstar.mvp.views.IFirstPageView;
import com.wmx.android.wrstar.views.activities.DetailCourseActivity;
import com.wmx.android.wrstar.views.activities.DetailVideoActivity;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.base.AbsBaseFragment;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import butterknife.Bind;

/**
 * Created by wubiao on 2016/2/15
 * <p/>
 * Des: 首页.
 */
public class HomeFragment extends AbsBaseFragment implements IFirstPageView, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "HomeFragment";
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;

    @Bind(R.id.rv_home)
    EasyRecyclerView rvHome;

    @Bind(R.id.tv_retry)
    TextView tvRetry;
    @Bind(R.id.net_error)
    LinearLayout lyNetError;


    public HomePresenter presenter;

    HomeAdapter adapter;


    private int index = 0;

    public boolean isRefresh = false;


    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_home;

    }


    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        actionBar.setTitle("首页");
        presenter = new HomePresenter(this, this);


        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog("正在重新加载...");
                onRefresh();
            }
        });


        rvHome.setLayoutManager(new LinearLayoutManager(mParentActivity));
        rvHome.setRefreshListener(this);
        rvHome.setAdapter(adapter = new HomeAdapter(getActivity()));

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Home home = adapter.getAllData().get(position);
                if (WRStarApplication.getUser() != null) {
                    if (home.type == 1) {

                        Intent it = new Intent(getActivity(), DetailCourseActivity.class);
                        it.putExtra("liveid", home.liveid);
                        startActivity(it);


                    } else if (home.type == 2) {
                        Intent it = new Intent(getActivity(), DetailVideoActivity.class);
                        it.putExtra("courseid", home.courseid);
                        it.putExtra("mode", "ondemand");
                        startActivity(it);
                    }
                } else {
                    login();
                }
            }
        });

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });


    }


    @Override
    public void loadData() {

        if (presenter == null) {
            ((AbsBaseActivity) getActivity()).protectApp();
        } else {

            if (index == -1) {
                adapter.stopMore();
                return;
            }


            presenter.getHomeList(index);
            index++;
        }


    }

    @Override
    protected String getPageTag() {
        return TAG;
    }


    @Override
    public void getHomeListSuccess(FirstPageResponse response) {
        // lists.addAll(response.body.items);
        if (lyNetError.getVisibility() == View.VISIBLE) {
            lyNetError.setVisibility(View.GONE);
            hideDialog();
        }


        if (isRefresh) {
            adapter.clear();
            isRefresh = false;
        }

        adapter.addAll(response.body.items);
        adapter.notifyDataSetChanged();
        index = response.body.next;
    }

    @Override
    public void getHomeListFailed() {
        lyNetError.setVisibility(View.VISIBLE);
    }


    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void onRefresh() {
        index = 0;
        isRefresh = true;
        loadData();
    }
}
