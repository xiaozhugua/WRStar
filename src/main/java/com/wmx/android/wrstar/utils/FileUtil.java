package com.wmx.android.wrstar.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.constants.Directories;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件工具.
 */
public final class FileUtil {

    public static   String fileDirPath =  "/sdcard/wmxfile/";

    /**
     * TAG.
     */
    public static final String TAG = "FileUtil";

    /**
     * 执行读写文件的线程池.
     */
    private static ExecutorService sExecutor;

    static {
        sExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * 文件工具.
     */
    private FileUtil() {
    }


    /**
     * 创建目录, 若目录已存在则不做操作.
     *
     * @param dir 目录的路径
     */
    public static void createDir(final String dir) {
        if (TextUtils.isEmpty(dir)) {
            throw new NullPointerException("dir不能为空");
        }
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 判断文件是否存在.
     *
     * @param filePath 文件路径
     * @return true - 文件存在, false - 文件不存在
     */
    public static boolean isFileExist(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            throw new NullPointerException("filePath不能为空");
        }
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 把Assets中的文件复制到存储设备中.
     *
     * @param context Context
     * @param dirPath 目标目录
     * @param fileName 文件名（在Assets和目标目录中同名）
     * @return true - 复制成功, false - 复制失败
     */
    public static boolean copyFileFromAssets(final Context context, final String dirPath, final String fileName) {
        boolean isSuccess = false;
        String filePath = dirPath + fileName;
        if (!isFileExist(filePath)) {
            createDir(dirPath);
            InputStream is = null;
            OutputStream os = null;
            try {
                is = context.getAssets().open(fileName);
                os = new FileOutputStream(filePath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                isSuccess = true;
            } catch (IOException e) {
                LogUtil.e("流操作异常");
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    LogUtil.e("流关闭异常");
                }
            }
        }
        return isSuccess;
    }

    /**
     * 把内容写入文件.
     *
     * @param fileDir 文件目录, 若不存在则创建
     * @param fileName 文件名
     * @param contents 待写入内容
     * @param isOverride 覆盖模式, true -- 覆盖, false -- 追加
     */
    public static void saveFile(final String fileDir, final String fileName, final byte[] contents, final boolean isOverride) {
        if (TextUtils.isEmpty(fileDir)) {
            throw new NullPointerException("fileDir不能为空");
        } else if (TextUtils.isEmpty(fileName)) {
            throw new NullPointerException("fileName不能为空");
        } else if (contents == null) {
            throw new NullPointerException("contents不能为空");
        }
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                /* 拼接文件的路径 */
                StringBuilder sbFilePath = new StringBuilder();
                sbFilePath.append(fileDir);
                sbFilePath.append(File.separator);
                sbFilePath.append(fileName);
                FileOutputStream fos = null;
                try {
                    FileUtil.createDir(fileDir);
                    File dirFile = new File(fileDir);
                    if (dirFile.exists()) {
                        File file = new File(sbFilePath.toString());
                        Boolean isExist = file.exists();
                        if (!isExist || isOverride) {
                            /* 文件不存在或覆盖模式 */
                            fos = new FileOutputStream(file);
                        } else if (!isOverride && isExist) {
                            /* 文件存在且追加模式 */
                            fos = new FileOutputStream(file, true);
                        }
                        fos.write(contents);
                    }
                } catch (IOException e) {
                    /* 无法使用文件的I/O流 */
                    Log.e(FileUtil.TAG, "无法使用文件的I/O流", e);
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            /* 无法关闭文件的I/O流 */
                            Log.e(FileUtil.TAG, "无法关闭文件的I/O流", e);
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取拍照后图片路径的uri.
     *
     * @return 图片路径的uri.
     */
    public static Uri getImageUri() {
        StringBuilder sbDirPath = new StringBuilder();
        sbDirPath.append(Environment.getExternalStorageDirectory().getPath());
        sbDirPath.append(File.separator);
        sbDirPath.append(Directories.APP_DIR_NAME);
        sbDirPath.append(File.separator);
        sbDirPath.append(Directories.IMAGE_DIR_NAME);
        String imageDir = sbDirPath.toString();
        String imageFile = Directories.IMAGE_FILE_NAME;
        FileUtil.createDir(imageDir);
        return Uri.fromFile(new File(imageDir, imageFile));
    }

    /**
     * 获取裁剪后图片路径的uri.
     *
     * @return 裁剪后图片路径的uri.
     */
    public static Uri getCropImageUri() {
        StringBuilder sbDirPath = new StringBuilder();
        sbDirPath.append(Environment.getExternalStorageDirectory().getPath());
        sbDirPath.append(File.separator);
        sbDirPath.append(Directories.APP_DIR_NAME);
        sbDirPath.append(File.separator);
        sbDirPath.append(Directories.IMAGE_DIR_NAME);
        String imageDir = sbDirPath.toString();
        String imageFile = Directories.CROP_IMAGE_FILE_NAME;
        FileUtil.createDir(imageDir);
        return Uri.fromFile(new File(imageDir, imageFile));
    }

    /**
     * 获取主题图片目录的路径.
     *
     * @return 主题图片目录的路径
     */
    public static String getThemeImageDir() {
        StringBuilder sbFilePath = new StringBuilder();
        sbFilePath.append(Environment.getExternalStorageDirectory().getPath());
        sbFilePath.append(File.separator);
        sbFilePath.append(Directories.APP_DIR_NAME);
        sbFilePath.append(File.separator);
        sbFilePath.append(Directories.IMAGE_DIR_NAME);
        sbFilePath.append(File.separator);
        return sbFilePath.toString();
    }

    /**
     * 获取主题图片的路径.
     *
     * @param context Context
     * @param themeImageUrl 主题图片的地址
     * @return 主题图片的路径, null表示未找到
     */
    public static String getThemeImagePath(final Context context, final String themeImageUrl) {
        if (TextUtils.isEmpty(themeImageUrl)) {
            throw new NullPointerException("themeImageUrl不能为空");
        }
        String themeImagePath = PreferenceUtils.getString(context, themeImageUrl, true);
        if (PreferenceUtils.STRING_DEFAULT.equals(themeImagePath)) {
            themeImagePath = null;
        }
        return themeImagePath;
    }
    /**
     * 删除指定目录下的文件
     */
    public static Boolean deleteFile(String filePath){
        if (isFileExist(filePath)) {
            File file = new File(filePath);
            file.delete();
            return true;
        }
        return false;
    }





    public static void writeFile(final String dirName, final String fileName,
                                 final byte[] b, final int byteCount)throws IOException {
        File path = new File(dirName);
        File file = new File(dirName + "/" + fileName);
        if (!path.exists()) {
            path.mkdir();
        }
        if (file.exists()) {
            file.delete();// 如果目标文件已经存在，则删除，产生覆盖旧文件的效果
        }
        file.createNewFile();
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(b, 0, byteCount);
        stream.close();
    }


    public static long getFolderSize(File file){

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);

                }else{
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }
}
