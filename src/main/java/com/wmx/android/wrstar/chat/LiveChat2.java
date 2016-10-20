package com.wmx.android.wrstar.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.ChatInfoGiftResponse;
import com.wmx.android.wrstar.biz.response.ChatInfoResponse;
import com.wmx.android.wrstar.biz.response.ChatJoinRoomResponse;
import com.wmx.android.wrstar.biz.response.ViewerResponse;
import com.wmx.android.wrstar.chat.adapter.ChatLVAdapter2;
import com.wmx.android.wrstar.chat.adapter.FaceGVAdapter;
import com.wmx.android.wrstar.chat.adapter.FaceVPAdapter;
import com.wmx.android.wrstar.chat.bean.ChatInfo;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.entities.ChatUser;
import com.wmx.android.wrstar.mvp.presenters.LiveRoomPresenter;
import com.wmx.android.wrstar.testemoji.util.EmojiUtil;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.SocketUtils;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.activities.LiveActivity2;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Administrator on 2016/5/26.
 */
public class LiveChat2 {

    public final String TAG = "LiveChat2";


    public final String SOCKET_EVENT_JOIN_ROOM = "joinroom";
    public final String SOCKET_EVENT_EXIT_ROOM = "exitroom";
    public final String SOCKET_EVENT_CHAT = "chat";
    public final String SOCKET_EVENT_REWARD = "reward";

    public final String SOCKET_EVENT_VIEWER = "viewer";




    Socket socket;

    public String roomId;
    public String liveId;
    public String userId;

    /**
     *
     */
    private String atUserId;


    public ChatLVAdapter2 mLvAdapter;

    // 7列3行
    private int columns = 6;
    private int rows = 4;
    private List<View> views = new ArrayList<View>();
    private List<String> staticFacesList;
    private LinkedList<ChatInfo> infos = new LinkedList<ChatInfo>();
    private SimpleDateFormat sd;

    private String reply = "";//模拟回复

    LiveActivity2 activity;

    Gson gson;
    LiveRoomPresenter presenter;
    public LiveChat2(LiveActivity2 activity, String liveId, LiveRoomPresenter presenter) {
        this.activity = activity;
        this.liveId = liveId;
        this.presenter = presenter ;
        userId = WRStarApplication.getUser().mobnum;

        initSocket();
        try {
            JoinChatRoom();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initStaticFaces();
        initChatView();


    }


    /**
     * 初始化表情列表staticFacesList
     */
    private void initStaticFaces() {
        try {
            staticFacesList = new ArrayList<String>();
            String[] faces = activity.getAssets().list("face/png");
            //将Assets中的表情名称转为字符串一一添加进staticFacesList
            for (int i = 0; i < faces.length; i++) {
                staticFacesList.add(faces[i]);
            }
            //去掉删除图片
            staticFacesList.remove("emotion_del_normal.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initChatView() {


        activity.etMessage.setOnClickListener(editClick);
        activity.imageFace.setOnClickListener(faceClick);
        activity.btnSend.setOnClickListener(sendClick);

        sd = new SimpleDateFormat("MM-dd HH:mm");
        //模拟收到信息
        //  infos.add(getChatInfoFrom("大家一起摇摆#[face/png/f_static_018.png]#"));
        mLvAdapter = new ChatLVAdapter2(activity, infos,presenter,activity.liveUserId);

        mLvAdapter.setOnAtUserListener(new ChatLVAdapter2.OnAtUserListener() {
            @Override
            public void setChatUser(ChatUser chatUser) {
                atUserId = chatUser.id;
                activity.etMessage.setText(activity.etMessage.getText() + " @" + chatUser.name + " ");

                CharSequence text = activity.etMessage.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }

            }
        });


        activity.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (activity.etMessage.getText().length() > 0) {
                    activity.btnSend.setBackgroundResource(R.drawable.bg_btn_yellow_p);
                } else {
                    activity.btnSend.setBackgroundResource(R.drawable.bg_btn_send);

                }
            }
        });

        /****************************1. 使最新的条目可以自动滚动到可视范围内**********************************/
        activity.mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        activity.mListView.setAdapter(mLvAdapter);


        activity.faceViewpager.setOnPageChangeListener(new PageChange());

        InitViewPager();
        //表情按钮
        // 发送

