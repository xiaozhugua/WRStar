package com.wmx.android.wrstar.constants;

/**
 * 错误码
 * 
 * @author qugf
 * 
 */
public class ServerCodes {

	/** 成功 */
	public  static final String SUCCESS = "0000";

	/**
	 * 密码错误.
	 */
	public static final String PASS_ERROR = "1020";

	/**
	 * 验证码错误.
	 */
	public static final String AUTH_CODE_ERROR = "1023";

	/**
	 * 验证码超时.
	 */
	public static final String AUTH_CODE_TIMEOUT = "1024";

	/**
	 * 手机号已被注册.
	 */
	public static final String ACCOUNT_EXISTED = "4001";

	/**
	 * 用户不存在.
	 */
	public static final String ACCOUNT_NOT_EXIST = "4003";

	/**
	 * 第三方账号未绑定手机号.
	 * */
	public static final String SOCIAL_ACCOUNT_UNBIND = "4010";


	public static final String ALREADY_BOOK = "8001";



	public static final String PARAM_NULL = "1001"; //缺少参数
	public static final String PARAM_ERROR = "1000";//参数错误
	public static final String LIVE_NO_EXIST_OR_OVER = "8010"; //直播不存在或者已经结束
	public static final String ROOM_NO_EXIST = "8012";//房间不存在
	public static final String IN_ROOM = "8013"; //已经在房间
	public static final String JOIN_ROOM_ERROR = "8014";  //加入房间失败

	public static final String NO_ENOUGHT_STARBEAN = "8005";  //星豆不足，请充值


	public static final String NO_COLLECT_LIVE = "5002";  //星豆不足，请充值

	public static final String TOKEND_DESTROY = "1017";  // TOKEN 失效

}
