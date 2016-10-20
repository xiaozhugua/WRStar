package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/11.
 */
public class ShareCourseDetailsDialog extends Dialog {


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

                new ShareAction(activity)
                        .setPlatform(SHARE_MEDIA.SINA)
                        .withTitle(PreferenceUtils.getCourseDetailTitle(context))
                        .withText(PreferenceUtils.getCourseDetailContext(context))
                        .withTargetUrl(PreferenceUtils.getCourseDetailContext(context))
                        .withMedia(new UMImage(activity,PreferenceUtils.getlivdLink(context)))
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

                new ShareAction(activity).setDisplayList( displaylist )
                        .withText(PreferenceUtils.getCourseDetailContext(context))
                        .withTitle(PreferenceUtils.getCourseDetailTitle(context))
                        .setPlatform(SHARE_MEDIA.QQ)
                        .withTargetUrl(PreferenceUtils.getCourseDetailContext(context))
//                        .withTargetUrl("http://www.baidu.com")
                        .withMedia( new UMImage(activity,PreferenceUtils.getCourseDetailIcon(context)))
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
                new ShareAction(activity)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .withTitle(PreferenceUtils.getCourseDetailTitle(context))
                        .withText(PreferenceUtils.getCourseDetailContext(context))
                        .withTargetUrl(PreferenceUtils.getCourseDetailContext(context))
                        .withMedia(new UMImage(activity,PreferenceUtils.getlivdLink(context)))
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

    public interface ICommentListener {
        void send(String comment);
    }

    private ICommentListener commentListener;

    private EditText et;
    private Handler handler = new Handler();

    private long PUBLISH_COMMENT_TIME;

    Context context;
    AbsBaseActivity activity;
    public ShareCourseDetailsDialog(Context context, AbsBaseActivity activity) {
        super(context, R.style.dialog);
        this.context = context;
        this.activity = activity ;
        setContentView(R.layout.dialog_share);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        init();
        windowDeploy();
    }


    public void setCommentListener(ICommentListener listener) {
        this.commentListener = listener;
    }


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
