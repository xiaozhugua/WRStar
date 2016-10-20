package com.wmx.android.wrstar.views.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.LSMediaCapture.lsMediaCapture;
import com.netease.LSMediaCapture.lsMediaCapture.LSLiveStreamingParaCtx;
import com.netease.LSMediaCapture.lsMediaCapture.Statistics;
import com.netease.LSMediaCapture.lsMessageHandler;
import com.netease.livestreamingFilter.filter.Filters;
import com.netease.livestreamingFilter.view.CameraSurfaceView;
import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.response.FansResponse;
import com.wmx.android.wrstar.biz.response.LiveEndResponse;
import com.wmx.android.wrstar.biz.response.OtherUserInfoResponse;
import com.wmx.android.wrstar.chat.StartLiveChat;
import com.wmx.android.wrstar.chat.view.DropdownListView;
import com.wmx.android.wrstar.entities.CourseWare;
import com.wmx.android.wrstar.entities.Fans;
import com.wmx.android.wrstar.mvp.adapter.FansAdapter;
import com.wmx.android.wrstar.mvp.presenters.LiveEndPresenter;
import com.wmx.android.wrstar.mvp.presenters.LiveRoomPresenter;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.ILiveRoomView;
import com.wmx.android.wrstar.mvp.views.LiveEndView;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.views.dialog.DetailsInfoDialog;
import com.wmx.android.wrstar.views.widgets.CirImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;


//由于直播推流的URL地址较长，可以直接在代码中的mliveStreamingURL设置直播推流的URL
public class MediaPreviewActivity extends Activity implements ILiveRoomView,ICommonView,LiveEndView,OnClickListener, lsMessageHandler {

	////////////////////////////////////
	@Bind(R.id.image_close)
	public ImageView image_close;
	@Bind(R.id.message_chat_listview)
	public DropdownListView mListView;
	@Bind(R.id.gv_fans)
	public GridView gvFans;
	@Bind(R.id.tv_num)
	public TextView  tvNum;
	@Bind(R.id.tv_Live_name)
	TextView     mTvLiveName;
	@Bind(R.id.tv_Live2_location)
	TextView     mTvLiveLocation;
	@Bind(R.id.img_avatar)
	CirImageView mImgAvatar;

	public String userName;
	public String userLogo;
	public String liveId;
	public String liveUserId;
	public String local;

	ArrayList<Fans> fansList;
	LiveRoomPresenter presenter;
	private int fansIndex;
	StartLiveChat startLiveChat;
	FansAdapter fansAdapter;

	LiveEndPresenter mLiveEndPreter;

	////////////////////网易云直播///////////////////////
    	
	private ImageButton startPauseResumeBtn;
	private Button filterBtn; 
	
	private ImageButton switchBtn;
	private ImageButton networkInfoBtn;
	
	private lsMediaCapture mLSMediaCapture = null;
	private String mliveStreamingURL = null;
	private String mVideoResolution = null;
	private boolean mAlert1 = false;
	private boolean mAlert2 = false;
	private boolean mFilter = false;
	
	private Handler mHandler;
	
	//SDK统计相关变量
	private LSLiveStreamingParaCtx mLSLiveStreamingParaCtx = null;
    private Statistics mStatistics = null;
	
    //SDK日志级别相关变量
    private int mLogLevel = LS_LOG_ERROR;
    
	private CameraSurfaceView mCameraSurfaceView;
    private LiveSurfaceView mVideoView;
    
	private boolean m_liveStreamingOn = false;
	private boolean m_liveStreamingPause = false;
	private boolean m_liveStreamingInit = false;
	private boolean m_liveStreamingInitFinished = false;
	private boolean m_tryToStopLivestreaming = false;
	private boolean m_QoSToStopLivestreaming = false;
	
	private Intent mIntentLiveStreamingStopFinished = new Intent("LiveStreamingStopFinished");  
	private Context mContext;
    
    private int mVideoPreviewWidth, mVideoPreviewHeight;

    private Intent mNetInfoIntent = new Intent("com.netease.netInfo");  

    private Intent mNetinfoIntent;
    private Intent mAlertServiceIntent;
    
    private boolean mAlertServiceOn = false;
    private boolean mNetworkInfoServiceOn = false;
    
    private long mLastVideoProcessErrorAlertTime = 0;
    private long mLastAudioProcessErrorAlertTime = 0;
    
    private boolean mHardWareEncEnable = false;
    
    //视频水印相关变量
    private boolean mWaterMarkOn = false;//视频水印开关，默认关闭，需要视频水印的用户可以开启此开关
    private String mWaterMarkFilePath;//视频水印文件路径
    private String mWaterMarkFileName = "logo.png";//视频水印文件名
    private File mWaterMarkAppFileDirectory = null;
    private int mWaterMarkPosX = 10;//视频水印坐标(X)
    private int mWaterMarkPosY = 10;//视频水印坐标(Y)

    //查询摄像头支持的采集分辨率信息相关变量
    private Thread mCameraThread;
    private Looper mCameraLooper;
    private int mCameraID = CAMERA_POSITION_BACK;//默认查询的是后置摄像头
    private Camera mCamera;
	
	private float mCurrentDistance;  
    private float mLastDistance = -1; 
                                                                                       
	public static final int CAMERA_POSITION_BACK = 0;
	public static final int CAMERA_POSITION_FRONT = 1;
	
	public static final int CAMERA_ORIENTATION_PORTRAIT = 0;
	public static final int CAMERA_ORIENTATION_LANDSCAPE = 1;
	
	public static final int LS_VIDEO_CODEC_AVC = 0;
	public static final int LS_VIDEO_CODEC_VP9 = 1;
	public static final int LS_VIDEO_CODEC_H265 = 2;
	
