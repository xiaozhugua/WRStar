package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wmx.android.wrstar.R;

/**
 * 第三方登录绑定手机号对话框.
 */
public class BindPhoneDialog extends Dialog {


    private IBindPhoneListenner mCheckPointsListenner;

    public interface IBindPhoneListenner {

       void onBind();
        void onUnbind();
    }

    public BindPhoneDialog(Context context) {
        super(context, R.style.RoundCornerDialog);
    }

    public void setBindPhoneListenner(IBindPhoneListenner listenner) {
        mCheckPointsListenner = listenner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bind_phone);

        final TextView tvBrowseRules = (TextView)findViewById(R.id.tv_bind);
        tvBrowseRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckPointsListenner != null) {
                    mCheckPointsListenner.onBind();
                }
            }
        });

        final TextView tvGotIt = (TextView)findViewById(R.id.tv_unbind);
        tvGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckPointsListenner != null) {
                    mCheckPointsListenner.onUnbind();
                }
            }
        });
    }
}
