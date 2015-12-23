package com.allen.oauth.utils;

public class Config {
	public static String APPID = "XXXXXXXXXXXX";
	// 受理商ID，身份标识
	public static String MCHID = "XXXXXXXXXXXXxx";
	// 商户支付密钥Key。审核通过后，在微信发送的邮件中查看
	public static String KEY = "XXXXXXXXXXXXXXXXX";
	// JSAPI接口中获取openid，审核后在公众平台开启开发模式后可查看
	public static String APPSECRET = "xxxxxxxxxxxxxx";
	// 重定向地址
	public static String REDIRECT_URL = "http://XXXXXXXXXXXXXXXXXXX/callWeiXinPay";
	// 异步回调地址
	public static String NOTIFY_URL = "http://XXXXXXXXXXXXXXXXXXXXXX/weixinPay_notify";
	// web回调地址
	public static String WEB_NOTIFY_URL = "http://XXXXXXXXXXXXXXXXXXXXXXXXX/weixinPay_notify";
}
