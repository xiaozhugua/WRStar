package com.wmx.android.wrstar.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.biz.response.HomePageResponse;
import com.wmx.android.wrstar.constants.Constant;
import com.wmx.android.wrstar.constants.SocialAccountInfo;
import com.wmx.android.wrstar.entities.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wubiao on 2015/12/29
 *
 * Des: 自定义Application
 */
public class WRStarApplication extends Application {

    public static List<String> datasTag=new ArrayList<String>();
    public static List<HomePageResponse.BodyEntity.NewestsEntity> newests;
    public static List<HomePageResponse.BodyEntity.HeatsEntity> heats;

    private static Context sContext = null;

    private static User sUser = null;
    private static WRStarApplication instance;
    public static ImageLoader imageLoader;




    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

      //  PluginHelper.getInstance().applicationOnCreate(getBaseContext());


        //初始化蒲公英Crash上报
        PgyCrashManager.register(this);

        //初始化友盟sdk
        initUmeng();

        //初始化图片读取工具
        initImageLoader();

        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            Constant.appVersion = pi.versionName;
            Constant.packageName = getPackageName();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        PluginHelper.getInstance().applicationAttachBaseContext(base);
//        super.attachBaseContext(base);
//    }

    public static Context getContext(){
        return sContext;
    }

    public static User getUser(){
        return sUser;
    }

    public static void setUser(User user){
        sUser = user;
    }

    private void initUmeng(){

        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);

        //加密日志

        //  AnalyticsConfig.enableEncrypt(true);

        // 微信配置
        PlatformConfig.setWeixin(SocialAccountInfo.WECHAT_APPID, SocialAccountInfo.WECHAT_APPSECRET);

        PlatformConfig.setQQZone(SocialAccountInfo.QQ_APPID,SocialAccountInfo.QQ_APPSECRET);

        PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
        //新浪微博 appkey appsecret

    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();




        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800)
                        // max width, max height，即保存的每个缓存文件的最大长宽
                        // .discCacheExtraOptions(480, 800, CompressFormat.PNG, 75,
                        // null)
                        // Can slow ImageLoader, use it carefully (Better don't use
                        // it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(2)
                        // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 3).denyCacheImageMultipleSizesInMemory()
                        // .memoryCache(new WeakMemoryCache())
                .memoryCache(new LruMemoryCache(8 * 1024 * 1024))

                        // You can pass your own memory cache
                        // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(9 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // .discCacheFileCount(200)
                        // 缓存的文件数量

              //  .discCache(new  UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout
                        // (5
                        // s),
                        // readTimeout
                        // (30
                        // s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建
        imageLoader.init(config);
    }

    public static DisplayImageOptions getListOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // // 设置图片在下载期间显示的图片
                   .showImageOnLoading(R.mipmap.ic_loading)
                // 设置图片Uri为空或是错误的时候显示的图片
                   .showImageForEmptyUri(R.mipmap.ic_loading)
                           // 设置图片加载/解码过程中错误时候显示的图片
                  .showImageOnFail(R.mipmap.ic_loading)
                .cacheInMemory(true)
                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)
                        // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                        // .decodingOptions(android.graphics.BitmapFactory.Options
                        // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)

                        // 设置图片下载前的延迟
                        // .delayBeforeLoading(int delayInMillis)//int
                        // delayInMillis为你设置的延迟时间
                        // 设置图片加入缓存前，对bitmap进行设置
                        // 。preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                        // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 淡入

                .build();
        return options;
    }

    public static DisplayImageOptions getAvatorOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // // 设置图片在下载期间显示的图片
                   .showImageOnLoading(R.mipmap.ic_avatar_default)
                // 设置图片Uri为空或是错误的时候显示的图片
                   .showImageForEmptyUri(R.mipmap.ic_avatar_default)
                // 设置图片加载/解码过程中错误时候显示的图片
                   .showImageOnFail(R.mipmap.ic_avatar_default)
                .cacheInMemory(true)
                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)
                        // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                        // .decodingOptions(android.graphics.BitmapFactory.Options
                        // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)

                        // 设置图片下载前的延迟
                        // .delayBeforeLoading(int delayInMillis)//int
                        // delayInMillis为你设置的延迟时间
                        // 设置图片加入缓存前，对bitmap进行设置
                        // 。preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                        // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 淡入

                .build();
        return options;
    }
}
