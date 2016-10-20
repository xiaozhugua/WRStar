package com.wmx.android.wrstar.views.fragements;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.views.activities.MainActivity;
import com.wmx.android.wrstar.views.base.AbsBaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/22.
 */
public class GuideFragment extends AbsBaseFragment {
    @Bind(R.id.iv_picture)
    ImageView ivPicture;
    @Bind(R.id.btn_fly)
    Button btnFly;

    private String imagePath;
    private boolean isFinal = false;

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        Bundle bundle = getArguments();
        this.imagePath = bundle.getString("imagePath");
        this.isFinal = bundle.getBoolean("isFinal");
        WRStarApplication.imageLoader.displayImage(imagePath, ivPicture, WRStarApplication.getListOptions());

        btnFly.setVisibility(isFinal ? View.VISIBLE : View.GONE);


        btnFly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


    }

    @Override
    public void loadData() {

    }

    @Override
    protected String getPageTag() {
        return null;
    }


}
