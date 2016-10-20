package com.wmx.android.wrstar.player;

import android.database.DataSetObserver;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wmx.android.wrstar.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * List adapter used to drive the ListView in the activity.
 */
public class NELocalDirectory extends BaseAdapter {
    public final static String TAG = "NELivePlayer/NELocalDirectory";

    public final static HashSet<String> VIDEO;
    public final static HashSet<String> AUDIO;
    public final static HashSet<String> SUBTITLES;

    static {
        final String[] videoExtensions = {
                ".3g2", ".3gp", ".3gp2", ".3gpp", ".amv", ".asf", ".avi", ".divx", ".drc", ".dv",
                ".f4v", ".flv", ".gvi", ".gxf", ".ismv", ".iso", ".m1v", ".m2v", ".m2t", ".m2ts",
                ".m4v", ".mkv", ".mov", ".mp2", ".mp2v", ".mp4", ".mp4v", ".mpe", ".mpeg",
                ".mpeg1", ".mpeg2", ".mpeg4", ".mpg", ".mpv2", ".mts", ".mtv", ".mxf", ".mxg",
                ".nsv", ".nut", ".nuv", ".ogm", ".ogv", ".ogx", ".ps", ".rec", ".rm", ".rmvb",
                ".tod", ".ts", ".tts", ".vob", ".vro", ".webm", ".wm", ".wmv", ".wtv", ".xesc" };

        final String[] audioExtensions = {
                ".3ga", ".a52", ".aac", ".ac3", ".adt", ".adts", ".aif", ".aifc", ".aiff", ".amr",
                ".aob", ".ape", ".awb", ".caf", ".dts", ".flac", ".it", ".m4a", ".m4b", ".m4p",
                ".mid", ".mka", ".mlp", ".mod", ".mpa", ".mp1", ".mp2", ".mp3", ".mpc", ".mpga",
                ".oga", ".ogg", ".oma", ".opus", ".ra", ".ram", ".rmi", ".s3m", ".spx", ".tta",
                ".voc", ".vqf", ".w64", ".wav", ".wma", ".wv", ".xa", ".xm" };

        final String[] subtitlesExtensions = {
                "idx", "sub",  "srt", "ssa", "ass",  "smi", "utf", "utf8", "utf-8",
                "rt",   "aqt", "txt", "usf", "jss",  "cdg", "psb", "mpsub","mpl2",
                "pjs", "dks", "stl", "vtt" };

        VIDEO = new HashSet<String>();
        for (String item : videoExtensions)
            VIDEO.add(item);
        AUDIO = new HashSet<String>();
        for (String item : audioExtensions)
            AUDIO.add(item);
        SUBTITLES = new HashSet<String>();
        for (String item : subtitlesExtensions)
            SUBTITLES.add(item);
    }

    private ArrayList<String> mFiles = new ArrayList<String>();
    private boolean mAudio;

    public NELocalDirectory() {
        mAudio = true;
        refresh();
    }

    public boolean isAudioMode() {
        return mAudio;
    }

    public void setAudioMode(boolean b) {
        mAudio = b;
    }

    public void refresh() {
    	 LogUtil.d(TAG, "Refreshing adapter in " + (mAudio ? "audio mode" : "VideoPageResponse mode"));

        File[] files;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            int system_version = android.os.Build.VERSION.SDK_INT;

            if (system_version >= 23) {
                files = Environment.getRootDirectory().listFiles();
            }
            else {
                files = Environment.getExternalStorageDirectory().listFiles();
            }
        }else {
             //files = Environment.getRootDirectory().listFiles();
            return;
        }
        mFiles.clear();
        for(File f : files) {
            if(f.getName().contains(".")) {
                int i = f.getName().lastIndexOf(".");
                if (i > 0) {
                    String ext = f.getName().substring(i);
                    if ((mAudio && AUDIO.contains(ext)) ||
                            (!mAudio && VIDEO.contains(ext))) {
                        mFiles.add(f.getName());
                    }
                }
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFiles.size();
    }

    @Override
	public Object getItem(int position) {
	    return Environment.getExternalStorageDirectory()
	            .getAbsolutePath() + '/' + mFiles.get(position);
	}

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public int getItemViewType(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if(v == null) {
            v = new TextView(parent.getContext());
        }
        ((TextView)v).setText(mFiles.get(position));

        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return mFiles.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver arg0) {
        super.registerDataSetObserver(arg0);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver arg0) {
        super.unregisterDataSetObserver(arg0);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int arg0) {
        return true;
    }

}
