package com.wmx.android.wrstar.constants;

/**
 * 网络请求地址.
 */
public class Urls {

    /** 服务器地址 */
    public static final String HOST = Flavors.HOST;

    /**直播聊天 socket */
    public static final String SOCKET_CHAT = Flavors.SOCKET_IP ;

    /** 获取验证码 */
    public static final String GET_DYNAMIC_PASS = HOST + "getdynamicpwdreq";

    /** 注册 */
    public static final String REGISTER = HOST + "userregisterreq";

    /** 登录 */
    public static final String LOGIN = HOST + "userloginreq";

    /** TOKEN登录 */
    public static final String TOKEN_LOGIN = HOST + "tokenlogin";

    /** 社会化登陆 */
    public static final String SOCIAL_LOGIN = HOST + "authloginreq";

    /**重置密码*/
    public static final String RESET_PASSWORD = HOST + "setpasswdreq";

    /**
     * 第三方登录验证手机号*/
    public static final String CHECK_PHONE_NUM = HOST + "validaccountreq";

    /**绑定手机号*/
    public static final String BIND_PHONE_NUM = HOST + "accountbindreq";


    /**大家都在看 */
    public static final String GET_ALLSEEVIDEO = HOST + "videoindex";

    /**具体视频信息 */
    public static final String GET_DETAILSVIDEO_INFO = HOST + "videoepisode";


    /**具体视频的评论  */
    public static final String DETAILSCOMMENT = HOST + "videocomment";


    /**精彩视频  */
    public static final String WONDERFUL_VIDEO = HOST + "video/course";


    /**视频 Like  */
    public static final String DETAILS_LIKE = HOST + "video/operation";


    /**获取我的信息  */
    public static final String GET_USER_INFO = HOST + "userinfo";

    /**获取他人信息  */
    public static final String GET_OTHER_USER_INFO = HOST + "otheruserinfo";


    /**修改我的信息  */
    public static final String MODIFY_USER_INFO = HOST + "setuserinforeq";


    /**我的视频  */
    public static final String MY_VIDEO = HOST + "my/collection";

    /**上传头像  */
    public static final String UPLOAD_AVATOR = HOST + "setuserlogoreq";


    /**详细直播信息  */
    public static final String DETAILS_LIVE = HOST + "livedetail";

    /**直播会堂LivePageFragment 列表 */
    public static final String LIVE_HALL = HOST + "livehall";

    /**预约直播 */
    public static final String LIVE_BOOK = HOST + "liveuseroperatoin";


    /** 直播课件 */
    public static final String LIVE_COURSEWARE = HOST + "livecourseware";
    /** 分享成功 */
    public static final String SHARESUCCESS = HOST + "addliveinvite";



    /** 获取 礼物 列表 */
    public static final String LIVE_REWARD = HOST + "livereward";

    /** 送出礼物   */
    public static final String LIVE_SEND_REWARD = HOST + "livedoreward";


    /** 礼物打赏购买记录   */
    public static final String MYBUY_REWARD = HOST + "getrewardrecord";


    /** 用户反馈FeedBack   */
    public static final String FEED_BACK = HOST + "feedbackmanager";


    /** 首页 请求   */
    public static final String FIRSTPAGE = HOST + "home";


    /** 支付宝支付*/

   public static final String PAY_ALI = HOST + "alipay";



    /** 微信支付*/

    public static final String PAY_WECHAT = HOST + "weixinpay";


    /** 土豪随意*/

    public static final String GIFT_RANDOM  = HOST + "baron";

    /** 关注 获取 粉丝 */

    public static final String FANS  = HOST + "fans";

    /** 创建标签 */

    public static final String CreateTAG  = HOST + "createtag";

    /** 获取热门标签 */

    public static final String GETHOTTAG  = HOST + "gethottag";

    /** 创建直播 */

    public static final String CREATELiVE  = HOST + "createlive";
    /** 上传直播宣传图 */

    public static final String UPLOADLIVEPIC  = HOST + "uploadimage";
    /** 管理直播信息 */

    public static final String MYLIVRINFOMANAGER  = HOST + "myliveinfomanager";
    /** 终止直播 */
    public static final String LIVEEND  = HOST + "liveend";
    /** 得到排行榜信息 */
    public static final String GETAPPPAI  = HOST + "getappshareinfo";
    /** 修改过的首页 --1 */
    public static final String HOMEPAGE_NEWS  = HOST + "homepage";

}
