package com.wmx.android.wrstar.views.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.WacthRecord;
import com.wmx.android.wrstar.mvp.adapter.WacthRecordAdapter;
import com.wmx.android.wrstar.store.DBHandler;
import com.wmx.android.wrstar.store.DBManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.widgets.FullyLinearLayoutManager;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/3.
 */
public class WatchRecordActivity extends AbsBaseActivity {

    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.rv_watch_record)
    RecyclerView rvWatchRecord;
    private DBManager dbManager;

    private WacthRecordAdapter adapter;
    private List<WacthRecord> list;
    private int totalPage = 0;


    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_watch_record;
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


        rvWatchRecord.setLayoutManager(new FullyLinearLayoutManager(this));


        dbManager = new DBManager(this);


        totalPage = dbManager.getRecordCount();
//        for(int i=0;i<60;i++){
//            dbManager.insertWatchRecord(i+"", System.currentTimeMillis() + "","'http://qhcdn.oss-cn-hangzhou.aliyuncs.com/qkl%2Fpic%2F1462794144017%2Fxgxymhzs.png", DBHandler.TYPE_Live,"test"+i);
//        }


        list = dbManager.queryRecord(page);
        for (WacthRecord wacthRecord : list) {
            LogUtil.i(wacthRecord.toString());
        }

        adapter = new WacthRecordAdapter(this, list);
        adapter.setOnRecyclerItemClickListener(new WacthRecordAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (WRStarApplication.getUser()!=null){
                    String type = list.get(position).type;
                    if (DBHandler.TYPE_Live.equals(type)) {
                        Intent it = new Intent(WatchRecordActivity.this, DetailCourseActivity.class);
                        it.putExtra("liveid", list.get(position).videoId);
                        it.putExtra("type", list.get(position).type);
                        startActivity(it);
                    } else if ((DBHandler.TYPE_Ondemand.equals(type))) {
                        DetailVideoActivity2.actionStart(WatchRecordActivity.this,list.get(position).videoId,"ondemand");
                    }
                }else{
                    login();
                }


            }
        });
        rvWatchRecord.setAdapter(adapter);

        rvWatchRecord.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                FullyLinearLayoutManager manager = (FullyLinearLayoutManager) rvWatchRecord.getLayoutManager();
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                // 判断是否滚动到底部
                if (lastVisibleItem == (totalItemCount - 1) && isfinish) {
                    //加载更多功能的代码
                    loadData();

                }
            }
        });

    }


    private int page = 0;
    private boolean isfinish = false;

    @Override
    protected void loadData() {
        if (page == totalPage) {
            isfinish = true;
        }
        List<WacthRecord> tList = dbManager.queryRecord(page++);
        list.addAll(tList);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected String getPageTag() {
        return null;
    }

}
