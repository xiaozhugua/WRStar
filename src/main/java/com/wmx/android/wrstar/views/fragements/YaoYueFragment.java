package com.wmx.android.wrstar.views.fragements;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.biz.response.GetAppPaiResponse;
import com.wmx.android.wrstar.biz.response.ShareSuccessResponse;
import com.wmx.android.wrstar.entities.Gongxian;
import com.wmx.android.wrstar.mvp.adapter.GongxiandAdapter;
import com.wmx.android.wrstar.mvp.presenters.GetAppPaiPresenter;
import com.wmx.android.wrstar.mvp.presenters.ShareSuccessPresenter;
import com.wmx.android.wrstar.mvp.views.GetAppPaiView;
import com.wmx.android.wrstar.mvp.views.ShareSuccessView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.base.AbsBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YaoYueFragment extends AbsBaseFragment implements ShareSuccessView,View.OnClickListener,GetAppPaiView,RecyclerArrayAdapter.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "YaoYueFragment";
    @Bind(R.id.rv_home)
    EasyRecyclerView rvHome;
    @Bind(R.id.tv_retry)
    TextView tvRetry;
    @Bind(R.id.net_error)
    LinearLayout lyNetError;
    @Bind(R.id.iv_yaoyue_share)
    ImageView iv_yaoyue_share;

    public GetAppPaiPresenter presenter;
    GongxiandAdapter adapter;
    private String index = "1";
    String action="2";   //"action": "2"  //1是推荐 2是邀约
    public boolean isRefresh = false;

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_yaoyue;
    }


    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        iv_yaoyue_share.setOnClickListener(this);
        presenter = new GetAppPaiPresenter(this, this);
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog("正在重新加载...");
                onRefresh();
            }
        });

        rvHome.setLayoutManager(new LinearLayoutManager(mParentActivity));
        rvHome.setRefreshListener(this);

//        adapter.setOnItemClickListener(new EasyRecyclerView.);

        loadData();
    }


    @Override
    public void loadData() {

        if (presenter == null) {
            ((AbsBaseActivity) getActivity()).protectApp();
        } else {
            presenter.getAppPaiList(PreferenceUtils.getToken(getActivity()), index,  action);
        }
    }

    @Override
    protected String getPageTag() {
        return TAG;
    }


    @Override
    public void onLoadMore() {
        showToast("hhhhhhhhh");
        loadData();
    }

    @Override
    public void onRefresh() {
        index = "1";
        isRefresh = true;
        items.clear();
        loadData();
    }

    private List<Gongxian> items=new ArrayList<>();
    @Override
    public void getAppPaiListSuccess(GetAppPaiResponse response) {

        if (lyNetError.getVisibility() == View.VISIBLE) {
            lyNetError.setVisibility(View.GONE);
            hideDialog();
        }
        if(response.body!=null){
            items.addAll(response.body.items);
            rvHome.setAdapter(adapter = new GongxiandAdapter(getActivity()));
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
            index = response.body.next+"";
            hideDialog();
        }

        if (isRefresh) {
            items.clear();
            isRefresh = false;
        }

    }

    @Override
    public void getAppPaiWareListFailed(String dec) {

        lyNetError.setVisibility(View.VISIBLE);
        showToast(dec);
    }
    RecommendShareDialog shh;
    ShareSuccessPresenter successPresenter;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_yaoyue_share:
                shh=new RecommendShareDialog(getActivity(),this);
                shh.show();
                successPresenter=new ShareSuccessPresenter(this,this);
                successPresenter.share(PreferenceUtils.getliveId(getActivity()),PreferenceUtils.getToken(getActivity()));
                break;
        }

    }

    @Override
    public void ShareSuccess(ShareSuccessResponse response) {
        successPresenter=null;
    }

    @Override
    public void ShareFailed(String dec) {

    }

    public class RecommendShareDialog extends Dialog {


        @Bind(R.id.ly_share_weibo)
        LinearLayout lyShareWeibo;
        @Bind(R.id.ly_share_qq)
        LinearLayout lyShareQq;
        @Bind(R.id.ly_share_wechat)
        LinearLayout lyShareWechat;
        @Bind(R.id.btn_close)
        Button btnClose;

        @OnClick({R.id.ly_share_weibo, R.id.ly_share_qq, R.id.ly_share_wechat, R.id.btn_close})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ly_share_weibo:
                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.SINA)
                            .withTitle(PreferenceUtils.getRecommendTitle(context))
                            .withText(PreferenceUtils.getRecommendUrl(context))
                            .withTargetUrl(PreferenceUtils.getRecommendUrl(context))
                            .withMedia(new UMImage(getActivity(),PreferenceUtils.getRecommendIcon(context)))
                            .setListenerList(new UMShareListener() {
                                @Override
                                public void onResult(SHARE_MEDIA share_media) {


                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismiss();
                                            activity.showToast("分享成功");
                                            LogUtil.i("share 分享成功啦  ");
                                        }
                                    });


                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                    LogUtil.i("throwable :" +throwable.toString());
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media) {

                                }
                            })
                            .share();

                    break;
                case R.id.ly_share_qq:


                    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                            {
                                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,
                                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.DOUBAN
                            };
                    new ShareAction(getActivity()).setDisplayList( displaylist )
                            .setPlatform(SHARE_MEDIA.QQ)
                            .withTitle(PreferenceUtils.getRecommendTitle(context))
                            .withText(PreferenceUtils.getRecommendUrl(context))
                            .withTargetUrl(PreferenceUtils.getRecommendUrl(context))
                            .withMedia(new UMImage(getActivity(),PreferenceUtils.getRecommendIcon(context)))
