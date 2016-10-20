package com.wmx.android.wrstar.views.fragements;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.ClassifyVideoResponse;
import com.wmx.android.wrstar.biz.response.VideoPageResponse;
import com.wmx.android.wrstar.entities.Banner;
import com.wmx.android.wrstar.entities.Tags;
import com.wmx.android.wrstar.entities.Video;
import com.wmx.android.wrstar.mvp.adapter.VideoFragmentAdapter;
import com.wmx.android.wrstar.mvp.presenters.VideoPagePresenter;
import com.wmx.android.wrstar.mvp.views.IClassifyView;
import com.wmx.android.wrstar.mvp.views.IVideoView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.activities.DetailVideoActivity2;
import com.wmx.android.wrstar.views.base.AbsBaseFragment;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;
import com.wmx.android.wrstar.views.widgets.MultiLineRadioGroup;
import com.wmx.android.wrstar.views.widgets.PagerSlidingTabStrip;
import com.wmx.android.wrstar.views.widgets.RollPager.RollPagerView;
import com.wmx.android.wrstar.views.widgets.RollPager.adapter.LoopPagerAdapter;
import com.wmx.android.wrstar.views.widgets.RollPager.hintview.IconHintView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by wubiao on 2016/2/15
 * <p/>
 * Des: 视频集合页面.
 */
public class VideoPageFragment extends AbsBaseFragment implements IVideoView, IClassifyView, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "VideoPageFragment";
    @Bind(R.id.rv_allsee)
    EasyRecyclerView rvAllsee;


    VideoFragmentAdapter adapter;


    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;

    @Bind(R.id.tv_retry)
    TextView tvRetry;
    @Bind(R.id.net_error)
    LinearLayout lyNetError;

    LayoutInflater inflater;
    View headerView;


    RollPagerView rollPagerView;
    PagerSlidingTabStrip slidingTabs;

    MultiLineRadioGroup rgSort;

    private String sortId;
    private String typeId;
    private boolean firstLoad = true;

    private VideoPagePresenter presenter;
    TestLoopAdapter loopAdapter;

    List<ClassifyVideoResponse.BodyEntity.SortkeysEntity> sortList;


    int currentIndex = 0;

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_video_page;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {

        presenter = new VideoPagePresenter(this, getContext(), this);

        inflater = LayoutInflater.from(getActivity());
        headerView = inflater.inflate(R.layout.header_videofragment, null);

        rollPagerView = (RollPagerView) headerView.findViewById(R.id.roll_pagerview);
        rollPagerView.setHintPadding(0, 0, 0, SysUtil.dp2px(getContext(), 8));
        rollPagerView.setPlayDelay(2000);
        rollPagerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SysUtil.dp2px(getContext(), 200)));


        rgSort = (MultiLineRadioGroup) headerView.findViewById(R.id.rg_sort);


        slidingTabs = (PagerSlidingTabStrip) headerView.findViewById(R.id.slide_tabs);
        slidingTabs.setTextSize(14);
        slidingTabs.setTabPaddingLeftRight(32); // defalut =24
        slidingTabs.setOnTabsClickListener(new PagerSlidingTabStrip.OnTabsClickListener() {
            @Override
            public void onClick(Tags tags) {
                typeId = tags.tagid;
                currentIndex = 0;
                loadData();
            }
        });


        actionBar.setTitle("精彩视频");


        GridLayoutManager manager = new GridLayoutManager(mParentActivity, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /**
                 *  分两列，普通的只需要1，而headerview则需要2
                 */
                if (position == 0) {
                    return 2;
                } else if (position == adapter.getAllData().size() + 1) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        rvAllsee.setLayoutManager(manager);
        rvAllsee.setRefreshListener(this);


        adapter = new VideoFragmentAdapter(getContext());

        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });


        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (WRStarApplication.getUser() == null) {
                    login();
                    return;
                }
                Video course = adapter.getAllData().get(position);
