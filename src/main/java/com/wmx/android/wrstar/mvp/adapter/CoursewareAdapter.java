package com.wmx.android.wrstar.mvp.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.CourseWare;
import com.wmx.android.wrstar.mvp.views.Complete;
import com.wmx.android.wrstar.utils.DownloadProUtil;
import com.wmx.android.wrstar.utils.FileUtil;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class CoursewareAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<CourseWare> list;
    AbsBaseActivity activity;
    public CoursewareAdapter(AbsBaseActivity activity,List<CourseWare> list){
        inflater = LayoutInflater.from(activity);
        this.list = list ;
        this.activity = activity ;
    }

    class Holder {
        TextView tv_name ;
        ImageView iv_picture;

    }
    Holder  holder ;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return  list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_courseware,null);

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            holder.iv_picture =(ImageView)convertView.findViewById(R.id.iv_picture);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }
        final CourseWare cw =list.get(position);

        WRStarApplication.imageLoader.displayImage(cw.coverimgurl,holder.iv_picture,WRStarApplication.getListOptions());
        holder.tv_name.setText(cw.name);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = cw.id;

                final String filename= "."+name+".pdf";
                File file = new File(FileUtil.fileDirPath+"/"+filename);

                if (file.exists()){
                    openPdf(file);
                } else {
                    downLoadFile(cw.url, "附件中", filename, new Complete() {
                        @Override
                        public void complete() {
                            openPdf(new File(FileUtil.fileDirPath + "/" + filename));
                        }
                    });
                }
            }
        });

        return convertView;
    }


    private void openPdf(File file) {
        // Intent intent = new Intent(FerdlsActivity.this,
        // PDFActivity.class);
        // intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME,
        // Util.filePath + "/" + name);
        // startActivity(intent);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");// 文档格式
        activity.startActivity(intent);
    }

    private void downLoadFile(final String url, final String title, final String apkName, final Complete complete) {
//        if (!Constant.netType.equals("WIFI")) {
//            new AlertDialog.Builder(activity).setTitle("图灵金融").setMessage("当前为" + Constant.netType + "网络，下载会消耗流量，确认下载？")
//                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            DownloadProUtil.showProgressDlg(title, url, apkName, activity, true, complete);
//                        }
//                    }).setNegativeButton("否", null).show();

      //  } else {
            DownloadProUtil.showProgressDlg(title, url, apkName, activity, true, complete);
      //  }

    }
}
