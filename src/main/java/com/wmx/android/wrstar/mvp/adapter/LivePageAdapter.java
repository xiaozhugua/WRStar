package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.Author;
import com.wmx.android.wrstar.entities.Live;
import com.wmx.android.wrstar.mvp.presenters.LivingPagePresenter;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.CirImageView;
import com.wmx.android.wrstar.views.widgets.RatingBar;

import java.util.List;


/**
 * Created by Administrator on 2016/5/9.
 */
public class LivePageAdapter extends RecyclerView.Adapter<LivePageAdapter.LiveHolder> {


    private OnRecyclerItemClickListener OnRecyclerItemClickListener;
    private Context context;
    private Resources mResources;
    private List<Live> lives;
    private LivingPagePresenter presenter;
    private AbsBaseActivity activity;

    public LivePageAdapter(Context context, List<Live> lives, LivingPagePresenter presenter, AbsBaseActivity activity) {
        this.context = context;
        this.lives = lives;
        mResources = context.getResources();
        this.presenter = presenter;
        this.activity = activity;
    }

    @Override
    public LiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LiveHolder holder = new LiveHolder(LayoutInflater.from(context).inflate(R.layout.item_live, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(LiveHolder holder, final int position) {



    }

    private void setBookView(View v, boolean isBook) {
        Button btn = (Button) v;
        btn.setTag(isBook);
        if ((boolean) v.getTag()) {
            btn.setTextColor(mResources.getColor(R.color.text_orange));
            btn.setBackgroundResource(R.mipmap.ic_book_bg);
            btn.setText("已预约");
        } else {
            btn.setTextColor(mResources.getColor(R.color.text_gray_deep));
            btn.setBackgroundResource(R.drawable.bg_btn_yellow);
            btn.setText("立即预约");
        }
    }


    @Override
    public int getItemCount() {
        return lives.size();
    }

    class LiveHolder extends RecyclerView.ViewHolder {



        public LiveHolder(View itemView) {
            super(itemView);

        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        OnRecyclerItemClickListener = onRecyclerItemClickListener;
    }
}
