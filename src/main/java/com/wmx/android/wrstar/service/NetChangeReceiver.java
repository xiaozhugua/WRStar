package com.wmx.android.wrstar.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wmx.android.wrstar.app.ActivityManager;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.constants.Constant;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.SysUtil;


public class NetChangeReceiver extends BroadcastReceiver {
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub


		Constant.netType = SysUtil.getAPNType(WRStarApplication.getContext());
		LogUtil.i("网络变化:"+ Constant.netType+"");
	}

}
