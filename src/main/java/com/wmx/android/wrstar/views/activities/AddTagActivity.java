package com.wmx.android.wrstar.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.biz.response.CreateTagResponse;
import com.wmx.android.wrstar.biz.response.GetHotTagResponse;
import com.wmx.android.wrstar.constants.Constant;
import com.wmx.android.wrstar.entities.TagsEntity;
import com.wmx.android.wrstar.mvp.adapter.getHotAdapter;
import com.wmx.android.wrstar.mvp.presenters.CreateTagPresenter;
import com.wmx.android.wrstar.mvp.presenters.GetTagPresenter;
import com.wmx.android.wrstar.mvp.views.CreateTagView;
import com.wmx.android.wrstar.mvp.views.GetTagView;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ToastUtils;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.FlowLayout;
import com.wmx.android.wrstar.views.widgets.TagAdapter;
import com.wmx.android.wrstar.views.widgets.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddTagActivity extends AbsBaseActivity implements GetTagView, CreateTagView, View.OnClickListener {

    @Bind(R.id.et_input_tag)
    EditText mEtInputTag;
    @Bind(R.id.btn_check)
    Button mBtnCheck;
//    @Bind(R.id.grid_addtag)
//    GridView mGridAddtag;

    List<TagsEntity> list;
    getHotAdapter adapter;
    GetTagPresenter presenter;
    @Bind(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;

    CreateTagPresenter mCreateTagPresenter;

    private String[] mVals;
    @Override
    protected int getContentViewLayout() {
        return R.layout.page2_create_tag;
    }


    @Override
    protected void initExtraData() {
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {

        presenter = new GetTagPresenter(this, this);
        mCreateTagPresenter=new CreateTagPresenter(this,this);
        mBtnCheck.setOnClickListener(this);

//        if(mEtInputTag.length()>0||mFlowLayout.getSelectedList().size()>0){
//            mBtnCheck.setText("确认");
//        }else{
//            mBtnCheck.setText("取消");
//        }
    }


    @Override
    protected void loadData() {

        if (list == null) {
            list = new ArrayList<TagsEntity>();
            list.clear();
            presenter.getHotTag();
        }

    }

    private String Tag=AddTagActivity.class.getSimpleName();
    @Override
    protected String getPageTag() {
        return Tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void createTagSuccess(CreateTagResponse mCreateTagResponse) {

        datasTag.add(mCreateTagResponse.body.tag.name);
        TagIds.add(mCreateTagResponse.body.tag.tagid);

    }

    @Override
    public void createTagFail(String dec) {
        ToastUtils.genToastUtils(this).showShort(dec);
    }

    @Override
    public void getHotTagSuccess(GetHotTagResponse mGetHotTagResponse) {

        hideDialog();

        list=mGetHotTagResponse.body.tags;
        mVals = new String[mGetHotTagResponse.body.tags.size()];
        for(int i=0;i<mGetHotTagResponse.body.tags.size();i++){
            mVals[i]=mGetHotTagResponse.body.tags.get(i).name;
        }

//        list.addAll(mGetHotTagResponse.body.tags);
//        adapter.notifyDataSetChanged();

        TagAdapter mTagAdapter=new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(AddTagActivity.this).inflate(R.layout.tv,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        mFlowLayout.setAdapter(mTagAdapter);

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                AddTagActivity.this.setTitle("choose:" + selectPosSet.toString());
            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                if(mFlowLayout.getSelectedList().size()>3){
                    Toast.makeText(AddTagActivity.this, "亲，最多只能选择3个哦！", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public void getHotTagFail(String dec) {
        Toast.makeText(this, dec.toString(), Toast.LENGTH_LONG).show();
    }

    private String conT;
    private ArrayList<String> datasTag;
    private ArrayList<String> TagIds;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_input_tag:
                break;
            case R.id.btn_check:  //确定

                conT= mEtInputTag.getText().toString().trim();
                Set<Integer> set=mFlowLayout.getSelectedList();
                 datasTag=new ArrayList<String>();
                 TagIds=new ArrayList<String>();
                for(Integer data :set){
                    datasTag.add(list.get(data).name);
                    TagIds.add(list.get(data).tagid);
                    LogUtil.i(data+"===="+ list.get(data).name);
                    LogUtil.i(data+"===="+ list.get(data).tagid);
                }

                if(!TextUtils.isEmpty(conT)&&conT.length()<16){
                    mCreateTagPresenter.createTag(conT);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);

                        Intent tt= new Intent(AddTagActivity.this,CreateLiveActivity.class);
                        tt.putStringArrayListExtra("result",datasTag);
                        tt.putStringArrayListExtra("resultTagIds",TagIds);
                        setResult(Constant.RPSPONSECODE,tt);
                        finish();
                    }
                }).start();



                break;
            case R.id.grid_addtag:
                break;
        }
    }
}