	public static final int LS_AUDIO_STREAMING_LOW_QUALITY = 0;
	public static final int LS_AUDIO_STREAMING_HIGH_QUALITY = 1;

	public static final int LS_AUDIO_CODEC_AAC = 0;
	public static final int LS_AUDIO_CODEC_SPEEX = 1;
	public static final int LS_AUDIO_CODEC_MP3 = 2;
	public static final int LS_AUDIO_CODEC_G711A = 3;
	public static final int LS_AUDIO_CODEC_G711U = 4;
    
	public static final int FLV = 0;
	public static final int RTMP = 1;

	public static final int HAVE_AUDIO = 0;
	public static final int HAVE_VIDEO = 1;
	public static final int HAVE_AV = 2;
	
	public static final int LS_LOG_QUIET       = 0x00;            //!< log输出模式：不输出
    public static final int LS_LOG_ERROR       = 1 << 0;          //!< log输出模式：输出错误
    public static final int LS_LOG_WARNING     = 1 << 1;          //!< log输出模式：输出警告
    public static final int LS_LOG_INFO        = 1 << 2;          //!< log输出模式：输出信息
    public static final int LS_LOG_DEBUG       = 1 << 3;          //!< log输出模式：输出调试信息
    public static final int LS_LOG_DETAIL      = 1 << 4;          //!< log输出模式：输出详细
    public static final int LS_LOG_RESV        = 1 << 5;          //!< log输出模式：保留
    public static final int LS_LOG_LEVEL_COUNT = 6;
    public static final int LS_LOG_DEFAULT     = LS_LOG_WARNING;	//!< log输出模式：默认输出警告
    
    
    private static final String TAG = "NeteaseLiveStream";
    
