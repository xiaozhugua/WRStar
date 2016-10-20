package com.wmx.android.wrstar.utils;
 
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.mvp.views.Complete;

public class DownloadProUtil {
 
   private static ProgressDialog mProgressDialog;
   private static Complete[] complete;
   private static Activity context;
   private static String fileName;
   private static String apkName;
   private static boolean noshow;
 
   /**
    * 启动进度条
    * 
    * @param
    *
    * @param
    *
    */
   public static void showProgressDlg(String msg, String url, String fileName,Activity context, Complete... complete) {
        LogUtil.i("momo", "显示进度条showProgressDlg");     
       if (null == mProgressDialog||DownloadProUtil.context!=context) {
           mProgressDialog = new ProgressDialog(context);
           mProgressDialog.setIndeterminate(false);
           mProgressDialog.setMax(100);
           mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       }
       mProgressDialog.setTitle(msg);
       mProgressDialog.setMessage("下载" + msg);
       final DownloadFile downloadFile = new DownloadFile();
       downloadFile.execute(url);
       DownloadProUtil.apkName = msg;
       DownloadProUtil.complete = complete;
       DownloadProUtil.context = context;
       DownloadProUtil.fileName = fileName;
       noshow = false;
       mProgressDialog.setOnCancelListener(new OnCancelListener() {
           @Override
           public void onCancel(DialogInterface arg0) {
               downloadFile.cancel(true);
               LogUtil.i("momo", " mProgressDialog.setOnCancelListener");
               // downLoadCancel(downloadFile);
           }
       });
   }
 
   public static void showProgressDlg(String msg, String url, String fileName,
           Activity context, boolean show, Complete... complete) {
       showProgressDlg(msg, url, fileName, context, complete);
       noshow = show;
   }
 
   public static class DownloadFile extends AsyncTask<String, Integer, String> {
       String url;
       byte[] bytes;
       boolean isStart = false;
 
       @Override
       protected String doInBackground(String... sUrl) {
           try {
               url = sUrl[0];
               HttpURLConnection conn = null;
               conn = (HttpURLConnection) new URL(url).openConnection();
               conn.setConnectTimeout(5000);
               conn.setDoInput(true);
               conn.setDoOutput(false);
               conn.setUseCaches(true);
               conn.connect();
               if (conn.getContentLength() < 0) {
                   return null;
               }
               bytes = new byte[conn.getContentLength()];
               InputStream in = null;
               try {
                   in = conn.getInputStream();
                   isStart = true;
                   int readBytes = 0;
                   while (true) {
                       int length = in.read(bytes,readBytes,bytes.length-readBytes);
                       if (length == -1)
                           break;
                       readBytes += length;
                       publishProgress((int) (readBytes * 100 / bytes.length));
                   }
                   conn.disconnect();
                   FileUtil.writeFile(FileUtil.fileDirPath, fileName, bytes, readBytes);
                  // Toast.makeText(context, " Util.writeFile(Util.fileDirPath——》"+Util.fileDirPath, Toast.LENGTH_SHORT).show();
               } catch (Exception ex) {
                   ex.printStackTrace();
                   return null;
               } finally {
                   try {
                       if (in != null)
                           in.close();
                   } catch (Exception ignored) {
                       ignored.printStackTrace();
                   }
               }
           } catch (Exception e) {
           }
           return null;
       }
 
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           mProgressDialog.show();
           publishProgress(0);
       }
       public static DecimalFormat df = new DecimalFormat("0.00");
       @Override
       protected void onProgressUpdate(Integer... progress) {
           super.onProgressUpdate(progress);
           mProgressDialog.setProgress(progress[0]);
           if (isStart) {
               isStart = false;
               mProgressDialog.setMessage("开始下载,总大小" +  df.format((float) bytes.length / 1000000) + "M");
           }
           if (progress[0] == 100) {
               mProgressDialog.dismiss();
               if (!noshow)
                   downLoadComplete(url);
               if (DownloadProUtil.complete != null) {
                   for (Complete complete : DownloadProUtil.complete) {
                       complete.complete();
                       DownloadProUtil.complete = null;
                   }
               }
           }
       }
   }
 
   public static void downLoadCancel(final DownloadFile downloadFile) {
       new AlertDialog.Builder(DownloadProUtil.context).setTitle("图灵金融")
               .setMessage("是否取消下载？")
               .setPositiveButton("是", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface arg0, int arg1) {
                       downloadFile.cancel(true);
                       mProgressDialog = null;
                   }
               }).setNegativeButton("否", null).show();
   }
 
   public static void downLoadComplete(final String url) {
       new AlertDialog.Builder(DownloadProUtil.context).setTitle(apkName)
               .setMessage("下载完成,是否立即进入？")
               .setPositiveButton("是", new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface arg0, int arg1) {
                	   LogUtil.i("momo", "下载完成后点击了是的——》进入");
                       SysUtil.lanchApp(DownloadProUtil.context, fileName);
                       mProgressDialog = null;
                   }
               }).setNegativeButton("否", null).show();
   }
}