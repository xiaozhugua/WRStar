package com.wmx.android.wrstar.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wmx.android.wrstar.app.ActivityManager;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.constants.SocialAccountInfo;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.views.activities.BuyVIPAcitivty;
import com.wmx.android.wrstar.views.activities.MyStarBeanActivity;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, SocialAccountInfo.WECHAT_APPID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tencent.mm.sdk.openapi.IWXAPIEventHandler#onResp(com.tencent.mm.sdk.
	 * modelbase.BaseResp)
	 */
	@Override
	public void onResp(BaseResp resp) {
		LogUtil.i("onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

			if (resp.errCode == 0) {
				Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();

				if ( ActivityManager.getActivityManager().getNowActivity() instanceof BuyVIPAcitivty) {
					((BuyVIPAcitivty) ActivityManager.getActivityManager().getNowActivity()).paysuccess("支付成功");
				}else if ( ActivityManager.getActivityManager().getNowActivity() instanceof MyStarBeanActivity) {
					((MyStarBeanActivity) ActivityManager.getActivityManager().getNowActivity()).paysuccess("支付成功");
				}


			} else if (resp.errCode == -1) {
				Toast.makeText(this, "支付异常,请重新支付或联系客服", Toast.LENGTH_SHORT).show();
			} else if (resp.errCode == -2) {
				Toast.makeText(this, "已取消支付", Toast.LENGTH_SHORT).show();
			}
		}
		finish();
	}
}