package com.persist.model;

import com.persist.info.UserInfo;

public class Model {

	public static String HTTPURL = "http://10.31.164.49/qiubai/";
//	public static String HTTPURL = "http://534429149.haoqie.net/qiubai/";
	public static String GANHUO = "ganhuo.php?";
	public static String NENCAO = "nencao.php?";
	public static String WENZI = "wenzi.php?";
	public static String AUDIT = "audit.php?";
	public static String RI = "ri.php?";
	public static String ZHOU = "zhou.php?";
	public static String YUE = "yue.php?";
	public static String YINGCAI = "yingcai.php?";
	public static String SHILING = "shiling.php?";
	public static String CHUANYUE = "chuanyue.php?";
	public static String COMMENTS = "comments.php?";
	public static String NEAR = "near.php?";
	public static String GETUSER = "getuser.php?";
	public static String UASHAMED = "uashamed.php?";
	public static String ADDVALUE = "addvalue.php";
	public static String REGISTET = "adduser.php";
	public static String ADDCOMMENT = "addcomment.php";
	public static String LOGIN = "login.php";
	public static String USERHEADURL = "http://10.31.164.49/qiubai/Userimg/";
	public static String QIMGURL = "http://10.31.164.49/qiubai/Valueimg/";
	public static boolean IMGFLAG = false;
	public static UserInfo MYUSERINFO = null;
	// APP客服KEY
	public static String APPKEY = "f241ebf4d4a1e1dfae6f1a3e49aad2f5";
	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
	public static final String APP_KEY = "3987368837";

	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
}
