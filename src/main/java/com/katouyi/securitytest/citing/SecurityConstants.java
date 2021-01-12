package com.katouyi.securitytest.citing;

public class SecurityConstants {
    /**
     * security 验证地址
     */
    public static final String DEFAULT_AUTHENTICATION_REQUIRE = "/authentication/require";
    /**
     * security 用户名密码表单登录地址
     */
    public static final String DEFAULT_AUTHENTICATION_FORM = "/authentication/form";
    /**
     * session失效跳转地址
     */
    public static final String DEFAULT_SESSION_INVALID = "/session/invalid";
    public static final String DEFAULT_SESSION_INVALID_URL = "/session-incalid.html";
    /**
     * 用户注册表单提交地址
     */
    public static final String DEFAULT_USER_REGIST = "/user/regist";
    /**
     * 注册时获取social用户信息
     */
    public static final String SOCIAL_USER_INFO = "/social/userInfo";
    /**
     * 图标
     */
    public static final String FAVICON_ICO = "/favicon.ico";
    /**
     * 短信登录入口
     */
    public static final String SMS_LOGIN_URI = "/authentication/mobile";
}