//                Intent it = new Intent(mParentActivity, DetailVideoActivity.class);
                Intent it = new Intent(mParentActivity, DetailVideoActivity2.class);
                it.putExtra("courseid", course.courseid + "");
                it.putExtra("mode", "ondemand");
                startActivity(it);
            }
        });


        rvAllsee.setAdapter(adapter);


        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog("正在重新加载...");
                loadData();
            }
        });

    }

    @Override
    public void loadData() {

        if (currentIndex == -1) {
            adapter.stopMore();
            return;
        }

        if (firstLoad) {
            presenter.getClassifyVideo(typeId, sortId, firstLoad, currentIndex);
        } else {
            presenter.getClassifyVideo(typeId, sortId, firstLoad, currentIndex);
        }

    }

    @Override
    protected String getPageTag() {
        return TAG;
    }


    @Override
    public void getVideoIndexSuccess(VideoPageResponse videoPageResponse) {

    }

    @Override
    public void getVideoIndexFailed() {
        lyNetError.setVisibility(View.VISIBLE);
    }

    @Override
    public void getClassifySuccess(ClassifyVideoResponse classifyVideoResponse) {
        if (currentIndex==0){
            adapter.clear();
        }

        currentIndex = classifyVideoResponse.body.next;


        if (firstLoad && classifyVideoResponse.body.coursetypes != null) {

            /**
             *  轮播
             */
            rollPagerView.setAdapter(loopAdapter = new TestLoopAdapter(rollPagerView, classifyVideoResponse.body.banners));
            rollPagerView.setHintView(new IconHintView(getActivity(), R.mipmap.vp_now_yellow, R.mipmap.vp_other, 20));
            loopAdapter.notifyDataSetChanged();

            /**
             * 分类栏
             */
            ArrayList<Tags> list = new ArrayList<>();
            List<ClassifyVideoResponse.BodyEntity.CoursetypesEntity> typeList = classifyVideoResponse.body.coursetypes;
            for (int i = 0; i < typeList.size(); i++) {
                //   rgClassify.append(typeList.get(i).name, typeList.get(i).typeid);
                if (i == 0) {
                    typeId = typeList.get(i).typeid;
                }
                Tags tags = new Tags();
                tags.name = typeList.get(i).name;
                tags.tagid = typeList.get(i).typeid;
                list.add(tags);

            }
            slidingTabs.setData(list);

            /**
             * 排序栏
             */
            sortList = classifyVideoResponse.body.sortkeys;
            for (int i = 0; i < sortList.size(); i++) {
                rgSort.append(sortList.get(i).name, sortList.get(i).code);
            }
            rgSort.setOnCheckChangedListener(new MultiLineRadioGroup.OnCheckedChangedListener() {
                @Override
                public void onItemChecked(MultiLineRadioGroup group, int position, boolean checked) {
                    LogUtil.i("test", "position:" + position + "getCheckKey:" + rgSort.getCheckKey());
                    sortId = rgSort.getCheckKey();
                    currentIndex = 0;
                    loadData();
                }
            });


            sortId = rgSort.getCheckKey();
            rgSort.setItemChecked(0);

            firstLoad = false;

        }


        adapter.addAll(classifyVideoResponse.body.items);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void onRefresh() {
        currentIndex = 0;
        loadData();
    }


    private class TestLoopAdapter extends LoopPagerAdapter {


        private List<Banner> banners;


        public TestLoopAdapter(RollPagerView viewPager, List<Banner> banners) {
            super(viewPager);
            this.banners = banners;

        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            //   view.setImageResource(imgs[position]);
            WRStarApplication.imageLoader.displayImage(banners.get(position).imgurl, view, WRStarApplication.getListOptions());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            //点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getUrl())));

                    if (WRStarApplication.getUser() == null) {
                        login();
                        return;
                    }


//                    Intent it = new Intent(mParentActivity, DetailVideoActivity.class);
                    Intent it = new Intent(mParentActivity, DetailVideoActivity2.class);
                    it.putExtra("courseid", banners.get(position).courseid + "");
                    it.putExtra("mode", "ondemand");
                    startActivity(it);

                }
            });


            return view;
        }

        @Override
        public int getRealCount() {
            return banners.size();
        }

    }
}
