package com.wmx.android.wrstar.chat.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.LiveGiftResponse;
import com.wmx.android.wrstar.biz.response.OtherUserInfoResponse;
import com.wmx.android.wrstar.chat.bean.ChatInfo;
import com.wmx.android.wrstar.chat.gif.AnimatedGifDrawable;
import com.wmx.android.wrstar.chat.gif.AnimatedImageSpan;
import com.wmx.android.wrstar.entities.ChatUser;
import com.wmx.android.wrstar.entities.Gift;
import com.wmx.android.wrstar.mvp.presenters.LiveRoomPresenter;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.views.dialog.DetailsInfoDialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressLint("NewApi")
public class ChatLVAdapter2 extends BaseAdapter {
    private Context mContext;
    private List<ChatInfo> list;
    private List<Gift> giftList;

    /**
     * 弹出的更多选择框
     */
    private PopupWindow popupWindow;

    /**
     * 复制，删除
     */
    private TextView copy, atReply;

    private LayoutInflater inflater;
    /**
     * 执行动画的时间
     */
    protected long mAnimationTime = 150;


    public static final int TYPE_CHAT = 0; //聊天布局
    public static final int TYPE_GIFT = 1; //送礼物布局

    public int red = Color.parseColor("#eb5244");

    private LiveRoomPresenter presenter;
    private DetailsInfoDialog detailsInfoDialog;


    public DetailsInfoDialog getDetailsInfoDialog() {
        return detailsInfoDialog;
    }

    public void setFocus(boolean isFocus){
        if (detailsInfoDialog!=null){
            detailsInfoDialog.setFocusView(isFocus);
        }
    }


    public void showOtherUserInfoDialog(OtherUserInfoResponse response){
        if (detailsInfoDialog!=null){
            detailsInfoDialog.setResponse(response);
            detailsInfoDialog.show();
        }
    }

    public ChatLVAdapter2(Context mContext, List<ChatInfo> list, final LiveRoomPresenter presenter, final String liveUserId) {
        super();
        this.mContext = mContext;
        this.list = list;
        this.presenter = presenter;
        detailsInfoDialog = new DetailsInfoDialog(mContext);

        String livegiftJson = PreferenceUtils.getLiveGift(mContext);
        Gson gson = new Gson();
        LiveGiftResponse liveGiftResponse = gson.fromJson(livegiftJson, LiveGiftResponse.class);
        this.giftList = liveGiftResponse.body.gifts;
        Gift star = new Gift();
        star.name = "星豆";
        star.classifier="枚";
        star.imgurl = "drawable://" + R.mipmap.ic_gift_star;

        giftList.add(star);


        inflater = LayoutInflater.from(mContext);
        initPopWindow();
    }

    private OnAtUserListener onAtUserListener;

    public interface OnAtUserListener {
        void setChatUser(ChatUser chatUser);
    }

    public void setOnAtUserListener(OnAtUserListener onAtUserListener) {
        this.onAtUserListener = onAtUserListener;
    }

