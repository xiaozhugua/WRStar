package com.wmx.android.wrstar.views.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.mvp.presenters.FeedBackPresenter;
import com.wmx.android.wrstar.mvp.views.IFeedBackView;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/13.
 */
public class FeedBackActivity extends AbsBaseActivity implements IFeedBackView {
    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.et_fedback)
    EditText etFedback;
    @Bind(R.id.btn_ensure)
    Button btnEnsure;

    FeedBackPresenter presenter;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        presenter = new FeedBackPresenter(this, this);
        actionBar.setActionBarListener(new ActionBarPrimary.ActionBarPrimaryListener() {
            @Override
            public void onLeftBtnClick() {
                finish();
            }

            @Override
            public void onRightTextClick() {

            }

            @Override
            public void onRightBtnClick() {

            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected String getPageTag() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_ensure)
    public void onClick() {

        presenter.sendFeedBack(etFedback.getText()+"");
    }

    @Override
    public void sendFeedBackSuccess() {
        etFedback.setText("");
    }
}