    //提示用户不能使用滤镜功能的对话框
    private void showAlertDialog1() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.alert_dialog1, null);

		TextView yes = (TextView) view.findViewById(R.id.tv_yes);
		yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}
    
    private void showAlertDialog2() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.alert_dialog2, null);

		TextView yes = (TextView) view.findViewById(R.id.tv_yes);
		yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}
	
    //查询Android摄像头支持的采样分辨率相关方法（1）
	public void openCamera() {
		final Semaphore lock = new Semaphore(0);
		final RuntimeException[] exception = new RuntimeException[1];
		mCameraThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				mCameraLooper = Looper.myLooper();
				try {
					mCamera = Camera.open(mCameraID);
				} catch (RuntimeException e) {
					exception[0] = e;
				} finally {
					lock.release();
					Looper.loop();
				}
			}
		});
		mCameraThread.start();
		lock.acquireUninterruptibly();
	}
	
	//查询Android摄像头支持的采样分辨率相关方法（2）
	public void lockCamera() {
		try {
			mCamera.reconnect();
		} catch (Exception e) {
		}
	}
	
	//查询Android摄像头支持的采样分辨率相关方法（3）
	public void releaseCamera() {
		if (mCamera != null) {
			lockCamera();
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}		
	}
	
	//查询Android摄像头支持的采样分辨率相关方法（4）
	public List<Camera.Size> getCameraSupportSize() {	
		openCamera();		
		if(mCamera != null) {
			Parameters param = mCamera.getParameters();		
			List<Camera.Size> previewSizes = param.getSupportedPreviewSizes();	
			releaseCamera();		
			return previewSizes;
		}	
		return null;
	}

	//视频云Demo层显示实时音视频信息的操作
	public void staticsHandle() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                
                Bundle bundle = msg.getData();  
                int videoFrameRate = bundle.getInt("FR");  
                int videoBitrate = bundle.getInt("VBR"); 
                int audioBitrate = bundle.getInt("ABR");   
                int totalRealBitrate = bundle.getInt("TBR");   

                if(mNetworkInfoServiceOn)
                {
                    mNetInfoIntent.putExtra("videoFrameRate", videoFrameRate);  
                	mNetInfoIntent.putExtra("videoBitRate", videoBitrate);
                	mNetInfoIntent.putExtra("audioBitRate", audioBitrate);
                	mNetInfoIntent.putExtra("totalRealBitrate", totalRealBitrate);

              	     if(mLSLiveStreamingParaCtx.sLSVideoParaCtx.width == 960 && mLSLiveStreamingParaCtx.sLSVideoParaCtx.height == 540)
              	     {
              	    	mNetInfoIntent.putExtra("resolution", 1);
              	     }
              	     else if(mLSLiveStreamingParaCtx.sLSVideoParaCtx.width == 640 && mLSLiveStreamingParaCtx.sLSVideoParaCtx.height == 480)
              	     {
              	    	mNetInfoIntent.putExtra("resolution", 2);
              	     }
              	     else if(mLSLiveStreamingParaCtx.sLSVideoParaCtx.width == 320 && mLSLiveStreamingParaCtx.sLSVideoParaCtx.height == 240)
              	     {
              	    	mNetInfoIntent.putExtra("resolution", 3);
              	     }
              	     
                     sendBroadcast(mNetInfoIntent);  
                }
            }
        };
	}
	
	//视频水印相关方法(1)
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	
	//视频水印相关方法(2)
	public void waterMark() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mWaterMarkAppFileDirectory = getExternalFilesDir(null);
		} else {
			mWaterMarkAppFileDirectory = getFilesDir();
		}

		AssetManager assetManager = getAssets();
		
        //拷贝水印文件到APP目录
    	String[] files = null;
    	File fileDirectory;
    	
        try {
            files = assetManager.list("waterMark");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        
		if(mWaterMarkAppFileDirectory != null)
        {        	
			fileDirectory = mWaterMarkAppFileDirectory;
        }
		else
        {
			fileDirectory = Environment.getExternalStorageDirectory();      	
			mWaterMarkFilePath = fileDirectory + "/" + mWaterMarkFileName;
        }
		
    	for(String filename : files) {
            try {
                InputStream in = assetManager.open("waterMark/" + filename);
                File outFile = new File(fileDirectory, filename);
                FileOutputStream out = new FileOutputStream(outFile);
                copyFile(in, out);
                mWaterMarkFilePath = outFile.toString();
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file", e);
            }  
    	}
	}
	
	//音视频参数设置
	public void paraSet(){	

        //滤镜模式下不开视频水印
		if(!mLSLiveStreamingParaCtx.eHaraWareEncType.hardWareEncEnable && mWaterMarkOn) {
		    waterMark();
		}
		
		//输出格式：视频、音频和音视频
		mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType = HAVE_AV;

		//输出封装格式
		mLSLiveStreamingParaCtx.eOutFormatType.outputFormatType = RTMP;
		
        //摄像头参数配置
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.interfaceOrientation.interfaceOrientation = CAMERA_ORIENTATION_PORTRAIT;//竖屏
        
        //音频编码参数配置
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.samplerate = 44100;
    	mLSLiveStreamingParaCtx.sLSAudioParaCtx.bitrate = 64000;
    	mLSLiveStreamingParaCtx.sLSAudioParaCtx.frameSize = 2048;
    	mLSLiveStreamingParaCtx.sLSAudioParaCtx.audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    	mLSLiveStreamingParaCtx.sLSAudioParaCtx.channelConfig = AudioFormat.CHANNEL_IN_MONO;	
    	mLSLiveStreamingParaCtx.sLSAudioParaCtx.codec.audioCODECType = LS_AUDIO_CODEC_AAC;

    	//硬件编码参数设置
        mLSLiveStreamingParaCtx.eHaraWareEncType.hardWareEncEnable = mHardWareEncEnable;	
        
        //视频编码参数配置，视频码率可以由用户任意设置，视频分辨率按照如下表格设置
        //采集分辨率     编码分辨率     建议码率
        //1280x720     1280x720     1500kbps
        //1280x720     960x540      800kbps
        //960x720      960x720      1000kbps
        //960x720      960x540      800kbps
        //960x540      960x540      800kbps
        //640x480      640x480      600kbps
        //640x480      640x360      500kbps
        //320x240      320x240      250kbps
        //320x240      320x180      200kbps
        //如下是编码分辨率等信息的设置
        if(mVideoResolution.equals("HD")) {
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 20;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 800000;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 960;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 540;
        }
        else if(mVideoResolution.equals("SD")) {
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 20;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 600000;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 640;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 480;
        }
        else {
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.fps = 15;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.bitrate = 250000;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec.videoCODECType = LS_VIDEO_CODEC_AVC;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.width = 320;
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.height = 240;
        }
	}

	public void buttonInit() {

        //开始/停止直播按钮初始化	
		startPauseResumeBtn = (ImageButton)findViewById(R.id.StartStopAVBtn);
        startPauseResumeBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v)
        	{
        		if(!m_liveStreamingOn)
        		{
        			if(!m_liveStreamingPause)
        			{
        		        if(mliveStreamingURL.isEmpty())
        			    return;

        		        startAV();
        			}

        			startPauseResumeBtn.setImageResource(R.mipmap.play);
        		}
        	}
        });

        //滤镜按钮初始化
		if(mHardWareEncEnable) {
			filterBtn = (Button) findViewById(R.id.filterBtn);
			filterBtn.setOnClickListener(this);
		}

        //切换前后摄像头按钮初始化
        switchBtn = (ImageButton)findViewById(R.id.switchBtn);	      
        switchBtn.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				switchCamera();
 			}
 		});
         
        //网络信息按钮初始化
        networkInfoBtn = (ImageButton)findViewById(R.id.NetworkInfoBtn);
        networkInfoBtn.setOnClickListener(new OnClickListener() {
           	public void onClick(View v)
           	{    	
           		mNetinfoIntent = new Intent(MediaPreviewActivity.this, NetInfoService.class);
       			startService(mNetinfoIntent);
       			mNetworkInfoServiceOn = true;
           	}
           	
           });		   		      
	}

	//直播滤镜相关方法（1）：滤镜种类
    private enum FilterType {        
        NORMAL, BLACK_WHITE, NIGHT_MODE, BLUR, FACE_WHITEN, SEPIA       
    }
    
    //直播滤镜相关方法（2）
    private static class FilterList {
        public List<String> names = new LinkedList<String>();
        public List<FilterType> filters = new LinkedList<FilterType>();

        public void addFilter(final String name, final FilterType filter) {
            names.add(name);
            filters.add(filter);
        }
    }
    
    //直播滤镜相关方法（3）
    private static int createFilterForType(final Context context, final FilterType type) {
    	int filter = Filters.FILTER_NONE;
    	switch (type) {
    	    case NORMAL:
    	    	filter = Filters.FILTER_NONE;
    	    	break;
    	    case BLACK_WHITE:
    	    	filter = Filters.FILTER_BLACK_WHITE;
    	    	break;
    	    case NIGHT_MODE:
    	    	filter = Filters.FILTER_NIGHT;
    	    	break;
    	    case BLUR:
    	    	filter = Filters.FILTER_BLUR;
    	    	break;
    	    case FACE_WHITEN:
    	    	filter = Filters.FILTER_WHITEN;
    	    	break;
    	    case SEPIA:
    	    	filter = Filters.FILTER_SEPIA;
    	    	break;    	    	
            default:
                throw new IllegalStateException("No filter of that type!");
        }
    	
    	return filter;

    }
    
    //直播滤镜相关方法（4）
    public static void showDialog(final Context context, final OnFilterChosenListener listener) {
        final FilterList filters = new FilterList();
        
        filters.addFilter("普通", FilterType.NORMAL);
        filters.addFilter("黑白", FilterType.BLACK_WHITE);
        filters.addFilter("夜景", FilterType.NIGHT_MODE);
        filters.addFilter("模糊", FilterType.BLUR);
        filters.addFilter("美白", FilterType.FACE_WHITEN);
        filters.addFilter("黄昏", FilterType.SEPIA);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(filters.names.toArray(new String[filters.names.size()]),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int item) {
                        listener.onFilterChosenListener(createFilterForType(context, filters.filters.get(item)));
                    }
                });

        builder.create().show();
    }
    
    //直播滤镜相关方法（5）
    public interface OnFilterChosenListener {
      void onFilterChosenListener(int filter);
    }
    
    //直播滤镜相关方法（6）   
    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.filterBtn:
    		showDialog(this, new OnFilterChosenListener() {
    			@Override
    			public void onFilterChosenListener(int filter) {
    				mLSMediaCapture.setFilterType(filter);
    				}
    			});
    		break;
    	case R.id.image_close:
			m_tryToStopLivestreaming = true;
			if(m_tryToStopLivestreaming){
				mLiveEndPreter.liveEnd(liveId,PreferenceUtils.getToken(this));
			}
    		break;
    	}
	}
    
    //切换前后摄像头
	private void switchCamera() {
		if(mLSMediaCapture != null) {
			mLSMediaCapture.switchCamera();
		}
	}
	
	//开始直播
	private void startAV(){
		if(mLSMediaCapture != null && m_liveStreamingInitFinished) {
			
			if(!mHardWareEncEnable && mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType == HAVE_AV || mLSLiveStreamingParaCtx.eOutStreamType.outputStreamType == HAVE_VIDEO)
			{
			    mLSMediaCapture.setWaterMarkPara(mWaterMarkOn, mWaterMarkFilePath, mWaterMarkPosX, mWaterMarkPosY);
			}
			
		    mLSMediaCapture.startLiveStreaming();
		    m_liveStreamingOn = true;
		}
	}
		
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
               
        //从直播设置页面获取推流URL和分辨率信息
        //alert1用于检测设备SDK版本是否符合开启滤镜要求，alert2用于检测设备的硬件编码模块（与滤镜相关）是否正常
        mliveStreamingURL = getIntent().getStringExtra("mediaPath");
        mVideoResolution = getIntent().getStringExtra("videoResolution");      
        mAlert1 = getIntent().getBooleanExtra("alert", false);
        mAlert2 = getIntent().getBooleanExtra("alert2", false);
        mFilter = getIntent().getBooleanExtra("filter", false);

		if(mFilter)
        {
            if(mAlert1)
            {
            	mHardWareEncEnable = false;
        	    showAlertDialog1();
            }
            else if(mAlert2)
            {
            	mHardWareEncEnable = false;
        	    showAlertDialog2();
            }
            else
            {
            	mHardWareEncEnable = true;
            }
        }
        else
        {
        	mHardWareEncEnable = false;
        }

		if(mHardWareEncEnable)
		{
			setContentView(R.layout.video_player_opengl_surface_view);
		    mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.camerasurfaceview);
		}
		else		
		{
//			setContentView(R.layout.video_player_surface_view);
			setContentView(R.layout.activity_live3);
			ButterKnife.bind(this);
            mVideoView = (LiveSurfaceView) findViewById(R.id.videoview);
			initExtraData();
			initViews();
        }

        m_liveStreamingOn = false;
        m_liveStreamingPause = false;
        m_tryToStopLivestreaming = false;
        
        //获取摄像头支持的分辨率信息，根据这个信息，用户可以选择合适的视频preview size和encode size
        //!!!!注意，用户在设置视频采集分辨率和编码分辨率的时候必须预先获取摄像头支持的采集分辨率，并设置摄像头支持的采集分辨率，否则会造成不可预知的错误，导致直播推流失败和SDK崩溃
        //用户可以采用下面的方法，获取摄像头支持的采集分辨率列表
        //获取分辨率之后，用户可以按照下表选择性设置采集分辨率和编码分辨率(注意：一定要Android设备支持该种采集分辨率才可以设置，否则会造成不可预知的错误，导致直播推流失败和SDK崩溃)
        //采集分辨率     编码分辨率     建议码率
        //1280x720     1280x720     1500kbps
        //1280x720     960x540      800kbps
        //960x720      960x720      1000kbps
        //960x720      960x540      800kbps
        //960x540      960x540      800kbps
        //640x480      640x480      600kbps
        //640x480      640x360      500kbps
        //320x240      320x240      250kbps
        //320x240      320x180      200kbps
        
        //List<Camera.Size> cameraSupportSize = getCameraSupportSize();
        
        if(mVideoResolution.equals("HD"))
        {
        	mVideoPreviewWidth = 960;
        	mVideoPreviewHeight = 720;
        }
        else if(mVideoResolution.equals("SD"))
        {
        	mVideoPreviewWidth = 640;
        	mVideoPreviewHeight = 480;
        }
        else
        {
        	mVideoPreviewWidth = 320;
        	mVideoPreviewHeight = 240;
        }
        
	    mContext = this; 	
	    
	    //创建直播实例
        mLSMediaCapture = new lsMediaCapture(this, mContext, mVideoPreviewWidth, mVideoPreviewHeight);

		if(mHardWareEncEnable)
		{
		    mCameraSurfaceView.setPreviewSize(mVideoPreviewWidth, mVideoPreviewHeight);
        }
		else
		{
		    mVideoView.setPreviewSize(mVideoPreviewWidth, mVideoPreviewHeight);
		}

        //创建参数实例
        mLSLiveStreamingParaCtx = mLSMediaCapture.new LSLiveStreamingParaCtx();
        mLSLiveStreamingParaCtx.eHaraWareEncType = mLSLiveStreamingParaCtx.new HardWareEncEnable();
        mLSLiveStreamingParaCtx.eOutFormatType = mLSLiveStreamingParaCtx.new OutputFormatType();
        mLSLiveStreamingParaCtx.eOutStreamType = mLSLiveStreamingParaCtx.new OutputStreamType();
        mLSLiveStreamingParaCtx.sLSAudioParaCtx = mLSLiveStreamingParaCtx.new LSAudioParaCtx();
        mLSLiveStreamingParaCtx.sLSAudioParaCtx.codec = mLSLiveStreamingParaCtx.sLSAudioParaCtx.new LSAudioCodecType();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx = mLSLiveStreamingParaCtx.new LSVideoParaCtx();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.codec = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new LSVideoCodecType();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new CameraPosition();
        mLSLiveStreamingParaCtx.sLSVideoParaCtx.interfaceOrientation = mLSLiveStreamingParaCtx.sLSVideoParaCtx.new CameraOrientation();

        //发送统计数据到网络信息界面
        staticsHandle();
    	
        if(mLSMediaCapture != null) {  
        	boolean ret = false;
        	
        	//设置摄像头信息，并开始本地视频预览
        	mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition.cameraPosition = CAMERA_POSITION_BACK;//默认后置摄像头，用户可以根据需要调整
            if(mHardWareEncEnable)
			{
			    mLSMediaCapture.startVideoPreviewOpenGL(mCameraSurfaceView, mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition.cameraPosition);
            }
			else
			{
			    mLSMediaCapture.startVideoPreview(mVideoView, mLSLiveStreamingParaCtx.sLSVideoParaCtx.cameraPosition.cameraPosition);
			}

            //配置音视频和camera参数
            paraSet();
            
            //设置日志级别
        	mLSMediaCapture.setTraceLevel(mLogLevel);
        	
            //初始化直播推流
	        ret = mLSMediaCapture.initLiveStream(mliveStreamingURL, mLSLiveStreamingParaCtx);
	        
	        if(ret) {
	        	m_liveStreamingInit = true;
	        	m_liveStreamingInitFinished = true;
	        }
	        else {
	        	m_liveStreamingInit = true;
	        	m_liveStreamingInitFinished = false;
	        }
        }
		
        //Demo控件的初始化
        buttonInit();
