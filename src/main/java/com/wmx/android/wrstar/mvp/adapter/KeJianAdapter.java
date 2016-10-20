package com.wmx.android.wrstar.mvp.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.CourseWare;

/**
 * Created by Administrator on 2016/5/25.
 */
public class KeJianAdapter extends RecyclerArrayAdapter<CourseWare> {
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent);
    }

    public KeJianAdapter(Context context) {
        super(context);
    }


    Holder holder;




//    private void openPdf(File file) {
//        // Intent intent = new Intent(FerdlsActivity.this,
//        // PDFActivity.class);
//        // intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME,
//        // Util.filePath + "/" + name);
//        // startActivity(intent);
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(file);
//        intent.setDataAndType(uri, "application/pdf");// 文档格式
//        .startActivity(intent);
//    }

//    private void downLoadFile(final String url, final String title, final String apkName, final Complete complete) {
//        //        if (!Constant.netType.equals("WIFI")) {
//        //            new AlertDialog.Builder(activity).setTitle("图灵金融").setMessage("当前为" + Constant.netType + "网络，下载会消耗流量，确认下载？")
//        //                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
//        //
//        //                        @Override
//        //                        public void onClick(DialogInterface arg0, int arg1) {
//        //                            DownloadProUtil.showProgressDlg(title, url, apkName, activity, true, complete);
//        //                        }
//        //                    }).setNegativeButton("否", null).show();
//
//        //  } else {
//        DownloadProUtil.showProgressDlg(title, url, apkName, activity, true, complete);
//        //  }
//
//    }

    static class Holder extends BaseViewHolder<CourseWare>{
        ImageView      iv_picture;
        ImageView      mImgKejianBofang;
        TextView       mItvKejianTime;
        ImageView      mIvPicture;
        TextView       mTvTitle;
        TextView       mTvTeacher;
        TextView       mTvNum;
        TextView       mTv2;
        TextView       mTv1;
        TextView       mTv3;

        Holder(ViewGroup parent) {
            super(parent, R.layout.item_kejian);

            iv_picture = (ImageView) itemView.findViewById(R.id.img_kejian_yulan);
            mImgKejianBofang = (ImageView) itemView.findViewById(R.id.img_kejian_bofang);
            mItvKejianTime = (TextView) itemView.findViewById(R.id.itv_kejian_time);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTvTeacher = (TextView) itemView.findViewById(R.id.tv_teacher);
            mItvKejianTime = (TextView) itemView.findViewById(R.id.itv_kejian_time);

            mTvNum = (TextView) itemView.findViewById(R.id.tv_num);
            mTv1 = (TextView) itemView.findViewById(R.id.tv1);
            mTv2 = (TextView) itemView.findViewById(R.id.tv2);
            mTv3 = (TextView) itemView.findViewById(R.id.tv3);

        }

        @Override
        public void setData(CourseWare data) {
            super.setData(data);

            WRStarApplication.imageLoader.displayImage(data.coverimgurl,iv_picture,WRStarApplication.getListOptions());
            mTvTitle.setText(data.name);



//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final String name = cw.id;
//
//                    final String filename= "."+name+".pdf";
//                    File file = new File(FileUtil.fileDirPath+"/"+filename);
//
//                    if (file.exists()){
//                        openPdf(file);
//                    } else {
//                        downLoadFile(cw.url, "附件中", filename, new Complete() {
//                            @Override
//                            public void complete() {
//                                openPdf(new File(FileUtil.fileDirPath + "/" + filename));
//                            }
//                        });
//                    }
//                }
//            });



        }
    }


}
