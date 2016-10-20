package com.wmx.android.wrstar.chat;

import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.google.gson.Gson;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.ChatInfoGiftResponse;
import com.wmx.android.wrstar.biz.response.ChatInfoResponse;
import com.wmx.android.wrstar.biz.response.ChatJoinRoomResponse;
import com.wmx.android.wrstar.biz.response.ViewerResponse;
import com.wmx.android.wrstar.chat.adapter.ChatLVAdapter2;
import com.wmx.android.wrstar.chat.bean.ChatInfo;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.entities.ChatUser;
import com.wmx.android.wrstar.mvp.presenters.LiveRoomPresenter;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.SocketUtils;
import com.wmx.android.wrstar.utils.SysUtil;
import com.wmx.android.wrstar.views.activities.MediaPreviewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Administrator on 2016/5/26.
 */
public class StartLiveChat {

    public final String TAG = "StartLiveChat";


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
    private List<String> staticFacesList;
    private LinkedList<ChatInfo> infos = new LinkedList<ChatInfo>();
    private SimpleDateFormat sd;


    MediaPreviewActivity activity;

    Gson gson;
    LiveRoomPresenter presenter;
    public StartLiveChat(MediaPreviewActivity activity, String liveId, LiveRoomPresenter presenter) {
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

        sd = new SimpleDateFormat("MM-dd HH:mm");
        //模拟收到信息
        //  infos.add(getChatInfoFrom("大家一起摇摆#[face/png/f_static_018.png]#"));
        mLvAdapter = new ChatLVAdapter2(activity, infos,presenter,activity.liveUserId);

        mLvAdapter.setOnAtUserListener(new ChatLVAdapter2.OnAtUserListener() {
            @Override
            public void setChatUser(ChatUser chatUser) {
                atUserId = chatUser.id;
            }
        });


        /****************************1. 使最新的条目可以自动滚动到可视范围内**********************************/
        activity.mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        activity.mListView.setAdapter(mLvAdapter);

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
}
