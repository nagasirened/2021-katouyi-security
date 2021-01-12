package com.katouyi.securitytest.myauth;

import org.springframework.context.annotation.Configuration;
// import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * author: ZGF
 * context : 认证服务器
 */

@Configuration
// @EnableAuthorizationServer
public class KatouyiOwnAuthencationServerConfig {

    /**
     * 1. 授权码模式
     *    需要提供两个提供给三方调用的接口：一个是获取授权码的接口，另一个是获取token的接口
     *    default: /oauth/authorize  和  /auth/token
     */
}
