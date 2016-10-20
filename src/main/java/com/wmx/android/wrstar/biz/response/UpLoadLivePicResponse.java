package com.wmx.android.wrstar.biz.response;

/**
 * 上传直播宣传图
 */
public class UpLoadLivePicResponse extends  BaseResponse{


    /**
     * url : http://qhcdn.oss-cn-hangzhou.aliyuncs.com/wmx/user/logo/18333615191_1469428573302_4_image.jpg
     */

    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
