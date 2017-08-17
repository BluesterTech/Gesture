package cn.bluester.recordtask.util;

/**
 * Created by JesseHu on 2016/9/8.
 */
public class MyConstants {
    //    public static final String DOMAIN = "http://114.215.101.112:3000/";
    public static final String DOMAIN = "http://139.196.48.189:3000/";
    //    public static final String DOMAIN = "http://192.168.2.104:3000";
    public final static String EXTRA_VENDOR_KEY = "74b8b33f7fdf4684a0d94018a3e7e657";
    //    public final static String EXTRA_VENDOR_KEY = "0f8583924e2448e6b2d4807145ec9c9e";
    public final static int CALLING_TYPE_VIDEO = 0x100;
    public final static int CALLING_TYPE_VOICE = 0x101;
    public final static String EXTRA_CALLING_TYPE = "EXTRA_CALLING_TYPE";
    public final static String EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID";
    public final static String CHANNEL_ID = "3";

    //api host
    public static final String APIHOST = "http://139.196.48.189:8080/";
    //登录接口
    public static final String LOGINURL = APIHOST + "api/user/blueAuthentication";
    //    public static final String LOGINURL = "http://192.168.2.104:8080/api/user/blueAuthentication";
    //sharedpreference文件名
    public static final String CONFIG_SP = "config";
    //用户token
    public static final String TOKEN = "token";
    //用户名
    public static final String USERNAME = "username";
    //用户id，视频会话房间号
    public static final String UID = "uid";
    //密码
    public static final String PASSWORD = "password";
    //MAC地址
    public static final String MACADRESS = "macadress";
    //登录过期时间
    public static final String LOGINTIME = "loginTime";
}