    public void setList(List<ChatInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        ChatInfo info = list.get(position);

        int type = info.type;
        return type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder hodler = null;
        GiftHolder giftHolder= null ;

        int type = getItemViewType(position);
        final ChatInfo chatInfo = list.get(position);
        if (convertView == null) {

            switch (type) {
                case TYPE_CHAT:

                    LogUtil.i("new ViewHolder()");
                    hodler = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_lv_item2, null);

                    hodler.fromContainer = (ViewGroup) convertView.findViewById(R.id.chart_from_container);
                    hodler.toContainer = (ViewGroup) convertView.findViewById(R.id.chart_to_container);

                    hodler.fromContent = (TextView) convertView.findViewById(R.id.chatfrom_content);
                    hodler.toContent = (TextView) convertView.findViewById(R.id.chatto_content);

                    hodler.fromName = (TextView) convertView.findViewById(R.id.chatfrom_name);
                    hodler.toName = (TextView) convertView.findViewById(R.id.chatto_name);
                    convertView.setTag(hodler);

                    break;
                case TYPE_GIFT:
                    LogUtil.i("new GiftHolder()");
                    giftHolder = new GiftHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_gift, null);
                    giftHolder.tv_gift = (TextView)convertView.findViewById(R.id.tv_gift);
                    giftHolder.iv_gift =(ImageView)convertView.findViewById(R.id.iv_gift);
                    convertView.setTag(giftHolder);
                    break;

            }


        } else {

            switch (type) {
                case TYPE_CHAT:
                    LogUtil.i("(ViewHolder) convertView.getTag()");
                    hodler = (ViewHolder) convertView.getTag();

                    break;

                case TYPE_GIFT:
                    LogUtil.i("(GiftHolder) convertView.getTag()");
                    giftHolder = (GiftHolder) convertView.getTag();

                    break;

            }


        }

        switch (type){
            case TYPE_CHAT:

                if (chatInfo.fromOrTo == 0) {
                    // 收到消息 from显示
                    hodler.toContainer.setVisibility(View.GONE);
                    hodler.fromContainer.setVisibility(View.VISIBLE);

                    // 对内容做处理
                    SpannableStringBuilder sb = handler(hodler.fromContent,
                            list.get(position).content);
                    hodler.fromContent.setText(sb);


                    if (chatInfo.isAt) {
                        LogUtil.i("被@了");

                        hodler.fromContent.setTextColor(ContextCompat.getColor(mContext, R.color.text_yellow_p));
                    } else {
                        hodler.fromContent.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray_deep));
                    }


                } else {
                    // 发送消息 to显示
                    hodler.toContainer.setVisibility(View.VISIBLE);
                    hodler.fromContainer.setVisibility(View.GONE);

                    // 对内容做处理
                    SpannableStringBuilder sb = handler(hodler.toContent,
                            list.get(position).content);
                    hodler.toContent.setText(sb);

                    hodler.toName.setText(WRStarApplication.getUser().username);

                }
                // 设置+按钮点击效果
                hodler.fromContent.setOnClickListener(new popAction(convertView,
                        position, list.get(position).fromOrTo));
                hodler.toContent.setOnClickListener(new popAction(convertView,
                        position, list.get(position).fromOrTo));

                break;

            case TYPE_GIFT:
                Gift gift = new Gift();
                LogUtil.i("giftList size:"+giftList.size());
                for (int i=0;i<giftList.size();i++){
                    if (chatInfo.giftId== giftList.get(i).id){
                        gift = giftList.get(i);
                        break;
                    }
                }
                if (gift.name!=null){
                    SpannableString giftInfo ;
                    if (chatInfo.giftType==2){
                          giftInfo = new SpannableString(chatInfo.chatUser.name+"送出"+chatInfo.bean+gift.classifier+gift.name);
                        giftInfo.setSpan(new ForegroundColorSpan(red), chatInfo.chatUser.name.length()+2, chatInfo.chatUser.name.length()+2+String.valueOf(chatInfo.bean).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }else{
                          giftInfo = new SpannableString(chatInfo.chatUser.name+"送出"+chatInfo.count+gift.classifier+gift.name);
                        giftInfo.setSpan(new ForegroundColorSpan(red), chatInfo.chatUser.name.length()+2, chatInfo.chatUser.name.length()+2+String.valueOf(chatInfo.count).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }



                    giftHolder.tv_gift.setText(giftInfo);
                    WRStarApplication.imageLoader.displayImage(gift.imgurl,giftHolder.iv_gift,WRStarApplication.getListOptions());
                }
                break;
        }



        return convertView;
    }


    private SpannableStringBuilder handler(final TextView gifTextView, String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            try {
                String num = tempText.substring("#[face/png/f_static_".length(), tempText.length() - ".png]#".length());
                String gif = "face/gif/f" + num + ".gif";
                /**
                 * 如果open这里不抛异常说明存在gif，则显示对应的gif
                 * 否则说明gif找不到，则显示png
                 * */
                InputStream is = mContext.getAssets().open(gif);
                sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(is, new AnimatedGifDrawable.UpdateListener() {
                            @Override
                            public void update() {
                                gifTextView.postInvalidate();
                            }
                        })), m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                is.close();
            } catch (Exception e) {
                String png = tempText.substring("#[".length(), tempText.length() - "]#".length());
                try {
                    sb.setSpan(new ImageSpan(mContext, BitmapFactory.decodeStream(mContext.getAssets().open(png))), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return sb;
    }

    class ViewHolder {
        TextView fromContent, toContent;
        ViewGroup fromContainer, toContainer;

        TextView fromName, toName;
    }

    class GiftHolder {
        TextView tv_gift;
        ImageView iv_gift;
    }

    /**
     * 屏蔽listitem的所有事件
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    /**
     * 初始化弹出的pop
     */
    private void initPopWindow() {
        View popView = inflater.inflate(R.layout.chat_item_copy_delete_menu,
                null);
        copy = (TextView) popView.findViewById(R.id.chat_copy_menu);
        atReply = (TextView) popView.findViewById(R.id.chat_at_menu);
        popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置popwindow出现和消失动画
        // popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
    }

    /**
     * 显示popWindow
     */
    public void showPop(View parent, int x, int y, final View view,
                        final int position, final int fromOrTo) {
        // 设置popwindow显示位置
        popupWindow.showAtLocation(parent, 0, x, y);
        // 获取popwindow焦点
        popupWindow.setFocusable(true);
        // 设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        // 为按钮绑定事件
        // 复制
        copy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                // 获取剪贴板管理服务
                ClipboardManager cm = (ClipboardManager) mContext
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本数据复制到剪贴板
                cm.setText(list.get(position).content);
            }
        });
        // @
        atReply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
