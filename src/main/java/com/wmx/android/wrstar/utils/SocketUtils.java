package com.wmx.android.wrstar.utils;

import android.content.Context;

import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.net.RequestManager;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Administrator on 2016/5/30.
 */
public class SocketUtils {
    /**
     * 获得Socket实例 SocketUtils.getInstance().openSocket().getSocket();
     */

    private static volatile SocketUtils sInstance;


    private Socket socket;

    public static SocketUtils getInstance() {
        if (sInstance == null) {
            synchronized (SocketUtils.class) {
                if (sInstance == null) {

                    sInstance = new SocketUtils();


                }
            }
        }
        return sInstance;
    }

    public boolean isOpenSocket() {
        return sInstance.socket != null;
    }

    public SocketUtils openSocket() {
        if (sInstance.socket == null) {
            sInstance.initSocket();
        }

        return sInstance;
    }


    public void closeSocket() {
        if (socket != null) {
            socket.disconnect();
            socket.close();
            socket = null;
        }
    }


    public Socket getSocket() {
        return socket;
    }

    public void initSocket() {
        try {
            socket = IO.socket(Urls.SOCKET_CHAT);
            socket.connect();




        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }


}
