package com.wmx.android.wrstar.biz;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.wmx.android.wrstar.biz.response.AddCommentLikeResponse;
import com.wmx.android.wrstar.biz.response.AddVideoLikeResponse;
import com.wmx.android.wrstar.biz.response.SendVideoCommentResponse;
import com.wmx.android.wrstar.constants.Urls;
import com.wmx.android.wrstar.entities.DetailViewInfo;
import com.wmx.android.wrstar.entities.VideoComment;
import com.wmx.android.wrstar.net.GsonRequest;
import com.wmx.android.wrstar.net.RequestManager;
import com.wmx.android.wrstar.utils.LogUtil;
import com.wmx.android.wrstar.utils.ParamsUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/10.
 */
public class DetailsVideoBiz {


    /**
     * TAG.
     */
    public static final String TAG = "VideoBiz";

    private static volatile DetailsVideoBiz sInstance;

    private static Context sContext;

    /**
     * 网络请求管理器.
     */
    private static RequestManager sRequestManager;

    /**
     * 帐号相关业务.
     */
    private DetailsVideoBiz() {
    }

    /**
     * 获取实例化对象.
     *
     * @param context Context
     * @return 单例的实例
     */
    public static DetailsVideoBiz getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (DetailsVideoBiz.class) {
                if (sInstance == null) {
                    sContext = context;
                    sInstance = new DetailsVideoBiz();
                    sRequestManager = RequestManager.getInstance(sContext);
                }
            }
        }
        return sInstance;
    }

    public void getDetailsVideoInfo(String mobnum, String courseId, String episodeId, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.GET_DETAILSVIDEO_INFO;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();
        if (courseId != null) {
            bodyParams.put("courseid", courseId);
        }
        if (episodeId != null) {
            bodyParams.put("episodeid", episodeId);
        }
        if (mobnum != null) {
            bodyParams.put("userid", mobnum);
        }

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<DetailViewInfo> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                DetailViewInfo.class, responseListener, errorListener);
        LogUtil.i("视频详情DetailsVideoInfo  =========:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }


    public void sendComment(String mobnum, String courseId, String comment, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.DETAILSCOMMENT;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        if (comment != null && !TextUtils.isEmpty(comment)) {

            bodyParams.put("comment", comment);
        } else {
            return;
        }
        bodyParams.put("userid", mobnum);
        bodyParams.put("action", "2");  //action=2  发送评论
        bodyParams.put("courseid", courseId);

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<SendVideoCommentResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                SendVideoCommentResponse.class, responseListener, errorListener);
        LogUtil.i("视频详情sendComment  =========:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }


    public void getComment(String courseId, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.DETAILSCOMMENT;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();


        bodyParams.put("action", "1");  //action=1  获取评论
        bodyParams.put("courseid", courseId);

        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<VideoComment> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                VideoComment.class, responseListener, errorListener);
        LogUtil.i("视频详情getComment  =========:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }


    public void addCommentLike(String mobnum, String courseId, String commentId, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.DETAILSCOMMENT;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("userid", mobnum);
        bodyParams.put("action", "3");  //action=3  add like
        bodyParams.put("courseid", courseId);
        bodyParams.put("commentid", commentId);
        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<AddCommentLikeResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                AddCommentLikeResponse.class, responseListener, errorListener);
        LogUtil.i("视频详情CommentLike  =========:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }


    public void addVideoLike(String mobnum, String courseId, Response.Listener responseListener, Response.ErrorListener errorListener, String tag) {

        final String url = Urls.DETAILS_LIKE;

        final Map<String, Object> params = ParamsUtil.getBasicInfo();

        Map<String, String> bodyParams = new HashMap<>();

        bodyParams.put("userid", mobnum);
        bodyParams.put("action", "1");  //action=1  add like
        bodyParams.put("courseid", courseId);

        // bodyParams.put("userid", userid);
        params.put("body", bodyParams);
        Gson gson = new Gson();
        final GsonRequest<AddVideoLikeResponse> gsonRequest = new GsonRequest<>(sContext, url, gson.toJson(params),
                AddVideoLikeResponse.class, responseListener, errorListener);
        LogUtil.i("视频详情Like  =========:"+new JSONObject(params).toString());
        sRequestManager.add(gsonRequest, tag);
    }


}
