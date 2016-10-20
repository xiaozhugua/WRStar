package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;

/**
 * 服务端接口响应结果的基类.
 */
public class BaseResponse {

    /**
     * 业务码.
     */
    @SerializedName("result")
    private String mResult;

    @SerializedName("msgname")
    private String msgname;



    /**
     * 业务信息.
     */
    @SerializedName("resultdesc")
    private String mResultDesc;

    @SerializedName("transactionid")
    private String mTransactionId;

    @SerializedName("timestamp")
    private String mTimeStamp;

    @SerializedName("version")
    private String version;

    @SerializedName("appid")
    private String appid;

    @SerializedName("appkey")
    private String appkey;

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

    public String getResultDesc() {
        return mResultDesc;
    }

    public void setResultDesc(String resultDesc) {
        mResultDesc = resultDesc;
    }

    public String getTransactionId() {
        return mTransactionId;
    }

    public void setTransactionId(String transactionId) {
        mTransactionId = transactionId;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        mTimeStamp = timeStamp;
    }


    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
