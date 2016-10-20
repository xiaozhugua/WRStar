package com.wmx.android.wrstar.chat.bean;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.ChatUser;
import com.wmx.android.wrstar.entities.Gift;

import java.io.Serializable;

public class ChatInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6240488099748291325L;

	public int type ; // 0 聊天信息   1 打赏信息



	public int iconFromResId;
	public String iconFromUrl;
	public String content;
	public String time;
	public int fromOrTo;// 0 是收到的消息，1是发送的消息


	public boolean isAt ;
	public ChatUser chatUser;




	public int giftId;   //  1 礼物 2星豆
	public int count;
	public int giftType;
	public int bean;



	@Override
	public String toString() {
		return "ChatInfoEntity [iconFromResId=" + iconFromResId
				+ ", iconFromUrl=" + iconFromUrl + ", content=" + content
				+ ", time=" + time + ", fromOrTo=" + fromOrTo + "]";
	}
}
