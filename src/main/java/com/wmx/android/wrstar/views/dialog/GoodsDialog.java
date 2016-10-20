package com.wmx.android.wrstar.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.biz.response.GoodsResponse;
import com.wmx.android.wrstar.entities.CourseWare;
import com.wmx.android.wrstar.entities.Goods;
import com.wmx.android.wrstar.mvp.adapter.GoodsAdapter;
import com.wmx.android.wrstar.mvp.views.GoodsView;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/11.
 */
public class GoodsDialog extends Dialog implements GoodsView,RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{


    @Bind(R.id.iv_close)
    ImageView  mIvClose;
    @Bind(R.id.text_goodsNum)
    TextView   mTextGoodsNum;
    @Bind(R.id.rv_goodsListView)
    EasyRecyclerView mRvGoodsListView;


    GoodsAdapter goodsAdapter;
    @OnClick({R.id.iv_close, R.id.text_goodsNum, R.id.rv_goodsListView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                this.dismiss();
                break;
            case R.id.text_goodsNum:
                break;
            case R.id.rv_goodsListView:
                break;
        }
    }


    private Handler handler = new Handler();

    private long PUBLISH_COMMENT_TIME;

    Context         context;
    AbsBaseActivity activity;

    public GoodsDialog(Context context, AbsBaseActivity activity) {
        super(context, R.style.dialog);
        this.context = context;
        this.activity = activity;
        setContentView(R.layout.dialog_goods);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        init();
        windowDeploy();
    }



    // 设置窗口显示
    public void windowDeploy() {
        Window win = getWindow(); // 得到对话框
        win.setWindowAnimations(R.style.speakdialog_bottom); // 设置窗口弹出动画
        win.setGravity(Gravity.BOTTOM);

        win.getDecorView().setPadding(0, 0, 0, 0); // 宽度占满，因为style里面本身带有padding
        WindowManager.LayoutParams lp = win.getAttributes();

        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

    }

    private void init() {


        goodsAdapter=new GoodsAdapter(context);

        mRvGoodsListView.setLayoutManager(new LinearLayoutManager(context));
        mRvGoodsListView.setRefreshListener(this);
        mRvGoodsListView.setAdapter(goodsAdapter);

        goodsAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Toast.makeText(context,"ddddddd",Toast.LENGTH_LONG).show();

            }
        });

        goodsAdapter.setNoMore(R.layout.view_nomore);
        goodsAdapter.setMore(R.layout.view_more, this);
        goodsAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsAdapter.resumeMore();
            }
        });

        /*******模拟数据******/

        List<Goods> list=new ArrayList<>();

        for(int i=0;i<10;i++){
            list.add(new Goods());
        }

        goodsAdapter.addAll(list);
        goodsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLoadMore() {
        
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void getGoodsListSuccess(GoodsResponse response) {

    }

    @Override
    public void getGoodsWareListFailed() {

    }
}
