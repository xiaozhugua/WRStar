package com.wmx.android.wrstar.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.morgoo.droidplugin.pm.PluginManager;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.mvp.views.Complete;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * 系统相关工具.
 */
public final class SysUtil {

    /**
     * 应用唯一标识.
     */
    private static String sID = null;

    /**
     * 标识存储目录.
     */
    private static final String INSTALLATION = "INSTALLATION";

    /**
     * 系统相关工具.
     */
    private SysUtil() {
    }

    /**
     * 判断系统版本是否高于(含等于)2.2.
     *
     * @return true - 系统版本高于(含等于)2.2<br />
     * false - 系统版本低于2.2
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    /**
     * 判断系统版本是否高于(含等于)2.3.
     *
     * @return true - 系统版本高于(含等于)2.3<br />
     * false - 系统版本低于2.3
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    /**
     * 判断系统版本是否高于(含等于)3.0.
     *
     * @return true - 系统版本高于(含等于)3.0<br />
     * false - 系统版本低于3.0
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    /**
     * 判断系统版本是否高于(含等于)3.1.
     *
     * @return true - 系统版本高于(含等于)<br />
     * 3.1 false - 系统版本低于3.1
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * 判断系统版本是否高于(含等于)4.1.
     *
     * @return true - 系统版本高于(含等于)4.1<br />
     * false - 系统版本低于4.1
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    /**
     * 判断系统版本是否高于(含等于)4.4.
     *
     * @return true - 系统版本高于(含等于)4.4<br />
     * false - 系统版本低于4.4
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
    }

    /**
     * 获取App版本号.
     *
     * @param context Context
     * @return App版本号
     */
    public static int getAppVersionCode(final Context context) {
        if (context == null) {
            throw new NullPointerException("context不能为空");
        }
        int appVersionCode = 0;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return appVersionCode;
    }

    /**
     * 获取App版本名.
     *
     * @param context Context
     * @return App版本名
     */
    public static String getAppVersionName(final Context context) {
        if (context == null) {
            throw new NullPointerException("context不能为空");
        }
        String appVersionName = "未知的版本名";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return appVersionName;
    }

    /**
     * 获取设备制造商名称.
     *
     * @return 设备制造商名称
     */
    public static String getManufacturerName() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备名称.
     *
     * @return 设备名称
     */
    public static String getModelName() {
        return Build.MODEL;
    }

    /**
     * 获取产品名称.
     *
     * @return 产品名称
     */
    public static String getProductName() {
        return Build.PRODUCT;
    }

    /**
     * 获取品牌名称.
     *
     * @return 品牌名称
     */
    public static String getBrandName() {
        return Build.BRAND;
    }

    /**
     * 获取操作系统版本号.
     *
     * @return 操作系统版本号
     */
    public static int getOsVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取操作系统版本名.
     *
     * @return 操作系统版本名
     */
    public static String getOsVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取操作系统版本显示名.
     *
     * @return 操作系统版本显示名
     */
    public static String getOsVersionDisplayName() {
        return Build.DISPLAY;
    }

    /**
     * 生成系统相关的信息.
     *
     * @param context 上下文信息
     * @return 系统相关的信息
     */
    public static String genInfo(final Context context) {
        if (context == null) {
            throw new NullPointerException("context不能为空");
        }
        StringBuilder sbInfo = new StringBuilder();
        sbInfo.append("[品牌信息]: " + getBrandName());
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append("[产品信息]: ").append(getProductName());
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append("[设备信息]: ").append(getModelName());
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append("[制造商信息]: ").append(getManufacturerName());
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append("[操作系统版本号]: ").append(getOsVersionCode());
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append("[操作系统版本名]: ").append(getOsVersionName());
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append("[操作系统版本显示名]: ").append(getOsVersionDisplayName());
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append("[App版本号]: ").append(getAppVersionCode(context));
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append("[App版本名]: ").append(getAppVersionName(context));
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append(SysUtil.getLineSeparator());
        sbInfo.append(SysUtil.getLineSeparator());
        return sbInfo.toString();
    }

    /**
     * 获取应用唯一标识.
     *
     * @param context Context
     * @return 应用的安装ID
     */
    public synchronized static String getInstallationId(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists()) {
                    writeInstallationFile(installation);
                }
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    /**
     * 获取系统换行符.
     *
     * @return 系统换行符
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * 获取屏幕宽度.
     *
     * @param context Context
     * @return 屏幕宽度（单位像素）
     */
    public static int getScreenWidth(final Context context) {
        if (context == null) {
            throw new NullPointerException("context不能为空");
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度.
     *
     * @param context Context
     * @return 屏幕高度（单位像素）
     */
    public static int getScreenHeight(final Context context) {
        if (context == null) {
            throw new NullPointerException("context不能为空");
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 从文件中读取应用的安装ID.
     *
     * @param installation 文件
     * @return 应用的安装ID
     * @throws IOException 读写异常
     */
    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "handleMessage");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    /**
     * 生成应用的安装ID并写入文件.
     *
     * @param installation 文件
     * @throws IOException 读写异常
     */
    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }


    /**
     * dp转px.
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }


    /**
     * 防止重复点击
     */
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    public static void startApp(final Context context, String apkName) {
        final File file = new File(filePath + "/" + apkName);
        readAssets(context, apkName, new Complete() {
            @Override
            public void complete() {
                lanchApp(context, file.getAbsolutePath());
            }
        });
    }

    public static void lanchApp(Context context, String url)   {
        try {
            PluginManager.getInstance().installPackage(url,0);
            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage("com.wmx.android.pdf");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    public static final String filePath = WRStarApplication.getContext().getFilesDir().getAbsolutePath();
    public static void readAssets(Context context, String fileName, Complete... completes) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            getFile(buffer, filePath, fileName, completes);
        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }
    }
    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName, Complete... completes) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (completes != null) {
                for (Complete complete : completes) {
                    complete.complete();
                }
            }
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }




    /**
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络   2 : net网络
     *
     * @param context
     * @return
     */

    public static int getAPNType(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            netType=2;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }




}