/////////////////////////////////////////////////////////////////////
		/**
		 * 设置为竖屏
		 */
		if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}
		loadData();
		loadFansData();

    }

	@Override
	protected void onDestroy() {
		if(m_liveStreamingInit) {
			m_liveStreamingInit = false;
		}
		
		//Demo层网络信息显示操作的销毁
        if(mNetworkInfoServiceOn) {       	
            mNetInfoIntent.putExtra("frameRate", 0);  
        	mNetInfoIntent.putExtra("bitRate", 0);
      	    mNetInfoIntent.putExtra("resolution", 2);
         
            sendBroadcast(mNetInfoIntent);  

            stopService(mNetinfoIntent); 
            mNetworkInfoServiceOn = false;
        }
        
        //Demo层报警信息操作的销毁
        if(mAlertServiceOn) {
        	mAlertServiceIntent = new Intent(MediaPreviewActivity.this, AlertService.class);
            stopService(mAlertServiceIntent); 
            mAlertServiceOn = false;
        }
        
        //停止直播调用相关API接口
		if(mLSMediaCapture != null && m_liveStreamingInitFinished && m_liveStreamingOn) {		
			mLSMediaCapture.stopLiveStreaming();		
			mLSMediaCapture.stopVideoPreview();
			mLSMediaCapture.destroyVideoPreview();	
			mLSMediaCapture = null;
			
  	        mIntentLiveStreamingStopFinished.putExtra("LiveStreamingStopFinished", 2);   
            sendBroadcast(mIntentLiveStreamingStopFinished); 
		}
		else if(mLSMediaCapture != null && m_liveStreamingInitFinished)
		{		
			mLSMediaCapture.stopVideoPreview();
			mLSMediaCapture.destroyVideoPreview();	
			mLSMediaCapture = null;
			
  	        mIntentLiveStreamingStopFinished.putExtra("LiveStreamingStopFinished", 1); 
            sendBroadcast(mIntentLiveStreamingStopFinished); 
		}
		else if(!m_liveStreamingInitFinished) {
  	        mIntentLiveStreamingStopFinished.putExtra("LiveStreamingStopFinished", 1);  
            sendBroadcast(mIntentLiveStreamingStopFinished);  
		}

		if(m_liveStreamingOn) {
		    m_liveStreamingOn = false;
		}
		if (startLiveChat != null) {
			startLiveChat.socketEventOff();
		}
		super.onDestroy();
	}

	@Override
	protected void onPause(){
        if(mLSMediaCapture != null) {  		
    		if(!m_tryToStopLivestreaming)
    		{  			
        		//继续视频推流，推固定图像
        		mLSMediaCapture.resumeVideoEncode();
        		
        		//释放音频采集资源
        		//mLSMediaCapture.stopAudioRecord();
    		}
        }
        super.onPause();
		if (startLiveChat != null) {
			startLiveChat.exitRoom();
		}
    }

	@Override
	protected void onRestart(){
        if(mLSMediaCapture != null) {
        	//关闭推流固定图像
        	mLSMediaCapture.stopVideoEncode();
            
            //关闭推流静音帧
            //mLSMediaCapture.stopAudioEncode();
        }        
        super.onRestart(); 
    }  

    //处理SDK抛上来的异常和事件，用户需要在这里监听各种消息，进行相应的处理。
    //例如监听断网消息，用户根据断网消息进行直播重连
	@Override
	public void handleMessage(int msg, Object object) {
		  switch (msg) {
		      case MSG_INIT_LIVESTREAMING_OUTFILE_ERROR://初始化直播出错
		      case MSG_INIT_LIVESTREAMING_VIDEO_ERROR:	
		      case MSG_INIT_LIVESTREAMING_AUDIO_ERROR:
		      {
		    	  if(m_liveStreamingInit)
		    	  {
	      		      Bundle bundle = new Bundle();
	                  bundle.putString("alert", "MSG_INIT_LIVESTREAMING_ERROR");
	          	      Intent intent = new Intent(MediaPreviewActivity.this, AlertService.class);
	          	      intent.putExtras(bundle);
	      		      startService(intent);
	      		      mAlertServiceOn = true;
		    	  }
		    	  break;
		      }
		      case MSG_START_LIVESTREAMING_ERROR://开始直播出错
		      {
		    	  break;
		      }
		      case MSG_STOP_LIVESTREAMING_ERROR://停止直播出错
		      {
		    	  if(m_liveStreamingOn)
		    	  {
	      		      Bundle bundle = new Bundle();
	                  bundle.putString("alert", "MSG_STOP_LIVESTREAMING_ERROR");
	          	      Intent intent = new Intent(MediaPreviewActivity.this, AlertService.class);
	          	      intent.putExtras(bundle);
	      		      startService(intent);
	      		      mAlertServiceOn = true;
		    	  }
		    	  break;
		      }
		      case MSG_AUDIO_PROCESS_ERROR://音频处理出错
		      {
		    	  if(m_liveStreamingOn && System.currentTimeMillis() - mLastAudioProcessErrorAlertTime >= 10000)
		    	  {
	      		      Bundle bundle = new Bundle();
	                  bundle.putString("alert", "MSG_AUDIO_PROCESS_ERROR");
	          	      Intent intent = new Intent(MediaPreviewActivity.this, AlertService.class);
	          	      intent.putExtras(bundle);
	      		      startService(intent);
	      		      mAlertServiceOn = true;
	      		      mLastAudioProcessErrorAlertTime = System.currentTimeMillis();
		    	  }
		    	  
		    	  break;
		      }
		      case MSG_VIDEO_PROCESS_ERROR://视频处理出错
		      {
		    	  if(m_liveStreamingOn && System.currentTimeMillis() - mLastVideoProcessErrorAlertTime >= 10000)
		    	  {
	      		      Bundle bundle = new Bundle();
	                  bundle.putString("alert", "MSG_VIDEO_PROCESS_ERROR");
	          	      Intent intent = new Intent(MediaPreviewActivity.this, AlertService.class);
	          	      intent.putExtras(bundle);
	      		      startService(intent);
	      		      mAlertServiceOn = true;
	      		      mLastVideoProcessErrorAlertTime = System.currentTimeMillis();
		    	  }
		    	  break;
		      }
		      case MSG_RTMP_URL_ERROR://断网消息
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_RTMP_URL_ERROR");

		    	  break;
		      }
		      case MSG_URL_NOT_AUTH://直播URL非法
		      {
		    	  if(m_liveStreamingInit)
		    	  {
	      		      Bundle bundle = new Bundle();
	                  bundle.putString("alert", "MSG_URL_NOT_AUTH");
	          	      Intent intent = new Intent(MediaPreviewActivity.this, AlertService.class);
	          	      intent.putExtras(bundle);
	      		      startService(intent);
	      		      mAlertServiceOn = true;
		    	  }
		    	  break;
		      }
		      case MSG_SEND_STATICS_LOG_ERROR://发送统计信息出错
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_SEND_STATICS_LOG_ERROR");
		    	  break;
		      }
		      case MSG_SEND_HEARTBEAT_LOG_ERROR://发送心跳信息出错
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_SEND_HEARTBEAT_LOG_ERROR");
		    	  break;
		      }
		      case MSG_AUDIO_SAMPLE_RATE_NOT_SUPPORT_ERROR://音频采集参数不支持
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_AUDIO_SAMPLE_RATE_NOT_SUPPORT_ERROR");
		    	  break;
		      }
		      case MSG_AUDIO_PARAMETER_NOT_SUPPORT_BY_HARDWARE_ERROR://音频参数不支持
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_AUDIO_PARAMETER_NOT_SUPPORT_BY_HARDWARE_ERROR");
		    	  break;
		      }
		      case MSG_NEW_AUDIORECORD_INSTANCE_ERROR://音频实例初始化出错
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_NEW_AUDIORECORD_INSTANCE_ERROR");
		    	  break;
		      }
		      case MSG_AUDIO_START_RECORDING_ERROR://音频采集出错
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_AUDIO_START_RECORDING_ERROR");
		    	  break;
		      }
		      case MSG_OTHER_AUDIO_PROCESS_ERROR://音频操作的其他错误
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_OTHER_AUDIO_PROCESS_ERROR");
		    	  break;
		      }
		      case MSG_QOS_TO_STOP_LIVESTREAMING://网络QoS极差，码率档次降到最低
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_QOS_TO_STOP_LIVESTREAMING");
//		    	  m_tryToStopLivestreaming = true;
//		    	  m_QoSToStopLivestreaming = true;
//		  		  mLSMediaCapture.stopLiveStreaming();
		    	  break;
		      }
		      case MSG_HW_VIDEO_PACKET_ERROR:
		      {
		    	  if(m_liveStreamingOn)
		    	  {
		    	      Bundle bundle = new Bundle();
	                  bundle.putString("alert", "MSG_HW_VIDEO_PACKET_ERROR");
	          	      Intent intent = new Intent(MediaPreviewActivity.this, AlertService.class);
	          	      intent.putExtras(bundle);
	      		      startService(intent);
	      		      mAlertServiceOn = true;
		    	  }
		    	  break;
		      }
		      case MSG_WATERMARK_INIT_ERROR://视频水印操作初始化出错
		      {
		    	  break;
		      }
		      case MSG_WATERMARK_PIC_OUT_OF_VIDEO_ERROR://视频水印图像超出原始视频出错
		      {
		    	  //Log.i(TAG, "test: in handleMessage: MSG_WATERMARK_PIC_OUT_OF_VIDEO_ERROR");
		    	  break;
		      }
		      case MSG_WATERMARK_PARA_ERROR://视频水印参数设置出错
		      {
		    	  //Log.i(TAG, "test: in handleMessage: MSG_WATERMARK_PARA_ERROR");
		    	  break;
		      }
		      case MSG_CAMERA_PREVIEW_SIZE_NOT_SUPPORT_ERROR://camera采集分辨率不支持
		      {
		    	  //Log.i(TAG, "test: in handleMessage: MSG_CAMERA_PREVIEW_SIZE_NOT_SUPPORT_ERROR");
		    	  break;
		      }
		      case MSG_START_PREVIEW_FINISHED://camera采集预览完成
		      {
		    	  break;
		      }
		      case MSG_START_LIVESTREAMING_FINISHED://开始直播完成
		      {
		    	  break;
		      }
		      case MSG_STOP_LIVESTREAMING_FINISHED://停止直播完成
		      {
		    	  //Log.i(TAG, "test: MSG_STOP_LIVESTREAMING_FINISHED");	
		          {
		        	  mIntentLiveStreamingStopFinished.putExtra("LiveStreamingStopFinished", 1);  
	                  sendBroadcast(mIntentLiveStreamingStopFinished); 
		          }
		          
	              break;
		      }
		      case MSG_STOP_VIDEO_CAPTURE_FINISHED:
		      {
		    	  //Log.i(TAG, "test: in handleMessage: MSG_STOP_VIDEO_CAPTURE_FINISHED");
		    	  if(!m_tryToStopLivestreaming && mLSMediaCapture != null)
		    	  {
		    	      //继续视频推流，推最后一帧图像
		    	      mLSMediaCapture.resumeVideoEncode();
		    	  }
		    	  break;
		      }
		      case MSG_STOP_RESUME_VIDEO_CAPTURE_FINISHED:
		      {
		    	  //Log.i(TAG, "test: in handleMessage: MSG_STOP_RESUME_VIDEO_CAPTURE_FINISHED");
		    	  //开启视频preview
		    	  if(mLSMediaCapture != null)
		    	  {
		              mLSMediaCapture.resumeVideoPreview();
		              m_liveStreamingOn = true;
		    	      //开启视频推流，推正常帧
		              mLSMediaCapture.startVideoLiveStream();
		    	  }
		    	  break;
		      }
		      case MSG_STOP_AUDIO_CAPTURE_FINISHED:
		      {
		    	  //Log.i(TAG, "test: in handleMessage: MSG_STOP_AUDIO_CAPTURE_FINISHED");
		    	  if(!m_tryToStopLivestreaming && mLSMediaCapture != null)
		    	  {
		    	      //继续音频推流，推静音帧
		    	      mLSMediaCapture.resumeAudioEncode();
		    	  }
		    	  break;
		      }
		      case MSG_STOP_RESUME_AUDIO_CAPTURE_FINISHED:
		      {
		    	  //Log.i(TAG, "test: in handleMessage: MSG_STOP_RESUME_AUDIO_CAPTURE_FINISHED");
		    	  //开启音频推流，推正常帧
		          mLSMediaCapture.startAudioLiveStream();
		    	  break;
		      }
		      case MSG_SWITCH_CAMERA_FINISHED://切换摄像头完成
		      {
		    	  int cameraId = (Integer) object;//切换之后的camera id
		    	  break;
		      }
		      case MSG_SEND_STATICS_LOG_FINISHED://发送统计信息完成
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_SEND_STATICS_LOG_FINISHED");
		    	  break;
		      }
		      case MSG_SERVER_COMMAND_STOP_LIVESTREAMING:
		      {
		    	  //Log.i(TAG, "test: in handleMessage, MSG_SERVER_COMMAND_STOP_LIVESTREAMING");
		    	  break;
		      }
		      case MSG_GET_STATICS_INFO:
		      {
				  Message message = new Message();
				  mStatistics = (Statistics) object;
				    			      			  
	              Bundle bundle = new Bundle();  
	              bundle.putInt("FR", mStatistics.videoSendFrameRate);  
	              bundle.putInt("VBR", mStatistics.videoSendBitRate);  
	              bundle.putInt("ABR", mStatistics.audioSendBitRate);   
	              bundle.putInt("TBR", mStatistics.totalRealSendBitRate);   
	              message.setData(bundle);  
	                  
	              mHandler.sendMessage(message);
		      }
		  }
	}	
	
	//Demo层视频缩放操作相关方法
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
        	    //Log.i(TAG, "test: down!!!");  
                break;                                                                                                
            case MotionEvent.ACTION_MOVE:                                                                                                                   
        	    //Log.i(TAG, "test: move!!!");  
                /** 
                 * 首先判断按下手指的个数是不是大于两个。 
                 * 如果大于两个则执行以下操作（即图片的缩放操作）。 
                 */  
                if (event.getPointerCount() >= 2) {  

                    float offsetX = event.getX(0) - event.getX(1);  
                    float offsetY = event.getY(0) - event.getY(1);  
                    /** 
                     * 原点和滑动后点的距离差 
                     */  
                    mCurrentDistance = (float) Math.sqrt(offsetX * offsetX + offsetY * offsetY);  
                    if (mLastDistance < 0) {  
                    	mLastDistance = mCurrentDistance;  
                    } else {  
                            /** 
                             * 如果当前滑动的距离（currentDistance）比最后一次记录的距离（lastDistance）相比大于5英寸（也可以为其他尺寸）， 
                             * 那么现实图片放大 
                             */  
                        if (mCurrentDistance - mLastDistance > 5) {  
                            //Log.i(TAG, "test: 放大！！！");  
                            if(mLSMediaCapture != null) {
                            	mLSMediaCapture.setCameraZoomPara(true);
                            }

                            mLastDistance = mCurrentDistance;  
                            /** 
                             * 如果最后的一次记录的距离（lastDistance）与当前的滑动距离（currentDistance）相比小于5英寸， 
                             * 那么图片缩小。 
                             */  
                        } else if (mLastDistance - mCurrentDistance > 5) {  
                            //Log.i(TAG, "test: 缩小！！！");
                            if(mLSMediaCapture != null) {
                            	mLSMediaCapture.setCameraZoomPara(false);
                            }
                            
                            mLastDistance = mCurrentDistance;  
                        }  
                    }  
                }  
                break;                                                                                                          
            case MotionEvent.ACTION_UP: 
        	    //Log.i(TAG, "test: up!!!");  
                break;
            default:
                break;
        }
        return true;
	}
	
	@Override  
    public void onBackPressed() {  
        super.onBackPressed();  
        m_tryToStopLivestreaming = true;
		if(m_tryToStopLivestreaming){
			mLiveEndPreter.liveEnd(liveId,PreferenceUtils.getToken(this));
		}
    }

	@Override
	public void showToast(String msg) {

	}

	@Override
	public void showDialog(String msg) {

	}

	@Override
	public void netError() {

	}

	@Override
	public void hideDialog() {

	}

	@Override
	public void login() {

	}

	@Override
	public void getCourseWareSuccess(List<CourseWare> list) {

	}

	@Override
	public void getFansSuccess(FansResponse response) {
		if (response.body.fans.size() <= 0) {
			showToast("该主播暂无粉丝，快来加关注吧");
		}
		fansIndex = response.body.next;
		fansList.clear();
		fansList.addAll(response.body.fans);
		fansAdapter.notifyDataSetChanged();
	}

	@Override
	public void sendGiftSuccess() {

	}

	@Override
	public void sendStarBeanSuccess() {

	}

	@Override
	public void getRandomBeanSuccess(String count) {

	}

	@Override
	public void setfocuseSuccess() {
		startLiveChat.mLvAdapter.setFocus(true);

	}

	@Override
	public void setfocuseCancelSuccess() {
		startLiveChat.mLvAdapter.setFocus(false);
	}

	@Override
	public void getOtherUserInfoSuccess(OtherUserInfoResponse response) {
		startLiveChat.mLvAdapter.showOtherUserInfoDialog(response);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (startLiveChat != null) {
			startLiveChat.joinRoom();
		}

	}
	@Override
	public void LiveEndSuccess(LiveEndResponse response) {
		if(response!=null&&response.getResult().equals("0000")){
			finish();
		}
	}

	@Override
	public void LiveEndFailed(String dec) {
		if(!TextUtils.isEmpty(dec)){
			showToast(dec);
		}
	}
	protected void initExtraData() {

		liveId=getIntent().getStringExtra("liveid");
//		local=getIntent().getStringExtra("local");
		local=PreferenceUtils.getLocal(this);
		liveUserId=getIntent().getStringExtra("userid");
		userName=getIntent().getStringExtra("username");
		userLogo=getIntent().getStringExtra("logourl");

		if(!TextUtils.isEmpty(local)){
			mTvLiveLocation.setText(local);
		}else{
			mTvLiveLocation.setVisibility(View.GONE);
		}
		if(userName!=null){
			mTvLiveName.setText(userName);
		}
		if(userLogo!=null){
			WRStarApplication.imageLoader.displayImage(userLogo, mImgAvatar, WRStarApplication.getListOptions());
		}
	}

	public void initViews() {
		image_close.setOnClickListener(this);

		gvFans.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
					case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

						// 判断滚动到底部
						if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                            LogUtil.i("到底部啦。可以请求刷新咯~~~~~~");
							loadFansData();
						}
						break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
		gvFans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

				presenter.getOtherUserInfo(fansList.get(position).userid);

				startLiveChat.mLvAdapter.getDetailsInfoDialog().setOnFocus(new DetailsInfoDialog.OnFocusListener() {
					@Override
					public void setFocus(boolean isFocus) {
						presenter.setFocus(fansList.get(position).userid, isFocus);
					}
				});

			}
		});
		mLiveEndPreter=new LiveEndPresenter(this,this);
		presenter = new LiveRoomPresenter(this, this);
		startLiveChat=new StartLiveChat(this,liveId,presenter);
	}
	protected void loadData() {

		if (fansList == null) {
			fansList = new ArrayList<Fans>();
			fansAdapter = new FansAdapter(getBaseContext(), fansList);
			gvFans.setAdapter(fansAdapter);
			loadFansData();
		}
	}

	public void loadFansData() {

		if (fansIndex == -1) {
			showToast("没有更多粉丝了");
			return;
		}

		presenter.getFans(liveUserId, fansIndex);

	}
}