//                        .withTargetUrl("http://www.baidu.com")
                            .setListenerList(new UMShareListener() {
                                @Override
                                public void onResult(SHARE_MEDIA share_media) {


                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismiss();
                                            activity.showToast("分享成功");
                                            LogUtil.i("share 分享成功啦  ");
                                        }
                                    });


                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                    LogUtil.i("throwable :" +throwable.toString());
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media) {

                                }
                            })
                            .share();

                    break;
                case R.id.ly_share_wechat:

                    new ShareAction(getActivity())
                            .setPlatform(SHARE_MEDIA.WEIXIN)
                            .withTitle(PreferenceUtils.getRecommendTitle(context))
                            .withText(PreferenceUtils.getRecommendUrl(context))
                            .withTargetUrl(PreferenceUtils.getRecommendUrl(context))
                            .withMedia(new UMImage(getActivity(),PreferenceUtils.getRecommendIcon(context)))
                            .setListenerList(new UMShareListener() {
                                @Override
                                public void onResult(SHARE_MEDIA share_media) {


                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismiss();
                                            activity.showToast("分享成功");
                                            LogUtil.i("share 分享成功啦  ");
                                        }
                                    });


                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                    LogUtil.i("throwable :" +throwable.toString());
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media) {

                                }
                            })
                            .share();

                    break;
                case R.id.btn_close:
                    this.dismiss();
                    break;
            }
        }

//        public interface ICommentListener {
//            void send(String comment);
//        }
//
//        private ICommentListener commentListener;

        private EditText et;
        private Handler handler = new Handler();

        private long PUBLISH_COMMENT_TIME;

        Context context;
        AbsBaseFragment activity;
        public RecommendShareDialog(Context context, AbsBaseFragment activity) {
            super(context, R.style.dialog);
            this.context = context;
            this.activity = activity ;
            setContentView(R.layout.dialog_share);
            ButterKnife.bind(this);
            setCanceledOnTouchOutside(true);
            init();
            windowDeploy();
        }


//        public void setCommentListener(ICommentListener listener) {
//            this.commentListener = listener;
//        }


        // 设置窗口显示
        public void windowDeploy() {
            Window win = getWindow(); // 得到对话框
            win.setWindowAnimations(R.style.speakdialog_bottom); // 设置窗口弹出动画
            win.setGravity(Gravity.BOTTOM);

            win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
            WindowManager.LayoutParams lp = win.getAttributes();

            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);

        }

        private void init() {


        }

    }
}