//				if (fromOrTo == 0) {
//					// from
//					leftRemoveAnimation(view, position);
//				} else if (fromOrTo == 1) {
//					// to
//					rightRemoveAnimation(view, position);
//				}
                if (onAtUserListener != null && list.get(position).chatUser != null) {
                    onAtUserListener.setChatUser(list.get(position).chatUser);
                }


                // list.remove(position);
                // notifyDataSetChanged();
            }
        });
        popupWindow.update();
        if (popupWindow.isShowing()) {

        }
    }

    /**
     * 每个ITEM中more按钮对应的点击动作
     */
    public class popAction implements View.OnClickListener {
        int position;
        View view;
        int fromOrTo;

        public popAction(View view, int position, int fromOrTo) {
            this.position = position;
            this.view = view;
            this.fromOrTo = fromOrTo;
        }

        @Override
        public void onClick(View v) {
            int[] arrayOfInt = new int[2];
            // 获取点击按钮的坐标
            v.getLocationOnScreen(arrayOfInt);
            int x = arrayOfInt[0];
            int y = arrayOfInt[1];
            // System.out.println("x: " + x + " y:" + y + " w: " +
            // v.getMeasuredWidth() + " h: " + v.getMeasuredHeight() );
            showPop(v, x, y, view, position, fromOrTo);
        }
    }

    /**
     * item删除动画
     */
    private void rightRemoveAnimation(final View view, final int position) {
        final Animation animation = AnimationUtils.loadAnimation(
                mContext, R.anim.chatto_remove_anim);
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                view.setAlpha(0);
                performDismiss(view, position);
                animation.cancel();
            }
        });

        view.startAnimation(animation);
    }

    /**
     * item删除动画
     */
    private void leftRemoveAnimation(final View view, final int position) {
        final Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.chatfrom_remove_anim);
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                view.setAlpha(0);
                performDismiss(view, position);
                animation.cancel();
            }
        });

        view.startAnimation(animation);
    }

    /**
     * 在此方法中执行item删除之后，其他的item向上或者向下滚动的动画，并且将position回调到方法onDismiss()中
     *
     * @param dismissView
     * @param dismissPosition
     */
    private void performDismiss(final View dismissView,
                                final int dismissPosition) {
        final LayoutParams lp = dismissView.getLayoutParams();// 获取item的布局参数
        final int originalHeight = dismissView.getHeight();// item的高度

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 0)
                .setDuration(mAnimationTime);
        animator.start();

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                list.remove(dismissPosition);
                notifyDataSetChanged();
                // 这段代码很重要，因为我们并没有将item从ListView中移除，而是将item的高度设置为0
                // 所以我们在动画执行完毕之后将item设置回来
                ViewHelper.setAlpha(dismissView, 1f);
                ViewHelper.setTranslationX(dismissView, 0);
                LayoutParams lp = dismissView.getLayoutParams();
                lp.height = originalHeight;
                dismissView.setLayoutParams(lp);
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 这段代码的效果是ListView删除某item之后，其他的item向上滑动的效果
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                dismissView.setLayoutParams(lp);
            }
        });

    }

}
