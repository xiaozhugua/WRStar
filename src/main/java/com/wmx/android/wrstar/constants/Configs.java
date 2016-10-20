
package com.wmx.android.wrstar.constants;

/**
 * Created by wubiao on 2015/12/29
 *
 * Des: 配置信息
 */
public class Configs {

    /**
     * 包配置信息
     */
    public static class Apk {

        /**
         * 正式版本标识.
         */
        public static final Boolean RELEASE = Flavors.RELEASE;
    }

    /**
     * 日志的配置信息.
     */
    public static class Log {

        /**
         * 写日志的等级.
         */
        public static final int WRITE_LEVEL = android.util.Log.DEBUG;
    }

    /**ImIm
     * 图片.
     */
    public static class Image {

        /**
         * 上传图片最大大小, 单位字节.
         */
        public static final int IMAGE_MAX_SIZE = 100 * 1024;

        /**
         * 图文图片的宽度.
         */
        public static final int IMAGE_WIDTH = 612;

        /**
         * 图文图片的高度.
         */
        public static final int IMAGE_HEIGHT = 612;

        /**
         * 头像宽度.
         */
        public static final int AVATAR_WIDTH = 200;
        /**
         * 头像宽度.
         */

        /**
         * 头像高度.
         */
        public static final int AVATAR_HEIGHT = 200;
    }

    /**
     * 分页.
     */
    public static class Paging {

        /**
         * 内容分页大小.
         */
        public static final int CONTENTS_SIZE = 20;
    }

    /**
     * 网络.
     */
    public static class Net {

        /**
         * 重试次数.
         */
        public static final int RETRY = 1;

        /**
         * 默认网络超时时间.
         */
        public static final int TIMEOUT = 8000;

        /**
         * 图片文件名.
         */
        public static final String IMAGE_NAME = "image.jpeg";
    }
}
