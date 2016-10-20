package com.wmx.android.wrstar.views.widgets;//package com.wmx.android.wrstar.views.widgets;
//
///**
// * Created by Administrator on 2016/5/20.
// */
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import com.wmx.android.wrstar.R;
//import com.wmx.android.wrstar.app.WRStarApplication;
//
//
//public class TitleViewPage {
//    private Context context;
//    private View view;
//    private ViewPager viewpager = null;
//    private List<ImageView> list = null;
//    private List<ImageView> mList = null;
//    private ImageView[] img = null;
//    public static String[] picUrl = {"drawable://" + R.drawable.inter6,
//            "drawable://" + R.drawable.inter6, "drawable://" + R.drawable.inter6};
//
//    private int pageChangeDelay = 0;
//
//    private Button[] btns;
//    //	private LinearLayout vpTime;
//    private int time = 8000;
//
//
//
//
//    public TitleViewPage(Context context,View view) {
//        // TODO Auto-generated constructor stub
//        this.context = context;
//        this.view = view;
//        initView();
//    }
//
//    public void reflush() {
//    }
//
//
//
//    private void initView() {
//
//        viewpager = (ViewPager) view.findViewById(R.id.live_viewpager);
//
//
//        list = new ArrayList<ImageView>();
//
//        for (int i = 0; i < picUrl.length; i++) {
//            ImageView view = new ImageView(context);
//
//            WRStarApplication.imageLoader.displayImage(picUrl[i],view,WRStarApplication.getListOptions());
//
//
//            view.setScaleType(ScaleType.FIT_XY);
//            // StartActivity.imageLoader.displayImage(picUrl[i], view);
//            //Util.setImage(picUrl[i], view, shouYe.handler);
//            list.add(view);
//            final int m = i;
//            view.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//
//
//                }
//            });
//        }
//        img = new ImageView[list.size()];
//
//        LinearLayout layout = (LinearLayout) view
//                .findViewById(R.id.ly_viewGroup);
//        layout.removeAllViews();
//        for (int i = 0; i < list.size(); i++) {
//            img[i] = new ImageView(context);
//            if (0 == i) {
//                img[i].setBackgroundResource(R.mipmap.vp_now);
//            } else {
//                img[i].setBackgroundResource(R.mipmap.vp_other);
//            }
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            params.leftMargin = 10;
//            params.width = 10;
//            params.height = 10;
//            layout.addView(img[i], params);
//        }
//        adapter.notifyDataSetChanged();
//        if (viewpager.getAdapter() == null) {
//            viewpager.setAdapter(adapter);
//            viewpager.setOnPageChangeListener(new OnPageChangeListener() {
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//                }
//
//                @Override
//                public void onPageScrolled(int page, float positionOffset,
//                                           int positionOffsetPixels) {
//                }
//
//                @Override
//                public void onPageSelected(int page) {
//                    check(page);
//                }
//            });
//        }
//    }
//
//    private void check(int page) {
//        pageChangeDelay = 0;
//        for (int i = 0; i < list.size(); i++) {
//            if (page == i) {
//                img[i].setBackgroundResource(R.mipmap.vp_now);
//            } else {
//                img[i].setBackgroundResource(R.mipmap.vp_other);
//            }
//        }
//    }
//
//    PagerAdapter adapter = new PagerAdapter() {
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            container.addView(list.get(position));
//            return list.get(position);
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(list.get(position));
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//    };
//
//    public void change() {
//        pageChangeDelay++;
//        if (pageChangeDelay < 5) {
//            return;
//        }
//        pageChangeDelay = 0;
//        if (viewpager.getCurrentItem() >= viewpager.getChildCount() - 1) {
//            viewpager.setCurrentItem(0);
//        } else {
//            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
//        }
//    }
//
////	private void initTime()
////	{
////		btns = new Button[4];
////		btns[0] = (Button) view.findViewById(R.id.tljr_fragnment_showye_vptime0);
////		btns[1] = (Button) view.findViewById(R.id.tljr_fragnment_showye_vptime1);
////		btns[2] = (Button) view.findViewById(R.id.tljr_fragnment_showye_vptime2);
////		btns[3] = (Button) view.findViewById(R.id.tljr_fragnment_showye_vptime3);
////	}
//
//    public void showTime() {
//        if (time > 6000) {
//            time = 3600;
//        }
//        if (time > 0)
//            time--;
//        showTime(time);
//    }
//
//    private void showTime(int time) {
//        String s = getTimeForInt(time);
//        for (int i = 0; i < s.length(); i++) {
//            if (btns.length > i) {
//                btns[i].setText(s.charAt(i) + "");
//            }
//        }
//    }
//
//    public static String getTimeForInt(int k) {
//        String time = "";
//        if (k > 60) {
//            int min = k / 60;
//            int s = k % 60;
//            time = getDoubelString(min) + getDoubelString(s);
//        } else {
//            time = "00" + getDoubelString(k) + "";
//        }
//        return time;
//    }
//
//    public static String getDoubelString(int i) {
//        String time = "" + i;
//        if (i < 10) {
//            time = "0" + i;
//        }
//        return time;
//    }
//}
//
