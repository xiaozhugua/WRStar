package com.wmx.android.wrstar.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

/**
 * Created by Administrator on 2016/6/12.
 */
public class AliPay {


    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    private static AliPay instance;
    private Activity context;

    public static AliPay getInstance() {
        if (instance == null)
            instance = new AliPay();
        return instance;
    }

    public AliPay init(Activity context) {
        this.context = context;
        return instance;
    }

    /**
     * call alipay sdk pay. 调用SDK支付 payInfo 完整的符合支付宝参数规范的订单信息
     *
     */
    public void pay(final String payInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     *
     */
    public void check() {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(context);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
//					Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                        if (context instanceof PayCallBack) {
                            ((PayCallBack) context).paysuccess("支付成功");
                        }
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
//						Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            if (context instanceof PayCallBack) {
                                ((PayCallBack) context).payerror("支付结果确认中");
                            }
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//						Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                            if (context instanceof PayCallBack) {
                                ((PayCallBack) context).payerror("支付失败");
                            }
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
//				Toast.makeText(context, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    if (context instanceof PayCallBack) {
                        ((PayCallBack) context).payerror("检查结果为：" + msg.obj);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}
