package com.wmx.android.wrstar.views.fragements;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.LivePageResponse;
import com.wmx.android.wrstar.entities.Live;
import com.wmx.android.wrstar.mvp.adapter.LiveFragmentAdapter;
import com.wmx.android.wrstar.mvp.presenters.LivingPagePresenter;
import com.wmx.android.wrstar.mvp.views.ILivePageView;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.activities.DetailCourseActivity;
import com.wmx.android.wrstar.views.base.AbsBaseFragment;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by wubiao on 2016/2/15
 * <p/>
 * Des: 直播页面.
 */
public class LivingPageFragment extends AbsBaseFragment implements ILivePageView ,RecyclerArrayAdapter.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "LivingPageFragment";
    //    @Bind(R.id.ly_viewpager)
//    RelativeLayout lyViewpager;
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    //    @Bind(R.id.gv_type)
//    GridView gvType;
    @Bind(R.id.rv_live)
    EasyRecyclerView rvLive;

    private ArrayList<View> girds = new ArrayList<View>();

    private int[] icons = {R.mipmap.ic_shop, R.mipmap.ic_mood, R.mipmap.ic_finance, R.mipmap.ic_yoga, R.mipmap.ic_football,};

    private String[] names = {"购物", "心情", "金融", "瑜伽", "足球"};

    LivingPagePresenter presenter;
    // LivePageAdapter adapter;

    LiveFragmentAdapter adapter;

    public boolean isRefresh =false ;


    private int index = 0;


    @Bind(R.id.tv_retry)
    TextView tvRetry;
    @Bind(R.id.net_error)
    LinearLayout lyNetError;

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_living_page;

    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        presenter = new LivingPagePresenter(this, this);
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog("正在重新加载...");
                onRefresh();
            }
        });

//        TitleViewPage tvp = new TitleViewPage(getContext(), lyViewpager);
//        gvType.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        initGird();

        actionBar.setTitle("直播会堂");

        //ssssdf


        rvLive.setLayoutManager(new LinearLayoutManager(mParentActivity));
        rvLive.setRefreshListener(this);
        rvLive.setAdapter(adapter = new LiveFragmentAdapter(getActivity(),presenter));

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Live live =adapter.getAllData().get(position);

                if (WRStarApplication.getUser() == null) {
                    login();
                } else {
                    Intent it = new Intent(getActivity(), DetailCourseActivity.class);

                    it.putExtra("liveid", live.id);
                    it.putExtra("type", live.type);

                    PreferenceUtils.setliveId(getActivity(),live.id);
                    startActivity(it);
                }
            }
        });

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more,this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });
        presenter.getGiftList();
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        if (haveLoad) {
//            index = 0;
//            loadData();
//        }
//
//    }

    @Override
    public void loadData() {

        if (index==-1){
            adapter.stopMore();
            return;
        }
        presenter.getDetailsCourseInfo(index++);

    }


    public void initGird() {
        girds.clear();
        for (int i = 0; i < names.length; i++) {
            View v = View.inflate(getContext(), R.layout.item_gv_type,
                    null);
            v.findViewById(R.id.iv_icon).setBackgroundResource(
                    icons[i]);
            ((TextView) v.findViewById(R.id.tv_name))
                    .setText(names[i]);
            AbsListView.LayoutParams Params = new AbsListView.LayoutParams(
                    SysUtil.getScreenWidth(getContext()) / names.length,
                    SysUtil.dp2px(getContext(), 90));
            v.setLayoutParams(Params);
            girds.add(v);
        }

//        gvType.setAdapter(new BaseAdapter() {
//
//            @Override
//            public View getView(int arg0, View arg1, ViewGroup arg2) {
//                // TODO Auto-generated method stub
//                return girds.get(arg0);
//            }
//
//            @Override
//            public long getItemId(int arg0) {
//                // TODO Auto-generated method stub
//                return arg0;
//            }
//
//            @Override
//            public Object getItem(int arg0) {
//                // TODO Auto-generated method stub
//                return girds.get(arg0);
//            }
//
//            @Override
//            public int getCount() {
//                // TODO Auto-generated method stub
//                return girds.size();
//            }
//        });

    }

    @Override
    protected String getPageTag() {
        return TAG;
    }


    @Override
    public void getLiveListSuccess(final LivePageResponse response) {


        if (lyNetError.getVisibility() == View.VISIBLE) {
            lyNetError.setVisibility(View.GONE);
        }

        hideDialog();
        index = response.body.next;

        if (isRefresh){
            adapter.clear();

            isRefresh=false;
        }

        adapter.addAll(response.body.lives);
        adapter.notifyDataSetChanged();

    }


    private void setBookView(View v, boolean isBook) {
        Button btn = (Button) v;
        btn.setTag(isBook);
        if ((boolean) v.getTag()) {
            btn.setTextColor(mResources.getColor(R.color.text_orange));
            btn.setBackgroundResource(R.mipmap.ic_book_bg);
            btn.setText("已预约");
            btn.setClickable(false);
        } else {
            btn.setTextColor(mResources.getColor(R.color.text_gray_deep));
            btn.setBackgroundDrawable(mResources.getDrawable(R.drawable.bg_btn_yellow));
            btn.setText("立即预约");
        }
    }

    @Override
    public void getLiveListFailed(String dec) {
        hideDialog();

        lyNetError.setVisibility(View.VISIBLE);
    }

    @Override
    public void bookLiveSuccess(View v) {

        setBookView(v, !((boolean) v.getTag()));
    }

    @Override
    public void bookLiveFailed() {
        hideDialog();
    }


    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void onRefresh() {
        index = 0 ;
        isRefresh = true;
        loadData();
    }
}