        //  activity.mListView.setOnRefreshListenerHead(this);
        activity.mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    if (activity.chat_face_container.getVisibility() == View.VISIBLE) {
                        activity.chat_face_container.setVisibility(View.GONE);
                        activity.ivSendGift.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });

    }

    private void initSocket() {
        gson = new Gson();

        socket = SocketUtils.getInstance().openSocket().getSocket();

        joinRoom();
        socket.on(SOCKET_EVENT_CHAT, socket_chat);
        socket.on(SOCKET_EVENT_REWARD, socket_reward);

        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        socket.on(SOCKET_EVENT_VIEWER, socket_viewer);



    }


    private Emitter.Listener socket_viewer = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.i("socket_viewer :" + args[0]);

            final ViewerResponse viewerResponse = gson.fromJson(args[0] + "", ViewerResponse.class);


            handler.post(new Runnable() {
                @Override
                public void run() {
                    activity.tvNum.setText("当前" + viewerResponse.body.count + "人观看");

                }
            });

        }
    };



    private Emitter.Listener socket_reward = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.i("SOCKET_EVENT_REWARD :" + args[0]);

            final ChatInfoGiftResponse chatInfoGiftResponse = gson.fromJson(args[0] + "", ChatInfoGiftResponse.class);


            handler.post(new Runnable() {
                @Override
                public void run() {
                    infos.add(getChatInfoGift(chatInfoGiftResponse));
                    mLvAdapter.notifyDataSetChanged();
//                    activity.mListView.setSelection(infos.size() - 1);
                    /****************************2. 使最新的条目可以自动滚动到可视范围内**********************************/
                    activity.mListView.setSelection(mLvAdapter.getCount() - 1);
                }
            });

        }
    };


    private Emitter.Listener socket_chat = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            LogUtil.i("chat :" + args[0]);
            final ChatInfoResponse chatresponse = gson.fromJson(args[0] + "", ChatInfoResponse.class);

            if (chatresponse.getResult().equals(ServerCodes.SUCCESS)) {
                if (chatresponse.body.user.id.equals(userId)) {
                    return;
                }
                final boolean isAt;
                if (chatresponse.body.atUser != null) {
                    isAt = chatresponse.body.atUser.id.equals(userId);
                } else {
                    isAt = false;
                }


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        infos.add(getChatInfoFrom(chatresponse.body.msg.content, chatresponse.body.user, isAt));
                        mLvAdapter.notifyDataSetChanged();
//                        activity.mListView.setSelection(infos.size() - 1);
                        activity.mListView.setSelection(mLvAdapter.getCount() - 1);
                    }
                });
            }


        }
    };


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtil.i("断开链接，开始重连");
                    if (SysUtil.isNetworkAvailable(activity)) {
                        LogUtil.i("restartSocket!!!!!");
                        restartSocket();
                    }

                }
            }, 6000);
        }
    };

    private void restartSocket() {

        infos.clear();
        if (mLvAdapter!=null){
            mLvAdapter.notifyDataSetChanged();
        }


        socketEventOff();
        SocketUtils.getInstance().closeSocket();
        initSocket();
        try {
            JoinChatRoom();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtil.i("链接错误，开始重连");
                    if (SysUtil.isNetworkAvailable(activity)) {
                        LogUtil.i("restartSocket!!!!!");
                        restartSocket();
                    }
                }
            }, 6000);

        }
    };


    public void joinRoom() {
        socket.on(SOCKET_EVENT_JOIN_ROOM, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtil.i("加入聊天房间");
                final ChatJoinRoomResponse response = gson.fromJson(args[0] + "", ChatJoinRoomResponse.class);

                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    roomId = response.body.roomId;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            activity.tvNum.setText("当前" + response.body.count + "人观看");



                            /**
                             * 进去直播房间读取10条之前的聊天信息
                             */

                            for (int i =0;i<response.body.lastMsg.size();i++){
                                ChatJoinRoomResponse.BodyEntity.LastMsgEntity entity = response.body.lastMsg.get(i);


                                if (entity.msgType==1){
                                    /**
                                     * 聊天信息
                                     */
                                    if (entity.user.id.equals(userId)) {
                                        infos.add(getChatInfoTo(entity.msg.content));
                                    }else{

                                        final boolean isAt;
                                        if (entity.atUser != null) {
                                            isAt = entity.atUser.id.equals(userId);
                                        } else {
                                            isAt = false;
                                        }

                                        infos.add(getChatInfoFrom(entity.msg.content, entity.user, isAt));
                                    }




                                }else if (entity.msgType==2){
                                    /**
                                     * 打赏信息
                                     */

                                    ChatInfo info = new ChatInfo();
                                    info.giftType = entity.type;
                                    info.giftId = entity.giftId;
                                    info.count = entity.count;
                                    info.bean = entity.bean;
                                    info.chatUser = entity.chatUser;
                                    info.type = 1;
                                    infos.add(info);

                                }


                            }
                            mLvAdapter.notifyDataSetChanged();
//                            activity.mListView.setSelection(infos.size() - 1);
                            activity.mListView.setSelection(mLvAdapter.getCount() - 1);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            activity.showToast("进入聊天室失败");
                        }
                    });

                }

                LogUtil.i("args:" + args[0]);

            }

        });
    }

    public void exitRoom() {

        socket.emit(SOCKET_EVENT_EXIT_ROOM);
        LogUtil.i("exitroom");
    }


    private void JoinChatRoom() throws JSONException {

        JSONObject object = new JSONObject();
        object.put("liveId", liveId);
        object.put("token", "135sdf46");
        object.put("userId", userId);
        LogUtil.i("发送进入房间参数:" + object.toString());
        socket.emit(SOCKET_EVENT_JOIN_ROOM, object);

    }


    public void socketEventOff() {
        if (socket != null) {
            socket.off(SOCKET_EVENT_CHAT, socket_chat);
            socket.off(SOCKET_EVENT_REWARD, socket_reward);

            socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        }

    }


    /*
   * 初始表情 *
   */
    private void InitViewPager() {
        // 获取页数
        for (int i = 0; i < getPagerCount(); i++) {
            views.add(viewPagerItem(i));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
            activity.face_dots_container.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        activity.faceViewpager.setAdapter(mVpAdapter);
        activity.face_dots_container.getChildAt(0).setSelected(true);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    mLvAdapter.setList(infos);
                    mLvAdapter.notifyDataSetChanged();
                    activity.mListView.onRefreshCompleteHeader();
                    break;

            }


        }
    };

    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private ImageView dotsItem(int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }


    /**
     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
     *
     * @return
     */
    private int getPagerCount() {
        int count = staticFacesList.size();
        return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
                : count / (columns * rows - 1) + 1;
    }


    /**
     * 向输入框里添加表情
     */
    private void insert(CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((activity.etMessage.getText()));
        int iCursorEnd = Selection.getSelectionEnd((activity.etMessage.getText()));
        if (iCursorStart != iCursorEnd) {
            activity.etMessage.getText().replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((activity.etMessage.getText()));
        activity.etMessage.getText().insert(iCursor, text);
    }

    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
     */
    private void delete() {
        if (activity.etMessage.getText().length() != 0) {
            int iCursorEnd = Selection.getSelectionEnd(activity.etMessage.getText());
            int iCursorStart = Selection.getSelectionStart(activity.etMessage.getText());
            if (iCursorEnd > 0) {
                if (iCursorEnd == iCursorStart) {
                    if (isDeletePng(iCursorEnd)) {
                        String st = "#[face/png/f_static_000.png]#";
                        activity.etMessage.getText().delete(
                                iCursorEnd - st.length(), iCursorEnd);
                    } else {
                        activity.etMessage.getText().delete(iCursorEnd - 1,
                                iCursorEnd);
                    }
                } else {
                    activity.etMessage.getText().delete(iCursorStart,
                            iCursorEnd);
                }
            }
        }
    }

    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
     **/
    private boolean isDeletePng(int cursor) {
        String st = "#[face/png/f_static_000.png]#";
        String content = activity.etMessage.getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),
                    content.length());
            String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    private View viewPagerItem(int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
        /**
         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
         * */
        List<String> subList = new ArrayList<String>();
        subList.addAll(staticFacesList
                .subList(position * (columns * rows - 1),
                        (columns * rows - 1) * (position + 1) > staticFacesList
                                .size() ? staticFacesList.size() : (columns
                                * rows - 1)
                                * (position + 1)));
        /**
         * 末尾添加删除图标
         * */
        subList.add("emotion_del_normal.png");
        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, activity);
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                    if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
                        insert(getFace(png));
                    } else {
                        delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return gridview;
    }

    private SpannableStringBuilder getFace(String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "#[" + png + "]#";
            sb.append(tempText);
            sb.setSpan(
                    new ImageSpan(activity, BitmapFactory
                            .decodeStream(activity.getAssets().open(png))), sb.length()
                            - tempText.length(), sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb;
    }

    private ChatInfo getChatInfoFrom(String message, ChatUser chatUser, boolean isAt) {
        ChatInfo info = new ChatInfo();
        info.content = message;
        info.fromOrTo = 0;
        info.time = sd.format(new Date());
        info.isAt = isAt;
        info.chatUser = chatUser;
        info.type = 0;

        return info;
    }

    private ChatInfo getChatInfoGift(ChatInfoGiftResponse chatInfoGiftResponse) {
        ChatInfo info = new ChatInfo();
        info.giftType = chatInfoGiftResponse.body.type;
        info.giftId = chatInfoGiftResponse.body.giftId;
        info.count = chatInfoGiftResponse.body.count;
        info.bean = chatInfoGiftResponse.body.bean;
        info.chatUser = chatInfoGiftResponse.body.chatUser;
        info.type = 1;


        return info;
    }

    /**
     * 发送的信息
     *
     * @param message
     * @return
     */
    private ChatInfo getChatInfoTo(String message) {
        ChatInfo info = new ChatInfo();
        info.content = message;
        info.fromOrTo = 1;
        info.time = sd.format(new Date());
        info.type = 0;
        return info;
    }


    /**
     * 表情页改变时，dots效果也要跟着改变
     */
    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < activity.face_dots_container.getChildCount(); i++) {
                activity.face_dots_container.getChildAt(i).setSelected(false);
            }
            activity.face_dots_container.getChildAt(arg0).setSelected(true);
        }

    }


    View.OnClickListener faceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideSoftInputView();//隐藏软键盘
            if (activity.chat_face_container.getVisibility() == View.GONE) {
                activity.chat_face_container.setVisibility(View.VISIBLE);
                activity.ivSendGift.setVisibility(View.GONE);
            } else {
                activity.chat_face_container.setVisibility(View.GONE);
                activity.ivSendGift.setVisibility(View.VISIBLE);
            }
        }
    };

    View.OnClickListener editClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (activity.chat_face_container.getVisibility() == View.VISIBLE) {
                activity.chat_face_container.setVisibility(View.GONE);
            }
        }
    };

    View.OnClickListener sendClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if(EmojiUtil.containsEmoji(activity.etMessage.getText().toString())){//判断是否含有emojicon
                    Toast.makeText(activity, "暂时不支持系统emojicon表情！", Toast.LENGTH_SHORT).show();
//                    tv_result.setText("过滤结果为："+EmojiUtil.filterEmoji(et_emoji.getText().toString()));
                    return;
                }else{
//                    Toast.makeText(activity, "内容中不含emojicon表情", Toast.LENGTH_SHORT).show();
                }


                reply = activity.etMessage.getText().toString();
                if (roomId != null) {
                    JSONObject chatObjt = new JSONObject();

                    chatObjt.put("roomId", roomId);
                    chatObjt.put("userId", userId);
                    chatObjt.put("content", activity.etMessage.getText());
                    chatObjt.put("liveId", liveId);


                    if (atUserId != null && reply.contains("@")) {
                        chatObjt.put("atUserId", atUserId);
                        atUserId = null;
                    }

                    LogUtil.i("发送聊天Json:" + chatObjt.toString());
                    socket.emit(SOCKET_EVENT_CHAT, chatObjt);
                }


            } catch (Exception e) {
                LogUtil.i(TAG, "socket.send error:" + e.toString());
            }


            if (!TextUtils.isEmpty(reply)) {
                infos.add(getChatInfoTo(reply));
                mLvAdapter.setList(infos);
                mLvAdapter.notifyDataSetChanged();
//                activity.mListView.setSelection(infos.size() - 1);
                activity.mListView.setSelection(mLvAdapter.getCount() - 1);

                activity.etMessage.setText("");
            }

            hideSoftInputView();
            activity.input_bottom.setVisibility(View.GONE);
            activity.rey_buttomm.setVisibility(View.VISIBLE);
            activity.ivSendGift.setVisibility(View.VISIBLE);
        }
    };


}
